package com.bancofortaleza.users.handler;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.bancofortaleza.users.domain.exceptions.ApiException;
import com.bancofortaleza.users.domain.model.ErrorResponse;
import feign.FeignException;
import feign.Request;
import feign.Response;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Path;
import java.nio.charset.StandardCharsets;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.core.MethodParameter;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingRequestHeaderException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.servlet.resource.NoResourceFoundException;

class GlobalExceptionHandlerTest {

    private GlobalExceptionHandler handler;
    private MockHttpServletRequest request;

    @BeforeEach
    void setUp() {
        handler = new GlobalExceptionHandler();
        request = new MockHttpServletRequest("GET", "/channel/v1/users");
    }

    @Test
    void handleFeignException_whenStatusIsValid_returnsDownstreamBodyAndHeaders() {
        FeignException exception = feignException(
            404,
            Map.of(
                "x-trace-id", List.of("trace-1"),
                HttpHeaders.CONNECTION, List.of("close")
            ),
            "not found"
        );

        ResponseEntity<byte[]> result = handler.handleFeignException(exception);

        assertThat(result.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        assertThat(result.getBody()).isEqualTo("not found".getBytes(StandardCharsets.UTF_8));
        assertThat(result.getHeaders()).containsEntry("x-trace-id", List.of("trace-1"));
        assertThat(result.getHeaders()).doesNotContainKey(HttpHeaders.CONNECTION);
    }

    @Test
    void handleFeignException_whenHeaderNameOrValuesAreNull_ignoresInvalidHeaders() {
        FeignException exception = feignException(
            502,
            Map.of("x-trace-id", List.of("trace-1")),
            "bad gateway"
        );

        ResponseEntity<byte[]> result = handler.handleFeignException(exception);

        assertThat(result.getStatusCode()).isEqualTo(HttpStatus.BAD_GATEWAY);
        assertThat(result.getHeaders()).containsEntry("x-trace-id", List.of("trace-1"));
    }

    @Test
    void handleFeignException_whenStatusIsInvalid_returnsBadGateway() {
        FeignException exception = mock(FeignException.class);
        when(exception.status()).thenReturn(0);

        ResponseEntity<byte[]> result = handler.handleFeignException(exception);

        assertThat(result.getStatusCode()).isEqualTo(HttpStatus.BAD_GATEWAY);
        assertThat(result.getBody()).isNull();
    }

    @Test
    void handleApiException_returnsConfiguredErrorResponse() {
        ApiException exception = new ApiException(HttpStatus.UNAUTHORIZED, "UNAUTHORIZED", "No tiene autorizacion");

        ResponseEntity<ErrorResponse> result = handler.handleApiException(exception, request);

        assertThat(result.getStatusCode()).isEqualTo(HttpStatus.UNAUTHORIZED);
        assertThat(result.getBody()).isNotNull();
        assertThat(result.getBody().timestamp()).isNotNull();
        assertThat(result.getBody().status()).isEqualTo(401);
        assertThat(result.getBody().error()).isEqualTo("Unauthorized");
        assertThat(result.getBody().code()).isEqualTo("UNAUTHORIZED");
        assertThat(result.getBody().path()).isEqualTo("/channel/v1/users");
        assertThat(result.getBody().details()).isEmpty();
    }

    @Test
    void handleValidation_returnsFieldErrorsWithPublicHeaderNames() {
        BeanPropertyBindingResult bindingResult = new BeanPropertyBindingResult(new Object(), "request");
        bindingResult.addError(new FieldError("request", "xSession", "must not be blank"));
        MethodArgumentNotValidException exception = new MethodArgumentNotValidException(
            mock(MethodParameter.class),
            bindingResult
        );

        ResponseEntity<ErrorResponse> result = handler.handleValidation(exception, request);

        assertThat(result.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(result.getBody()).isNotNull();
        assertThat(result.getBody().code()).isEqualTo("VALIDATION_ERROR");
        assertThat(result.getBody().details())
            .containsExactly(new ErrorResponse.FieldError("x-session", "must not be blank"));
    }

    @Test
    void handleConstraintViolation_returnsSanitizedFieldErrors() {
        ConstraintViolation<Object> violation = mock(ConstraintViolation.class);
        Path path = mock(Path.class);
        when(violation.getPropertyPath()).thenReturn(path);
        when(path.toString()).thenReturn("listUsers.xDeviceIp");
        when(violation.getMessage()).thenReturn("must not be blank");
        ConstraintViolationException exception = new ConstraintViolationException("invalid", Set.of(violation));

        ResponseEntity<ErrorResponse> result = handler.handleConstraintViolation(exception, request);

        assertThat(result.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(result.getBody()).isNotNull();
        assertThat(result.getBody().details())
            .containsExactly(new ErrorResponse.FieldError("x-device-ip", "must not be blank"));
    }

    @Test
    void handleBadRequest_returnsInvalidRequestResponse() {
        ResponseEntity<ErrorResponse> result = handler.handleBadRequest(new IllegalArgumentException("bad"), request);

        assertThat(result.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(result.getBody()).isNotNull();
        assertThat(result.getBody().code()).isEqualTo("BAD_REQUEST");
        assertThat(result.getBody().message()).isEqualTo("Invalid request");
    }

    @Test
    void handleMissingRequestParameter_returnsParameterDetail() {
        MissingServletRequestParameterException exception = new MissingServletRequestParameterException("search", "String");

        ResponseEntity<ErrorResponse> result = handler.handleMissingRequestParameter(exception, request);

        assertThat(result.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(result.getBody()).isNotNull();
        assertThat(result.getBody().code()).isEqualTo("MISSING_REQUIRED_PARAMETER");
        assertThat(result.getBody().details())
            .containsExactly(new ErrorResponse.FieldError("search", "Parameter is required"));
    }

    @Test
    void handleMissingRequestHeader_returnsHeaderDetail() {
        MissingRequestHeaderException exception = new MissingRequestHeaderException(
            "x-session",
            mock(MethodParameter.class)
        );

        ResponseEntity<ErrorResponse> result = handler.handleMissingRequestHeader(exception, request);

        assertThat(result.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(result.getBody()).isNotNull();
        assertThat(result.getBody().code()).isEqualTo("MISSING_REQUIRED_HEADER");
        assertThat(result.getBody().details())
            .containsExactly(new ErrorResponse.FieldError("x-session", "Header is required"));
    }

    @Test
    void handleNotFound_returnsNotFoundResponse() {
        NoResourceFoundException exception = new NoResourceFoundException(HttpMethod.GET, "/missing");

        ResponseEntity<ErrorResponse> result = handler.handleNotFound(exception, request);

        assertThat(result.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        assertThat(result.getBody()).isNotNull();
        assertThat(result.getBody().code()).isEqualTo("NOT_FOUND");
    }

    @Test
    void handleMethodNotAllowed_returnsMethodNotAllowedResponse() {
        HttpRequestMethodNotSupportedException exception = new HttpRequestMethodNotSupportedException("POST");

        ResponseEntity<ErrorResponse> result = handler.handleMethodNotAllowed(exception, request);

        assertThat(result.getStatusCode()).isEqualTo(HttpStatus.METHOD_NOT_ALLOWED);
        assertThat(result.getBody()).isNotNull();
        assertThat(result.getBody().code()).isEqualTo("METHOD_NOT_ALLOWED");
    }

    @Test
    void handleUnexpected_returnsInternalServerErrorResponse() {
        ResponseEntity<ErrorResponse> result = handler.handleUnexpected(new RuntimeException("boom"), request);

        assertThat(result.getStatusCode()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR);
        assertThat(result.getBody()).isNotNull();
        assertThat(result.getBody().code()).isEqualTo("INTERNAL_SERVER_ERROR");
        assertThat(result.getBody().message()).isEqualTo("Unexpected error");
    }

    private FeignException feignException(
        int status,
        Map<String, Collection<String>> headers,
        String body
    ) {
        Request request = Request.create(
            Request.HttpMethod.GET,
            "/support",
            Map.of(),
            null,
            StandardCharsets.UTF_8,
            null
        );
        Response response = Response.builder()
            .status(status)
            .reason("status")
            .request(request)
            .headers(headers)
            .body(body, StandardCharsets.UTF_8)
            .build();
        return FeignException.errorStatus("method", response);
    }
}
