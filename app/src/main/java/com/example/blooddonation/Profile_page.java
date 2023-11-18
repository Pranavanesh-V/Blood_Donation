package com.example.blooddonation;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.File;
import java.io.IOException;

public class Profile_page extends AppCompatActivity {

    private SharedPreferences sharedPreferences;
    private static final String PREFS_NAME = "MyPrefs";
    DatabaseReference databaseReference;
    String savedUsername;
    Button back_req4;
    int flag;
    TextView blood_Group,name2,mail_id2,Address,Edit;
    ImageView profile;
    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_page);

        profile=findViewById(R.id.profile);
        back_req4=findViewById(R.id.back6);
        blood_Group=findViewById(R.id.blood_Group);
        name2=findViewById(R.id.name2);
        mail_id2=findViewById(R.id.mail_id2);
        Address=findViewById(R.id.address);
        Edit=findViewById(R.id.Edit);
        progressBar=findViewById(R.id.progressBar);
        try {
            downloadImage();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        sharedPreferences = getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        savedUsername = sharedPreferences.getString("username", "");

        fetchDataFromFirebase();

        back_req4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        Edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(Profile_page.this, Edit_profile_page.class);
                intent.putExtra("Flag",flag);
                startActivityForResult(intent,1);
            }
        });

    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode==1)
        {
            try {
                downloadImage();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    private void fetchDataFromFirebase() {
        // Initialize Firebase Realtime Database

        databaseReference = FirebaseDatabase.getInstance().getReference("Donars");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {


                    if (dataSnapshot.child(savedUsername).child("Blood Group").exists() && dataSnapshot.child(savedUsername).child("Address").exists() && dataSnapshot.child(savedUsername).child("Email").exists())
                    {
                        String blood = dataSnapshot.child(savedUsername).child("Blood Group").getValue(String.class);
                        String address = dataSnapshot.child(savedUsername).child("Address").getValue(String.class);
                        String mail = dataSnapshot.child(savedUsername).child("Email").getValue(String.class);
                        String City = dataSnapshot.child(savedUsername).child("City").getValue(String.class);
                        name2.setText(savedUsername);
                        if (blood.equals(""))
                        {
                            blood_Group.setText(R.string.blood_group);
                            blood_Group.setTextColor(Color.GRAY);
                        }
                        else
                        {
                            blood_Group.setText(blood);
                            blood_Group.setTextColor(Color.WHITE);
                        }

                        Address.setText(String.format("%s\n%s", address, City));
                        mail_id2.setText(mail);
                        flag=1;
                    }
                    else
                    {
                        if (dataSnapshot.child(savedUsername).child("Email").exists())
                        {
                            String mail = dataSnapshot.child(savedUsername).child("Email").getValue(String.class);
                            name2.setText(savedUsername);
                            mail_id2.setText(mail);
                            blood_Group.setTextColor(Color.GRAY);
                            Address.setTextColor(Color.GRAY);
                            flag=0;
                        }
                    }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
    public void downloadImage() throws IOException {
        // Get a reference to the Firebase Storage
        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference storageRef = storage.getReference();

        String savedU;
        sharedPreferences = getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        savedU = sharedPreferences.getString("username", "");
        // Reference to the image file
        StorageReference imageRef = storageRef.child("Profile/"+savedU+"/"+savedU + ".jpg");
        System.out.println(savedU);
        // Download the image into a local file
        File localFile = File.createTempFile("images", "jpg");

        imageRef.getFile(localFile)
                .addOnSuccessListener(taskSnapshot -> {
                    // Image downloaded successfully
                    // Now, load the image into ImageView using Glide
                    Glide.with(this).load(localFile)
                            .listener(new RequestListener<Drawable>() {
                                @Override
                                public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                                    // Hide the loading indicator in case of a failure
                                    progressBar.setVisibility(View.GONE);
                                    return false;
                                }

                                @Override
                                public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                                    // Hide the loading indicator when the image is ready
                                    progressBar.setVisibility(View.GONE);
                                    return false;
                                }
                            }).into(profile);
                })
                .addOnFailureListener(exception -> {
                    // Handle errors
                    Log.e("Firebase", "Error downloading image: " + exception.getMessage());
                });

    }
}