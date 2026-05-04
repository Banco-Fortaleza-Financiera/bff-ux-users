package com.bancofortaleza.users.configuration;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

import com.bancofortaleza.users.services.TokenValidationService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.http.HttpHeaders;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

@ExtendWith(MockitoExtension.class)
class TokenValidationInterceptorTest {

    @Mock
    private ObjectProvider<TokenValidationService> tokenValidationServiceProvider;

    @Mock
    private TokenValidationService tokenValidationService;

    @Test
    void preHandle_whenDeviceIpIsMissing_skipsTokenValidation() {
        TokenValidationInterceptor interceptor = new TokenValidationInterceptor(tokenValidationServiceProvider);
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.addHeader("x-session", "session-1");

        boolean result = interceptor.preHandle(request, new MockHttpServletResponse(), new Object());

        assertThat(result).isTrue();
        verifyNoInteractions(tokenValidationServiceProvider);
    }

    @Test
    void preHandle_whenSessionIsBlank_skipsTokenValidation() {
        TokenValidationInterceptor interceptor = new TokenValidationInterceptor(tokenValidationServiceProvider);
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.addHeader("x-device-ip", "10.0.0.1");
        request.addHeader("x-session", " ");

        boolean result = interceptor.preHandle(request, new MockHttpServletResponse(), new Object());

        assertThat(result).isTrue();
        verifyNoInteractions(tokenValidationServiceProvider);
    }

    @Test
    void preHandle_whenRequiredHeadersArePresent_validatesTokenAndStoresAuthenticatedUserId() {
        TokenValidationInterceptor interceptor = new TokenValidationInterceptor(tokenValidationServiceProvider);
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.addHeader("x-device-ip", "10.0.0.1");
        request.addHeader("x-session", "session-1");
        request.addHeader(HttpHeaders.AUTHORIZATION, "Bearer token");
        when(tokenValidationServiceProvider.getObject()).thenReturn(tokenValidationService);
        when(tokenValidationService.validate("Bearer token", "10.0.0.1", "session-1")).thenReturn(31);

        boolean result = interceptor.preHandle(request, new MockHttpServletResponse(), new Object());

        assertThat(result).isTrue();
        assertThat(request.getAttribute(TokenValidationInterceptor.AUTHENTICATED_USER_ID_ATTRIBUTE)).isEqualTo(31);
        verify(tokenValidationService).validate("Bearer token", "10.0.0.1", "session-1");
    }
}
