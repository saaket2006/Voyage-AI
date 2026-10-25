package com.voyageai.auth;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.voyageai.auth.dto.AuthResponse;
import com.voyageai.auth.dto.RegisterRequest;
import com.voyageai.trip.TripRepository;
import com.voyageai.trip.dto.CreateTripRequest;
import com.voyageai.user.UserRepository;
import com.voyageai.user.dto.UpdateProfileRequest;
import com.voyageai.user.dto.UserSyncRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class UserApiTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private AuthService authService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TripRepository tripRepository;

    private String userToken;
    private String userId;

    @BeforeEach
    void setUp() {
        objectMapper.registerModule(new com.fasterxml.jackson.datatype.jsr310.JavaTimeModule());
        tripRepository.deleteAll();
        userRepository.deleteAll();

        AuthResponse auth = authService.register(new RegisterRequest("explorer@voyageai.local", "Password123!", "Travel Explorer"));
        this.userToken = auth.getToken();
        this.userId = auth.getUser().getId();
    }

    @Test
    @DisplayName("GET /api/v1/users/me returns authenticated user's profile")
    void getProfile_authenticated_returnsProfile() throws Exception {
        mockMvc.perform(get("/api/v1/users/me")
                        .header("Authorization", "Bearer " + userToken)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.id").value(userId))
                .andExpect(jsonPath("$.data.email").value("explorer@voyageai.local"))
                .andExpect(jsonPath("$.data.displayName").value("Travel Explorer"))
                .andExpect(jsonPath("$.data.authProvider").value("LOCAL"));
    }

    @Test
    @DisplayName("POST /api/v1/users/me/sync updates federated provider details and avatar")
    void syncProfile_updatesProviderAndImage() throws Exception {
        UserSyncRequest request = new UserSyncRequest(
                "explorer@voyageai.local", "Google Explorer",
                "https://avatar.com/user.png", "GOOGLE", "g-sub-987654"
        );

        mockMvc.perform(post("/api/v1/users/me/sync")
                        .header("Authorization", "Bearer " + userToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.authProvider").value("GOOGLE"))
                .andExpect(jsonPath("$.data.profileImageUrl").value("https://avatar.com/user.png"))
                .andExpect(jsonPath("$.data.displayName").value("Google Explorer"));
    }

    @Test
    @DisplayName("PATCH /api/v1/users/me/profile updates user display name and profile picture")
    void updateProfile_updatesNameAndImage() throws Exception {
        UpdateProfileRequest request = new UpdateProfileRequest(
                "Updated Explorer", "https://images.example.com/pic.jpg", "{\"budget\":\"Luxury\"}"
        );

        mockMvc.perform(patch("/api/v1/users/me/profile")
                        .header("Authorization", "Bearer " + userToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.displayName").value("Updated Explorer"))
                .andExpect(jsonPath("$.data.profileImageUrl").value("https://images.example.com/pic.jpg"));
    }

    @Test
    @DisplayName("User Trips CRUD: /api/v1/users/me/trips creates, lists, retrieves, and deletes trips")
    void userTrips_crudCycle() throws Exception {
        // 1. Initially empty
        mockMvc.perform(get("/api/v1/users/me/trips")
                        .header("Authorization", "Bearer " + userToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data", hasSize(0)));

        // 2. Create trip
        CreateTripRequest createReq = new CreateTripRequest(
                "Jaipur Royal Voyage", "jaipur", "Jaipur",
                LocalDate.of(2026, 11, 10), LocalDate.of(2026, 11, 13)
        );
        createReq.setTravelerCount(2);
        createReq.setTargetBudget(new BigDecimal("45000.00"));
        createReq.setDescription("Heritage exploration");


        String createResp = mockMvc.perform(post("/api/v1/users/me/trips")
                        .header("Authorization", "Bearer " + userToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createReq)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.data.title").value("Jaipur Royal Voyage"))
                .andExpect(jsonPath("$.data.destinationId").value("jaipur"))
                .andReturn().getResponse().getContentAsString();

        String tripId = objectMapper.readTree(createResp).path("data").path("id").asText();

        // 3. Get user trips -> size 1
        mockMvc.perform(get("/api/v1/users/me/trips")
                        .header("Authorization", "Bearer " + userToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data", hasSize(1)))
                .andExpect(jsonPath("$.data[0].id").value(tripId));

        // 4. Get by ID
        mockMvc.perform(get("/api/v1/users/me/trips/" + tripId)
                        .header("Authorization", "Bearer " + userToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.title").value("Jaipur Royal Voyage"));

        // 5. Delete trip
        mockMvc.perform(delete("/api/v1/users/me/trips/" + tripId)
                        .header("Authorization", "Bearer " + userToken))
                .andExpect(status().isOk());

        // 6. Verify empty again
        mockMvc.perform(get("/api/v1/users/me/trips")
                        .header("Authorization", "Bearer " + userToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data", hasSize(0)));
    }
}
