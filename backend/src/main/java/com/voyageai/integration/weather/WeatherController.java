package com.voyageai.integration.weather;

import com.voyageai.common.api.ApiResponse;
import com.voyageai.integration.weather.dto.WeatherResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * Controller exposing weather endpoints backed by the Open-Meteo meteorological integration.
 */
@RestController
@RequestMapping("/api/v1/weather")
public class WeatherController {

    private final WeatherProvider weatherProvider;

    public WeatherController(WeatherProvider weatherProvider) {
        this.weatherProvider = weatherProvider;
    }

    /**
     * Retrieve current weather and forecast for given geographic coordinates.
     *
     * @param latitude  Latitude between -90.0 and 90.0
     * @param longitude Longitude between -180.0 and 180.0
     * @return ApiResponse containing typed WeatherResponse
     */
    @GetMapping
    public ResponseEntity<ApiResponse<WeatherResponse>> getWeather(
            @RequestParam(name = "latitude") Double latitude,
            @RequestParam(name = "longitude") Double longitude) {

        WeatherResponse weatherResponse = weatherProvider.getWeather(latitude, longitude);
        return ResponseEntity.ok(ApiResponse.success(weatherResponse, "Weather forecast retrieved successfully."));
    }
}
