package com.carbonara.gardenadvisor.util.ui;

import com.carbonara.gardenadvisor.R;
import com.carbonara.gardenadvisor.ai.dto.GardeningItem;

public class BackgroundChooser {

  public static int getBackgroundForItem(GardeningItem gardeningItem) {
    switch (gardeningItem.getType()) {
      case FRUIT:
        return R.drawable.fruit_background;
      case FLOWER:
        return R.drawable.flower_background;
      case VEGETABLE:
        return R.drawable.vegetable_background;
    }
    return R.color.app_accent_1;
  }
}
