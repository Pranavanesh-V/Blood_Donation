package com.example.blooddonation;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.PopupWindow;
import android.widget.RadioButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class donation_declaration extends AppCompatActivity {
    Button donate2,back_req2;
    RadioButton agree;
    String Phone_number,Name,savedUsername;
    ConstraintLayout layout;
    DatabaseReference databaseReference;
    private static final String PREFS_NAME = "MyPrefs";
    private SharedPreferences sharedPreferences;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_donation_declaration);

        Log.d("page","Declaration page after the donor selects a requester");

        donate2=findViewById(R.id.donate2);
        back_req2=findViewById(R.id.back9);
        agree=findViewById(R.id.Agree);
        layout=findViewById(R.id.dd);

        sharedPreferences = getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        savedUsername = sharedPreferences.getString("username", "");

        //Gets the Requesters name and phone number
        Intent intent=getIntent();
        Phone_number=intent.getStringExtra("Phone");
        Name=intent.getStringExtra("Name");

        //Back function finishes the activity
        back_req2.setOnClickListener(view -> finish());

        //If donate button clicked navigates to dial screen to call the requester
        donate2.setOnClickListener(view -> {
            //It navigates only if the declaration is agreed
            Log.d("output","Navigates the user to dial pad of his device if the user accepts to declaration");
            if (agree.isChecked())
            {
                System.out.println(Phone_number);
                Intent intent1 =new Intent(Intent.ACTION_DIAL,
                        Uri.parse("tel:"+Phone_number));
                startActivityForResult(intent1,1);
            }
            else
            {
                Log.d("output","Don't navigate if the user didn't agree");
                Toast.makeText(donation_declaration.this, R.string.please_agree_to_our_declaration, Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        Log.d("output","See if the user is accepted by the requester");

        //display a pop up asking if the user was accepted to donate or not
        if (requestCode==1)
        {
            LayoutInflater inflater=(LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
            @SuppressLint("InflateParams") View popUpView=inflater.inflate(R.layout.donation_acceptance,null);

            int width= ViewGroup.LayoutParams.MATCH_PARENT;
            int height=ViewGroup.LayoutParams.MATCH_PARENT;
            boolean focusable=true;
            PopupWindow popupWindow=new PopupWindow(popUpView,width,height,focusable);
            layout.post(() -> popupWindow.showAtLocation(layout, Gravity.CENTER,0,0));

            //Buttons
            Button yes,no;
            yes=popUpView.findViewById(R.id.yes);
            no=popUpView.findViewById(R.id.no);

            //If yes is selected upload the requester details to the user's database
            //which will be used to display in history
            yes.setOnClickListener(v -> {
                Get();
                Toast.makeText(donation_declaration.this, "Thanks for Saving a life!!!", Toast.LENGTH_SHORT).show();
                popupWindow.dismiss();
                Intent intent=new Intent(donation_declaration.this, home_page.class);
                startActivity(intent);
                finish();
            });
            //if no is selected navigate the user to the requester list to make him help others
            no.setOnClickListener(v -> {
                Toast.makeText(donation_declaration.this, "Help Another!!!", Toast.LENGTH_SHORT).show();
                popupWindow.dismiss();
                Intent intent=new Intent(donation_declaration.this, home_page.class);
                startActivity(intent);
                finish();
            });
        }
    }

    private void Get()
    {
        // Initialize Firebase Realtime Database
        //get the details of the requester to upload to the user's database
        databaseReference = FirebaseDatabase.getInstance().getReference("Request");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String Blood=dataSnapshot.child(Name).child("RequesterBloodGroup").getValue(String.class);
                String Phone=dataSnapshot.child(Name).child("Requester Phone").getValue(String.class);
                String Address=dataSnapshot.child(Name).child("RequesterLocation").getValue(String.class);

                push(Blood,Phone,Address,Name);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }

    private void push(String blood,String phone,String address,String name) {

        //pass the data to the user's database
        // Initialize Firebase Realtime Database
        databaseReference = FirebaseDatabase.getInstance().getReference("Donars");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                databaseReference.child(savedUsername).child("History").child(name).child("Blood Group").setValue(blood);
                databaseReference.child(savedUsername).child("History").child(name).child("Phone Number").setValue(phone);
                databaseReference.child(savedUsername).child("History").child(name).child("Address").setValue(address);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }
}