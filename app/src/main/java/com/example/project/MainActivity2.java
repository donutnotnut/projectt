package com.example.project;

import static android.text.format.DateUtils.formatElapsedTime;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.core.content.ContextCompat;

import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.RemoteViews;
import android.widget.TextView;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.DecimalFormat;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.ArrayList;

public class MainActivity2 extends AppCompatActivity {
    private Runnable updateNotificationRunnable;
    private boolean IsShiftActive = false;
    private Timestamp startShift = null;
    private Timestamp endShift = null;
    private double Salary = 0;
    private double hours = 0;
    private double earned = 0;
    private String name = "";



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        SharedPreferences sharedPreferences = getSharedPreferences("MyPrefs", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        if (sharedPreferences.getLong("LastUpdated", 0) == 0) {
            @SuppressLint("StaticFieldLeak") AsyncTask asyncTask = new AsyncTask() {
                @Override
                protected Object doInBackground(Object[] objects) {
                    try {
                        Connection connection = new ConnectionHelper().connectionclass();
                        if (connection != null) {
                            ResultSet resultSet = connection.createStatement().executeQuery("SELECT * FROM LastUpdated");
                            resultSet.next();
                            Timestamp timestamp = resultSet.getTimestamp("LastUpdated");
                            editor.putLong("LastUpdated", timestamp.getTime());
                        }
                    } catch (Exception e) {
                        Log.e("error", e.getMessage());
                    }
                    return null;
                }
            };
            asyncTask.execute();
        }
        Intent intent = new Intent(this, BackgroundCheck.class);
        if (!BackgroundCheck.isServiceRunning()) {
            startService(intent);
        }


        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        SharedPreferences sharedPreferences3 = getSharedPreferences("MyPrefs", MODE_PRIVATE);
        int id = sharedPreferences3.getInt("id", 0);
        if (id == 0) {
            Log.e("error", "id is 0");
            Intent intent1 = new Intent(MainActivity2.this, MainActivity.class);
            startActivity(intent1);
        }
        NotificationManager notificationManager3 = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager3.cancel("SLAY", 1);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.showOverflowMenu();
        Animation animation = AnimationUtils.loadAnimation(this, R.anim.frombottomtotop);
        TextView text1 = findViewById(R.id.textView2);
        TextView text2 = findViewById(R.id.textView6);
        TextView text3 = findViewById(R.id.textView7);
        TextView text4 = findViewById(R.id.textView8);
        TextView WelcomeText = findViewById(R.id.MainTextShiftHistoryAdmin);
        TextView HoursText = findViewById(R.id.HoursTextMain);
        TextView EarnedText = findViewById(R.id.EarnedTextMain);
        TextView SalaryText = findViewById(R.id.SalaryTextMain);
        TextView NextWorkday = findViewById(R.id.NextWorkdayTextMain);
        Button StartShift = findViewById(R.id.StartShiftButtonMain);
        Button CustomPunchIn = findViewById(R.id.CustomShiftButtonMain);
        BottomNavigationView tabs = findViewById(R.id.tabslayout);
        tabs.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {

            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                item.setChecked(true);
                return true;
            }
        });
        tabs.setSelectedItemId(0);
        text1.startAnimation(animation);
        text2.startAnimation(animation);
        text3.startAnimation(animation);
        text4.startAnimation(animation);
        HoursText.startAnimation(animation);
        EarnedText.startAnimation(animation);
        SalaryText.startAnimation(animation);
        NextWorkday.startAnimation(animation);
        StartShift.startAnimation(animation);
        CustomPunchIn.startAnimation(animation);
        Connection connection = new ConnectionHelper().connectionclass();
        SharedPreferences sharedPreferences2 = getSharedPreferences("MyPrefs", MODE_PRIVATE);

        //initial setup for texts
        @SuppressLint("StaticFieldLeak") AsyncTask asyncTask = new AsyncTask() {

            @Override
            protected Object doInBackground(Object[] objects) {
                try {
                    PreparedStatement preparedStatement = connection.prepareStatement("SELECT * from currentweek Where workerid = ?");
                    preparedStatement.setInt(1, id);
                    ResultSet alldaysofwork = preparedStatement.executeQuery();

                    if (alldaysofwork.next()) {
                        ArrayList<Boolean> days = new ArrayList<>();
                        days.add(alldaysofwork.getBoolean("Monday"));
                        days.add(alldaysofwork.getBoolean("Tuesday"));
                        days.add(alldaysofwork.getBoolean("Wednesday"));
                        days.add(alldaysofwork.getBoolean("Thursday"));
                        days.add(alldaysofwork.getBoolean("Friday"));
                        days.add(alldaysofwork.getBoolean("Saturday"));
                        days.add(alldaysofwork.getBoolean("Sunday"));

                        int currentDayOfWeek = LocalDate.now().getDayOfWeek().getValue();

                        Log.e("day", "Current Day of Week: " + currentDayOfWeek);

                        for (int i = 0; i < 7; i++) {
                            int nextDayIndex = (currentDayOfWeek + i) % 7;
                            if (days.get(nextDayIndex)) {
                                String dayName = DayOfWeek.of(nextDayIndex + 1).name().substring(0, 1).toUpperCase() +
                                        DayOfWeek.of(nextDayIndex + 1).name().substring(1).toLowerCase();
                                NextWorkday.setText(dayName);
                                Log.e("day", "Found next workday: " + dayName);
                                break;
                            } else if (i == 6) {
                                NextWorkday.setText("No more shifts this week");
                                Log.e("day", "No more shifts this week");
                            }
                        }
                    } else {
                        NextWorkday.setText("No shifts found for the given worker ID");
                        Log.e("day", "No shifts found for the given worker ID");
                    }

                } catch (Exception e) {
                    Log.e("error with server", e.getMessage());
                }
                // time for main menu functional
                Connection con = new ConnectionHelper().connectionclass();
                try {
                    String q = "SELECT * from info WHERE ID=?";
                    PreparedStatement preps = con.prepareStatement(q);
                    preps.setInt(1, id);
                    ResultSet result = preps.executeQuery();
                    result.next();
                    Salary = result.getDouble("Salary");
                    name = result.getString("Name");
                    hours = 0;
                    String query = "SELECT * FROM shifthistory WHERE WorkerID = ? AND StartTime >= ?";
                    LocalDate firstDayOfMonth = LocalDate.now().withDayOfMonth(1);
                    PreparedStatement preparedStatement = con.prepareStatement(query);
                    preparedStatement.setInt(1, id);
                    preparedStatement.setDate(2, java.sql.Date.valueOf(firstDayOfMonth.toString()));
                    ResultSet res = preparedStatement.executeQuery();
                    while (res.next()) {
                        Timestamp start = res.getTimestamp("StartTime");
                        Timestamp end = res.getTimestamp("EndTime");
                        long timeDifference = end.getTime() - start.getTime();
                        hours += (double) timeDifference / (1000 * 60 * 60);
                    }
                    DecimalFormat decimalFormat = new DecimalFormat("#.0");
                    String formattedValue = decimalFormat.format(hours);
                    hours = Double.parseDouble(formattedValue);
                    earned = hours * Salary;
                    name=result.getString("Name");
                    formattedValue = decimalFormat.format(earned);
                    earned = Double.parseDouble(formattedValue);
                } catch (Exception e) {
                    Log.e("error while counting hours", e.getMessage());
                }
                return null;
            }
            @Override
            protected void onPostExecute(Object o) {
                    WelcomeText.setText("Welcome, " + name);
                    SalaryText.setText(Salary + "₪");
                    EarnedText.setText(earned + "₪");
                    HoursText.setText(hours + " hours");
                    Log.i("hours", String.valueOf(hours));
                    Log.i("earned", String.valueOf(earned));
                    Log.i("salary", String.valueOf(Salary));
                    Log.i("name", String.valueOf(name));
            }
        };
        asyncTask.execute();

        //notif
        NotificationCompat.Builder notifBuilder = new NotificationCompat.Builder(MainActivity2.this, "CHANNEL_ID");
        RemoteViews remoteViews = new RemoteViews(getPackageName(), R.layout.notification);
        notifBuilder.setCustomContentView(remoteViews);
        notifBuilder.setStyle(new NotificationCompat.DecoratedCustomViewStyle());
        notifBuilder.setColorized(true);
        notifBuilder.setOngoing(true);
        notifBuilder.setSilent(true);
        notifBuilder.setColor(ContextCompat.getColor(MainActivity2.this, R.color.accent_dark));
        notifBuilder.setSmallIcon(R.drawable.ic_launcher_foreground);
        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(MainActivity2.this);
        notificationManager.createNotificationChannel(new NotificationChannel("CHANNEL_ID", "CHANNEL_NAME", NotificationManager.IMPORTANCE_HIGH));
        Handler handler = new Handler(Looper.getMainLooper());
        //button punch in functional
        StartShift.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!IsShiftActive) {
                    if (startShift==null) {
                        startShift = new Timestamp(System.currentTimeMillis());
                        SharedPreferences sharedPreferences = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.putLong("startShift", startShift.getTime());
                        editor.apply();
                        Log.e("log","sucesfully set");
                        updateNotificationRunnable = new Runnable() {
                            @Override
                            public void run() {
                                // Update the notification's RemoteViews
                                updateNotification(remoteViews, notificationManager, notifBuilder);
                                if (ActivityCompat.checkSelfPermission(MainActivity2.this, android.Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
                                    ActivityCompat.requestPermissions(MainActivity2.this, new String[]{android.Manifest.permission.POST_NOTIFICATIONS}, 1);
                                    return;
                                }

                                handler.postDelayed(this, 1000);
                            }
                        };
                        handler.post(updateNotificationRunnable);
                    }
                    IsShiftActive = true;
                    v.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#fa737c")));
                    Button btn = (Button) v;
                    btn.setText("End Shift");

                } else {
                    SharedPreferences sharedPreferences = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putLong("startShift", 0);
                    editor.apply();
                    Log.e("log","sucesfully deleted");
                    Log.e("log32",sharedPreferences.getLong("startShift",0)+"");
                    IsShiftActive = false;
                    endShift = new Timestamp(System.currentTimeMillis());
                    v.setBackgroundTintList(ContextCompat.getColorStateList(MainActivity2.this, R.color.accent));
                    Button btn = (Button) v;
                    btn.setText("Start Shift");
                    ConnectionHelper connectionHelper = new ConnectionHelper();
                    Connection connection = connectionHelper.connectionclass();
                    String insertSQL = "INSERT INTO shifthistory (WorkerID, StartTime, EndTime) VALUES (?, ?, ?)";
                    try (PreparedStatement preparedStatement = connection.prepareStatement(insertSQL)) {
                        preparedStatement.setInt(1, id);
                        preparedStatement.setTimestamp(2, Timestamp.valueOf(startShift.toString()));
                        preparedStatement.setTimestamp(3, Timestamp.valueOf(endShift.toString()));
                        preparedStatement.executeUpdate();
                    } catch (Exception e) {
                        Log.e("error while pushing", e.getMessage());

                    }
                    startShift=null;
                    NotificationManagerCompat notificationManager = NotificationManagerCompat.from(MainActivity2.this);
                    notificationManager.cancel(1);
                    handler.removeCallbacks(updateNotificationRunnable);
                }
            }
        });
        CustomPunchIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), com.example.project.CustomPunchIn.class);
                intent.putExtra("id", id);
                startActivity(intent);
            }
        });
        tabs.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                if (item.getItemId() == R.id.HistoryItem) {
                    Intent intent = new Intent(getApplicationContext(), ShiftHystory.class);
                    intent.putExtra("id", id);
                    startActivity(intent);
                }
                if (item.getItemId() == R.id.NextWeekShiftTem) {
                    Intent intent = new Intent(getApplicationContext(), SelectNextWeekShifts.class);
                    intent.putExtra("id", id);
                    startActivity(intent);
                }
                return false;
            }
        });
        Log.e("email", sharedPreferences.getString("email", null) + "");
        if (sharedPreferences.getString("email", null) == null) {
            Connection connectionclass = new ConnectionHelper().connectionclass();
            try {

                ResultSet result = connectionclass.createStatement().executeQuery("SELECT * FROM info WHERE ID = " + id);
                result.next();

                String email = result.getString("Email");
                String password = result.getString("Password");
                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity2.this);
                builder.setTitle("Saving Password");
                builder.setMessage("Do you want to save your password?");
                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        SharedPreferences sharedPreferences = getSharedPreferences("MyPrefs", MODE_PRIVATE);
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.putString("email", email);
                        editor.putString("password", password);
                        editor.apply();
                    }
                });
                builder.setNegativeButton("No", null);
                builder.show();

            } catch (Exception e) {
                Log.e("error with server", e.getMessage());
            }
        }
        if (sharedPreferences2.getLong("startShift",0)!=0){
            startShift = new Timestamp(sharedPreferences2.getLong("startShift",0));
            StartShift.performClick();
        }

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menutop, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        if (item.getItemId() == R.id.Logout) {
            SharedPreferences sharedPreferences = getSharedPreferences("MyPrefs", MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.clear();
            editor.apply();
            Intent intent = new Intent(this, MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            finish();
            return super.onOptionsItemSelected(item);
        } else {
            String string = name+"'s information"+"\n" + "\nHours worked: " + hours + " hours" + "\nEarnings: " + earned + "₪" + "\nSalary: " + Salary + "₪/h";
            Intent intent = new Intent(Intent.ACTION_SEND);
            intent.setType("text/plain");
            intent.putExtra(Intent.EXTRA_TEXT, string);
            startActivity(Intent.createChooser(intent, "Share via"));
            return super.onOptionsItemSelected(item);
        }

    }

    private void updateNotification(RemoteViews remoteViews, NotificationManagerCompat notificationManager, NotificationCompat.Builder notifBuilder) {
        long elapsedTime = System.currentTimeMillis() - startShift.getTime();
        String formattedTime = formatElapsedTime(elapsedTime);
        remoteViews.setTextViewText(R.id.timertext, formattedTime);

        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        notificationManager.notify(1, notifBuilder.build());
    }
    private String formatElapsedTime(long elapsedTime) {
        long seconds = (elapsedTime / 1000) % 60;
        long minutes = (elapsedTime / (1000 * 60)) % 60;
        long hours = elapsedTime / (1000 * 60 * 60);

        // Format the result as HH:MM:SS
        return String.format("%02d:%02d:%02d", hours, minutes, seconds);
    }

}
