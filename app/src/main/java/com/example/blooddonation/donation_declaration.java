package com.example.blooddonation;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class donation_declaration extends AppCompatActivity {
    Button donate2,back_req2;
    RadioButton agree;
    String Phone_number,Name;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_donation_declaration);

        donate2=findViewById(R.id.donate2);
        back_req2=findViewById(R.id.back9);
        agree=findViewById(R.id.Agree);

        //Gets the Requesters name and phone number
        Intent intent=getIntent();
        Phone_number=intent.getStringExtra("Phone");
        Name=intent.getStringExtra("Name");

        //Back function finishes the activity
        back_req2.setOnClickListener(view -> finish());

        //If donate button clicked navigates to dial screen to call the requester
        donate2.setOnClickListener(view -> {
            //It navigates only if the declaration is agreed
            if (agree.isChecked())
            {
                System.out.println(Phone_number);
                Intent intent1 =new Intent(Intent.ACTION_DIAL,
                        Uri.parse("tel:"+Phone_number));
                startActivity(intent1);
            }
            else
            {
                Toast.makeText(donation_declaration.this, R.string.please_agree_to_our_declaration, Toast.LENGTH_SHORT).show();
            }
        });
    }
}