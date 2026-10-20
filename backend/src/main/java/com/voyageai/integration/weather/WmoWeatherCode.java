package com.voyageai.integration.weather;

import java.util.HashMap;
import java.util.Map;

/**
 * Utility mapping World Meteorological Organization (WMO) weather interpretation codes
 * to standard, user-friendly descriptive weather strings.
 */
public final class WmoWeatherCode {

    private static final Map<Integer, String> CODE_DESCRIPTIONS = new HashMap<>();

    static {
        CODE_DESCRIPTIONS.put(0, "Clear sky");
        CODE_DESCRIPTIONS.put(1, "Mainly clear");
        CODE_DESCRIPTIONS.put(2, "Partly cloudy");
        CODE_DESCRIPTIONS.put(3, "Overcast");
        CODE_DESCRIPTIONS.put(45, "Fog");
        CODE_DESCRIPTIONS.put(48, "Depositing rime fog");
        CODE_DESCRIPTIONS.put(51, "Light drizzle");
        CODE_DESCRIPTIONS.put(53, "Moderate drizzle");
        CODE_DESCRIPTIONS.put(55, "Dense drizzle");
        CODE_DESCRIPTIONS.put(56, "Light freezing drizzle");
        CODE_DESCRIPTIONS.put(57, "Dense freezing drizzle");
        CODE_DESCRIPTIONS.put(61, "Slight rain");
        CODE_DESCRIPTIONS.put(63, "Moderate rain");
        CODE_DESCRIPTIONS.put(65, "Heavy rain");
        CODE_DESCRIPTIONS.put(66, "Light freezing rain");
        CODE_DESCRIPTIONS.put(67, "Heavy freezing rain");
        CODE_DESCRIPTIONS.put(71, "Slight snow fall");
        CODE_DESCRIPTIONS.put(73, "Moderate snow fall");
        CODE_DESCRIPTIONS.put(75, "Heavy snow fall");
        CODE_DESCRIPTIONS.put(77, "Snow grains");
        CODE_DESCRIPTIONS.put(80, "Slight rain showers");
        CODE_DESCRIPTIONS.put(81, "Moderate rain showers");
        CODE_DESCRIPTIONS.put(82, "Violent rain showers");
        CODE_DESCRIPTIONS.put(85, "Slight snow showers");
        CODE_DESCRIPTIONS.put(86, "Heavy snow showers");
        CODE_DESCRIPTIONS.put(95, "Thunderstorm");
        CODE_DESCRIPTIONS.put(96, "Thunderstorm with slight hail");
        CODE_DESCRIPTIONS.put(99, "Thunderstorm with heavy hail");
    }

    private WmoWeatherCode() {
    }

    public static String getDescription(Integer code) {
        if (code == null) {
            return "Unknown";
        }
        return CODE_DESCRIPTIONS.getOrDefault(code, "Unclassified weather condition (WMO code " + code + ")");
    }
}
