package com.example.project;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

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

        // Initialize RecyclerView
        RecyclerView recyclerView = findViewById(R.id.RecyclerViewShiftHistoryAdmin);

        // Set up animation
        Animation animation = AnimationUtils.loadAnimation(this,R.anim.frombottomtotop);
        recyclerView.startAnimation(animation);

        // Establish database connection
        Connection connection = new ConnectionHelper().connectionclass();

        try {
            // Retrieve shift history data from the database
            ResultSet result = connection.createStatement().executeQuery("select * from shifthistory");

            // Process each row of the result set
            while (result.next()){
                // Retrieve worker information associated with the shift
                ResultSet names = connection.createStatement().executeQuery("select * from info where id = '"+result.getInt("WorkerId")+"'");
                if (names.next()) {

                    // Retrieve timestamps for shift start and end times
                    Timestamp start = result.getTimestamp("StartTime");
                    Timestamp end = result.getTimestamp("EndTime");

                    // Format timestamps to string representation
                    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MM/dd HH:mm");
                    String startString = simpleDateFormat.format(start);
                    String endString = simpleDateFormat.format(end);

                    // Create a ShiftObject and add it to the array list
                    array.add(new ShiftObject(startString, endString, result.getInt("WorkerID"), result.getInt("ShiftID"), start, end, names.getString("Name") + " " + names.getString("Surname")));
                    }
                }
        }
        catch (Exception e){
            // Log any errors that occur during database operations
            Log.e("Error", e.getMessage());
            Log.e("Erorr", String.valueOf(e.getCause()));
        }

        // Create and set adapter for RecyclerView
        ShiftsAdapter shiftsAdapter = new ShiftsAdapter(this, array);
        LinearLayoutManager llm = new LinearLayoutManager(this);
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(llm);
        recyclerView.setAdapter(shiftsAdapter);
    }
}
