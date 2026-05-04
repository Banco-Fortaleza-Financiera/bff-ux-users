package com.bancofortaleza.users.services.mapper;

import java.util.List;
import java.util.Locale;
import java.util.Set;
import org.mapstruct.Mapper;
import org.mapstruct.NullValueMappingStrategy;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;

@Mapper(componentModel = "spring", nullValueIterableMappingStrategy = NullValueMappingStrategy.RETURN_DEFAULT)
public interface OpenApiModelMapper {

    Set<String> HOP_BY_HOP_HEADERS = Set.of(
        HttpHeaders.CONNECTION.toLowerCase(Locale.ROOT),
        HttpHeaders.CONTENT_LENGTH.toLowerCase(Locale.ROOT),
        HttpHeaders.TRANSFER_ENCODING.toLowerCase(Locale.ROOT),
        "keep-alive",
        "proxy-authenticate",
        "proxy-authorization",
        "te",
        "trailer",
        "upgrade"
    );

    com.bff.services.client.models.Status toClientStatus(com.bff.services.server.models.Status status);

    com.bff.services.client.models.UserType toClientUserType(com.bff.services.server.models.UserType userType);

    com.bff.services.client.models.StatusUpdateRequest toClientStatusUpdateRequest(
        com.bff.services.server.models.StatusUpdateRequest request
    );

    com.bff.services.client.models.UserCreateRequest toClientUserCreateRequest(
        com.bff.services.server.models.UserCreateRequest request
    );

    com.bff.services.client.models.AddressCreateRequest toClientAddressCreateRequest(
        com.bff.services.server.models.AddressCreateRequest request
    );

    com.bff.services.client.models.PhoneCreateRequest toClientPhoneCreateRequest(
        com.bff.services.server.models.PhoneCreateRequest request
    );

    com.bff.services.server.models.UserResponse toServerUserResponse(
        com.bff.services.client.models.UserResponse response
    );

    com.bff.services.server.models.AddressResponse toServerAddressResponse(
        com.bff.services.client.models.AddressResponse response
    );

    com.bff.services.server.models.PhoneResponse toServerPhoneResponse(
        com.bff.services.client.models.PhoneResponse response
    );

    List<com.bff.services.server.models.UserResponse> toServerUserResponses(
        List<com.bff.services.client.models.UserResponse> responses
    );

    List<com.bff.services.server.models.AddressResponse> toServerAddressResponses(
        List<com.bff.services.client.models.AddressResponse> responses
    );

    List<com.bff.services.server.models.PhoneResponse> toServerPhoneResponses(
        List<com.bff.services.client.models.PhoneResponse> responses
    );

    default ResponseEntity<com.bff.services.server.models.UserResponse> mapUserResponse(
        ResponseEntity<com.bff.services.client.models.UserResponse> source
    ) {
        return ResponseEntity
            .status(source.getStatusCode())
            .headers(sanitizeHeaders(source.getHeaders()))
            .body(toServerUserResponse(source.getBody()));
    }

    default ResponseEntity<List<com.bff.services.server.models.UserResponse>> mapUserListResponse(
        ResponseEntity<List<com.bff.services.client.models.UserResponse>> source
    ) {
        return ResponseEntity
            .status(source.getStatusCode())
            .headers(sanitizeHeaders(source.getHeaders()))
            .body(toServerUserResponses(source.getBody()));
    }

    default ResponseEntity<com.bff.services.server.models.AddressResponse> mapAddressResponse(
        ResponseEntity<com.bff.services.client.models.AddressResponse> source
    ) {
        return ResponseEntity
            .status(source.getStatusCode())
            .headers(sanitizeHeaders(source.getHeaders()))
            .body(toServerAddressResponse(source.getBody()));
    }

    default ResponseEntity<List<com.bff.services.server.models.AddressResponse>> mapAddressListResponse(
        ResponseEntity<List<com.bff.services.client.models.AddressResponse>> source
    ) {
        return ResponseEntity
            .status(source.getStatusCode())
            .headers(sanitizeHeaders(source.getHeaders()))
            .body(toServerAddressResponses(source.getBody()));
    }

    default ResponseEntity<com.bff.services.server.models.PhoneResponse> mapPhoneResponse(
        ResponseEntity<com.bff.services.client.models.PhoneResponse> source
    ) {
        return ResponseEntity
            .status(source.getStatusCode())
            .headers(sanitizeHeaders(source.getHeaders()))
            .body(toServerPhoneResponse(source.getBody()));
    }

    default ResponseEntity<List<com.bff.services.server.models.PhoneResponse>> mapPhoneListResponse(
        ResponseEntity<List<com.bff.services.client.models.PhoneResponse>> source
    ) {
        return ResponseEntity
            .status(source.getStatusCode())
            .headers(sanitizeHeaders(source.getHeaders()))
            .body(toServerPhoneResponses(source.getBody()));
    }

    private HttpHeaders sanitizeHeaders(HttpHeaders source) {
        HttpHeaders headers = new HttpHeaders();
        source.forEach((name, values) -> {
            if (!isHopByHopHeader(name)) {
                values.forEach(value -> headers.add(name, value));
            }
        });
        return headers;
    }

    private boolean isHopByHopHeader(String name) {
        return HOP_BY_HOP_HEADERS.contains(name.toLowerCase(Locale.ROOT));
    }
}
