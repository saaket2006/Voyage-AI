package com.voyageai.destination;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.voyageai.auth.AuthService;
import com.voyageai.auth.dto.AuthResponse;
import com.voyageai.auth.dto.RegisterRequest;
import com.voyageai.destination.ingestion.AdminDestinationController;
import com.voyageai.destination.ingestion.DestinationImportRequest;
import com.voyageai.destination.ingestion.DestinationImportResult;
import com.voyageai.destination.ingestion.DestinationIngestionService;
import com.voyageai.user.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.ArgumentMatchers.anyBoolean;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class AdminDestinationControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private AuthService authService;

    @Autowired
    private UserRepository userRepository;

    @MockitoBean
    private DestinationIngestionService ingestionService;

    private String adminToken;
    private String regularUserToken;

    @BeforeEach
    void setUp() {
        userRepository.deleteAll();
        AuthResponse adminAuth = authService.register(new RegisterRequest("admin@voyageai.local", "Password123!", "Admin User"));
        this.adminToken = adminAuth.getToken();

        AuthResponse regularAuth = authService.register(new RegisterRequest("regular@voyageai.local", "Password123!", "Regular User"));
        this.regularUserToken = regularAuth.getToken();
    }

    @Test
    @DisplayName("POST /api/v1/admin/destinations/import returns 401 Unauthorized without auth token")
    void importDestinations_unauthenticated_returns401() throws Exception {
        mockMvc.perform(post("/api/v1/admin/destinations/import")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{}"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @DisplayName("POST /api/v1/admin/destinations/import with regular user role returns 403 Forbidden")
    void importDestinations_regularUser_returns403Forbidden() throws Exception {
        DestinationImportRequest request = new DestinationImportRequest(List.of("Jaipur"), false);

        mockMvc.perform(post("/api/v1/admin/destinations/import")
                        .header("Authorization", "Bearer " + regularUserToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isForbidden())
                .andExpect(jsonPath("$.code").value("FORBIDDEN"));
    }

    @Test
    @DisplayName("POST /api/v1/admin/destinations/import with admin role invokes ingestion and returns 200 OK")
    void importDestinations_adminRole_returns200() throws Exception {
        DestinationImportResult mockResult = new DestinationImportResult();
        mockResult.setRecordsFetched(4);
        mockResult.setRecordsValidated(4);
        mockResult.setRecordsInserted(3);
        mockResult.setRecordsUpdated(0);
        mockResult.setRecordsSkipped(1);
        mockResult.setImportedDestinations(List.of("Jaipur", "Hyderabad", "Delhi"));

        when(ingestionService.importDestinations(anyList(), anyBoolean())).thenReturn(mockResult);

        DestinationImportRequest request = new DestinationImportRequest(List.of("Jaipur", "Hyderabad", "Delhi", "Mumbai"), false);

        mockMvc.perform(post("/api/v1/admin/destinations/import")
                        .header("Authorization", "Bearer " + adminToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.recordsInserted").value(3))
                .andExpect(jsonPath("$.data.recordsSkipped").value(1))
                .andExpect(jsonPath("$.data.importedDestinations[0]").value("Jaipur"));
    }
}
