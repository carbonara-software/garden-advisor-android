package com.carbonara.gardenadvisor.util.task;

import android.content.Context;
import com.carbonara.gardenadvisor.persistence.AppDatabase;
import com.carbonara.gardenadvisor.persistence.dao.GardenDao;
import com.carbonara.gardenadvisor.persistence.dao.PlantDao;
import com.carbonara.gardenadvisor.persistence.entity.Garden;
import com.carbonara.gardenadvisor.persistence.entity.Plant;
import com.carbonara.gardenadvisor.persistence.repository.GardenRepository;
import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.core.ObservableEmitter;
import io.reactivex.rxjava3.core.ObservableOnSubscribe;
import java.util.ArrayList;
import java.util.List;

public class CreatePlantsInGardenEmitter implements ObservableOnSubscribe<Boolean> {

  private final Context c;
  private final Garden garden;
  private final List<Plant> plants;

  public CreatePlantsInGardenEmitter(Context c, Garden garden, List<Plant> plants) {
    this.c = c;
    this.garden = garden;
    this.plants = plants;
  }

  public CreatePlantsInGardenEmitter(Context c, Garden garden, Plant plant) {
    this.c = c;
    this.garden = garden;
    this.plants = new ArrayList<>();
    plants.add(plant);
  }

  @Override
  public void subscribe(@NonNull ObservableEmitter<Boolean> emitter) throws Throwable {
    GardenDao gDao = AppDatabase.getDatabase(c).gardenDao();
    PlantDao pDao = AppDatabase.getDatabase(c).plantDao();
    GardenRepository repo = new GardenRepository(gDao, pDao);
    repo.addPlantsToGarden(garden.getId(), plants)
        .doOnError(emitter::tryOnError)
        .doOnComplete(emitter::onComplete)
        .subscribe();
  }
}
