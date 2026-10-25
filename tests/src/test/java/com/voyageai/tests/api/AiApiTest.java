package com.voyageai.tests.api;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.voyageai.tests.config.TestConfig;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Black-box REST API tests verifying AI endpoints against the running Voyage-AI backend.
 */
@DisplayName("Black-Box AI API Tests")
public class AiApiTest {

    private static final Logger log = LoggerFactory.getLogger(AiApiTest.class);

    private final HttpClient httpClient = HttpClient.newBuilder()
            .connectTimeout(Duration.ofSeconds(10))
            .build();

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Test
    @Tag("smoke")
    @DisplayName("GET /api/v1/ai/health returns 200 OK with model status OPERATIONAL and model gemini-3.5-flash-lite")
    void testAiHealthEndpoint() throws IOException, InterruptedException {
        String url = TestConfig.getBackendBaseUrl() + "/ai/health";
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .GET()
                .build();

        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

        assertThat(response.statusCode()).isEqualTo(200);
        JsonNode json = objectMapper.readTree(response.body());
        JsonNode data = json.has("data") ? json.get("data") : json;

        assertThat(data.get("status").asText()).isEqualTo("OPERATIONAL");
        assertThat(data.get("model").asText()).isEqualTo("gemini-3.5-flash-lite");
        assertThat(data.get("configured").asBoolean()).isTrue();
        assertThat(data.get("reachable").asBoolean()).isTrue();
    }

    @Test
    @Tag("smoke")
    @DisplayName("Mandatory Real Gemini API Test: POST /api/v1/ai/generate with 'What\\'s capital of India?' returns New Delhi")
    void testRealGeminiGenerationDirectApi() throws IOException, InterruptedException {
        String url = TestConfig.getBackendBaseUrl() + "/ai/generate";
        String payload = "{\"prompt\": \"What's capital of India?\"}";

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .header("Content-Type", "application/json")
                .header("Accept", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(payload))
                .timeout(Duration.ofSeconds(20))
                .build();

        long start = System.currentTimeMillis();
        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        long elapsed = System.currentTimeMillis() - start;

        assertThat(response.statusCode()).isEqualTo(200);
        JsonNode json = objectMapper.readTree(response.body());

        assertThat(json.get("success").asBoolean()).isTrue();
        JsonNode data = json.has("data") ? json.get("data") : json;
        assertThat(data.get("model").asText()).isEqualTo("gemini-3.5-flash-lite");

        String text = data.get("response").asText();
        assertThat(text).isNotBlank();
        assertThat(text).containsIgnoringCase("New Delhi");

        log.info("Direct AI API Success: status={}, model={}, latency={}ms, promptTokens={}, candidateTokens={}",
                response.statusCode(),
                data.get("model").asText(),
                elapsed,
                data.has("promptTokens") ? data.get("promptTokens").asInt() : "N/A",
                data.has("candidateTokens") ? data.get("candidateTokens").asInt() : "N/A"
        );
    }

    @Test
    @DisplayName("POST /api/v1/ai/generate with empty prompt returns 400 Bad Request")
    void testEmptyPromptReturns400() throws IOException, InterruptedException {
        String url = TestConfig.getBackendBaseUrl() + "/ai/generate";
        String payload = "{\"prompt\": \"   \"}";

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .header("Content-Type", "application/json")
                .header("Accept", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(payload))
                .build();

        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

        assertThat(response.statusCode()).isEqualTo(400);
        JsonNode json = objectMapper.readTree(response.body());
        assertThat(json.has("code")).isTrue();
    }
}
