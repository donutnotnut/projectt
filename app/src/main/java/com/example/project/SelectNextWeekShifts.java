package com.example.project;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
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

import kotlinx.coroutines.GlobalScope;

public class SelectNextWeekShifts extends AppCompatActivity {
    private ImageView background;
    private CheckBox Monday;
    private CheckBox Tuesday;
    private CheckBox Wednesday;
    private CheckBox Thursday;
    private CheckBox Friday;
    private CheckBox Saturday;
    private CheckBox Sunday;
    private TextView Warning;
    private Button save;
    private RecyclerView recyclerView;
    private BottomNavigationView bottomNavigationView;
    private int id =0;
    private ArrayList<CheckBox> array = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
         id = getIntent().getIntExtra("id",0);

        setContentView(R.layout.activity_select_next_week_shifts);

        Animation animation = AnimationUtils.loadAnimation(this, R.anim.frombottomtotop);
         background = findViewById(R.id.imageView3);
         Monday = findViewById(R.id.MondayCheckBoxSelectNextWeek);
         Tuesday = findViewById(R.id.TuesdayCheckBoxSelectNextWeek);
         Wednesday = findViewById(R.id.WednesdayCheckBoxSelectNextWeek);
         Thursday = findViewById(R.id.ThursdayCheckBoxSelectNextWeek);
         Friday = findViewById(R.id.FridayCheckBoxSelectNextWeek);
         Saturday = findViewById(R.id.SaturdayCheckBoxSelectNextWeek);
         Sunday = findViewById(R.id.SundayChekBoxSelectNextWeek);
         Warning = findViewById(R.id.ShitsWarningSelectNextWeek);
         save = findViewById(R.id.button);
         recyclerView = findViewById(R.id.CurrentWeekrRecycler);
         bottomNavigationView = findViewById(R.id.tabslayout);
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

        array.add(Monday);
        array.add(Tuesday);
        array.add(Wednesday);
        array.add(Thursday);
        array.add(Friday);
        array.add(Saturday);
        array.add(Sunday);
        new as().execute();


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
        String name = "";
        try {
            Connection com = new ConnectionHelper().connectionclass();
            PreparedStatement ps = com.prepareStatement("select * from info where ID='"+id+"'");
            ResultSet rs = ps.executeQuery();
            rs.next();
            name = rs.getString("Name");
            com.close();
        }catch (SQLException e) {
            Log.e("error", e.getMessage());
        }

        bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
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
        Connection con = new ConnectionHelper().connectionclass();
        ArrayList<workdayitem> arrayforadapter = new ArrayList<>();

        try {
            ResultSet rs = con.createStatement().executeQuery("select * from currentweek where WorkerID='" + id + "'");

            String[] daysOfWeek = {"Sunday", "Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday"};
            if (rs.next()) {
                for (String day : daysOfWeek) {
                    if (rs.getBoolean(day.toLowerCase())) {
                        ResultSet working = con.createStatement().executeQuery("select * from currentweek where " + day + "=1");
                        ArrayList<String> names = new ArrayList<>();
                        while (working.next()) {
                            ResultSet nameworker = con.createStatement().executeQuery("select * from info where id='" + working.getInt("WorkerID") + "'");
                            nameworker.next();
                            if (!nameworker.getString("Name").equals(name)) {
                                names.add(nameworker.getString("Name"));
                            }
                        }
                        if (names.size() == 0) {
                            names.add("No one");
                        }
                        arrayforadapter.add(new workdayitem(day, (ArrayList<String>) names.clone()));
                    }
                }
                con.close();
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        recyclerView.setLayoutManager(layoutManager);
        AdapterCurrentWeek adapter = new AdapterCurrentWeek(arrayforadapter,this);
        recyclerView.setAdapter(adapter);

    }
    private class as extends android.os.AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... voids) {
            try {
                Connection connection = new ConnectionHelper().connectionclass();
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
                                                "SET Locked = 1, " +
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
                                        connection.close();
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

            }
            catch (Exception e) {
                Log.e("error", e.getMessage());
            }
            return null;
        }
    }
    private void SetterCheckBox(ArrayList<CheckBox> array){
        for(int i=0;i<array.size();i++){
            array.get(i).setEnabled(false);
        }

    }
}