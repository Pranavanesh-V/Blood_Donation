package com.example.blooddonation;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Donor_info_pg extends AppCompatActivity {

    TextView T_Name, T_Phone, T_Blood, T_Mail_id, T_Address;
    DatabaseReference databaseReference;
    Button back;
    String Phone,Email_id,Address,S_Location;
    ImageView imageView8;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_donar_info_pg);

        T_Name = findViewById(R.id.Name);
        T_Phone = findViewById(R.id.Phone_number);
        T_Blood = findViewById(R.id.Blood_Group);
        T_Mail_id =findViewById(R.id.Mail_id);
        T_Address =findViewById(R.id.Address);
        back=findViewById(R.id.back);
        imageView8=findViewById(R.id.imageView8);

        //get the intent values
        Intent intent = getIntent();
        String S_name = intent.getStringExtra("Name");
        String blood = intent.getStringExtra("Blood");
        String S_uri=intent.getStringExtra("Image_uri");
        Uri uri=Uri.parse(S_uri);
        T_Name.setText(S_name);
        T_Blood.setText(blood);
        imageView8.setImageURI(uri);
        // Initialize Firebase Realtime Database
        databaseReference = FirebaseDatabase.getInstance().getReference("Donars");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    // Parse data from the snapshot
                    String name=snapshot.getKey();
                    if (S_name.equals(name)) {
                         Email_id = snapshot.child("Email").getValue(String.class);
                        Address = snapshot.child("Address").getValue(String.class);
                        Phone = snapshot.child("Phone No").getValue(String.class);
                        S_Location=snapshot.child("City").getValue(String.class);
                        T_Mail_id.setText(Email_id);
                        T_Address.setText(String.format("%s,\n%s", Address, S_Location));
                        T_Phone.setText(Phone);
                        //if image doesn't exists place a common user image for each gender
                        break;
                    }
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {}
        });

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        T_Address.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String address=T_Address.getText().toString().trim();
                Intent intent=new Intent(Intent.ACTION_VIEW,
                        Uri.parse("geo:0,0?q="+address));
                startActivity(intent);
            }
        });

        T_Phone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent =new Intent(Intent.ACTION_DIAL, Uri.parse("tel:"+Phone));
                startActivity(intent);
            }
        });
    }
}