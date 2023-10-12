package com.example.project;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.sql.Connection;
import java.sql.ResultSet;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        TextView email = findViewById(R.id.EmailTextMainPage);
        TextView password = findViewById(R.id.PasswordTextMainPage);
        Button btn = findViewById(R.id.LogInButtonMainPage);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    Connection connection = null;
                    ConnectionHelper conh=new ConnectionHelper();
                    connection = conh.connectionclass();
                    ResultSet result = connection.createStatement().executeQuery("SELECT * FROM info WHERE Email = '" + email.getText().toString() + "'");
                    if (!result.next()) {
                        btn.setText("didnt get any lines");
                    }
                    if (result.getString("Password").equals(password.getText().toString())){
                        btn.setText("Welcome");
                    }
                }
                catch (Exception e){
                    Log.e("error",e.getMessage());
                }
            }
        });

    }
}