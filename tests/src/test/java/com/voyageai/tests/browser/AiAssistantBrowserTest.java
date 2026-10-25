package com.voyageai.tests.browser;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.Response;
import com.microsoft.playwright.options.AriaRole;
import com.voyageai.tests.base.BasePlaywrightTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.concurrent.atomic.AtomicReference;
import java.util.regex.Pattern;

import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;
import static org.assertj.core.api.Assertions.assertThat;

/**
 * End-to-end browser automation tests for the AI Travel Concierge component.
 * Verifies tab navigation, prompt submission, validation handling, and real live
 * Gemini generation with model gemini-3.5-flash-lite through the browser UI.
 */
@DisplayName("AI Assistant Browser UI Tests")
public class AiAssistantBrowserTest extends BasePlaywrightTest {

    private static final Logger log = LoggerFactory.getLogger(AiAssistantBrowserTest.class);
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Test
    @DisplayName("Navigates to Itinerary Planner tab and displays AI Concierge interface with model details")
    void testNavigateToPlannerTabAndVerifyAiInterface() {
        page.navigate("/");

        // Click Itinerary Planner tab in navbar
        Locator plannerTab = page.locator("nav button:has-text('Itinerary Planner')");
        plannerTab.click();

        // Verify AI Travel Concierge card is rendered
        assertThat(page.locator("text=AI Travel Concierge")).isVisible();
        assertThat(page.locator("text=Curated Travel Intelligence")).isVisible();
        assertThat(page.locator("text=gemini-3.5-flash-lite")).isVisible();
        assertThat(page.locator("textarea[placeholder*='walking tour']")).isVisible();
        assertThat(page.locator("button:has-text('Ask Gemini')")).isVisible();
    }

    @Test
    @DisplayName("Submitting empty prompt triggers validation warning in UI")
    void testEmptyPromptValidation() {
        page.navigate("/");
        page.locator("nav button:has-text('Itinerary Planner')").click();

        Locator askButton = page.locator("button:has-text('Ask Gemini')");
        // Ask button should be disabled when textarea is empty
        assertThat(askButton).isDisabled();
    }

    @Test
    @Tag("smoke")
    @DisplayName("Mandatory Real Gemini API Test: Submits 'What's capital of India?' via UI and receives New Delhi from Gemini")
    void testRealGeminiApiGenerationViaBrowser() throws IOException {
        page.navigate("/");
        page.locator("nav button:has-text('Itinerary Planner')").click();

        Locator textarea = page.locator("textarea[placeholder*='walking tour']");
        assertThat(textarea).isVisible();

        String prompt = "What's capital of India?";
        textarea.fill(prompt);

        Locator askButton = page.locator("button:has-text('Ask Gemini')");
        assertThat(askButton).isEnabled();

        // Track the real backend network response to verify genuine Gemini API round-trip
        AtomicReference<Response> apiResponseRef = new AtomicReference<>();
        page.onResponse(response -> {
            if (response.url().contains("/api/v1/ai/generate") && response.request().method().equalsIgnoreCase("POST")) {
                apiResponseRef.set(response);
            }
        });

        long startTime = System.currentTimeMillis();

        // Click Ask Gemini
        askButton.click();

        // Verify button indicates progress
        assertThat(page.locator("text=Thinking with Gemini...")).isVisible();

        // Wait for generation to complete and response to appear in UI (up to 25s for live API)
        Locator responseContainer = page.locator("div.whitespace-pre-wrap");
        assertThat(responseContainer).isVisible(new com.microsoft.playwright.assertions.LocatorAssertions.IsVisibleOptions().setTimeout(25000));

        long durationMs = System.currentTimeMillis() - startTime;
        String generatedText = responseContainer.innerText();

        // 1. Verify text contains "New Delhi"
        assertThat(generatedText)
                .as("Generated answer must contain 'New Delhi'")
                .containsIgnoringCase("New Delhi");

        // 2. Verify network response from backend
        Response networkResponse = apiResponseRef.get();
        assertThat(networkResponse).as("Backend /api/v1/ai/generate response must be received").isNotNull();
        assertThat(networkResponse.status()).isEqualTo(200);

        JsonNode jsonResponse = objectMapper.readTree(networkResponse.body());
        assertThat(jsonResponse.get("success").asBoolean()).isTrue();
        JsonNode dataNode = (jsonResponse.has("data") && !jsonResponse.get("data").isNull())
                ? jsonResponse.get("data")
                : jsonResponse;
        assertThat(dataNode.get("model").asText()).isEqualTo("gemini-3.5-flash-lite");

        String apiText = dataNode.get("response").asText();
        assertThat(apiText).containsIgnoringCase("New Delhi");

        // 3. Verify model badge in UI
        assertThat(page.locator("span:has-text('gemini-3.5-flash-lite')").first()).isVisible();

        log.info("Browser Real Gemini Generation Success: HTTP {}, model={}, duration={}ms, promptTokens={}, candidateTokens={}, responseSummary='{}'",
                networkResponse.status(),
                dataNode.get("model").asText(),
                durationMs,
                dataNode.has("promptTokens") ? dataNode.get("promptTokens").asInt() : "N/A",
                dataNode.has("candidateTokens") ? dataNode.get("candidateTokens").asInt() : "N/A",
                generatedText.length() > 60 ? generatedText.substring(0, 60) + "..." : generatedText
        );
    }
}
