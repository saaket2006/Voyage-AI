package com.voyageai.listing;

import com.voyageai.common.api.ApiResponse;
import com.voyageai.listing.dto.TravelListingResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/listings")
public class TravelListingController {

    private final TravelListingService listingService;

    public TravelListingController(TravelListingService listingService) {
        this.listingService = listingService;
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<TravelListingResponse>>> getListings(
            @RequestParam(required = false) String destinationId,
            @RequestParam(required = false) ListingType type) {
        List<TravelListingResponse> listings = listingService.getListings(destinationId, type);
        return ResponseEntity.ok(ApiResponse.success(listings, "Travel listings retrieved successfully."));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<TravelListingResponse>> getListingById(@PathVariable String id) {
        TravelListingResponse listing = listingService.getListingById(id);
        return ResponseEntity.ok(ApiResponse.success(listing, "Travel listing retrieved successfully."));
    }
}
