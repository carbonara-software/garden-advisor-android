package com.alexinnocenzi.gardenadvisor.openmeteo;

import com.alexinnocenzi.gardenadvisor.openmeteo.request.OpenMeteoRequest;

import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;

public class OkHttpOpenMeteoClient implements OpenMeteoClient{

    OkHttpClient httpClient = new OkHttpClient();

    @Override
    public String getWeatherData(Float lat, Float lon) {
        //TODO
        return "";
    }

    protected Request toOkHttpRequest(OpenMeteoRequest openMeteoRequest) throws Exception {
        HttpUrl httpUrl = HttpUrl.parse(OpenMeteoConstants.Request.API_URL);

        if (httpUrl == null) {
            throw new Exception();
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
