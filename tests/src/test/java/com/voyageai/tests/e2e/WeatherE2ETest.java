package com.voyageai.tests.e2e;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.microsoft.playwright.APIRequestContext;
import com.microsoft.playwright.APIResponse;
import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.Route;
import com.microsoft.playwright.options.AriaRole;
import com.voyageai.tests.base.BasePlaywrightTest;
import com.voyageai.tests.config.TestConfig;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;
import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("Weather Integration & E2E Tests")
public class WeatherE2ETest extends BasePlaywrightTest {

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Test
    @DisplayName("Deterministic mock test: Intercepts weather API requests with clean mocked fixture")
    void testDeterministicWeatherMocking() {
        String mockWeatherJson = readFixture("fixtures/mock-weather.json");

        // Route interception with mocked fixture
        page.route("**/api/v1/weather*", route -> {
            route.fulfill(new Route.FulfillOptions()
                    .setStatus(200)
                    .setContentType("application/json")
                    .setBody(mockWeatherJson));
        });

        page.navigate("/");

        // Verify page renders cleanly with active mock interceptor
        assertThat(page.locator("text=Voyage-AI").first()).isVisible();
        assertThat(page.locator("text=Jaipur, Rajasthan").first()).isVisible();
    }

    @Test
    @DisplayName("Deterministic error test: Handles 503 weather provider error gracefully without app crash")
    void testDeterministicWeather503ErrorHandling() {
        String errorJson = "{" +
                "\"code\": \"WEATHER_PROVIDER_TIMEOUT\"," +
                "\"message\": \"Weather forecast service is currently unreachable due to network timeout.\"," +
                "\"timestamp\": \"2026-10-20T16:00:00Z\"" +
                "}";

        page.route("**/api/v1/weather*", route -> {
            route.fulfill(new Route.FulfillOptions()
                    .setStatus(503)
                    .setContentType("application/json")
                    .setBody(errorJson));
        });

        page.navigate("/");

        // Page must remain fully interactive even if weather provider fails
        assertThat(page.locator("text=Voyage-AI").first()).isVisible();
        Locator northBtn = page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("North India").setExact(true));
        northBtn.click();
        assertThat(page.locator("text=Jaipur, Rajasthan")).isVisible();
    }

    @Test
    @Tag("smoke")
    @DisplayName("@smoke: Real-service integration test calls local backend and verified Open-Meteo endpoint")
    void testRealServiceIntegrationSmoke() throws IOException {
        String backendBaseUrl = TestConfig.getBackendBaseUrl();

        APIRequestContext request = playwright.request().newContext();

        // 1. Verify backend health endpoint
        APIResponse healthResponse = request.get(backendBaseUrl + "/health");
        assertThat(healthResponse.ok()).isTrue();

        JsonNode healthJson = objectMapper.readTree(healthResponse.body());
        assertThat(healthJson.get("success").asBoolean()).isTrue();
        assertThat(healthJson.get("data").get("status").asText()).isEqualTo("UP");

        // 2. Call real weather endpoint for Jaipur (26.9124, 75.7873)
        APIResponse weatherResponse = request.get(backendBaseUrl + "/weather?latitude=26.9124&longitude=75.7873");
        assertThat(weatherResponse.status()).isEqualTo(200);

        JsonNode weatherJson = objectMapper.readTree(weatherResponse.body());
        assertThat(weatherJson.get("success").asBoolean()).isTrue();

        // Verify real Open-Meteo response structure
        JsonNode data = weatherJson.get("data");
        assertThat(data.get("latitude").asDouble()).isBetween(26.0, 27.5);
        assertThat(data.get("longitude").asDouble()).isBetween(75.0, 76.5);
        assertThat(data.get("timezone").asText()).isEqualTo("Asia/Kolkata");

        // Verify real current weather data
        JsonNode current = data.get("current");
        assertThat(current).isNotNull();
        assertThat(current.get("temperature").isNumber()).isTrue();
        assertThat(current.get("weatherCondition").asText()).isNotEmpty();

        // Verify daily forecast array
        JsonNode dailyForecast = data.get("dailyForecast");
        assertThat(dailyForecast.isArray()).isTrue();
        assertThat(dailyForecast.size()).isGreaterThanOrEqualTo(1);

        // Verify provenance points to Open-Meteo and NOT a fixture
        JsonNode provenance = data.get("provenance");
        assertThat(provenance).isNotNull();
        assertThat(provenance.get("sourceProvider").asText()).isEqualTo("Open-Meteo");
        assertThat(provenance.get("attribution").asText()).contains("Open-Meteo");
        assertThat(provenance.get("license").asText()).isEqualTo("CC-BY 4.0");
        assertThat(provenance.get("forecast").asBoolean()).isTrue();

        request.dispose();
    }
}
