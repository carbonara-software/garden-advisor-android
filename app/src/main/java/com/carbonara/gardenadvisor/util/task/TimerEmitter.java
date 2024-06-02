package com.carbonara.gardenadvisor.util.task;

import static com.carbonara.gardenadvisor.util.LogUtil.logi;

import android.content.Context;
import com.carbonara.gardenadvisor.persistence.AppDatabase;
import com.carbonara.gardenadvisor.persistence.entity.FunFact;
import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.core.ObservableEmitter;
import io.reactivex.rxjava3.core.ObservableOnSubscribe;

public class TimerEmitter implements ObservableOnSubscribe<FunFact> {

  private final Context c;

  public TimerEmitter(Context c) {
    this.c = c;
  }

  @Override
  public void subscribe(@NonNull ObservableEmitter<FunFact> emitter) throws Throwable {
    while (!emitter.isDisposed()) {
      FunFact fact = AppDatabase.getDatabase(c).funFactDao().getRandomFunFact().blockingFirst();
      emitter.onNext(fact);
      try {
        Thread.sleep(5000);
      } catch (InterruptedException ex) {
        logi("interrotto in anticipo");
      }
    }
    if (!emitter.isDisposed()) {
      emitter.isDisposed();
    }
  }
}
