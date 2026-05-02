package com.bancofortaleza.users.controller;

import com.bancofortaleza.users.services.AddressService;
import com.bancofortaleza.users.services.PhoneService;
import com.bancofortaleza.users.services.UsersService;
import com.bff.services.server.ChannelApi;
import com.bff.services.server.models.*;

import lombok.RequiredArgsConstructor;

import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class UsersController implements ChannelApi {

    private final UsersService usersService;
    private final PhoneService phoneService;
    private final AddressService addressService;

    @Override
    public ResponseEntity<UserResponse> createUser(String xDeviceIp, String xSession, UserCreateRequest userCreateRequest) {
        return usersService.createUser(xDeviceIp, xSession, userCreateRequest);
    }

    @Override
    public ResponseEntity<AddressResponse> createUserAddress(String xDeviceIp, String xSession, Integer id, AddressCreateRequest addressCreateRequest) {
        return addressService.createUserAddress(xDeviceIp, xSession, id, addressCreateRequest);
    }

    @Override
    public ResponseEntity<PhoneResponse> createUserPhone(String xDeviceIp, String xSession, Integer id, PhoneCreateRequest phoneCreateRequest) {
        return phoneService.createUserPhone(xDeviceIp, xSession, id, phoneCreateRequest);
    }

    @Override
    public ResponseEntity<AddressResponse> getUserAddressById(String xDeviceIp, String xSession, Integer id, Integer addressId) {
        return addressService.getUserAddressById(xDeviceIp, xSession, id, addressId);
    }

    @Override
    public ResponseEntity<UserResponse> getUserById(String xDeviceIp, String xSession, Integer id) {
        return usersService.getUserById(xDeviceIp, xSession, id);
    }

    @Override
    public ResponseEntity<PhoneResponse> getUserPhoneById(String xDeviceIp, String xSession, Integer id, Integer phoneId) {
        return phoneService.getUserPhoneById(xDeviceIp, xSession, id, phoneId);
    }

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
        return addressService.listUserAddresses(xDeviceIp, xSession, id, xPage, xPageSize, search, status);
    }

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
        return phoneService.listUserPhones(xDeviceIp, xSession, id, xPage, xPageSize, search, status);
    }

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
        return usersService.listUsers(xDeviceIp, xSession, xPage, xPageSize, search, status, userType);
    }

    @Override
    public ResponseEntity<AddressResponse> updateUserAddressStatus(String xDeviceIp, String xSession, Integer id, Integer addressId, StatusUpdateRequest statusUpdateRequest) {
        return addressService.updateUserAddressStatus(xDeviceIp, xSession, id, addressId, statusUpdateRequest);
    }

    @Override
    public ResponseEntity<PhoneResponse> updateUserPhoneStatus(String xDeviceIp, String xSession, Integer id, Integer phoneId, StatusUpdateRequest statusUpdateRequest) {
        return phoneService.updateUserPhoneStatus(xDeviceIp, xSession, id, phoneId, statusUpdateRequest);
    }

    @Override
    public ResponseEntity<UserResponse> updateUserStatus(String xDeviceIp, String xSession, Integer id, StatusUpdateRequest statusUpdateRequest) {
        return usersService.updateUserStatus(xDeviceIp, xSession, id, statusUpdateRequest);
    }
}
