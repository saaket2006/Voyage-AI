package com.voyageai.user;

import com.voyageai.auth.AuthService;
import com.voyageai.auth.dto.UpdatePreferencesRequest;
import com.voyageai.auth.dto.UserProfileResponse;
import com.voyageai.common.api.ApiResponse;
import com.voyageai.security.UserPrincipal;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/me")
public class UserController {

    private final AuthService authService;

    public UserController(AuthService authService) {
        this.authService = authService;
    }

    @GetMapping
    public ResponseEntity<ApiResponse<UserProfileResponse>> getProfile(
            @AuthenticationPrincipal UserPrincipal principal) {
        UserProfileResponse profile = authService.getProfile(principal.getId());
        return ResponseEntity.ok(ApiResponse.success(profile, "Profile retrieved successfully."));
    }

    @PutMapping("/preferences")
    public ResponseEntity<ApiResponse<UserProfileResponse>> updatePreferences(
            @AuthenticationPrincipal UserPrincipal principal,
            @RequestBody UpdatePreferencesRequest request) {
        UserProfileResponse profile = authService.updatePreferences(principal.getId(), request);
        return ResponseEntity.ok(ApiResponse.success(profile, "Preferences updated successfully."));
    }
}
