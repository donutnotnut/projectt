package com.example.project;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import com.google.android.material.tabs.TabLayout;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Time;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

public class ShiftHystory extends AppCompatActivity {
    private int id = 0;
    ArrayList<ShiftObject> shiftarray = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shift_hystory);
        id = getIntent().getIntExtra("id", 0);
        View background = findViewById(R.id.BackgroundShiftHistory);
        Animation animation = AnimationUtils.loadAnimation(this, R.anim.frombottomtotop);
        background.startAnimation(animation);
        RecyclerView recyclerView = findViewById(R.id.RecyclerViewShiftHistory);
        recyclerView.startAnimation(animation);
        TabLayout tabLayout=findViewById(R.id.TabLayoutForShiftHistory);
        tabLayout.selectTab(tabLayout.getTabAt(1));
        Connection con = new ConnectionHelper().connectionclass();
        try {
            ResultSet result = con.createStatement().executeQuery("SELECT * FROM shifthistory WHERE WorkerId = '" + id+"'");
            while (result.next()) {
                Timestamp start = result.getTimestamp("StartTime");
                Timestamp end = result.getTimestamp("EndTime");
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MM/dd HH:mm");
                String startString = simpleDateFormat.format(start);
                String endString = simpleDateFormat.format(end);
                shiftarray.add(new ShiftObject(startString, endString, id, result.getInt("ShiftID"), start, end, ""));
            }
        } catch (SQLException e) {
            Log.e("error", e.getMessage());
        }
        ShiftsAdapter shiftsAdapter = new ShiftsAdapter(this, shiftarray);
        LinearLayoutManager llm = new LinearLayoutManager(this);
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(llm);
        recyclerView.setAdapter(shiftsAdapter);
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {

            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                switch (tab.getPosition()) {
                    case 0:
                        Intent intent = new Intent(getApplicationContext(), MainActivity2.class);
                        intent.putExtra("id", id);
                        startActivity(intent);

                        break;
                    case 1:
                        break;
                    case 2:
                        Intent intent2 = new Intent(getApplicationContext(), SelectNextWeekShifts.class);
                        intent2.putExtra("id", id);
                        startActivity(intent2);

                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }
}