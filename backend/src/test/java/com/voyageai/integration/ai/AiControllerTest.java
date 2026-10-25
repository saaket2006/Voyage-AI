package com.voyageai.integration.ai;

import com.voyageai.integration.ai.dto.AiGenerateRequest;
import com.voyageai.integration.ai.dto.AiGenerateResponse;
import com.voyageai.integration.ai.dto.AiHealthResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class AiControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private GeminiService geminiService;

    @Test
    @DisplayName("POST /api/v1/ai/generate with valid prompt returns 200 OK and generation payload")
    void generate_validPrompt_returns200() throws Exception {
        AiGenerateResponse mockResponse = new AiGenerateResponse(
                "gemini-3.5-flash-lite",
                "Here is your itinerary recommendation.",
                true,
                10,
                20,
                30
        );

        when(geminiService.generate(any(AiGenerateRequest.class))).thenReturn(mockResponse);

        mockMvc.perform(post("/api/v1/ai/generate")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"prompt\":\"Plan a day in Jaipur\"}")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.model").value("gemini-3.5-flash-lite"))
                .andExpect(jsonPath("$.data.response").value("Here is your itinerary recommendation."))
                .andExpect(jsonPath("$.data.promptTokens").value(10))
                .andExpect(jsonPath("$.data.candidateTokens").value(20));
    }

    @Test
    @DisplayName("POST /api/v1/ai/generate with blank prompt returns 400 Bad Request with VALIDATION_ERROR")
    void generate_blankPrompt_returns400() throws Exception {
        mockMvc.perform(post("/api/v1/ai/generate")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"prompt\":\"   \"}")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value("VALIDATION_ERROR"))
                .andExpect(jsonPath("$.fieldErrors.prompt").exists());
    }

    @Test
    @DisplayName("POST /api/v1/ai/generate when key is missing returns 503 with GEMINI_CONFIG_MISSING")
    void generate_missingKey_returns503() throws Exception {
        when(geminiService.generate(any(AiGenerateRequest.class)))
                .thenThrow(new GeminiServiceException(
                        "Gemini API key is not configured.",
                        HttpStatus.SERVICE_UNAVAILABLE,
                        "GEMINI_CONFIG_MISSING"
                ));

        mockMvc.perform(post("/api/v1/ai/generate")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"prompt\":\"Tell me about Varanasi\"}")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isServiceUnavailable())
                .andExpect(jsonPath("$.code").value("GEMINI_CONFIG_MISSING"))
                .andExpect(jsonPath("$.message").value("Gemini API key is not configured."));
    }

    @Test
    @DisplayName("POST /api/v1/ai/generate when rate limited returns 429 with GEMINI_RATE_LIMITED")
    void generate_rateLimited_returns429() throws Exception {
        when(geminiService.generate(any(AiGenerateRequest.class)))
                .thenThrow(new GeminiServiceException(
                        "Gemini API rate limit or quota exceeded.",
                        HttpStatus.TOO_MANY_REQUESTS,
                        "GEMINI_RATE_LIMITED"
                ));

        mockMvc.perform(post("/api/v1/ai/generate")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"prompt\":\"Tell me about Munnar\"}")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isTooManyRequests())
                .andExpect(jsonPath("$.code").value("GEMINI_RATE_LIMITED"));
    }

    @Test
    @DisplayName("GET /api/v1/ai/health when operational returns 200 OK")
    void checkHealth_operational_returns200() throws Exception {
        AiHealthResponse health = new AiHealthResponse(
                "OPERATIONAL",
                "gemini-3.5-flash-lite",
                true,
                true,
                "Gemini API is reachable and responding successfully."
        );

        when(geminiService.checkHealth()).thenReturn(health);

        mockMvc.perform(get("/api/v1/ai/health")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.status").value("OPERATIONAL"))
                .andExpect(jsonPath("$.data.reachable").value(true));
    }

    @Test
    @DisplayName("GET /api/v1/ai/health when missing config returns 503 Service Unavailable")
    void checkHealth_missingConfig_returns503() throws Exception {
        AiHealthResponse health = new AiHealthResponse(
                "MISSING_CONFIGURATION",
                "gemini-3.5-flash-lite",
                false,
                false,
                "GEMINI_API_KEY is not configured in environment or .env."
        );

        when(geminiService.checkHealth()).thenReturn(health);

        mockMvc.perform(get("/api/v1/ai/health")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isServiceUnavailable())
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.data.status").value("MISSING_CONFIGURATION"))
                .andExpect(jsonPath("$.data.configured").value(false));
    }
}
