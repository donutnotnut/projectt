package com.example.project;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationManagerCompat;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.snackbar.Snackbar;

import java.sql.Connection;
import java.sql.ResultSet;

/**
 * The main activity for user login and navigation.
 */
public class MainActivity extends AppCompatActivity {
    InternetBroadcast broadcastReceiver = new InternetBroadcast();

    /**
     * Initializes the main activity.
     * @param savedInstanceState The saved instance state.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Register broadcast receiver to monitor internet connectivity
        IntentFilter filter = new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
        registerReceiver(broadcastReceiver, filter);

        // Set content view and initialize UI elements
        setContentView(R.layout.activity_main);
        TextView email = findViewById(R.id.EmailTextMainPage);
        TextView password = findViewById(R.id.PasswordTextMainPage);
        Button btn = findViewById(R.id.LogInButtonMainPage);
        Switch AdminSwitch = findViewById(R.id.AdminSwitch);

        // Animation for UI elements
        Animation animation = AnimationUtils.loadAnimation(this, R.anim.frombottomtotop);
        email.startAnimation(animation);
        password.startAnimation(animation);
        btn.startAnimation(animation);

        // Retrieve email and password from SharedPreferences if available
        SharedPreferences sharedPreferences = getSharedPreferences("MyPrefs", MODE_PRIVATE);
        String Email = sharedPreferences.getString("email", null);
        String Password = sharedPreferences.getString("password", null);

        // Auto-fill email and password fields if SharedPreferences has stored values
        if (Email!=null) {
            email.setText(Email);
            password.setText(Password);
            btn.performClick(); // Perform login automatically
        }

        // Button click listener for login functionality
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    // Establish database connection and retrieve user information
                    Connection connection = new ConnectionHelper().connectionclass();
                    ResultSet result = connection.createStatement().executeQuery("SELECT * FROM info WHERE Email = '" + email.getText().toString() + "'");

                    // Handle incorrect email
                    if (!result.next()){
                        Snackbar.make(v, "Wrong email", Snackbar.LENGTH_SHORT).show();
                    }

                    // Check if the entered password is correct
                    if (result.getString("Password").equals(password.getText().toString())) {
                        int id=result.getInt("ID");
                        //put id into shared preferences
                        SharedPreferences sharedPreferences = getSharedPreferences("MyPrefs", MODE_PRIVATE);
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.putInt("id", id);
                        editor.apply();

                        // Start appropriate activity based on user role (admin or regular user)
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
                        Snackbar.make(v, "Wrong password", Snackbar.LENGTH_SHORT).show();
                    }
                }
                catch (Exception e){
                    Log.e("error",e.getMessage());
                }
            }
        });
    }

    /**
     * Called when the activity is stopped.
     */
    @Override
    public void onStop() {
        super.onStop();
    }
}
