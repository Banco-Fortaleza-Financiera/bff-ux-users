package com.bancofortaleza.users.services.impl;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

import com.bancofortaleza.users.domain.exceptions.ApiException;
import com.bff.services.auth.AuthApiClient;
import com.bff.services.auth.models.TokenValidationRequest;
import com.bff.services.auth.models.TokenValidationResponse;
import feign.FeignException;
import feign.Request;
import feign.Response;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

@ExtendWith(MockitoExtension.class)
class TokenValidationServiceImplTest {

    private static final String DEVICE_IP = "192.168.0.10";
    private static final String SESSION = "session-1";

    @Mock
    private AuthApiClient authApiClient;

    @InjectMocks
    private TokenValidationServiceImpl service;

    @Test
    void validate_whenTokenIsValid_returnsAuthenticatedUserId() {
        when(authApiClient.validateToken(eq(DEVICE_IP), eq(SESSION), any()))
            .thenReturn(ResponseEntity.ok(new TokenValidationResponse().valid(true).idUser(12)));

        Integer result = service.validate("Bearer access-token", DEVICE_IP, SESSION);

        ArgumentCaptor<TokenValidationRequest> requestCaptor = ArgumentCaptor.forClass(TokenValidationRequest.class);
        verify(authApiClient).validateToken(eq(DEVICE_IP), eq(SESSION), requestCaptor.capture());
        assertThat(result).isEqualTo(12);
        assertThat(requestCaptor.getValue().getAccessToken()).isEqualTo("access-token");
        assertThat(requestCaptor.getValue().getTokenType()).isEqualTo("Bearer");
    }

    @Test
    void validate_whenBearerHasExtraSpaces_stillUsesAccessToken() {
        when(authApiClient.validateToken(eq(DEVICE_IP), eq(SESSION), any()))
            .thenReturn(ResponseEntity.ok(new TokenValidationResponse().valid(true).idUser(20)));

        Integer result = service.validate("  bearer   token-value  ", DEVICE_IP, SESSION);

        ArgumentCaptor<TokenValidationRequest> requestCaptor = ArgumentCaptor.forClass(TokenValidationRequest.class);
        verify(authApiClient).validateToken(eq(DEVICE_IP), eq(SESSION), requestCaptor.capture());
        assertThat(result).isEqualTo(20);
        assertThat(requestCaptor.getValue().getAccessToken()).isEqualTo("token-value");
    }

    @Test
    void validate_whenAuthorizationHeaderIsMissing_throwsUnauthorized() {
        assertThatThrownBy(() -> service.validate(null, DEVICE_IP, SESSION))
            .isInstanceOfSatisfying(ApiException.class, exception -> {
                assertThat(exception.getStatus()).isEqualTo(HttpStatus.UNAUTHORIZED);
                assertThat(exception.getCode()).isEqualTo("UNAUTHORIZED");
            });

        verifyNoInteractions(authApiClient);
    }

    @Test
    void validate_whenAuthorizationHeaderIsBlank_throwsUnauthorized() {
        assertThatThrownBy(() -> service.validate(" ", DEVICE_IP, SESSION))
            .isInstanceOfSatisfying(ApiException.class, exception ->
                assertThat(exception.getStatus()).isEqualTo(HttpStatus.UNAUTHORIZED)
            );

        verifyNoInteractions(authApiClient);
    }

    @Test
    void validate_whenAuthorizationHeaderHasInvalidFormat_throwsUnauthorized() {
        assertThatThrownBy(() -> service.validate("Bearer", DEVICE_IP, SESSION))
            .isInstanceOfSatisfying(ApiException.class, exception ->
                assertThat(exception.getStatus()).isEqualTo(HttpStatus.UNAUTHORIZED)
            );

        verifyNoInteractions(authApiClient);
    }

    @Test
    void validate_whenAuthorizationHeaderHasInvalidType_throwsUnauthorized() {
        assertThatThrownBy(() -> service.validate("Basic token", DEVICE_IP, SESSION))
            .isInstanceOfSatisfying(ApiException.class, exception ->
                assertThat(exception.getStatus()).isEqualTo(HttpStatus.UNAUTHORIZED)
            );

        verifyNoInteractions(authApiClient);
    }

    @Test
    void validate_whenAuthResponseHasNullBody_throwsUnauthorized() {
        when(authApiClient.validateToken(any(), any(), any())).thenReturn(ResponseEntity.ok(null));

        assertThatThrownBy(() -> service.validate("Bearer token", DEVICE_IP, SESSION))
            .isInstanceOfSatisfying(ApiException.class, exception ->
                assertThat(exception.getStatus()).isEqualTo(HttpStatus.UNAUTHORIZED)
            );
    }

    @Test
    void validate_whenTokenIsInvalid_throwsUnauthorized() {
        when(authApiClient.validateToken(any(), any(), any()))
            .thenReturn(ResponseEntity.ok(new TokenValidationResponse().valid(false).idUser(12)));

        assertThatThrownBy(() -> service.validate("Bearer token", DEVICE_IP, SESSION))
            .isInstanceOfSatisfying(ApiException.class, exception ->
                assertThat(exception.getStatus()).isEqualTo(HttpStatus.UNAUTHORIZED)
            );
    }

    @Test
    void validate_whenUserIdIsMissing_throwsUnauthorized() {
        when(authApiClient.validateToken(any(), any(), any()))
            .thenReturn(ResponseEntity.ok(new TokenValidationResponse().valid(true)));

        assertThatThrownBy(() -> service.validate("Bearer token", DEVICE_IP, SESSION))
            .isInstanceOfSatisfying(ApiException.class, exception ->
                assertThat(exception.getStatus()).isEqualTo(HttpStatus.UNAUTHORIZED)
            );
    }

    @Test
    void validate_whenAuthServiceRejectsToken_throwsUnauthorized() {
        when(authApiClient.validateToken(any(), any(), any())).thenThrow(feignException(401));

        assertThatThrownBy(() -> service.validate("Bearer token", DEVICE_IP, SESSION))
            .isInstanceOfSatisfying(ApiException.class, exception -> {
                assertThat(exception.getStatus()).isEqualTo(HttpStatus.UNAUTHORIZED);
                assertThat(exception.getMessage()).isEqualTo("No tiene autorizacion");
            });
    }

    @Test
    void validate_whenAuthServiceFails_throwsServiceUnavailable() {
        when(authApiClient.validateToken(any(), any(), any())).thenThrow(feignException(503));

        assertThatThrownBy(() -> service.validate("Bearer token", DEVICE_IP, SESSION))
            .isInstanceOfSatisfying(ApiException.class, exception -> {
                assertThat(exception.getStatus()).isEqualTo(HttpStatus.SERVICE_UNAVAILABLE);
                assertThat(exception.getCode()).isEqualTo("TOKEN_VALIDATION_UNAVAILABLE");
            });
    }

    private FeignException feignException(int status) {
        Request request = Request.create(
            Request.HttpMethod.POST,
            "/auth/validate",
            Map.of(),
            null,
            StandardCharsets.UTF_8,
            null
        );
        Response response = Response.builder()
            .status(status)
            .reason("status")
            .request(request)
            .headers(Map.of())
            .body("error", StandardCharsets.UTF_8)
            .build();
        return FeignException.errorStatus("validateToken", response);
    }
}
