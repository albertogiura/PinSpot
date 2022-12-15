package com.blackbox.pinspotdemo.util;

import android.app.Activity;
import android.app.AlertDialog;
import android.view.LayoutInflater;

import com.blackbox.pinspotdemo.databinding.DialogLayoutBinding;

public class LoadingDialog {
    private Activity activity;
    private AlertDialog alertDialog;

    public LoadingDialog (Activity activity) {
        this.activity = activity;
    }

    public void startLoading() {
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        DialogLayoutBinding binding = DialogLayoutBinding.inflate(LayoutInflater.from(activity), null, false);
        builder.setView(binding.getRoot());
        builder.setCancelable(false);
        alertDialog = builder.create();
        alertDialog.show();
    }

    public void stopLoading() {
        alertDialog.dismiss();
    }
}
