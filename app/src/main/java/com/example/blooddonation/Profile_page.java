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
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.google.firebase.Timestamp;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Profile_page extends AppCompatActivity {

    private SharedPreferences sharedPreferences;
    private static final String PREFS_NAME = "MyPrefs";
    DatabaseReference databaseReference;
    String savedUsername;
    Button back_req4;
    int flag;
    String time="";
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

        back_req4.setOnClickListener(view -> finish());

        Edit.setOnClickListener(view -> {
            Timestamp firebaseTimestamp = Timestamp.now();

            // Convert Firebase Timestamp to java.util.Date
            Date date = firebaseTimestamp.toDate();
            // Create a formatter for a readable date and time format
            @SuppressLint("SimpleDateFormat") SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            // Format and display the original and updated timestamps
            String formattedOriginalTimestamp = formatter.format(date);
            //the if statement is for the people who have already update
            String time = Time();
            if (!time.isEmpty())
            {
                int res = time.compareTo(formattedOriginalTimestamp);
                System.out.println(res);
                System.out.println(time);
                if (res < 0) {
                    Intent intent = new Intent(Profile_page.this, Edit_profile_page.class);
                    intent.putExtra("Flag", flag);
                    startActivityForResult(intent, 1);
                }
                if (res > 0) {
                    Toast.makeText(Profile_page.this, "You have updated your profile \n Recently wait for some Time", Toast.LENGTH_SHORT).show();
                }
            }

        });
    }

    public String Time()
    {

        // Initialize Firebase Realtime Database
        databaseReference = FirebaseDatabase.getInstance().getReference("Donars");
        databaseReference.addValueEventListener(new ValueEventListener(){
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot){
                if(dataSnapshot.child(savedUsername).child("Time Remove").exists()){
                  time = dataSnapshot.child(savedUsername).child("Time Remove").getValue(String.class);
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        return time;
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode==1 && resultCode==RESULT_OK)
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
                        }
                        else
                        {
                            blood_Group.setText(blood);
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
        DatabaseReference reference= FirebaseDatabase.getInstance().getReference().child("Donars");
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot datasnapshot) {
                // Push data to a new unique key
                String p_val=datasnapshot.child(savedUsername).child("Profile").getValue(String.class);
                System.out.println(p_val);
                if (p_val.equals("Yes"))
                {
                    System.out.println("its in");
                    // Reference to the image file
                    StorageReference imageRef = storageRef.child("Profile/"+savedU+"/"+savedU + ".jpg");
                    // Download the image into a local file
                    File localFile = null;
                    try {
                        localFile = File.createTempFile("images", "jpg");
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }

                    File finalLocalFile = localFile;
                    imageRef.getFile(localFile)
                            .addOnSuccessListener((FileDownloadTask.TaskSnapshot taskSnapshot) -> {
                                // Image downloaded successfully
                                // Now, load the image into ImageView using Glide
                                Glide.with(Profile_page.this).load(finalLocalFile)
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
                else
                {
                    String val="https://firebasestorage.googleapis.com/v0/b/mysql-3bcb9.appspot.com/o/Profile%2FUser_Test%2FUser_Test.jpg?alt=media&token=e5e40c07-d38a-4588-9da9-f57c3ea236030";
                    Glide.with(Profile_page.this).load(val).into(profile);
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });

    }
}