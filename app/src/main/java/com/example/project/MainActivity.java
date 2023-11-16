package com.example.project;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.Switch;
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
        Switch AdminSwitch = findViewById(R.id.AdminSwitch);
        Animation animation = AnimationUtils.loadAnimation(this, R.anim.frombottomtotop);
        email.startAnimation(animation);
        password.startAnimation(animation);
        btn.startAnimation(animation);
        SharedPreferences sharedPreferences = getSharedPreferences("MyPrefs", MODE_PRIVATE);
        String Email = sharedPreferences.getString("email", null);
        String Password = sharedPreferences.getString("password", null);
        if (Email!=null) {
            email.setText(Email);
            password.setText(Password);
            btn.performClick();
        }
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    Connection connection = null;
                    ConnectionHelper conh=new ConnectionHelper();
                    connection = conh.connectionclass();
                    ResultSet result = connection.createStatement().executeQuery("SELECT * FROM info WHERE Email = '" + email.getText().toString() + "'");
                    if (!result.next()){
                        Toast.makeText(MainActivity.this, "Wrong email", Toast.LENGTH_SHORT).show();
                    }
                    if (result.getString("Password").equals(password.getText().toString())) {
                        int id=result.getInt("ID");
                        connection.close();

                        if (!AdminSwitch.isChecked()) {
                            Intent intent = new Intent(MainActivity.this, MainActivity2.class);
                            intent.putExtra("id",id);
                            startActivity(intent);
                        }
                        else{
                            Intent intent = new Intent(MainActivity.this, AdminMainPage.class);
                            intent.putExtra("id",id);
                            startActivity(intent);
                        }

                    }
                    else {
                        Toast.makeText(MainActivity.this, "Wrong email", Toast.LENGTH_SHORT).show();
                    }
                }
                catch (Exception e){
                    Log.e("error",e.getMessage());
                }
            }
        });

    }
}