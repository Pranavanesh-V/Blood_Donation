package com.example.blooddonation;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class Edit_profile_page extends AppCompatActivity {

    private SharedPreferences sharedPreferences;
    private static final String PREFS_NAME = "MyPrefs";
    DatabaseReference databaseReference,databaseReference1;
    EditText blood_Group2,mail_id3,address2;
    TextView name3;
    Button back10,save;
    String savedUsername;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile_page);

        blood_Group2=findViewById(R.id.blood_Group2);
        name3=findViewById(R.id.name3);
        mail_id3=findViewById(R.id.mail_id3);
        address2=findViewById(R.id.address2);
        back10=findViewById(R.id.back10);
        save=findViewById(R.id.Save);

        Intent intent=getIntent();
        Integer integer=intent.getIntExtra("Flag",0);
        sharedPreferences = getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        savedUsername = sharedPreferences.getString("username", "");
        name3.setText(savedUsername);

        if (integer==1)
        {
            blood_Group2.setEnabled(true);
            address2.setEnabled(true);
            mail_id3.setEnabled(true);
        }
        else
        {
            blood_Group2.setEnabled(false);
            address2.setEnabled(false);
            mail_id3.setEnabled(true);
        }
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean res=fetch();
                if (res)
                {
                    Toast.makeText(Edit_profile_page.this,"Unsuccessfully updated",Toast.LENGTH_SHORT).show();
                }
                else
                {
                    Toast.makeText(Edit_profile_page.this,"Successfully updated",Toast.LENGTH_SHORT).show();
                }
            }
        });
        back10.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    public Boolean fetch(){

        final boolean[] flag = {false};
        // Initialize Firebase Realtime Database
        databaseReference = FirebaseDatabase.getInstance().getReference("Donars");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot)
            {

                if (dataSnapshot.child(savedUsername).child("Blood Group").exists() && dataSnapshot.child(savedUsername).child("Address").exists() && dataSnapshot.child(savedUsername).child("Email").exists())
                {
                    String blood=blood_Group2.getText().toString().trim();
                    String mail=mail_id3.getText().toString().trim();
                    String Address=address2.getText().toString().trim();

                    databaseReference.child(savedUsername).child("Blood Group").setValue(blood);
                    databaseReference.child(savedUsername).child("Address").setValue(Address);
                    databaseReference.child(savedUsername).child("Email").setValue(mail);
                    flag[0] =true;
                    finish();
                }
                else {
                    String mail=mail_id3.getText().toString().trim();
                    databaseReference.child(savedUsername).child("Email").setValue(mail);
                    flag[0] =true;
                    finish();
                }

            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    return flag[0];
    }
}