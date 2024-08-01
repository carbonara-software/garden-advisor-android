package com.carbonara.gardenadvisor.util.ui;

import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import com.carbonara.gardenadvisor.MainActivity;
import com.carbonara.gardenadvisor.ui.dialog.error.ErrorDialog;
import com.carbonara.gardenadvisor.ui.dialog.loading.LoadingDialog;
import com.carbonara.gardenadvisor.ui.dialog.success.SuccessDialog;

public class BaseFragment extends Fragment {

  DialogFragment dialog;

  public void displayErrorDialog(String message) {
    if (dialogDismissible()) {
      dialog.dismiss();
    }
    dialog = new ErrorDialog(message);
    dialog.show(getChildFragmentManager(), ErrorDialog.TAG);
  }

  public void displaySuccessDialog(String message) {
    if (dialogDismissible()) {
      dialog.dismiss();
    }
    dialog = new SuccessDialog(message);
    dialog.show(getChildFragmentManager(), SuccessDialog.TAG);
  }

  public void displayLoadingDialog() {
    if (dialogDismissible()) {
      dialog.dismiss();
    }
    dialog = new LoadingDialog();
    dialog.show(getChildFragmentManager(), ErrorDialog.TAG);
  }

  public void closeDialog() {
    if (dialogDismissible()) {
      dialog.dismiss();
    }
  }

  public void showBottomBar() {
    if (getActivity() != null && getActivity() instanceof MainActivity) {
      ((MainActivity) getActivity()).showBottomBar();
    }
  }

  public void hideBottomBar() {
    if (getActivity() != null && getActivity() instanceof MainActivity) {
      ((MainActivity) getActivity()).hideBottomBar();
    }
  }

  private boolean dialogDismissible() {
    return dialog != null && getContext() != null;
  }
}
