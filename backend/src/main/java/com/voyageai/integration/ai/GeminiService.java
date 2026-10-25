package com.voyageai.integration.ai;

import com.voyageai.integration.ai.dto.AiGenerateRequest;
import com.voyageai.integration.ai.dto.AiGenerateResponse;
import com.voyageai.integration.ai.dto.AiHealthResponse;

/**
 * Service interface for interacting with Google Gemini AI models.
 */
public interface GeminiService {

    /**
     * Generates text content given an input prompt using the configured Gemini model.
     *
     * @param request Validated prompt request
     * @return Generation response containing model, generated text, and token metrics
     */
    AiGenerateResponse generate(AiGenerateRequest request);

    /**
     * Performs a lightweight connectivity and health check against the configured Gemini API.
     *
     * @return AI connectivity status and configuration diagnostic information
     */
    AiHealthResponse checkHealth();
}
