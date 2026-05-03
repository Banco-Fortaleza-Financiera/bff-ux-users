package com.bancofortaleza.users.services;

public interface TokenValidationService {

    Integer validate(String authorizationHeader, String xDeviceIp, String xSession);
}
