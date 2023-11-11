package com.example.project;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

public class ShiftsHistoryAdmin extends AppCompatActivity {
    ArrayList<ShiftObject> array = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.shift_hystory_admin);
        RecyclerView recyclerView = findViewById(R.id.RecyclerViewShiftHistoryAdmin);
        Animation animation = AnimationUtils.loadAnimation(this,R.anim.frombottomtotop);
        recyclerView.startAnimation(animation);
        Connection connection = new ConnectionHelper().connectionclass();
        try {
            ResultSet result = connection.createStatement().executeQuery("select * from shifthistory");
            while (result.next()){
                ResultSet names = connection.createStatement().executeQuery("select * from info where id = '"+result.getInt("WorkerId")+"'");
                names.next();
                Timestamp start = result.getTimestamp("StartTime");
                Timestamp end = result.getTimestamp("EndTime");
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MM/dd HH:mm");
                String startString = simpleDateFormat.format(start);
                String endString = simpleDateFormat.format(end);
                array.add(new ShiftObject(startString, endString, result.getInt("WorkerId"), result.getInt("ShiftID"), start, end, names.getString("Name")+ " "+names.getString("Surname")));
                connection.close();
            }
        }
        catch (Exception e){
            Log.e("Error", e.getMessage());
        }
        ShiftsAdapter shiftsAdapter = new ShiftsAdapter(this, array);
        LinearLayoutManager llm = new LinearLayoutManager(this);
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(llm);
        recyclerView.setAdapter(shiftsAdapter);
    }
}