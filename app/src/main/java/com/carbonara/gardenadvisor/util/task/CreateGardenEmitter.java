package com.carbonara.gardenadvisor.util.task;

import android.content.Context;
import com.carbonara.gardenadvisor.persistence.AppDatabase;
import com.carbonara.gardenadvisor.persistence.dao.GardenDao;
import com.carbonara.gardenadvisor.persistence.dao.PlantDao;
import com.carbonara.gardenadvisor.persistence.entity.Garden;
import com.carbonara.gardenadvisor.persistence.repository.GardenRepository;
import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.core.ObservableEmitter;
import io.reactivex.rxjava3.core.ObservableOnSubscribe;

public class CreateGardenEmitter implements ObservableOnSubscribe<Boolean> {

  private final Context context;
  private final Garden garden;

  public CreateGardenEmitter(Context context, Garden garden) {
    this.context = context;
    this.garden = garden;
  }

  @Override
  public void subscribe(@NonNull ObservableEmitter<Boolean> emitter) throws Throwable {
    GardenDao gDao = AppDatabase.getDatabase(context).gardenDao();
    PlantDao pDao = AppDatabase.getDatabase(context).plantDao();
    GardenRepository repo = new GardenRepository(gDao, pDao);
    repo.insertGarden(garden)
        .doOnError(emitter::tryOnError)
        .doOnComplete(emitter::onComplete)
        .subscribe();
  }
}
