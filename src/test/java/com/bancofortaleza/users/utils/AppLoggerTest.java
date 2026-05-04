package com.bancofortaleza.users.utils;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;

import java.util.Map;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

class AppLoggerTest {

    @AfterEach
    void tearDown() {
        AppLogger.clearContext();
    }

    @Test
    void putContext_whenValueIsPresent_storesValueInMdc() {
        AppLogger.putContext(AppLogger.REQUEST_ID, "request-1");

        assertThat(AppLogger.getContext(AppLogger.REQUEST_ID)).isEqualTo("request-1");
    }

    @Test
    void putContext_whenValueIsNullOrBlank_doesNotStoreValue() {
        AppLogger.putContext(AppLogger.REQUEST_ID, null);
        AppLogger.putContext(AppLogger.METHOD, " ");

        assertThat(AppLogger.getContext(AppLogger.REQUEST_ID)).isNull();
        assertThat(AppLogger.getContext(AppLogger.METHOD)).isNull();
    }

    @Test
    void putContext_whenMapIsProvided_storesOnlyNonBlankValues() {
        AppLogger.putContext(Map.of(AppLogger.PATH, "/users", AppLogger.DEVICE_IP, "10.0.0.1"));

        assertThat(AppLogger.getContext(AppLogger.PATH)).isEqualTo("/users");
        assertThat(AppLogger.getContext(AppLogger.DEVICE_IP)).isEqualTo("10.0.0.1");
    }

    @Test
    void removeContext_removesStoredValue() {
        AppLogger.putContext(AppLogger.REQUEST_ID, "request-1");

        AppLogger.removeContext(AppLogger.REQUEST_ID);

        assertThat(AppLogger.getContext(AppLogger.REQUEST_ID)).isNull();
    }

    @Test
    void loggingMethods_doNotThrowExceptions() {
        RuntimeException exception = new RuntimeException("boom");

        assertThatCode(() -> {
            AppLogger.trace(AppLoggerTest.class, "trace {}", "value");
            AppLogger.debug(AppLoggerTest.class, "debug {}", "value");
            AppLogger.info(AppLoggerTest.class, "info {}", "value");
            AppLogger.warn(AppLoggerTest.class, "warn {}", "value");
            AppLogger.warn(AppLoggerTest.class, "warn", exception);
            AppLogger.error(AppLoggerTest.class, "error {}", "value");
            AppLogger.error(AppLoggerTest.class, "error", exception);
            AppLogger.requestStarted(AppLoggerTest.class, "GET", "/users");
            AppLogger.requestCompleted(AppLoggerTest.class, "GET", "/users", 200, 15);
            AppLogger.requestFailed(AppLoggerTest.class, "GET", "/users", 15, exception);
        }).doesNotThrowAnyException();
    }
}
