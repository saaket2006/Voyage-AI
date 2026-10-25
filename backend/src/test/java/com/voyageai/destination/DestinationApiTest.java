package com.voyageai.destination;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class DestinationApiTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    @DisplayName("GET /api/v1/destinations returns 200 OK with all persistent authentic destinations")
    void getDestinations_returnsAllDestinations() throws Exception {
        mockMvc.perform(get("/api/v1/destinations")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data", hasSize(greaterThanOrEqualTo(4))))
                .andExpect(jsonPath("$.data[*].id", hasItems("jaipur", "varanasi", "munnar", "hampi")));
    }

    @Test
    @DisplayName("GET /api/v1/destinations?search=Jaipur filters correctly")
    void getDestinations_searchFilter_returnsJaipur() throws Exception {
        mockMvc.perform(get("/api/v1/destinations")
                        .param("search", "Jaipur")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data", hasSize(1)))
                .andExpect(jsonPath("$.data[0].id").value("jaipur"))
                .andExpect(jsonPath("$.data[0].name").value("Jaipur"));
    }

    @Test
    @DisplayName("GET /api/v1/destinations?region=Western Ghats returns Munnar")
    void getDestinations_regionFilter_returnsMunnar() throws Exception {
        mockMvc.perform(get("/api/v1/destinations")
                        .param("region", "Western Ghats")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data", hasSize(1)))
                .andExpect(jsonPath("$.data[0].id").value("munnar"));
    }

    @Test
    @DisplayName("GET /api/v1/destinations?budgetTier=Budget returns budget destinations")
    void getDestinations_budgetTierFilter_returnsBudgetDestinations() throws Exception {
        mockMvc.perform(get("/api/v1/destinations")
                        .param("budgetTier", "Budget")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data", hasSize(2)))
                .andExpect(jsonPath("$.data[*].id", hasItems("varanasi", "hampi")));
    }

    @Test
    @DisplayName("Zero-fabrication policy: search for nonexistent place returns 200 OK with empty array")
    void getDestinations_nonexistentQuery_returnsEmptyArray() throws Exception {
        mockMvc.perform(get("/api/v1/destinations")
                        .param("search", "NonExistentFictionalAtlantis")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data", hasSize(0)));
    }

    @Test
    @DisplayName("GET /api/v1/destinations/{id} returns authentic destination detail with listings and provenance")
    void getDestinationById_jaipur_returnsDetailsAndProvenance() throws Exception {
        mockMvc.perform(get("/api/v1/destinations/jaipur")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.id").value("jaipur"))
                .andExpect(jsonPath("$.data.name").value("Jaipur"))
                .andExpect(jsonPath("$.data.state").value("Rajasthan"))
                .andExpect(jsonPath("$.data.provenance.sourceProvider").value("ARCHAEOLOGICAL_SURVEY_OF_INDIA"))
                .andExpect(jsonPath("$.data.provenance.verificationStatus").value("VERIFIED_OFFICIAL"))
                .andExpect(jsonPath("$.data.provenance.licenseType").isNotEmpty())
                .andExpect(jsonPath("$.data.stays", hasSize(greaterThanOrEqualTo(1))))
                .andExpect(jsonPath("$.data.dining", hasSize(greaterThanOrEqualTo(1))))
                .andExpect(jsonPath("$.data.attractions", hasSize(greaterThanOrEqualTo(2))));
    }

    @Test
    @DisplayName("GET /api/v1/destinations/{id} with invalid ID returns 404 Not Found")
    void getDestinationById_invalidId_returns404() throws Exception {
        mockMvc.perform(get("/api/v1/destinations/invalid-nonexistent-dest")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.code").value("NOT_FOUND"));
    }

    @Test
    @DisplayName("GET /api/v1/listings?destinationId=jaipur returns listings for destination")
    void getListings_byDestination_returnsListings() throws Exception {
        mockMvc.perform(get("/api/v1/listings")
                        .param("destinationId", "jaipur")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data", hasSize(greaterThanOrEqualTo(4))));
    }

    @Test
    @DisplayName("GET /api/v1/listings/{id} returns single listing with authentic provenance")
    void getListingById_amberFort_returnsListing() throws Exception {
        mockMvc.perform(get("/api/v1/listings/jp-att-1")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.id").value("jp-att-1"))
                .andExpect(jsonPath("$.data.name").value("Amber Fort & Palace"))
                .andExpect(jsonPath("$.data.entryFee").value(100.00))
                .andExpect(jsonPath("$.data.provenance.sourceProvider").value("ARCHAEOLOGICAL_SURVEY_OF_INDIA"))
                .andExpect(jsonPath("$.data.provenance.verificationStatus").value("VERIFIED_OFFICIAL"));
    }
}
