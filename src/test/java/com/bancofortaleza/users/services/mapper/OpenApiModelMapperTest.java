package com.bancofortaleza.users.services.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.OffsetDateTime;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

class OpenApiModelMapperTest {

    private final OpenApiModelMapper mapper = Mappers.getMapper(OpenApiModelMapper.class);

    @Test
    void toServerUserResponse_whenSourceIsNull_returnsNull() {
        assertThat(mapper.toServerUserResponse(null)).isNull();
    }

    @Test
    void toServerUserResponse_whenSourceHasValues_mapsAllFields() {
        com.bff.services.client.models.UserResponse source = new com.bff.services.client.models.UserResponse()
            .id(7)
            .status(com.bff.services.client.models.Status.ACTIVE)
            .userType(com.bff.services.client.models.UserType.ADMIN)
            .addresses(List.of(new com.bff.services.client.models.AddressResponse()
                .id(11)
                .address("Zona 10")
                .status(com.bff.services.client.models.Status.ACTIVE)))
            .phones(List.of(new com.bff.services.client.models.PhoneResponse()
                .id(13)
                .phone("55555555")
                .status(com.bff.services.client.models.Status.INACTIVE)));

        com.bff.services.server.models.UserResponse result = mapper.toServerUserResponse(source);

        assertThat(result.getId()).isEqualTo(7);
        assertThat(result.getStatus()).isEqualTo(com.bff.services.server.models.Status.ACTIVE);
        assertThat(result.getUserType()).isEqualTo(com.bff.services.server.models.UserType.ADMIN);
        assertThat(result.getAddresses()).hasSize(1);
        assertThat(result.getAddresses().getFirst().getAddress()).isEqualTo("Zona 10");
        assertThat(result.getPhones()).hasSize(1);
        assertThat(result.getPhones().getFirst().getPhone()).isEqualTo("55555555");
    }

    @Test
    void toServerAddressResponses_whenSourceIsNull_returnsEmptyList() {
        assertThat(mapper.toServerAddressResponses(null)).isEmpty();
    }

    @Test
    void toClientStatusUpdateRequest_mapsStatus() {
        com.bff.services.server.models.StatusUpdateRequest request =
            new com.bff.services.server.models.StatusUpdateRequest()
                .status(com.bff.services.server.models.Status.INACTIVE);

        com.bff.services.client.models.StatusUpdateRequest result = mapper.toClientStatusUpdateRequest(request);

        assertThat(result.getStatus()).isEqualTo(com.bff.services.client.models.Status.INACTIVE);
    }

    @Test
    void toClientStatusAndUserType_whenSourceIsNull_returnsNull() {
        assertThat(mapper.toClientStatus(null)).isNull();
        assertThat(mapper.toClientUserType(null)).isNull();
    }

    @Test
    void toClientEnums_mapsAllValues() {
        assertThat(mapper.toClientStatus(com.bff.services.server.models.Status.ACTIVE))
            .isEqualTo(com.bff.services.client.models.Status.ACTIVE);
        assertThat(mapper.toClientStatus(com.bff.services.server.models.Status.INACTIVE))
            .isEqualTo(com.bff.services.client.models.Status.INACTIVE);
        assertThat(mapper.toClientUserType(com.bff.services.server.models.UserType.ADMIN))
            .isEqualTo(com.bff.services.client.models.UserType.ADMIN);
        assertThat(mapper.toClientUserType(com.bff.services.server.models.UserType.NORMAL))
            .isEqualTo(com.bff.services.client.models.UserType.NORMAL);
    }

    @Test
    void toClientUserCreateRequest_whenSourceHasValues_mapsAllFields() {
        com.bff.services.server.models.UserCreateRequest source = new com.bff.services.server.models.UserCreateRequest()
            .name("Gerson")
            .lastName("Ramos")
            .password("Str0ngP@ssword")
            .gender(com.bff.services.server.models.Gender.FEMALE)
            .age(30)
            .identification("1234567890101")
            .status(com.bff.services.server.models.Status.ACTIVE)
            .userType(com.bff.services.server.models.UserType.NORMAL)
            .addresses(List.of(new com.bff.services.server.models.AddressCreateRequest()
                .address("10 Calle 1-23 Zona 10")
                .typeAddress("HOME")
                .status(com.bff.services.server.models.Status.ACTIVE)))
            .phones(List.of(new com.bff.services.server.models.PhoneCreateRequest()
                .phone("+502 5555 1234")
                .typePhone("MOBILE")
                .status(com.bff.services.server.models.Status.INACTIVE)));

        com.bff.services.client.models.UserCreateRequest result = mapper.toClientUserCreateRequest(source);

        assertThat(result.getName()).isEqualTo("Gerson");
        assertThat(result.getLastName()).isEqualTo("Ramos");
        assertThat(result.getPassword()).isEqualTo("Str0ngP@ssword");
        assertThat(result.getGender()).isEqualTo(com.bff.services.client.models.Gender.FEMALE);
        assertThat(result.getAge()).isEqualTo(30);
        assertThat(result.getIdentification()).isEqualTo("1234567890101");
        assertThat(result.getStatus()).isEqualTo(com.bff.services.client.models.Status.ACTIVE);
        assertThat(result.getUserType()).isEqualTo(com.bff.services.client.models.UserType.NORMAL);
        assertThat(result.getAddresses()).hasSize(1);
        assertThat(result.getAddresses().getFirst().getAddress()).isEqualTo("10 Calle 1-23 Zona 10");
        assertThat(result.getAddresses().getFirst().getTypeAddress()).isEqualTo("HOME");
        assertThat(result.getAddresses().getFirst().getStatus()).isEqualTo(com.bff.services.client.models.Status.ACTIVE);
        assertThat(result.getPhones()).hasSize(1);
        assertThat(result.getPhones().getFirst().getPhone()).isEqualTo("+502 5555 1234");
        assertThat(result.getPhones().getFirst().getTypePhone()).isEqualTo("MOBILE");
        assertThat(result.getPhones().getFirst().getStatus()).isEqualTo(com.bff.services.client.models.Status.INACTIVE);
    }

    @Test
    void toClientCreateRequests_whenSourceIsNull_returnsNull() {
        assertThat(mapper.toClientStatusUpdateRequest(null)).isNull();
        assertThat(mapper.toClientUserCreateRequest(null)).isNull();
        assertThat(mapper.toClientAddressCreateRequest(null)).isNull();
        assertThat(mapper.toClientPhoneCreateRequest(null)).isNull();
    }

    @Test
    void toClientUserCreateRequest_whenNestedListsAreNull_returnsEmptyLists() {
        com.bff.services.server.models.UserCreateRequest source = new com.bff.services.server.models.UserCreateRequest()
            .addresses(null)
            .phones(null);

        com.bff.services.client.models.UserCreateRequest result = mapper.toClientUserCreateRequest(source);

        assertThat(result.getAddresses()).isEmpty();
        assertThat(result.getPhones()).isEmpty();
    }

    @Test
    void toServerAddressResponse_whenSourceHasValues_mapsAllFields() {
        OffsetDateTime createdAt = OffsetDateTime.parse("2026-05-01T16:35:00Z");
        OffsetDateTime updatedAt = OffsetDateTime.parse("2026-05-01T16:40:00Z");
        com.bff.services.client.models.AddressResponse source = new com.bff.services.client.models.AddressResponse()
            .id(11)
            .idUser(7)
            .address("Zona 10")
            .typeAddress("HOME")
            .status(com.bff.services.client.models.Status.ACTIVE)
            .createdAt(createdAt)
            .updatedAt(updatedAt);

        com.bff.services.server.models.AddressResponse result = mapper.toServerAddressResponse(source);

        assertThat(result.getId()).isEqualTo(11);
        assertThat(result.getIdUser()).isEqualTo(7);
        assertThat(result.getAddress()).isEqualTo("Zona 10");
        assertThat(result.getTypeAddress()).isEqualTo("HOME");
        assertThat(result.getStatus()).isEqualTo(com.bff.services.server.models.Status.ACTIVE);
        assertThat(result.getCreatedAt()).isEqualTo(createdAt);
        assertThat(result.getUpdatedAt()).isEqualTo(updatedAt);
    }

    @Test
    void toServerPhoneResponse_whenSourceHasValues_mapsAllFields() {
        OffsetDateTime createdAt = OffsetDateTime.parse("2026-05-01T16:35:00Z");
        OffsetDateTime updatedAt = OffsetDateTime.parse("2026-05-01T16:40:00Z");
        com.bff.services.client.models.PhoneResponse source = new com.bff.services.client.models.PhoneResponse()
            .id(13)
            .idUser(7)
            .phone("55555555")
            .typePhone("MOBILE")
            .status(com.bff.services.client.models.Status.INACTIVE)
            .createdAt(createdAt)
            .updatedAt(updatedAt);

        com.bff.services.server.models.PhoneResponse result = mapper.toServerPhoneResponse(source);

        assertThat(result.getId()).isEqualTo(13);
        assertThat(result.getIdUser()).isEqualTo(7);
        assertThat(result.getPhone()).isEqualTo("55555555");
        assertThat(result.getTypePhone()).isEqualTo("MOBILE");
        assertThat(result.getStatus()).isEqualTo(com.bff.services.server.models.Status.INACTIVE);
        assertThat(result.getCreatedAt()).isEqualTo(createdAt);
        assertThat(result.getUpdatedAt()).isEqualTo(updatedAt);
    }

    @Test
    void toServerResponses_whenSourceIsNull_returnsNull() {
        assertThat(mapper.toServerAddressResponse(null)).isNull();
        assertThat(mapper.toServerPhoneResponse(null)).isNull();
    }

    @Test
    void toServerLists_whenSourceIsNull_returnsEmptyLists() {
        assertThat(mapper.toServerUserResponses(null)).isEmpty();
        assertThat(mapper.toServerPhoneResponses(null)).isEmpty();
    }

    @Test
    void mapUserResponse_sanitizesHopByHopHeadersAndMapsBody() {
        HttpHeaders headers = new HttpHeaders();
        headers.add("x-trace-id", "trace-1");
        headers.add(HttpHeaders.CONNECTION, "close");
        headers.add(HttpHeaders.CONTENT_LENGTH, "99");
        ResponseEntity<com.bff.services.client.models.UserResponse> source = ResponseEntity
            .status(HttpStatus.CREATED)
            .headers(headers)
            .body(new com.bff.services.client.models.UserResponse().id(7));

        ResponseEntity<com.bff.services.server.models.UserResponse> result = mapper.mapUserResponse(source);

        assertThat(result.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(result.getBody()).isNotNull();
        assertThat(result.getBody().getId()).isEqualTo(7);
        assertThat(result.getHeaders()).containsEntry("x-trace-id", List.of("trace-1"));
        assertThat(result.getHeaders()).doesNotContainKey(HttpHeaders.CONNECTION);
        assertThat(result.getHeaders()).doesNotContainKey(HttpHeaders.CONTENT_LENGTH);
    }

    @Test
    void mapUserListResponse_sanitizesHeadersAndMapsBodyList() {
        HttpHeaders headers = new HttpHeaders();
        headers.add("x-total-count", "1");
        headers.add("TE", "trailers");
        ResponseEntity<List<com.bff.services.client.models.UserResponse>> source = ResponseEntity
            .accepted()
            .headers(headers)
            .body(List.of(new com.bff.services.client.models.UserResponse().id(7)));

        ResponseEntity<List<com.bff.services.server.models.UserResponse>> result = mapper.mapUserListResponse(source);

        assertThat(result.getStatusCode()).isEqualTo(HttpStatus.ACCEPTED);
        assertThat(result.getBody()).hasSize(1);
        assertThat(result.getBody().getFirst().getId()).isEqualTo(7);
        assertThat(result.getHeaders()).containsEntry("x-total-count", List.of("1"));
        assertThat(result.getHeaders()).doesNotContainKey("TE");
    }

    @Test
    void mapAddressResponse_sanitizesHeadersAndMapsBody() {
        HttpHeaders headers = new HttpHeaders();
        headers.add("x-trace-id", "trace-2");
        headers.add("Upgrade", "websocket");
        ResponseEntity<com.bff.services.client.models.AddressResponse> source = ResponseEntity
            .ok()
            .headers(headers)
            .body(new com.bff.services.client.models.AddressResponse().id(11));

        ResponseEntity<com.bff.services.server.models.AddressResponse> result = mapper.mapAddressResponse(source);

        assertThat(result.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(result.getBody()).isNotNull();
        assertThat(result.getBody().getId()).isEqualTo(11);
        assertThat(result.getHeaders()).containsEntry("x-trace-id", List.of("trace-2"));
        assertThat(result.getHeaders()).doesNotContainKey("Upgrade");
    }

    @Test
    void mapAddressListResponse_sanitizesHeadersAndMapsBodyList() {
        HttpHeaders headers = new HttpHeaders();
        headers.add("x-total-count", "1");
        headers.add("Proxy-Authorization", "secret");
        ResponseEntity<List<com.bff.services.client.models.AddressResponse>> source = ResponseEntity
            .ok()
            .headers(headers)
            .body(List.of(new com.bff.services.client.models.AddressResponse().id(11)));

        ResponseEntity<List<com.bff.services.server.models.AddressResponse>> result = mapper.mapAddressListResponse(source);

        assertThat(result.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(result.getBody()).hasSize(1);
        assertThat(result.getBody().getFirst().getId()).isEqualTo(11);
        assertThat(result.getHeaders()).containsEntry("x-total-count", List.of("1"));
        assertThat(result.getHeaders()).doesNotContainKey("Proxy-Authorization");
    }

    @Test
    void mapPhoneResponse_sanitizesHeadersAndMapsBody() {
        HttpHeaders headers = new HttpHeaders();
        headers.add("x-trace-id", "trace-3");
        headers.add("Keep-Alive", "timeout=5");
        ResponseEntity<com.bff.services.client.models.PhoneResponse> source = ResponseEntity
            .ok()
            .headers(headers)
            .body(new com.bff.services.client.models.PhoneResponse().id(13));

        ResponseEntity<com.bff.services.server.models.PhoneResponse> result = mapper.mapPhoneResponse(source);

        assertThat(result.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(result.getBody()).isNotNull();
        assertThat(result.getBody().getId()).isEqualTo(13);
        assertThat(result.getHeaders()).containsEntry("x-trace-id", List.of("trace-3"));
        assertThat(result.getHeaders()).doesNotContainKey("Keep-Alive");
    }

    @Test
    void mapPhoneListResponse_sanitizesHeadersAndMapsBodyList() {
        HttpHeaders headers = new HttpHeaders();
        headers.add("x-total-count", "2");
        headers.add(HttpHeaders.TRANSFER_ENCODING, "chunked");
        ResponseEntity<List<com.bff.services.client.models.PhoneResponse>> source = ResponseEntity
            .ok()
            .headers(headers)
            .body(List.of(new com.bff.services.client.models.PhoneResponse().id(1)));

        ResponseEntity<List<com.bff.services.server.models.PhoneResponse>> result = mapper.mapPhoneListResponse(source);

        assertThat(result.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(result.getBody()).hasSize(1);
        assertThat(result.getBody().getFirst().getId()).isEqualTo(1);
        assertThat(result.getHeaders()).containsEntry("x-total-count", List.of("2"));
        assertThat(result.getHeaders()).doesNotContainKey(HttpHeaders.TRANSFER_ENCODING);
    }
}
