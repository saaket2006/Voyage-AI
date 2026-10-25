package com.voyageai.tests.browser;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.Response;
import com.microsoft.playwright.Route;
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
        assertThat(page.locator("button:has-text('Ask AI')")).isVisible();
        assertThat(page.locator("button:has-text('Ask Gemini')")).hasCount(0);
    }

    @Test
    @DisplayName("Submitting empty prompt triggers validation warning in UI")
    void testEmptyPromptValidation() {
        page.navigate("/");
        page.locator("nav button:has-text('Itinerary Planner')").click();

        Locator askButton = page.locator("button:has-text('Ask AI')");
        // Ask button should be disabled when textarea is empty
        assertThat(askButton).isDisabled();
    }

    @Test
    @DisplayName("Verifies AI formatted Markdown response renders headings, lists, bold text, links, and tables without raw Markdown symbols")
    void testFormattedMarkdownRenderingInUi() {
        // Intercept /api/v1/ai/generate to supply rich Markdown for UI formatting validation with proper CORS headers
        page.route(url -> url.contains("/api/v1/ai/generate"), route -> {
            if ("OPTIONS".equalsIgnoreCase(route.request().method())) {
                route.fulfill(new Route.FulfillOptions()
                        .setStatus(200)
                        .setHeaders(java.util.Map.of(
                                "Access-Control-Allow-Origin", "*",
                                "Access-Control-Allow-Methods", "POST, OPTIONS",
                                "Access-Control-Allow-Headers", "*"
                        )));
            } else {
                String mockBody = "{"
                        + "\"success\": true,"
                        + "\"model\": \"gemini-3.5-flash-lite\","
                        + "\"response\": \"### Top Jaipur Destinations\\n\\nHere are **must-visit** highlights:\\n\\n* **Amer Fort** - Historic fort\\n* **Hawa Mahal** - Palace of Winds\\n\\nVisit the [Rajasthan Tourism](https://tourism.rajasthan.gov.in) portal for tickets.\\n\\n| Monument | Best Time |\\n|---|---|\\n| Amer Fort | Morning |\\n\\n`console.log(\\\"Voyage AI\\\");`\","
                        + "\"promptTokens\": 22,"
                        + "\"candidateTokens\": 48,"
                        + "\"totalTokens\": 70"
                        + "}";
                route.fulfill(new Route.FulfillOptions()
                        .setStatus(200)
                        .setContentType("application/json")
                        .setHeaders(java.util.Map.of(
                                "Access-Control-Allow-Origin", "*",
                                "Access-Control-Allow-Methods", "POST, OPTIONS",
                                "Access-Control-Allow-Headers", "*"
                        ))
                        .setBody(mockBody));
            }
        });

        page.navigate("/");
        page.locator("nav button:has-text('Itinerary Planner')").click();

        Locator textarea = page.locator("textarea[placeholder*='walking tour']");
        textarea.fill("Recommend top attractions in Jaipur");

        Locator askButton = page.locator("button:has-text('Ask AI')");
        assertThat(askButton).isEnabled();
        askButton.click();

        // 1. Verify loading state text is 'Thinking…' without provider branding
        Locator loadingIndicator = page.locator("button:has-text('Thinking…')");
        // Loading might resolve quickly with route fulfillment, verify response arrives
        Locator responseCard = page.locator("[data-testid='ai-response-content']");
        assertThat(responseCard).isVisible(new com.microsoft.playwright.assertions.LocatorAssertions.IsVisibleOptions().setTimeout(10000));

        // 2. Verify heading rendered as <h3> and contains no raw ###
        Locator heading = responseCard.locator("h3:has-text('Top Jaipur Destinations')");
        assertThat(heading).isVisible();
        assertThat(heading.innerText()).isEqualTo("Top Jaipur Destinations");
        assertThat(responseCard.innerText()).doesNotContain("###");

        // 3. Verify bold text rendered as <strong> and contains no raw **
        Locator boldMustVisit = responseCard.locator("strong:has-text('must-visit')");
        assertThat(boldMustVisit).isVisible();
        Locator boldAmer = responseCard.locator("strong:has-text('Amer Fort')");
        assertThat(boldAmer).isVisible();
        assertThat(responseCard.innerText()).doesNotContain("**must-visit**");

        // 4. Verify bullet list items rendered as <li> and contains no raw *
        Locator listItems = responseCard.locator("ul > li");
        assertThat(listItems).hasCount(2);
        assertThat(listItems.first()).containsText("Amer Fort - Historic fort");

        // 5. Verify external link rendered with security attributes
        Locator link = responseCard.locator("a:has-text('Rajasthan Tourism')");
        assertThat(link).isVisible();
        assertThat(link).hasAttribute("href", "https://tourism.rajasthan.gov.in");
        assertThat(link).hasAttribute("target", "_blank");
        assertThat(link).hasAttribute("rel", "noopener noreferrer");

        // 6. Verify table rendered
        Locator table = responseCard.locator("table");
        assertThat(table).isVisible();
        assertThat(table.locator("th:has-text('Monument')")).isVisible();
        assertThat(table.locator("td:has-text('Amer Fort')")).isVisible();

        // 7. Verify token metadata and copy button
        assertThat(page.locator("text=70 tokens")).isVisible();
        assertThat(page.locator("button:has-text('Copy')")).isVisible();
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

        Locator askButton = page.locator("button:has-text('Ask AI')");
        assertThat(askButton).isEnabled();

        // Track the real backend network response to verify genuine Gemini API round-trip
        AtomicReference<Response> apiResponseRef = new AtomicReference<>();
        page.onResponse(response -> {
            if (response.url().contains("/api/v1/ai/generate") && response.request().method().equalsIgnoreCase("POST")) {
                apiResponseRef.set(response);
            }
        });

        long startTime = System.currentTimeMillis();

        // Click Ask AI
        askButton.click();

        // Verify button indicates progress with de-branded text
        assertThat(page.locator("text=Thinking…")).isVisible();

        // Wait for generation to complete and response to appear in UI (up to 25s for live API)
        Locator responseContainer = page.locator("[data-testid='ai-response-content']");
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
