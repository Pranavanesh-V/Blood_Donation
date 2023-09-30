package com.example.blooddonation;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

public class Donor_info_pg extends AppCompatActivity {

    TextView Name,Phone;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_donar_info_pg);

        Name=findViewById(R.id.Name);
        Phone=findViewById(R.id.Phone_number);

        Intent intent=getIntent();
        String name=intent.getStringExtra("selectedItemTitle");
        Name.setText(name);

    }
}