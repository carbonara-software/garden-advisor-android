package com.carbonara.gardenadvisor.ai.task;

import com.carbonara.gardenadvisor.ai.dto.GeminiResult;
import io.reactivex.rxjava3.core.SingleOnSubscribe;

public interface GeminiSingleOnSubscriber<T extends GeminiResult> extends SingleOnSubscribe<T> {}
