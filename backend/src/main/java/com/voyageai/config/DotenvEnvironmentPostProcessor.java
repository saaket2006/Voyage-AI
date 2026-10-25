package com.voyageai.config;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.context.config.ConfigDataEnvironmentPostProcessor;
import org.springframework.boot.env.EnvironmentPostProcessor;
import org.springframework.core.Ordered;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.MapPropertySource;
import org.springframework.core.env.PropertySource;
import org.springframework.core.env.StandardEnvironment;

import java.util.HashMap;
import java.util.Map;

/**
 * Spring Boot EnvironmentPostProcessor that injects local .env properties
 * into Spring's Environment early in the startup lifecycle (before application.yml evaluation).
 * Allows real system environment variables to maintain precedence, while overriding blank/empty
 * environment variables and supplying relaxed property aliases.
 */
public class DotenvEnvironmentPostProcessor implements EnvironmentPostProcessor, Ordered {

    public static final String PROPERTY_SOURCE_NAME = "dotenvProperties";
    public static final String OVERRIDE_BLANK_SOURCE_NAME = "dotenvOverrideBlankProperties";

    // Run before ConfigDataEnvironmentPostProcessor evaluates application.yml
    public static final int ORDER = ConfigDataEnvironmentPostProcessor.ORDER - 1;

    @Override
    public void postProcessEnvironment(ConfigurableEnvironment environment, SpringApplication application) {
        Map<String, Object> dotenvProps = DotenvLoader.load();
        if (dotenvProps.isEmpty()) {
            return;
        }

        Map<String, Object> nonBlankProps = new HashMap<>();
        dotenvProps.forEach((k, v) -> {
            if (v != null && !v.toString().trim().isEmpty()) {
                String cleanVal = DotenvLoader.cleanValue(v.toString());
                nonBlankProps.put(k, cleanVal);

                // Populate standard Spring property aliases
                if ("GEMINI_API_KEY".equalsIgnoreCase(k)) {
                    nonBlankProps.put("gemini.api-key", cleanVal);
                    nonBlankProps.put("gemini.apiKey", cleanVal);
                    System.setProperty("gemini.api-key", cleanVal);
                    System.setProperty("GEMINI_API_KEY", cleanVal);
                } else if ("GEMINI_MODEL".equalsIgnoreCase(k)) {
                    nonBlankProps.put("gemini.model", cleanVal);
                }
            }
        });

        if (nonBlankProps.isEmpty()) {
            return;
        }

        PropertySource<?> sysEnv = environment.getPropertySources()
                .get(StandardEnvironment.SYSTEM_ENVIRONMENT_PROPERTY_SOURCE_NAME);

        if (sysEnv != null) {
            Map<String, Object> overrideBlankSysEnvs = new HashMap<>();
            nonBlankProps.forEach((k, v) -> {
                Object sysVal = sysEnv.getProperty(k);
                if (sysVal != null && sysVal.toString().trim().isEmpty()) {
                    overrideBlankSysEnvs.put(k, v);
                }
            });

            if (!overrideBlankSysEnvs.isEmpty()) {
                environment.getPropertySources().addBefore(
                        StandardEnvironment.SYSTEM_ENVIRONMENT_PROPERTY_SOURCE_NAME,
                        new MapPropertySource(OVERRIDE_BLANK_SOURCE_NAME, overrideBlankSysEnvs)
                );
            }

            environment.getPropertySources().addAfter(
                    StandardEnvironment.SYSTEM_ENVIRONMENT_PROPERTY_SOURCE_NAME,
                    new MapPropertySource(PROPERTY_SOURCE_NAME, nonBlankProps)
            );
        } else {
            environment.getPropertySources().addFirst(new MapPropertySource(PROPERTY_SOURCE_NAME, nonBlankProps));
        }
    }

    @Override
    public int getOrder() {
        return ORDER;
    }
}

