package com.bancofortaleza.users.services;

import com.bff.services.server.models.PhoneCreateRequest;
import com.bff.services.server.models.PhoneResponse;
import com.bff.services.server.models.Status;
import com.bff.services.server.models.StatusUpdateRequest;
import java.util.List;
import org.springframework.http.ResponseEntity;

public interface PhoneService {

    ResponseEntity<List<PhoneResponse>> listUserPhones(
        String xDeviceIp,
        String xSession,
        Integer id,
        Integer xPage,
        Integer xPageSize,
        String search,
        Status status
    );

    ResponseEntity<PhoneResponse> createUserPhone(
        String xDeviceIp,
        String xSession,
        Integer id,
        PhoneCreateRequest phoneCreateRequest
    );

    ResponseEntity<PhoneResponse> getUserPhoneById(String xDeviceIp, String xSession, Integer id, Integer phoneId);

    ResponseEntity<PhoneResponse> updateUserPhoneStatus(
        String xDeviceIp,
        String xSession,
        Integer id,
        Integer phoneId,
        StatusUpdateRequest statusUpdateRequest
    );
}
