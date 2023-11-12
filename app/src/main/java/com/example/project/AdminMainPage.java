package com.example.project;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.Button;

public class AdminMainPage extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_main_page);
        Button EditWorkersButton = findViewById(R.id.EditWorkersButtonAdminPage);
        Button EditShiftsButton = findViewById(R.id.EditShiftsButtonAdminPAge);
        Button NextWeekScheduleButton = findViewById(R.id.NextWeekScheduleAdminPage);
        EditWorkersButton.startAnimation(AnimationUtils.loadAnimation(this, R.anim.frombottomtotop));
        EditShiftsButton.startAnimation(AnimationUtils.loadAnimation(this, R.anim.frombottomtotop));
        NextWeekScheduleButton.startAnimation(AnimationUtils.loadAnimation(this, R.anim.frombottomtotop));
        EditWorkersButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AdminMainPage.this, AdminEditWorkersPage.class);
                startActivity(intent);
            }
        });
        EditShiftsButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AdminMainPage.this, ShiftsHistoryAdmin.class);
                startActivity(intent);
            }
        });
        NextWeekScheduleButton.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               Intent intent = new Intent(AdminMainPage.this, NextWeekAdmin.class);
               startActivity(intent);
           }
        });
    }
}