package com.example.project;

import static android.text.format.DateUtils.formatElapsedTime;

import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.util.Log;
import android.widget.RemoteViews;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.core.content.ContextCompat;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.Timestamp;

public class MyNfcService extends Service {
    static Timestamp start = null;
    private final Handler handler = new Handler(Looper.getMainLooper());
    private Runnable updateNotificationRunnable;

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        SharedPreferences prefs = getSharedPreferences("MyPrefs", MODE_PRIVATE);
        long startTime = prefs.getLong("startShift", 0);

        if (startTime == 0) {
            start = new Timestamp(System.currentTimeMillis());
            prefs.edit().putLong("startShift", start.getTime()).apply();
            showToast("Shift started");

            // Call startForegroundNotification after starting the shift
            startForegroundNotification();
        } else {
            start = new Timestamp(startTime);
            logShift();
            prefs.edit().remove("startShift").apply();

            // Call startForegroundNotification after logging the shift

        }

        return START_STICKY;
    }

    private void showToast(String message) {
        handler.post(() -> Toast.makeText(MyNfcService.this, message, Toast.LENGTH_SHORT).show());
    }

    private void startForegroundNotification() {
        NotificationCompat.Builder notifBuilder = new NotificationCompat.Builder(this, "CHANNEL_ID");
        RemoteViews remoteViews = new RemoteViews(getPackageName(), R.layout.notification);
        notifBuilder.setCustomContentView(remoteViews);
        notifBuilder.setStyle(new NotificationCompat.DecoratedCustomViewStyle());
        notifBuilder.setColorized(true);
        notifBuilder.setOngoing(true);
        notifBuilder.setSilent(true);
        notifBuilder.setColor(ContextCompat.getColor(this, R.color.accent_dark));
        notifBuilder.setSmallIcon(R.drawable.ic_launcher_foreground);
        createNotificationChannel();

        startForeground(1, notifBuilder.build());

        updateNotificationPeriodically(remoteViews, notifBuilder);
    }

    private void updateNotificationPeriodically(RemoteViews remoteViews, NotificationCompat.Builder notifBuilder) {
        updateNotificationRunnable = new Runnable() {
            @Override
            public void run() {
                if (start == null) {
                    return;
                }
                long elapsedTime = System.currentTimeMillis() - start.getTime();
                String formattedTime = formatElapsedTime(elapsedTime / 1000);
                remoteViews.setTextViewText(R.id.timertext, formattedTime);
                if (ActivityCompat.checkSelfPermission(MyNfcService.this, android.Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
                    return;
                }
                NotificationManagerCompat.from(MyNfcService.this).notify(1, notifBuilder.build());
                handler.postDelayed(this, 1000);
            }
        };
        handler.post(updateNotificationRunnable);
    }

    @SuppressLint("StaticFieldLeak")
    private void logShift() {
        Timestamp end = new Timestamp(System.currentTimeMillis());
        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... voids) {
                try {
                    SharedPreferences prefs = getSharedPreferences("MyPrefs", MODE_PRIVATE);
                    int workerID = prefs.getInt("id", 0);
                    if (workerID == 0) {
                        showToast("Please login first");
                        return null;
                    }
                    Connection connection = ConnectionHelper.connectionclass();
                    if (connection != null) {
                        String insertSQL = "INSERT INTO shifthistory (WorkerID, StartTime, EndTime) VALUES (?, ?, ?)";
                        PreparedStatement preparedStatement = connection.prepareStatement(insertSQL);
                        preparedStatement.setInt(1, workerID);
                        preparedStatement.setTimestamp(2, start);
                        preparedStatement.setTimestamp(3, end);
                        preparedStatement.executeUpdate();
                        Log.i("inserted", "Shift data inserted successfully.");
                        start = null;

                    }
                } catch (Exception e) {
                    Log.e("error while pushing", e.getMessage());
                }
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                showToast("Shift ended");
            }
        }.execute();
        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);
        handler.removeCallbacks(updateNotificationRunnable);
        stopSelf();
        notificationManager.cancel(1);
    }

    private void createNotificationChannel() {
        int importance = NotificationManager.IMPORTANCE_DEFAULT;
        NotificationChannel channel = new NotificationChannel("CHANNEL_ID", "CHANNEL_NAME", importance);
        NotificationManager notificationManager = getSystemService(NotificationManager.class);
        notificationManager.createNotificationChannel(channel);
    }

    @Override
    public void onDestroy() {
        handler.removeCallbacksAndMessages(null);
        super.onDestroy();
    }
}
