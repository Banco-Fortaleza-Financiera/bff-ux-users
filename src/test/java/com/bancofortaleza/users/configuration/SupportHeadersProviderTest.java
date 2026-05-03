package com.bancofortaleza.users.configuration;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.bancofortaleza.users.domain.exceptions.ApiException;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.mock.web.MockHttpServletRequest;

class SupportHeadersProviderTest {

    @Test
    void getAuthenticatedUserId_whenAttributeIsInteger_returnsUserId() {
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.setAttribute(TokenValidationInterceptor.AUTHENTICATED_USER_ID_ATTRIBUTE, 25);
        SupportHeadersProvider provider = new SupportHeadersProvider(request);

        Integer result = provider.getAuthenticatedUserId();

        assertThat(result).isEqualTo(25);
    }

    @Test
    void getAuthenticatedUserId_whenAttributeIsMissing_throwsUnauthorized() {
        SupportHeadersProvider provider = new SupportHeadersProvider(new MockHttpServletRequest());

        assertThatThrownBy(provider::getAuthenticatedUserId)
            .isInstanceOfSatisfying(ApiException.class, exception -> {
                assertThat(exception.getStatus()).isEqualTo(HttpStatus.UNAUTHORIZED);
                assertThat(exception.getCode()).isEqualTo("UNAUTHORIZED");
            });
    }

    @Test
    void getAuthenticatedUserId_whenAttributeHasUnexpectedType_throwsUnauthorized() {
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.setAttribute(TokenValidationInterceptor.AUTHENTICATED_USER_ID_ATTRIBUTE, "25");
        SupportHeadersProvider provider = new SupportHeadersProvider(request);

        assertThatThrownBy(provider::getAuthenticatedUserId)
            .isInstanceOfSatisfying(ApiException.class, exception ->
                assertThat(exception.getStatus()).isEqualTo(HttpStatus.UNAUTHORIZED)
            );
    }
}
