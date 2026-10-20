package com.voyageai.integration.weather;

import com.voyageai.integration.weather.dto.WeatherResponse;

/**
 * Common weather provider interface separating domain callers from external weather APIs.
 */
public interface WeatherProvider {

    /**
     * Retrieve current weather and forecast for geographic coordinates.
     *
     * @param latitude  valid latitude (-90.0 to 90.0)
     * @param longitude valid longitude (-180.0 to 180.0)
     * @return typed WeatherResponse with explicit units and provenance
     */
    WeatherResponse getWeather(double latitude, double longitude);
}
