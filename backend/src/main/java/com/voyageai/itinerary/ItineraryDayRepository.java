package com.voyageai.itinerary;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ItineraryDayRepository extends JpaRepository<ItineraryDay, String> {

    List<ItineraryDay> findByTripIdOrderByDayNumberAsc(String tripId);

    Optional<ItineraryDay> findByIdAndTripId(String id, String tripId);
}
