package com.example.project;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
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
    private boolean IsShiftActive=false;
    private Timestamp startShift=null;
    private Timestamp endShift=null;
    private double Salary =0;
    private double hours =0;
    private double earned =0;
    private String name = "";
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        SharedPreferences sharedPreferences = getSharedPreferences("MyPrefs", MODE_PRIVATE);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        int id = getIntent().getIntExtra("id",0);
        if (id==0) {
            Log.e("error", "Error in id");
        }
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.showOverflowMenu();
        Animation animation = AnimationUtils.loadAnimation(this, R.anim.frombottomtotop);
        TextView text1= findViewById(R.id.textView2);
        TextView text2= findViewById(R.id.textView6);
        TextView text3= findViewById(R.id.textView7);
        TextView text4= findViewById(R.id.textView8);
        TextView WelcomeText= findViewById(R.id.MainTextShiftHistoryAdmin);
        TextView HoursText = findViewById(R.id.HoursTextMain);
        TextView EarnedText = findViewById(R.id.EarnedTextMain);
        TextView SalaryText = findViewById(R.id.SalaryTextMain);
        TextView NextWorkday = findViewById(R.id.NextWorkdayTextMain);
        Button StartShift = findViewById(R.id.StartShiftButtonMain);
        Button CustomPunchIn = findViewById(R.id.CustomShiftButtonMain);
        BottomNavigationView tabs=findViewById(R.id.tabslayout);
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
        //initial setup for texts

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

            connection.close();
        } catch (SQLException e) {
            Log.e("error with server", e.getMessage());
        }


        // time for main menu functional
        Connection con = new ConnectionHelper().connectionclass();
        try {
            String q="SELECT * from info WHERE ID=?";
            PreparedStatement preps = con.prepareStatement(q);
            preps.setInt(1, id);
            ResultSet result = preps.executeQuery();
            result.next();
            Salary = result.getDouble("Salary");
            WelcomeText.setText("Welcome, "+result.getString("Name"));
            name = result.getString("Name")+"'s "+result.getString("Surname")+" information:";
            hours =0;
            String query = "SELECT * FROM shifthistory WHERE WorkerID = ? AND StartTime >= ?";
            LocalDate firstDayOfMonth = LocalDate.now().withDayOfMonth(1);
            PreparedStatement preparedStatement = con.prepareStatement(query);
            preparedStatement.setInt(1, id);
            preparedStatement.setDate(2, java.sql.Date.valueOf(firstDayOfMonth.toString()));
            ResultSet res = preparedStatement.executeQuery();
            while (res.next()){
                Timestamp start=res.getTimestamp("StartTime");
                Timestamp end = res.getTimestamp("EndTime");
                long timeDifference = end.getTime() - start.getTime();
                hours += (double) timeDifference / (1000 * 60 * 60);
            }
            con.close();
            DecimalFormat decimalFormat = new DecimalFormat("#.0");
            String formattedValue = decimalFormat.format(hours);
            hours=Double.parseDouble(formattedValue);
            HoursText.setText(hours+" hours");
            earned=hours*Salary;
            SalaryText.setText(Salary+"₪");
            formattedValue = decimalFormat.format(earned);
            earned=Double.parseDouble(formattedValue);
            EarnedText.setText(earned+"₪");
        }catch (SQLException e){
            Log.e("error while counting hours", e.getMessage());
        }
        //button punch in functional
        StartShift.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!IsShiftActive) {
                    startShift =new Timestamp(System.currentTimeMillis());
                    IsShiftActive = true;
                    v.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#fa737c")));
                    Button btn = (Button)v;
                    btn.setText("End Shift");
                }
                else {
                    IsShiftActive=false;
                    endShift=new Timestamp(System.currentTimeMillis());
                    v.setBackgroundTintList(ContextCompat.getColorStateList(MainActivity2.this, R.color.accent));
                    Button btn = (Button)v;
                    btn.setText("Start Shift");
                    ConnectionHelper connectionHelper= new ConnectionHelper();
                    Connection connection=connectionHelper.connectionclass();
                    String insertSQL = "INSERT INTO shifthistory (WorkerID, StartTime, EndTime) VALUES (?, ?, ?)";
                    try (PreparedStatement preparedStatement = connection.prepareStatement(insertSQL)) {
                        preparedStatement.setInt(1, id);
                        preparedStatement.setTimestamp(2, Timestamp.valueOf(startShift.toString()));
                        preparedStatement.setTimestamp(3, Timestamp.valueOf(endShift.toString()));
                        preparedStatement.executeUpdate();
                        connection.close();
                    } catch (SQLException e) {
                        Log.e("error while pushing", e.getMessage());

                    }
                }
            }
        });
        CustomPunchIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent= new Intent(getApplicationContext(), com.example.project.CustomPunchIn.class);
                intent.putExtra("id", id);
                startActivity(intent);
            }
        });
        tabs.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                if (item.getItemId()==R.id.HistoryItem){
                    Intent intent = new Intent(getApplicationContext(), ShiftHystory.class);
                    intent.putExtra("id", id);
                    startActivity(intent);
                }
                if (item.getItemId()==R.id.NextWeekShiftTem) {
                    Intent intent = new Intent(getApplicationContext(), SelectNextWeekShifts.class);
                    intent.putExtra("id", id);
                    startActivity(intent);
                }
                return false;
            }
        });
        if (sharedPreferences.getString("email",null)==null) {
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

            } catch (SQLException e) {
                Log.e("error with server", e.getMessage());
            }
        }

    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menutop, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        if (item.getItemId() == R.id.Logout){
            SharedPreferences sharedPreferences = getSharedPreferences("MyPrefs", MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.clear();
            editor.apply();
            Intent intent = new Intent(this, MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            finish();
            return super.onOptionsItemSelected(item);
        }
        else{
            String string = name+"\nHours worked: "+hours+" hours"+"\nEarnings: "+earned+"₪"+"\nSalary: "+Salary+"₪/h";
            Intent intent = new Intent(Intent.ACTION_SEND);
            intent.setType("text/plain");
            intent.putExtra(Intent.EXTRA_TEXT, string);
            startActivity(Intent.createChooser(intent, "Share via"));
            return super.onOptionsItemSelected(item);
        }

    }
}