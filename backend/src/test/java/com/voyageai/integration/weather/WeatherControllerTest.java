package com.voyageai.integration.weather;

import com.voyageai.integration.weather.dto.*;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.Instant;
import java.util.List;

import static org.mockito.ArgumentMatchers.anyDouble;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class WeatherControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private WeatherProvider weatherProvider;

    @Test
    @DisplayName("GET /api/v1/weather with valid coordinates returns 200 OK and weather payload")
    void getWeather_validCoordinates_returns200() throws Exception {
        CurrentWeatherDTO current = new CurrentWeatherDTO(
                "2026-10-20T16:30", 31.5, 32.0, 42, 8.5, 0.0, 0, "Clear sky", true
        );
        DailyForecastDTO daily = new DailyForecastDTO("2026-10-20", 0, "Clear sky", 34.0, 21.0);
        WeatherUnitsDTO units = new WeatherUnitsDTO("°C", "°C", "%", "km/h", "mm");
        WeatherProvenanceDTO provenance = new WeatherProvenanceDTO(
                "Open-Meteo", Instant.now(), "Weather data by Open-Meteo.com", "CC-BY 4.0", true
        );

        WeatherResponse mockResponse = new WeatherResponse(
                26.9124, 75.7873, "Asia/Kolkata", 435.0, current, List.of(daily), units, provenance
        );

        when(weatherProvider.getWeather(26.9124, 75.7873)).thenReturn(mockResponse);

        mockMvc.perform(get("/api/v1/weather")
                        .param("latitude", "26.9124")
                        .param("longitude", "75.7873")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.latitude").value(26.9124))
                .andExpect(jsonPath("$.data.longitude").value(75.7873))
                .andExpect(jsonPath("$.data.timezone").value("Asia/Kolkata"))
                .andExpect(jsonPath("$.data.current.temperature").value(31.5))
                .andExpect(jsonPath("$.data.current.weatherCondition").value("Clear sky"))
                .andExpect(jsonPath("$.data.units.temperatureUnit").value("°C"))
                .andExpect(jsonPath("$.data.provenance.sourceProvider").value("Open-Meteo"))
                .andExpect(jsonPath("$.data.provenance.attribution").value("Weather data by Open-Meteo.com"));
    }

    @Test
    @DisplayName("GET /api/v1/weather with missing coordinates returns 400 Bad Request with MISSING_PARAMETER")
    void getWeather_missingCoordinates_returns400() throws Exception {
        mockMvc.perform(get("/api/v1/weather")
                        .param("latitude", "26.9124")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value("MISSING_PARAMETER"))
                .andExpect(jsonPath("$.message").value(org.hamcrest.Matchers.containsString("longitude")));
    }

    @Test
    @DisplayName("GET /api/v1/weather with non-numeric coordinates returns 400 Bad Request with TYPE_MISMATCH")
    void getWeather_nonNumericCoordinates_returns400() throws Exception {
        mockMvc.perform(get("/api/v1/weather")
                        .param("latitude", "invalid-coord")
                        .param("longitude", "75.7873")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value("TYPE_MISMATCH"))
                .andExpect(jsonPath("$.message").value(org.hamcrest.Matchers.containsString("latitude")));
    }

    @Test
    @DisplayName("GET /api/v1/weather with out-of-range coordinates returns 400 with INVALID_ARGUMENT")
    void getWeather_outOfRangeCoordinates_returns400() throws Exception {
        when(weatherProvider.getWeather(150.0, 75.0))
                .thenThrow(new IllegalArgumentException("Latitude must be between -90.0 and 90.0 degrees. Provided: 150.0"));

        mockMvc.perform(get("/api/v1/weather")
                        .param("latitude", "150.0")
                        .param("longitude", "75.0")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value("INVALID_ARGUMENT"))
                .andExpect(jsonPath("$.message").value(org.hamcrest.Matchers.containsString("Latitude must be between -90.0 and 90.0")));
    }

    @Test
    @DisplayName("GET /api/v1/weather when provider times out returns 503 with WEATHER_PROVIDER_TIMEOUT")
    void getWeather_providerTimeout_returns503() throws Exception {
        when(weatherProvider.getWeather(anyDouble(), anyDouble()))
                .thenThrow(new WeatherServiceException(
                        "Weather forecast service is currently unreachable due to network timeout or connection failure. Please try again later.",
                        HttpStatus.SERVICE_UNAVAILABLE,
                        "WEATHER_PROVIDER_TIMEOUT"
                ));

        mockMvc.perform(get("/api/v1/weather")
                        .param("latitude", "26.9124")
                        .param("longitude", "75.7873")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isServiceUnavailable())
                .andExpect(jsonPath("$.code").value("WEATHER_PROVIDER_TIMEOUT"))
                .andExpect(jsonPath("$.message").value(org.hamcrest.Matchers.containsString("unreachable due to network timeout")))
                .andExpect(jsonPath("$.timestamp").exists());
    }

    @Test
    @DisplayName("GET /api/v1/weather when upstream provider returns error returns 503 with WEATHER_PROVIDER_ERROR")
    void getWeather_upstreamProviderError_returns503() throws Exception {
        when(weatherProvider.getWeather(anyDouble(), anyDouble()))
                .thenThrow(new WeatherServiceException(
                        "Weather provider returned an error: Bad Gateway",
                        HttpStatus.SERVICE_UNAVAILABLE,
                        "WEATHER_PROVIDER_ERROR"
                ));

        mockMvc.perform(get("/api/v1/weather")
                        .param("latitude", "26.9124")
                        .param("longitude", "75.7873")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isServiceUnavailable())
                .andExpect(jsonPath("$.code").value("WEATHER_PROVIDER_ERROR"))
                .andExpect(jsonPath("$.message").value(org.hamcrest.Matchers.containsString("Weather provider returned an error")));
    }

    @Test
    @DisplayName("GET /api/v1/weather when weather data is empty returns 503 with WEATHER_DATA_UNAVAILABLE")
    void getWeather_emptyData_returns503() throws Exception {
        when(weatherProvider.getWeather(anyDouble(), anyDouble()))
                .thenThrow(new WeatherServiceException(
                        "Open-Meteo returned an empty weather response for the specified coordinates.",
                        HttpStatus.SERVICE_UNAVAILABLE,
                        "WEATHER_DATA_UNAVAILABLE"
                ));

        mockMvc.perform(get("/api/v1/weather")
                        .param("latitude", "26.9124")
                        .param("longitude", "75.7873")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isServiceUnavailable())
                .andExpect(jsonPath("$.code").value("WEATHER_DATA_UNAVAILABLE"))
                .andExpect(jsonPath("$.message").value(org.hamcrest.Matchers.containsString("empty weather response")));
    }
}
