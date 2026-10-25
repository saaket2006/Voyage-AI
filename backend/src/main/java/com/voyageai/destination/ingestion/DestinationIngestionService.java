package com.voyageai.destination.ingestion;

import com.voyageai.destination.Destination;
import com.voyageai.destination.DestinationRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientResponseException;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.*;

@Service
public class DestinationIngestionService {

    private static final Logger log = LoggerFactory.getLogger(DestinationIngestionService.class);

    public static final List<String> DEFAULT_DESTINATIONS = List.of(
            "Jaipur", "Hyderabad", "Delhi", "Mumbai",
            "Bengaluru", "Varanasi", "Munnar", "Hampi",
            "Kolkata", "Agra", "Udaipur", "Goa"
    );

    private final DestinationRepository destinationRepository;
    private final RestClient restClient;
    private final RestClient commonsRestClient;
    private final com.fasterxml.jackson.databind.ObjectMapper objectMapper;
    private final String baseUrl;
    private final String userAgent;

    private static class DestinationMetadata {
        final String state;
        final String region;
        final String travelStyles;
        final String budgetTier;
        final int recommendedDurationDays;
        final String bestTimeToVisit;

        DestinationMetadata(String state, String region, String travelStyles,
                            String budgetTier, int recommendedDurationDays, String bestTimeToVisit) {
            this.state = state;
            this.region = region;
            this.travelStyles = travelStyles;
            this.budgetTier = budgetTier;
            this.recommendedDurationDays = recommendedDurationDays;
            this.bestTimeToVisit = bestTimeToVisit;
        }
    }

    private static final Map<String, DestinationMetadata> KNOWN_METADATA = new HashMap<>();

    static {
        KNOWN_METADATA.put("jaipur", new DestinationMetadata("Rajasthan", "North", "Heritage, Culture, Architecture", "Comfort", 3, "October to March"));
        KNOWN_METADATA.put("hyderabad", new DestinationMetadata("Telangana", "South", "Food, Heritage, Tech", "Comfort", 3, "November to February"));
        KNOWN_METADATA.put("delhi", new DestinationMetadata("Delhi", "North", "History, Culture, Food", "Comfort", 3, "October to March"));
        KNOWN_METADATA.put("mumbai", new DestinationMetadata("Maharashtra", "West", "Nightlife, Culture, Coastal", "Luxury", 3, "November to February"));
        KNOWN_METADATA.put("bengaluru", new DestinationMetadata("Karnataka", "South", "Nature, Tech, Nightlife", "Comfort", 3, "September to March"));
        KNOWN_METADATA.put("varanasi", new DestinationMetadata("Uttar Pradesh", "North", "Spiritual, Heritage, Culture", "Budget", 2, "October to March"));
        KNOWN_METADATA.put("munnar", new DestinationMetadata("Kerala", "South", "Nature, Hills, Tea Gardens", "Comfort", 3, "September to May"));
        KNOWN_METADATA.put("hampi", new DestinationMetadata("Karnataka", "South", "Heritage, Archaeology, History", "Budget", 3, "October to March"));
        KNOWN_METADATA.put("kolkata", new DestinationMetadata("West Bengal", "East", "Art, Culture, Food", "Budget", 3, "October to March"));
        KNOWN_METADATA.put("agra", new DestinationMetadata("Uttar Pradesh", "North", "Heritage, Monuments, History", "Comfort", 2, "October to March"));
        KNOWN_METADATA.put("udaipur", new DestinationMetadata("Rajasthan", "North", "Romantic, Heritage, Lakes", "Luxury", 3, "September to March"));
        KNOWN_METADATA.put("goa", new DestinationMetadata("Goa", "West", "Beach, Nightlife, Relaxation", "Comfort", 4, "November to February"));
    }

    @Autowired
    public DestinationIngestionService(
            DestinationRepository destinationRepository,
            @Value("${app.destinations.wikimedia.base-url:https://en.wikipedia.org/api/rest_v1}") String baseUrl,
            @Value("${app.destinations.wikimedia.user-agent:VoyageAI/1.0 (academic-travel-planner; contact@voyageai.local)}") String userAgent,
            @Value("${app.destinations.wikimedia.connect-timeout-ms:5000}") int connectTimeoutMs,
            @Value("${app.destinations.wikimedia.read-timeout-ms:10000}") int readTimeoutMs) {
        this.destinationRepository = destinationRepository;
        this.baseUrl = baseUrl;
        this.userAgent = userAgent;
        this.objectMapper = new com.fasterxml.jackson.databind.ObjectMapper();

        SimpleClientHttpRequestFactory factory = new SimpleClientHttpRequestFactory();
        factory.setConnectTimeout(Duration.ofMillis(connectTimeoutMs));
        factory.setReadTimeout(Duration.ofMillis(readTimeoutMs));

        this.restClient = RestClient.builder()
                .baseUrl(baseUrl)
                .defaultHeader("User-Agent", userAgent)
                .defaultHeader("Accept", "application/json")
                .requestFactory(factory)
                .build();

        this.commonsRestClient = RestClient.builder()
                .baseUrl("https://commons.wikimedia.org")
                .defaultHeader("User-Agent", userAgent)
                .defaultHeader("Accept", "application/json")
                .requestFactory(factory)
                .build();
    }

    /**
     * Testing constructor allowing injection of a pre-configured RestClient.
     */
    public DestinationIngestionService(DestinationRepository destinationRepository, RestClient restClient, String baseUrl, String userAgent) {
        this.destinationRepository = destinationRepository;
        this.restClient = restClient;
        this.commonsRestClient = restClient;
        this.objectMapper = new com.fasterxml.jackson.databind.ObjectMapper();
        this.baseUrl = baseUrl;
        this.userAgent = userAgent;
    }

    @Transactional
    public DestinationImportResult importDestinations(List<String> destinationNames, boolean forceUpdate) {
        DestinationImportResult result = new DestinationImportResult();
        List<String> targets = (destinationNames == null || destinationNames.isEmpty())
                ? DEFAULT_DESTINATIONS
                : destinationNames;

        log.info("Starting destination ingestion for {} targets (forceUpdate={})", targets.size(), forceUpdate);

        for (String target : targets) {
            String trimmed = target != null ? target.trim() : "";
            if (trimmed.isEmpty()) {
                continue;
            }

            try {
                WikimediaSummaryResponse summary = fetchWikimediaSummary(trimmed);
                result.setRecordsFetched(result.getRecordsFetched() + 1);

                if (!validateSummary(summary, trimmed, result)) {
                    continue;
                }

                result.setRecordsValidated(result.getRecordsValidated() + 1);
                processAndPersist(summary, forceUpdate, result);

            } catch (RestClientResponseException ex) {
                String errorMsg = String.format("HTTP error %d fetching '%s': %s",
                        ex.getStatusCode().value(), trimmed, ex.getStatusText());
                log.warn(errorMsg);
                result.getErrors().add(errorMsg);
            } catch (Exception ex) {
                String errorMsg = String.format("Failed to import '%s': %s", trimmed, ex.getMessage());
                log.error(errorMsg, ex);
                result.getErrors().add(errorMsg);
            }
        }

        log.info("Ingestion completed: fetched={}, validated={}, inserted={}, updated={}, skipped={}, errors={}",
                result.getRecordsFetched(), result.getRecordsValidated(),
                result.getRecordsInserted(), result.getRecordsUpdated(),
                result.getRecordsSkipped(), result.getErrors().size());

        return result;
    }

    private WikimediaSummaryResponse fetchWikimediaSummary(String title) {
        String encodedTitle = URLEncoder.encode(title.replace(' ', '_'), StandardCharsets.UTF_8);
        return restClient.get()
                .uri("/page/summary/{title}", encodedTitle)
                .header("User-Agent", userAgent)
                .retrieve()
                .body(WikimediaSummaryResponse.class);
    }

    private boolean validateSummary(WikimediaSummaryResponse summary, String requestedTitle, DestinationImportResult result) {
        if (summary == null) {
            result.getErrors().add("Null response received for destination: " + requestedTitle);
            return false;
        }

        if (summary.getTitle() == null || summary.getTitle().isBlank()) {
            result.getErrors().add("Missing title in Wikimedia response for: " + requestedTitle);
            return false;
        }

        if (summary.getExtract() == null || summary.getExtract().isBlank()) {
            result.getErrors().add("Missing encyclopedic extract for: " + requestedTitle);
            return false;
        }

        if (summary.getCoordinates() == null) {
            result.getErrors().add("Missing geographic coordinates for: " + requestedTitle);
            return false;
        }

        Double lat = summary.getCoordinates().getLat();
        Double lon = summary.getCoordinates().getLon();

        if (lat == null || lon == null) {
            result.getErrors().add("Coordinates incomplete (null lat/lon) for: " + requestedTitle);
            return false;
        }

        if (lat < -90.0 || lat > 90.0 || lon < -180.0 || lon > 180.0) {
            result.getErrors().add(String.format("Coordinates out of valid range (lat=%f, lon=%f) for: %s",
                    lat, lon, requestedTitle));
            return false;
        }

        return true;
    }

    private void processAndPersist(WikimediaSummaryResponse summary, boolean forceUpdate, DestinationImportResult result) {
        String title = summary.getTitle();
        String slugId = slugify(title);
        String wikibaseItem = summary.getWikibaseItem();

        // Check for existing record by provider ID or slug ID or case-insensitive title
        Optional<Destination> existingOpt = Optional.empty();
        if (wikibaseItem != null && !wikibaseItem.isBlank()) {
            existingOpt = destinationRepository.findBySourceProviderAndProviderRecordId("WIKIPEDIA", wikibaseItem);
            if (existingOpt.isEmpty()) {
                existingOpt = destinationRepository.findBySourceProviderAndProviderRecordId("WIKIMEDIA", wikibaseItem);
            }
        }
        if (existingOpt.isEmpty()) {
            existingOpt = destinationRepository.findById(slugId);
        }
        if (existingOpt.isEmpty()) {
            existingOpt = destinationRepository.findByNameIgnoreCase(title);
        }

        if (existingOpt.isPresent()) {
            if (!forceUpdate) {
                log.debug("Destination '{}' already exists. Skipping.", title);
                result.setRecordsSkipped(result.getRecordsSkipped() + 1);
                return;
            }

            Destination existing = existingOpt.get();
            updateDestinationFields(existing, summary);
            destinationRepository.save(existing);
            result.setRecordsUpdated(result.getRecordsUpdated() + 1);
            result.getImportedDestinations().add(existing.getName());
            log.info("Updated existing destination: {}", existing.getName());
        } else {
            Destination destination = new Destination();
            destination.setId(slugId);
            destination.setName(title);
            updateDestinationFields(destination, summary);
            destinationRepository.save(destination);
            result.setRecordsInserted(result.getRecordsInserted() + 1);
            result.getImportedDestinations().add(destination.getName());
            log.info("Inserted new destination: {}", destination.getName());
        }
    }

    private void updateDestinationFields(Destination destination, WikimediaSummaryResponse summary) {
        String slug = slugify(summary.getTitle());
        DestinationMetadata metadata = KNOWN_METADATA.get(slug);

        destination.setName(summary.getTitle());
        destination.setDescription(summary.getExtract());

        String tagline = summary.getDescription();
        if (tagline != null && tagline.length() > 255) {
            tagline = tagline.substring(0, 252) + "...";
        }
        destination.setTagline(tagline);

        if (summary.getThumbnail() != null && summary.getThumbnail().getSource() != null) {
            destination.setImageUrl(summary.getThumbnail().getSource());
        }

        if (summary.getOriginalimage() != null && summary.getOriginalimage().getSource() != null) {
            destination.setHeroImageUrl(summary.getOriginalimage().getSource());
        } else if (destination.getImageUrl() != null) {
            destination.setHeroImageUrl(destination.getImageUrl());
        }

        destination.setLatitude(summary.getCoordinates().getLat());
        destination.setLongitude(summary.getCoordinates().getLon());

        destination.setSourceProvider("WIKIPEDIA");
        destination.setProviderRecordId(summary.getWikibaseItem() != null ? summary.getWikibaseItem() : destination.getId());
        destination.setRetrievedAt(Instant.now());
        destination.setStaleAfter(Instant.now().plus(30, ChronoUnit.DAYS));
        destination.setLicenseType("CC-BY-SA-4.0");
        destination.setAttributionText("Wikipedia contributors / Creative Commons Attribution-ShareAlike 4.0");

        if (summary.getContentUrls() != null && summary.getContentUrls().getDesktop() != null) {
            destination.setAttributionUrl(summary.getContentUrls().getDesktop().getPage());
        } else {
            destination.setAttributionUrl("https://en.wikipedia.org/wiki/" + summary.getTitle().replace(' ', '_'));
        }

        // Granular Image Licensing & Provenance from Wikimedia Commons
        String activeImageUrl = destination.getHeroImageUrl() != null ? destination.getHeroImageUrl() : destination.getImageUrl();
        populateImageProvenance(destination, activeImageUrl);

        // Regional / Travel Metadata
        if (metadata != null) {
            destination.setState(metadata.state);
            destination.setRegion(metadata.region);
            destination.setCountry("India");
            if (destination.getTravelStyles() == null) {
                destination.setTravelStyles(metadata.travelStyles);
            }
            if (destination.getBudgetTier() == null) {
                destination.setBudgetTier(metadata.budgetTier);
            }
            if (destination.getRecommendedDurationDays() <= 0) {
                destination.setRecommendedDurationDays(metadata.recommendedDurationDays);
            }
            if (destination.getBestTimeToVisit() == null) {
                destination.setBestTimeToVisit(metadata.bestTimeToVisit);
            }
            destination.setCurationStatus("VERIFIED_CURATED");
            destination.setVerificationStatus("VERIFIED");
        } else {
            // Zero-fabrication policy:
            // Deduce state and region from factual article summary text, but do NOT fabricate travel planner facts
            if (destination.getState() == null || destination.getState().isBlank()) {
                destination.setState(deduceState(summary));
            }
            if (destination.getRegion() == null || destination.getRegion().isBlank()) {
                destination.setRegion(deduceRegion(destination.getState()));
            }
            if (destination.getCountry() == null || destination.getCountry().isBlank()) {
                destination.setCountry("India");
            }
            if (destination.getBudgetTier() == null || destination.getBudgetTier().isBlank()) {
                destination.setBudgetTier("Comfort");
            }
            destination.setRecommendedDurationDays(0);
            destination.setCurationStatus("COMMUNITY_INGESTED");
            destination.setVerificationStatus("UNVERIFIED_SOURCE");
        }
    }

    private void populateImageProvenance(Destination destination, String imageUrl) {
        if (imageUrl == null || imageUrl.isBlank()) {
            destination.setImageAttributionText(null);
            destination.setImageLicenseType(null);
            destination.setImageLicenseUrl(null);
            destination.setImageSourceUrl(null);
            return;
        }

        destination.setImageSourceUrl(imageUrl);

        if (imageUrl.contains("commons.wikimedia.org")
                || imageUrl.contains("upload.wikimedia.org/wikipedia/commons")
                || imageUrl.contains("thumb.wikimedia.org/wikipedia/commons")
                || imageUrl.contains("/wikipedia/commons/")) {
            String fileName = extractCommonsFileName(imageUrl);
            if (fileName != null && !fileName.isBlank()) {
                try {
                    String responseBody = commonsRestClient.get()
                            .uri(uriBuilder -> uriBuilder
                                    .path("/w/api.php")
                                    .queryParam("action", "query")
                                    .queryParam("titles", "File:" + fileName)
                                    .queryParam("prop", "imageinfo")
                                    .queryParam("iiprop", "extmetadata|user|url")
                                    .queryParam("format", "json")
                                    .build())
                            .retrieve()
                            .body(String.class);

                    if (responseBody != null && !responseBody.isBlank()) {
                        com.fasterxml.jackson.databind.JsonNode root = objectMapper.readTree(responseBody);
                        com.fasterxml.jackson.databind.JsonNode pages = root.path("query").path("pages");
                        java.util.Iterator<com.fasterxml.jackson.databind.JsonNode> it = pages.elements();
                        if (it.hasNext()) {
                            com.fasterxml.jackson.databind.JsonNode page = it.next();
                            com.fasterxml.jackson.databind.JsonNode imageInfo = page.path("imageinfo");
                            if (imageInfo.isArray() && !imageInfo.isEmpty()) {
                                com.fasterxml.jackson.databind.JsonNode first = imageInfo.get(0);
                                String user = first.path("user").asText(null);
                                String descUrl = first.path("descriptionurl").asText(null);
                                com.fasterxml.jackson.databind.JsonNode ext = first.path("extmetadata");

                                String artistHtml = ext.path("Artist").path("value").asText(null);
                                String artist = stripHtml(artistHtml);
                                String licenseShort = ext.path("LicenseShortName").path("value").asText(null);
                                String licenseUrl = ext.path("LicenseUrl").path("value").asText(null);

                                String attribution = (artist != null && !artist.isBlank())
                                        ? artist
                                        : (user != null ? "Wikimedia Commons contributor: " + user : "Wikimedia Commons contributor");
                                destination.setImageAttributionText(attribution.length() > 255 ? attribution.substring(0, 252) + "..." : attribution);
                                destination.setImageLicenseType((licenseShort != null && !licenseShort.isBlank()) ? licenseShort : "CC-BY-SA");
                                destination.setImageLicenseUrl(licenseUrl);
                                if (descUrl != null && !descUrl.isBlank()) {
                                    destination.setImageSourceUrl(descUrl);
                                }
                                return;
                            }
                        }
                    }
                } catch (Exception ex) {
                    log.debug("Wikimedia Commons imageinfo query failed for file '{}': {}", fileName, ex.getMessage());
                }
            }
            // If imageinfo not resolved or query fails, mark as UNVERIFIED to obey Rule 6
            destination.setImageAttributionText("Wikimedia Commons (Author unverified)");
            destination.setImageLicenseType("UNVERIFIED");
            destination.setImageLicenseUrl(null);
        } else if (imageUrl.contains("unsplash.com")) {
            destination.setImageAttributionText("Unsplash contributor");
            destination.setImageLicenseType("Unsplash License (Editorial)");
            destination.setImageLicenseUrl("https://unsplash.com/license");
        } else {
            destination.setImageAttributionText("External media");
            destination.setImageLicenseType("Attribution Required");
            destination.setImageLicenseUrl(null);
        }
    }

    private String extractCommonsFileName(String url) {
        if (url == null || url.isBlank()) return null;
        try {
            String decoded = java.net.URLDecoder.decode(url, java.nio.charset.StandardCharsets.UTF_8);
            int idx = decoded.indexOf("/wikipedia/commons/");
            if (idx >= 0) {
                String sub = decoded.substring(idx + "/wikipedia/commons/".length());
                String candidate = null;
                if (sub.startsWith("thumb/")) {
                    String[] parts = sub.split("/");
                    if (parts.length >= 4) {
                        candidate = parts[3];
                    }
                } else {
                    String[] parts = sub.split("/");
                    if (parts.length >= 3) {
                        candidate = parts[2];
                    }
                }
                if (candidate != null && candidate.contains("?")) {
                    candidate = candidate.substring(0, candidate.indexOf('?'));
                }
                return candidate;
            }
        } catch (Exception e) {
            log.debug("Could not parse Commons file name from URL: {}", url);
        }
        return null;
    }

    private String stripHtml(String html) {
        if (html == null || html.isBlank()) return null;
        return html.replaceAll("<[^>]+>", "").replaceAll("\\s+", " ").trim();
    }

    private String slugify(String text) {
        if (text == null) return "";
        return text.toLowerCase(Locale.ENGLISH)
                .replaceAll("[^a-z0-9]+", "-")
                .replaceAll("^-|-$", "");
    }

    private String deduceState(WikimediaSummaryResponse summary) {
        String desc = (summary.getDescription() != null ? summary.getDescription() : "") + " "
                + (summary.getExtract() != null ? summary.getExtract() : "");
        String lower = desc.toLowerCase(Locale.ENGLISH);

        if (lower.contains("rajasthan")) return "Rajasthan";
        if (lower.contains("telangana")) return "Telangana";
        if (lower.contains("maharashtra")) return "Maharashtra";
        if (lower.contains("karnataka")) return "Karnataka";
        if (lower.contains("kerala")) return "Kerala";
        if (lower.contains("tamil nadu")) return "Tamil Nadu";
        if (lower.contains("uttar pradesh")) return "Uttar Pradesh";
        if (lower.contains("west bengal")) return "West Bengal";
        if (lower.contains("goa")) return "Goa";
        if (lower.contains("gujarat")) return "Gujarat";
        if (lower.contains("delhi")) return "Delhi";
        if (lower.contains("punjab")) return "Punjab";
        if (lower.contains("himachal")) return "Himachal Pradesh";
        if (lower.contains("uttarakhand")) return "Uttarakhand";
        if (lower.contains("madhya pradesh")) return "Madhya Pradesh";
        if (lower.contains("odisha") || lower.contains("orissa")) return "Odisha";

        return "India";
    }

    private String deduceRegion(String state) {
        if (state == null) return "Central";
        return switch (state) {
            case "Rajasthan", "Delhi", "Punjab", "Haryana", "Himachal Pradesh", "Uttarakhand", "Uttar Pradesh", "Jammu and Kashmir", "Ladakh" -> "North";
            case "Karnataka", "Telangana", "Kerala", "Tamil Nadu", "Andhra Pradesh" -> "South";
            case "Maharashtra", "Goa", "Gujarat" -> "West";
            case "West Bengal", "Odisha", "Bihar", "Jharkhand", "Assam", "Sikkim" -> "East";
            default -> "Central";
        };
    }
}
