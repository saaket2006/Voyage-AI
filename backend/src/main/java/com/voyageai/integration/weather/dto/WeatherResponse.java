package com.voyageai.integration.weather.dto;

import java.util.List;

/**
 * Top-level domain and API response for weather queries.
 */
public class WeatherResponse {

    private double latitude;
    private double longitude;
    private String timezone;
    private double elevation;
    private CurrentWeatherDTO current;
    private List<DailyForecastDTO> dailyForecast;
    private WeatherUnitsDTO units;
    private WeatherProvenanceDTO provenance;

    public WeatherResponse() {
    }

    public WeatherResponse(double latitude, double longitude, String timezone, double elevation,
                           CurrentWeatherDTO current, List<DailyForecastDTO> dailyForecast,
                           WeatherUnitsDTO units, WeatherProvenanceDTO provenance) {
        this.latitude = latitude;
        this.longitude = longitude;
        this.timezone = timezone;
        this.elevation = elevation;
        this.current = current;
        this.dailyForecast = dailyForecast;
        this.units = units;
        this.provenance = provenance;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public String getTimezone() {
        return timezone;
    }

    public void setTimezone(String timezone) {
        this.timezone = timezone;
    }

    public double getElevation() {
        return elevation;
    }

    public void setElevation(double elevation) {
        this.elevation = elevation;
    }

    public CurrentWeatherDTO getCurrent() {
        return current;
    }

    public void setCurrent(CurrentWeatherDTO current) {
        this.current = current;
    }

    public List<DailyForecastDTO> getDailyForecast() {
        return dailyForecast;
    }

    public void setDailyForecast(List<DailyForecastDTO> dailyForecast) {
        this.dailyForecast = dailyForecast;
    }

    public WeatherUnitsDTO getUnits() {
        return units;
    }

    public void setUnits(WeatherUnitsDTO units) {
        this.units = units;
    }

    public WeatherProvenanceDTO getProvenance() {
        return provenance;
    }

    public void setProvenance(WeatherProvenanceDTO provenance) {
        this.provenance = provenance;
    }
}
