package com.voyageai.integration.weather;

import com.voyageai.integration.weather.dto.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientResponseException;

import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

/**
 * Service integrating with the Open-Meteo public meteorological API.
 * Adheres strictly to the Voyage-AI Data Authenticity Requirement:
 * - Coordinates are validated before execution.
 * - HTTP timeouts are bounded.
 * - Errors and missing fields never default to hardcoded mock weather.
 * - Data provenance and units are explicitly recorded.
 */
@Service
public class OpenMeteoWeatherService implements WeatherProvider {

    private static final Logger log = LoggerFactory.getLogger(OpenMeteoWeatherService.class);

    private final RestClient restClient;
    private final String baseUrl;

    @org.springframework.beans.factory.annotation.Autowired
    public OpenMeteoWeatherService(
            @Value("${open-meteo.base-url:https://api.open-meteo.com/v1}") String baseUrl,
            @Value("${open-meteo.connect-timeout-ms:3000}") int connectTimeoutMs,
            @Value("${open-meteo.read-timeout-ms:5000}") int readTimeoutMs) {
        this.baseUrl = baseUrl;

        SimpleClientHttpRequestFactory factory = new SimpleClientHttpRequestFactory();
        factory.setConnectTimeout(Duration.ofMillis(connectTimeoutMs));
        factory.setReadTimeout(Duration.ofMillis(readTimeoutMs));

        this.restClient = RestClient.builder()
                .baseUrl(baseUrl)
                .requestFactory(factory)
                .build();
    }

    /**
     * Constructor allowing injection of a pre-configured RestClient (primarily for unit testing).
     */
    public OpenMeteoWeatherService(RestClient restClient, String baseUrl) {
        this.restClient = restClient;
        this.baseUrl = baseUrl;
    }

    @Override
    public WeatherResponse getWeather(double latitude, double longitude) {
        validateCoordinates(latitude, longitude);

        try {
            OpenMeteoApiResponse response = restClient.get()
                    .uri(uriBuilder -> uriBuilder
                            .path("/forecast")
                            .queryParam("latitude", latitude)
                            .queryParam("longitude", longitude)
                            .queryParam("current", "temperature_2m,relative_humidity_2m,apparent_temperature,is_day,precipitation,weather_code,wind_speed_10m")
                            .queryParam("daily", "weather_code,temperature_2m_max,temperature_2m_min")
                            .queryParam("timezone", "auto")
                            .build())
                    .accept(MediaType.APPLICATION_JSON)
                    .retrieve()
                    .body(OpenMeteoApiResponse.class);

            if (response == null || response.getCurrent() == null) {
                log.error("Open-Meteo returned null or empty weather body for coordinates: lat={}, lon={}", latitude, longitude);
                throw new WeatherServiceException(
                        "Open-Meteo returned an empty weather response for the specified coordinates.",
                        HttpStatus.SERVICE_UNAVAILABLE,
                        "WEATHER_DATA_UNAVAILABLE"
                );
            }

            return mapToWeatherResponse(response);

        } catch (ResourceAccessException ex) {
            log.error("Timeout or connection failure connecting to Open-Meteo at {}: {}", baseUrl, ex.getMessage());
            throw new WeatherServiceException(
                    "Weather forecast service is currently unreachable due to network timeout or connection failure. Please try again later.",
                    ex,
                    HttpStatus.SERVICE_UNAVAILABLE,
                    "WEATHER_PROVIDER_TIMEOUT"
            );
        } catch (RestClientResponseException ex) {
            log.error("Open-Meteo API returned error status {}: {}", ex.getStatusCode(), ex.getResponseBodyAsString());
            throw new WeatherServiceException(
                    "Weather provider returned an error: " + ex.getStatusText(),
                    ex,
                    HttpStatus.SERVICE_UNAVAILABLE,
                    "WEATHER_PROVIDER_ERROR"
            );
        } catch (IllegalArgumentException | WeatherServiceException ex) {
            throw ex;
        } catch (Exception ex) {
            log.error("Unexpected error processing weather data for lat={}, lon={}", latitude, longitude, ex);
            throw new WeatherServiceException(
                    "An unexpected error occurred while processing weather data.",
                    ex,
                    HttpStatus.INTERNAL_SERVER_ERROR,
                    "WEATHER_INTERNAL_ERROR"
            );
        }
    }

    public static void validateCoordinates(Double latitude, Double longitude) {
        if (latitude == null || longitude == null) {
            throw new IllegalArgumentException("Latitude and longitude must not be null.");
        }
        if (latitude < -90.0 || latitude > 90.0) {
            throw new IllegalArgumentException("Latitude must be between -90.0 and 90.0 degrees. Provided: " + latitude);
        }
        if (longitude < -180.0 || longitude > 180.0) {
            throw new IllegalArgumentException("Longitude must be between -180.0 and 180.0 degrees. Provided: " + longitude);
        }
    }

    private WeatherResponse mapToWeatherResponse(OpenMeteoApiResponse api) {
        OpenMeteoApiResponse.CurrentData cur = api.getCurrent();
        String currentCondition = WmoWeatherCode.getDescription(cur.getWeatherCode());

        CurrentWeatherDTO currentDTO = new CurrentWeatherDTO(
                cur.getTime(),
                cur.getTemperature2m(),
                cur.getApparentTemperature(),
                cur.getRelativeHumidity2m(),
                cur.getWindSpeed10m(),
                cur.getPrecipitation(),
                cur.getWeatherCode(),
                currentCondition,
                cur.getIsDay() != null && cur.getIsDay() == 1
        );

        List<DailyForecastDTO> dailyList = new ArrayList<>();
        if (api.getDaily() != null && api.getDaily().getTime() != null) {
            List<String> dates = api.getDaily().getTime();
            List<Integer> codes = api.getDaily().getWeatherCode();
            List<Double> maxTemps = api.getDaily().getTemperature2mMax();
            List<Double> minTemps = api.getDaily().getTemperature2mMin();

            for (int i = 0; i < dates.size(); i++) {
                String date = dates.get(i);
                Integer code = (codes != null && i < codes.size()) ? codes.get(i) : null;
                Double maxT = (maxTemps != null && i < maxTemps.size()) ? maxTemps.get(i) : null;
                Double minT = (minTemps != null && i < minTemps.size()) ? minTemps.get(i) : null;
                dailyList.add(new DailyForecastDTO(date, code, WmoWeatherCode.getDescription(code), maxT, minT));
            }
        }

        WeatherUnitsDTO units = new WeatherUnitsDTO("°C", "°C", "%", "km/h", "mm");

        WeatherProvenanceDTO provenance = new WeatherProvenanceDTO(
                "Open-Meteo",
                Instant.now(),
                "Weather data by Open-Meteo.com",
                "CC-BY 4.0",
                true
        );

        return new WeatherResponse(
                api.getLatitude(),
                api.getLongitude(),
                api.getTimezone(),
                api.getElevation(),
                currentDTO,
                dailyList,
                units,
                provenance
        );
    }
}
