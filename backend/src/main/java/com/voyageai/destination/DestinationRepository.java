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
           "(:region IS NULL OR :region = '' OR :region = 'All' OR LOWER(d.region) = LOWER(:region)) AND " +
           "(:travelStyle IS NULL OR :travelStyle = '' OR :travelStyle = 'All' OR LOWER(d.travelStyles) LIKE LOWER(CONCAT('%', :travelStyle, '%'))) AND " +
           "(:budgetTier IS NULL OR :budgetTier = '' OR :budgetTier = 'All' OR LOWER(d.budgetTier) = LOWER(:budgetTier))")
    Page<Destination> findWithFilters(@Param("search") String search,
                                      @Param("region") String region,
                                      @Param("travelStyle") String travelStyle,
                                      @Param("budgetTier") String budgetTier,
                                      Pageable pageable);

    List<Destination> findByRegion(String region);
}
