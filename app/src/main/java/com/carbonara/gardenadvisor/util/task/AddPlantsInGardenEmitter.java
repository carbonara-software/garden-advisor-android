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
import io.reactivex.rxjava3.core.SingleEmitter;
import io.reactivex.rxjava3.core.SingleOnSubscribe;
import io.reactivex.rxjava3.disposables.Disposable;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class AddPlantsInGardenEmitter implements SingleOnSubscribe<List<Plant>> {

  private final Context context;
  private final long gardenId;
  private final List<Plant> plants;
  private GardenRepository repo;
  private ObservableEmitter<Boolean> emitter;

  public AddPlantsInGardenEmitter(Context context, long gardenId, Set<Plant> plants) {
    this.context = context;
    this.gardenId = gardenId;
    this.plants = new ArrayList<>(plants);
  }

  @Override
  public void subscribe(@NonNull SingleEmitter<List<Plant>> emitter) throws Throwable {
    GardenDao gDao = AppDatabase.getDatabase(context).gardenDao();
    PlantDao pDao = AppDatabase.getDatabase(context).plantDao();
    repo = new GardenRepository(gDao, pDao);
    repo.addPlantsToGarden(gardenId, plants)
        .doOnError(emitter::tryOnError)
        .doOnComplete(() -> {
          Disposable b = repo.getGardenWithPlants(gardenId)
              .subscribe(garden -> {
                emitter.onSuccess(new ArrayList<>(garden.getPlants()));
              }, emitter::tryOnError);
        })
        .subscribe();
  }
}
