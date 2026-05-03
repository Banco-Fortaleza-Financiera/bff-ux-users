package com.bancofortaleza.users.services.impl;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.bancofortaleza.users.configuration.SupportHeadersProvider;
import com.bancofortaleza.users.services.mapper.OpenApiModelMapper;
import com.bff.services.client.SupportApiClient;
import com.bff.services.server.models.PhoneCreateRequest;
import com.bff.services.server.models.PhoneResponse;
import com.bff.services.server.models.Status;
import com.bff.services.server.models.StatusUpdateRequest;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;

@ExtendWith(MockitoExtension.class)
class PhoneServiceImplTest {

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
    private PhoneServiceImpl service;

    @Test
    void listUserPhones_delegatesToSupportClientAndMapsResponse() {
        Status status = Status.ACTIVE;
        com.bff.services.client.models.Status clientStatus = com.bff.services.client.models.Status.ACTIVE;
        ResponseEntity<List<com.bff.services.client.models.PhoneResponse>> clientResponse = ResponseEntity.ok(List.of());
        ResponseEntity<List<PhoneResponse>> expectedResponse = ResponseEntity.ok(List.of(new PhoneResponse()));
        when(supportHeadersProvider.getAuthenticatedUserId()).thenReturn(AUTHENTICATED_USER_ID);
        when(mapper.map(status, com.bff.services.client.models.Status.class)).thenReturn(clientStatus);
        when(supportApiClient.listUserPhones(DEVICE_IP, SESSION, AUTHENTICATED_USER_ID, 7, 1, 20, "555", clientStatus))
            .thenReturn(clientResponse);
        when(mapper.mapListResponse(clientResponse, PhoneResponse.class)).thenReturn(expectedResponse);

        ResponseEntity<List<PhoneResponse>> result = service.listUserPhones(DEVICE_IP, SESSION, 7, 1, 20, "555", status);

        assertThat(result).isSameAs(expectedResponse);
        verify(supportApiClient).listUserPhones(DEVICE_IP, SESSION, AUTHENTICATED_USER_ID, 7, 1, 20, "555", clientStatus);
    }

    @Test
    void createUserPhone_delegatesToSupportClientAndMapsResponse() {
        PhoneCreateRequest request = new PhoneCreateRequest();
        com.bff.services.client.models.PhoneCreateRequest clientRequest = new com.bff.services.client.models.PhoneCreateRequest();
        ResponseEntity<com.bff.services.client.models.PhoneResponse> clientResponse = ResponseEntity.ok(new com.bff.services.client.models.PhoneResponse());
        ResponseEntity<PhoneResponse> expectedResponse = ResponseEntity.ok(new PhoneResponse());
        when(supportHeadersProvider.getAuthenticatedUserId()).thenReturn(AUTHENTICATED_USER_ID);
        when(mapper.map(request, com.bff.services.client.models.PhoneCreateRequest.class)).thenReturn(clientRequest);
        when(supportApiClient.createUserPhone(DEVICE_IP, SESSION, AUTHENTICATED_USER_ID, 7, clientRequest)).thenReturn(clientResponse);
        when(mapper.mapResponse(clientResponse, PhoneResponse.class)).thenReturn(expectedResponse);

        ResponseEntity<PhoneResponse> result = service.createUserPhone(DEVICE_IP, SESSION, 7, request);

        assertThat(result).isSameAs(expectedResponse);
        verify(supportApiClient).createUserPhone(DEVICE_IP, SESSION, AUTHENTICATED_USER_ID, 7, clientRequest);
    }

    @Test
    void getUserPhoneById_delegatesToSupportClientAndMapsResponse() {
        ResponseEntity<com.bff.services.client.models.PhoneResponse> clientResponse = ResponseEntity.ok(new com.bff.services.client.models.PhoneResponse());
        ResponseEntity<PhoneResponse> expectedResponse = ResponseEntity.ok(new PhoneResponse());
        when(supportHeadersProvider.getAuthenticatedUserId()).thenReturn(AUTHENTICATED_USER_ID);
        when(supportApiClient.getUserPhoneById(DEVICE_IP, SESSION, AUTHENTICATED_USER_ID, 7, 3)).thenReturn(clientResponse);
        when(mapper.mapResponse(clientResponse, PhoneResponse.class)).thenReturn(expectedResponse);

        ResponseEntity<PhoneResponse> result = service.getUserPhoneById(DEVICE_IP, SESSION, 7, 3);

        assertThat(result).isSameAs(expectedResponse);
        verify(supportApiClient).getUserPhoneById(DEVICE_IP, SESSION, AUTHENTICATED_USER_ID, 7, 3);
    }

    @Test
    void updateUserPhoneStatus_delegatesToSupportClientAndMapsResponse() {
        StatusUpdateRequest request = new StatusUpdateRequest();
        com.bff.services.client.models.StatusUpdateRequest clientRequest = new com.bff.services.client.models.StatusUpdateRequest();
        ResponseEntity<com.bff.services.client.models.PhoneResponse> clientResponse = ResponseEntity.ok(new com.bff.services.client.models.PhoneResponse());
        ResponseEntity<PhoneResponse> expectedResponse = ResponseEntity.ok(new PhoneResponse());
        when(supportHeadersProvider.getAuthenticatedUserId()).thenReturn(AUTHENTICATED_USER_ID);
        when(mapper.map(request, com.bff.services.client.models.StatusUpdateRequest.class)).thenReturn(clientRequest);
        when(supportApiClient.updateUserPhoneStatus(DEVICE_IP, SESSION, AUTHENTICATED_USER_ID, 7, 3, clientRequest)).thenReturn(clientResponse);
        when(mapper.mapResponse(clientResponse, PhoneResponse.class)).thenReturn(expectedResponse);

        ResponseEntity<PhoneResponse> result = service.updateUserPhoneStatus(DEVICE_IP, SESSION, 7, 3, request);

        assertThat(result).isSameAs(expectedResponse);
        verify(supportApiClient).updateUserPhoneStatus(DEVICE_IP, SESSION, AUTHENTICATED_USER_ID, 7, 3, clientRequest);
    }
}
