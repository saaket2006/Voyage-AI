package com.voyageai.auth.dto;

import java.time.Instant;

public class UserProfileResponse {

    private String id;
    private String email;
    private String displayName;
    private String preferences;
    private Instant createdAt;

    public UserProfileResponse() {
    }

    public UserProfileResponse(String id, String email, String displayName, String preferences, Instant createdAt) {
        this.id = id;
        this.email = email;
        this.displayName = displayName;
        this.preferences = preferences;
        this.createdAt = createdAt;
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

    public String getPreferences() {
        return preferences;
    }

    public void setPreferences(String preferences) {
        this.preferences = preferences;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }
}
