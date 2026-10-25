package com.voyageai.integration.db;

import com.voyageai.destination.Destination;
import com.voyageai.destination.DestinationRepository;
import com.voyageai.itinerary.ItineraryDay;
import com.voyageai.itinerary.ItineraryDayRepository;
import com.voyageai.itinerary.ItineraryItem;
import com.voyageai.itinerary.ItineraryItemRepository;
import com.voyageai.listing.ListingType;
import com.voyageai.listing.TravelListing;
import com.voyageai.listing.TravelListingRepository;
import com.voyageai.trip.Trip;
import com.voyageai.trip.TripRepository;
import com.voyageai.user.User;
import com.voyageai.user.UserRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@ActiveProfiles("test")
@Transactional
class PersistenceIntegrationTest {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private DestinationRepository destinationRepository;

    @Autowired
    private TravelListingRepository listingRepository;

    @Autowired
    private TripRepository tripRepository;

    @Autowired
    private ItineraryDayRepository dayRepository;

    @Autowired
    private ItineraryItemRepository itemRepository;

    @Test
    @DisplayName("User persistence: can save, retrieve by email, and check existence")
    void testUserPersistence() {
        String id = UUID.randomUUID().toString();
        User user = new User(id, "persistence@example.com", "hashed_pass", "Persist User");
        userRepository.save(user);

        Optional<User> found = userRepository.findByEmail("persistence@example.com");
        assertThat(found).isPresent();
        assertThat(found.get().getDisplayName()).isEqualTo("Persist User");
        assertThat(found.get().getCreatedAt()).isNotNull();
        assertThat(found.get().getUpdatedAt()).isNotNull();
        assertThat(userRepository.existsByEmail("persistence@example.com")).isTrue();
    }

    @Test
    @DisplayName("Destination & TravelListing persistence: relationship and lazy fetching")
    void testDestinationAndListingPersistence() {
        Optional<Destination> jaipur = destinationRepository.findById("jaipur");
        assertThat(jaipur).isPresent();
        assertThat(jaipur.get().getName()).isEqualTo("Jaipur");
        assertThat(jaipur.get().getSourceProvider()).isEqualTo("ARCHAEOLOGICAL_SURVEY_OF_INDIA");

        List<TravelListing> jaipurListings = listingRepository.findByDestinationId("jaipur");
        assertThat(jaipurListings).isNotEmpty();

        List<TravelListing> jaipurAttractions = listingRepository.findByDestinationIdAndListingType("jaipur", ListingType.ATTRACTION);
        assertThat(jaipurAttractions).hasSizeGreaterThanOrEqualTo(2);
    }

    @Test
    @DisplayName("Trip & Itinerary hierarchy: cascade persist and delete")
    void testTripHierarchyPersistence() {
        // Create user
        User user = new User(UUID.randomUUID().toString(), "triphierarchy@example.com", "pass", "Trip Owner");
        userRepository.save(user);

        // Create trip
        Trip trip = new Trip();
        trip.setId(UUID.randomUUID().toString());
        trip.setUser(user);
        trip.setTitle("Himalayan Trek");
        trip.setStartDate(LocalDate.now());
        trip.setEndDate(LocalDate.now().plusDays(2));
        trip.setDestinationName("Manali");
        trip.setTargetBudget(new BigDecimal("15000.00"));

        // Create Day 1
        ItineraryDay day1 = new ItineraryDay();
        day1.setId(UUID.randomUUID().toString());
        day1.setTrip(trip);
        day1.setDayNumber(1);
        day1.setTitle("Arrival & Acclimatization");

        // Create Item for Day 1
        ItineraryItem item1 = new ItineraryItem();
        item1.setId(UUID.randomUUID().toString());
        item1.setItineraryDay(day1);
        item1.setTitle("Walk around Old Manali");
        item1.setCategory("ACTIVITY");
        item1.setItemOrder(1);

        day1.getItems().add(item1);
        trip.getDays().add(day1);

        // Save trip - should cascade to Day and Item
        tripRepository.save(trip);

        // Verify retrieval
        Optional<Trip> loadedTrip = tripRepository.findById(trip.getId());
        assertThat(loadedTrip).isPresent();
        assertThat(loadedTrip.get().getDays()).hasSize(1);
        assertThat(loadedTrip.get().getDays().get(0).getItems()).hasSize(1);

        // Delete trip - should cascade and delete day and item
        tripRepository.delete(loadedTrip.get());
        assertThat(tripRepository.existsById(trip.getId())).isFalse();
        assertThat(dayRepository.existsById(day1.getId())).isFalse();
        assertThat(itemRepository.existsById(item1.getId())).isFalse();
    }
}
