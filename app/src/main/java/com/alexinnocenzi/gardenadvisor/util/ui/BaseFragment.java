package com.alexinnocenzi.gardenadvisor.util.ui;

import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;

import com.alexinnocenzi.gardenadvisor.ui.dialog.error.ErrorDialog;
import com.alexinnocenzi.gardenadvisor.ui.dialog.loading.LoadingDialog;
import com.alexinnocenzi.gardenadvisor.ui.dialog.success.SuccessDialog;

public class BaseFragment extends Fragment {

    DialogFragment dialog;

    public void displayErrorDialog(String message){
        if(dialog!=null){
            dialog.dismiss();
        }
        dialog = new ErrorDialog(message);
        dialog.show(getChildFragmentManager(),ErrorDialog.TAG);
    }

    public void displaySuccessDialog(String message){
        if(dialog!=null){
            dialog.dismiss();
        }
        dialog = new SuccessDialog(message);
        dialog.show(getChildFragmentManager(),SuccessDialog.TAG);
    }

    public void displayLoadingDialog(){
        if(dialog!=null){
            dialog.dismiss();
        }
        dialog = new LoadingDialog();
        dialog.show(getChildFragmentManager(),ErrorDialog.TAG);
    }

    public void closeDialog(){
        if(dialog != null){
            dialog.dismiss();
        }
    }

}
