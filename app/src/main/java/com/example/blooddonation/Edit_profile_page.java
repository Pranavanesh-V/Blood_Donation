package com.example.blooddonation;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

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
    DatabaseReference databaseReference,databaseReference1;
    EditText blood_Group2,mail_id3,address2;
    TextView name3;
    Button back10,save,cancel,f_device;
    String savedUsername;
    ImageView profile2;
    private Uri image_uri;
    ConstraintLayout layout;

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
            boolean res=fetch();
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
        profile2.setOnClickListener(view -> createPopUpWindow());
    }
    @SuppressLint("ClickableViewAccessibility")
    public void createPopUpWindow()
    {
        LayoutInflater inflater= (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
        @SuppressLint("InflateParams") View popupView=inflater.inflate(R.layout.mainpopup,null);

        int width= ViewGroup.LayoutParams.MATCH_PARENT;
        int height=ViewGroup.LayoutParams.MATCH_PARENT;
        boolean focusable=true;
        cancel=popupView.findViewById(R.id.cancel_button);

        PopupWindow popupWindow=new PopupWindow(popupView,width,height,focusable);
        layout.post(() -> popupWindow.showAtLocation(layout, Gravity.BOTTOM,1,1));
        cancel.setOnClickListener(view -> popupWindow.dismiss());
        popupView.setOnTouchListener((view, motionEvent) -> {
            popupWindow.dismiss();
            return true;
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