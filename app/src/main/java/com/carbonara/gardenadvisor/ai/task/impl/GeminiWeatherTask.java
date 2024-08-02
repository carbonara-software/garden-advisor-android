package com.carbonara.gardenadvisor.ai.task.impl;

import static com.carbonara.gardenadvisor.ai.prompt.ConstPrompt.WEATHER_PROMPT;
import static com.carbonara.gardenadvisor.util.ApiKeyUtility.getGeminiApiKey;
import static com.carbonara.gardenadvisor.util.LogUtil.loge;

import com.carbonara.gardenadvisor.ai.cache.WeatherCache;
import com.carbonara.gardenadvisor.ai.dto.GeminiWeather;
import com.carbonara.gardenadvisor.ai.task.GeminiSingleOnSubscriber;
import com.carbonara.gardenadvisor.ai.task.GeminiTask;
import com.carbonara.gardenadvisor.util.AppCache;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.google.ai.client.generativeai.GenerativeModel;
import com.google.ai.client.generativeai.java.GenerativeModelFutures;
import com.google.ai.client.generativeai.type.Content;
import com.google.ai.client.generativeai.type.GenerateContentResponse;
import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.core.SingleEmitter;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatterBuilder;
import java.util.concurrent.CancellationException;
import java.util.concurrent.ExecutionException;
import lombok.Getter;

@Getter
public class GeminiWeatherTask extends GeminiTask
    implements GeminiSingleOnSubscriber<GeminiWeather> {

  public GeminiWeatherTask(float lat, float lon, String locationName) {
    super(lat, lon, locationName);
  }

  @Override
  public String getPrompt(String weather) {
    return weather
        + "\nLocation Name: "
        + getLocationName()
        + "\n"
        + WEATHER_PROMPT
        + LocalDate.now()
            .format(new DateTimeFormatterBuilder().appendPattern("yyyy/MM/dd").toFormatter());
  }

  public void subscribe(@NonNull SingleEmitter<GeminiWeather> emitter) {
    try {
      GeminiWeather weatherCached = geminiWeather();
      if (weatherCached != null) {
        if (!emitter.isDisposed()) emitter.onSuccess(weatherCached);
        return;
      }
      String weatherString = weatherData();
      Content content = new Content.Builder().addText(getPrompt(weatherString)).build();
      GenerativeModel gm = new GenerativeModel("gemini-1.5-flash", getGeminiApiKey());
      GenerativeModelFutures model = GenerativeModelFutures.from(gm);
      GenerateContentResponse response = model.generateContent(content).get();
      String resultText = response.getText();
      ObjectMapper mapper = new ObjectMapper();
      mapper.registerModule(new JavaTimeModule());

      GeminiWeather weather = mapper.readValue(resultText, GeminiWeather.class);
      if (!emitter.isDisposed()) {
        emitter.onSuccess(weather);
      }

      AppCache.getInstance()
          .addCachedData(
              WeatherCache.builder()
                  .lat(getLat())
                  .lon(getLon())
                  .locationName(getLocationName())
                  .weather(weather)
                  .weatherString(weatherString)
                  .build());
    } catch (IOException | CancellationException | ExecutionException e) {
      if (!emitter.isDisposed()) emitter.onError(e);
    } catch (InterruptedException interruptedException) {
      loge("GeminiWeatherTask interrupted", interruptedException);
    }
  }
}
