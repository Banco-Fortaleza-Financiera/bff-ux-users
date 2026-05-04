package com.bancofortaleza.users.configuration;

import static org.mockito.Mockito.mock;

import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;

class WebMvcConfigurationTest {

    @Test
    void addInterceptors_registersTokenValidationInterceptor() {
        TokenValidationInterceptor interceptor = mock(TokenValidationInterceptor.class);
        WebMvcConfiguration configuration = new WebMvcConfiguration(interceptor);
        TestInterceptorRegistry registry = new TestInterceptorRegistry();

        configuration.addInterceptors(registry);

        org.assertj.core.api.Assertions.assertThat(((TestInterceptorRegistry) registry).registeredInterceptors()).hasSize(1);
    }

    private static class TestInterceptorRegistry extends InterceptorRegistry {

        List<Object> registeredInterceptors() {
            return getInterceptors();
        }
    }
}
