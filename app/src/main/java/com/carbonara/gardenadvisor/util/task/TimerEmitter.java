package com.carbonara.gardenadvisor.util.task;

import android.content.Context;
import com.carbonara.gardenadvisor.persistence.AppDatabase;
import com.carbonara.gardenadvisor.persistence.entity.FunFact;
import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.core.ObservableEmitter;
import io.reactivex.rxjava3.core.ObservableOnSubscribe;
import io.reactivex.rxjava3.exceptions.UndeliverableException;

public class TimerEmitter implements ObservableOnSubscribe<FunFact> {

  private final Context context;

  public TimerEmitter(Context context) {
    this.context = context;
  }

  @Override
  public void subscribe(@NonNull ObservableEmitter<FunFact> emitter) throws Throwable {
    try {
      while (!emitter.isDisposed()) {
        FunFact fact =
            AppDatabase.getDatabase(context).funFactDao().getRandomFunFact().blockingFirst();
        emitter.onNext(fact);
        Thread.sleep(5000);
      }
      if (!emitter.isDisposed()) {
        emitter.onComplete();
      }
    } catch (UndeliverableException | InterruptedException ex) {
      // loge("TimerEmitter Interrupted", ex);
    }
  }
}
