package com.bancofortaleza.users.services.impl;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.bancofortaleza.users.configuration.SupportHeadersProvider;
import com.bancofortaleza.users.services.mapper.OpenApiModelMapper;
import com.bff.services.client.SupportApiClient;
import com.bff.services.server.models.Status;
import com.bff.services.server.models.StatusUpdateRequest;
import com.bff.services.server.models.UserCreateRequest;
import com.bff.services.server.models.UserResponse;
import com.bff.services.server.models.UserType;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;

@ExtendWith(MockitoExtension.class)
class UsersServiceImplTest {

    private static final String DEVICE_IP = "10.0.0.1";
    private static final String SESSION = "session-1";
    private static final Integer AUTHENTICATED_USER_ID = 99;

    @Mock
    private SupportApiClient supportApiClient;

    @Mock
    private OpenApiModelMapper mapper;

    @Mock
    private SupportHeadersProvider supportHeadersProvider;

    @InjectMocks
    private UsersServiceImpl service;

    @Test
    void listUsers_delegatesToSupportClientAndMapsResponse() {
        Status status = Status.ACTIVE;
        UserType userType = UserType.ADMIN;
        com.bff.services.client.models.Status clientStatus = com.bff.services.client.models.Status.ACTIVE;
        com.bff.services.client.models.UserType clientUserType = com.bff.services.client.models.UserType.ADMIN;
        ResponseEntity<List<com.bff.services.client.models.UserResponse>> clientResponse = ResponseEntity.ok(List.of());
        ResponseEntity<List<UserResponse>> expectedResponse = ResponseEntity.ok(List.of(new UserResponse()));
        when(supportHeadersProvider.getAuthenticatedUserId()).thenReturn(AUTHENTICATED_USER_ID);
        when(mapper.toClientStatus(status)).thenReturn(clientStatus);
        when(mapper.toClientUserType(userType)).thenReturn(clientUserType);
        when(supportApiClient.listUsers(DEVICE_IP, SESSION, AUTHENTICATED_USER_ID, 1, 20, "ana", clientStatus, clientUserType))
            .thenReturn(clientResponse);
        when(mapper.mapUserListResponse(clientResponse)).thenReturn(expectedResponse);

        ResponseEntity<List<UserResponse>> result = service.listUsers(DEVICE_IP, SESSION, 1, 20, "ana", status, userType);

        assertThat(result).isSameAs(expectedResponse);
        verify(supportApiClient).listUsers(DEVICE_IP, SESSION, AUTHENTICATED_USER_ID, 1, 20, "ana", clientStatus, clientUserType);
    }

    @Test
    void createUser_delegatesToSupportClientAndMapsResponse() {
        UserCreateRequest request = new UserCreateRequest();
        com.bff.services.client.models.UserCreateRequest clientRequest = new com.bff.services.client.models.UserCreateRequest();
        ResponseEntity<com.bff.services.client.models.UserResponse> clientResponse = ResponseEntity.ok(new com.bff.services.client.models.UserResponse());
        ResponseEntity<UserResponse> expectedResponse = ResponseEntity.ok(new UserResponse());
        when(supportHeadersProvider.getAuthenticatedUserId()).thenReturn(AUTHENTICATED_USER_ID);
        when(mapper.toClientUserCreateRequest(request)).thenReturn(clientRequest);
        when(supportApiClient.createUser(DEVICE_IP, SESSION, AUTHENTICATED_USER_ID, clientRequest)).thenReturn(clientResponse);
        when(mapper.mapUserResponse(clientResponse)).thenReturn(expectedResponse);

        ResponseEntity<UserResponse> result = service.createUser(DEVICE_IP, SESSION, request);

        assertThat(result).isSameAs(expectedResponse);
        verify(supportApiClient).createUser(DEVICE_IP, SESSION, AUTHENTICATED_USER_ID, clientRequest);
    }

    @Test
    void getUserById_delegatesToSupportClientAndMapsResponse() {
        ResponseEntity<com.bff.services.client.models.UserResponse> clientResponse = ResponseEntity.ok(new com.bff.services.client.models.UserResponse());
        ResponseEntity<UserResponse> expectedResponse = ResponseEntity.ok(new UserResponse());
        when(supportHeadersProvider.getAuthenticatedUserId()).thenReturn(AUTHENTICATED_USER_ID);
        when(supportApiClient.getUserById(DEVICE_IP, SESSION, AUTHENTICATED_USER_ID, 7)).thenReturn(clientResponse);
        when(mapper.mapUserResponse(clientResponse)).thenReturn(expectedResponse);

        ResponseEntity<UserResponse> result = service.getUserById(DEVICE_IP, SESSION, 7);

        assertThat(result).isSameAs(expectedResponse);
        verify(supportApiClient).getUserById(DEVICE_IP, SESSION, AUTHENTICATED_USER_ID, 7);
    }

    @Test
    void updateUserStatus_delegatesToSupportClientAndMapsResponse() {
        StatusUpdateRequest request = new StatusUpdateRequest();
        com.bff.services.client.models.StatusUpdateRequest clientRequest = new com.bff.services.client.models.StatusUpdateRequest();
        ResponseEntity<com.bff.services.client.models.UserResponse> clientResponse = ResponseEntity.ok(new com.bff.services.client.models.UserResponse());
        ResponseEntity<UserResponse> expectedResponse = ResponseEntity.ok(new UserResponse());
        when(supportHeadersProvider.getAuthenticatedUserId()).thenReturn(AUTHENTICATED_USER_ID);
        when(mapper.toClientStatusUpdateRequest(request)).thenReturn(clientRequest);
        when(supportApiClient.updateUserStatus(DEVICE_IP, SESSION, AUTHENTICATED_USER_ID, 7, clientRequest)).thenReturn(clientResponse);
        when(mapper.mapUserResponse(clientResponse)).thenReturn(expectedResponse);

        ResponseEntity<UserResponse> result = service.updateUserStatus(DEVICE_IP, SESSION, 7, request);

        assertThat(result).isSameAs(expectedResponse);
        verify(supportApiClient).updateUserStatus(DEVICE_IP, SESSION, AUTHENTICATED_USER_ID, 7, clientRequest);
    }
}
