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
        if (id == 0) {
            Log.e("error while getting id", "no id was given");
        }
        View background = findViewById(R.id.BackgroundShiftHistory);
        Animation animation = AnimationUtils.loadAnimation(this, R.anim.frombottomtotop);
        background.startAnimation(animation);
        RecyclerView recyclerView = findViewById(R.id.RecyclerViewShiftHistory);
        recyclerView.startAnimation(animation);
        Connection con = new ConnectionHelper().connectionclass();
        try {
            ResultSet result = con.createStatement().executeQuery("SELECT * FROM shifthistory WHERE WorkerId = '" + id+"'");
            while (result.next()) {
                Timestamp start = result.getTimestamp("StartTime");
                Timestamp end = result.getTimestamp("EndTime");
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MM/dd HH:mm");
                String startString = simpleDateFormat.format(start);
                String endString = simpleDateFormat.format(end);
                shiftarray.add(new ShiftObject(startString, endString, id, result.getInt("ShiftID")));
            }
        } catch (SQLException e) {
            Log.e("error", e.getMessage());
        }
        ShiftsAdapter shiftsAdapter = new ShiftsAdapter(this, shiftarray);
        LinearLayoutManager llm = new LinearLayoutManager(this);
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(llm);
        recyclerView.setAdapter(shiftsAdapter);
    }
}