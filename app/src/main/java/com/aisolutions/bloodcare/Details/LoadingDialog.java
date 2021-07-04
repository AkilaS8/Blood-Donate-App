package com.aisolutions.bloodcare.Details;/*
Created by Akila Ishan on 2021-06-18.
*/

import android.app.Activity;
import android.app.AlertDialog;
import android.view.LayoutInflater;

import com.aisolutions.bloodcare.R;

public class LoadingDialog {
    Activity activity;
    AlertDialog alertDialog;

    public LoadingDialog(Activity activity) {
        this.activity = activity;
    }

    public void startLoading() {
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        LayoutInflater inflater = activity.getLayoutInflater();
        builder.setView(inflater.inflate(R.layout.dialog_dox_loading, null));
        builder.setCancelable(false);

        alertDialog = builder.create();
        alertDialog.show();
    }

    public void dismissLoading() {
        alertDialog.dismiss();
    }
}
