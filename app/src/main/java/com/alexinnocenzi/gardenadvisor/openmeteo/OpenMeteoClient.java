package com.alexinnocenzi.gardenadvisor.openmeteo;

public interface OpenMeteoClient {

    String getWeatherData(Float lat, Float lon);
}
