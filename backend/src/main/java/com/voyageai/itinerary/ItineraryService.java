package com.voyageai.itinerary;

import com.voyageai.common.exception.ForbiddenException;
import com.voyageai.common.exception.ResourceNotFoundException;
import com.voyageai.itinerary.dto.*;
import com.voyageai.listing.TravelListing;
import com.voyageai.listing.TravelListingRepository;
import com.voyageai.trip.Trip;
import com.voyageai.trip.TripRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
public class ItineraryService {

    private final TripRepository tripRepository;
    private final ItineraryDayRepository dayRepository;
    private final ItineraryItemRepository itemRepository;
    private final TravelListingRepository listingRepository;

    public ItineraryService(TripRepository tripRepository,
                            ItineraryDayRepository dayRepository,
                            ItineraryItemRepository itemRepository,
                            TravelListingRepository listingRepository) {
        this.tripRepository = tripRepository;
        this.dayRepository = dayRepository;
        this.itemRepository = itemRepository;
        this.listingRepository = listingRepository;
    }

    @Transactional
    public ItineraryDayResponse addDayToTrip(String tripId, String userId, CreateItineraryDayRequest request) {
        Trip trip = tripRepository.findById(tripId)
                .orElseThrow(() -> new ResourceNotFoundException("Trip not found with id: " + tripId));
        verifyOwnership(trip, userId);

        ItineraryDay day = new ItineraryDay();
        day.setId(UUID.randomUUID().toString());
        day.setTrip(trip);
        day.setDayNumber(request.getDayNumber());
        day.setDate(request.getDate());
        day.setTitle(request.getTitle() != null ? request.getTitle() : "Day " + request.getDayNumber());
        day.setNotes(request.getNotes());

        ItineraryDay saved = dayRepository.save(day);
        return ItineraryMapper.toDayResponse(saved);
    }

    @Transactional
    public ItineraryItemResponse addItemToDay(String tripId, String dayId, String userId, CreateItineraryItemRequest request) {
        Trip trip = tripRepository.findById(tripId)
                .orElseThrow(() -> new ResourceNotFoundException("Trip not found with id: " + tripId));
        verifyOwnership(trip, userId);

        ItineraryDay day = dayRepository.findByIdAndTripId(dayId, tripId)
                .orElseThrow(() -> new ResourceNotFoundException("Itinerary day not found with id: " + dayId));

        TravelListing listing = null;
        if (request.getListingId() != null && !request.getListingId().isBlank()) {
            listing = listingRepository.findById(request.getListingId()).orElse(null);
        }

        ItineraryItem item = new ItineraryItem();
        item.setId(UUID.randomUUID().toString());
        item.setItineraryDay(day);
        item.setListing(listing);
        item.setTitle(request.getTitle().trim());
        item.setCategory(request.getCategory() != null ? request.getCategory() : "CUSTOM");
        item.setNotes(request.getNotes());
        item.setSuggestedTime(request.getSuggestedTime());
        item.setDurationMinutes(request.getDurationMinutes());
        item.setEstimatedCost(request.getEstimatedCost());
        item.setCurrency(request.getCurrency() != null ? request.getCurrency() : "INR");
        item.setItemOrder(request.getItemOrder());
        item.setOriginType(request.getOriginType() != null ? request.getOriginType() : "MANUAL");

        ItineraryItem saved = itemRepository.save(item);
        return ItineraryMapper.toItemResponse(saved);
    }

    @Transactional
    public ItineraryItemResponse updateItem(String tripId, String dayId, String itemId, String userId, UpdateItineraryItemRequest request) {
        Trip trip = tripRepository.findById(tripId)
                .orElseThrow(() -> new ResourceNotFoundException("Trip not found with id: " + tripId));
        verifyOwnership(trip, userId);

        dayRepository.findByIdAndTripId(dayId, tripId)
                .orElseThrow(() -> new ResourceNotFoundException("Itinerary day not found with id: " + dayId));

        ItineraryItem item = itemRepository.findByIdAndItineraryDayId(itemId, dayId)
                .orElseThrow(() -> new ResourceNotFoundException("Itinerary item not found with id: " + itemId));

        if (request.getTitle() != null && !request.getTitle().isBlank()) {
            item.setTitle(request.getTitle().trim());
        }
        if (request.getCategory() != null) {
            item.setCategory(request.getCategory());
        }
        if (request.getListingId() != null) {
            if (request.getListingId().isBlank()) {
                item.setListing(null);
            } else {
                listingRepository.findById(request.getListingId()).ifPresent(item::setListing);
            }
        }
        item.setNotes(request.getNotes());
        item.setSuggestedTime(request.getSuggestedTime());
        item.setDurationMinutes(request.getDurationMinutes());
        item.setEstimatedCost(request.getEstimatedCost());
        if (request.getCurrency() != null) {
            item.setCurrency(request.getCurrency());
        }
        item.setItemOrder(request.getItemOrder());

        ItineraryItem saved = itemRepository.save(item);
        return ItineraryMapper.toItemResponse(saved);
    }

    @Transactional
    public void deleteItem(String tripId, String dayId, String itemId, String userId) {
        Trip trip = tripRepository.findById(tripId)
                .orElseThrow(() -> new ResourceNotFoundException("Trip not found with id: " + tripId));
        verifyOwnership(trip, userId);

        dayRepository.findByIdAndTripId(dayId, tripId)
                .orElseThrow(() -> new ResourceNotFoundException("Itinerary day not found with id: " + dayId));

        ItineraryItem item = itemRepository.findByIdAndItineraryDayId(itemId, dayId)
                .orElseThrow(() -> new ResourceNotFoundException("Itinerary item not found with id: " + itemId));

        itemRepository.delete(item);
    }

    private void verifyOwnership(Trip trip, String userId) {
        if (!trip.getUser().getId().equals(userId)) {
            throw new ForbiddenException("You do not have permission to access or modify this trip.");
        }
    }
}
