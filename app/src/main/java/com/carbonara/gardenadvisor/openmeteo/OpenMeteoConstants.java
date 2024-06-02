package com.carbonara.gardenadvisor.openmeteo;

import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class OpenMeteoConstants {

  public static class Request {

    public static final String API_URL = "https://api.open-meteo.com/v1/forecast";

    public static final String LAT_PARAM = "latitude";
    public static final String LON_PARAM = "longitude";
    public static final String HOURLY_PARAM = "hourly";
    public static final String FORMAT_PARAM = "format";
    public static final String PAST_DAYS_PARAM = "past_days";
    public static final String FORECAST_DAYS_PARAM = "forecast_days";

    public static final Set<String> HOURLY_WEATHER_VALUES =
        Stream.of(
                "temperature_2m",
                "relative_humidity_2m",
                "precipitation_probability",
                "rain",
                "showers",
                "snowfall",
                "snow_depth",
                "surface_pressure",
                "cloud_cover",
                "wind_speed_10m",
                "wind_speed_80m",
                "wind_speed_120m",
                "soil_temperature_0cm",
                "soil_temperature_6cm",
                "soil_temperature_18cm",
                "soil_moisture_0_to_1cm",
                "soil_moisture_1_to_3cm",
                "soil_moisture_3_to_9cm")
            .collect(Collectors.toSet());
  }
}
