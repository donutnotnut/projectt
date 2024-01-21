package com.example.project;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.IBinder;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationManagerCompat;

import java.sql.Connection;
import java.sql.ResultSet;

public class BackgroundCheck extends Service {
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
    private static boolean isRunning =false;
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.createNotificationChannel(new NotificationChannel("1", "1", NotificationManager.IMPORTANCE_HIGH));
        Log.i("service", "service started");

        if (!isRunning) {
            isRunning = true;

            Thread thread = new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        Log.i("service", "thread started");

                        while (isRunning) {
                            Connection connection = new ConnectionHelper().connectionclass();
                            ResultSet result = connection.createStatement().executeQuery("SELECT * FROM LastUpdated");
                            result.next();
                            SharedPreferences sharedPreferences = getSharedPreferences("MyPrefs", MODE_PRIVATE);
                            SharedPreferences.Editor editor = sharedPreferences.edit();

                            if (result.getTimestamp("LastUpdated").getTime() != sharedPreferences.getLong("LastUpdated", 0)) {
                                editor.putLong("LastUpdated", result.getTimestamp("LastUpdated").getTime());
                                editor.apply();
                                notificationManager.notify(1, new Notification.Builder(getApplicationContext(), "1").setContentText("Shifts for next week are available").setSmallIcon(R.drawable.ic_launcher_foreground).build());
                            }

                            connection.close();
                            Thread.sleep(300000);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });

            thread.start();
        }

        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        isRunning = false;
        Log.i("service", "service stopped");
    }
    public static boolean isServiceRunning() {
        return isRunning;
    }
}
