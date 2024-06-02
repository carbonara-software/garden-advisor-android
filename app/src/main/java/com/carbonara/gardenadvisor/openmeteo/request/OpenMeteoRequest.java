package com.carbonara.gardenadvisor.openmeteo.request;

import com.carbonara.gardenadvisor.openmeteo.OpenMeteoConstants;

import java.util.Set;

import lombok.Builder;
import lombok.Getter;
import lombok.NonNull;
import lombok.ToString;

@Builder
@Getter
@ToString
public class OpenMeteoRequest {
    @NonNull
    private final Float lat;

    @NonNull
    private final Float lon;

    @Builder.Default
    private final Integer pastDays = 7;

    @Builder.Default
    private final Integer forecastDays = 7;

    @Builder.Default
    private final Set<String> hourlyWeatherVariables = OpenMeteoConstants.Request.HOURLY_WEATHER_VALUES;

    @Builder.Default
    private final String format = "csv";
}
