package com.example.blooddonation;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Handler handler = new Handler();
        handler.postDelayed(this::navigateToNextActivity, 1200); // Delay in milliseconds (1.20 seconds in this example)

    }

    public void navigateToNextActivity() {
        Intent intent = new Intent(this, intro_pages.class);
        startActivity(intent);

    }
}