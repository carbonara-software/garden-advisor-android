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
import io.reactivex.rxjava3.disposables.Disposable;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class CreatePlantsInGardenEmitter implements ObservableOnSubscribe<Boolean> {

  private final Context c;
  private final Garden garden;
  private final List<Plant> plants;
  private Disposable d;
  private GardenRepository repo;
  private ObservableEmitter<Boolean> emitter;

  public CreatePlantsInGardenEmitter(Context c, Garden garden, Set<Plant> plants) {
    this.c = c;
    this.garden = garden;
    this.plants = new ArrayList<>(plants);
  }

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
    this.emitter = emitter;
    GardenDao gDao = AppDatabase.getDatabase(c).gardenDao();
    PlantDao pDao = AppDatabase.getDatabase(c).plantDao();
    repo = new GardenRepository(gDao, pDao);
    d = repo.deletePlantsFromGarden(garden).subscribe(this::onCompleteDelete, this::onErrorDelete);
  }

  private void onErrorDelete(Throwable throwable) {
    if (emitter != null && !emitter.isDisposed()) {
      // Not shure but this could lead in a crash because it might opens
      // a dialog and it is running in the background thread... Vincenzo will fix this <3
      emitter.tryOnError(throwable);
    }
  }

  private void onCompleteDelete() {
    if (emitter != null && !emitter.isDisposed())
      repo.addPlantsToGarden(garden.getId(), plants)
          .doOnError(emitter::tryOnError)
          .doOnComplete(emitter::onComplete)
          .subscribe();
  }
}
