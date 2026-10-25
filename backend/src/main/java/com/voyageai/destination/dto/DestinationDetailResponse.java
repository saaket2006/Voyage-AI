package com.voyageai.destination.dto;

import com.voyageai.listing.dto.TravelListingResponse;

import java.util.ArrayList;
import java.util.List;

public class DestinationDetailResponse extends DestinationSummaryResponse {

    private List<TravelListingResponse> listings = new ArrayList<>();
    private List<TravelListingResponse> stays = new ArrayList<>();
    private List<TravelListingResponse> dining = new ArrayList<>();
    private List<TravelListingResponse> attractions = new ArrayList<>();

    public DestinationDetailResponse() {
    }

    public List<TravelListingResponse> getListings() {
        return listings;
    }

    public void setListings(List<TravelListingResponse> listings) {
        this.listings = listings;
    }

    public List<TravelListingResponse> getStays() {
        return stays;
    }

    public void setStays(List<TravelListingResponse> stays) {
        this.stays = stays;
    }

    public List<TravelListingResponse> getDining() {
        return dining;
    }

    public void setDining(List<TravelListingResponse> dining) {
        this.dining = dining;
    }

    public List<TravelListingResponse> getAttractions() {
        return attractions;
    }

    public void setAttractions(List<TravelListingResponse> attractions) {
        this.attractions = attractions;
    }
}
