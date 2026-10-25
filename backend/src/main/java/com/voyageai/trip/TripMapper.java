package com.voyageai.trip;

import com.voyageai.itinerary.ItineraryMapper;
import com.voyageai.trip.dto.TripResponse;

import java.util.stream.Collectors;

public class TripMapper {

    public static TripResponse toResponse(Trip trip) {
        if (trip == null) {
            return null;
        }

        TripResponse dto = new TripResponse();
        dto.setId(trip.getId());
        if (trip.getUser() != null) {
            dto.setOwnerId(trip.getUser().getId());
        }
        dto.setDestinationId(trip.getDestinationId());
        dto.setDestinationName(trip.getDestinationName());
        dto.setTitle(trip.getTitle());
        dto.setDescription(trip.getDescription());
        dto.setStartDate(trip.getStartDate());
        dto.setEndDate(trip.getEndDate());
        dto.setTravelerCount(trip.getTravelerCount());
        dto.setTargetBudget(trip.getTargetBudget());
        dto.setCurrency(trip.getCurrency());
        dto.setStatus(trip.getStatus());
        dto.setCreatedAt(trip.getCreatedAt());
        dto.setUpdatedAt(trip.getUpdatedAt());

        if (trip.getDays() != null) {
            dto.setDays(trip.getDays().stream()
                    .map(ItineraryMapper::toDayResponse)
                    .collect(Collectors.toList()));
        }

        return dto;
    }
}
