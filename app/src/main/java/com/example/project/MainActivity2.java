package com.example.project;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
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
import java.time.LocalDate;

public class MainActivity2 extends AppCompatActivity {
    private boolean IsShiftActive=false;
    private Timestamp startShift=null;
    private Timestamp endShift=null;
    private double Salary =0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        int id = getIntent().getIntExtra("id",0);
        if (id==0) {
            Log.e("error", "Error in id");
        }
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

        //initial setup for texts
        try {
            ConnectionHelper connectionHelper = new ConnectionHelper();
            Connection connection = connectionHelper.connectionclass();
            ResultSet result = connection.createStatement().executeQuery("SELECT * FROM info WHERE ID='"+id+"'");
            result.next();
            Salary=result.getDouble("Salary");
            WelcomeText.setText("Welcome, "+result.getString("Name"));
            SalaryText.setText(String.valueOf(result.getDouble("Salary"))+"₪/h");
            connection.close();
        } catch (SQLException e) {
            Log.e("error with server", e.getMessage());
        }
        // time for main menu functional
        Connection con = new ConnectionHelper().connectionclass();
        try {
            double hours =0;
            String query = "SELECT * FROM shifthistory WHERE WorkerID = ? AND StartTime >= ?";
            LocalDate firstDayOfMonth = LocalDate.now().withDayOfMonth(1);
            PreparedStatement preparedStatement = con.prepareStatement(query);
            preparedStatement.setInt(1, id);
            preparedStatement.setDate(2, java.sql.Date.valueOf(firstDayOfMonth.toString()));
            ResultSet result = preparedStatement.executeQuery();
            while (result.next()){
                Timestamp start=result.getTimestamp("StartTime");
                Timestamp end = result.getTimestamp("EndTime");
                long timeDifference = end.getTime() - start.getTime();
                hours += (double) timeDifference / (1000 * 60 * 60);
            }
            con.close();
            DecimalFormat decimalFormat = new DecimalFormat("#.0");
            String formattedValue = decimalFormat.format(hours);
            hours=Double.parseDouble(formattedValue);
            HoursText.setText(hours+" hours");
            double earned=hours*Salary;
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

    }
}