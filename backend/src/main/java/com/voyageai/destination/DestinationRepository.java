package com.voyageai.destination;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DestinationRepository extends JpaRepository<Destination, String> {

    @Query("SELECT d FROM Destination d WHERE " +
           "(:search IS NULL OR :search = '' OR " +
           " LOWER(d.name) LIKE LOWER(CONCAT('%', :search, '%')) OR " +
           " LOWER(d.state) LIKE LOWER(CONCAT('%', :search, '%')) OR " +
           " LOWER(d.description) LIKE LOWER(CONCAT('%', :search, '%')) OR " +
           " LOWER(d.tagline) LIKE LOWER(CONCAT('%', :search, '%'))) AND " +
           "(:region IS NULL OR :region = '' OR :region = 'All' OR " +
           " LOWER(d.region) = LOWER(:region) OR " +
           " LOWER(d.region) LIKE LOWER(CONCAT(:region, '%')) OR " +
           " LOWER(:region) LIKE LOWER(CONCAT(d.region, '%'))) AND " +
           "(:travelStyle IS NULL OR :travelStyle = '' OR :travelStyle = 'All' OR " +
           " LOWER(d.travelStyles) LIKE LOWER(CONCAT('%', :travelStyle, '%')) OR " +
           " (:travelStyle = 'Heritage & Culture' AND (LOWER(d.travelStyles) LIKE '%heritage%' OR LOWER(d.travelStyles) LIKE '%culture%')) OR " +
           " (:travelStyle = 'Nature & Wildlife' AND (LOWER(d.travelStyles) LIKE '%nature%' OR LOWER(d.travelStyles) LIKE '%wildlife%')) OR " +
           " (:travelStyle = 'Wellness & Spiritual' AND (LOWER(d.travelStyles) LIKE '%spiritual%' OR LOWER(d.travelStyles) LIKE '%wellness%')) OR " +
           " (:travelStyle = 'Beach & Coastal' AND (LOWER(d.travelStyles) LIKE '%beach%' OR LOWER(d.travelStyles) LIKE '%coastal%')) OR " +
           " (:travelStyle = 'Adventure & Trekking' AND (LOWER(d.travelStyles) LIKE '%adventure%' OR LOWER(d.travelStyles) LIKE '%trekking%')) OR " +
           " (:travelStyle = 'Culinary & Food' AND (LOWER(d.travelStyles) LIKE '%food%' OR LOWER(d.travelStyles) LIKE '%culinary%'))) AND " +
           "(:budgetTier IS NULL OR :budgetTier = '' OR :budgetTier = 'All' OR LOWER(d.budgetTier) = LOWER(:budgetTier))")
    Page<Destination> findWithFilters(@Param("search") String search,
                                      @Param("region") String region,
                                      @Param("travelStyle") String travelStyle,
                                      @Param("budgetTier") String budgetTier,
                                      Pageable pageable);

    List<Destination> findByRegion(String region);

    java.util.Optional<Destination> findBySourceProviderAndProviderRecordId(String sourceProvider, String providerRecordId);

    java.util.Optional<Destination> findByNameIgnoreCase(String name);

    @Query("SELECT d FROM Destination d WHERE " +
           "LOWER(d.name) LIKE LOWER(CONCAT('%', :query, '%')) OR " +
           "LOWER(d.state) LIKE LOWER(CONCAT('%', :query, '%')) " +
           "ORDER BY CASE WHEN LOWER(d.name) LIKE LOWER(CONCAT(:query, '%')) THEN 0 ELSE 1 END, d.name ASC")
    List<Destination> searchSuggestions(@Param("query") String query, Pageable pageable);
}

