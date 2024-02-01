package com.example.project;

import android.app.AlertDialog;
import android.app.Service;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.IBinder;
import android.provider.Settings;
import android.view.WindowManager;
import android.widget.Toast;

public class InternetChecker extends Service {

    private AlertDialog networkDialog;

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        showNetworkDialog();
        return START_STICKY;
    }

    private void showNetworkDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getApplicationContext());
        builder.setTitle("Network Unavailable");
        builder.setMessage("Please check your internet connection.");
        builder.setCancelable(false);
        if (Settings.canDrawOverlays(this)) {
            networkDialog = builder.create();
            networkDialog.getWindow().setType(WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY);
            networkDialog.show();
        }
        else {
            Toast.makeText(getApplicationContext(), "Please allow the permission", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION, Uri.parse("package:" + getPackageName()));
            startActivity(intent);
        }

    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (networkDialog != null && networkDialog.isShowing()) {
            networkDialog.cancel();
        }
    }
}