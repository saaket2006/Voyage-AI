package com.voyageai.tests.api;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.voyageai.tests.config.TestConfig;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("Black-Box Weather API Tests")
public class WeatherApiTest {

    private final HttpClient httpClient = HttpClient.newBuilder()
            .connectTimeout(Duration.ofSeconds(5))
            .build();

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Test
    @Tag("smoke")
    @DisplayName("@smoke: GET /api/v1/weather with valid coordinates returns 200 OK and real forecast")
    void testLiveWeatherEndpoint() throws IOException, InterruptedException {
        String url = TestConfig.getBackendBaseUrl() + "/weather?latitude=26.9124&longitude=75.7873";
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .GET()
                .build();

        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

        assertThat(response.statusCode()).isEqualTo(200);
        JsonNode json = objectMapper.readTree(response.body());
        assertThat(json.get("success").asBoolean()).isTrue();

        JsonNode data = json.get("data");
        assertThat(data.get("latitude").asDouble()).isBetween(26.0, 27.5);
        assertThat(data.get("longitude").asDouble()).isBetween(75.0, 76.5);
        assertThat(data.get("timezone").asText()).isEqualTo("Asia/Kolkata");

        // Verify provenance
        JsonNode provenance = data.get("provenance");
        assertThat(provenance.get("sourceProvider").asText()).isEqualTo("Open-Meteo");
        assertThat(provenance.get("attribution").asText()).contains("Open-Meteo");
    }

    @Test
    @DisplayName("GET /api/v1/weather with missing coordinates returns 400 Bad Request")
    void testMissingCoordinatesReturns400() throws IOException, InterruptedException {
        String url = TestConfig.getBackendBaseUrl() + "/weather";
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .GET()
                .build();

        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

        assertThat(response.statusCode()).isEqualTo(400);
        JsonNode json = objectMapper.readTree(response.body());
        assertThat(json.get("code").asText()).isEqualTo("MISSING_PARAMETER");
    }

    @Test
    @DisplayName("GET /api/v1/weather with out-of-range coordinates returns 400 Bad Request")
    void testOutOfRangeCoordinatesReturns400() throws IOException, InterruptedException {
        String url = TestConfig.getBackendBaseUrl() + "/weather?latitude=999.0&longitude=75.0";
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .GET()
                .build();

        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

        assertThat(response.statusCode()).isEqualTo(400);
        JsonNode json = objectMapper.readTree(response.body());
        assertThat(json.get("code").asText()).isEqualTo("INVALID_ARGUMENT");
    }
}
