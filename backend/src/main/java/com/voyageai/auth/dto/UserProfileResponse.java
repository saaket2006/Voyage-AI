package com.voyageai.auth.dto;

import java.time.Instant;

public class UserProfileResponse {

    private String id;
    private String email;
    private String displayName;
    private String profileImageUrl;
    private String authProvider;
    private String preferences;
    private String role;
    private Instant createdAt;
    private Instant updatedAt;

    public UserProfileResponse() {
    }

    public UserProfileResponse(String id, String email, String displayName, String preferences, Instant createdAt) {
        this.id = id;
        this.email = email;
        this.displayName = displayName;
        this.preferences = preferences;
        this.createdAt = createdAt;
        this.role = "ROLE_USER";
    }

    public UserProfileResponse(String id, String email, String displayName, String profileImageUrl, String authProvider, String preferences, Instant createdAt, Instant updatedAt) {
        this(id, email, displayName, profileImageUrl, authProvider, preferences, "ROLE_USER", createdAt, updatedAt);
    }

    public UserProfileResponse(String id, String email, String displayName, String profileImageUrl, String authProvider, String preferences, String role, Instant createdAt, Instant updatedAt) {
        this.id = id;
        this.email = email;
        this.displayName = displayName;
        this.profileImageUrl = profileImageUrl;
        this.authProvider = authProvider;
        this.preferences = preferences;
        this.role = role != null ? role : "ROLE_USER";
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
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

    public String getAuthProvider() {
        return authProvider;
    }

    public void setAuthProvider(String authProvider) {
        this.authProvider = authProvider;
    }

    public String getPreferences() {
        return preferences;
    }

    public void setPreferences(String preferences) {
        this.preferences = preferences;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }

    public Instant getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Instant updatedAt) {
        this.updatedAt = updatedAt;
    }
}
