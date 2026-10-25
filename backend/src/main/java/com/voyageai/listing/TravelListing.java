package com.voyageai.listing;

import com.voyageai.destination.Destination;
import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.Instant;

@Entity
@Table(name = "travel_listings")
public class TravelListing {

    @Id
    @Column(length = 64, nullable = false)
    private String id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "destination_id", nullable = false)
    private Destination destination;

    @Enumerated(EnumType.STRING)
    @Column(name = "listing_type", length = 32, nullable = false)
    private ListingType listingType;

    @Column(length = 128, nullable = false)
    private String name;

    @Column(length = 64)
    private String category;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Column(name = "image_url", length = 512)
    private String imageUrl;

    @Column(name = "price_estimate", precision = 10, scale = 2)
    private BigDecimal priceEstimate;

    @Column(name = "price_range", length = 16)
    private String priceRange;

    @Column(length = 8, nullable = false)
    private String currency = "INR";

    @Column(precision = 3, scale = 2)
    private BigDecimal rating;

    @Column(name = "must_try", length = 128)
    private String mustTry;

    @Column(name = "estimated_hours", precision = 4, scale = 2)
    private BigDecimal estimatedHours;

    @Column(name = "entry_fee", precision = 10, scale = 2)
    private BigDecimal entryFee;

    @Column(columnDefinition = "TEXT")
    private String highlights;

    @Column(length = 255)
    private String address;

    private Double latitude;

    private Double longitude;

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

    @Column(name = "created_at", nullable = false, updatable = false)
    private Instant createdAt;

    @Column(name = "updated_at", nullable = false)
    private Instant updatedAt;

    public TravelListing() {
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
        if (this.staleAfter == null) {
            this.staleAfter = Instant.now().plusSeconds(86400 * 30);
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

    public Destination getDestination() {
        return destination;
    }

    public void setDestination(Destination destination) {
        this.destination = destination;
    }

    public ListingType getListingType() {
        return listingType;
    }

    public void setListingType(ListingType listingType) {
        this.listingType = listingType;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
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

    public BigDecimal getPriceEstimate() {
        return priceEstimate;
    }

    public void setPriceEstimate(BigDecimal priceEstimate) {
        this.priceEstimate = priceEstimate;
    }

    public String getPriceRange() {
        return priceRange;
    }

    public void setPriceRange(String priceRange) {
        this.priceRange = priceRange;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public BigDecimal getRating() {
        return rating;
    }

    public void setRating(BigDecimal rating) {
        this.rating = rating;
    }

    public String getMustTry() {
        return mustTry;
    }

    public void setMustTry(String mustTry) {
        this.mustTry = mustTry;
    }

    public BigDecimal getEstimatedHours() {
        return estimatedHours;
    }

    public void setEstimatedHours(BigDecimal estimatedHours) {
        this.estimatedHours = estimatedHours;
    }

    public BigDecimal getEntryFee() {
        return entryFee;
    }

    public void setEntryFee(BigDecimal entryFee) {
        this.entryFee = entryFee;
    }

    public String getHighlights() {
        return highlights;
    }

    public void setHighlights(String highlights) {
        this.highlights = highlights;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
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
