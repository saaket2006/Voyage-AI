package com.voyageai.listing;

import com.voyageai.common.exception.ResourceNotFoundException;
import com.voyageai.listing.dto.TravelListingResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class TravelListingService {

    private final TravelListingRepository listingRepository;

    public TravelListingService(TravelListingRepository listingRepository) {
        this.listingRepository = listingRepository;
    }

    @Transactional(readOnly = true)
    public List<TravelListingResponse> getListings(String destinationId, ListingType listingType) {
        List<TravelListing> listings;
        if (destinationId != null && !destinationId.isBlank() && listingType != null) {
            listings = listingRepository.findByDestinationIdAndListingType(destinationId, listingType);
        } else if (destinationId != null && !destinationId.isBlank()) {
            listings = listingRepository.findByDestinationId(destinationId);
        } else if (listingType != null) {
            listings = listingRepository.findByListingType(listingType);
        } else {
            listings = listingRepository.findAll();
        }

        return listings.stream()
                .map(TravelListingMapper::toResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public TravelListingResponse getListingById(String id) {
        TravelListing listing = listingRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Travel listing not found with id: " + id));

        return TravelListingMapper.toResponse(listing);
    }
}
