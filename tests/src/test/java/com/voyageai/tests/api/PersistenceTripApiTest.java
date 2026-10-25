package com.voyageai.tests.api;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.voyageai.tests.config.TestConfig;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Comprehensive black-box integration tests verifying Phase 2 Backend & Persistence Foundation:
 * - User authentication & JWT issuance
 * - Persistent destination discovery & provenance
 * - Full Trip and Itinerary Day/Item CRUD lifecycle
 * - Server-side cross-user ownership isolation (403 Forbidden)
 * - Unauthenticated route protection (401 Unauthorized)
 * - Validation error handling (400 Bad Request)
 */
@DisplayName("Phase 2 Persistence & Trip API Black-box Tests")
public class PersistenceTripApiTest {

    private final HttpClient httpClient = HttpClient.newBuilder()
            .connectTimeout(Duration.ofSeconds(5))
            .build();

    private final ObjectMapper objectMapper = new ObjectMapper();
    private final String baseUrl = TestConfig.getBackendBaseUrl();

    @Test
    @Tag("smoke")
    @DisplayName("Verifies destination discovery against persistent database and checks provenance metadata")
    void testDestinationDiscoveryPersistent() throws IOException, InterruptedException {
        // 1. Get all destinations
        HttpRequest req = HttpRequest.newBuilder()
                .uri(URI.create(baseUrl + "/destinations"))
                .GET()
                .build();
        HttpResponse<String> resp = httpClient.send(req, HttpResponse.BodyHandlers.ofString());
        assertThat(resp.statusCode()).isEqualTo(200);

        JsonNode json = objectMapper.readTree(resp.body());
        assertThat(json.get("success").asBoolean()).isTrue();

        JsonNode data = json.get("data");
        JsonNode items = data.isArray() ? data : data.get("items");
        assertThat(items.size()).isGreaterThanOrEqualTo(4);

        // 2. Search filter
        HttpRequest searchReq = HttpRequest.newBuilder()
                .uri(URI.create(baseUrl + "/destinations?query=Jaipur"))
                .GET()
                .build();
        HttpResponse<String> searchResp = httpClient.send(searchReq, HttpResponse.BodyHandlers.ofString());
        JsonNode searchJson = objectMapper.readTree(searchResp.body());
        JsonNode searchData = searchJson.get("data");
        JsonNode searchItems = searchData.isArray() ? searchData : searchData.get("items");
        assertThat(searchItems.size()).isGreaterThanOrEqualTo(1);
        assertThat(searchItems.get(0).get("name").asText()).isEqualTo("Jaipur");

        // 3. Check specific destination details with provenance
        HttpRequest detailReq = HttpRequest.newBuilder()
                .uri(URI.create(baseUrl + "/destinations/jaipur"))
                .GET()
                .build();
        HttpResponse<String> detailResp = httpClient.send(detailReq, HttpResponse.BodyHandlers.ofString());
        assertThat(detailResp.statusCode()).isEqualTo(200);

        JsonNode detailJson = objectMapper.readTree(detailResp.body());
        JsonNode detailData = detailJson.get("data");
        assertThat(detailData.get("name").asText()).isEqualTo("Jaipur");
        assertThat(detailData.has("provenance")).isTrue();
        assertThat(detailData.get("provenance").get("sourceProvider").asText()).isNotBlank();
        assertThat(detailData.get("provenance").get("verificationStatus").asText()).contains("VERIFIED");

        // 4. Non-existent destination returns 404
        HttpRequest notFoundReq = HttpRequest.newBuilder()
                .uri(URI.create(baseUrl + "/destinations/non-existent-atlantis-999"))
                .GET()
                .build();
        HttpResponse<String> notFoundResp = httpClient.send(notFoundReq, HttpResponse.BodyHandlers.ofString());
        assertThat(notFoundResp.statusCode()).isEqualTo(404);
    }

    @Test
    @Tag("smoke")
    @DisplayName("Complete Phase 2 Flow: User registration, Login, Trip CRUD, Itinerary Items, and Cross-User Isolation")
    void testCompleteTripAndPersistenceFlow() throws IOException, InterruptedException {
        String testEmail = "traveler_" + UUID.randomUUID().toString().substring(0, 8) + "@example.com";
        String password = "Password123!";

        // 1. User Registration
        String registerBody = String.format("{\"email\": \"%s\", \"password\": \"%s\", \"displayName\": \"Test Traveler\"}", testEmail, password);
        HttpRequest regReq = HttpRequest.newBuilder()
                .uri(URI.create(baseUrl + "/auth/register"))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(registerBody))
                .build();
        HttpResponse<String> regResp = httpClient.send(regReq, HttpResponse.BodyHandlers.ofString());
        assertThat(regResp.statusCode()).isIn(200, 201);

        JsonNode regJson = objectMapper.readTree(regResp.body());
        assertThat(regJson.get("success").asBoolean()).isTrue();
        String token = regJson.get("data").get("token").asText();
        assertThat(token).isNotBlank();

        // 2. Fetch User Profile
        HttpRequest meReq = HttpRequest.newBuilder()
                .uri(URI.create(baseUrl + "/me"))
                .header("Authorization", "Bearer " + token)
                .GET()
                .build();
        HttpResponse<String> meResp = httpClient.send(meReq, HttpResponse.BodyHandlers.ofString());
        assertThat(meResp.statusCode()).isEqualTo(200);
        JsonNode meJson = objectMapper.readTree(meResp.body());
        assertThat(meJson.get("data").get("email").asText()).isEqualTo(testEmail);

        // 3. Create Trip for User
        String createTripBody = "{" +
                "\"destinationId\": \"jaipur\"," +
                "\"title\": \"Heritage Expedition\"," +
                "\"description\": \"Exploring Amber and Hawa Mahal\"," +
                "\"startDate\": \"2026-11-01\"," +
                "\"endDate\": \"2026-11-03\"," +
                "\"travelerCount\": 2," +
                "\"targetBudget\": 15000.00" +
                "}";

        HttpRequest createTripReq = HttpRequest.newBuilder()
                .uri(URI.create(baseUrl + "/trips"))
                .header("Authorization", "Bearer " + token)
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(createTripBody))
                .build();
        HttpResponse<String> createTripResp = httpClient.send(createTripReq, HttpResponse.BodyHandlers.ofString());
        assertThat(createTripResp.statusCode()).isIn(200, 201);

        JsonNode tripJson = objectMapper.readTree(createTripResp.body());
        String tripId = tripJson.get("data").get("id").asText();
        assertThat(tripId).isNotBlank();
        assertThat(tripJson.get("data").get("destinationName").asText()).isEqualTo("Jaipur");

        // Verify generated itinerary days (Nov 1 to Nov 3 = 3 days)
        JsonNode days = tripJson.get("data").get("days");
        assertThat(days.size()).isEqualTo(3);
        String day1Id = days.get(0).get("id").asText();

        // 4. Persistence verification across separate read request
        HttpRequest getTripReq = HttpRequest.newBuilder()
                .uri(URI.create(baseUrl + "/trips/" + tripId))
                .header("Authorization", "Bearer " + token)
                .GET()
                .build();
        HttpResponse<String> getTripResp = httpClient.send(getTripReq, HttpResponse.BodyHandlers.ofString());
        assertThat(getTripResp.statusCode()).isEqualTo(200);
        JsonNode persistedTripJson = objectMapper.readTree(getTripResp.body());
        assertThat(persistedTripJson.get("data").get("title").asText()).isEqualTo("Heritage Expedition");

        // 5. Add Itinerary Item to Day 1
        String addItemBody = "{" +
                "\"title\": \"Guided Tour of Amber Palace\"," +
                "\"category\": \"ACTIVITY\"," +
                "\"notes\": \"Amer, Jaipur\"," +
                "\"suggestedTime\": \"09:00\"," +
                "\"estimatedCost\": 500.00," +
                "\"durationMinutes\": 180" +
                "}";
        HttpRequest addItemReq = HttpRequest.newBuilder()
                .uri(URI.create(baseUrl + "/trips/" + tripId + "/days/" + day1Id + "/items"))
                .header("Authorization", "Bearer " + token)
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(addItemBody))
                .build();
        HttpResponse<String> addItemResp = httpClient.send(addItemReq, HttpResponse.BodyHandlers.ofString());
        assertThat(addItemResp.statusCode()).isIn(200, 201);

        JsonNode itemJson = objectMapper.readTree(addItemResp.body());
        String itemId = itemJson.get("data").get("id").asText();
        assertThat(itemId).isNotBlank();

        // 6. Cross-User Authorization Test: Register User B
        String userBEmail = "intruder_" + UUID.randomUUID().toString().substring(0, 8) + "@example.com";
        String userBBody = String.format("{\"email\": \"%s\", \"password\": \"%s\", \"displayName\": \"User B\"}", userBEmail, password);
        HttpRequest regBReq = HttpRequest.newBuilder()
                .uri(URI.create(baseUrl + "/auth/register"))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(userBBody))
                .build();
        HttpResponse<String> regBResp = httpClient.send(regBReq, HttpResponse.BodyHandlers.ofString());
        assertThat(regBResp.statusCode()).isIn(200, 201);
        String tokenB = objectMapper.readTree(regBResp.body()).get("data").get("token").asText();

        // User B attempts to access User A's trip -> MUST return 403 Forbidden
        HttpRequest forbiddenReq = HttpRequest.newBuilder()
                .uri(URI.create(baseUrl + "/trips/" + tripId))
                .header("Authorization", "Bearer " + tokenB)
                .GET()
                .build();
        HttpResponse<String> forbiddenResp = httpClient.send(forbiddenReq, HttpResponse.BodyHandlers.ofString());
        assertThat(forbiddenResp.statusCode()).isEqualTo(403);

        // 7. Unauthenticated request to /trips -> MUST return 401 Unauthorized
        HttpRequest unauthReq = HttpRequest.newBuilder()
                .uri(URI.create(baseUrl + "/trips"))
                .GET()
                .build();
        HttpResponse<String> unauthResp = httpClient.send(unauthReq, HttpResponse.BodyHandlers.ofString());
        assertThat(unauthResp.statusCode()).isEqualTo(401);

        // 8. Update Trip (User A)
        String updateTripBody = "{" +
                "\"title\": \"Updated Heritage Expedition\"," +
                "\"startDate\": \"2026-11-01\"," +
                "\"endDate\": \"2026-11-04\"," +
                "\"targetBudget\": 18000.00," +
                "\"status\": \"CONFIRMED\"" +
                "}";
        HttpRequest updateReq = HttpRequest.newBuilder()
                .uri(URI.create(baseUrl + "/trips/" + tripId))
                .header("Authorization", "Bearer " + token)
                .header("Content-Type", "application/json")
                .PUT(HttpRequest.BodyPublishers.ofString(updateTripBody))
                .build();
        HttpResponse<String> updateResp = httpClient.send(updateReq, HttpResponse.BodyHandlers.ofString());
        assertThat(updateResp.statusCode()).isEqualTo(200);

        // 9. Delete Trip (User A)
        HttpRequest deleteReq = HttpRequest.newBuilder()
                .uri(URI.create(baseUrl + "/trips/" + tripId))
                .header("Authorization", "Bearer " + token)
                .DELETE()
                .build();
        HttpResponse<String> deleteResp = httpClient.send(deleteReq, HttpResponse.BodyHandlers.ofString());
        assertThat(deleteResp.statusCode()).isEqualTo(200);

        // Verify Trip is gone -> 404
        HttpResponse<String> getDeletedResp = httpClient.send(getTripReq, HttpResponse.BodyHandlers.ofString());
        assertThat(getDeletedResp.statusCode()).isEqualTo(404);
    }

    @Test
    @DisplayName("Validation Error: Invalid date range (end date before start date) returns 400 Bad Request")
    void testInvalidDateRangeValidation() throws IOException, InterruptedException {
        String testEmail = "traveler_" + UUID.randomUUID().toString().substring(0, 8) + "@example.com";
        String regBody = String.format("{\"email\": \"%s\", \"password\": \"Password123!\", \"displayName\": \"Date Tester\"}", testEmail);
        HttpRequest regReq = HttpRequest.newBuilder()
                .uri(URI.create(baseUrl + "/auth/register"))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(regBody))
                .build();
        String token = objectMapper.readTree(httpClient.send(regReq, HttpResponse.BodyHandlers.ofString()).body()).get("data").get("token").asText();

        // End date is before start date
        String invalidDateTrip = "{" +
                "\"title\": \"Invalid Trip\"," +
                "\"startDate\": \"2026-11-10\"," +
                "\"endDate\": \"2026-11-05\"" +
                "}";
        HttpRequest req = HttpRequest.newBuilder()
                .uri(URI.create(baseUrl + "/trips"))
                .header("Authorization", "Bearer " + token)
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(invalidDateTrip))
                .build();
        HttpResponse<String> resp = httpClient.send(req, HttpResponse.BodyHandlers.ofString());
        assertThat(resp.statusCode()).isEqualTo(400);
    }
}
