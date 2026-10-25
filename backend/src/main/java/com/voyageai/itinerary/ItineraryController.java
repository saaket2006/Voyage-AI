package com.voyageai.itinerary;

import com.voyageai.common.api.ApiResponse;
import com.voyageai.itinerary.dto.*;
import com.voyageai.security.UserPrincipal;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/trips/{tripId}")
public class ItineraryController {

    private final ItineraryService itineraryService;

    public ItineraryController(ItineraryService itineraryService) {
        this.itineraryService = itineraryService;
    }

    @PostMapping("/days")
    public ResponseEntity<ApiResponse<ItineraryDayResponse>> addDay(
            @PathVariable String tripId,
            @Valid @RequestBody CreateItineraryDayRequest request,
            @AuthenticationPrincipal UserPrincipal principal) {
        ItineraryDayResponse response = itineraryService.addDayToTrip(tripId, principal.getId(), request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success(response, "Itinerary day added successfully."));
    }

    @PostMapping("/days/{dayId}/items")
    public ResponseEntity<ApiResponse<ItineraryItemResponse>> addItem(
            @PathVariable String tripId,
            @PathVariable String dayId,
            @Valid @RequestBody CreateItineraryItemRequest request,
            @AuthenticationPrincipal UserPrincipal principal) {
        ItineraryItemResponse response = itineraryService.addItemToDay(tripId, dayId, principal.getId(), request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success(response, "Itinerary item added successfully."));
    }

    @PutMapping("/days/{dayId}/items/{itemId}")
    public ResponseEntity<ApiResponse<ItineraryItemResponse>> updateItem(
            @PathVariable String tripId,
            @PathVariable String dayId,
            @PathVariable String itemId,
            @Valid @RequestBody UpdateItineraryItemRequest request,
            @AuthenticationPrincipal UserPrincipal principal) {
        ItineraryItemResponse response = itineraryService.updateItem(tripId, dayId, itemId, principal.getId(), request);
        return ResponseEntity.ok(ApiResponse.success(response, "Itinerary item updated successfully."));
    }

    @DeleteMapping("/days/{dayId}/items/{itemId}")
    public ResponseEntity<ApiResponse<Void>> deleteItem(
            @PathVariable String tripId,
            @PathVariable String dayId,
            @PathVariable String itemId,
            @AuthenticationPrincipal UserPrincipal principal) {
        itineraryService.deleteItem(tripId, dayId, itemId, principal.getId());
        return ResponseEntity.ok(ApiResponse.success(null, "Itinerary item deleted successfully."));
    }
}
