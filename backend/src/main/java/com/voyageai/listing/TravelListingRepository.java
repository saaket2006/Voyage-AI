package com.voyageai.listing;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TravelListingRepository extends JpaRepository<TravelListing, String> {

    List<TravelListing> findByDestinationId(String destinationId);

    List<TravelListing> findByDestinationIdAndListingType(String destinationId, ListingType listingType);

    List<TravelListing> findByListingType(ListingType listingType);
}
