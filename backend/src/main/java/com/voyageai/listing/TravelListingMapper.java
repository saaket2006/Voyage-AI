package com.voyageai.listing;

import com.voyageai.common.dto.ProvenanceDto;
import com.voyageai.listing.dto.TravelListingResponse;

public class TravelListingMapper {

    public static TravelListingResponse toResponse(TravelListing listing) {
        if (listing == null) {
            return null;
        }

        TravelListingResponse dto = new TravelListingResponse();
        dto.setId(listing.getId());
        if (listing.getDestination() != null) {
            dto.setDestinationId(listing.getDestination().getId());
            dto.setDestinationName(listing.getDestination().getName());
        }
        dto.setListingType(listing.getListingType());
        dto.setName(listing.getName());
        dto.setCategory(listing.getCategory());
        dto.setDescription(listing.getDescription());
        dto.setImageUrl(listing.getImageUrl());
        dto.setPriceEstimate(listing.getPriceEstimate());
        dto.setPriceRange(listing.getPriceRange());
        dto.setCurrency(listing.getCurrency());
        dto.setRating(listing.getRating());
        dto.setMustTry(listing.getMustTry());
        dto.setEstimatedHours(listing.getEstimatedHours());
        dto.setEntryFee(listing.getEntryFee());
        dto.setHighlights(listing.getHighlights());
        dto.setAddress(listing.getAddress());
        dto.setLatitude(listing.getLatitude());
        dto.setLongitude(listing.getLongitude());

        ProvenanceDto provenance = new ProvenanceDto(
                listing.getSourceProvider(),
                listing.getProviderRecordId(),
                listing.getRetrievedAt(),
                listing.getStaleAfter(),
                listing.getLicenseType(),
                listing.getAttributionText(),
                listing.getAttributionUrl(),
                listing.getVerificationStatus()
        );
        dto.setProvenance(provenance);

        return dto;
    }
}
