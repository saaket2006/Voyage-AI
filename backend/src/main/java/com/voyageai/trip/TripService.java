package com.voyageai.trip;

import com.voyageai.common.exception.ForbiddenException;
import com.voyageai.common.exception.ResourceNotFoundException;
import com.voyageai.destination.DestinationRepository;
import com.voyageai.itinerary.ItineraryDay;
import com.voyageai.trip.dto.CreateTripRequest;
import com.voyageai.trip.dto.TripResponse;
import com.voyageai.trip.dto.UpdateTripRequest;
import com.voyageai.user.User;
import com.voyageai.user.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class TripService {

    private final TripRepository tripRepository;
    private final UserRepository userRepository;
    private final DestinationRepository destinationRepository;

    public TripService(TripRepository tripRepository, UserRepository userRepository, DestinationRepository destinationRepository) {
        this.tripRepository = tripRepository;
        this.userRepository = userRepository;
        this.destinationRepository = destinationRepository;
    }

    @Transactional
    public TripResponse createTrip(String userId, CreateTripRequest request) {
        validateDates(request.getStartDate(), request.getEndDate());

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + userId));

        Trip trip = new Trip();
        trip.setId(UUID.randomUUID().toString());
        trip.setUser(user);
        trip.setTitle(request.getTitle().trim());
        trip.setDestinationId(request.getDestinationId());

        if (request.getDestinationName() != null && !request.getDestinationName().isBlank()) {
            trip.setDestinationName(request.getDestinationName().trim());
        } else if (request.getDestinationId() != null) {
            destinationRepository.findById(request.getDestinationId())
                    .ifPresent(dest -> trip.setDestinationName(dest.getName()));
        }

        trip.setDescription(request.getDescription());
        trip.setStartDate(request.getStartDate());
        trip.setEndDate(request.getEndDate());
        trip.setTravelerCount(request.getTravelerCount() > 0 ? request.getTravelerCount() : 1);
        trip.setTargetBudget(request.getTargetBudget());
        trip.setCurrency(request.getCurrency() != null ? request.getCurrency() : "INR");
        trip.setStatus("PLANNING");

        // Automatically initialize itinerary days for each date in trip duration
        long totalDays = ChronoUnit.DAYS.between(request.getStartDate(), request.getEndDate()) + 1;
        List<ItineraryDay> days = new ArrayList<>();
        for (int i = 0; i < totalDays; i++) {
            ItineraryDay day = new ItineraryDay();
            day.setId(UUID.randomUUID().toString());
            day.setTrip(trip);
            day.setDayNumber(i + 1);
            day.setDate(request.getStartDate().plusDays(i));
            day.setTitle("Day " + (i + 1));
            days.add(day);
        }
        trip.setDays(days);

        Trip saved = tripRepository.save(trip);
        return TripMapper.toResponse(saved);
    }

    @Transactional(readOnly = true)
    public List<TripResponse> getTripsForUser(String userId) {
        return tripRepository.findByUserIdOrderByCreatedAtDesc(userId).stream()
                .map(TripMapper::toResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public TripResponse getTripByIdAndUser(String tripId, String userId) {
        Trip trip = tripRepository.findById(tripId)
                .orElseThrow(() -> new ResourceNotFoundException("Trip not found with id: " + tripId));

        verifyOwnership(trip, userId);

        return TripMapper.toResponse(trip);
    }

    @Transactional
    public TripResponse updateTrip(String tripId, String userId, UpdateTripRequest request) {
        validateDates(request.getStartDate(), request.getEndDate());

        Trip trip = tripRepository.findById(tripId)
                .orElseThrow(() -> new ResourceNotFoundException("Trip not found with id: " + tripId));

        verifyOwnership(trip, userId);

        trip.setTitle(request.getTitle().trim());
        trip.setDestinationId(request.getDestinationId());
        if (request.getDestinationName() != null && !request.getDestinationName().isBlank()) {
            trip.setDestinationName(request.getDestinationName().trim());
        } else if (request.getDestinationId() != null) {
            destinationRepository.findById(request.getDestinationId())
                    .ifPresent(dest -> trip.setDestinationName(dest.getName()));
        }
        trip.setDescription(request.getDescription());
        trip.setStartDate(request.getStartDate());
        trip.setEndDate(request.getEndDate());
        trip.setTravelerCount(request.getTravelerCount() > 0 ? request.getTravelerCount() : 1);
        trip.setTargetBudget(request.getTargetBudget());
        if (request.getCurrency() != null) {
            trip.setCurrency(request.getCurrency());
        }
        if (request.getStatus() != null) {
            trip.setStatus(request.getStatus());
        }

        Trip updated = tripRepository.save(trip);
        return TripMapper.toResponse(updated);
    }

    @Transactional
    public void deleteTrip(String tripId, String userId) {
        Trip trip = tripRepository.findById(tripId)
                .orElseThrow(() -> new ResourceNotFoundException("Trip not found with id: " + tripId));

        verifyOwnership(trip, userId);

        tripRepository.delete(trip);
    }

    private void verifyOwnership(Trip trip, String userId) {
        if (!trip.getUser().getId().equals(userId)) {
            throw new ForbiddenException("You do not have permission to access or modify this trip.");
        }
    }

    private void validateDates(LocalDate startDate, LocalDate endDate) {
        if (startDate == null || endDate == null) {
            throw new IllegalArgumentException("Start date and end date are required.");
        }
        if (endDate.isBefore(startDate)) {
            throw new IllegalArgumentException("End date cannot precede start date.");
        }
    }
}
