package com.example.blooddonation;

import android.os.Bundle;
import android.util.Log;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class Security_page extends AppCompatActivity {

    //Just a Content page displays the security details of the app
    Button back5;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_security_page);

        Log.d("page","Security page which displayed security content of the application");

        back5=findViewById(R.id.back5);
        back5.setOnClickListener(view -> finish());
    }
}