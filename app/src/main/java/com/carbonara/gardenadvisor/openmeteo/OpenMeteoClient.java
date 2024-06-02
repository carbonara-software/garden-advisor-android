package com.carbonara.gardenadvisor.openmeteo;
import com.carbonara.gardenadvisor.openmeteo.request.OpenMeteoRequest;

import java.io.IOException;

import okhttp3.Callback;

public interface OpenMeteoClient {

    String getWeatherData(OpenMeteoRequest openMeteoRequest) throws IOException;
    void getWeatherDataAsync(OpenMeteoRequest openMeteoRequest, Callback callback) throws IOException;

    String getWeatherData(float lat, float lon) throws IOException;
    void getWeatherDataAsync(float lat, float lon, Callback callback) throws IOException;
}
