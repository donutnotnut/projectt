package com.example.project;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.Button;

/**
 * This is the main page for the admin user.
 * It provides the admin with options to edit workers, shifts, and view the schedule for the next week.
 */
public class AdminMainPage extends AppCompatActivity {

    /**
     * This method is called when the activity is starting.
     * It is where most initialization happens: calling setContentView(int) to inflate the activity's UI,
     * and using findViewById(int) to programmatically interact with widgets in the UI.
     *
     * @param savedInstanceState If the activity is being re-initialized after previously being shut down
     * then this Bundle contains the data it most recently supplied in onSaveInstanceState(Bundle).
     * Note: Otherwise it is null.
     */
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

        /**
         * Register a callback to be invoked when the EditWorkersButton view is clicked.
         * If this view is not clickable, it becomes clickable.
         */
        EditWorkersButton.setOnClickListener(new View.OnClickListener() {

            /**
             * Called when the EditWorkersButton view has been clicked.
             * It starts the AdminEditWorkersPage activity.
             *
             * @param v The view that was clicked.
             */
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AdminMainPage.this, AdminEditWorkersPage.class);
                startActivity(intent);
            }
        });

        /**
         * Register a callback to be invoked when the EditShiftsButton view is clicked.
         * If this view is not clickable, it becomes clickable.
         */
        EditShiftsButton.setOnClickListener(new View.OnClickListener() {

            /**
             * Called when the EditShiftsButton view has been clicked.
             * It starts the ShiftsHistoryAdmin activity.
             *
             * @param v The view that was clicked.
             */
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AdminMainPage.this, ShiftsHistoryAdmin.class);
                startActivity(intent);
            }
        });

        /**
         * Register a callback to be invoked when the NextWeekScheduleButton view is clicked.
         * If this view is not clickable, it becomes clickable.
         */
        NextWeekScheduleButton.setOnClickListener(new View.OnClickListener() {

            /**
             * Called when the NextWeekScheduleButton view has been clicked.
             * It starts the NextWeekAdmin activity.
             *
             * @param v The view that was clicked.
             */
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AdminMainPage.this, NextWeekAdmin.class);
                startActivity(intent);
            }
        });
    }
}
