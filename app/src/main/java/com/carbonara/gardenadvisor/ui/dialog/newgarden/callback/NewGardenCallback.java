package com.carbonara.gardenadvisor.ui.dialog.newgarden.callback;

public interface NewGardenCallback {

  void successGardenCreation();

  void errorGardenCreation(Throwable error);
}
