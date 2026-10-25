package com.voyageai.user.dto;

import jakarta.validation.constraints.Size;

public class UpdateProfileRequest {

    @Size(max = 100, message = "Display name cannot exceed 100 characters")
    private String displayName;

    @Size(max = 512, message = "Profile image URL cannot exceed 512 characters")
    private String profileImageUrl;

    private String preferences;

    public UpdateProfileRequest() {
    }

    public UpdateProfileRequest(String displayName, String profileImageUrl, String preferences) {
        this.displayName = displayName;
        this.profileImageUrl = profileImageUrl;
        this.preferences = preferences;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public String getProfileImageUrl() {
        return profileImageUrl;
    }

    public void setProfileImageUrl(String profileImageUrl) {
        this.profileImageUrl = profileImageUrl;
    }

    public String getPreferences() {
        return preferences;
    }

    public void setPreferences(String preferences) {
        this.preferences = preferences;
    }
}
