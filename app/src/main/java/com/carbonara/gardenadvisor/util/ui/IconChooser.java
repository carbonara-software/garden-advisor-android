package com.carbonara.gardenadvisor.util.ui;

import com.carbonara.gardenadvisor.R;

public class IconChooser {

  public static int getIcon(int icon) {
    switch (icon) {
      case 1:
        return R.drawable.bad_clouds;
      case 2:
        return R.drawable.cloud;
      case 3:
        return R.drawable.cloud_tunder;
      case 4:
        return R.drawable.fog;
      case 5:
        return R.drawable.rain;
      case 6:
        return R.drawable.snow;
      case 7:
        return R.drawable.sun;
      case 8:
        return R.drawable.sun_cloud;
      case 9:
        return R.drawable.sun_cloud_rain;
      case 10:
        return R.drawable.thunder;
    }
    return R.drawable.cloud;
  }
}
