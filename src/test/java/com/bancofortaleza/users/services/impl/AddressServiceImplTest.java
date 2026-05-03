package com.bancofortaleza.users.services.impl;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.bancofortaleza.users.configuration.SupportHeadersProvider;
import com.bancofortaleza.users.services.mapper.OpenApiModelMapper;
import com.bff.services.client.SupportApiClient;
import com.bff.services.server.models.AddressCreateRequest;
import com.bff.services.server.models.AddressResponse;
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
class AddressServiceImplTest {

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
    private AddressServiceImpl service;

    @Test
    void listUserAddresses_delegatesToSupportClientAndMapsResponse() {
        Status status = Status.ACTIVE;
        com.bff.services.client.models.Status clientStatus = com.bff.services.client.models.Status.ACTIVE;
        ResponseEntity<List<com.bff.services.client.models.AddressResponse>> clientResponse = ResponseEntity.ok(List.of());
        ResponseEntity<List<AddressResponse>> expectedResponse = ResponseEntity.ok(List.of(new AddressResponse()));
        when(supportHeadersProvider.getAuthenticatedUserId()).thenReturn(AUTHENTICATED_USER_ID);
        when(mapper.map(status, com.bff.services.client.models.Status.class)).thenReturn(clientStatus);
        when(supportApiClient.listUserAddresses(DEVICE_IP, SESSION, AUTHENTICATED_USER_ID, 7, 1, 20, "zona", clientStatus))
            .thenReturn(clientResponse);
        when(mapper.mapListResponse(clientResponse, AddressResponse.class)).thenReturn(expectedResponse);

        ResponseEntity<List<AddressResponse>> result = service.listUserAddresses(DEVICE_IP, SESSION, 7, 1, 20, "zona", status);

        assertThat(result).isSameAs(expectedResponse);
        verify(supportApiClient).listUserAddresses(DEVICE_IP, SESSION, AUTHENTICATED_USER_ID, 7, 1, 20, "zona", clientStatus);
    }

    @Test
    void createUserAddress_delegatesToSupportClientAndMapsResponse() {
        AddressCreateRequest request = new AddressCreateRequest();
        com.bff.services.client.models.AddressCreateRequest clientRequest = new com.bff.services.client.models.AddressCreateRequest();
        ResponseEntity<com.bff.services.client.models.AddressResponse> clientResponse = ResponseEntity.ok(new com.bff.services.client.models.AddressResponse());
        ResponseEntity<AddressResponse> expectedResponse = ResponseEntity.ok(new AddressResponse());
        when(supportHeadersProvider.getAuthenticatedUserId()).thenReturn(AUTHENTICATED_USER_ID);
        when(mapper.map(request, com.bff.services.client.models.AddressCreateRequest.class)).thenReturn(clientRequest);
        when(supportApiClient.createUserAddress(DEVICE_IP, SESSION, AUTHENTICATED_USER_ID, 7, clientRequest)).thenReturn(clientResponse);
        when(mapper.mapResponse(clientResponse, AddressResponse.class)).thenReturn(expectedResponse);

        ResponseEntity<AddressResponse> result = service.createUserAddress(DEVICE_IP, SESSION, 7, request);

        assertThat(result).isSameAs(expectedResponse);
        verify(supportApiClient).createUserAddress(DEVICE_IP, SESSION, AUTHENTICATED_USER_ID, 7, clientRequest);
    }

    @Test
    void getUserAddressById_delegatesToSupportClientAndMapsResponse() {
        ResponseEntity<com.bff.services.client.models.AddressResponse> clientResponse = ResponseEntity.ok(new com.bff.services.client.models.AddressResponse());
        ResponseEntity<AddressResponse> expectedResponse = ResponseEntity.ok(new AddressResponse());
        when(supportHeadersProvider.getAuthenticatedUserId()).thenReturn(AUTHENTICATED_USER_ID);
        when(supportApiClient.getUserAddressById(DEVICE_IP, SESSION, AUTHENTICATED_USER_ID, 7, 3)).thenReturn(clientResponse);
        when(mapper.mapResponse(clientResponse, AddressResponse.class)).thenReturn(expectedResponse);

        ResponseEntity<AddressResponse> result = service.getUserAddressById(DEVICE_IP, SESSION, 7, 3);

        assertThat(result).isSameAs(expectedResponse);
        verify(supportApiClient).getUserAddressById(DEVICE_IP, SESSION, AUTHENTICATED_USER_ID, 7, 3);
    }

    @Test
    void updateUserAddressStatus_delegatesToSupportClientAndMapsResponse() {
        StatusUpdateRequest request = new StatusUpdateRequest();
        com.bff.services.client.models.StatusUpdateRequest clientRequest = new com.bff.services.client.models.StatusUpdateRequest();
        ResponseEntity<com.bff.services.client.models.AddressResponse> clientResponse = ResponseEntity.ok(new com.bff.services.client.models.AddressResponse());
        ResponseEntity<AddressResponse> expectedResponse = ResponseEntity.ok(new AddressResponse());
        when(supportHeadersProvider.getAuthenticatedUserId()).thenReturn(AUTHENTICATED_USER_ID);
        when(mapper.map(request, com.bff.services.client.models.StatusUpdateRequest.class)).thenReturn(clientRequest);
        when(supportApiClient.updateUserAddressStatus(DEVICE_IP, SESSION, AUTHENTICATED_USER_ID, 7, 3, clientRequest)).thenReturn(clientResponse);
        when(mapper.mapResponse(clientResponse, AddressResponse.class)).thenReturn(expectedResponse);

        ResponseEntity<AddressResponse> result = service.updateUserAddressStatus(DEVICE_IP, SESSION, 7, 3, request);

        assertThat(result).isSameAs(expectedResponse);
        verify(supportApiClient).updateUserAddressStatus(DEVICE_IP, SESSION, AUTHENTICATED_USER_ID, 7, 3, clientRequest);
    }
}
