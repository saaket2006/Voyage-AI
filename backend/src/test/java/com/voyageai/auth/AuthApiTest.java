package com.voyageai.auth;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.voyageai.auth.dto.LoginRequest;
import com.voyageai.auth.dto.RegisterRequest;
import com.voyageai.auth.dto.UpdatePreferencesRequest;
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

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class AuthApiTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private UserRepository userRepository;

    @BeforeEach
    void setUp() {
        objectMapper.registerModule(new com.fasterxml.jackson.datatype.jsr310.JavaTimeModule());
        userRepository.deleteAll();
    }

    @Test
    @DisplayName("POST /api/v1/auth/register with valid credentials returns 201 Created and JWT token")
    void register_validPayload_returns201AndToken() throws Exception {
        RegisterRequest request = new RegisterRequest("testuser@example.com", "SecurePass123!", "Test Explorer");

        mockMvc.perform(post("/api/v1/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.message").value("User registered successfully."))
                .andExpect(jsonPath("$.data.token").isString())
                .andExpect(jsonPath("$.data.tokenType").value("Bearer"))
                .andExpect(jsonPath("$.data.expiresIn").isNumber())
                .andExpect(jsonPath("$.data.user.email").value("testuser@example.com"))
                .andExpect(jsonPath("$.data.user.displayName").value("Test Explorer"))
                .andExpect(jsonPath("$.data.user.id").isString());
    }

    @Test
    @DisplayName("POST /api/v1/auth/register with invalid email returns 400 Bad Request with field validation errors")
    void register_invalidEmail_returns400ValidationError() throws Exception {
        RegisterRequest request = new RegisterRequest("not-an-email", "SecurePass123!", "Test Explorer");

        mockMvc.perform(post("/api/v1/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value("VALIDATION_ERROR"))
                .andExpect(jsonPath("$.fieldErrors.email").exists());
    }

    @Test
    @DisplayName("POST /api/v1/auth/register with short password returns 400 Bad Request")
    void register_shortPassword_returns400ValidationError() throws Exception {
        RegisterRequest request = new RegisterRequest("user@example.com", "short", "Test Explorer");

        mockMvc.perform(post("/api/v1/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value("VALIDATION_ERROR"))
                .andExpect(jsonPath("$.fieldErrors.password").exists());
    }

    @Test
    @DisplayName("POST /api/v1/auth/register with duplicate email returns 409 Conflict")
    void register_duplicateEmail_returns409Conflict() throws Exception {
        RegisterRequest request = new RegisterRequest("duplicate@example.com", "SecurePass123!", "Explorer One");

        mockMvc.perform(post("/api/v1/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated());

        RegisterRequest duplicate = new RegisterRequest("duplicate@example.com", "AnotherPassword123!", "Explorer Two");

        mockMvc.perform(post("/api/v1/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(duplicate)))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.code").value("CONFLICT"))
                .andExpect(jsonPath("$.message").value(containsString("already exists")));
    }

    @Test
    @DisplayName("POST /api/v1/auth/login with valid credentials returns 200 OK and JWT token")
    void login_validCredentials_returns200AndToken() throws Exception {
        RegisterRequest register = new RegisterRequest("login@example.com", "MyPassword123!", "Login Traveler");
        mockMvc.perform(post("/api/v1/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(register)))
                .andExpect(status().isCreated());

        LoginRequest login = new LoginRequest("login@example.com", "MyPassword123!");
        mockMvc.perform(post("/api/v1/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(login)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.token").isString())
                .andExpect(jsonPath("$.data.user.email").value("login@example.com"));
    }

    @Test
    @DisplayName("POST /api/v1/auth/login with incorrect password returns 401 Unauthorized")
    void login_wrongPassword_returns401Unauthorized() throws Exception {
        RegisterRequest register = new RegisterRequest("login2@example.com", "CorrectPassword123!", "Traveler");
        mockMvc.perform(post("/api/v1/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(register)))
                .andExpect(status().isCreated());

        LoginRequest login = new LoginRequest("login2@example.com", "WrongPassword999!");
        mockMvc.perform(post("/api/v1/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(login)))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.code").value("UNAUTHENTICATED"))
                .andExpect(jsonPath("$.message").value("Invalid email or password."));
    }

    @Test
    @DisplayName("GET /api/v1/me without token returns 401 Unauthorized")
    void me_unauthenticated_returns401() throws Exception {
        mockMvc.perform(get("/api/v1/me"))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.code").value("UNAUTHENTICATED"));
    }

    @Test
    @DisplayName("GET /api/v1/me with valid Bearer token returns 200 OK and user profile")
    void me_authenticated_returnsUserProfile() throws Exception {
        RegisterRequest register = new RegisterRequest("profile@example.com", "Password123!", "Profile User");
        MvcResult result = mockMvc.perform(post("/api/v1/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(register)))
                .andExpect(status().isCreated())
                .andReturn();

        String responseBody = result.getResponse().getContentAsString();
        String token = objectMapper.readTree(responseBody).path("data").path("token").asText();

        mockMvc.perform(get("/api/v1/me")
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.email").value("profile@example.com"))
                .andExpect(jsonPath("$.data.displayName").value("Profile User"));
    }

    @Test
    @DisplayName("PUT /api/v1/me/preferences updates preferences and returns updated profile")
    void updatePreferences_authenticated_persistsPreferences() throws Exception {
        RegisterRequest register = new RegisterRequest("prefs@example.com", "Password123!", "Prefs User");
        MvcResult result = mockMvc.perform(post("/api/v1/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(register)))
                .andExpect(status().isCreated())
                .andReturn();

        String token = objectMapper.readTree(result.getResponse().getContentAsString()).path("data").path("token").asText();

        UpdatePreferencesRequest updateReq = new UpdatePreferencesRequest("{\"preferredRegion\": \"Western Ghats\", \"budget\": \"Comfort\"}");

        mockMvc.perform(put("/api/v1/me/preferences")
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateReq)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.preferences").value(containsString("Western Ghats")));
    }

    @Test
    @DisplayName("POST /api/v1/auth/logout returns 200 OK")
    void logout_returns200() throws Exception {
        mockMvc.perform(post("/api/v1/auth/logout"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true));
    }
}
