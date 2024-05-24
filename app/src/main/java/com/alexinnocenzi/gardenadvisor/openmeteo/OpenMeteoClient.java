package com.alexinnocenzi.gardenadvisor.openmeteo;
import com.alexinnocenzi.gardenadvisor.openmeteo.request.OpenMeteoRequest;

import java.io.IOException;

import okhttp3.Callback;

public interface OpenMeteoClient {

    String getWeatherData(OpenMeteoRequest openMeteoRequest) throws IOException;
    void getWeatherDataAsync(OpenMeteoRequest openMeteoRequest, Callback callback) throws IOException;
}
