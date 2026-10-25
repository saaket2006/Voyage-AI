package com.voyageai.trip;

import com.voyageai.common.api.ApiResponse;
import com.voyageai.security.UserPrincipal;
import com.voyageai.trip.dto.CreateTripRequest;
import com.voyageai.trip.dto.TripResponse;
import com.voyageai.trip.dto.UpdateTripRequest;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/trips")
public class TripController {

    private final TripService tripService;

    public TripController(TripService tripService) {
        this.tripService = tripService;
    }

    @PostMapping
    public ResponseEntity<ApiResponse<TripResponse>> createTrip(
            @Valid @RequestBody CreateTripRequest request,
            @AuthenticationPrincipal UserPrincipal principal) {
        TripResponse response = tripService.createTrip(principal.getId(), request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success(response, "Trip created successfully."));
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<TripResponse>>> getTrips(
            @AuthenticationPrincipal UserPrincipal principal) {
        List<TripResponse> response = tripService.getTripsForUser(principal.getId());
        return ResponseEntity.ok(ApiResponse.success(response, "Trips retrieved successfully."));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<TripResponse>> getTripById(
            @PathVariable String id,
            @AuthenticationPrincipal UserPrincipal principal) {
        TripResponse response = tripService.getTripByIdAndUser(id, principal.getId());
        return ResponseEntity.ok(ApiResponse.success(response, "Trip retrieved successfully."));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<TripResponse>> updateTrip(
            @PathVariable String id,
            @Valid @RequestBody UpdateTripRequest request,
            @AuthenticationPrincipal UserPrincipal principal) {
        TripResponse response = tripService.updateTrip(id, principal.getId(), request);
        return ResponseEntity.ok(ApiResponse.success(response, "Trip updated successfully."));
    }

    @PatchMapping("/{id}")
    public ResponseEntity<ApiResponse<TripResponse>> patchTrip(
            @PathVariable String id,
            @Valid @RequestBody UpdateTripRequest request,
            @AuthenticationPrincipal UserPrincipal principal) {
        TripResponse response = tripService.updateTrip(id, principal.getId(), request);
        return ResponseEntity.ok(ApiResponse.success(response, "Trip updated successfully."));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteTrip(
            @PathVariable String id,
            @AuthenticationPrincipal UserPrincipal principal) {
        tripService.deleteTrip(id, principal.getId());
        return ResponseEntity.ok(ApiResponse.success(null, "Trip deleted successfully."));
    }
}
