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

@DisplayName("User Profile & Scoped Trips API Black-box Tests")
public class UserProfileApiTest {

    private final HttpClient httpClient = HttpClient.newBuilder()
            .connectTimeout(Duration.ofSeconds(10))
            .build();

    private final ObjectMapper objectMapper = new ObjectMapper();
    private final String baseUrl = TestConfig.getBackendBaseUrl();

    private record AuthInfo(String token, String userId, String email) {}

    private AuthInfo registerUser(String namePrefix) throws IOException, InterruptedException {
        String suffix = UUID.randomUUID().toString().substring(0, 8);
        String email = namePrefix.toLowerCase() + "-" + suffix + "@voyageai.local";
        String body = String.format("""
            {
              "email": "%s",
              "password": "Password123!",
              "displayName": "%s %s"
            }
            """, email, namePrefix, suffix);

        HttpRequest req = HttpRequest.newBuilder()
                .uri(URI.create(baseUrl + "/auth/register"))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(body))
                .build();

        HttpResponse<String> resp = httpClient.send(req, HttpResponse.BodyHandlers.ofString());
        JsonNode json = objectMapper.readTree(resp.body());
        String token = json.get("data").get("token").asText();
        String userId = json.get("data").get("user").get("id").asText();
        return new AuthInfo(token, userId, email);
    }

    @Test
    @Tag("smoke")
    @DisplayName("Tests /api/v1/users/me profile lifecycle, sync, and preferences")
    void testUserProfileLifecycle() throws IOException, InterruptedException {
        AuthInfo user = registerUser("ProfileUser");

        // 1. Unauthenticated GET /users/me -> 401
        HttpRequest unauthReq = HttpRequest.newBuilder()
                .uri(URI.create(baseUrl + "/users/me"))
                .GET()
                .build();
        HttpResponse<String> unauthResp = httpClient.send(unauthReq, HttpResponse.BodyHandlers.ofString());
        assertThat(unauthResp.statusCode()).isEqualTo(401);

        // 2. Authenticated GET /users/me
        HttpRequest getReq = HttpRequest.newBuilder()
                .uri(URI.create(baseUrl + "/users/me"))
                .header("Authorization", "Bearer " + user.token())
                .GET()
                .build();
        HttpResponse<String> getResp = httpClient.send(getReq, HttpResponse.BodyHandlers.ofString());
        assertThat(getResp.statusCode()).isEqualTo(200);
        JsonNode getJson = objectMapper.readTree(getResp.body());
        assertThat(getJson.get("data").get("email").asText()).isEqualTo(user.email());
        assertThat(getJson.get("data").get("authProvider").asText()).isEqualTo("LOCAL");

        // 3. POST /users/me/sync (Federated provider sync)
        String syncBody = """
            {
              "authProvider": "GOOGLE",
              "providerSubject": "g-sub-12345",
              "profileImageUrl": "https://lh3.googleusercontent.com/avatar.jpg",
              "displayName": "Synced Google User"
            }
            """;
        HttpRequest syncReq = HttpRequest.newBuilder()
                .uri(URI.create(baseUrl + "/users/me/sync"))
                .header("Content-Type", "application/json")
                .header("Authorization", "Bearer " + user.token())
                .POST(HttpRequest.BodyPublishers.ofString(syncBody))
                .build();
        HttpResponse<String> syncResp = httpClient.send(syncReq, HttpResponse.BodyHandlers.ofString());
        assertThat(syncResp.statusCode()).isEqualTo(200);
        JsonNode syncJson = objectMapper.readTree(syncResp.body());
        assertThat(syncJson.get("data").get("authProvider").asText()).isEqualTo("GOOGLE");
        assertThat(syncJson.get("data").get("profileImageUrl").asText()).isEqualTo("https://lh3.googleusercontent.com/avatar.jpg");
        assertThat(syncJson.get("data").get("displayName").asText()).isEqualTo("Synced Google User");

        // 4. PATCH /users/me/profile
        String patchBody = """
            {
              "displayName": "Renamed Voyager",
              "profileImageUrl": "https://example.com/new-pic.png"
            }
            """;
        HttpRequest patchReq = HttpRequest.newBuilder()
                .uri(URI.create(baseUrl + "/users/me/profile"))
                .header("Content-Type", "application/json")
                .header("Authorization", "Bearer " + user.token())
                .method("PATCH", HttpRequest.BodyPublishers.ofString(patchBody))
                .build();
        HttpResponse<String> patchResp = httpClient.send(patchReq, HttpResponse.BodyHandlers.ofString());
        assertThat(patchResp.statusCode()).isEqualTo(200);
        JsonNode patchJson = objectMapper.readTree(patchResp.body());
        assertThat(patchJson.get("data").get("displayName").asText()).isEqualTo("Renamed Voyager");
        assertThat(patchJson.get("data").get("profileImageUrl").asText()).isEqualTo("https://example.com/new-pic.png");
    }

    @Test
    @Tag("smoke")
    @DisplayName("Tests /api/v1/users/me/trips CRUD and cross-user isolation")
    void testUserTripsCrudAndIsolation() throws IOException, InterruptedException {
        AuthInfo userA = registerUser("VoyagerA");
        AuthInfo userB = registerUser("VoyagerB");

        // 1. User A creates trip via /users/me/trips
        String createBody = """
            {
              "title": "Jaipur Explorer 2026",
              "destinationId": "jaipur",
              "destinationName": "Jaipur",
              "startDate": "2026-11-01",
              "endDate": "2026-11-04",
              "travelerCount": 2,
              "targetBudget": 30000.00,
              "description": "User A exploration"
            }
            """;
        HttpRequest createReq = HttpRequest.newBuilder()
                .uri(URI.create(baseUrl + "/users/me/trips"))
                .header("Content-Type", "application/json")
                .header("Authorization", "Bearer " + userA.token())
                .POST(HttpRequest.BodyPublishers.ofString(createBody))
                .build();
        HttpResponse<String> createResp = httpClient.send(createReq, HttpResponse.BodyHandlers.ofString());
        assertThat(createResp.statusCode()).isEqualTo(201);
        JsonNode createJson = objectMapper.readTree(createResp.body());
        String tripId = createJson.get("data").get("id").asText();

        // 2. User A retrieves /users/me/trips -> list contains tripId
        HttpRequest listReq = HttpRequest.newBuilder()
                .uri(URI.create(baseUrl + "/users/me/trips"))
                .header("Authorization", "Bearer " + userA.token())
                .GET()
                .build();
        HttpResponse<String> listResp = httpClient.send(listReq, HttpResponse.BodyHandlers.ofString());
        assertThat(listResp.statusCode()).isEqualTo(200);
        JsonNode listJson = objectMapper.readTree(listResp.body());
        assertThat(listJson.get("data").size()).isGreaterThanOrEqualTo(1);

        // 3. User B retrieves /users/me/trips -> list is empty for User B
        HttpRequest listBReq = HttpRequest.newBuilder()
                .uri(URI.create(baseUrl + "/users/me/trips"))
                .header("Authorization", "Bearer " + userB.token())
                .GET()
                .build();
        HttpResponse<String> listBResp = httpClient.send(listBReq, HttpResponse.BodyHandlers.ofString());
        assertThat(listBResp.statusCode()).isEqualTo(200);
        JsonNode listBJson = objectMapper.readTree(listBResp.body());
        assertThat(listBJson.get("data").size()).isEqualTo(0);

        // 4. Cross-user access: User B tries to GET User A's trip -> 403 Forbidden
        HttpRequest crossGetReq = HttpRequest.newBuilder()
                .uri(URI.create(baseUrl + "/users/me/trips/" + tripId))
                .header("Authorization", "Bearer " + userB.token())
                .GET()
                .build();
        HttpResponse<String> crossGetResp = httpClient.send(crossGetReq, HttpResponse.BodyHandlers.ofString());
        assertThat(crossGetResp.statusCode()).isEqualTo(403);

        // 5. User A deletes trip via /users/me/trips/{tripId}
        HttpRequest deleteReq = HttpRequest.newBuilder()
                .uri(URI.create(baseUrl + "/users/me/trips/" + tripId))
                .header("Authorization", "Bearer " + userA.token())
                .DELETE()
                .build();
        HttpResponse<String> deleteResp = httpClient.send(deleteReq, HttpResponse.BodyHandlers.ofString());
        assertThat(deleteResp.statusCode()).isEqualTo(200);
    }
}
