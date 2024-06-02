package com.carbonara.gardenadvisor.openmeteo;

import com.carbonara.gardenadvisor.openmeteo.request.OpenMeteoRequest;
import com.carbonara.gardenadvisor.openmeteo.response.OpenMeteoResponseException;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class OkHttpOpenMeteoClient implements OpenMeteoClient{

    private final OkHttpClient httpClient;

    public OkHttpOpenMeteoClient(OkHttpClient httpClient) {
        this.httpClient = httpClient;
    }

    @Override
    public String getWeatherData(OpenMeteoRequest openMeteoRequest) throws IOException {
        Call call = buildCall(openMeteoRequest);
        Response response = call.execute();

        if (!response.isSuccessful()) {
            throw new OpenMeteoResponseException(response.message());
        }

        if (response.body() == null) {
            throw new OpenMeteoResponseException("Empty Response.");
        }

        return response.body().string();
    }

    @Override
    public void getWeatherDataAsync(OpenMeteoRequest openMeteoRequest, Callback callback) throws IOException {
        Call call = buildCall(openMeteoRequest);
        call.enqueue(callback);
    }

    @Override
    public String getWeatherData(float lat, float lon) throws IOException {
        OpenMeteoRequest request = OpenMeteoRequest.builder().lat(lat).lon(lon).build();
        return getWeatherData(request);
    }

    @Override
    public void getWeatherDataAsync(float lat, float lon, Callback callback) throws IOException {
        OpenMeteoRequest request = OpenMeteoRequest.builder().lat(lat).lon(lon).build();
        getWeatherDataAsync(request,callback);
    }

    protected Call buildCall(OpenMeteoRequest openMeteoRequest) {
        return httpClient.newCall(toOkHttpRequest(openMeteoRequest));
    }

    protected Request toOkHttpRequest(OpenMeteoRequest openMeteoRequest) {
        HttpUrl httpUrl = HttpUrl.parse(OpenMeteoConstants.Request.API_URL);

        // API_URL is a constant that should be correct, throwing a generic Runtime Exception
        // denoting a programming error - verified by unit tests as not occurring (or failing if the URL is updated)
        if (httpUrl == null) {
            throw new RuntimeException("Could not build HttpUrl from request API_URL");
        }

        HttpUrl.Builder httpUrlBuilder = httpUrl.newBuilder();
        httpUrlBuilder
                .addQueryParameter(OpenMeteoConstants.Request.LAT_PARAM, openMeteoRequest.getLat().toString())
                .addQueryParameter(OpenMeteoConstants.Request.LON_PARAM, openMeteoRequest.getLon().toString())
                .addQueryParameter(OpenMeteoConstants.Request.FORMAT_PARAM, openMeteoRequest.getFormat())
                .addQueryParameter(OpenMeteoConstants.Request.HOURLY_PARAM, String.join(",", openMeteoRequest.getHourlyWeatherVariables()))
                .addQueryParameter(OpenMeteoConstants.Request.PAST_DAYS_PARAM, openMeteoRequest.getPastDays().toString())
                .addQueryParameter(OpenMeteoConstants.Request.FORECAST_DAYS_PARAM, openMeteoRequest.getForecastDays().toString())
                .build();

        return new Request.Builder().url(httpUrlBuilder.build()).build();
    }
}
