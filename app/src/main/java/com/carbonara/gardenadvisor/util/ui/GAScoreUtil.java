package com.carbonara.gardenadvisor.util.ui;

import com.carbonara.gardenadvisor.ai.dto.GardeningItem;

public class GAScoreUtil {

  public static int getGaScore(GardeningItem gardeningItem) {
    return gardeningItem.getMaintenanceScore() + gardeningItem.getRecommendedScore();
  }
}
