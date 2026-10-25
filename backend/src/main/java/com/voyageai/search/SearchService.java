package com.voyageai.search;

import com.voyageai.destination.Destination;
import com.voyageai.destination.DestinationRepository;
import com.voyageai.listing.ListingType;
import com.voyageai.listing.TravelListing;
import com.voyageai.listing.TravelListingRepository;
import com.voyageai.search.dto.*;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
public class SearchService {

    private final DestinationRepository destinationRepository;
    private final TravelListingRepository travelListingRepository;

    public SearchService(DestinationRepository destinationRepository,
                         TravelListingRepository travelListingRepository) {
        this.destinationRepository = destinationRepository;
        this.travelListingRepository = travelListingRepository;
    }

    @Transactional(readOnly = true)
    public SearchSuggestionResponse getSuggestions(String query, int limit) {
        Pageable pageable = PageRequest.of(0, limit);

        // 1. Match Cities
        List<Destination> destinations = destinationRepository.searchSuggestions(query, pageable);
        List<SearchItem> cityItems = new ArrayList<>();
        List<String> matchingCityIds = new ArrayList<>();

        for (Destination d : destinations) {
            matchingCityIds.add(d.getId());
            SearchItemLocation location = new SearchItemLocation(
                    d.getName(),
                    d.getState() != null ? d.getState() : d.getRegion(),
                    d.getCountry() != null ? d.getCountry() : "India",
                    d.getLatitude(),
                    d.getLongitude()
            );

            String description = d.getTagline() != null && !d.getTagline().isBlank()
                    ? d.getTagline()
                    : d.getDescription();
            String sourceUrl = d.getAttributionUrl() != null ? d.getAttributionUrl() : d.getImageSourceUrl();

            cityItems.add(new SearchItem(
                    d.getId(),
                    d.getName(),
                    "Cities",
                    description,
                    location,
                    d.getImageUrl(),
                    sourceUrl,
                    "/destinations/" + d.getId()
            ));
        }

        // 2. Match Attractions associated with matching cities or matching the query directly
        List<TravelListing> attractions = matchingCityIds.isEmpty()
                ? travelListingRepository.searchSuggestionsWithoutDestIds(query, ListingType.ATTRACTION, pageable)
                : travelListingRepository.searchSuggestionsWithDestIds(query, matchingCityIds, ListingType.ATTRACTION, pageable);

        List<SearchItem> attractionItems = new ArrayList<>();
        for (TravelListing l : attractions) {
            String city = l.getDestination() != null ? l.getDestination().getName() : null;
            String region = l.getDestination() != null ? l.getDestination().getState() : null;
            String country = l.getDestination() != null ? l.getDestination().getCountry() : "India";
            SearchItemLocation location = new SearchItemLocation(city, region, country, l.getLatitude(), l.getLongitude());

            attractionItems.add(new SearchItem(
                    l.getId(),
                    l.getName(),
                    "Attractions",
                    l.getDescription(),
                    location,
                    l.getImageUrl(),
                    l.getAttributionUrl(),
                    "/attractions/" + l.getId()
            ));
        }

        // 3. Match Dining associated with matching cities or matching the query directly
        List<TravelListing> dining = matchingCityIds.isEmpty()
                ? travelListingRepository.searchSuggestionsWithoutDestIds(query, ListingType.RESTAURANT, pageable)
                : travelListingRepository.searchSuggestionsWithDestIds(query, matchingCityIds, ListingType.RESTAURANT, pageable);

        List<SearchItem> diningItems = new ArrayList<>();
        for (TravelListing l : dining) {
            String city = l.getDestination() != null ? l.getDestination().getName() : null;
            String region = l.getDestination() != null ? l.getDestination().getState() : null;
            String country = l.getDestination() != null ? l.getDestination().getCountry() : "India";
            SearchItemLocation location = new SearchItemLocation(city, region, country, l.getLatitude(), l.getLongitude());

            diningItems.add(new SearchItem(
                    l.getId(),
                    l.getName(),
                    "Dining",
                    l.getDescription(),
                    location,
                    l.getImageUrl(),
                    l.getAttributionUrl(),
                    "/dining/" + l.getId()
            ));
        }

        // Always return three groups in order: CITY, ATTRACTION, DINING
        List<SearchGroup> groups = List.of(
                new SearchGroup("CITY", "Cities", cityItems),
                new SearchGroup("ATTRACTION", "Attractions", attractionItems),
                new SearchGroup("DINING", "Dining", diningItems)
        );

        int totalResults = cityItems.size() + attractionItems.size() + diningItems.size();
        SearchMeta meta = new SearchMeta(limit, totalResults);

        return new SearchSuggestionResponse(query, groups, meta);
    }
}
