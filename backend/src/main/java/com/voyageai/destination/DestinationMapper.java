package com.voyageai.destination;

import com.voyageai.common.dto.ProvenanceDto;
import com.voyageai.destination.dto.DestinationDetailResponse;
import com.voyageai.destination.dto.DestinationSummaryResponse;
import com.voyageai.listing.ListingType;
import com.voyageai.listing.TravelListingMapper;
import com.voyageai.listing.dto.TravelListingResponse;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class DestinationMapper {

    public static DestinationSummaryResponse toSummary(Destination destination) {
        if (destination == null) {
            return null;
        }

        DestinationSummaryResponse dto = new DestinationSummaryResponse();
        mapSummaryFields(destination, dto);
        return dto;
    }

    public static DestinationDetailResponse toDetail(Destination destination) {
        if (destination == null) {
            return null;
        }

        DestinationDetailResponse dto = new DestinationDetailResponse();
        mapSummaryFields(destination, dto);

        if (destination.getListings() != null) {
            List<TravelListingResponse> allListings = new ArrayList<>();
            List<TravelListingResponse> stays = new ArrayList<>();
            List<TravelListingResponse> dining = new ArrayList<>();
            List<TravelListingResponse> attractions = new ArrayList<>();

            destination.getListings().forEach(listing -> {
                TravelListingResponse listingDto = TravelListingMapper.toResponse(listing);
                allListings.add(listingDto);
                if (listing.getListingType() == ListingType.ACCOMMODATION) {
                    stays.add(listingDto);
                } else if (listing.getListingType() == ListingType.RESTAURANT) {
                    dining.add(listingDto);
                } else if (listing.getListingType() == ListingType.ATTRACTION || listing.getListingType() == ListingType.ACTIVITY) {
                    attractions.add(listingDto);
                }
            });

            dto.setListings(allListings);
            dto.setStays(stays);
            dto.setDining(dining);
            dto.setAttractions(attractions);
        }

        return dto;
    }

    private static void mapSummaryFields(Destination destination, DestinationSummaryResponse dto) {
        dto.setId(destination.getId());
        dto.setName(destination.getName());
        dto.setState(destination.getState());
        dto.setCountry(destination.getCountry());
        dto.setRegion(destination.getRegion());
        dto.setTagline(destination.getTagline());
        dto.setDescription(destination.getDescription());
        dto.setImageUrl(destination.getImageUrl());
        dto.setHeroImageUrl(destination.getHeroImageUrl());

        if (destination.getTravelStyles() != null && !destination.getTravelStyles().isBlank()) {
            dto.setTravelStyles(Arrays.stream(destination.getTravelStyles().split(","))
                    .map(String::trim)
                    .filter(s -> !s.isEmpty())
                    .toList());
        } else {
            dto.setTravelStyles(List.of());
        }

        dto.setBudgetTier(destination.getBudgetTier());
        dto.setRecommendedDurationDays(destination.getRecommendedDurationDays());
        dto.setBestTimeToVisit(destination.getBestTimeToVisit());
        dto.setLatitude(destination.getLatitude());
        dto.setLongitude(destination.getLongitude());

        ProvenanceDto provenance = new ProvenanceDto(
                destination.getSourceProvider(),
                destination.getProviderRecordId(),
                destination.getRetrievedAt(),
                destination.getStaleAfter(),
                destination.getLicenseType(),
                destination.getAttributionText(),
                destination.getAttributionUrl(),
                destination.getVerificationStatus(),
                destination.getImageAttributionText(),
                destination.getImageLicenseType(),
                destination.getImageLicenseUrl(),
                destination.getImageSourceUrl(),
                destination.getCurationStatus()
        );
        dto.setProvenance(provenance);
    }
}
