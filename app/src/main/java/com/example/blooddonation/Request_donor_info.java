package com.example.blooddonation;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Request_donor_info extends AppCompatActivity {

    //Class for Requester information display
    String name,phone_no;
    TextView Blood_group_req,name_req,city_req,reason_req;
    Button donate,back_req;
    DatabaseReference databaseReference;
    ImageView profile_req;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_request_donor_info);

        back_req=findViewById(R.id.back8);
        Blood_group_req=findViewById(R.id.Blood_group_req);
        name_req=findViewById(R.id.name_req);
        city_req=findViewById(R.id.city_req);
        donate=findViewById(R.id.donate);
        reason_req=findViewById(R.id.reason_req);
        profile_req=findViewById(R.id.profile_req);

        //Get the data from intents
        Intent intent= getIntent();
        name=intent.getStringExtra("Name");
        String blood=intent.getStringExtra("Blood");
        String location= intent.getStringExtra("Location");
        String TXT=intent.getStringExtra("Txt");
        String Profile_U=intent.getStringExtra("URL_P");

        //set the data
        Uri u=Uri.parse(Profile_U);
        Blood_group_req.setText(blood);
        name_req.setText(name);
        city_req.setText(location);
        reason_req.setText(TXT);

        //If profile doesn't exists set default image
        if (Profile_U.equals("No"))
        {
            profile_req.setImageResource(R.drawable.user);
        }
        else {
            Glide.with(this).load(u)
                    .into(profile_req);
        }

        //Function to fetch the phone number of the Requester
        request_fetch();

        //Finish activity once the user press back button
        back_req.setOnClickListener(view -> finish());

        //Navigate to declaration page
        donate.setOnClickListener(view -> {
            Intent intent1=new Intent(Request_donor_info.this, donation_declaration.class);
            intent1.putExtra("Phone",phone_no);
            intent1.putExtra("Name",name);
            startActivity(intent1);
        });

    }
    public void request_fetch()
    {
        // Initialize Firebase Realtime Database
        databaseReference = FirebaseDatabase.getInstance().getReference("Request");

        databaseReference.addValueEventListener(new ValueEventListener() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                phone_no=dataSnapshot.child(name).child("Requester Phone").getValue(String.class);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }
}