package com.voyageai.destination.dto;

import com.voyageai.common.dto.ProvenanceDto;

import java.util.List;

public class DestinationSummaryResponse {

    private String id;
    private String name;
    private String state;
    private String country;
    private String region;
    private String tagline;
    private String description;
    private String imageUrl;
    private String heroImageUrl;
    private List<String> travelStyles;
    private String budgetTier;
    private int recommendedDurationDays;
    private String bestTimeToVisit;
    private Double latitude;
    private Double longitude;
    private ProvenanceDto provenance;

    public DestinationSummaryResponse() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region;
    }

    public String getTagline() {
        return tagline;
    }

    public void setTagline(String tagline) {
        this.tagline = tagline;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getHeroImageUrl() {
        return heroImageUrl;
    }

    public void setHeroImageUrl(String heroImageUrl) {
        this.heroImageUrl = heroImageUrl;
    }

    public List<String> getTravelStyles() {
        return travelStyles;
    }

    public void setTravelStyles(List<String> travelStyles) {
        this.travelStyles = travelStyles;
    }

    public String getBudgetTier() {
        return budgetTier;
    }

    public void setBudgetTier(String budgetTier) {
        this.budgetTier = budgetTier;
    }

    public int getRecommendedDurationDays() {
        return recommendedDurationDays;
    }

    public void setRecommendedDurationDays(int recommendedDurationDays) {
        this.recommendedDurationDays = recommendedDurationDays;
    }

    public String getBestTimeToVisit() {
        return bestTimeToVisit;
    }

    public void setBestTimeToVisit(String bestTimeToVisit) {
        this.bestTimeToVisit = bestTimeToVisit;
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    public ProvenanceDto getProvenance() {
        return provenance;
    }

    public void setProvenance(ProvenanceDto provenance) {
        this.provenance = provenance;
    }
}
