package com.example.project;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class SelectNextWeekShifts extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        int id = getIntent().getIntExtra("id",0);

        setContentView(R.layout.activity_select_next_week_shifts);
        ImageView background = findViewById(R.id.imageView3);
        CheckBox Monday = findViewById(R.id.MondayCheckBoxSelectNextWeek);
        CheckBox Tuesday = findViewById(R.id.TuesdayCheckBoxSelectNextWeek);
        CheckBox Wednesday = findViewById(R.id.WednesdayCheckBoxSelectNextWeek);
        CheckBox Thursday = findViewById(R.id.ThursdayCheckBoxSelectNextWeek);
        CheckBox Friday = findViewById(R.id.FridayCheckBoxSelectNextWeek);
        CheckBox Saturday = findViewById(R.id.SaturdayCheckBoxSelectNextWeek);
        CheckBox Sunday = findViewById(R.id.SundayChekBoxSelectNextWeek);
        TextView Warning = findViewById(R.id.ShitsWarningSelectNextWeek);
        Button save= findViewById(R.id.button);
        Button check= findViewById(R.id.button2);
        BottomNavigationView bottomNavigationView = findViewById(R.id.tabslayout);

        Animation animation = AnimationUtils.loadAnimation(this, R.anim.frombottomtotop);
        Warning.startAnimation(animation);
        background.startAnimation(animation);
        Monday.startAnimation(animation);
        Tuesday.startAnimation(animation);
        Wednesday.startAnimation(animation);
        Thursday.startAnimation(animation);
        Friday.startAnimation(animation);
        Saturday.startAnimation(animation);
        Sunday.startAnimation(animation);
        save.startAnimation(animation);
        check.startAnimation(animation);
        Connection connection = new ConnectionHelper().connectionclass();
        ArrayList<CheckBox> array = new ArrayList<>();
        array.add(Monday);
        array.add(Tuesday);
        array.add(Wednesday);
        array.add(Thursday);
        array.add(Friday);
        array.add(Saturday);
        array.add(Sunday);

        bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {

            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                item.setChecked(true);
                return true;
            }
        });
        bottomNavigationView.setSelectedItemId(R.id.NextWeekShiftTem);
        if (id==0) {
            Log.e("error", "Error in id");
        }
        try {
            PreparedStatement ps = connection.prepareStatement("select * from NextWeek where WorkerID='"+id+"'");
            ResultSet rs = ps.executeQuery();
            rs.next();
            if (rs.getBoolean("Locked")) {
                SetterCheckBox(array);
                Warning.setVisibility(View.VISIBLE);
            }
            else {
                save.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        AlertDialog builder = new AlertDialog.Builder(SelectNextWeekShifts.this)
                                .setTitle("Warning")
                                .setMessage("You wont be able to change the days afterwards, are you sure?")
                                .setPositiveButton("Yes", null)
                                .setNegativeButton("No", null)
                                .create();
                        builder.setButton(AlertDialog.BUTTON_POSITIVE, "Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                try {
                                    PreparedStatement preparedStatement = connection.prepareStatement("UPDATE NextWeek " +
                                            "SET locked = 1, " +
                                            "sunday = ?, " +
                                            "monday = ?, " +
                                            "tuesday = ?, " +
                                            "wednesday = ?, " +
                                            "thursday = ?, " +
                                            "friday = ?, " +
                                            "saturday = ? " +
                                            "WHERE WorkerID = ?");
                                    preparedStatement.setBoolean(1, Sunday.isChecked());
                                    preparedStatement.setBoolean(2, Monday.isChecked());
                                    preparedStatement.setBoolean(3, Tuesday.isChecked());
                                    preparedStatement.setBoolean(4, Wednesday.isChecked());
                                    preparedStatement.setBoolean(5, Thursday.isChecked());
                                    preparedStatement.setBoolean(6, Friday.isChecked());
                                    preparedStatement.setBoolean(7, Saturday.isChecked());
                                    preparedStatement.setInt(8, id);
                                    preparedStatement.executeUpdate();
                                    SetterCheckBox(array);
                                    Warning.setVisibility(View.VISIBLE);

                                }
                                catch (SQLException e) {
                                    Log.e("error", e.getMessage());
                                }
                            }
                        });
                        builder.show();

                    }
                });
            }
            connection.close();

        }
        catch (Exception e) {
            Log.e("error", e.getMessage());
        }
        check.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //finish this
            }
        });
        bottomNavigationView.setOnItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                if (item.getItemId()==R.id.HistoryItem){
                    Intent intent = new Intent(getApplicationContext(), ShiftHystory.class);
                    intent.putExtra("id", id);
                    startActivity(intent);
                }
                if (item.getItemId()==R.id.HomeItem) {
                    Intent intent = new Intent(getApplicationContext(), MainActivity2.class);
                    intent.putExtra("id", id);
                    startActivity(intent);
                }
                return false;
            }
        });

    }
    private void SetterCheckBox(ArrayList<CheckBox> array){
        for(int i=0;i<array.size();i++){
            array.get(i).setEnabled(false);
        }

    }
}