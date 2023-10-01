package com.example.blooddonation;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class Donor_info_pg extends AppCompatActivity {

    TextView Name,Phone,Blood;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_donar_info_pg);

        Name=findViewById(R.id.Name);
        Phone=findViewById(R.id.Phone_number);
        Blood=findViewById(R.id.Blood_Group);

        Intent intent=getIntent();
        String name=intent.getStringExtra("Name");
        String blood=intent.getStringExtra("Blood");
        Name.setText(name);
        Blood.setText(blood);


    }
}