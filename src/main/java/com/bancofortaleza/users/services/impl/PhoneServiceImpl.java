package com.bancofortaleza.users.services.impl;

import com.bancofortaleza.users.configuration.SupportHeadersProvider;
import com.bancofortaleza.users.services.PhoneService;
import com.bancofortaleza.users.services.mapper.OpenApiModelMapper;
import com.bff.services.client.SupportApiClient;
import com.bff.services.server.models.PhoneCreateRequest;
import com.bff.services.server.models.PhoneResponse;
import com.bff.services.server.models.Status;
import com.bff.services.server.models.StatusUpdateRequest;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PhoneServiceImpl implements PhoneService {

    private final SupportApiClient supportApiClient;
    private final OpenApiModelMapper mapper;
    private final SupportHeadersProvider supportHeadersProvider;

    @Override
    public ResponseEntity<List<PhoneResponse>> listUserPhones(
        String xDeviceIp,
        String xSession,
        Integer id,
        Integer xPage,
        Integer xPageSize,
        String search,
        Status status
    ) {
        ResponseEntity<List<com.bff.services.client.models.PhoneResponse>> response = supportApiClient.listUserPhones(
            xDeviceIp,
            xSession,
            supportHeadersProvider.getAuthenticatedUserId(),
            id,
            xPage,
            xPageSize,
            search,
            mapper.toClientStatus(status)
        );
        return mapper.mapPhoneListResponse(response);
    }

    @Override
    public ResponseEntity<PhoneResponse> createUserPhone(
        String xDeviceIp,
        String xSession,
        Integer id,
        PhoneCreateRequest phoneCreateRequest
    ) {
        ResponseEntity<com.bff.services.client.models.PhoneResponse> response = supportApiClient.createUserPhone(
            xDeviceIp,
            xSession,
            supportHeadersProvider.getAuthenticatedUserId(),
            id,
            mapper.toClientPhoneCreateRequest(phoneCreateRequest)
        );
        return mapper.mapPhoneResponse(response);
    }

    @Override
    public ResponseEntity<PhoneResponse> getUserPhoneById(String xDeviceIp, String xSession, Integer id, Integer phoneId) {
        ResponseEntity<com.bff.services.client.models.PhoneResponse> response = supportApiClient.getUserPhoneById(
            xDeviceIp,
            xSession,
            supportHeadersProvider.getAuthenticatedUserId(),
            id,
            phoneId
        );
        return mapper.mapPhoneResponse(response);
    }

    @Override
    public ResponseEntity<PhoneResponse> updateUserPhoneStatus(
        String xDeviceIp,
        String xSession,
        Integer id,
        Integer phoneId,
        StatusUpdateRequest statusUpdateRequest
    ) {
        ResponseEntity<com.bff.services.client.models.PhoneResponse> response = supportApiClient.updateUserPhoneStatus(
            xDeviceIp,
            xSession,
            supportHeadersProvider.getAuthenticatedUserId(),
            id,
            phoneId,
            mapper.toClientStatusUpdateRequest(statusUpdateRequest)
        );
        return mapper.mapPhoneResponse(response);
    }
}
