package com.bancofortaleza.users.services.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

class OpenApiModelMapperTest {

    private OpenApiModelMapper mapper;

    @BeforeEach
    void setUp() {
        mapper = new OpenApiModelMapper(new ObjectMapper());
    }

    @Test
    void map_whenSourceIsNull_returnsNull() {
        Target result = mapper.map(null, Target.class);

        assertThat(result).isNull();
    }

    @Test
    void map_whenSourceHasCompatibleFields_returnsTargetType() {
        Target result = mapper.map(Map.of("id", 10, "name", "Ana"), Target.class);

        assertThat(result.id()).isEqualTo(10);
        assertThat(result.name()).isEqualTo("Ana");
    }

    @Test
    void mapList_whenSourceIsNull_returnsEmptyList() {
        List<Target> result = mapper.mapList(null, Target.class);

        assertThat(result).isEmpty();
    }

    @Test
    void mapList_whenSourceHasItems_returnsMappedItems() {
        List<Target> result = mapper.mapList(
            List.of(Map.of("id", 1, "name", "Ana"), Map.of("id", 2, "name", "Luis")),
            Target.class
        );

        assertThat(result)
            .extracting(Target::name)
            .containsExactly("Ana", "Luis");
    }

    @Test
    void mapResponse_sanitizesHopByHopHeadersAndMapsBody() {
        HttpHeaders headers = new HttpHeaders();
        headers.add("x-trace-id", "trace-1");
        headers.add(HttpHeaders.CONNECTION, "close");
        headers.add(HttpHeaders.CONTENT_LENGTH, "99");
        ResponseEntity<Map<String, Object>> source = ResponseEntity
            .status(HttpStatus.CREATED)
            .headers(headers)
            .body(Map.of("id", 7, "name", "Maria"));

        ResponseEntity<Target> result = mapper.mapResponse(source, Target.class);

        assertThat(result.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(result.getBody()).isEqualTo(new Target(7, "Maria"));
        assertThat(result.getHeaders()).containsEntry("x-trace-id", List.of("trace-1"));
        assertThat(result.getHeaders()).doesNotContainKey(HttpHeaders.CONNECTION);
        assertThat(result.getHeaders()).doesNotContainKey(HttpHeaders.CONTENT_LENGTH);
    }

    @Test
    void mapListResponse_sanitizesHeadersAndMapsBodyList() {
        HttpHeaders headers = new HttpHeaders();
        headers.add("x-total-count", "2");
        headers.add(HttpHeaders.TRANSFER_ENCODING, "chunked");
        ResponseEntity<List<Map<String, Object>>> source = ResponseEntity
            .ok()
            .headers(headers)
            .body(List.of(Map.of("id", 1, "name", "Ana"), Map.of("id", 2, "name", "Luis")));

        ResponseEntity<List<Target>> result = mapper.mapListResponse(source, Target.class);

        assertThat(result.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(result.getBody()).containsExactly(new Target(1, "Ana"), new Target(2, "Luis"));
        assertThat(result.getHeaders()).containsEntry("x-total-count", List.of("2"));
        assertThat(result.getHeaders()).doesNotContainKey(HttpHeaders.TRANSFER_ENCODING);
    }

    private record Target(Integer id, String name) {
    }
}
