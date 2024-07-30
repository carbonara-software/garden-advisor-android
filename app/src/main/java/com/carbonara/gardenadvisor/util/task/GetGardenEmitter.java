package com.carbonara.gardenadvisor.util.task;

import android.content.Context;
import com.carbonara.gardenadvisor.persistence.AppDatabase;
import com.carbonara.gardenadvisor.persistence.dao.GardenDao;
import com.carbonara.gardenadvisor.persistence.dao.PlantDao;
import com.carbonara.gardenadvisor.persistence.entity.GardenWithPlants;
import com.carbonara.gardenadvisor.persistence.repository.GardenRepository;
import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.core.ObservableEmitter;
import io.reactivex.rxjava3.core.ObservableOnSubscribe;

public class GetGardenEmitter implements ObservableOnSubscribe<GardenWithPlants> {

  private final Context c;
  private final long id;

  public GetGardenEmitter(Context c, long id) {
    this.c = c;
    this.id = id;
  }

  @Override
  public void subscribe(@NonNull ObservableEmitter<GardenWithPlants> emitter) throws Throwable {
    GardenDao gDao = AppDatabase.getDatabase(c).gardenDao();
    PlantDao pDao = AppDatabase.getDatabase(c).plantDao();
    GardenRepository repo = new GardenRepository(gDao, pDao);
    repo.getGardenWithPlants(id)
        .doOnError(emitter::tryOnError)
        .doOnComplete(emitter::onComplete)
        .subscribe();
  }
}
