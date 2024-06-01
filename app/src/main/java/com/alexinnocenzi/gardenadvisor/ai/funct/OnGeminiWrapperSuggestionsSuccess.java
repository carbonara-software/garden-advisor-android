package com.alexinnocenzi.gardenadvisor.ai.funct;

import com.alexinnocenzi.gardenadvisor.ai.dto.GeminiGardeningSugg;

public interface OnGeminiWrapperSuggestionsSuccess {

    void getAnswer(GeminiGardeningSugg weather);

}
