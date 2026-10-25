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

@DisplayName("Destination Ingestion & Real Wikimedia Pipeline Black-box Tests")
public class DestinationIngestionApiTest {

    private final HttpClient httpClient = HttpClient.newBuilder()
            .connectTimeout(Duration.ofSeconds(10))
            .build();

    private final ObjectMapper objectMapper = new ObjectMapper();
    private final String baseUrl = TestConfig.getBackendBaseUrl();

    private String getRegularUserToken() throws IOException, InterruptedException {
        String randomSuffix = UUID.randomUUID().toString().substring(0, 8);
        String email = "regular-user-" + randomSuffix + "@voyageai.local";
        String body = String.format("""
            {
              "email": "%s",
              "password": "Password123!",
              "displayName": "Regular User"
            }
            """, email);

        HttpRequest registerReq = HttpRequest.newBuilder()
                .uri(URI.create(baseUrl + "/auth/register"))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(body))
                .build();

        HttpResponse<String> resp = httpClient.send(registerReq, HttpResponse.BodyHandlers.ofString());
        JsonNode json = objectMapper.readTree(resp.body());
        return json.get("data").get("token").asText();
    }

    private String getAdminToken() throws IOException, InterruptedException {
        String email = "admin@voyageai.local";
        String password = "Password123!";
        String body = String.format("""
            {
              "email": "%s",
              "password": "%s",
              "displayName": "System Administrator"
            }
            """, email, password);

        HttpRequest registerReq = HttpRequest.newBuilder()
                .uri(URI.create(baseUrl + "/auth/register"))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(body))
                .build();

        HttpResponse<String> resp = httpClient.send(registerReq, HttpResponse.BodyHandlers.ofString());
        if (resp.statusCode() == 200 || resp.statusCode() == 201) {
            JsonNode json = objectMapper.readTree(resp.body());
            return json.get("data").get("token").asText();
        }

        String loginBody = String.format("""
            {
              "email": "%s",
              "password": "%s"
            }
            """, email, password);
        HttpRequest loginReq = HttpRequest.newBuilder()
                .uri(URI.create(baseUrl + "/auth/login"))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(loginBody))
                .build();
        HttpResponse<String> loginResp = httpClient.send(loginReq, HttpResponse.BodyHandlers.ofString());
        JsonNode json = objectMapper.readTree(loginResp.body());
        return json.get("data").get("token").asText();
    }

    @Test
    @Tag("smoke")
    @DisplayName("Admin destination import enforces RBAC (401 unauth, 403 regular, 200 admin) and executes pipeline")
    void testDestinationImportAndSearch() throws IOException, InterruptedException {
        // 1. Unauthenticated request to /admin/destinations/import must return 401
        HttpRequest unauthReq = HttpRequest.newBuilder()
                .uri(URI.create(baseUrl + "/admin/destinations/import"))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString("{}"))
                .build();
        HttpResponse<String> unauthResp = httpClient.send(unauthReq, HttpResponse.BodyHandlers.ofString());
        assertThat(unauthResp.statusCode()).isEqualTo(401);

        // 2. Regular user (ROLE_USER) to /admin/destinations/import must return 403 Forbidden
        String userToken = getRegularUserToken();
        HttpRequest userReq = HttpRequest.newBuilder()
                .uri(URI.create(baseUrl + "/admin/destinations/import"))
                .header("Content-Type", "application/json")
                .header("Authorization", "Bearer " + userToken)
                .POST(HttpRequest.BodyPublishers.ofString("{}"))
                .build();
        HttpResponse<String> userResp = httpClient.send(userReq, HttpResponse.BodyHandlers.ofString());
        assertThat(userResp.statusCode()).isEqualTo(403);

        // 3. Authorized Admin user (ROLE_ADMIN) imports genuine destinations: Jaipur, Hyderabad, Delhi, Mumbai
        String adminToken = getAdminToken();
        String importBody = """
            {
              "destinations": ["Jaipur", "Hyderabad", "Delhi", "Mumbai"],
              "forceUpdate": false
            }
            """;
        HttpRequest importReq = HttpRequest.newBuilder()
                .uri(URI.create(baseUrl + "/admin/destinations/import"))
                .header("Content-Type", "application/json")
                .header("Authorization", "Bearer " + adminToken)
                .POST(HttpRequest.BodyPublishers.ofString(importBody))
                .build();
        HttpResponse<String> importResp = httpClient.send(importReq, HttpResponse.BodyHandlers.ofString());
        assertThat(importResp.statusCode()).isEqualTo(200);

        JsonNode importJson = objectMapper.readTree(importResp.body());
        assertThat(importJson.get("success").asBoolean()).isTrue();
        JsonNode data = importJson.get("data");
        assertThat(data.get("recordsFetched").asInt()).isGreaterThanOrEqualTo(4);
        assertThat(data.get("recordsValidated").asInt()).isGreaterThanOrEqualTo(4);

        // 4. Search for Jaipur
        HttpRequest jaipurReq = HttpRequest.newBuilder()
                .uri(URI.create(baseUrl + "/destinations?search=Jaipur"))
                .GET()
                .build();
        HttpResponse<String> jaipurResp = httpClient.send(jaipurReq, HttpResponse.BodyHandlers.ofString());
        assertThat(jaipurResp.statusCode()).isEqualTo(200);
        JsonNode jaipurJson = objectMapper.readTree(jaipurResp.body());
        JsonNode jaipurList = jaipurJson.get("data");
        assertThat(jaipurList.size()).isGreaterThanOrEqualTo(1);
        boolean foundJaipur = false;
        for (JsonNode item : jaipurList) {
            if ("Jaipur".equalsIgnoreCase(item.get("name").asText())) {
                foundJaipur = true;
                break;
            }
        }
        assertThat(foundJaipur).isTrue();

        // 5. Search for Hyderabad (verifying authentic coordinates and provider metadata)
        HttpRequest hydReq = HttpRequest.newBuilder()
                .uri(URI.create(baseUrl + "/destinations?search=Hyderabad"))
                .GET()
                .build();
        HttpResponse<String> hydResp = httpClient.send(hydReq, HttpResponse.BodyHandlers.ofString());
        assertThat(hydResp.statusCode()).isEqualTo(200);
        JsonNode hydJson = objectMapper.readTree(hydResp.body());
        JsonNode hydList = hydJson.get("data");
        assertThat(hydList.size()).isGreaterThanOrEqualTo(1);
        JsonNode hydItem = hydList.get(0);
        assertThat(hydItem.get("name").asText()).isEqualTo("Hyderabad");
        assertThat(hydItem.get("latitude").asDouble()).isBetween(17.0, 18.0);
        assertThat(hydItem.get("longitude").asDouble()).isBetween(78.0, 79.0);
        assertThat(hydItem.get("provenance").get("sourceProvider").asText()).isEqualTo("WIKIPEDIA");

        // 5. Search for non-existent destination -> returns empty list []
        HttpRequest emptyReq = HttpRequest.newBuilder()
                .uri(URI.create(baseUrl + "/destinations?search=NonExistentCityXYZ99"))
                .GET()
                .build();
        HttpResponse<String> emptyResp = httpClient.send(emptyReq, HttpResponse.BodyHandlers.ofString());
        assertThat(emptyResp.statusCode()).isEqualTo(200);
        JsonNode emptyJson = objectMapper.readTree(emptyResp.body());
        assertThat(emptyJson.get("data").size()).isEqualTo(0);

        // 6. Test deduplication: repeated import for Hyderabad with forceUpdate=false should skip
        String dedupBody = """
            {
              "destinations": ["Hyderabad"],
              "forceUpdate": false
            }
            """;
        HttpRequest dedupReq = HttpRequest.newBuilder()
                .uri(URI.create(baseUrl + "/admin/destinations/import"))
                .header("Content-Type", "application/json")
                .header("Authorization", "Bearer " + adminToken)
                .POST(HttpRequest.BodyPublishers.ofString(dedupBody))
                .build();
        HttpResponse<String> dedupResp = httpClient.send(dedupReq, HttpResponse.BodyHandlers.ofString());
        assertThat(dedupResp.statusCode()).isEqualTo(200);
        JsonNode dedupJson = objectMapper.readTree(dedupResp.body());
        JsonNode dedupData = dedupJson.get("data");
        assertThat(dedupData.get("recordsSkipped").asInt()).isEqualTo(1);
        assertThat(dedupData.get("recordsInserted").asInt()).isEqualTo(0);
    }
}
