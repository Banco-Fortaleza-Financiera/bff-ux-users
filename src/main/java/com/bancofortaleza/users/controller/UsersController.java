package com.bancofortaleza.users.controller;

import com.bff.services.server.ChannelApi;
import com.bff.services.server.models.*;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;


@RestController
public class UsersController implements ChannelApi {

    @Override
    public ResponseEntity<UserResponse> createUser(String xDeviceIp, String xSession, UserCreateRequest userCreateRequest) {
        return ChannelApi.super.createUser(xDeviceIp, xSession, userCreateRequest);
    }

    @Override
    public ResponseEntity<AddressResponse> createUserAddress(String xDeviceIp, String xSession, Integer id, AddressCreateRequest addressCreateRequest) {
        return ChannelApi.super.createUserAddress(xDeviceIp, xSession, id, addressCreateRequest);
    }

    @Override
    public ResponseEntity<PhoneResponse> createUserPhone(String xDeviceIp, String xSession, Integer id, PhoneCreateRequest phoneCreateRequest) {
        return ChannelApi.super.createUserPhone(xDeviceIp, xSession, id, phoneCreateRequest);
    }

    @Override
    public ResponseEntity<AddressResponse> getUserAddressById(String xDeviceIp, String xSession, Integer id, Integer addressId) {
        return ChannelApi.super.getUserAddressById(xDeviceIp, xSession, id, addressId);
    }

    @Override
    public ResponseEntity<UserResponse> getUserById(String xDeviceIp, String xSession, Integer id) {
        return ChannelApi.super.getUserById(xDeviceIp, xSession, id);
    }

    @Override
    public ResponseEntity<PhoneResponse> getUserPhoneById(String xDeviceIp, String xSession, Integer id, Integer phoneId) {
        return ChannelApi.super.getUserPhoneById(xDeviceIp, xSession, id, phoneId);
    }

    @Override
    public ResponseEntity<List<AddressResponse>> listUserAddresses(String xDeviceIp, String xSession, Integer id, Status status) {
        return ChannelApi.super.listUserAddresses(xDeviceIp, xSession, id, status);
    }

    @Override
    public ResponseEntity<List<PhoneResponse>> listUserPhones(String xDeviceIp, String xSession, Integer id, Status status) {
        return ChannelApi.super.listUserPhones(xDeviceIp, xSession, id, status);
    }

    @Override
    public ResponseEntity<List<UserResponse>> listUsers(String xDeviceIp, String xSession, Status status, UserType userType) {
        return ChannelApi.super.listUsers(xDeviceIp, xSession, status, userType);
    }

    @Override
    public ResponseEntity<AddressResponse> updateUserAddressStatus(String xDeviceIp, String xSession, Integer id, Integer addressId, StatusUpdateRequest statusUpdateRequest) {
        return ChannelApi.super.updateUserAddressStatus(xDeviceIp, xSession, id, addressId, statusUpdateRequest);
    }

    @Override
    public ResponseEntity<PhoneResponse> updateUserPhoneStatus(String xDeviceIp, String xSession, Integer id, Integer phoneId, StatusUpdateRequest statusUpdateRequest) {
        return ChannelApi.super.updateUserPhoneStatus(xDeviceIp, xSession, id, phoneId, statusUpdateRequest);
    }

    @Override
    public ResponseEntity<UserResponse> updateUserStatus(String xDeviceIp, String xSession, Integer id, StatusUpdateRequest statusUpdateRequest) {
        return ChannelApi.super.updateUserStatus(xDeviceIp, xSession, id, statusUpdateRequest);
    }
}
