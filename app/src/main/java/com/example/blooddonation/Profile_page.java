package com.example.blooddonation;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

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
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

public class Profile_page extends AppCompatActivity {
    private SharedPreferences sharedPreferences;
    private static final String PREFS_NAME = "MyPrefs";
    DatabaseReference databaseReference;
    String savedUsername,time="";
    Button back_req4;
    int flag;
    ConstraintLayout layout;
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
        layout=findViewById(R.id.Layout_profile);
        progressBar=findViewById(R.id.progressBar);

        //Download the image
        downloadImage();

        //Get the username from the Shared Preferences
        sharedPreferences = getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        savedUsername = sharedPreferences.getString("username", "");

        //Fetch the data of the current user from the firebase
        fetchDataFromFirebase();

        back_req4.setOnClickListener(view -> finish());

        //To edit the details of the current user
        Edit.setOnClickListener(view -> {

            //The below Time functions are used to see if the current user is eligible to edit or not
            //If eligible then Navigate to Edit page
            //Else Make a message stating he needs to try later
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
                    Toast.makeText(Profile_page.this, "You have updated your profile \n Recently wait for some Time", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    //Get the time when the user can Edit His details
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

    //This is for loading Screen which is shown After the editing the details of the current user
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode==1)
        {
            if (resultCode==RESULT_OK)
            {
                LayoutInflater inflater=(LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
                @SuppressLint("InflateParams") View popUpView=inflater.inflate(R.layout.loading_lay,null);

                int width= ViewGroup.LayoutParams.MATCH_PARENT;
                int height=ViewGroup.LayoutParams.WRAP_CONTENT;
                boolean focusable=true;
                PopupWindow popupWindow=new PopupWindow(popUpView,width,height,focusable);
                layout.post(() -> popupWindow.showAtLocation(layout, Gravity.CENTER,0,0));
                ProgressBar progressBar1;
                progressBar1=popUpView.findViewById(R.id.prof);
                new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        progressBar1.setVisibility(View.GONE);
                        popupWindow.dismiss();
                        Toast.makeText(Profile_page.this, "Successfully Updated", Toast.LENGTH_SHORT).show();
                        finish();
                    }
                },5000);

            }
            if (resultCode==RESULT_CANCELED)
            {
                Toast.makeText(this, "Changes Not Made", Toast.LENGTH_SHORT).show();
            }
        }
        else {
            Toast.makeText(this, "Failed", Toast.LENGTH_SHORT).show();
        }
    }

    //The details of the Current user displayed and fetched from the firebase database
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
                        //If the User is only a user not a donor
                        //Then he consists of email and profile to display
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
    public void downloadImage()  {
        //check if profile is present or not
        DatabaseReference reference= FirebaseDatabase.getInstance().getReference().child("Donars");
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot datasnapshot) {
                // Push data to a new unique key
                if (datasnapshot.child(savedUsername).child("Profile").exists())
                {
                    String val=datasnapshot.child(savedUsername).child("Profile").getValue(String.class);
                    boolean res= val.equals("Yes");
                    if (res) {
                        String val_url=datasnapshot.child(savedUsername).child("Profile Value").getValue(String.class);
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.putString("url",val_url).apply();
                        Glide.with(Profile_page.this).load(val_url)
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
                    }
                    else
                    {
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.putString("url","No").apply();
                        String val1="https://firebasestorage.googleapis.com/v0/b/mysql-3bcb9.appspot.com/o/Profile%2Fuser_admin%2Fuser_admin.jpg?alt=media&token=806038cd-611b-49fd-b37b-7dd707043ba8";
                        Glide.with(Profile_page.this).load(val1)
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
                    }
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }
}