package com.carbonara.gardenadvisor.util.task;

import com.carbonara.gardenadvisor.ai.dto.GeminiCameraSuggestion;
import com.carbonara.gardenadvisor.util.AppCache;
import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.core.SingleEmitter;
import io.reactivex.rxjava3.core.SingleOnSubscribe;
import java.util.List;

public class CameraHistoryEmitter implements SingleOnSubscribe<List<GeminiCameraSuggestion>> {

  @Override
  public void subscribe(@NonNull SingleEmitter<List<GeminiCameraSuggestion>> emitter)
      throws Throwable {
    List<GeminiCameraSuggestion> cachedSuggestions =
        AppCache.getInstance().getCachedCameraSuggestions();
    if (cachedSuggestions != null && !emitter.isDisposed()) {
      emitter.onSuccess(cachedSuggestions);
    }
  }
}
