package com.example.project;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.android.material.snackbar.Snackbar;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.Timestamp;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

public class Editingshift extends AppCompatActivity {
    LocalDateTime StartLocalDate;
    LocalDateTime EndLocalDate;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.editshift);
        Button PunchInDate = findViewById(R.id.PunchInDateEditShift);
        Button PunchInTime = findViewById(R.id.PunchInTimeEditShift);
        Button PunchOutDate = findViewById(R.id.PunchOutDateEditShift);
        Button PunchOutTime = findViewById(R.id.PunchOutTimeEditShift);
        Button Save = findViewById(R.id.SaveEditShift);
        Button Cancel = findViewById(R.id.CancelEditShift);
        TextView text1 = findViewById(R.id.textView4);
        TextView text2 = findViewById(R.id.textView5);
        Animation animation = AnimationUtils.loadAnimation(this,R.anim.frombottomtotop);
        PunchInDate.startAnimation(animation);
        PunchInTime.startAnimation(animation);
        PunchOutDate.startAnimation(animation);
        PunchOutTime.startAnimation(animation);
        Save.startAnimation(animation);
        Cancel.startAnimation(animation);
        text1.startAnimation(animation);
        text2.startAnimation(animation);
        int id = getIntent().getIntExtra("id", 0);
        int ShiftId=getIntent().getIntExtra("ShiftID",0);
        long StartTime =getIntent().getLongExtra("StartTime",0);
        long EndTime =getIntent().getLongExtra("EndTime",0);
        if (id==0 || ShiftId==0) {
            Log.e("error", "Error in id");
        }
        if (StartTime==0 || EndTime==0) {
            Log.e("error", "Error in time");
        }
        Instant instant = Instant.ofEpochMilli(StartTime);
        StartLocalDate = instant.atZone(ZoneId.systemDefault()).toLocalDateTime();
        instant = Instant.ofEpochMilli(EndTime);
        EndLocalDate = instant.atZone(ZoneId.systemDefault()).toLocalDateTime();
        PunchInDate.setText(StartLocalDate.getMonthValue()+"/"+StartLocalDate.getDayOfMonth());
        PunchInTime.setText(StartLocalDate.getHour()+":"+StartLocalDate.getMinute());
        if (PunchInTime.getText().length()<=4) {
            PunchInTime.append("0");
        }
        PunchOutDate.setText(EndLocalDate.getMonthValue()+"/"+EndLocalDate.getDayOfMonth());
        PunchOutTime.setText(EndLocalDate.getHour()+":"+EndLocalDate.getMinute());
        if (PunchOutTime.getText().length()<=4) {
            Log.e("done","done");
            PunchOutTime.append("0");
        }
        DatePickerDialog DatePunchInPicker = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                StartLocalDate=StartLocalDate.withYear(year).withMonth(month).withDayOfMonth(dayOfMonth);
                PunchInDate.setText(StartLocalDate.getMonth()+"/"+StartLocalDate.getDayOfMonth());
            }
        },StartLocalDate.getYear(),StartLocalDate.getMonthValue()-1,StartLocalDate.getDayOfMonth());
        DatePickerDialog DatePunchOutPicker = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                EndLocalDate=EndLocalDate.withYear(year).withMonth(month).withDayOfMonth(dayOfMonth);
                PunchOutDate.setText(EndLocalDate.getMonth()+"/"+EndLocalDate.getDayOfMonth());
            }
        },EndLocalDate.getYear(),EndLocalDate.getMonthValue()-1,EndLocalDate.getDayOfMonth());
        TimePickerDialog TimePunchInPicker = new TimePickerDialog(this, new TimePickerDialog.OnTimeSetListener() {

            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                StartLocalDate=StartLocalDate.withHour(hourOfDay).withMinute(minute);
                PunchInTime.setText(StartLocalDate.getHour()+":"+StartLocalDate.getMinute());
                if (PunchInTime.getText().length()<=4) {
                    PunchInTime.append("0");
                }
            }
        },StartLocalDate.getHour(),StartLocalDate.getMinute(),true);
        TimePickerDialog TimePunchOutPicker = new TimePickerDialog(this, new TimePickerDialog.OnTimeSetListener() {

            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                EndLocalDate=EndLocalDate.withHour(hourOfDay).withMinute(minute);
                PunchOutTime.setText(EndLocalDate.getHour()+":"+EndLocalDate.getMinute());
                if (PunchOutTime.getText().length()<=4) {
                    PunchOutTime.append("0");}
                }
        },EndLocalDate.getHour(),EndLocalDate.getMinute(),true);
        PunchInDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePunchInPicker.show();
            }
        });
        PunchOutDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePunchOutPicker.show();
            }
        });
        PunchInTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TimePunchInPicker.show();
            }
        });
        PunchOutTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TimePunchOutPicker.show();
            }
        });
        Save.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                @SuppressLint("StaticFieldLeak") AsyncTask asyncTask = new AsyncTask() {

                    @Override
                    protected Object doInBackground(Object[] objects) {
                        Connection con = new ConnectionHelper().connectionclass();
                        String sql = "UPDATE shifthistory " + "SET StartTime = ?, EndTime = ? " + "WHERE ShiftID = ?";
                        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss[.nnnnnnnnn]");
                        String StringStartTime = StartLocalDate.format(formatter);
                        String StringEndTime = EndLocalDate.format(formatter);
                        Timestamp startTime = Timestamp.valueOf(StringStartTime);
                        Timestamp endTime = Timestamp.valueOf(StringEndTime);
                        try {
                            PreparedStatement pstmt = con.prepareStatement(sql);
                            pstmt.setTimestamp(1, startTime);
                            pstmt.setTimestamp(2, endTime);
                            pstmt.setInt(3, ShiftId);
                            pstmt.executeUpdate();
                            con.close();
                            Snackbar.make(findViewById(android.R.id.content), "Shift Updated", Snackbar.LENGTH_LONG).show();
                        }
                        catch (Exception e){
                            Log.e("error", e.getMessage());
                        }
                        return null;
                    }
                };
                asyncTask.execute();


            }
        });
        Cancel.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), ShiftHystory.class);
                intent.putExtra("id", id);
                startActivity(intent);
                finish();
            }
        });
    }
}