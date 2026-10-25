package com.voyageai.itinerary.dto;

import com.voyageai.listing.dto.TravelListingResponse;

import java.math.BigDecimal;
import java.time.Instant;

public class ItineraryItemResponse {

    private String id;
    private String itineraryDayId;
    private String listingId;
    private String title;
    private String category;
    private String notes;
    private String suggestedTime;
    private Integer durationMinutes;
    private BigDecimal estimatedCost;
    private String currency;
    private int itemOrder;
    private String originType;
    private Instant createdAt;
    private TravelListingResponse listing;

    public ItineraryItemResponse() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getItineraryDayId() {
        return itineraryDayId;
    }

    public void setItineraryDayId(String itineraryDayId) {
        this.itineraryDayId = itineraryDayId;
    }

    public String getListingId() {
        return listingId;
    }

    public void setListingId(String listingId) {
        this.listingId = listingId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public String getSuggestedTime() {
        return suggestedTime;
    }

    public void setSuggestedTime(String suggestedTime) {
        this.suggestedTime = suggestedTime;
    }

    public Integer getDurationMinutes() {
        return durationMinutes;
    }

    public void setDurationMinutes(Integer durationMinutes) {
        this.durationMinutes = durationMinutes;
    }

    public BigDecimal getEstimatedCost() {
        return estimatedCost;
    }

    public void setEstimatedCost(BigDecimal estimatedCost) {
        this.estimatedCost = estimatedCost;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public int getItemOrder() {
        return itemOrder;
    }

    public void setItemOrder(int itemOrder) {
        this.itemOrder = itemOrder;
    }

    public String getOriginType() {
        return originType;
    }

    public void setOriginType(String originType) {
        this.originType = originType;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }

    public TravelListingResponse getListing() {
        return listing;
    }

    public void setListing(TravelListingResponse listing) {
        this.listing = listing;
    }
}
