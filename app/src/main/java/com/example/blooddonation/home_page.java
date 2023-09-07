package com.example.blooddonation;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class home_page extends AppCompatActivity {

    Button button,button2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page);

        button=findViewById(R.id.button);
        button2=findViewById(R.id.button2);

        button.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("UseCompatLoadingForDrawables")
            @Override
            public void onClick(View view) {
                button.setBackground(getDrawable(R.drawable.cus_join11));
                button2.setBackground(getDrawable(R.drawable.cus_join2));
            }
        });
        button2.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("UseCompatLoadingForDrawables")
            @Override
            public void onClick(View view) {
                button.setBackground(getDrawable(R.drawable.cus_join1));
                button2.setBackground(getDrawable(R.drawable.cus_join22));
            }
        });



    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finishAffinity();
    }
}