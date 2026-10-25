package com.voyageai.search;

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
public class SearchApiTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    @DisplayName("Successfully returns suggestions grouped by CITY, ATTRACTION, DINING for Delhi")
    void testSuggestions_delhi_success() throws Exception {
        mockMvc.perform(get("/api/v1/search/suggestions")
                        .param("q", "Delhi")
                        .param("limit", "5")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.query").value("Delhi"))
                .andExpect(jsonPath("$.groups", hasSize(3)))
                .andExpect(jsonPath("$.groups[0].type").value("CITY"))
                .andExpect(jsonPath("$.groups[0].label").value("Cities"))
                .andExpect(jsonPath("$.groups[0].items[*].name", hasItem("Delhi")))
                .andExpect(jsonPath("$.groups[1].type").value("ATTRACTION"))
                .andExpect(jsonPath("$.groups[1].label").value("Attractions"))
                .andExpect(jsonPath("$.groups[1].items[*].name", hasItem("Red Fort")))
                .andExpect(jsonPath("$.groups[2].type").value("DINING"))
                .andExpect(jsonPath("$.groups[2].label").value("Dining"))
                .andExpect(jsonPath("$.groups[2].items[*].name", hasItem("Karim's")))
                .andExpect(jsonPath("$.meta.limitPerGroup").value(5))
                .andExpect(jsonPath("$.meta.totalResults", greaterThanOrEqualTo(3)));
    }

    @Test
    @DisplayName("Successfully returns suggestions grouped by CITY, ATTRACTION, DINING for Jaipur")
    void testSuggestions_jaipur_success() throws Exception {
        mockMvc.perform(get("/api/v1/search/suggestions")
                        .param("q", "Jaipur")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.query").value("Jaipur"))
                .andExpect(jsonPath("$.groups[0].type").value("CITY"))
                .andExpect(jsonPath("$.groups[0].items[0].name").value("Jaipur"))
                .andExpect(jsonPath("$.groups[0].items[0].route").value("/destinations/jaipur"))
                .andExpect(jsonPath("$.groups[1].type").value("ATTRACTION"))
                .andExpect(jsonPath("$.groups[1].items[*].name", hasItem("Amber Fort & Palace")))
                .andExpect(jsonPath("$.groups[2].type").value("DINING"))
                .andExpect(jsonPath("$.groups[2].items[0].name").value("Laxmi Mishthan Bhandar (LMB)"));
    }

    @Test
    @DisplayName("Returns empty groups in strict order when query matches no catalog items")
    void testSuggestions_emptyResults() throws Exception {
        mockMvc.perform(get("/api/v1/search/suggestions")
                        .param("q", "NonExistentAtlantisPlace999")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.query").value("NonExistentAtlantisPlace999"))
                .andExpect(jsonPath("$.groups", hasSize(3)))
                .andExpect(jsonPath("$.groups[0].type").value("CITY"))
                .andExpect(jsonPath("$.groups[0].items", hasSize(0)))
                .andExpect(jsonPath("$.groups[1].type").value("ATTRACTION"))
                .andExpect(jsonPath("$.groups[1].items", hasSize(0)))
                .andExpect(jsonPath("$.groups[2].type").value("DINING"))
                .andExpect(jsonPath("$.groups[2].items", hasSize(0)))
                .andExpect(jsonPath("$.meta.totalResults").value(0));
    }

    @Test
    @DisplayName("Respects limit parameter per category")
    void testSuggestions_respectsLimit() throws Exception {
        mockMvc.perform(get("/api/v1/search/suggestions")
                        .param("q", "Delhi")
                        .param("limit", "1")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.meta.limitPerGroup").value(1))
                .andExpect(jsonPath("$.groups[0].items.length()", lessThanOrEqualTo(1)))
                .andExpect(jsonPath("$.groups[1].items.length()", lessThanOrEqualTo(1)))
                .andExpect(jsonPath("$.groups[2].items.length()", lessThanOrEqualTo(1)));
    }

    @Test
    @DisplayName("Fails with 400 Bad Request when search query is missing or blank")
    void testSuggestions_missingOrBlankQuery() throws Exception {
        // Missing query parameter
        mockMvc.perform(get("/api/v1/search/suggestions")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value("INVALID_SEARCH_QUERY"))
                .andExpect(jsonPath("$.status").value(400));

        // Blank query parameter
        mockMvc.perform(get("/api/v1/search/suggestions")
                        .param("q", "   ")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value("INVALID_SEARCH_QUERY"))
                .andExpect(jsonPath("$.message").value("The search query must contain between 1 and 100 characters."));
    }

    @Test
    @DisplayName("Fails with 400 Bad Request when search query exceeds 100 characters")
    void testSuggestions_queryExceedsLimit() throws Exception {
        String longQuery = "A".repeat(101);
        mockMvc.perform(get("/api/v1/search/suggestions")
                        .param("q", longQuery)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value("INVALID_SEARCH_QUERY"))
                .andExpect(jsonPath("$.status").value(400));
    }

    @Test
    @DisplayName("Fails with 400 Bad Request when limit is outside 1-10 range")
    void testSuggestions_invalidLimitRange() throws Exception {
        // limit = 0
        mockMvc.perform(get("/api/v1/search/suggestions")
                        .param("q", "Jaipur")
                        .param("limit", "0")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value("INVALID_SEARCH_LIMIT"))
                .andExpect(jsonPath("$.message").value("The limit parameter must be an integer between 1 and 10."));

        // limit = 11
        mockMvc.perform(get("/api/v1/search/suggestions")
                        .param("q", "Jaipur")
                        .param("limit", "11")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value("INVALID_SEARCH_LIMIT"));
    }
}
