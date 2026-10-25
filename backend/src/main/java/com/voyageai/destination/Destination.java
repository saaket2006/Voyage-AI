package com.voyageai.destination;

import com.voyageai.listing.TravelListing;
import jakarta.persistence.*;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "destinations")
public class Destination {

    @Id
    @Column(name = "id", length = 64, nullable = false)
    private String id;

    @Column(name = "name", length = 128, nullable = false)
    private String name;

    @Column(name = "state", length = 64, nullable = false)
    private String state;

    @Column(name = "country", length = 64, nullable = false)
    private String country = "India";

    @Column(name = "region", length = 64, nullable = false)
    private String region;

    @Column(name = "tagline", length = 255)
    private String tagline;

    @Column(name = "description", columnDefinition = "TEXT", nullable = false)
    private String description;

    @Column(name = "image_url", length = 512)
    private String imageUrl;

    @Column(name = "hero_image_url", length = 512)
    private String heroImageUrl;

    @Column(name = "travel_styles", length = 255)
    private String travelStyles;

    @Column(name = "budget_tier", length = 32, nullable = false)
    private String budgetTier = "Comfort";

    @Column(name = "recommended_duration_days", nullable = false)
    private int recommendedDurationDays = 3;

    @Column(name = "best_time_to_visit", length = 128)
    private String bestTimeToVisit;

    @Column(name = "latitude")
    private Double latitude;

    @Column(name = "longitude")
    private Double longitude;

    // Provenance Fields
    @Column(name = "source_provider", length = 64, nullable = false)
    private String sourceProvider = "OPEN_STREET_MAP";

    @Column(name = "provider_record_id", length = 128, nullable = false)
    private String providerRecordId;

    @Column(name = "retrieved_at", nullable = false)
    private Instant retrievedAt;

    @Column(name = "stale_after", nullable = false)
    private Instant staleAfter;

    @Column(name = "license_type", length = 64, nullable = false)
    private String licenseType = "ODbL";

    @Column(name = "attribution_text", length = 255, nullable = false)
    private String attributionText;

    @Column(name = "attribution_url", length = 512)
    private String attributionUrl;

    @Column(name = "verification_status", length = 32, nullable = false)
    private String verificationStatus = "UNVERIFIED";

    @Column(name = "image_attribution_text", length = 255)
    private String imageAttributionText;

    @Column(name = "image_license_type", length = 64)
    private String imageLicenseType;

    @Column(name = "image_license_url", length = 512)
    private String imageLicenseUrl;

    @Column(name = "image_source_url", length = 512)
    private String imageSourceUrl;

    @Column(name = "curation_status", length = 32, nullable = false)
    private String curationStatus = "VERIFIED";

    @Column(name = "created_at", nullable = false, updatable = false)
    private Instant createdAt;

    @Column(name = "updated_at", nullable = false)
    private Instant updatedAt;

    @OneToMany(mappedBy = "destination", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<TravelListing> listings = new ArrayList<>();

    public Destination() {
    }

    @PrePersist
    protected void onCreate() {
        if (this.createdAt == null) {
            this.createdAt = Instant.now();
        }
        if (this.updatedAt == null) {
            this.updatedAt = Instant.now();
        }
        if (this.retrievedAt == null) {
            this.retrievedAt = Instant.now();
        }
    }

    @PreUpdate
    protected void onUpdate() {
        this.updatedAt = Instant.now();
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

    public String getTravelStyles() {
        return travelStyles;
    }

    public void setTravelStyles(String travelStyles) {
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

    public String getImageAttributionText() {
        return imageAttributionText;
    }

    public void setImageAttributionText(String imageAttributionText) {
        this.imageAttributionText = imageAttributionText;
    }

    public String getImageLicenseType() {
        return imageLicenseType;
    }

    public void setImageLicenseType(String imageLicenseType) {
        this.imageLicenseType = imageLicenseType;
    }

    public String getImageLicenseUrl() {
        return imageLicenseUrl;
    }

    public void setImageLicenseUrl(String imageLicenseUrl) {
        this.imageLicenseUrl = imageLicenseUrl;
    }

    public String getImageSourceUrl() {
        return imageSourceUrl;
    }

    public void setImageSourceUrl(String imageSourceUrl) {
        this.imageSourceUrl = imageSourceUrl;
    }

    public String getCurationStatus() {
        return curationStatus;
    }

    public void setCurationStatus(String curationStatus) {
        this.curationStatus = curationStatus;
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

    public List<TravelListing> getListings() {
        return listings;
    }

    public void setListings(List<TravelListing> listings) {
        this.listings = listings;
    }
}
