package com.bancofortaleza.users.configuration;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.bancofortaleza.users.utils.AppLogger;
import jakarta.servlet.ServletException;
import java.io.IOException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockFilterChain;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

class HttpRequestLoggingFilterTest {

    private final HttpRequestLoggingFilter filter = new HttpRequestLoggingFilter();

    @AfterEach
    void tearDown() {
        AppLogger.clearContext();
    }

    @Test
    void doFilterInternal_whenRequestIdHeaderExists_usesItInResponseAndClearsContext() throws Exception {
        MockHttpServletRequest request = new MockHttpServletRequest("GET", "/channel/v1/users");
        request.addHeader("x-request-id", "request-1");
        request.addHeader("x-device-ip", "10.0.0.1");
        MockHttpServletResponse response = new MockHttpServletResponse();

        filter.doFilterInternal(request, response, new MockFilterChain());

        assertThat(response.getHeader("x-request-id")).isEqualTo("request-1");
        assertThat(AppLogger.getContext(AppLogger.REQUEST_ID)).isNull();
    }

    @Test
    void doFilterInternal_whenRequestIdAndDeviceIpAreMissing_generatesRequestIdAndUsesRemoteAddress() throws Exception {
        MockHttpServletRequest request = new MockHttpServletRequest("POST", "/channel/v1/users");
        request.setRemoteAddr("127.0.0.1");
        MockHttpServletResponse response = new MockHttpServletResponse();

        filter.doFilterInternal(request, response, new MockFilterChain());

        assertThat(response.getHeader("x-request-id")).isNotBlank();
        assertThat(AppLogger.getContext(AppLogger.DEVICE_IP)).isNull();
    }

    @Test
    void doFilterInternal_whenFilterChainFails_clearsContextAndPropagatesException() {
        MockHttpServletRequest request = new MockHttpServletRequest("GET", "/channel/v1/users");
        MockHttpServletResponse response = new MockHttpServletResponse();
        MockFilterChain filterChain = new MockFilterChain() {
            @Override
            public void doFilter(jakarta.servlet.ServletRequest request, jakarta.servlet.ServletResponse response)
                throws IOException, ServletException {
                throw new ServletException("boom");
            }
        };

        assertThatThrownBy(() -> filter.doFilterInternal(request, response, filterChain))
            .isInstanceOf(ServletException.class)
            .hasMessage("boom");
        assertThat(AppLogger.getContext(AppLogger.REQUEST_ID)).isNull();
    }
}
