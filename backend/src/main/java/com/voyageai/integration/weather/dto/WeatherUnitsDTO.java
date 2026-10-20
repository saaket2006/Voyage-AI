package com.voyageai.integration.weather.dto;

/**
 * Explicit unit representation for weather metrics.
 */
public class WeatherUnitsDTO {

    private String temperatureUnit;
    private String apparentTemperatureUnit;
    private String relativeHumidityUnit;
    private String windSpeedUnit;
    private String precipitationUnit;

    public WeatherUnitsDTO() {
        this.temperatureUnit = "°C";
        this.apparentTemperatureUnit = "°C";
        this.relativeHumidityUnit = "%";
        this.windSpeedUnit = "km/h";
        this.precipitationUnit = "mm";
    }

    public WeatherUnitsDTO(String temperatureUnit, String apparentTemperatureUnit, String relativeHumidityUnit, String windSpeedUnit, String precipitationUnit) {
        this.temperatureUnit = temperatureUnit;
        this.apparentTemperatureUnit = apparentTemperatureUnit;
        this.relativeHumidityUnit = relativeHumidityUnit;
        this.windSpeedUnit = windSpeedUnit;
        this.precipitationUnit = precipitationUnit;
    }

    public String getTemperatureUnit() {
        return temperatureUnit;
    }

    public void setTemperatureUnit(String temperatureUnit) {
        this.temperatureUnit = temperatureUnit;
    }

    public String getApparentTemperatureUnit() {
        return apparentTemperatureUnit;
    }

    public void setApparentTemperatureUnit(String apparentTemperatureUnit) {
        this.apparentTemperatureUnit = apparentTemperatureUnit;
    }

    public String getRelativeHumidityUnit() {
        return relativeHumidityUnit;
    }

    public void setRelativeHumidityUnit(String relativeHumidityUnit) {
        this.relativeHumidityUnit = relativeHumidityUnit;
    }

    public String getWindSpeedUnit() {
        return windSpeedUnit;
    }

    public void setWindSpeedUnit(String windSpeedUnit) {
        this.windSpeedUnit = windSpeedUnit;
    }

    public String getPrecipitationUnit() {
        return precipitationUnit;
    }

    public void setPrecipitationUnit(String precipitationUnit) {
        this.precipitationUnit = precipitationUnit;
    }
}
