package com.bancofortaleza.users.services.impl;

import com.bancofortaleza.users.configuration.SupportHeadersProvider;
import com.bancofortaleza.users.services.UsersService;
import com.bancofortaleza.users.services.mapper.OpenApiModelMapper;
import com.bff.services.client.SupportApiClient;
import com.bff.services.server.models.Status;
import com.bff.services.server.models.StatusUpdateRequest;
import com.bff.services.server.models.UserCreateRequest;
import com.bff.services.server.models.UserResponse;
import com.bff.services.server.models.UserType;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UsersServiceImpl implements UsersService {

    private final SupportApiClient supportApiClient;
    private final OpenApiModelMapper mapper;
    private final SupportHeadersProvider supportHeadersProvider;

    @Override
    public ResponseEntity<List<UserResponse>> listUsers(
        String xDeviceIp,
        String xSession,
        Integer xPage,
        Integer xPageSize,
        String search,
        Status status,
        UserType userType
    ) {
        ResponseEntity<List<com.bff.services.client.models.UserResponse>> response = supportApiClient.listUsers(
            xDeviceIp,
            xSession,
            supportHeadersProvider.getAuthenticatedUserId(),
            xPage,
            xPageSize,
            search,
            mapper.toClientStatus(status),
            mapper.toClientUserType(userType)
        );
        return mapper.mapUserListResponse(response);
    }

    @Override
    public ResponseEntity<UserResponse> createUser(String xDeviceIp, String xSession, UserCreateRequest userCreateRequest) {
        ResponseEntity<com.bff.services.client.models.UserResponse> response = supportApiClient.createUser(
            xDeviceIp,
            xSession,
            supportHeadersProvider.getAuthenticatedUserId(),
            mapper.toClientUserCreateRequest(userCreateRequest)
        );
        return mapper.mapUserResponse(response);
    }

    @Override
    public ResponseEntity<UserResponse> getUserById(String xDeviceIp, String xSession, Integer id) {
        ResponseEntity<com.bff.services.client.models.UserResponse> response = supportApiClient.getUserById(
            xDeviceIp,
            xSession,
            supportHeadersProvider.getAuthenticatedUserId(),
            id
        );
        return mapper.mapUserResponse(response);
    }

    @Override
    public ResponseEntity<UserResponse> updateUserStatus(
        String xDeviceIp,
        String xSession,
        Integer id,
        StatusUpdateRequest statusUpdateRequest
    ) {
        ResponseEntity<com.bff.services.client.models.UserResponse> response = supportApiClient.updateUserStatus(
            xDeviceIp,
            xSession,
            supportHeadersProvider.getAuthenticatedUserId(),
            id,
            mapper.toClientStatusUpdateRequest(statusUpdateRequest)
        );
        return mapper.mapUserResponse(response);
    }
}
