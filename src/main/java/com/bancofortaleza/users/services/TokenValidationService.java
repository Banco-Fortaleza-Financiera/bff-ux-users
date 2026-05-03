package com.bancofortaleza.users.services;

public interface TokenValidationService {

    void validate(String authorizationHeader, String xDeviceIp, String xSession);
}
