package com.alexinnocenzi.gardenadvisor.ui.dialog.loading;

import static com.alexinnocenzi.gardenadvisor.util.LogUtil.logd;
import static com.alexinnocenzi.gardenadvisor.util.LogUtil.loge;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;

import com.alexinnocenzi.gardenadvisor.R;
import com.alexinnocenzi.gardenadvisor.databinding.DialogLoadingBinding;
import com.alexinnocenzi.gardenadvisor.persistence.AppDatabase;
import com.alexinnocenzi.gardenadvisor.persistence.entity.FunFact;
import com.alexinnocenzi.gardenadvisor.util.task.TimerEmitter;

import java.sql.Time;
import java.util.concurrent.TimeUnit;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.functions.Consumer;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class LoadingDialog extends DialogFragment {

    public static final String TAG = "LoadDialog";
    DialogLoadingBinding binding;
    private Disposable disposable;

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        binding = DialogLoadingBinding.inflate(getLayoutInflater());
        return new AlertDialog.Builder(requireContext(), R.style.TransparentDialog)
                .setCancelable(false)
                .setView(binding.getRoot()).create();
    }

    @Override
    public void onStart() {
        super.onStart();
        startWordTimer();
    }

    private void startWordTimer() {
        disposable = Observable.create(new TimerEmitter(requireContext())).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::nextFunFact,this::fail,this::complete);
    }

    private void complete() {
    }

    private void nextFunFact(FunFact s) {
        binding.message.setText(s.getFact());
    }

    private void fail(Throwable throwable) {
        if(disposable!=null && !disposable.isDisposed()){
            disposable.dispose();
            disposable = null;
        }
        startWordTimer();
    }

    @Override
    public void onCancel(@NonNull DialogInterface dialog) {
        super.onCancel(dialog);
        if(disposable!=null && !disposable.isDisposed()){
            disposable.dispose();
            disposable = null;
        }
    }

    @Override
    public void onDismiss(@NonNull DialogInterface dialog) {
        super.onDismiss(dialog);
        if(disposable!=null && !disposable.isDisposed()){
            disposable.dispose();
            disposable = null;
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if(disposable!=null && !disposable.isDisposed()){
            disposable.dispose();
            disposable = null;
        }
    }
}
