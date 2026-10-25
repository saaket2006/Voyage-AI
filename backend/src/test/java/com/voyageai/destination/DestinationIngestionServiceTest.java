package com.voyageai.destination;

import com.voyageai.destination.ingestion.DestinationImportResult;
import com.voyageai.destination.ingestion.DestinationIngestionService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.client.ExpectedCount;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestClient;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.method;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withStatus;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withSuccess;

@SpringBootTest
@ActiveProfiles("test")
@Transactional
class DestinationIngestionServiceTest {

    private static final String BASE_URL = "https://en.wikipedia.org/api/rest_v1";

    @Autowired
    private DestinationRepository destinationRepository;

    private MockRestServiceServer mockServer;
    private DestinationIngestionService ingestionService;

    @BeforeEach
    void setUp() {
        RestClient.Builder builder = RestClient.builder().baseUrl(BASE_URL);
        this.mockServer = MockRestServiceServer.bindTo(builder).build();
        RestClient restClient = builder.build();
        this.ingestionService = new DestinationIngestionService(
                destinationRepository,
                restClient,
                BASE_URL,
                "VoyageAI/1.0 (academic-travel-planner; test@voyageai.local)"
        );
    }

    @Test
    @DisplayName("Successfully fetches Wikimedia summary, validates coordinates, and persists new destination")
    void importDestinations_success_createsNewDestination() {
        String hyderabadJson = """
            {
              "type": "standard",
              "title": "Hyderabad",
              "displaytitle": "<span class=\\"mw-page-title-main\\">Hyderabad</span>",
              "wikibase_item": "Q1361",
              "description": "Capital of Telangana, India",
              "coordinates": {
                "lat": 17.3850,
                "lon": 78.4867
              },
              "thumbnail": {
                "source": "https://upload.wikimedia.org/wikipedia/commons/thumb/c/c5/Charminar_Hyderabad.jpg/320px-Charminar_Hyderabad.jpg"
              },
              "originalimage": {
                "source": "https://upload.wikimedia.org/wikipedia/commons/c/c5/Charminar_Hyderabad.jpg"
              },
              "content_urls": {
                "desktop": {
                  "page": "https://en.wikipedia.org/wiki/Hyderabad"
                }
              },
              "extract": "Hyderabad is the capital and largest city of the Indian state of Telangana and the de jure capital of Andhra Pradesh."
            }
            """;

        String imageInfoJson = """
            {
              "query": {
                "pages": {
                  "12345": {
                    "title": "File:Charminar Hyderabad.jpg",
                    "imageinfo": [
                      {
                        "user": "M.A.Raza",
                        "descriptionurl": "https://commons.wikimedia.org/wiki/File:Charminar_Hyderabad.jpg",
                        "extmetadata": {
                          "Artist": { "value": "<a href=\\"https://commons.wikimedia.org\\">M.A. Raza</a>" },
                          "LicenseShortName": { "value": "CC BY-SA 4.0" },
                          "LicenseUrl": { "value": "https://creativecommons.org/licenses/by-sa/4.0" }
                        }
                      }
                    ]
                  }
                }
              }
            }
            """;

        mockServer.expect(ExpectedCount.once(), requestTo(BASE_URL + "/page/summary/Hyderabad"))
                .andExpect(method(HttpMethod.GET))
                .andRespond(withSuccess(hyderabadJson, MediaType.APPLICATION_JSON));

        mockServer.expect(ExpectedCount.once(), requestTo(org.hamcrest.Matchers.containsString("action=query")))
                .andExpect(method(HttpMethod.GET))
                .andRespond(withSuccess(imageInfoJson, MediaType.APPLICATION_JSON));

        DestinationImportResult result = ingestionService.importDestinations(List.of("Hyderabad"), false);

        mockServer.verify();

        assertThat(result.getRecordsFetched()).isEqualTo(1);
        assertThat(result.getRecordsValidated()).isEqualTo(1);
        assertThat(result.getRecordsInserted()).isEqualTo(1);
        assertThat(result.getRecordsSkipped()).isEqualTo(0);
        assertThat(result.getErrors()).isEmpty();

        Optional<Destination> saved = destinationRepository.findById("hyderabad");
        assertThat(saved).isPresent();
        Destination dest = saved.get();
        assertThat(dest.getName()).isEqualTo("Hyderabad");
        assertThat(dest.getState()).isEqualTo("Telangana");
        assertThat(dest.getRegion()).isEqualTo("South");
        assertThat(dest.getSourceProvider()).isEqualTo("WIKIPEDIA");
        assertThat(dest.getProviderRecordId()).isEqualTo("Q1361");
        assertThat(dest.getLatitude()).isEqualTo(17.3850);
        assertThat(dest.getLongitude()).isEqualTo(78.4867);
        assertThat(dest.getVerificationStatus()).isEqualTo("VERIFIED");
        assertThat(dest.getCurationStatus()).isEqualTo("VERIFIED_CURATED");
        assertThat(dest.getImageAttributionText()).isEqualTo("M.A. Raza");
        assertThat(dest.getImageLicenseType()).isEqualTo("CC BY-SA 4.0");
        assertThat(dest.getImageLicenseUrl()).isEqualTo("https://creativecommons.org/licenses/by-sa/4.0");
    }

    @Test
    @DisplayName("Deduplication skips already-persisted destination on repeated import without forceUpdate")
    void importDestinations_deduplication_skipsExisting() {
        // Jaipur is already seeded in V2 migration
        String jaipurJson = """
            {
              "type": "standard",
              "title": "Jaipur",
              "wikibase_item": "Q66485",
              "description": "Capital city of Rajasthan, India",
              "coordinates": { "lat": 26.9167, "lon": 75.8667 },
              "extract": "Jaipur is the capital and largest city of Rajasthan."
            }
            """;

        mockServer.expect(ExpectedCount.once(), requestTo(BASE_URL + "/page/summary/Jaipur"))
                .andExpect(method(HttpMethod.GET))
                .andRespond(withSuccess(jaipurJson, MediaType.APPLICATION_JSON));

        DestinationImportResult result = ingestionService.importDestinations(List.of("Jaipur"), false);

        mockServer.verify();

        assertThat(result.getRecordsFetched()).isEqualTo(1);
        assertThat(result.getRecordsValidated()).isEqualTo(1);
        assertThat(result.getRecordsInserted()).isEqualTo(0);
        assertThat(result.getRecordsSkipped()).isEqualTo(1);
        assertThat(result.getErrors()).isEmpty();
    }

    @Test
    @DisplayName("ForceUpdate updates existing destination record with updated Wikimedia data")
    void importDestinations_forceUpdate_updatesExisting() {
        String jaipurUpdatedJson = """
            {
              "type": "standard",
              "title": "Jaipur",
              "wikibase_item": "Q66485",
              "description": "The iconic Pink City of India",
              "coordinates": { "lat": 26.9190, "lon": 75.8690 },
              "extract": "Jaipur is famously known as the Pink City of Rajasthan."
            }
            """;

        mockServer.expect(ExpectedCount.once(), requestTo(BASE_URL + "/page/summary/Jaipur"))
                .andExpect(method(HttpMethod.GET))
                .andRespond(withSuccess(jaipurUpdatedJson, MediaType.APPLICATION_JSON));

        DestinationImportResult result = ingestionService.importDestinations(List.of("Jaipur"), true);

        mockServer.verify();

        assertThat(result.getRecordsFetched()).isEqualTo(1);
        assertThat(result.getRecordsValidated()).isEqualTo(1);
        assertThat(result.getRecordsUpdated()).isEqualTo(1);

        Destination updated = destinationRepository.findById("jaipur").orElseThrow();
        assertThat(updated.getTagline()).isEqualTo("The iconic Pink City of India");
        assertThat(updated.getSourceProvider()).isEqualTo("WIKIPEDIA");
        assertThat(updated.getProviderRecordId()).isEqualTo("Q66485");
    }

    @Test
    @DisplayName("Invalid coordinates outside [-90, 90] lat are rejected with error record and not persisted")
    void importDestinations_invalidCoordinates_rejected() {
        String invalidCoordJson = """
            {
              "type": "standard",
              "title": "InvalidCity",
              "wikibase_item": "Q999999",
              "description": "Bogus coordinates test",
              "coordinates": { "lat": 195.0, "lon": 75.0 },
              "extract": "A test entity with invalid latitude coordinates."
            }
            """;

        mockServer.expect(ExpectedCount.once(), requestTo(BASE_URL + "/page/summary/InvalidCity"))
                .andExpect(method(HttpMethod.GET))
                .andRespond(withSuccess(invalidCoordJson, MediaType.APPLICATION_JSON));

        DestinationImportResult result = ingestionService.importDestinations(List.of("InvalidCity"), false);

        mockServer.verify();

        assertThat(result.getRecordsFetched()).isEqualTo(1);
        assertThat(result.getRecordsValidated()).isEqualTo(0);
        assertThat(result.getRecordsInserted()).isEqualTo(0);
        assertThat(result.getErrors()).hasSize(1);
        assertThat(result.getErrors().get(0)).contains("Coordinates out of valid range");
    }

    @Test
    @DisplayName("HTTP 404 Not Found from Wikimedia degrades truthfully without crashing")
    void importDestinations_http404_recordsError() {
        mockServer.expect(ExpectedCount.once(), requestTo(BASE_URL + "/page/summary/UnknownCity12345"))
                .andExpect(method(HttpMethod.GET))
                .andRespond(withStatus(HttpStatus.NOT_FOUND));

        DestinationImportResult result = ingestionService.importDestinations(List.of("UnknownCity12345"), false);

        mockServer.verify();

        assertThat(result.getRecordsFetched()).isEqualTo(0);
        assertThat(result.getRecordsInserted()).isEqualTo(0);
        assertThat(result.getErrors()).hasSize(1);
        assertThat(result.getErrors().get(0)).contains("404");
    }

    @Test
    @DisplayName("Uncurated city ingested without synthetic fabrication: marks COMMUNITY_INGESTED and 0 days duration")
    void importDestinations_uncuratedCity_avoidsSyntheticFabrication() {
        String indoreJson = """
            {
              "type": "standard",
              "title": "Indore",
              "wikibase_item": "Q200663",
              "description": "City in Madhya Pradesh, India",
              "coordinates": { "lat": 22.7196, "lon": 75.8577 },
              "extract": "Indore is the largest and most populous city in the Indian state of Madhya Pradesh."
            }
            """;

        mockServer.expect(ExpectedCount.once(), requestTo(BASE_URL + "/page/summary/Indore"))
                .andExpect(method(HttpMethod.GET))
                .andRespond(withSuccess(indoreJson, MediaType.APPLICATION_JSON));

        DestinationImportResult result = ingestionService.importDestinations(List.of("Indore"), false);

        mockServer.verify();

        assertThat(result.getRecordsInserted()).isEqualTo(1);
        Destination saved = destinationRepository.findById("indore").orElseThrow();
        assertThat(saved.getCurationStatus()).isEqualTo("COMMUNITY_INGESTED");
        assertThat(saved.getVerificationStatus()).isEqualTo("UNVERIFIED_SOURCE");
        assertThat(saved.getRecommendedDurationDays()).isEqualTo(0);
        assertThat(saved.getSourceProvider()).isEqualTo("WIKIPEDIA");
        assertThat(saved.getAttributionUrl()).isNotBlank();
    }
}
