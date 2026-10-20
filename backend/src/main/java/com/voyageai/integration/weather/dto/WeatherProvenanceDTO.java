package com.voyageai.integration.weather.dto;

import java.time.Instant;

/**
 * Data provenance tracking for weather information, in accordance with
 * Voyage-AI Data Authenticity and Provenance Requirements.
 */
public class WeatherProvenanceDTO {

    private String sourceProvider;
    private Instant retrievedAt;
    private String attribution;
    private String license;
    private boolean isForecast;

    public WeatherProvenanceDTO() {
    }

    public WeatherProvenanceDTO(String sourceProvider, Instant retrievedAt, String attribution, String license, boolean isForecast) {
        this.sourceProvider = sourceProvider;
        this.retrievedAt = retrievedAt;
        this.attribution = attribution;
        this.license = license;
        this.isForecast = isForecast;
    }

    public String getSourceProvider() {
        return sourceProvider;
    }

    public void setSourceProvider(String sourceProvider) {
        this.sourceProvider = sourceProvider;
    }

    public Instant getRetrievedAt() {
        return retrievedAt;
    }

    public void setRetrievedAt(Instant retrievedAt) {
        this.retrievedAt = retrievedAt;
    }

    public String getAttribution() {
        return attribution;
    }

    public void setAttribution(String attribution) {
        this.attribution = attribution;
    }

    public String getLicense() {
        return license;
    }

    public void setLicense(String license) {
        this.license = license;
    }

    public boolean isForecast() {
        return isForecast;
    }

    public void setForecast(boolean forecast) {
        isForecast = forecast;
    }
}
