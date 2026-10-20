package com.voyageai.integration.weather;

import com.voyageai.integration.weather.dto.WeatherResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.web.client.RestClient;

import java.net.SocketTimeoutException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.method;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.response.MockRestResponseCreators.*;

class OpenMeteoWeatherServiceTest {

    private static final String BASE_URL = "https://api.open-meteo.com/v1";

    private MockRestServiceServer mockServer;
    private OpenMeteoWeatherService weatherService;

    @BeforeEach
    void setUp() {
        RestClient.Builder builder = RestClient.builder().baseUrl(BASE_URL);
        this.mockServer = MockRestServiceServer.bindTo(builder).build();
        RestClient restClient = builder.build();
        this.weatherService = new OpenMeteoWeatherService(restClient, BASE_URL);
    }

    @Test
    @DisplayName("Successfully retrieves and maps real Open-Meteo weather and forecast data")
    void getWeather_success() {
        String mockJsonResponse = """
            {
              "latitude": 26.92,
              "longitude": 75.82,
              "elevation": 435.0,
              "timezone": "Asia/Kolkata",
              "current": {
                "time": "2026-10-20T16:30",
                "temperature_2m": 31.5,
                "apparent_temperature": 32.0,
                "relative_humidity_2m": 42,
                "wind_speed_10m": 8.5,
                "precipitation": 0.0,
                "weather_code": 0,
                "is_day": 1
              },
              "daily": {
                "time": ["2026-10-20", "2026-10-21"],
                "weather_code": [0, 2],
                "temperature_2m_max": [34.0, 33.5],
                "temperature_2m_min": [21.0, 20.5]
              }
            }
            """;

        mockServer.expect(requestTo(org.hamcrest.Matchers.startsWith(BASE_URL + "/forecast")))
                .andExpect(method(HttpMethod.GET))
                .andRespond(withSuccess(mockJsonResponse, MediaType.APPLICATION_JSON));

        WeatherResponse response = weatherService.getWeather(26.9124, 75.7873);

        mockServer.verify();

        assertThat(response).isNotNull();
        assertThat(response.getLatitude()).isEqualTo(26.92);
        assertThat(response.getLongitude()).isEqualTo(75.82);
        assertThat(response.getTimezone()).isEqualTo("Asia/Kolkata");

        // Current weather assertions
        assertThat(response.getCurrent()).isNotNull();
        assertThat(response.getCurrent().getTemperature()).isEqualTo(31.5);
        assertThat(response.getCurrent().getApparentTemperature()).isEqualTo(32.0);
        assertThat(response.getCurrent().getRelativeHumidity()).isEqualTo(42);
        assertThat(response.getCurrent().getWindSpeed()).isEqualTo(8.5);
        assertThat(response.getCurrent().getPrecipitation()).isEqualTo(0.0);
        assertThat(response.getCurrent().getWeatherCode()).isEqualTo(0);
        assertThat(response.getCurrent().getWeatherCondition()).isEqualTo("Clear sky");
        assertThat(response.getCurrent().getIsDay()).isTrue();

        // Forecast assertions
        assertThat(response.getDailyForecast()).hasSize(2);
        assertThat(response.getDailyForecast().get(0).getDate()).isEqualTo("2026-10-20");
        assertThat(response.getDailyForecast().get(0).getMaxTemperature()).isEqualTo(34.0);
        assertThat(response.getDailyForecast().get(0).getMinTemperature()).isEqualTo(21.0);
        assertThat(response.getDailyForecast().get(0).getWeatherCondition()).isEqualTo("Clear sky");
        assertThat(response.getDailyForecast().get(1).getWeatherCondition()).isEqualTo("Partly cloudy");

        // Units assertions
        assertThat(response.getUnits().getTemperatureUnit()).isEqualTo("°C");
        assertThat(response.getUnits().getWindSpeedUnit()).isEqualTo("km/h");
        assertThat(response.getUnits().getPrecipitationUnit()).isEqualTo("mm");

        // Provenance assertions
        assertThat(response.getProvenance()).isNotNull();
        assertThat(response.getProvenance().getSourceProvider()).isEqualTo("Open-Meteo");
        assertThat(response.getProvenance().getAttribution()).isEqualTo("Weather data by Open-Meteo.com");
        assertThat(response.getProvenance().getLicense()).isEqualTo("CC-BY 4.0");
        assertThat(response.getProvenance().isForecast()).isTrue();
    }

    @Test
    @DisplayName("Throws IllegalArgumentException when coordinates are out of valid range")
    void getWeather_invalidCoordinates() {
        assertThatThrownBy(() -> weatherService.getWeather(95.0, 75.0))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Latitude must be between -90.0 and 90.0");

        assertThatThrownBy(() -> weatherService.getWeather(-95.0, 75.0))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Latitude must be between -90.0 and 90.0");

        assertThatThrownBy(() -> weatherService.getWeather(20.0, 185.0))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Longitude must be between -180.0 and 180.0");

        assertThatThrownBy(() -> weatherService.getWeather(20.0, -185.0))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Longitude must be between -180.0 and 180.0");

        assertThatThrownBy(() -> OpenMeteoWeatherService.validateCoordinates(null, 75.0))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Latitude and longitude must not be null");
    }

    @Test
    @DisplayName("Throws WeatherServiceException when Open-Meteo returns 500 error")
    void getWeather_providerReturns500() {
        mockServer.expect(requestTo(org.hamcrest.Matchers.startsWith(BASE_URL + "/forecast")))
                .andExpect(method(HttpMethod.GET))
                .andRespond(withServerError());

        assertThatThrownBy(() -> weatherService.getWeather(26.9124, 75.7873))
                .isInstanceOf(WeatherServiceException.class)
                .hasMessageContaining("Weather provider returned an error")
                .satisfies(ex -> {
                    WeatherServiceException wse = (WeatherServiceException) ex;
                    assertThat(wse.getStatus()).isEqualTo(HttpStatus.SERVICE_UNAVAILABLE);
                    assertThat(wse.getErrorCode()).isEqualTo("WEATHER_PROVIDER_ERROR");
                });

        mockServer.verify();
    }

    @Test
    @DisplayName("Throws WeatherServiceException with WEATHER_DATA_UNAVAILABLE when body is null or empty")
    void getWeather_emptyBody() {
        mockServer.expect(requestTo(org.hamcrest.Matchers.startsWith(BASE_URL + "/forecast")))
                .andExpect(method(HttpMethod.GET))
                .andRespond(withSuccess("{}", MediaType.APPLICATION_JSON));

        assertThatThrownBy(() -> weatherService.getWeather(26.9124, 75.7873))
                .isInstanceOf(WeatherServiceException.class)
                .hasMessageContaining("empty weather response")
                .satisfies(ex -> {
                    WeatherServiceException wse = (WeatherServiceException) ex;
                    assertThat(wse.getErrorCode()).isEqualTo("WEATHER_DATA_UNAVAILABLE");
                });

        mockServer.verify();
    }

    @Test
    @DisplayName("Throws WeatherServiceException with WEATHER_PROVIDER_TIMEOUT when connection times out")
    void getWeather_providerTimeout() {
        mockServer.expect(requestTo(org.hamcrest.Matchers.startsWith(BASE_URL + "/forecast")))
                .andExpect(method(HttpMethod.GET))
                .andRespond(withException(new java.io.IOException("Connection timed out")));

        assertThatThrownBy(() -> weatherService.getWeather(26.9124, 75.7873))
                .isInstanceOf(WeatherServiceException.class)
                .hasMessageContaining("currently unreachable due to network timeout")
                .satisfies(ex -> {
                    WeatherServiceException wse = (WeatherServiceException) ex;
                    assertThat(wse.getStatus()).isEqualTo(HttpStatus.SERVICE_UNAVAILABLE);
                    assertThat(wse.getErrorCode()).isEqualTo("WEATHER_PROVIDER_TIMEOUT");
                });

        mockServer.verify();
    }

    @Test
    @DisplayName("Throws WeatherServiceException with WEATHER_PROVIDER_ERROR when Open-Meteo returns 429 Too Many Requests")
    void getWeather_providerRateLimit429() {
        mockServer.expect(requestTo(org.hamcrest.Matchers.startsWith(BASE_URL + "/forecast")))
                .andExpect(method(HttpMethod.GET))
                .andRespond(withRawStatus(429));

        assertThatThrownBy(() -> weatherService.getWeather(26.9124, 75.7873))
                .isInstanceOf(WeatherServiceException.class)
                .hasMessageContaining("Weather provider returned an error")
                .satisfies(ex -> {
                    WeatherServiceException wse = (WeatherServiceException) ex;
                    assertThat(wse.getStatus()).isEqualTo(HttpStatus.SERVICE_UNAVAILABLE);
                    assertThat(wse.getErrorCode()).isEqualTo("WEATHER_PROVIDER_ERROR");
                });

        mockServer.verify();
    }
}
