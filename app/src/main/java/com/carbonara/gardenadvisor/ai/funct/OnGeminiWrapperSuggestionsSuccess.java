package com.carbonara.gardenadvisor.ai.funct;

import com.carbonara.gardenadvisor.ai.dto.GeminiGardeningSugg;

public interface OnGeminiWrapperSuggestionsSuccess {

    void getAnswer(GeminiGardeningSugg weather);

}
