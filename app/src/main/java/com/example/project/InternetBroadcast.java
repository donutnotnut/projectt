package com.example.project;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

public class InternetBroadcast extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        if (ConnectivityManager.CONNECTIVITY_ACTION.equals(intent.getAction())) {
            Intent serviceIntent = new Intent(context, InternetChecker.class);
            boolean isInternetAvailable = isNetworkAvailable(context);
            // Pass the internet availability status to the service
            serviceIntent.putExtra("internetAvailable", isInternetAvailable);
            if (!isInternetAvailable) {
                context.startService(serviceIntent);
            } else {
                context.startService(serviceIntent); // Use startService() to restart
            }
        }
    }

    private boolean isNetworkAvailable(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }
}