package com.voyageai.search;

import com.voyageai.search.dto.SearchSuggestionResponse;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/search")
public class SearchController {

    private final SearchService searchService;

    public SearchController(SearchService searchService) {
        this.searchService = searchService;
    }

    @GetMapping("/suggestions")
    public ResponseEntity<SearchSuggestionResponse> getSuggestions(
            @RequestParam(name = "q", required = false) String q,
            @RequestParam(name = "limit", required = false, defaultValue = "5") Integer limit,
            HttpServletRequest request) {

        if (q == null || q.trim().isEmpty() || q.trim().length() > 100) {
            throw new SearchValidationException(
                    "INVALID_SEARCH_QUERY",
                    "The search query must contain between 1 and 100 characters.",
                    HttpStatus.BAD_REQUEST
            );
        }

        if (limit == null || limit < 1 || limit > 10) {
            throw new SearchValidationException(
                    "INVALID_SEARCH_LIMIT",
                    "The limit parameter must be an integer between 1 and 10.",
                    HttpStatus.BAD_REQUEST
            );
        }

        SearchSuggestionResponse response = searchService.getSuggestions(q.trim(), limit);
        return ResponseEntity.ok(response);
    }
}
