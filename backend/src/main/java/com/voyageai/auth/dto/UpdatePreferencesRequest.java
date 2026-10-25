package com.voyageai.auth.dto;

public class UpdatePreferencesRequest {

    private String preferences;

    public UpdatePreferencesRequest() {
    }

    public UpdatePreferencesRequest(String preferences) {
        this.preferences = preferences;
    }

    public String getPreferences() {
        return preferences;
    }

    public void setPreferences(String preferences) {
        this.preferences = preferences;
    }
}
