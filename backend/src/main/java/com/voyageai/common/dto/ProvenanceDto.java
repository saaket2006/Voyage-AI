package com.voyageai.common.dto;

import java.time.Instant;

public class ProvenanceDto {

    private String sourceProvider;
    private String providerRecordId;
    private Instant retrievedAt;
    private Instant staleAfter;
    private String licenseType;
    private String attributionText;
    private String attributionUrl;
    private String verificationStatus;

    public ProvenanceDto() {
    }

    public ProvenanceDto(String sourceProvider, String providerRecordId, Instant retrievedAt,
                         Instant staleAfter, String licenseType, String attributionText,
                         String attributionUrl, String verificationStatus) {
        this.sourceProvider = sourceProvider;
        this.providerRecordId = providerRecordId;
        this.retrievedAt = retrievedAt;
        this.staleAfter = staleAfter;
        this.licenseType = licenseType;
        this.attributionText = attributionText;
        this.attributionUrl = attributionUrl;
        this.verificationStatus = verificationStatus;
    }

    public String getSourceProvider() {
        return sourceProvider;
    }

    public void setSourceProvider(String sourceProvider) {
        this.sourceProvider = sourceProvider;
    }

    public String getProviderRecordId() {
        return providerRecordId;
    }

    public void setProviderRecordId(String providerRecordId) {
        this.providerRecordId = providerRecordId;
    }

    public Instant getRetrievedAt() {
        return retrievedAt;
    }

    public void setRetrievedAt(Instant retrievedAt) {
        this.retrievedAt = retrievedAt;
    }

    public Instant getStaleAfter() {
        return staleAfter;
    }

    public void setStaleAfter(Instant staleAfter) {
        this.staleAfter = staleAfter;
    }

    public String getLicenseType() {
        return licenseType;
    }

    public void setLicenseType(String licenseType) {
        this.licenseType = licenseType;
    }

    public String getAttributionText() {
        return attributionText;
    }

    public void setAttributionText(String attributionText) {
        this.attributionText = attributionText;
    }

    public String getAttributionUrl() {
        return attributionUrl;
    }

    public void setAttributionUrl(String attributionUrl) {
        this.attributionUrl = attributionUrl;
    }

    public String getVerificationStatus() {
        return verificationStatus;
    }

    public void setVerificationStatus(String verificationStatus) {
        this.verificationStatus = verificationStatus;
    }
}
