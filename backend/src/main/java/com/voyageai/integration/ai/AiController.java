package com.voyageai.integration.ai;

import com.voyageai.common.api.ApiResponse;
import com.voyageai.integration.ai.dto.AiGenerateRequest;
import com.voyageai.integration.ai.dto.AiGenerateResponse;
import com.voyageai.integration.ai.dto.AiHealthResponse;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * Controller exposing Google Gemini AI operations.
 */
@RestController
@RequestMapping("/api/v1/ai")
public class AiController {

    private final GeminiService geminiService;

    public AiController(GeminiService geminiService) {
        this.geminiService = geminiService;
    }

    /**
     * Generates AI responses from Google Gemini model (default: gemini-3.5-flash-lite).
     *
     * @param request Validated prompt payload
     * @return Standard ApiResponse enclosing generated text and metadata
     */
    @PostMapping("/generate")
    public ResponseEntity<ApiResponse<AiGenerateResponse>> generateContent(@Valid @RequestBody AiGenerateRequest request) {
        AiGenerateResponse response = geminiService.generate(request);
        return ResponseEntity.ok(ApiResponse.success(response, "Content generated successfully."));
    }

    /**
     * Dedicated health and connectivity check verifying whether the Gemini integration is operational.
     * Performs a minimal live verification when configured.
     *
     * @return Status payload distinguishing configured, reachable, missing, and unavailable states
     */
    @GetMapping("/health")
    public ResponseEntity<ApiResponse<AiHealthResponse>> checkHealth() {
        AiHealthResponse health = geminiService.checkHealth();
        if (health.isReachable()) {
            return ResponseEntity.ok(ApiResponse.success(health, "Gemini integration is operational."));
        } else {
            return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE)
                    .body(new ApiResponse<>(false, health.getMessage(), health));
        }
    }
}
