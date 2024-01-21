package com.example.project;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class AdminEditWorkersPage extends AppCompatActivity {
    ArrayList<WorkerItem> array = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_edit_workers_page);
        RecyclerView recyclerView = findViewById(R.id.WorkersRecyclerList);
        Button NewWorkerButton = findViewById(R.id.NewWorkerEditWorkers);

        Animation animation = AnimationUtils.loadAnimation(this, R.anim.frombottomtotop);
        NewWorkerButton.startAnimation(animation);
        recyclerView.startAnimation(animation);

        //setup adapter and recycler
        Connection con = new ConnectionHelper().connectionclass();
        try {
            ResultSet result = con.createStatement().executeQuery("SELECT * FROM info");
            while (result.next()) {
                array.add(new WorkerItem( result.getInt("ID"), result.getString("Name")+" "+result.getString("Surname")));

            }
            con.close();
        } catch (SQLException e) {
            Log.e("error", e.getMessage());
        }
        AdapterForAdminWorkers adapterForAdminWorkers = new AdapterForAdminWorkers(this, array);
        recyclerView.setAdapter(adapterForAdminWorkers);
        LinearLayoutManager llm = new LinearLayoutManager(this);
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(llm);
        NewWorkerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AdminEditWorkersPage.this, NewWorker.class);
                startActivity(intent);
            }
        });
    }
}