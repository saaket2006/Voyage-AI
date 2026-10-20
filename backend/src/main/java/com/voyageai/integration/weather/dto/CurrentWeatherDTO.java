package com.voyageai.integration.weather.dto;

public class CurrentWeatherDTO {

    private String time;
    private Double temperature;
    private Double apparentTemperature;
    private Integer relativeHumidity;
    private Double windSpeed;
    private Double precipitation;
    private Integer weatherCode;
    private String weatherCondition;
    private Boolean isDay;

    public CurrentWeatherDTO() {
    }

    public CurrentWeatherDTO(String time, Double temperature, Double apparentTemperature, Integer relativeHumidity,
                             Double windSpeed, Double precipitation, Integer weatherCode, String weatherCondition, Boolean isDay) {
        this.time = time;
        this.temperature = temperature;
        this.apparentTemperature = apparentTemperature;
        this.relativeHumidity = relativeHumidity;
        this.windSpeed = windSpeed;
        this.precipitation = precipitation;
        this.weatherCode = weatherCode;
        this.weatherCondition = weatherCondition;
        this.isDay = isDay;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public Double getTemperature() {
        return temperature;
    }

    public void setTemperature(Double temperature) {
        this.temperature = temperature;
    }

    public Double getApparentTemperature() {
        return apparentTemperature;
    }

    public void setApparentTemperature(Double apparentTemperature) {
        this.apparentTemperature = apparentTemperature;
    }

    public Integer getRelativeHumidity() {
        return relativeHumidity;
    }

    public void setRelativeHumidity(Integer relativeHumidity) {
        this.relativeHumidity = relativeHumidity;
    }

    public Double getWindSpeed() {
        return windSpeed;
    }

    public void setWindSpeed(Double windSpeed) {
        this.windSpeed = windSpeed;
    }

    public Double getPrecipitation() {
        return precipitation;
    }

    public void setPrecipitation(Double precipitation) {
        this.precipitation = precipitation;
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

    public Boolean getIsDay() {
        return isDay;
    }

    public void setIsDay(Boolean isDay) {
        this.isDay = isDay;
    }
}
