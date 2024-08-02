package com.carbonara.gardenadvisor.util.task;

import static com.carbonara.gardenadvisor.util.LogUtil.loge;

import android.content.Context;
import com.carbonara.gardenadvisor.persistence.AppDatabase;
import com.carbonara.gardenadvisor.persistence.entity.FunFact;
import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.core.ObservableEmitter;
import io.reactivex.rxjava3.core.ObservableOnSubscribe;

public class TimerEmitter implements ObservableOnSubscribe<FunFact> {

  private final Context context;

  public TimerEmitter(Context context) {
    this.context = context;
  }

  @Override
  public void subscribe(@NonNull ObservableEmitter<FunFact> emitter) throws Throwable {
    while (!emitter.isDisposed()) {
      FunFact fact =
          AppDatabase.getDatabase(context).funFactDao().getRandomFunFact().blockingFirst();
      emitter.onNext(fact);
      try {
        Thread.sleep(5000);
      } catch (InterruptedException ex) {
        loge("TimerEmitter Interrupted", ex);
      }
    }
    if (!emitter.isDisposed()) {
      emitter.isDisposed();
    }
  }
}
