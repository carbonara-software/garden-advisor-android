package com.alexinnocenzi.gardenadvisor.ai;

import static com.alexinnocenzi.gardenadvisor.util.LogUtil.loge;
import static com.alexinnocenzi.gardenadvisor.util.LogUtil.logi;

import androidx.annotation.NonNull;

import com.alexinnocenzi.gardenadvisor.ai.dto.GeminiWeather;
import com.alexinnocenzi.gardenadvisor.ai.exceptions.GeminiWeatherException;
import com.alexinnocenzi.gardenadvisor.ai.funct.OnAnserFail;
import com.alexinnocenzi.gardenadvisor.ai.funct.OnAnswerListener;
import com.alexinnocenzi.gardenadvisor.openmeteo.OkHttpOpenMeteoClient;
import com.alexinnocenzi.gardenadvisor.openmeteo.request.OpenMeteoRequest;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.ai.client.generativeai.GenerativeModel;
import com.google.ai.client.generativeai.java.GenerativeModelFutures;
import com.google.ai.client.generativeai.type.Content;
import com.google.ai.client.generativeai.type.GenerateContentResponse;
import com.google.common.util.concurrent.FutureCallback;
import com.google.common.util.concurrent.Futures;
import com.google.common.util.concurrent.ListenableFuture;

import java.io.IOException;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Response;

public class GeminiWrapper{


    private static GeminiWrapper instance;

    GenerativeModelFutures model ;

    float lat = 42.024862f,lon = 12.998049f;

    public static GeminiWrapper getInstance(){
        if(instance == null)
            instance = new GeminiWrapper();
        return instance;
    }

    private GeminiWrapper(){
        GenerativeModel gm = new GenerativeModel(
                "gemini-1.5-flash",
                "AIzaSyCgSYQoHBUKb8IyN_6AqjZ7MTCtu3u9w3U" //TODO: must find a safer place
        );
        model = GenerativeModelFutures.from(gm);
    }



    public void getAnswerWeather(OnAnswerListener success, OnAnserFail fail) {
        OkHttpOpenMeteoClient client = new OkHttpOpenMeteoClient(new OkHttpClient());
        OpenMeteoRequest request =  OpenMeteoRequest.builder().lon(lon).lat(lat).build();
        try {
            client.getWeatherDataAsync(request, new Callback() {
                @Override
                public void onFailure(@NonNull Call call, @NonNull IOException e) {
                    fail.getAnswerFail(e);
                }

                @Override
                public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                    if(!response.isSuccessful() || response.body()==null){
                        loge("Errore response");
                        loge(response.toString());
                        fail.getAnswerFail(new GeminiWeatherException("Weather not returned...Strange and sad at the same time...."));
                    }else{
                        String message = "\nLocation Name: Roviano (RM)\n"
                                + "Given the weather and location data provide the following information in JSON format:\n" +
                                " - a \"location\" object, containing the location data, such as name, region and country\n" +
                                " - a \"weather\" object, containing the weather forecast (min and max temperatures and weather conditions for today and the next 6 days)\n"+
                                " - the output json must respect the following schema or example:\n"+
                                "{\n" +
                                "  \"location\": {\n" +
                                "    \"name\": \"Craco\",\n" +
                                "    \"region\": \"Basilicata\",\n" +
                                "    \"country\": \"Italy\"\n" +
                                "  },\n" +
                                "  \"weather\": {\n" +
                                "    \"forecast\": [\n" +
                                "      {\n" +
                                "        \"date\": \"2024-05-17\",\n" +
                                "        \"min_temp\": 12.4,\n" +
                                "        \"max_temp\": 28.8,\n" +
                                "        \"conditions\": \"Sunny\"\n" +
                                "      },\n" +
                                "      {\n" +
                                "        \"date\": \"2024-05-18\",\n" +
                                "        \"min_temp\": 11.8,\n" +
                                "        \"max_temp\": 25.8,\n" +
                                "        \"conditions\": \"Partly cloudy with a chance of showers\"\n" +
                                "      },\n" +
                                "      {\n" +
                                "        \"date\": \"2024-05-19\",\n" +
                                "        \"min_temp\": 13.6,\n" +
                                "        \"max_temp\": 25.3,\n" +
                                "        \"conditions\": \"Partly cloudy with a chance of showers\"\n" +
                                "      },\n" +
                                "      {\n" +
                                "        \"date\": \"2024-05-20\",\n" +
                                "        \"min_temp\": 12.7,\n" +
                                "        \"max_temp\": 24.6,\n" +
                                "        \"conditions\": \"Mostly cloudy\"\n" +
                                "      },\n" +
                                "      {\n" +
                                "        \"date\": \"2024-05-21\",\n" +
                                "        \"min_temp\": 14.8,\n" +
                                "        \"max_temp\": 25.2,\n" +
                                "        \"conditions\": \"Sunny and windy\"\n" +
                                "      },\n" +
                                "      {\n" +
                                "        \"date\": \"2024-05-22\",\n" +
                                "        \"min_temp\": 12.7,\n" +
                                "        \"max_temp\": 23.0,\n" +
                                "        \"conditions\": \"Sunny and windy\"\n" +
                                "      },\n" +
                                "      {\n" +
                                "        \"date\": \"2024-05-23\",\n" +
                                "        \"min_temp\": 12.1,\n" +
                                "        \"max_temp\": 25.2,\n" +
                                "        \"conditions\": \"Sunny\"\n" +
                                "      }\n" +
                                "    ]\n" +
                                "  }\n" +
                                "}" +
                                "\n Important notice: DO NOT PRINT ANY THING ELSE BUT THE JSON ASKED as a plain text without any Markdown formatting please";
                        logi("INFO: " + message);
                        Content content = new Content.Builder()
                                .addText(response.body().string()+message)
                                .build();
                        Executor executor = Executors.newSingleThreadExecutor();
                        ListenableFuture<GenerateContentResponse> resp = model.generateContent(content);
                        Futures.addCallback(resp, new FutureCallback<GenerateContentResponse>() {
                            @Override
                            public void onSuccess(GenerateContentResponse result) {
                                String resultText = result.getText();
                                try{
                                    loge("JSON: " + resultText);
                                    ObjectMapper mapper = new ObjectMapper();
                                    GeminiWeather weather = mapper.readValue(resultText, GeminiWeather.class);
                                    success.getAnswer(weather);
                                }catch (JsonProcessingException e){
                                    fail.getAnswerFail(e);
                                }
                            }

                            @Override
                            public void onFailure(Throwable t) {
                                fail.getAnswerFail(t);

                            }
                        }, executor);
                    }
                }
            });
        } catch (IOException e) {
            fail.getAnswerFail(e);
        }

    }
}
