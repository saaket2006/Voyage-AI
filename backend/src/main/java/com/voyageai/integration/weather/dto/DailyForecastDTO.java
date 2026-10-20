package com.voyageai.integration.weather.dto;

public class DailyForecastDTO {

    private String date;
    private Integer weatherCode;
    private String weatherCondition;
    private Double maxTemperature;
    private Double minTemperature;

    public DailyForecastDTO() {
    }

    public DailyForecastDTO(String date, Integer weatherCode, String weatherCondition, Double maxTemperature, Double minTemperature) {
        this.date = date;
        this.weatherCode = weatherCode;
        this.weatherCondition = weatherCondition;
        this.maxTemperature = maxTemperature;
        this.minTemperature = minTemperature;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public Integer getWeatherCode() {
        return weatherCode;
    }

    public void setWeatherCode(Integer weatherCode) {
        this.weatherCode = weatherCode;
    }

    public String getWeatherCondition() {
        return weatherCondition;
    }

    public void setWeatherCondition(String weatherCondition) {
        this.weatherCondition = weatherCondition;
    }

    public Double getMaxTemperature() {
        return maxTemperature;
    }

    public void setMaxTemperature(Double maxTemperature) {
        this.maxTemperature = maxTemperature;
    }

    public Double getMinTemperature() {
        return minTemperature;
    }

    public void setMinTemperature(Double minTemperature) {
        this.minTemperature = minTemperature;
    }
}
