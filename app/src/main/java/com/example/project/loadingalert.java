package com.example.project;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;

import androidx.appcompat.app.AlertDialog;

public class loadingalert {
    public static AlertDialog showCustomDialog(Activity activity) {
        LayoutInflater li = LayoutInflater.from(activity);
        View promptsView = li.inflate(R.layout.loadingoverlay, null);

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(activity);
        alertDialogBuilder.setView(promptsView);
        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
        return alertDialog;
    }
}
