package com.voyageai.listing;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TravelListingRepository extends JpaRepository<TravelListing, String> {

    List<TravelListing> findByDestinationId(String destinationId);

    List<TravelListing> findByDestinationIdAndListingType(String destinationId, ListingType listingType);

    List<TravelListing> findByListingType(ListingType listingType);

    @Query("SELECT l FROM TravelListing l JOIN FETCH l.destination d WHERE " +
           "l.listingType = :listingType AND (" +
           "d.id IN :destIds OR " +
           "LOWER(d.name) LIKE LOWER(CONCAT('%', :query, '%')) OR " +
           "LOWER(l.name) LIKE LOWER(CONCAT('%', :query, '%')) OR " +
           "LOWER(l.category) LIKE LOWER(CONCAT('%', :query, '%'))) " +
           "ORDER BY CASE WHEN LOWER(l.name) LIKE LOWER(CONCAT(:query, '%')) THEN 0 " +
           "              WHEN LOWER(d.name) LIKE LOWER(CONCAT(:query, '%')) THEN 1 ELSE 2 END, l.name ASC")
    List<TravelListing> searchSuggestionsWithDestIds(@Param("query") String query,
                                                     @Param("destIds") List<String> destIds,
                                                     @Param("listingType") ListingType listingType,
                                                     Pageable pageable);

    @Query("SELECT l FROM TravelListing l JOIN FETCH l.destination d WHERE " +
           "l.listingType = :listingType AND (" +
           "LOWER(d.name) LIKE LOWER(CONCAT('%', :query, '%')) OR " +
           "LOWER(d.state) LIKE LOWER(CONCAT('%', :query, '%')) OR " +
           "LOWER(l.name) LIKE LOWER(CONCAT('%', :query, '%')) OR " +
           "LOWER(l.category) LIKE LOWER(CONCAT('%', :query, '%'))) " +
           "ORDER BY CASE WHEN LOWER(l.name) LIKE LOWER(CONCAT(:query, '%')) THEN 0 " +
           "              WHEN LOWER(d.name) LIKE LOWER(CONCAT(:query, '%')) THEN 1 ELSE 2 END, l.name ASC")
    List<TravelListing> searchSuggestionsWithoutDestIds(@Param("query") String query,
                                                        @Param("listingType") ListingType listingType,
                                                        Pageable pageable);
}
