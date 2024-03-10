package com.example.project;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import android.content.Context;

public class NfcHandlerActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.i("started", "started");
        super.onCreate(savedInstanceState);
        
        Intent serviceIntent = new Intent(this, MyNfcService.class);
        ContextCompat.startForegroundService(this, serviceIntent);

        finish();
    }
}