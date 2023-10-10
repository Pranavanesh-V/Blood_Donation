package com.example.blooddonation;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class donation_declaration extends AppCompatActivity {
    Button donate2,back_req2;
    String Phone_number;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_donation_declaration);

        donate2=findViewById(R.id.donate2);
        back_req2=findViewById(R.id.back9);

        Intent intent=getIntent();
        Phone_number=intent.getStringExtra("Phone");

        back_req2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        donate2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(Intent.ACTION_DIAL,
                        Uri.parse("tel:"+Phone_number));
                startActivity(intent);
            }
        });


    }
}