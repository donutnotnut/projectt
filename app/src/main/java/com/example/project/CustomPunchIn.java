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
    // Default punch out and punch in times
    private LocalDateTime PunchOutTime = LocalDateTime.now();
    private LocalDateTime PunchInTime = LocalDateTime.now();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // Retrieving the ID passed from the previous activity
        int id = getIntent().getIntExtra("id", 0);
        if (id == 0) {
            Log.e("error while getting id", "no id was given");
        }
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_custom_punch_in);

        // Initializing UI elements
        Button PunchInDateButton = findViewById(R.id.PunchInDateCustomPunchIn);
        Button PunchInTimeButton = findViewById(R.id.PunchInTimeCustomPunchIn);
        Button PunchOutDateButton = findViewById(R.id.PunchOutDateCustomPunchIn);
        Button PunchOutTimeButton = findViewById(R.id.PunchOutTimeCustomPunchIn);
        Button Exit = findViewById(R.id.CancelCustomPunchIn);
        Button Save = findViewById(R.id.SaveCustomPunchIn);
        TextView text1 = findViewById(R.id.textView4);
        TextView text2 = findViewById(R.id.textView5);
        // Applying animations to UI elements
        text1.startAnimation(AnimationUtils.loadAnimation(this, R.anim.frombottomtotop));
        text2.startAnimation(AnimationUtils.loadAnimation(this, R.anim.frombottomtotop));
        PunchInTimeButton.startAnimation(AnimationUtils.loadAnimation(this, R.anim.frombottomtotop));
        PunchInDateButton.startAnimation(AnimationUtils.loadAnimation(this, R.anim.frombottomtotop));
        PunchOutDateButton.startAnimation(AnimationUtils.loadAnimation(this, R.anim.frombottomtotop));
        PunchOutTimeButton.startAnimation(AnimationUtils.loadAnimation(this, R.anim.frombottomtotop));
        Exit.startAnimation(AnimationUtils.loadAnimation(this, R.anim.frombottomtotop));
        Save.startAnimation(AnimationUtils.loadAnimation(this, R.anim.frombottomtotop));

        // Set default times on buttons
        PunchInDateButton.setText(PunchInTime.getYear() + "/" + PunchInTime.getMonthValue() + "/" + PunchInTime.getDayOfMonth());
        PunchInTimeButton.setText(PunchInTime.getHour() + ":" + PunchInTime.getMinute());

        PunchOutDateButton.setText(PunchOutTime.getYear() + "/" + PunchInTime.getMonthValue() + "/" + PunchInTime.getDayOfMonth());
        PunchOutTimeButton.setText(PunchOutTime.getHour() + ":" + PunchOutTime.getMinute());

        // Initialize date and time picker dialogs
        DatePickerDialog datePickerDialogPunchIn = new DatePickerDialog(CustomPunchIn.this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                PunchInTime = PunchInTime.withYear(year).withMonth(month + 1).withDayOfMonth(dayOfMonth);
                PunchInDateButton.setText(PunchInTime.getYear() + "/" + PunchInTime.getMonthValue() + "/" + PunchInTime.getDayOfMonth());
            }
        }, PunchInTime.getYear(), PunchInTime.getMonthValue() - 1, PunchInTime.getDayOfMonth());

        // Continue for other date and time pickers...

        // Set listeners for date and time pickers
        PunchInDateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                datePickerDialogPunchIn.show();
            }
        });

        // Continue for other buttons...

        Save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                @SuppressLint("StaticFieldLeak") AsyncTask asyncTask = new AsyncTask() {
                    @Override
                    protected Object doInBackground(Object[] objects) {
                        // Inserting punch in and punch out data into the database
                        String insertSQL = "INSERT INTO shifthistory (WorkerID, StartTime, EndTime) VALUES (?, ?, ?)";
                        try {
                            Connection connection = new ConnectionHelper().connectionclass();
                            PreparedStatement preparedStatement = connection.prepareStatement(insertSQL);
                            preparedStatement.setInt(1, id);
                            preparedStatement.setTimestamp(2, new Timestamp(PunchInTime.toInstant(ZoneOffset.UTC).toEpochMilli()));
                            preparedStatement.setTimestamp(3, new Timestamp(PunchOutTime.toInstant(ZoneOffset.UTC).toEpochMilli()));
                            preparedStatement.executeUpdate();
                        } catch (Exception e) {
                            Log.e("error while pushing", e.getMessage());
                        }
                        return null;
                    }

                    // Show a dialog after execution
                    public void onPostExecute(Object o) {
                        AlertDialog.Builder builder = new AlertDialog.Builder(CustomPunchIn.this).setTitle("Added succesfully").setMessage("Shift saved");
                        builder.setOnDismissListener(new DialogInterface.OnDismissListener() {
                            @Override
                            public void onDismiss(DialogInterface dialog) {
                                Intent intent = new Intent(CustomPunchIn.this, MainActivity2.class);
                                intent.putExtra("id", id);
                                startActivity(intent);
                            }
                        });
                        AlertDialog alert = builder.create();
                        alert.show();
                    }
                };
                asyncTask.execute();
            }
        });

        // Finish the activity on exit button click
        Exit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}
