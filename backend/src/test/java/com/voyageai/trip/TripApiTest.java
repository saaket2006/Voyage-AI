package com.voyageai.trip;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.voyageai.auth.AuthService;
import com.voyageai.auth.dto.AuthResponse;
import com.voyageai.auth.dto.RegisterRequest;
import com.voyageai.itinerary.dto.CreateItineraryItemRequest;
import com.voyageai.itinerary.dto.UpdateItineraryItemRequest;
import com.voyageai.trip.dto.CreateTripRequest;
import com.voyageai.trip.dto.UpdateTripRequest;
import com.voyageai.user.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class TripApiTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private AuthService authService;

    @Autowired
    private TripRepository tripRepository;

    @Autowired
    private UserRepository userRepository;

    private String userAToken;
    private String userAId;

    private String userBToken;
    private String userBId;

    @BeforeEach
    void setUp() {
        objectMapper.registerModule(new com.fasterxml.jackson.datatype.jsr310.JavaTimeModule());
        tripRepository.deleteAll();
        userRepository.deleteAll();

        // Register User A
        AuthResponse authA = authService.register(new RegisterRequest("userA@example.com", "Password123!", "User Alpha"));
        userAToken = authA.getToken();
        userAId = authA.getUser().getId();

        // Register User B
        AuthResponse authB = authService.register(new RegisterRequest("userB@example.com", "Password123!", "User Beta"));
        userBToken = authB.getToken();
        userBId = authB.getUser().getId();
    }

    @Test
    @DisplayName("Unauthenticated request to /api/v1/trips returns 401 Unauthorized")
    void getTrips_unauthenticated_returns401() throws Exception {
        mockMvc.perform(get("/api/v1/trips"))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.code").value("UNAUTHENTICATED"));
    }

    @Test
    @DisplayName("POST /api/v1/trips creates trip and initializes itinerary days for authenticated user")
    void createTrip_validRequest_persistsTripAndDays() throws Exception {
        CreateTripRequest request = new CreateTripRequest(
                "Winter in Pink City",
                "jaipur",
                "Jaipur",
                LocalDate.now().plusDays(10),
                LocalDate.now().plusDays(12)
        );
        request.setDescription("Exploring Amber fort and Rajasthani culture");
        request.setTravelerCount(2);
        request.setTargetBudget(new BigDecimal("25000.00"));

        MvcResult result = mockMvc.perform(post("/api/v1/trips")
                        .header("Authorization", "Bearer " + userAToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.title").value("Winter in Pink City"))
                .andExpect(jsonPath("$.data.ownerId").value(userAId))
                .andExpect(jsonPath("$.data.destinationId").value("jaipur"))
                .andExpect(jsonPath("$.data.travelerCount").value(2))
                .andExpect(jsonPath("$.data.days", hasSize(3)))
                .andReturn();

        String tripId = objectMapper.readTree(result.getResponse().getContentAsString()).path("data").path("id").asText();
        assertThat(tripRepository.existsById(tripId)).isTrue();
    }

    @Test
    @DisplayName("POST /api/v1/trips with endDate before startDate returns 400 Bad Request")
    void createTrip_invalidDates_returns400() throws Exception {
        CreateTripRequest request = new CreateTripRequest(
                "Time Travel Trip",
                "jaipur",
                "Jaipur",
                LocalDate.now().plusDays(10),
                LocalDate.now().plusDays(5) // End before start
        );

        mockMvc.perform(post("/api/v1/trips")
                        .header("Authorization", "Bearer " + userAToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value("INVALID_ARGUMENT"))
                .andExpect(jsonPath("$.message").value(containsString("End date cannot precede start date")));
    }

    @Test
    @DisplayName("GET /api/v1/trips returns only trips belonging to the authenticated user")
    void getTrips_userIsolation_returnsOnlyOwnTrips() throws Exception {
        // User A creates trip
        CreateTripRequest tripA = new CreateTripRequest("Trip Alpha", "jaipur", "Jaipur",
                LocalDate.now().plusDays(1), LocalDate.now().plusDays(2));
        mockMvc.perform(post("/api/v1/trips")
                        .header("Authorization", "Bearer " + userAToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(tripA)))
                .andExpect(status().isCreated());

        // User B creates trip
        CreateTripRequest tripB = new CreateTripRequest("Trip Beta", "munnar", "Munnar",
                LocalDate.now().plusDays(5), LocalDate.now().plusDays(8));
        mockMvc.perform(post("/api/v1/trips")
                        .header("Authorization", "Bearer " + userBToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(tripB)))
                .andExpect(status().isCreated());

        // User A retrieves trips: should only see Trip Alpha
        mockMvc.perform(get("/api/v1/trips")
                        .header("Authorization", "Bearer " + userAToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data", hasSize(1)))
                .andExpect(jsonPath("$.data[0].title").value("Trip Alpha"));

        // User B retrieves trips: should only see Trip Beta
        mockMvc.perform(get("/api/v1/trips")
                        .header("Authorization", "Bearer " + userBToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data", hasSize(1)))
                .andExpect(jsonPath("$.data[0].title").value("Trip Beta"));
    }

    @Test
    @DisplayName("Cross-user authorization: User B accessing User A's trip is rejected with 403 Forbidden")
    void getTripById_crossUserAccess_returns403Forbidden() throws Exception {
        // User A creates trip
        CreateTripRequest tripA = new CreateTripRequest("Private Trip", "varanasi", "Varanasi",
                LocalDate.now().plusDays(1), LocalDate.now().plusDays(3));
        MvcResult result = mockMvc.perform(post("/api/v1/trips")
                        .header("Authorization", "Bearer " + userAToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(tripA)))
                .andExpect(status().isCreated())
                .andReturn();

        String tripId = objectMapper.readTree(result.getResponse().getContentAsString()).path("data").path("id").asText();

        // User B attempts to access User A's trip
        mockMvc.perform(get("/api/v1/trips/" + tripId)
                        .header("Authorization", "Bearer " + userBToken))
                .andExpect(status().isForbidden())
                .andExpect(jsonPath("$.code").value("FORBIDDEN"));
    }

    @Test
    @DisplayName("PUT /api/v1/trips/{id} updates permitted trip properties for owner")
    void updateTrip_owner_updatesSuccessfully() throws Exception {
        CreateTripRequest tripA = new CreateTripRequest("Initial Title", "hampi", "Hampi",
                LocalDate.now().plusDays(1), LocalDate.now().plusDays(3));
        MvcResult result = mockMvc.perform(post("/api/v1/trips")
                        .header("Authorization", "Bearer " + userAToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(tripA)))
                .andExpect(status().isCreated())
                .andReturn();

        String tripId = objectMapper.readTree(result.getResponse().getContentAsString()).path("data").path("id").asText();

        UpdateTripRequest updateRequest = new UpdateTripRequest();
        updateRequest.setTitle("Updated Title for Hampi");
        updateRequest.setDestinationId("hampi");
        updateRequest.setDestinationName("Hampi");
        updateRequest.setDescription("Updated description");
        updateRequest.setStartDate(LocalDate.now().plusDays(1));
        updateRequest.setEndDate(LocalDate.now().plusDays(4));
        updateRequest.setTravelerCount(3);
        updateRequest.setStatus("CONFIRMED");

        mockMvc.perform(put("/api/v1/trips/" + tripId)
                        .header("Authorization", "Bearer " + userAToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.title").value("Updated Title for Hampi"))
                .andExpect(jsonPath("$.data.travelerCount").value(3))
                .andExpect(jsonPath("$.data.status").value("CONFIRMED"));
    }

    @Test
    @DisplayName("DELETE /api/v1/trips/{id} deletes trip and cascades to days and items")
    void deleteTrip_owner_cascadesDeletion() throws Exception {
        CreateTripRequest tripA = new CreateTripRequest("Trip To Delete", "jaipur", "Jaipur",
                LocalDate.now().plusDays(1), LocalDate.now().plusDays(2));
        MvcResult result = mockMvc.perform(post("/api/v1/trips")
                        .header("Authorization", "Bearer " + userAToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(tripA)))
                .andExpect(status().isCreated())
                .andReturn();

        String tripId = objectMapper.readTree(result.getResponse().getContentAsString()).path("data").path("id").asText();

        // User B attempts to delete: 403 Forbidden
        mockMvc.perform(delete("/api/v1/trips/" + tripId)
                        .header("Authorization", "Bearer " + userBToken))
                .andExpect(status().isForbidden());

        // User A deletes trip: 200 OK
        mockMvc.perform(delete("/api/v1/trips/" + tripId)
                        .header("Authorization", "Bearer " + userAToken))
                .andExpect(status().isOk());

        assertThat(tripRepository.existsById(tripId)).isFalse();
    }

    @Test
    @DisplayName("Itinerary item operations: adding, updating, and removing items on a day")
    void itineraryItem_lifecycle_succeeds() throws Exception {
        // Create trip
        CreateTripRequest tripA = new CreateTripRequest("Itinerary Test Trip", "jaipur", "Jaipur",
                LocalDate.now().plusDays(1), LocalDate.now().plusDays(2));
        MvcResult tripResult = mockMvc.perform(post("/api/v1/trips")
                        .header("Authorization", "Bearer " + userAToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(tripA)))
                .andExpect(status().isCreated())
                .andReturn();

        String tripResponseStr = tripResult.getResponse().getContentAsString();
        String tripId = objectMapper.readTree(tripResponseStr).path("data").path("id").asText();
        String dayId = objectMapper.readTree(tripResponseStr).path("data").path("days").get(0).path("id").asText();

        // 1. Add item to Day 1
        CreateItineraryItemRequest itemRequest = new CreateItineraryItemRequest("Morning Amber Fort Visit", "ATTRACTION");
        itemRequest.setListingId("jp-att-1");
        itemRequest.setSuggestedTime("09:00");
        itemRequest.setDurationMinutes(180);
        itemRequest.setEstimatedCost(new BigDecimal("100.00"));
        itemRequest.setNotes("Meet guide at Suraj Pol");

        MvcResult itemResult = mockMvc.perform(post("/api/v1/trips/" + tripId + "/days/" + dayId + "/items")
                        .header("Authorization", "Bearer " + userAToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(itemRequest)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.data.title").value("Morning Amber Fort Visit"))
                .andExpect(jsonPath("$.data.listing.name").value("Amber Fort & Palace"))
                .andReturn();

        String itemId = objectMapper.readTree(itemResult.getResponse().getContentAsString()).path("data").path("id").asText();

        // 2. User B cannot modify item (403 Forbidden)
        UpdateItineraryItemRequest updateReq = new UpdateItineraryItemRequest();
        updateReq.setTitle("Hacked Title");
        mockMvc.perform(put("/api/v1/trips/" + tripId + "/days/" + dayId + "/items/" + itemId)
                        .header("Authorization", "Bearer " + userBToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateReq)))
                .andExpect(status().isForbidden());

        // 3. User A updates item
        updateReq.setTitle("Updated Amber Fort Morning Tour");
        updateReq.setCategory("ATTRACTION");
        updateReq.setDurationMinutes(210);
        mockMvc.perform(put("/api/v1/trips/" + tripId + "/days/" + dayId + "/items/" + itemId)
                        .header("Authorization", "Bearer " + userAToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateReq)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.title").value("Updated Amber Fort Morning Tour"))
                .andExpect(jsonPath("$.data.durationMinutes").value(210));

        // 4. Verify trip reflects item
        mockMvc.perform(get("/api/v1/trips/" + tripId)
                        .header("Authorization", "Bearer " + userAToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.days[0].items", hasSize(1)))
                .andExpect(jsonPath("$.data.days[0].items[0].title").value("Updated Amber Fort Morning Tour"));

        // 5. User A deletes item
        mockMvc.perform(delete("/api/v1/trips/" + tripId + "/days/" + dayId + "/items/" + itemId)
                        .header("Authorization", "Bearer " + userAToken))
                .andExpect(status().isOk());

        // 6. Verify item is removed
        mockMvc.perform(get("/api/v1/trips/" + tripId)
                        .header("Authorization", "Bearer " + userAToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.days[0].items", hasSize(0)));
    }
}
