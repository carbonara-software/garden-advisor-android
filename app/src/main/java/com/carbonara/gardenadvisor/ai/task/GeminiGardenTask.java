package com.carbonara.gardenadvisor.ai.task;

import com.carbonara.gardenadvisor.ai.dto.GeminiGardeningSugg;
import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.core.SingleEmitter;
import io.reactivex.rxjava3.core.SingleOnSubscribe;

public class GeminiGardenTask implements SingleOnSubscribe<GeminiGardeningSugg> {

  @Override
  public void subscribe(@NonNull SingleEmitter<GeminiGardeningSugg> emitter) throws Throwable {

  }
}
