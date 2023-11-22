package com.example.blooddonation;

import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class About_page extends AppCompatActivity {

    Button back4;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_page);

        back4=findViewById(R.id.back4);
        back4.setOnClickListener(view -> finish());
    }
}