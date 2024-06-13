package com.example.project;

import android.app.AlertDialog;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.IBinder;
import android.provider.Settings;
import android.view.WindowManager;
import android.widget.Toast;

public class InternetChecker extends Service {

    private AlertDialog networkDialog;
    private boolean isInternetAvailable = false;

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        // Get the boolean value passed from the BroadcastReceiver
        isInternetAvailable = intent.getBooleanExtra("internetAvailable", false);
        if (!isInternetAvailable) {
            showNetworkDialog();
        } else {
            dismissNetworkDialog();
        }
        return START_STICKY;
    }

    /**
     * Displays a dialog to inform the user about the unavailable network connection.
     */
    private void showNetworkDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getApplicationContext());
        builder.setTitle("Network Unavailable");
        builder.setMessage("Please check your internet connection.");
        builder.setCancelable(false);
        if (Settings.canDrawOverlays(this)) {
            networkDialog = builder.create();
            networkDialog.getWindow().setType(WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY);
            networkDialog.show();
        } else {
            Toast.makeText(getApplicationContext(), "Please allow the permission", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION, Uri.parse("package:" + getPackageName()));
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        }
    }

    /**
     * Dismisses the network dialog.
     */
    private void dismissNetworkDialog() {
        if (networkDialog != null && networkDialog.isShowing()) {
            networkDialog.dismiss();
        }
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        dismissNetworkDialog();
    }
}