package com.voyageai.tests.config;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * Centralized test configuration loader.
 * Checks System properties first, then Environment variables, then test.properties defaults.
 */
public final class TestConfig {

    private static final Properties PROPERTIES = new Properties();

    static {
        try (InputStream is = TestConfig.class.getClassLoader().getResourceAsStream("test.properties")) {
            if (is != null) {
                PROPERTIES.load(is);
            }
        } catch (IOException e) {
            System.err.println("Warning: Unable to load test.properties from classpath: " + e.getMessage());
        }
    }

    private TestConfig() {}

    public static String get(String key, String defaultValue) {
        String sysProp = System.getProperty(key);
        if (sysProp != null && !sysProp.isBlank()) {
            return sysProp.trim();
        }

        String envKey = key.replace('.', '_').toUpperCase();
        String envVal = System.getenv(envKey);
        if (envVal != null && !envVal.isBlank()) {
            return envVal.trim();
        }

        return PROPERTIES.getProperty(key, defaultValue);
    }

    public static String getFrontendBaseUrl() {
        return get("frontend.base.url", "http://localhost:5173");
    }

    public static String getBackendBaseUrl() {
        return get("backend.base.url", "http://localhost:8081/api/v1");
    }

    public static String getActuatorUrl() {
        return get("backend.actuator.url", "http://localhost:8081/actuator");
    }

    public static boolean isHeadless() {
        return Boolean.parseBoolean(get("playwright.headless", "true"));
    }

    public static int getTimeoutMs() {
        return Integer.parseInt(get("playwright.timeout", "30000"));
    }
}
