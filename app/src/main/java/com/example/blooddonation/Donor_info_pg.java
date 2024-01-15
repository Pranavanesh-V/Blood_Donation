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

public class Donor_info_pg extends AppCompatActivity {

    TextView T_Name, T_Phone, T_Blood, T_Mail_id, T_Address;
    DatabaseReference databaseReference;
    Button back;
    String Phone,Email_id,Address,S_Location;
    ImageView p_img;

    //Class is used only for displaying the details of the donars
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
        p_img=findViewById(R.id.p_img);

        //get the intent values
        Intent intent = getIntent();
        String S_name = intent.getStringExtra("Name");
        String blood = intent.getStringExtra("Blood");
        String Profile_U=intent.getStringExtra("URL_P");

        //sets the profile using uri
        Uri u=Uri.parse(Profile_U);
        T_Name.setText(S_name);
        T_Blood.setText(blood);
        //if the profile value is no then default image is displayed
        //else the profile of the donor is shown
        if (Profile_U.equals("No"))
        {
            p_img.setImageResource(R.drawable.user);
        }
        else {
            Glide.with(this).load(u)
                    .into(p_img);
        }
        // Initialize Firebase Realtime Database
        databaseReference = FirebaseDatabase.getInstance().getReference("Donars");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @SuppressLint({"NotifyDataSetChanged", "SetTextI18n"})
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    // Parse data from the snapshot
                    String name=snapshot.getKey();
                    if (S_name.equals(name)) {
                        //Fetch the data from the database
                        Email_id = snapshot.child("Email").getValue(String.class);
                        Address = snapshot.child("Address").getValue(String.class);
                        Phone = snapshot.child("Phone No").getValue(String.class);
                        S_Location=snapshot.child("City").getValue(String.class);

                        //For displaying the email dynamically according to the size
                        if (!(Email_id.length()<23)) {
                            T_Mail_id.setText(Email_id.substring(0,23)+"\n"+ Email_id.substring(23));
                        }else {
                        T_Mail_id.setText(Email_id);}
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

        back.setOnClickListener(view -> finish());

        //Navigates to the Google maps for the given address
        T_Address.setOnClickListener(view -> {
            String address=T_Address.getText().toString().trim();
            Intent intent1 =new Intent(Intent.ACTION_VIEW,
                    Uri.parse("geo:0,0?q="+address));
            startActivity(intent1);
        });

        //Navigates to the Dial screen for the displayed Mobile Number
        T_Phone.setOnClickListener(view -> {
            Intent intent12 =new Intent(Intent.ACTION_DIAL, Uri.parse("tel:"+Phone));
            startActivity(intent12);
        });
    }
}