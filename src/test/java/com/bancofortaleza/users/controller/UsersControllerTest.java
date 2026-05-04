package com.bancofortaleza.users.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.bancofortaleza.users.services.AddressService;
import com.bancofortaleza.users.services.PhoneService;
import com.bancofortaleza.users.services.UsersService;
import com.bff.services.server.models.AddressCreateRequest;
import com.bff.services.server.models.AddressResponse;
import com.bff.services.server.models.PhoneCreateRequest;
import com.bff.services.server.models.PhoneResponse;
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
class UsersControllerTest {

    private static final String DEVICE_IP = "10.0.0.1";
    private static final String SESSION = "session-1";

    @Mock
    private UsersService usersService;

    @Mock
    private PhoneService phoneService;

    @Mock
    private AddressService addressService;

    @InjectMocks
    private UsersController controller;

    @Test
    void createUser_delegatesToUsersService() {
        UserCreateRequest request = new UserCreateRequest();
        ResponseEntity<UserResponse> expectedResponse = ResponseEntity.ok(new UserResponse());
        when(usersService.createUser(DEVICE_IP, SESSION, request)).thenReturn(expectedResponse);

        ResponseEntity<UserResponse> result = controller.createUser(DEVICE_IP, SESSION, request);

        assertThat(result).isSameAs(expectedResponse);
        verify(usersService).createUser(DEVICE_IP, SESSION, request);
    }

    @Test
    void createUserAddress_delegatesToAddressService() {
        AddressCreateRequest request = new AddressCreateRequest();
        ResponseEntity<AddressResponse> expectedResponse = ResponseEntity.ok(new AddressResponse());
        when(addressService.createUserAddress(DEVICE_IP, SESSION, 7, request)).thenReturn(expectedResponse);

        ResponseEntity<AddressResponse> result = controller.createUserAddress(DEVICE_IP, SESSION, 7, request);

        assertThat(result).isSameAs(expectedResponse);
        verify(addressService).createUserAddress(DEVICE_IP, SESSION, 7, request);
    }

    @Test
    void createUserPhone_delegatesToPhoneService() {
        PhoneCreateRequest request = new PhoneCreateRequest();
        ResponseEntity<PhoneResponse> expectedResponse = ResponseEntity.ok(new PhoneResponse());
        when(phoneService.createUserPhone(DEVICE_IP, SESSION, 7, request)).thenReturn(expectedResponse);

        ResponseEntity<PhoneResponse> result = controller.createUserPhone(DEVICE_IP, SESSION, 7, request);

        assertThat(result).isSameAs(expectedResponse);
        verify(phoneService).createUserPhone(DEVICE_IP, SESSION, 7, request);
    }

    @Test
    void getUserById_delegatesToUsersService() {
        ResponseEntity<UserResponse> expectedResponse = ResponseEntity.ok(new UserResponse());
        when(usersService.getUserById(DEVICE_IP, SESSION, 7)).thenReturn(expectedResponse);

        ResponseEntity<UserResponse> result = controller.getUserById(DEVICE_IP, SESSION, 7);

        assertThat(result).isSameAs(expectedResponse);
        verify(usersService).getUserById(DEVICE_IP, SESSION, 7);
    }

    @Test
    void getUserAddressById_delegatesToAddressService() {
        ResponseEntity<AddressResponse> expectedResponse = ResponseEntity.ok(new AddressResponse());
        when(addressService.getUserAddressById(DEVICE_IP, SESSION, 7, 3)).thenReturn(expectedResponse);

        ResponseEntity<AddressResponse> result = controller.getUserAddressById(DEVICE_IP, SESSION, 7, 3);

        assertThat(result).isSameAs(expectedResponse);
        verify(addressService).getUserAddressById(DEVICE_IP, SESSION, 7, 3);
    }

    @Test
    void getUserPhoneById_delegatesToPhoneService() {
        ResponseEntity<PhoneResponse> expectedResponse = ResponseEntity.ok(new PhoneResponse());
        when(phoneService.getUserPhoneById(DEVICE_IP, SESSION, 7, 3)).thenReturn(expectedResponse);

        ResponseEntity<PhoneResponse> result = controller.getUserPhoneById(DEVICE_IP, SESSION, 7, 3);

        assertThat(result).isSameAs(expectedResponse);
        verify(phoneService).getUserPhoneById(DEVICE_IP, SESSION, 7, 3);
    }

    @Test
    void listUsers_delegatesToUsersService() {
        ResponseEntity<List<UserResponse>> expectedResponse = ResponseEntity.ok(List.of(new UserResponse()));
        when(usersService.listUsers(DEVICE_IP, SESSION, 1, 20, "ana", Status.ACTIVE, UserType.ADMIN)).thenReturn(expectedResponse);

        ResponseEntity<List<UserResponse>> result = controller.listUsers(
            DEVICE_IP,
            SESSION,
            1,
            20,
            "ana",
            Status.ACTIVE,
            UserType.ADMIN
        );

        assertThat(result).isSameAs(expectedResponse);
        verify(usersService).listUsers(DEVICE_IP, SESSION, 1, 20, "ana", Status.ACTIVE, UserType.ADMIN);
    }

    @Test
    void listUserAddresses_delegatesToAddressService() {
        ResponseEntity<List<AddressResponse>> expectedResponse = ResponseEntity.ok(List.of(new AddressResponse()));
        when(addressService.listUserAddresses(DEVICE_IP, SESSION, 7, 1, 20, "zona", Status.ACTIVE)).thenReturn(expectedResponse);

        ResponseEntity<List<AddressResponse>> result = controller.listUserAddresses(
            DEVICE_IP,
            SESSION,
            7,
            1,
            20,
            "zona",
            Status.ACTIVE
        );

        assertThat(result).isSameAs(expectedResponse);
        verify(addressService).listUserAddresses(DEVICE_IP, SESSION, 7, 1, 20, "zona", Status.ACTIVE);
    }

    @Test
    void listUserPhones_delegatesToPhoneService() {
        ResponseEntity<List<PhoneResponse>> expectedResponse = ResponseEntity.ok(List.of(new PhoneResponse()));
        when(phoneService.listUserPhones(DEVICE_IP, SESSION, 7, 1, 20, "555", Status.ACTIVE)).thenReturn(expectedResponse);

        ResponseEntity<List<PhoneResponse>> result = controller.listUserPhones(
            DEVICE_IP,
            SESSION,
            7,
            1,
            20,
            "555",
            Status.ACTIVE
        );

        assertThat(result).isSameAs(expectedResponse);
        verify(phoneService).listUserPhones(DEVICE_IP, SESSION, 7, 1, 20, "555", Status.ACTIVE);
    }

    @Test
    void updateUserStatus_delegatesToUsersService() {
        StatusUpdateRequest request = new StatusUpdateRequest();
        ResponseEntity<UserResponse> expectedResponse = ResponseEntity.ok(new UserResponse());
        when(usersService.updateUserStatus(DEVICE_IP, SESSION, 7, request)).thenReturn(expectedResponse);

        ResponseEntity<UserResponse> result = controller.updateUserStatus(DEVICE_IP, SESSION, 7, request);

        assertThat(result).isSameAs(expectedResponse);
        verify(usersService).updateUserStatus(DEVICE_IP, SESSION, 7, request);
    }

    @Test
    void updateUserAddressStatus_delegatesToAddressService() {
        StatusUpdateRequest request = new StatusUpdateRequest();
        ResponseEntity<AddressResponse> expectedResponse = ResponseEntity.ok(new AddressResponse());
        when(addressService.updateUserAddressStatus(DEVICE_IP, SESSION, 7, 3, request)).thenReturn(expectedResponse);

        ResponseEntity<AddressResponse> result = controller.updateUserAddressStatus(DEVICE_IP, SESSION, 7, 3, request);

        assertThat(result).isSameAs(expectedResponse);
        verify(addressService).updateUserAddressStatus(DEVICE_IP, SESSION, 7, 3, request);
    }

    @Test
    void updateUserPhoneStatus_delegatesToPhoneService() {
        StatusUpdateRequest request = new StatusUpdateRequest();
        ResponseEntity<PhoneResponse> expectedResponse = ResponseEntity.ok(new PhoneResponse());
        when(phoneService.updateUserPhoneStatus(DEVICE_IP, SESSION, 7, 3, request)).thenReturn(expectedResponse);

        ResponseEntity<PhoneResponse> result = controller.updateUserPhoneStatus(DEVICE_IP, SESSION, 7, 3, request);

        assertThat(result).isSameAs(expectedResponse);
        verify(phoneService).updateUserPhoneStatus(DEVICE_IP, SESSION, 7, 3, request);
    }
}
