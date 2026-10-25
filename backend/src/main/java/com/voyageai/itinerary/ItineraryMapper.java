package com.voyageai.itinerary;

import com.voyageai.itinerary.dto.ItineraryDayResponse;
import com.voyageai.itinerary.dto.ItineraryItemResponse;
import com.voyageai.listing.TravelListingMapper;

import java.util.stream.Collectors;

public class ItineraryMapper {

    public static ItineraryItemResponse toItemResponse(ItineraryItem item) {
        if (item == null) {
            return null;
        }

        ItineraryItemResponse dto = new ItineraryItemResponse();
        dto.setId(item.getId());
        if (item.getItineraryDay() != null) {
            dto.setItineraryDayId(item.getItineraryDay().getId());
        }
        if (item.getListing() != null) {
            dto.setListingId(item.getListing().getId());
            dto.setListing(TravelListingMapper.toResponse(item.getListing()));
        }
        dto.setTitle(item.getTitle());
        dto.setCategory(item.getCategory());
        dto.setNotes(item.getNotes());
        dto.setSuggestedTime(item.getSuggestedTime());
        dto.setDurationMinutes(item.getDurationMinutes());
        dto.setEstimatedCost(item.getEstimatedCost());
        dto.setCurrency(item.getCurrency());
        dto.setItemOrder(item.getItemOrder());
        dto.setOriginType(item.getOriginType());
        dto.setCreatedAt(item.getCreatedAt());

        return dto;
    }

    public static ItineraryDayResponse toDayResponse(ItineraryDay day) {
        if (day == null) {
            return null;
        }

        ItineraryDayResponse dto = new ItineraryDayResponse();
        dto.setId(day.getId());
        dto.setDayNumber(day.getDayNumber());
        dto.setDate(day.getDate());
        dto.setTitle(day.getTitle());
        dto.setNotes(day.getNotes());

        if (day.getItems() != null) {
            dto.setItems(day.getItems().stream()
                    .map(ItineraryMapper::toItemResponse)
                    .collect(Collectors.toList()));
        }

        return dto;
    }
}
