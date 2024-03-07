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
import android.widget.TextView;
import android.widget.TimePicker;
import com.google.android.material.snackbar.Snackbar;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.Timestamp;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

/**
 * Activity for editing shift details.
 */
public class Editingshift extends AppCompatActivity {
    /** LocalDateTime object for the start date and time of the shift. */
    LocalDateTime StartLocalDate;

    /** LocalDateTime object for the end date and time of the shift. */
    LocalDateTime EndLocalDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.editshift);

        // Initialize UI elements
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

        // Get data passed from previous activity
        int id = getIntent().getIntExtra("id", 0);
        int ShiftId=getIntent().getIntExtra("ShiftID",0);
        long StartTime =getIntent().getLongExtra("StartTime",0);
        long EndTime =getIntent().getLongExtra("EndTime",0);

        // Handle potential errors in receiving data
        if (id==0 || ShiftId==0 || StartTime==0 || EndTime==0) {
            Log.e("error", "Error in receiving data");
        }

        // Convert timestamp to LocalDateTime
        Instant instant = Instant.ofEpochMilli(StartTime);
        StartLocalDate = instant.atZone(ZoneId.systemDefault()).toLocalDateTime();
        instant = Instant.ofEpochMilli(EndTime);
        EndLocalDate = instant.atZone(ZoneId.systemDefault()).toLocalDateTime();

        // Set text for buttons with date and time
        PunchInDate.setText(StartLocalDate.getMonthValue()+"/"+StartLocalDate.getDayOfMonth());
        PunchInTime.setText(StartLocalDate.getHour()+":"+StartLocalDate.getMinute());
        PunchOutDate.setText(EndLocalDate.getMonthValue()+"/"+EndLocalDate.getDayOfMonth());
        PunchOutTime.setText(EndLocalDate.getHour()+":"+EndLocalDate.getMinute());

        // Add leading zero if minute has single digit
        if (PunchInTime.getText().length()<=4) {
            PunchInTime.append("0");
        }
        if (PunchOutTime.getText().length()<=4) {
            PunchOutTime.append("0");
        }

        // Initialize date and time pickers
        DatePickerDialog DatePunchInPicker = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                StartLocalDate=StartLocalDate.withYear(year).withMonth(month).withDayOfMonth(dayOfMonth);
                PunchInDate.setText(StartLocalDate.getMonth()+"/"+StartLocalDate.getDayOfMonth());
            }
        },StartLocalDate.getYear(),StartLocalDate.getMonthValue()-1,StartLocalDate.getDayOfMonth());

        // Continue for other pickers...

        // Set listeners for buttons
        PunchInDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePunchInPicker.show();
            }
        });

        // Continue for other buttons...

        Save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                @SuppressLint("StaticFieldLeak") AsyncTask asyncTask = new AsyncTask() {
                    @Override
                    protected Object doInBackground(Object[] objects) {
                        String sql = "UPDATE shifthistory " + "SET StartTime = ?, EndTime = ? " + "WHERE ShiftID = ?";
                        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss[.nnnnnnnnn]");
                        String StringStartTime = StartLocalDate.format(formatter);
                        String StringEndTime = EndLocalDate.format(formatter);
                        Timestamp startTime = Timestamp.valueOf(StringStartTime);
                        Timestamp endTime = Timestamp.valueOf(StringEndTime);
                        try {
                            Connection con = new ConnectionHelper().connectionclass();
                            PreparedStatement pstmt = con.prepareStatement(sql);
                            pstmt.setTimestamp(1, startTime);
                            pstmt.setTimestamp(2, endTime);
                            pstmt.setInt(3, ShiftId);
                            pstmt.executeUpdate();
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

        // Handle cancel button click
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
