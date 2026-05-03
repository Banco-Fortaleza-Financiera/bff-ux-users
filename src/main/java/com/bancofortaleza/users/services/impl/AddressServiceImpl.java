package com.bancofortaleza.users.services.impl;

import com.bancofortaleza.users.configuration.SupportHeadersProvider;
import com.bancofortaleza.users.services.AddressService;
import com.bancofortaleza.users.services.mapper.OpenApiModelMapper;
import com.bff.services.client.SupportApiClient;
import com.bff.services.server.models.AddressCreateRequest;
import com.bff.services.server.models.AddressResponse;
import com.bff.services.server.models.Status;
import com.bff.services.server.models.StatusUpdateRequest;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AddressServiceImpl implements AddressService {

    private final SupportApiClient supportApiClient;
    private final OpenApiModelMapper mapper;
    private final SupportHeadersProvider supportHeadersProvider;

    @Override
    public ResponseEntity<List<AddressResponse>> listUserAddresses(
        String xDeviceIp,
        String xSession,
        Integer id,
        Integer xPage,
        Integer xPageSize,
        String search,
        Status status
    ) {
        ResponseEntity<List<com.bff.services.client.models.AddressResponse>> response = supportApiClient.listUserAddresses(
            xDeviceIp,
            xSession,
            supportHeadersProvider.getAuthenticatedUserId(),
            id,
            xPage,
            xPageSize,
            search,
            mapper.map(status, com.bff.services.client.models.Status.class)
        );
        return mapper.mapListResponse(response, AddressResponse.class);
    }

    @Override
    public ResponseEntity<AddressResponse> createUserAddress(
        String xDeviceIp,
        String xSession,
        Integer id,
        AddressCreateRequest addressCreateRequest
    ) {
        ResponseEntity<com.bff.services.client.models.AddressResponse> response = supportApiClient.createUserAddress(
            xDeviceIp,
            xSession,
            supportHeadersProvider.getAuthenticatedUserId(),
            id,
            mapper.map(addressCreateRequest, com.bff.services.client.models.AddressCreateRequest.class)
        );
        return mapper.mapResponse(response, AddressResponse.class);
    }

    @Override
    public ResponseEntity<AddressResponse> getUserAddressById(String xDeviceIp, String xSession, Integer id, Integer addressId) {
        ResponseEntity<com.bff.services.client.models.AddressResponse> response = supportApiClient.getUserAddressById(
            xDeviceIp,
            xSession,
            supportHeadersProvider.getAuthenticatedUserId(),
            id,
            addressId
        );
        return mapper.mapResponse(response, AddressResponse.class);
    }

    @Override
    public ResponseEntity<AddressResponse> updateUserAddressStatus(
        String xDeviceIp,
        String xSession,
        Integer id,
        Integer addressId,
        StatusUpdateRequest statusUpdateRequest
    ) {
        ResponseEntity<com.bff.services.client.models.AddressResponse> response = supportApiClient.updateUserAddressStatus(
            xDeviceIp,
            xSession,
            supportHeadersProvider.getAuthenticatedUserId(),
            id,
            addressId,
            mapper.map(statusUpdateRequest, com.bff.services.client.models.StatusUpdateRequest.class)
        );
        return mapper.mapResponse(response, AddressResponse.class);
    }
}
