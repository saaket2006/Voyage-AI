package com.voyageai.config;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.env.EnvironmentPostProcessor;
import org.springframework.core.Ordered;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.MapPropertySource;

import java.util.Map;

/**
 * Spring Boot EnvironmentPostProcessor that injects local .env properties
 * into Spring's Environment early in the startup lifecycle.
 * Highest order number ensures lowest priority, allowing real system/cloud
 * environment variables to override local .env definitions.
 */
public class DotenvEnvironmentPostProcessor implements EnvironmentPostProcessor, Ordered {

    public static final String PROPERTY_SOURCE_NAME = "dotenvProperties";

    @Override
    public void postProcessEnvironment(ConfigurableEnvironment environment, SpringApplication application) {
        Map<String, Object> dotenvProps = DotenvLoader.load();
        if (!dotenvProps.isEmpty()) {
            // addLast ensures systemEnvironment and systemProperties have higher precedence
            environment.getPropertySources().addLast(new MapPropertySource(PROPERTY_SOURCE_NAME, dotenvProps));
        }
    }

    @Override
    public int getOrder() {
        return Ordered.LOWEST_PRECEDENCE;
    }
}
