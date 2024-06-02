package com.carbonara.gardenadvisor.persistence;

import android.content.Context;
import android.util.Log;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import com.carbonara.gardenadvisor.persistence.dao.FunFactDao;
import com.carbonara.gardenadvisor.persistence.dao.GardenDao;
import com.carbonara.gardenadvisor.persistence.dao.PlantDao;
import com.carbonara.gardenadvisor.persistence.entity.FunFact;
import com.carbonara.gardenadvisor.persistence.entity.Garden;
import com.carbonara.gardenadvisor.persistence.entity.Plant;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

@Database(
    entities = {FunFact.class, Garden.class, Plant.class},
    version = 2)
public abstract class AppDatabase extends RoomDatabase {
  public abstract FunFactDao funFactDao();

  public abstract GardenDao gardenDao();

  public abstract PlantDao plantDao();

  private static volatile AppDatabase appDatabase;

  public static AppDatabase getDatabase(final Context context) {
    if (appDatabase == null) {
      synchronized (AppDatabase.class) {
        appDatabase =
            Room.databaseBuilder(
                    context.getApplicationContext(), AppDatabase.class, "garden-advisor.db")
                .build();

        try {
          appDatabase.populate(context);
        } catch (IOException e) {
          Log.e("AppDatabase", "could not pre-populate db. {}", e);
        }
      }
    }
    return appDatabase;
  }

  private void populate(Context context) throws IOException {
    InputStream funFactsJsonStream = context.getAssets().open("json/fun_facts.json");
    List<FunFact> funFacts = getFunFacts(funFactsJsonStream);
    appDatabase.funFactDao().insertFunFacts(funFacts).subscribe();
  }

  private static List<FunFact> getFunFacts(InputStream jsonInputStream) throws IOException {
    return new ObjectMapper().readValue(jsonInputStream, new TypeReference<List<FunFact>>() {});
  }
}
