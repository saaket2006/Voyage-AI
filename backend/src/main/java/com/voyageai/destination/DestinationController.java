package com.voyageai.destination;

import com.voyageai.common.api.ApiResponse;
import com.voyageai.destination.dto.DestinationDetailResponse;
import com.voyageai.destination.dto.DestinationSummaryResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/destinations")
public class DestinationController {

    private final DestinationService destinationService;

    public DestinationController(DestinationService destinationService) {
        this.destinationService = destinationService;
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<DestinationSummaryResponse>>> getDestinations(
            @RequestParam(required = false) String search,
            @RequestParam(required = false) String query,
            @RequestParam(required = false) String region,
            @RequestParam(required = false) String travelStyle,
            @RequestParam(required = false) String budgetTier,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "50") int size) {

        String searchTerm = search != null ? search : query;
        PageRequest pageRequest = PageRequest.of(page, size, Sort.by("name").ascending());

        Page<DestinationSummaryResponse> pageResult = destinationService.searchDestinations(
                searchTerm, region, travelStyle, budgetTier, pageRequest);

        List<DestinationSummaryResponse> destinations = pageResult.getContent();
        return ResponseEntity.ok(ApiResponse.success(destinations, "Destinations retrieved successfully."));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<DestinationDetailResponse>> getDestinationById(@PathVariable String id) {
        DestinationDetailResponse destination = destinationService.getDestinationById(id);
        return ResponseEntity.ok(ApiResponse.success(destination, "Destination retrieved successfully."));
    }
}
