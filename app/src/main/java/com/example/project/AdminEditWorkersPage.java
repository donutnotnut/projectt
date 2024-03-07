package com.example.project;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

/**
 * Activity for editing workers by the admin.
 */
public class AdminEditWorkersPage extends AppCompatActivity {
    ArrayList<WorkerItem> array = new ArrayList<>();

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
        setContentView(R.layout.activity_admin_edit_workers_page);

        RecyclerView recyclerView = findViewById(R.id.WorkersRecyclerList);
        Button NewWorkerButton = findViewById(R.id.NewWorkerEditWorkers);

        Animation animation = AnimationUtils.loadAnimation(this, R.anim.frombottomtotop);
        NewWorkerButton.startAnimation(animation);
        recyclerView.startAnimation(animation);

        /**
         * AsyncTask enables proper and easy use of the UI thread.
         * This class allows performing background operations and publishing results on the UI thread
         * without having to manipulate threads and/or handlers.
         */
        @SuppressLint("StaticFieldLeak") AsyncTask asyncTask = new AsyncTask() {
            /**
             * This method performs a computation on a background thread.
             *
             * @param objects The parameters of the task.
             * @return A result, defined by the subclass of this task.
             */
            @Override
            protected Object doInBackground(Object[] objects) {
                try {
                    Connection con = new ConnectionHelper().connectionclass();
                    ResultSet result = con.createStatement().executeQuery("SELECT * FROM info");
                    while (result.next()) {
                        array.add(new WorkerItem(result.getInt("ID"), result.getString("Name") + " " + result.getString("Surname")));
                    }
                } catch (Exception e) {
                    Log.e("error", e.getMessage());
                }
                AdapterForAdminWorkers adapterForAdminWorkers = new AdapterForAdminWorkers(AdminEditWorkersPage.this, array);
                return adapterForAdminWorkers;
            }

            /**
             * Runs on the UI thread after doInBackground(Object[]).
             * The specified result is the value returned by doInBackground(Object[]).
             *
             * @param o The result of the operation computed by doInBackground(Object[]).
             */
            @Override
            protected void onPostExecute(Object o) {
                recyclerView.setAdapter((RecyclerView.Adapter) o);
                LinearLayoutManager llm = new LinearLayoutManager(AdminEditWorkersPage.this);
                llm.setOrientation(LinearLayoutManager.VERTICAL);
                recyclerView.setLayoutManager(llm);
            }
        };
        asyncTask.execute();

        /**
         * Register a callback to be invoked when this view is clicked.
         * If this view is not clickable, it becomes clickable.
         */
        NewWorkerButton.setOnClickListener(new View.OnClickListener() {
            /**
             * Called when a view has been clicked.
             *
             * @param v The view that was clicked.
             */
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AdminEditWorkersPage.this, NewWorker.class);
                startActivity(intent);
            }
        });
    }
}
