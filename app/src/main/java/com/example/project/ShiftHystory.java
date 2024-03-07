package com.example.project;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.google.android.material.tabs.TabLayout;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Time;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

public class ShiftHystory extends AppCompatActivity {
    private int id = 0;
    ArrayList<ShiftObject> shiftarray = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shift_hystory);
        id = getIntent().getIntExtra("id", 0);

        // Load animation for RecyclerView
        Animation animation = AnimationUtils.loadAnimation(this, R.anim.frombottomtotop);
        RecyclerView recyclerView = findViewById(R.id.RecyclerViewShiftHistory);
        recyclerView.startAnimation(animation);

        // Set up bottom navigation view
        BottomNavigationView tabLayout=findViewById(R.id.tabslayout);
        tabLayout.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                item.setChecked(true);
                return true;
            }
        });
        tabLayout.setSelectedItemId(R.id.HistoryItem);

        // Connect to the database and fetch shift history
        Connection con = new ConnectionHelper().connectionclass();
        try {
            ResultSet result = con.createStatement().executeQuery("SELECT * FROM shifthistory WHERE WorkerId = '" + id+"'");
            while (result.next()) {
                Calendar currentCalendar = Calendar.getInstance();
                Calendar date = Calendar.getInstance();
                date.setTime(result.getTimestamp("StartTime"));
                if (date.get(Calendar.MONTH) == currentCalendar.get(Calendar.MONTH)) {
                    Timestamp start = result.getTimestamp("StartTime");
                    Timestamp end = result.getTimestamp("EndTime");
                    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MM/dd HH:mm");
                    String startString = simpleDateFormat.format(start);
                    String endString = simpleDateFormat.format(end);
                    // Assuming ShiftObject is defined elsewhere
                    shiftarray.add(new ShiftObject(startString, endString, id, result.getInt("ShiftID"), start, end, ""));
                }
            }
        } catch (Exception e) {
            Log.e("error", e.getMessage());
        }

        // Set up RecyclerView adapter for shift history
        ShiftsAdapter shiftsAdapter = new ShiftsAdapter(this, shiftarray);
        LinearLayoutManager llm = new LinearLayoutManager(this);
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(llm);
        recyclerView.setAdapter(shiftsAdapter);

        // Set bottom navigation actions
        tabLayout.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                if (item.getItemId()==R.id.HomeItem){
                    Intent intent = new Intent(getApplicationContext(), MainActivity2.class);
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
