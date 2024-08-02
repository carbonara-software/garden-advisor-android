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
import java.util.Set;

public class CreatePlantsInGardenEmitter implements ObservableOnSubscribe<Boolean> {

  private final Context context;
  private final Garden garden;
  private final List<Plant> plants;
  private GardenRepository repo;
  private ObservableEmitter<Boolean> emitter;

  public CreatePlantsInGardenEmitter(Context context, Garden garden, Set<Plant> plants) {
    this.context = context;
    this.garden = garden;
    this.plants = new ArrayList<>(plants);
  }

  @Override
  public void subscribe(@NonNull ObservableEmitter<Boolean> emitter) throws Throwable {
    this.emitter = emitter;
    GardenDao gDao = AppDatabase.getDatabase(context).gardenDao();
    PlantDao pDao = AppDatabase.getDatabase(context).plantDao();
    repo = new GardenRepository(gDao, pDao);
    repo.deletePlantsFromGarden(garden)
        .doOnComplete(this::onCompleteDelete)
        .doOnError(this::onErrorDelete)
        .subscribe();
  }

  private void onErrorDelete(Throwable throwable) {
    if (emitter != null && !emitter.isDisposed()) {
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
