package com.voyageai.integration.ai;

import com.voyageai.config.DotenvLoader;
import com.voyageai.integration.ai.dto.AiGenerateRequest;
import com.voyageai.integration.ai.dto.AiGenerateResponse;
import com.voyageai.integration.ai.dto.AiHealthResponse;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.web.client.RestClient;

import java.time.Duration;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.fail;
import static org.junit.jupiter.api.Assumptions.assumeTrue;

/**
 * Real end-to-end integration test verifying actual Google Gemini Developer API connectivity.
 * Uses exact model ID 'gemini-3.5-flash-lite'.
 *
 * Execution Conventions:
 * - When run in standard 'mvn test', skips gracefully if GEMINI_API_KEY is missing.
 * - When explicitly invoked (e.g. 'mvn test -Dtest=GeminiApiIntegrationTest' or '-Dgemini.live.test=true'),
 *   fails if GEMINI_API_KEY is missing rather than falsely passing.
 */
@Tag("live-api")
@Tag("smoke")
@DisplayName("Real Google Gemini API Integration Tests")
class GeminiApiIntegrationTest {

    private static final Logger log = LoggerFactory.getLogger(GeminiApiIntegrationTest.class);

    private static final String DEFAULT_MODEL = "gemini-3.5-flash-lite";
    private static final String DEFAULT_BASE_URL = "https://generativelanguage.googleapis.com/v1beta";

    private static String apiKey;
    private static String model;
    private static boolean explicitlyInvoked;
    private static GoogleGeminiService liveGeminiService;

    @BeforeAll
    static void setUp() {
        // Detect whether the test was explicitly requested by name or flag
        String testProp = System.getProperty("test");
        explicitlyInvoked = (testProp != null && testProp.contains("GeminiApiIntegrationTest"))
                || Boolean.getBoolean("gemini.live.test")
                || "true".equalsIgnoreCase(System.getProperty("live.test"));

        // 1. Check system environment
        apiKey = System.getenv("GEMINI_API_KEY");

        // 2. Check system properties
        if (apiKey == null || apiKey.isBlank()) {
            apiKey = System.getProperty("GEMINI_API_KEY");
        }

        // 3. Check local .env file
        if (apiKey == null || apiKey.isBlank()) {
            Map<String, Object> dotenv = DotenvLoader.load();
            Object val = dotenv.get("GEMINI_API_KEY");
            if (val != null) {
                apiKey = val.toString();
            }
        }

        apiKey = DotenvLoader.cleanValue(apiKey);

        model = System.getenv("GEMINI_MODEL");
        if (model == null || model.isBlank()) {
            model = System.getProperty("GEMINI_MODEL", DEFAULT_MODEL);
        }
        model = model.trim();

        if (apiKey.isBlank()) {
            if (explicitlyInvoked) {
                fail("GEMINI_API_KEY is missing or blank, but GeminiApiIntegrationTest was explicitly invoked. "
                        + "Please configure a valid GEMINI_API_KEY in the environment or in .env.");
            } else {
                assumeTrue(false, "Skipping live Gemini API integration test: GEMINI_API_KEY is not configured.");
            }
            return;
        }

        // Build live RestClient with 15s timeout
        SimpleClientHttpRequestFactory factory = new SimpleClientHttpRequestFactory();
        factory.setConnectTimeout(Duration.ofSeconds(10));
        factory.setReadTimeout(Duration.ofSeconds(20));

        RestClient restClient = RestClient.builder()
                .baseUrl(DEFAULT_BASE_URL)
                .requestFactory(factory)
                .build();

        liveGeminiService = new GoogleGeminiService(restClient, apiKey, model, DEFAULT_BASE_URL);
    }

    @Test
    @DisplayName("Sends deterministic prompt to real Gemini API and receives non-empty text response")
    void liveApi_generateContent_success() {
        assumeTrue(liveGeminiService != null, "Live service uninitialized (missing API key)");

        long start = System.currentTimeMillis();
        AiGenerateRequest request = new AiGenerateRequest("Reply with 'VOYAGE_AI_VERIFIED' and nothing else.");
        AiGenerateResponse response = liveGeminiService.generate(request);
        long duration = System.currentTimeMillis() - start;

        assertThat(response).isNotNull();
        assertThat(response.isSuccess()).isTrue();
        assertThat(response.getModel()).isEqualTo(model);
        assertThat(response.getResponse())
                .isNotBlank()
                .describedAs("Gemini response text must not be empty");

        log.info("Live Gemini API Success: model={}, duration={}ms, promptTokens={}, candidateTokens={}, responseLength={}",
                response.getModel(), duration, response.getPromptTokens(), response.getCandidateTokens(), response.getResponse().length());
    }

    @Test
    @DisplayName("Executes live health connectivity check against configured Gemini model")
    void liveApi_checkHealth_operational() {
        assumeTrue(liveGeminiService != null, "Live service uninitialized (missing API key)");

        AiHealthResponse health = liveGeminiService.checkHealth();

        assertThat(health).isNotNull();
        assertThat(health.isConfigured()).isTrue();
        assertThat(health.isReachable()).isTrue();
        assertThat(health.getStatus()).isEqualTo("OPERATIONAL");
        assertThat(health.getModel()).isEqualTo(model);

        log.info("Live Gemini Health Check: status={}, model={}, message={}",
                health.getStatus(), health.getModel(), health.getMessage());
    }
}
