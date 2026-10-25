package com.voyageai.integration.ai;

import com.voyageai.config.DotenvLoader;
import com.voyageai.integration.ai.dto.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientResponseException;

import java.time.Duration;

/**
 * Service implementation communicating with Google Gemini Developer API.
 * Uses exact model ID 'gemini-3.5-flash-lite' by default.
 * Enforces bounded timeouts, safe exception mapping, and data privacy without synthetic fallbacks.
 */
@Service
public class GoogleGeminiService implements GeminiService {

    private static final Logger log = LoggerFactory.getLogger(GoogleGeminiService.class);

    private final RestClient restClient;
    private final String apiKey;
    private final String model;
    private final String baseUrl;

    @Autowired
    public GoogleGeminiService(
            @Value("${gemini.api-key:}") String apiKey,
            @Value("${gemini.model:gemini-3.5-flash-lite}") String model,
            @Value("${gemini.base-url:https://generativelanguage.googleapis.com/v1beta}") String baseUrl,
            @Value("${gemini.connect-timeout-ms:5000}") int connectTimeoutMs,
            @Value("${gemini.read-timeout-ms:15000}") int readTimeoutMs) {
        String resolvedKey = DotenvLoader.cleanValue(apiKey);
        if (resolvedKey.isBlank()) {
            String sysPropKey = System.getProperty("gemini.api-key");
            if (sysPropKey != null && !sysPropKey.isBlank()) {
                resolvedKey = DotenvLoader.cleanValue(sysPropKey);
            }
        }
        if (resolvedKey.isBlank()) {
            String sysPropEnvKey = System.getProperty("GEMINI_API_KEY");
            if (sysPropEnvKey != null && !sysPropEnvKey.isBlank()) {
                resolvedKey = DotenvLoader.cleanValue(sysPropEnvKey);
            }
        }
        if (resolvedKey.isBlank()) {
            String envKey = System.getenv("GEMINI_API_KEY");
            if (envKey != null && !envKey.isBlank()) {
                resolvedKey = DotenvLoader.cleanValue(envKey);
            }
        }
        if (resolvedKey.isBlank()) {
            java.util.Map<String, Object> dotenv = DotenvLoader.load();
            Object keyObj = dotenv.get("GEMINI_API_KEY");
            if (keyObj != null && !keyObj.toString().isBlank()) {
                resolvedKey = DotenvLoader.cleanValue(keyObj.toString());
            }
        }
        this.apiKey = resolvedKey;
        this.model = (model == null || model.isBlank()) ? "gemini-3.5-flash-lite" : model.trim();
        this.baseUrl = (baseUrl == null || baseUrl.isBlank()) ? "https://generativelanguage.googleapis.com/v1beta" : baseUrl.trim();

        if (this.apiKey.isBlank()) {
            log.warn("Gemini API key is NOT configured [presence: FALSE, non-blank: FALSE]. Checked gemini.api-key, system properties, environment variables, and .env. AI features will report GEMINI_CONFIG_MISSING.");
        } else {
            log.info("Gemini API key is configured [presence: TRUE, non-blank: TRUE, length: {} chars, model: {}]. GoogleGeminiService initialized successfully.", this.apiKey.length(), this.model);
        }

        SimpleClientHttpRequestFactory factory = new SimpleClientHttpRequestFactory();
        factory.setConnectTimeout(Duration.ofMillis(connectTimeoutMs));
        factory.setReadTimeout(Duration.ofMillis(readTimeoutMs));

        this.restClient = RestClient.builder()
                .baseUrl(this.baseUrl)
                .requestFactory(factory)
                .build();
    }

    /**
     * Testing constructor allowing injection of a pre-configured RestClient.
     */
    public GoogleGeminiService(RestClient restClient, String apiKey, String model, String baseUrl) {
        this.restClient = restClient;
        this.apiKey = DotenvLoader.cleanValue(apiKey);
        this.model = (model == null || model.isBlank()) ? "gemini-3.5-flash-lite" : model.trim();
        this.baseUrl = (baseUrl == null || baseUrl.isBlank()) ? "https://generativelanguage.googleapis.com/v1beta" : baseUrl.trim();
    }

    @Override
    public AiGenerateResponse generate(AiGenerateRequest request) {
        if (apiKey == null || apiKey.isBlank()) {
            log.error("Gemini API call attempted but GEMINI_API_KEY is not configured.");
            throw new GeminiServiceException(
                    "Gemini API key is not configured. Please set GEMINI_API_KEY in environment or .env.",
                    HttpStatus.SERVICE_UNAVAILABLE,
                    "GEMINI_CONFIG_MISSING"
            );
        }

        if (request == null || request.getPrompt() == null || request.getPrompt().isBlank()) {
            throw new IllegalArgumentException("Prompt must not be empty or blank.");
        }

        GeminiApiRequest apiRequest = new GeminiApiRequest(request.getPrompt().trim());

        try {
            GeminiApiResponse response = restClient.post()
                    .uri(uriBuilder -> uriBuilder
                            .path("/models/{model}:generateContent")
                            .build(this.model))
                    .header("x-goog-api-key", this.apiKey)
                    .contentType(MediaType.APPLICATION_JSON)
                    .accept(MediaType.APPLICATION_JSON)
                    .body(apiRequest)
                    .retrieve()
                    .body(GeminiApiResponse.class);

            if (response == null || response.candidates() == null || response.candidates().isEmpty()) {
                log.warn("Gemini API returned empty candidates list for model: {}", this.model);
                throw new GeminiServiceException(
                        "Gemini API returned an empty candidate response.",
                        HttpStatus.SERVICE_UNAVAILABLE,
                        "GEMINI_EMPTY_RESPONSE"
                );
            }

            GeminiApiResponse.Candidate candidate = response.candidates().get(0);
            if (candidate.content() == null || candidate.content().parts() == null || candidate.content().parts().isEmpty()) {
                String finishReason = candidate.finishReason();
                log.warn("Gemini candidate content has no parts. Finish reason: {}", finishReason);
                throw new GeminiServiceException(
                        "Gemini generated response was empty (finish reason: " + (finishReason != null ? finishReason : "NONE") + ").",
                        HttpStatus.SERVICE_UNAVAILABLE,
                        "GEMINI_EMPTY_RESPONSE"
                );
            }

            String generatedText = candidate.content().parts().get(0).text();
            if (generatedText == null || generatedText.isBlank()) {
                throw new GeminiServiceException(
                        "Gemini generated response text is empty.",
                        HttpStatus.SERVICE_UNAVAILABLE,
                        "GEMINI_EMPTY_RESPONSE"
                );
            }

            Integer promptTokens = response.usageMetadata() != null ? response.usageMetadata().promptTokenCount() : null;
            Integer candidateTokens = response.usageMetadata() != null ? response.usageMetadata().candidatesTokenCount() : null;
            Integer totalTokens = response.usageMetadata() != null ? response.usageMetadata().totalTokenCount() : null;

            return new AiGenerateResponse(this.model, generatedText, true, promptTokens, candidateTokens, totalTokens);

        } catch (ResourceAccessException ex) {
            log.error("Network timeout or connection failure connecting to Gemini API at {}: {}", baseUrl, ex.getMessage());
            throw new GeminiServiceException(
                    "Gemini API request timed out or connection failed. Please try again later.",
                    ex,
                    HttpStatus.SERVICE_UNAVAILABLE,
                    "GEMINI_TIMEOUT"
            );
        } catch (RestClientResponseException ex) {
            int statusCode = ex.getStatusCode().value();
            log.error("Gemini API returned HTTP error status {}: {}", statusCode, ex.getStatusText());

            if (statusCode == 401 || statusCode == 403) {
                throw new GeminiServiceException(
                        "Gemini API authentication failed. The configured API key was rejected by Google AI Studio (HTTP " + statusCode + "). Please check your GEMINI_API_KEY.",
                        ex,
                        HttpStatus.UNAUTHORIZED,
                        "GEMINI_AUTHENTICATION_FAILED"
                );
            } else if (statusCode == 404) {
                throw new GeminiServiceException(
                        "Gemini model '" + this.model + "' was not found or is unsupported for API version v1beta (HTTP 404).",
                        ex,
                        HttpStatus.NOT_FOUND,
                        "GEMINI_MODEL_NOT_FOUND"
                );
            } else if (statusCode == 429) {
                throw new GeminiServiceException(
                        "Gemini API rate limit or quota exceeded. Please try again in a few moments.",
                        ex,
                        HttpStatus.TOO_MANY_REQUESTS,
                        "GEMINI_RATE_LIMITED"
                );
            } else if (statusCode >= 500) {
                throw new GeminiServiceException(
                        "Google Gemini service is temporarily unavailable (HTTP " + statusCode + "). Please try again later.",
                        ex,
                        HttpStatus.SERVICE_UNAVAILABLE,
                        "GEMINI_UPSTREAM_ERROR"
                );
            } else {
                throw new GeminiServiceException(
                        "Gemini API rejected request with status " + statusCode + ".",
                        ex,
                        HttpStatus.BAD_REQUEST,
                        "GEMINI_INVALID_REQUEST"
                );
            }
        }
    }

    @Override
    public AiHealthResponse checkHealth() {
        if (apiKey == null || apiKey.isBlank()) {
            return new AiHealthResponse(
                    "MISSING_CONFIGURATION",
                    this.model,
                    false,
                    false,
                    "GEMINI_API_KEY is not configured in environment or .env."
            );
        }

        try {
            GeminiApiRequest pingRequest = new GeminiApiRequest("ping");
            GeminiApiResponse response = restClient.post()
                    .uri(uriBuilder -> uriBuilder
                            .path("/models/{model}:generateContent")
                            .build(this.model))
                    .header("x-goog-api-key", this.apiKey)
                    .contentType(MediaType.APPLICATION_JSON)
                    .accept(MediaType.APPLICATION_JSON)
                    .body(pingRequest)
                    .retrieve()
                    .body(GeminiApiResponse.class);

            if (response != null && response.candidates() != null && !response.candidates().isEmpty()) {
                return new AiHealthResponse(
                        "OPERATIONAL",
                        this.model,
                        true,
                        true,
                        "Gemini API is reachable and responding successfully."
                );
            } else {
                return new AiHealthResponse(
                        "UNAVAILABLE",
                        this.model,
                        true,
                        false,
                        "Gemini API responded with an empty candidate payload."
                );
            }
        } catch (RestClientResponseException ex) {
            int status = ex.getStatusCode().value();
            String desc = (status == 401 || status == 403)
                    ? "Authentication failed: invalid GEMINI_API_KEY (HTTP " + status + ")."
                    : (status == 404)
                    ? "Model not found or unsupported: " + this.model + " (HTTP 404)."
                    : (status == 429) ? "Rate limit or quota exhausted (HTTP 429)." : "Upstream error (HTTP " + status + ").";
            return new AiHealthResponse(
                    "UNAVAILABLE",
                    this.model,
                    true,
                    false,
                    "Gemini API check failed: " + desc
            );
        } catch (Exception ex) {
            return new AiHealthResponse(
                    "UNAVAILABLE",
                    this.model,
                    true,
                    false,
                    "Gemini API unreachable: " + ex.getClass().getSimpleName()
            );
        }
    }

    public String getModel() {
        return model;
    }

    public String getBaseUrl() {
        return baseUrl;
    }

    public boolean isConfigured() {
        return apiKey != null && !apiKey.isBlank();
    }
}
