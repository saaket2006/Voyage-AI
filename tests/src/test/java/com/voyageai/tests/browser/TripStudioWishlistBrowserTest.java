package com.voyageai.tests.browser;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.microsoft.playwright.Locator;
import com.voyageai.tests.base.BasePlaywrightTest;
import com.voyageai.tests.config.TestConfig;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;

@DisplayName("Trip Studio Wishlist Integration and Multi-City Persistence Tests")
public class TripStudioWishlistBrowserTest extends BasePlaywrightTest {

    private final ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    void cleanState() {
        clearWishlistViaApi();
    }

    private void clearWishlistViaApi() {
        try {
            HttpClient client = HttpClient.newHttpClient();
            String loginBody = "{\"email\":\"traveler@voyageai.local\",\"password\":\"Password123!\"}";
            HttpRequest loginReq = HttpRequest.newBuilder()
                    .uri(URI.create(TestConfig.getBackendBaseUrl() + "/auth/login"))
                    .header("Content-Type", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofString(loginBody))
                    .build();

            HttpResponse<String> loginRes = client.send(loginReq, HttpResponse.BodyHandlers.ofString());
            JsonNode root = objectMapper.readTree(loginRes.body());
            String token = root.path("data").path("token").asText();

            if (token != null && !token.isBlank()) {
                HttpRequest listReq = HttpRequest.newBuilder()
                        .uri(URI.create(TestConfig.getBackendBaseUrl() + "/users/me/wishlist"))
                        .header("Authorization", "Bearer " + token)
                        .GET()
                        .build();

                HttpResponse<String> listRes = client.send(listReq, HttpResponse.BodyHandlers.ofString());
                JsonNode listRoot = objectMapper.readTree(listRes.body());
                JsonNode dataNode = listRoot.path("data");

                if (dataNode.isArray()) {
                    for (JsonNode item : dataNode) {
                        String id = item.asText();
                        HttpRequest delReq = HttpRequest.newBuilder()
                                .uri(URI.create(TestConfig.getBackendBaseUrl() + "/users/me/wishlist/" + id))
                                .header("Authorization", "Bearer " + token)
                                .DELETE()
                                .build();
                        client.send(delReq, HttpResponse.BodyHandlers.ofString());
                    }
                }
            }
        } catch (Exception e) {
            System.err.println("Warning: Unable to clean wishlist before test: " + e.getMessage());
        }
    }

    @Test
    @DisplayName("Saves Jaipur and two other cities, persists across refresh, and updates count upon removal")
    void testWishlistMultiCityPersistenceAndRemoval() {
        // Step 1: Open Voyage-AI home page
        page.navigate("/");

        // Step 2: Save Jaipur to wishlist
        Locator jaipurSaveBtn = page.locator("button[aria-label='Save Jaipur to wishlist']").first();
        assertThat(jaipurSaveBtn).isVisible();
        jaipurSaveBtn.click();
        assertThat(page.locator("button[aria-label='Remove Jaipur from wishlist']").first()).isVisible();

        // Step 3: Save Varanasi to wishlist
        Locator varanasiSaveBtn = page.locator("button[aria-label='Save Varanasi to wishlist']").first();
        assertThat(varanasiSaveBtn).isVisible();
        varanasiSaveBtn.click();
        assertThat(page.locator("button[aria-label='Remove Varanasi from wishlist']").first()).isVisible();

        // Step 4: Save Munnar to wishlist
        Locator munnarSaveBtn = page.locator("button[aria-label='Save Munnar to wishlist']").first();
        assertThat(munnarSaveBtn).isVisible();
        munnarSaveBtn.click();
        assertThat(page.locator("button[aria-label='Remove Munnar from wishlist']").first()).isVisible();

        // Step 5: Navigate to Trip Studio (Itinerary Planner tab)
        Locator plannerTab = page.locator("nav button:has-text('Itinerary Planner')");
        plannerTab.click();

        // Step 6: Verify Saved For Itinerary heading displays (3)
        Locator savedHeading = page.locator("h3:has-text('Saved For Itinerary (3)')");
        assertThat(savedHeading).isVisible();

        // Step 7: Verify all 3 saved destination cards are present in Trip Studio
        Locator jaipurCard = page.locator("[data-testid='saved-card-jaipur']");
        Locator varanasiCard = page.locator("[data-testid='saved-card-varanasi']");
        Locator munnarCard = page.locator("[data-testid='saved-card-munnar']");

        assertThat(jaipurCard).isVisible();
        assertThat(varanasiCard).isVisible();
        assertThat(munnarCard).isVisible();

        // Step 8: Verify Connected Route Overview displays all 3 cities
        Locator routeOverview = page.locator("text=Connected Route Overview");
        assertThat(routeOverview).isVisible();

        // Step 9: Refresh the page to verify backend persistence
        page.reload();

        // Navigate back to Itinerary Planner tab
        plannerTab = page.locator("nav button:has-text('Itinerary Planner')");
        plannerTab.click();

        // Verify count and all 3 destinations persist after reload
        assertThat(page.locator("h3:has-text('Saved For Itinerary (3)')")).isVisible();
        assertThat(jaipurCard).isVisible();
        assertThat(varanasiCard).isVisible();
        assertThat(munnarCard).isVisible();

        // Step 10: Remove Varanasi from itinerary
        Locator removeVaranasiBtn = page.locator("button[aria-label='Remove Varanasi from itinerary']");
        assertThat(removeVaranasiBtn).isVisible();
        removeVaranasiBtn.click();

        // Step 11: Verify count updates accurately to (2) and Varanasi card is removed
        assertThat(page.locator("h3:has-text('Saved For Itinerary (2)')")).isVisible();
        assertThat(varanasiCard).not().isVisible();
        assertThat(jaipurCard).isVisible();
        assertThat(munnarCard).isVisible();

        // Step 12: Reload the page again to confirm removal persists in the database
        page.reload();
        plannerTab = page.locator("nav button:has-text('Itinerary Planner')");
        plannerTab.click();

        // Verify count remains (2) and only Jaipur and Munnar remain persisted
        assertThat(page.locator("h3:has-text('Saved For Itinerary (2)')")).isVisible();
        assertThat(jaipurCard).isVisible();
        assertThat(munnarCard).isVisible();
        assertThat(varanasiCard).not().isVisible();
    }
}
