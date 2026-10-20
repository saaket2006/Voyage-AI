package com.voyageai.tests.api;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.voyageai.tests.config.TestConfig;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("Black-Box Health API Tests")
public class HealthApiTest {

    private final HttpClient httpClient = HttpClient.newBuilder()
            .connectTimeout(Duration.ofSeconds(5))
            .build();

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Test
    @DisplayName("GET /api/v1/health returns 200 OK with UP status")
    void testCustomHealthEndpoint() throws IOException, InterruptedException {
        String url = TestConfig.getBackendBaseUrl() + "/health";
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .GET()
                .build();

        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

        assertThat(response.statusCode()).isEqualTo(200);
        assertThat(response.headers().firstValue("content-type")).isPresent()
                .hasValueSatisfying(ct -> assertThat(ct).contains("application/json"));

        JsonNode json = objectMapper.readTree(response.body());
        assertThat(json.get("success").asBoolean()).isTrue();
        assertThat(json.get("message").asText()).isEqualTo("Service is healthy and responding.");
        assertThat(json.get("data").get("status").asText()).isEqualTo("UP");
        assertThat(json.get("data").get("service").asText()).isEqualTo("voyage-ai-backend");
        assertThat(json.get("timestamp")).isNotNull();
    }

    @Test
    @DisplayName("GET /actuator/health returns 200 OK with UP status")
    void testActuatorHealthEndpoint() throws IOException, InterruptedException {
        String url = TestConfig.getActuatorUrl() + "/health";
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .GET()
                .build();

        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

        assertThat(response.statusCode()).isEqualTo(200);
        JsonNode json = objectMapper.readTree(response.body());
        assertThat(json.get("status").asText()).isEqualTo("UP");
    }
}
