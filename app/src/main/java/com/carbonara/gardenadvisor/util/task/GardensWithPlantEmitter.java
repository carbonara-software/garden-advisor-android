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
import io.reactivex.rxjava3.disposables.Disposable;
import java.util.List;

public class GardensWithPlantEmitter implements ObservableOnSubscribe<List<GardenWithPlants>> {

  private final Context context;
  private Disposable disposable;

  public GardensWithPlantEmitter(Context c) {
    this.context = c;
  }

  @Override
  public void subscribe(@NonNull ObservableEmitter<List<GardenWithPlants>> emitter)
      throws Throwable {
    GardenDao gDao = AppDatabase.getDatabase(context).gardenDao();
    PlantDao pDao = AppDatabase.getDatabase(context).plantDao();
    GardenRepository repo = new GardenRepository(gDao, pDao);
    disposable = repo.getGardensWithPlants().subscribe(emitter::onNext, emitter::tryOnError);
  }
}
