package com.bancofortaleza.users.services;

import com.bff.services.server.models.AddressCreateRequest;
import com.bff.services.server.models.AddressResponse;
import com.bff.services.server.models.Status;
import com.bff.services.server.models.StatusUpdateRequest;
import java.util.List;
import org.springframework.http.ResponseEntity;

public interface AddressService {

    ResponseEntity<List<AddressResponse>> listUserAddresses(
        String xDeviceIp,
        String xSession,
        Integer id,
        Integer xPage,
        Integer xPageSize,
        String search,
        Status status
    );

    ResponseEntity<AddressResponse> createUserAddress(
        String xDeviceIp,
        String xSession,
        Integer id,
        AddressCreateRequest addressCreateRequest
    );

    ResponseEntity<AddressResponse> getUserAddressById(String xDeviceIp, String xSession, Integer id, Integer addressId);

    ResponseEntity<AddressResponse> updateUserAddressStatus(
        String xDeviceIp,
        String xSession,
        Integer id,
        Integer addressId,
        StatusUpdateRequest statusUpdateRequest
    );
}
