package com.example.project;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.TimePicker;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.ZoneOffset;

public class CustomPunchIn extends AppCompatActivity {
    private LocalDateTime PunchOutTime=LocalDateTime.now();
    private LocalDateTime PunchInTime=LocalDateTime.now();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        int id = getIntent().getIntExtra("id",0);
        if (id==0) {
            Log.e("error while getting id","no id was given");
        }
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_custom_punch_in);
        Button PunchInDateButton=findViewById(R.id.PunchInDateCustomPunchIn);
        Button PunchInTimeButton=findViewById(R.id.PunchInTimeCustomPunchIn);
        Button PunchOutDateButton=findViewById(R.id.PunchOutDateCustomPunchIn);
        Button PunchOutTimeButton=findViewById(R.id.PunchOutTimeCustomPunchIn);
        Button Exit = findViewById(R.id.CancelCustomPunchIn);
        Button Save= findViewById(R.id.SaveCustomPunchIn);
        TextView text1= findViewById(R.id.textView4);
        TextView text2=findViewById(R.id.textView5);
        text1.startAnimation(AnimationUtils.loadAnimation(this, R.anim.frombottomtotop));
        text2.startAnimation(AnimationUtils.loadAnimation(this, R.anim.frombottomtotop));
        PunchInTimeButton.startAnimation(AnimationUtils.loadAnimation(this, R.anim.frombottomtotop));
        PunchInDateButton.startAnimation(AnimationUtils.loadAnimation(this, R.anim.frombottomtotop));
        PunchOutDateButton.startAnimation(AnimationUtils.loadAnimation(this, R.anim.frombottomtotop));
        PunchOutTimeButton.startAnimation(AnimationUtils.loadAnimation(this, R.anim.frombottomtotop));
        Exit.startAnimation(AnimationUtils.loadAnimation(this, R.anim.frombottomtotop));
        Save.startAnimation(AnimationUtils.loadAnimation(this, R.anim.frombottomtotop));

        //set times and buttons to standard

        PunchInDateButton.setText(PunchInTime.getYear()+"/"+PunchInTime.getMonthValue()+"/"+PunchInTime.getDayOfMonth());
        PunchInTimeButton.setText(PunchInTime.getHour()+":"+PunchInTime.getMinute());

        PunchOutDateButton.setText(PunchOutTime.getYear()+"/"+PunchInTime.getMonthValue()+"/"+PunchInTime.getDayOfMonth());
        PunchOutTimeButton.setText(PunchOutTime.getHour()+":"+PunchOutTime.getMinute());

        //set pickers

        DatePickerDialog datePickerDialogPunchIn= new DatePickerDialog(CustomPunchIn.this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                PunchInTime=PunchInTime.withYear(year).withMonth(month).withDayOfMonth(dayOfMonth);
                PunchInDateButton.setText(PunchInTime.getYear()+"/"+PunchInTime.getMonthValue()+"/"+PunchInTime.getDayOfMonth());
            }
        },PunchInTime.getYear(),PunchInTime.getMonthValue()-1,PunchInTime.getDayOfMonth());

        DatePickerDialog datePickerDialogPunchOut= new DatePickerDialog(CustomPunchIn.this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                PunchOutTime=PunchOutTime.withYear(year).withMonth(month).withDayOfMonth(dayOfMonth);
                PunchOutDateButton.setText(PunchOutTime.getYear()+"/"+PunchOutTime.getMonthValue()+"/"+PunchOutTime.getDayOfMonth());
            }
        },PunchOutTime.getYear(),PunchOutTime.getMonthValue()-1,PunchOutTime.getDayOfMonth());
        TimePickerDialog timePickerDialogPunchIn = new TimePickerDialog(CustomPunchIn.this, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                PunchInTime=PunchInTime.withHour(hourOfDay).withMinute(minute);
                PunchInTimeButton.setText(PunchInTime.getHour()+":"+PunchInTime.getMinute());
            }
        },PunchInTime.getHour(),PunchInTime.getMinute(),true);
        TimePickerDialog timePickerDialogPunchOut = new TimePickerDialog(CustomPunchIn.this, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                PunchOutTime=PunchOutTime.withHour(hourOfDay).withMinute(minute);
                PunchOutTimeButton.setText(PunchOutTime.getHour()+":"+PunchOutTime.getMinute());
            }
        },PunchOutTime.getHour(),PunchOutTime.getMinute(),true);

        PunchInDateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                datePickerDialogPunchIn.show();
            }
        });
        PunchInTimeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                timePickerDialogPunchIn.show();
            }
        });
        PunchOutDateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                datePickerDialogPunchOut.show();
            }
        });
        PunchOutTimeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                timePickerDialogPunchOut.show();
            }
        });
        Exit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(CustomPunchIn.this, MainActivity2.class);
                intent.putExtra("id",id);
                startActivity(intent);
            }
        });
        Save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                @SuppressLint("StaticFieldLeak") AsyncTask asyncTask= new AsyncTask() {
                    @Override
                    protected Object doInBackground(Object[] objects) {
                        Connection connection= new ConnectionHelper().connectionclass();
                        String insertSQL = "INSERT INTO shifthistory (WorkerID, StartTime, EndTime) VALUES (?, ?, ?)";
                        try (PreparedStatement preparedStatement = connection.prepareStatement(insertSQL)) {
                            preparedStatement.setInt(1, id); // Replace with the actual ID value
                            preparedStatement.setTimestamp(2, new Timestamp(PunchInTime.toInstant(ZoneOffset.UTC).toEpochMilli()));
                            preparedStatement.setTimestamp(3, new Timestamp(PunchOutTime.toInstant(ZoneOffset.UTC).toEpochMilli()));
                            preparedStatement.executeUpdate();



                        } catch (SQLException e) {
                            Log.e("error while pushing", e.getMessage());
                        }
                        return null;
                    }
                    public void onPostExecute(Object o){
                        AlertDialog.Builder builder = new AlertDialog.Builder(CustomPunchIn.this).setTitle("Added succesfully").setMessage("Shift saved");
                        builder.setOnDismissListener(new DialogInterface.OnDismissListener() {
                            @Override
                            public void onDismiss(DialogInterface dialog) {
                                Intent intent = new Intent(CustomPunchIn.this, MainActivity2.class);
                                intent.putExtra("id", id);
                                startActivity(intent);
                            }
                        });
                        AlertDialog alert=builder.create();
                        alert.show();
                    }
                };
                asyncTask.execute();

            }
        });
        Exit.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}