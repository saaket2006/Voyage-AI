package com.voyageai.user;

import com.voyageai.auth.AuthService;
import com.voyageai.auth.dto.UpdatePreferencesRequest;
import com.voyageai.auth.dto.UserProfileResponse;
import com.voyageai.common.api.ApiResponse;
import com.voyageai.security.UserPrincipal;
import com.voyageai.trip.TripService;
import com.voyageai.trip.dto.CreateTripRequest;
import com.voyageai.trip.dto.TripResponse;
import com.voyageai.trip.dto.UpdateTripRequest;
import com.voyageai.user.dto.UpdateProfileRequest;
import com.voyageai.user.dto.UserSyncRequest;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping({"/api/v1/users/me", "/api/v1/me"})
public class UserController {

    private final AuthService authService;
    private final TripService tripService;

    public UserController(AuthService authService, TripService tripService) {
        this.authService = authService;
        this.tripService = tripService;
    }

    @GetMapping
    public ResponseEntity<ApiResponse<UserProfileResponse>> getProfile(
            @AuthenticationPrincipal UserPrincipal principal) {
        UserProfileResponse profile = authService.getProfile(principal.getId());
        return ResponseEntity.ok(ApiResponse.success(profile, "Profile retrieved successfully."));
    }

    @PostMapping("/sync")
    public ResponseEntity<ApiResponse<UserProfileResponse>> syncProfile(
            @AuthenticationPrincipal UserPrincipal principal,
            @RequestBody UserSyncRequest request) {
        UserProfileResponse profile = authService.syncProfile(principal.getId(), request);
        return ResponseEntity.ok(ApiResponse.success(profile, "Profile synchronized successfully."));
    }

    @PatchMapping("/profile")
    public ResponseEntity<ApiResponse<UserProfileResponse>> updateProfile(
            @AuthenticationPrincipal UserPrincipal principal,
            @Valid @RequestBody UpdateProfileRequest request) {
        UserProfileResponse profile = authService.updateProfile(principal.getId(), request);
        return ResponseEntity.ok(ApiResponse.success(profile, "Profile updated successfully."));
    }

    @PutMapping("/preferences")
    public ResponseEntity<ApiResponse<UserProfileResponse>> updatePreferences(
            @AuthenticationPrincipal UserPrincipal principal,
            @RequestBody UpdatePreferencesRequest request) {
        UserProfileResponse profile = authService.updatePreferences(principal.getId(), request);
        return ResponseEntity.ok(ApiResponse.success(profile, "Preferences updated successfully."));
    }

    // --- User Trips Endpoints ---

    @GetMapping("/trips")
    public ResponseEntity<ApiResponse<List<TripResponse>>> getUserTrips(
            @AuthenticationPrincipal UserPrincipal principal) {
        List<TripResponse> trips = tripService.getTripsForUser(principal.getId());
        return ResponseEntity.ok(ApiResponse.success(trips, "Trips retrieved successfully."));
    }

    @PostMapping("/trips")
    public ResponseEntity<ApiResponse<TripResponse>> createUserTrip(
            @AuthenticationPrincipal UserPrincipal principal,
            @Valid @RequestBody CreateTripRequest request) {
        TripResponse response = tripService.createTrip(principal.getId(), request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success(response, "Trip created successfully."));
    }

    @GetMapping("/trips/{tripId}")
    public ResponseEntity<ApiResponse<TripResponse>> getUserTripById(
            @AuthenticationPrincipal UserPrincipal principal,
            @PathVariable String tripId) {
        TripResponse response = tripService.getTripByIdAndUser(tripId, principal.getId());
        return ResponseEntity.ok(ApiResponse.success(response, "Trip retrieved successfully."));
    }

    @PatchMapping("/trips/{tripId}")
    public ResponseEntity<ApiResponse<TripResponse>> patchUserTrip(
            @AuthenticationPrincipal UserPrincipal principal,
            @PathVariable String tripId,
            @Valid @RequestBody UpdateTripRequest request) {
        TripResponse response = tripService.updateTrip(tripId, principal.getId(), request);
        return ResponseEntity.ok(ApiResponse.success(response, "Trip updated successfully."));
    }

    @PutMapping("/trips/{tripId}")
    public ResponseEntity<ApiResponse<TripResponse>> updateUserTrip(
            @AuthenticationPrincipal UserPrincipal principal,
            @PathVariable String tripId,
            @Valid @RequestBody UpdateTripRequest request) {
        TripResponse response = tripService.updateTrip(tripId, principal.getId(), request);
        return ResponseEntity.ok(ApiResponse.success(response, "Trip updated successfully."));
    }

    @DeleteMapping("/trips/{tripId}")
    public ResponseEntity<ApiResponse<Void>> deleteUserTrip(
            @AuthenticationPrincipal UserPrincipal principal,
            @PathVariable String tripId) {
        tripService.deleteTrip(tripId, principal.getId());
        return ResponseEntity.ok(ApiResponse.success(null, "Trip deleted successfully."));
    }
}
