package com.example.project;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.material.snackbar.Snackbar;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.time.LocalDate;
import java.util.ArrayList;

public class NextWeekAdmin extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_next_week_admin);

        // Initialize RecyclerView and button
        RecyclerView recyclerView = findViewById(R.id.RecyclerAdminPageShiftNext);
        recyclerView.startAnimation(AnimationUtils.loadAnimation(this, R.anim.frombottomtotop));
        Button button = findViewById(R.id.AcceptTheChanges);

        // Create an ArrayList to hold NextWeekScheduleItem objects
        ArrayList<NextWeekScheduleItem> array= new ArrayList<>();

        // Establish database connection
        Connection connection = new ConnectionHelper().connectionclass();

        try {
            // Retrieve data from the NextWeek table and populate the ArrayList
            PreparedStatement getIdWithWorkdays= connection.prepareStatement("SELECT * FROM NextWeek");
            ResultSet resultSet = getIdWithWorkdays.executeQuery();
            while (resultSet.next()){
                ResultSet namegetter= connection.prepareStatement("SELECT * FROM info WHERE id='"+resultSet.getInt("WorkerID")+"'").executeQuery();
                namegetter.next();
                array.add(new NextWeekScheduleItem(namegetter.getInt("ID"),namegetter.getString("Name"),resultSet.getBoolean("Sunday"),resultSet.getBoolean("Monday"),resultSet.getBoolean("Tuesday"),resultSet.getBoolean("Wednesday"),resultSet.getBoolean("Thursday"),resultSet.getBoolean("Friday"),resultSet.getBoolean("Saturday")));
            }
        }
        catch (Exception e) {
            Log.e("error 1",e.getMessage());
        }

        // Create adapter and set it to the RecyclerView
        AdapterAdminNextWeek adapter = new AdapterAdminNextWeek(array,this);
        LinearLayoutManager llm = new LinearLayoutManager(this);
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(llm);
        recyclerView.setAdapter(adapter);

        // Set button click listener for accepting changes
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Connection connection1 = new ConnectionHelper().connectionclass();
                String updateQuery = "UPDATE currentweek SET Sunday=?, Monday=?, Tuesday=?, Wednesday=?, Thursday=?, Friday=?, Saturday=? WHERE WorkerID=?";

                try (PreparedStatement preparedStatement = connection1.prepareStatement(updateQuery)) {
                    for (int i = 0; i < array.size(); i++) {
                        preparedStatement.setBoolean(1, array.get(i).getSunday()); // Sunday
                        preparedStatement.setBoolean(2, array.get(i).getMonday()); // Monday
                        preparedStatement.setBoolean(3, array.get(i).getTuesday()); // Tuesday
                        preparedStatement.setBoolean(4, array.get(i).getWednesday()); // Wednesday
                        preparedStatement.setBoolean(5, array.get(i).getThursday()); // Thursday
                        preparedStatement.setBoolean(6, array.get(i).getFriday()); // Friday
                        preparedStatement.setBoolean(7, array.get(i).getSaturday()); // Saturday
                        preparedStatement.setInt(8,array.get(i).getId());

                        // Execute the update statement
                        preparedStatement.executeUpdate();
                    }
                    String updateQuery2 = "UPDATE NextWeek " +
                            "SET Sunday = 0, " +
                            "    Monday = 0, " +
                            "    Tuesday = 0, " +
                            "    Wednesday = 0, " +
                            "    Thursday = 0, " +
                            "    Friday = 0, " +
                            "    Saturday = 0, " +
                            "    Locked = 0";
                    PreparedStatement preparedStatement2 = connection1.prepareStatement(updateQuery2);
                    preparedStatement2.executeUpdate();
                    long timestampMillis = System.currentTimeMillis(); // Your timestamp
                    java.sql.Timestamp timestamp = new java.sql.Timestamp(timestampMillis);
                    String formattedDateTime = new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(timestamp);
                    connection1.prepareStatement("UPDATE LastUpdated SET LastUpdated = '"+ timestamp+"'").executeUpdate();
                    Snackbar.make(v, "Changes Accepted", Snackbar.LENGTH_SHORT).show();
                }
                catch (Exception e){
                    Log.e("error 2",e.getMessage());
                }
            }
        });

    }
}
