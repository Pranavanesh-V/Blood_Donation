package com.example.blooddonation;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Edit_profile_page extends AppCompatActivity {

    private SharedPreferences sharedPreferences;
    private static final String PREFS_NAME = "MyPrefs";
    DatabaseReference databaseReference;
    EditText blood_Group2,mail_id3,address2;
    TextView name3;
    Button back10,save,cancel,f_device,camera;
    String savedUsername;
    ImageView profile2;
    private static final int PICK_IMAGE_REQUEST=1;
    private Uri image_uri;
    ConstraintLayout layout;
    ActivityResultLauncher<Intent> imagePickLauncher;

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
        layout=findViewById(R.id.R_layout);
        profile2=findViewById(R.id.profile2);

        Intent intent=getIntent();
        int integer=intent.getIntExtra("Flag",0);
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
        save.setOnClickListener(view -> {
            int[] data={0,0,0};
            if(blood_Group2.getText().toString().trim().isEmpty())
            {
                if (mail_id3.getText().toString().trim().isEmpty())
                {
                    if (address2.getText().toString().trim().isEmpty())
                    {
                        System.out.println("no change");
                    }
                    else
                    {
                        data[2]=1;
                        System.out.println("Address only");
                    }
                }
                else
                {
                    if (address2.getText().toString().trim().isEmpty())
                    {
                        data[1]=1;
                        System.out.println("only mail_id");
                    }
                    else
                    {
                        data[1]=1;
                        data[2]=1;
                        System.out.println("mail_id and address");
                    }
                }
            }
            else
            {
                if (mail_id3.getText().toString().trim().isEmpty())
                {
                    if (address2.getText().toString().trim().isEmpty())
                    {
                        data[0]=1;
                        System.out.println("only blood_g");
                    }
                    else
                    {
                        data[0]=1;
                        data[2]=1;
                        System.out.println("blood_g and address only");
                    }
                }
                else
                {
                    if (address2.getText().toString().trim().isEmpty())
                    {
                        data[0]=1;
                        data[1]=1;
                        System.out.println("blood_g and mail_id");
                    }
                    else
                    {
                        data[0]=1;
                        data[1]=1;
                        data[2]=1;
                        System.out.println("all options");
                    }
                }
            }
            boolean res=fetch(data);
            if (res)
            {
                Toast.makeText(Edit_profile_page.this,"Unsuccessfully updated",Toast.LENGTH_SHORT).show();
            }
            else
            {
                Toast.makeText(Edit_profile_page.this,"Successfully updated",Toast.LENGTH_SHORT).show();
            }
        });
        back10.setOnClickListener(view -> finish());
    }
    public Boolean fetch(int[] d){

        final boolean[] flag = {false};
        // Initialize Firebase Realtime Database
        databaseReference = FirebaseDatabase.getInstance().getReference("Donars");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot)
            {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    if (snapshot.child(savedUsername).child("Blood Group").exists() && snapshot.child(savedUsername).child("Address").exists() && snapshot.child(savedUsername).child("Email").exists()) {
                        //to check if the user doesn't changes the existing data but changes the profile
                        //if he changes the data handel it else just update the profile
                        String blood, mail, Address;
                        if (d[0] == 1 || d[1] == 1 || d[2] == 1) {
                            if (d[0] == 1) {
                                blood = blood_Group2.getText().toString().trim();
                                databaseReference.child(savedUsername).child("Blood Group").setValue(blood);
                                System.out.println("place 1");
                            }
                            if (d[1] == 1) {
                                mail = mail_id3.getText().toString().trim();
                                databaseReference.child(savedUsername).child("Email").setValue(mail);
                                System.out.println("place 2");
                            }
                            if (d[2] == 1) {
                                Address = address2.getText().toString().trim();
                                databaseReference.child(savedUsername).child("Address").setValue(Address);
                                System.out.println("place 3");
                            }
                        } else {
                            //do nothing
                        }
                    } else {
                        if (d[1] == 1) {
                            String mail = mail_id3.getText().toString().trim();
                            databaseReference.child(savedUsername).child("Email").setValue(mail);
                            System.out.println("Email_id");
                        }
                    }
                    flag[0] = true;
                    finish();
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    return flag[0];
    }
    private void initSharedPreferences() {
        sharedPreferences = getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
    }
}