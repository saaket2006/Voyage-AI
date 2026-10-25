package com.voyageai.user.dto;

public class UserSyncRequest {

    private String email;
    private String displayName;
    private String profileImageUrl;
    private String authProvider;
    private String providerSubject;

    public UserSyncRequest() {
    }

    public UserSyncRequest(String email, String displayName, String profileImageUrl, String authProvider, String providerSubject) {
        this.email = email;
        this.displayName = displayName;
        this.profileImageUrl = profileImageUrl;
        this.authProvider = authProvider;
        this.providerSubject = providerSubject;
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

    public String getProviderSubject() {
        return providerSubject;
    }

    public void setProviderSubject(String providerSubject) {
        this.providerSubject = providerSubject;
    }
}
