package com.carbonara.gardenadvisor.ai.task.impl;

import static com.carbonara.gardenadvisor.util.AppUtil.byteArrayToBitmap;
import static com.carbonara.gardenadvisor.util.AppUtil.readFileContent;

import android.content.Context;
import android.graphics.Bitmap;
import com.carbonara.gardenadvisor.ai.dto.CachedCameraSuggestion;
import com.carbonara.gardenadvisor.ai.dto.GeminiCameraSuggestion;
import com.carbonara.gardenadvisor.util.AppCache;
import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.core.SingleEmitter;
import io.reactivex.rxjava3.core.SingleOnSubscribe;
import java.io.IOException;

public class GeminiCachedCameraTask implements SingleOnSubscribe<CachedCameraSuggestion> {

  private final String cachedSuggestionId;
  private final Context context;

  public GeminiCachedCameraTask(String cachedSuggestionId, Context context) {
    this.cachedSuggestionId = cachedSuggestionId;
    this.context = context;
  }

  @Override
  public void subscribe(@NonNull SingleEmitter<CachedCameraSuggestion> emitter) throws Throwable {
    GeminiCameraSuggestion cameraSuggestion =
        AppCache.getInstance().getCachedCameraSuggestions().get(cachedSuggestionId);

    try {
      byte[] savedPicture = readFileContent(context, cachedSuggestionId);
      Bitmap bitmap = byteArrayToBitmap(savedPicture);
      emitter.onSuccess(new CachedCameraSuggestion(cameraSuggestion, bitmap));
    } catch (IOException e) {
      emitter.onError(e);
    }
  }
}
