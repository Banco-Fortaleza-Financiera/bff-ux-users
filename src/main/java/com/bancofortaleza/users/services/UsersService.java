package com.bancofortaleza.users.services;

import com.bff.services.server.models.Status;
import com.bff.services.server.models.StatusUpdateRequest;
import com.bff.services.server.models.UserCreateRequest;
import com.bff.services.server.models.UserResponse;
import com.bff.services.server.models.UserType;
import java.util.List;
import org.springframework.http.ResponseEntity;

public interface UsersService {

    ResponseEntity<List<UserResponse>> listUsers(
        String xDeviceIp,
        String xSession,
        Integer xPage,
        Integer xPageSize,
        String search,
        Status status,
        UserType userType
    );

    ResponseEntity<UserResponse> createUser(String xDeviceIp, String xSession, UserCreateRequest userCreateRequest);

    ResponseEntity<UserResponse> getUserById(String xDeviceIp, String xSession, Integer id);

    ResponseEntity<UserResponse> updateUserStatus(
        String xDeviceIp,
        String xSession,
        Integer id,
        StatusUpdateRequest statusUpdateRequest
    );
}
