package com.bancofortaleza.users.services.impl;

import com.bancofortaleza.users.domain.exceptions.ApiException;
import com.bancofortaleza.users.services.TokenValidationService;
import com.bancofortaleza.users.utils.AppLogger;
import com.bff.services.auth.AuthApiClient;
import com.bff.services.auth.models.TokenValidationRequest;
import com.bff.services.auth.models.TokenValidationResponse;
import feign.FeignException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TokenValidationServiceImpl implements TokenValidationService {

    private static final String BEARER_TOKEN_TYPE = "Bearer";
    private static final String UNAUTHORIZED_CODE = "UNAUTHORIZED";
    private static final String UNAUTHORIZED_MESSAGE = "No tiene autorización";

    private final AuthApiClient authApiClient;

    @Override
    public void validate(String authorizationHeader, String xDeviceIp, String xSession) {
        AuthorizationToken authorizationToken = resolveAuthorizationToken(authorizationHeader);

        try {
            TokenValidationRequest request = new TokenValidationRequest()
                .accessToken(authorizationToken.accessToken())
                .tokenType(authorizationToken.tokenType());

            ResponseEntity<TokenValidationResponse> response = authApiClient.validateToken(xDeviceIp, xSession, request);
            TokenValidationResponse body = response.getBody();

            if (body == null || !Boolean.TRUE.equals(body.getValid())) {
                throw unauthorized();
            }
        } catch (FeignException.Unauthorized exception) {
            AppLogger.warn(TokenValidationServiceImpl.class, "Token validation rejected by auth service");
            throw unauthorized();
        }
    }

    private AuthorizationToken resolveAuthorizationToken(String authorizationHeader) {
        if (authorizationHeader == null || authorizationHeader.isBlank()) {
            throw unauthorized();
        }

        String[] parts = authorizationHeader.trim().split("\\s+", 2);
        if (parts.length != 2 || !BEARER_TOKEN_TYPE.equalsIgnoreCase(parts[0]) || parts[1].isBlank()) {
            throw unauthorized();
        }

        return new AuthorizationToken(BEARER_TOKEN_TYPE, parts[1]);
    }

    private ApiException unauthorized() {
        return new ApiException(HttpStatus.UNAUTHORIZED, UNAUTHORIZED_CODE, UNAUTHORIZED_MESSAGE);
    }

    private record AuthorizationToken(String tokenType, String accessToken) {
    }
}
