package com.example.project;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationManagerCompat;

import java.sql.Connection;
import java.sql.ResultSet;

/**
 * Background service that periodically checks for updates in a database and notifies the user if there are new updates.
 */
public class BackgroundCheck extends Service {

    // Flag to keep track of whether the service is currently running
    private static boolean isRunning = false;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        // This service is not designed to be bound by components, so it returns null
        return null;
    }

    @Override
    public void onCreate() {
        // Called by the system when the service is first created.
        super.onCreate();
    }

    /**
     * Called by the system every time a client explicitly starts the service by calling startService(Intent),
     * providing the arguments it supplied and a unique integer token representing the start request.
     *
     * @param intent The Intent supplied to startService(Intent), as given.
     * @param flags Additional data about this start request.
     * @param startId A unique integer representing this specific request to start.
     * @return The return value indicates what semantics the system should use for the service's current started state.
     */
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        if (!isRunning) {
            isRunning = true;

            Handler handler = new Handler();
            Runnable hand = new Runnable() {
                @Override
                public void run() {
                    try {
                        // Establishing connection to the database
                        Connection connection = new ConnectionHelper().connectionclass();
                        // Querying the database for the last update timestamp
                        ResultSet result = connection.createStatement().executeQuery("SELECT * FROM LastUpdated");
                        result.next();
                        // Accessing shared preferences to store/fetch the last update timestamp
                        SharedPreferences sharedPreferences = getSharedPreferences("MyPrefs", MODE_PRIVATE);
                        SharedPreferences.Editor editor = sharedPreferences.edit();

                        // Checking if there is a new update in the database compared to what's stored locally
                        if (result.getTimestamp("LastUpdated").getTime() != sharedPreferences.getLong("LastUpdated", 0)) {
                            // Updating the stored timestamp with the new value from the database
                            editor.putLong("LastUpdated", result.getTimestamp("LastUpdated").getTime());
                            editor.apply();
                            // Notifying the user about the availability of new shifts
                            NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
                            notificationManager.notify(1, new Notification.Builder(getApplicationContext(), "1")
                                    .setContentText("Shifts for next week are available")
                                    .setSmallIcon(R.drawable.ic_launcher_foreground)
                                    .build());
                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    // Re-scheduling the runnable to run again after a specified delay
                    handler.postDelayed(this, 1000);
                }
            };
            // Scheduling the first execution of the runnable
            handler.postDelayed(hand,100);
        }
        // Making the service sticky so it continues to run until explicitly stopped
        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        // Called by the system to notify a Service that it is no longer used and is being removed.
        super.onDestroy();
        isRunning = false;
        Log.i("service", "Service stopped");
    }

    /**
     * Checks if the background service is currently running.
     *
     * @return True if the service is running, false otherwise.
     */
    public static boolean isServiceRunning() {
        return isRunning;
    }
}
