package com.voyageai.integration.ai;

import com.voyageai.integration.ai.dto.AiGenerateRequest;
import com.voyageai.integration.ai.dto.AiGenerateResponse;
import com.voyageai.integration.ai.dto.AiHealthResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.web.client.RestClient;

import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.*;
import static org.springframework.test.web.client.response.MockRestResponseCreators.*;

class GeminiServiceTest {

    private static final String BASE_URL = "https://generativelanguage.googleapis.com/v1beta";
    private static final String MODEL = "gemini-3.5-flash-lite";
    private static final String TEST_API_KEY = "test-mock-api-key-12345";

    private MockRestServiceServer mockServer;
    private GoogleGeminiService geminiService;

    @BeforeEach
    void setUp() {
        RestClient.Builder builder = RestClient.builder().baseUrl(BASE_URL);
        this.mockServer = MockRestServiceServer.bindTo(builder).build();
        RestClient restClient = builder.build();
        this.geminiService = new GoogleGeminiService(restClient, TEST_API_KEY, MODEL, BASE_URL);
    }

    @Test
    @DisplayName("Successfully parses Google Gemini response with model and token counts")
    void generate_success() {
        String mockJsonResponse = """
            {
              "candidates": [
                {
                  "content": {
                    "parts": [
                      {
                        "text": "Jaipur offers remarkable heritage sites including Hawa Mahal and Amber Fort."
                      }
                    ],
                    "role": "model"
                  },
                  "finishReason": "STOP",
                  "index": 0
                }
              ],
              "usageMetadata": {
                "promptTokenCount": 14,
                "candidatesTokenCount": 18,
                "totalTokenCount": 32
              },
              "modelVersion": "gemini-3.5-flash-lite-2026"
            }
            """;

        mockServer.expect(requestTo(BASE_URL + "/models/" + MODEL + ":generateContent"))
                .andExpect(method(HttpMethod.POST))
                .andExpect(header("x-goog-api-key", TEST_API_KEY))
                .andExpect(header("Content-Type", MediaType.APPLICATION_JSON_VALUE))
                .andExpect(content().json("{\"contents\":[{\"parts\":[{\"text\":\"Recommend attractions in Jaipur\"}]}]}"))
                .andRespond(withSuccess(mockJsonResponse, MediaType.APPLICATION_JSON));

        AiGenerateResponse response = geminiService.generate(new AiGenerateRequest("Recommend attractions in Jaipur"));

        mockServer.verify();

        assertThat(response).isNotNull();
        assertThat(response.getModel()).isEqualTo(MODEL);
        assertThat(response.getResponse()).isEqualTo("Jaipur offers remarkable heritage sites including Hawa Mahal and Amber Fort.");
        assertThat(response.isSuccess()).isTrue();
        assertThat(response.getPromptTokens()).isEqualTo(14);
        assertThat(response.getCandidateTokens()).isEqualTo(18);
        assertThat(response.getTotalTokens()).isEqualTo(32);
    }

    @Test
    @DisplayName("Rejects null or blank prompts with IllegalArgumentException")
    void generate_emptyPrompt_throwsIllegalArgumentException() {
        assertThatThrownBy(() -> geminiService.generate(new AiGenerateRequest("")))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Prompt must not be empty or blank");

        assertThatThrownBy(() -> geminiService.generate(new AiGenerateRequest("   ")))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Prompt must not be empty or blank");

        assertThatThrownBy(() -> geminiService.generate(null))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("Throws GeminiServiceException with GEMINI_CONFIG_MISSING when API key is missing or blank")
    void generate_missingApiKey_throwsConfigMissing() {
        RestClient.Builder builder = RestClient.builder().baseUrl(BASE_URL);
        GoogleGeminiService unconfiguredService = new GoogleGeminiService(builder.build(), "", MODEL, BASE_URL);

        assertThatThrownBy(() -> unconfiguredService.generate(new AiGenerateRequest("Hello")))
                .isInstanceOf(GeminiServiceException.class)
                .satisfies(ex -> {
                    GeminiServiceException gse = (GeminiServiceException) ex;
                    assertThat(gse.getStatus()).isEqualTo(HttpStatus.SERVICE_UNAVAILABLE);
                    assertThat(gse.getErrorCode()).isEqualTo("GEMINI_CONFIG_MISSING");
                    assertThat(gse.getMessage()).contains("GEMINI_API_KEY");
                });
    }

    @Test
    @DisplayName("Throws GeminiServiceException with GEMINI_EMPTY_RESPONSE when candidates list is null or empty")
    void generate_emptyCandidates_throwsEmptyResponse() {
        mockServer.expect(requestTo(BASE_URL + "/models/" + MODEL + ":generateContent"))
                .andExpect(method(HttpMethod.POST))
                .andRespond(withSuccess("{\"candidates\":[]}", MediaType.APPLICATION_JSON));

        assertThatThrownBy(() -> geminiService.generate(new AiGenerateRequest("Hello")))
                .isInstanceOf(GeminiServiceException.class)
                .satisfies(ex -> {
                    GeminiServiceException gse = (GeminiServiceException) ex;
                    assertThat(gse.getErrorCode()).isEqualTo("GEMINI_EMPTY_RESPONSE");
                });

        mockServer.verify();
    }

    @Test
    @DisplayName("Throws GeminiServiceException with GEMINI_AUTHENTICATION_FAILED on 401 Unauthorized")
    void generate_upstream401_throwsAuthFailed() {
        mockServer.expect(requestTo(BASE_URL + "/models/" + MODEL + ":generateContent"))
                .andExpect(method(HttpMethod.POST))
                .andRespond(withUnauthorizedRequest());

        assertThatThrownBy(() -> geminiService.generate(new AiGenerateRequest("Hello")))
                .isInstanceOf(GeminiServiceException.class)
                .satisfies(ex -> {
                    GeminiServiceException gse = (GeminiServiceException) ex;
                    assertThat(gse.getStatus()).isEqualTo(HttpStatus.UNAUTHORIZED);
                    assertThat(gse.getErrorCode()).isEqualTo("GEMINI_AUTHENTICATION_FAILED");
                    assertThat(gse.getMessage()).doesNotContain(TEST_API_KEY);
                });

        mockServer.verify();
    }

    @Test
    @DisplayName("Throws GeminiServiceException with GEMINI_AUTHENTICATION_FAILED on 403 Forbidden")
    void generate_upstream403_throwsAuthFailed() {
        mockServer.expect(requestTo(BASE_URL + "/models/" + MODEL + ":generateContent"))
                .andExpect(method(HttpMethod.POST))
                .andRespond(withRawStatus(403));

        assertThatThrownBy(() -> geminiService.generate(new AiGenerateRequest("Hello")))
                .isInstanceOf(GeminiServiceException.class)
                .satisfies(ex -> {
                    GeminiServiceException gse = (GeminiServiceException) ex;
                    assertThat(gse.getStatus()).isEqualTo(HttpStatus.UNAUTHORIZED);
                    assertThat(gse.getErrorCode()).isEqualTo("GEMINI_AUTHENTICATION_FAILED");
                });

        mockServer.verify();
    }

    @Test
    @DisplayName("Throws GeminiServiceException with GEMINI_RATE_LIMITED on 429 Too Many Requests")
    void generate_upstream429_throwsRateLimited() {
        mockServer.expect(requestTo(BASE_URL + "/models/" + MODEL + ":generateContent"))
                .andExpect(method(HttpMethod.POST))
                .andRespond(withRawStatus(429));

        assertThatThrownBy(() -> geminiService.generate(new AiGenerateRequest("Hello")))
                .isInstanceOf(GeminiServiceException.class)
                .satisfies(ex -> {
                    GeminiServiceException gse = (GeminiServiceException) ex;
                    assertThat(gse.getStatus()).isEqualTo(HttpStatus.TOO_MANY_REQUESTS);
                    assertThat(gse.getErrorCode()).isEqualTo("GEMINI_RATE_LIMITED");
                });

        mockServer.verify();
    }

    @Test
    @DisplayName("Throws GeminiServiceException with GEMINI_UPSTREAM_ERROR on 500 Server Error")
    void generate_upstream500_throwsUpstreamError() {
        mockServer.expect(requestTo(BASE_URL + "/models/" + MODEL + ":generateContent"))
                .andExpect(method(HttpMethod.POST))
                .andRespond(withServerError());

        assertThatThrownBy(() -> geminiService.generate(new AiGenerateRequest("Hello")))
                .isInstanceOf(GeminiServiceException.class)
                .satisfies(ex -> {
                    GeminiServiceException gse = (GeminiServiceException) ex;
                    assertThat(gse.getStatus()).isEqualTo(HttpStatus.SERVICE_UNAVAILABLE);
                    assertThat(gse.getErrorCode()).isEqualTo("GEMINI_UPSTREAM_ERROR");
                });

        mockServer.verify();
    }

    @Test
    @DisplayName("Throws GeminiServiceException with GEMINI_TIMEOUT when connection or read times out")
    void generate_timeout_throwsGeminiTimeout() {
        mockServer.expect(requestTo(BASE_URL + "/models/" + MODEL + ":generateContent"))
                .andExpect(method(HttpMethod.POST))
                .andRespond(withException(new IOException("Read timed out")));

        assertThatThrownBy(() -> geminiService.generate(new AiGenerateRequest("Hello")))
                .isInstanceOf(GeminiServiceException.class)
                .satisfies(ex -> {
                    GeminiServiceException gse = (GeminiServiceException) ex;
                    assertThat(gse.getStatus()).isEqualTo(HttpStatus.SERVICE_UNAVAILABLE);
                    assertThat(gse.getErrorCode()).isEqualTo("GEMINI_TIMEOUT");
                });

        mockServer.verify();
    }

    @Test
    @DisplayName("checkHealth returns OPERATIONAL when Gemini responds successfully")
    void checkHealth_operational() {
        String mockPingResponse = """
            {
              "candidates": [
                {
                  "content": {
                    "parts": [{"text": "pong"}]
                  }
                }
              ]
            }
            """;

        mockServer.expect(requestTo(BASE_URL + "/models/" + MODEL + ":generateContent"))
                .andExpect(method(HttpMethod.POST))
                .andExpect(header("x-goog-api-key", TEST_API_KEY))
                .andRespond(withSuccess(mockPingResponse, MediaType.APPLICATION_JSON));

        AiHealthResponse health = geminiService.checkHealth();

        mockServer.verify();

        assertThat(health).isNotNull();
        assertThat(health.getStatus()).isEqualTo("OPERATIONAL");
        assertThat(health.isConfigured()).isTrue();
        assertThat(health.isReachable()).isTrue();
        assertThat(health.getModel()).isEqualTo(MODEL);
    }

    @Test
    @DisplayName("checkHealth returns MISSING_CONFIGURATION when API key is blank")
    void checkHealth_missingConfig() {
        RestClient.Builder builder = RestClient.builder().baseUrl(BASE_URL);
        GoogleGeminiService unconfiguredService = new GoogleGeminiService(builder.build(), "", MODEL, BASE_URL);

        AiHealthResponse health = unconfiguredService.checkHealth();

        assertThat(health).isNotNull();
        assertThat(health.getStatus()).isEqualTo("MISSING_CONFIGURATION");
        assertThat(health.isConfigured()).isFalse();
        assertThat(health.isReachable()).isFalse();
    }

    @Test
    @DisplayName("checkHealth returns UNAVAILABLE when Gemini rejects ping request")
    void checkHealth_upstreamFailure() {
        mockServer.expect(requestTo(BASE_URL + "/models/" + MODEL + ":generateContent"))
                .andExpect(method(HttpMethod.POST))
                .andRespond(withServerError());

        AiHealthResponse health = geminiService.checkHealth();

        mockServer.verify();

        assertThat(health).isNotNull();
        assertThat(health.getStatus()).isEqualTo("UNAVAILABLE");
        assertThat(health.isConfigured()).isTrue();
        assertThat(health.isReachable()).isFalse();
    }
}
