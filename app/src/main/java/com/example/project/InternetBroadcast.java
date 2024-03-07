package com.example.project;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/**
 * Broadcast receiver to monitor internet connectivity changes.
 */
public class InternetBroadcast extends BroadcastReceiver {

    /**
     * Method triggered when network connectivity changes.
     * @param context The application context.
     * @param intent The intent indicating the change in network connectivity.
     */
    @Override
    public void onReceive(Context context, Intent intent) {
        if (ConnectivityManager.CONNECTIVITY_ACTION.equals(intent.getAction())) {
            Intent serviceIntent = new Intent(context, InternetChecker.class);
            if (!isNetworkAvailable(context)) {
                context.startService(serviceIntent);
            } else {
                context.stopService(serviceIntent);
            }
        }
    }

    /**
     * Checks if network connectivity is available.
     * @param context The application context.
     * @return True if network connectivity is available, false otherwise.
     */
    private boolean isNetworkAvailable(Context context) {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }
}
