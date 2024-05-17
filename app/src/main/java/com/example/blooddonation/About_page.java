package com.example.blooddonation;

import android.os.Bundle;
import android.util.Log;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class About_page extends AppCompatActivity {

    Button back4;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_page);

        //About page with only back button
        //Displays about the application

        back4=findViewById(R.id.back4);
        Log.d("page","About page is being viewed");

        //back button in the about page
        back4.setOnClickListener(view -> finish());
    }
}