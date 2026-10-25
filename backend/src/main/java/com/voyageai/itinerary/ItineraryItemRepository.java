package com.voyageai.itinerary;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ItineraryItemRepository extends JpaRepository<ItineraryItem, String> {

    List<ItineraryItem> findByItineraryDayIdOrderByItemOrderAsc(String itineraryDayId);

    Optional<ItineraryItem> findByIdAndItineraryDayId(String id, String itineraryDayId);
}
