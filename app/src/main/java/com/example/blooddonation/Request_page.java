package com.example.blooddonation;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.Button;
import android.widget.EditText;
import android.widget.PopupMenu;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.Timestamp;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class Request_page extends AppCompatActivity {

    //This class is to send Request (i.e) Request in need for blood
    TextInputLayout name,Address,Phone_no,Reason,Desc_Reason,blood_g;
    Button submit,back_req3;
    TextInputEditText B_G;
    private SharedPreferences sharedPreferences;
    private static final String PREFS_NAME = "MyPrefs";
    String S_name="",S_address="",S_phone="",S_Reason="",S_Desc_Reason="",S_blood_g="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_request_page);
        name=findViewById(R.id.name);
        Address=findViewById(R.id.Address);
        Phone_no=findViewById(R.id.Phone_no);
        Reason=findViewById(R.id.Reason);
        blood_g = findViewById(R.id.blood_G);
        Desc_Reason=findViewById(R.id.Desc_Reason);
        submit=findViewById(R.id.submit2);
        B_G=findViewById(R.id.B_g);
        back_req3=findViewById(R.id.back7);

        //Get the user name and profile picture url
        sharedPreferences = getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        String savedUsername=sharedPreferences.getString("username","");
        String profile=sharedPreferences.getString("url","No");

        //The PopUp Menu to select Blood Group
        B_G.setOnClickListener(view -> showPopupMenu());

        //Close the current Activity
        back_req3.setOnClickListener(view -> finish());

        //Get the String value from EditText
        EditText E_name=name.getEditText();
        EditText E_Address=Address.getEditText();
        EditText E_Phone=Phone_no.getEditText();
        EditText E_Reason=Reason.getEditText();
        EditText E_Desc_Reason=Desc_Reason.getEditText();

        assert E_name != null;
        assert E_Address != null;
        assert E_Phone != null;
        assert E_Reason != null;
        assert E_Desc_Reason != null;

        TextWatcher login=new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                S_name=E_name.getText().toString().trim();
                S_address=E_Address.getText().toString().trim();
                S_phone=E_Phone.getText().toString().trim();
                S_Reason=E_Reason.getText().toString().trim();
                S_Desc_Reason=E_Desc_Reason.getText().toString().trim();
            }
            @Override
            public void afterTextChanged(Editable s) {}
        };
        E_name.addTextChangedListener(login);
        E_Address.addTextChangedListener(login);
        E_Phone.addTextChangedListener(login);
        E_Reason.addTextChangedListener(login);
        E_Desc_Reason.addTextChangedListener(login);

        //Raises a Request for blood
        submit.setOnClickListener(view -> {

            if (S_name.isEmpty()||S_address.isEmpty()||S_phone.isEmpty()||S_blood_g.isEmpty()||S_Reason.isEmpty()||S_Desc_Reason.isEmpty())
            {
                Toast.makeText(Request_page.this,"Empty Credentials",Toast.LENGTH_SHORT).show();
            }
            else {
                DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("Request");
                reference.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot datasnapshot) {

                        //Time Denotes how long the Request will be Displayed
                        Timestamp firebaseTimestamp = Timestamp.now();

                        // Convert Firebase Timestamp to java.util.Date
                        Date date = firebaseTimestamp.toDate();

                        // Add 24 hours to the time
                        Calendar calendar = Calendar.getInstance();
                        calendar.setTime(date);
                        calendar.add(Calendar.HOUR_OF_DAY, 24);

                        // Convert the updated time back to a Date
                        Date updatedDate = calendar.getTime();

                        // Create a formatter for a readable date and time format
                        @SuppressLint("SimpleDateFormat") SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                        // Format and display the original and updated timestamps
                        String formattedOriginalTimestamp = formatter.format(date);
                        String formattedUpdatedTimestamp = formatter.format(updatedDate);

                        //Store the data into the Firebase Database
                        reference.child(S_name).child("RequesterLocation").setValue(S_address);
                        reference.child(S_name).child("Requester Phone").setValue(S_phone);
                        reference.child(S_name).child("RequesterReason").setValue(S_Reason);
                        reference.child(S_name).child("Desc Reason").setValue(S_Desc_Reason);
                        reference.child(S_name).child("RequesterBloodGroup").setValue(S_blood_g);
                        reference.child(S_name).child("Received").setValue("No");
                        reference.child(S_name).child("Profile").setValue(profile);
                        reference.child(S_name).child("Time Uploaded").setValue(formattedOriginalTimestamp);
                        reference.child(S_name).child("Time Remove").setValue(formattedUpdatedTimestamp);

                        //Send the Request to every donor who has same blood group
                        Sms_sender sms_sender=new Sms_sender();
                        sms_sender.send(S_blood_g,S_name);
                        Toast.makeText(Request_page.this,"Request Generated Successfully",Toast.LENGTH_SHORT).show();
                        setResult(RESULT_OK);
                        onBackPressed();
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Toast.makeText(Request_page.this,"Request Generating Unsuccessfully",Toast.LENGTH_SHORT).show();
                        finish();
                    }
                });
            }
        });
    }

    //PopUp Menu for Blood Group
    private void showPopupMenu() {
        PopupMenu popupMenu = new PopupMenu(this, blood_g);
        popupMenu.getMenu().add("O+");
        popupMenu.getMenu().add("O-");
        popupMenu.getMenu().add("A+");
        popupMenu.getMenu().add("A-");
        popupMenu.getMenu().add("B+");
        popupMenu.getMenu().add("B-");
        popupMenu.getMenu().add("AB+");
        popupMenu.getMenu().add("Ab-");

        // Set an item click listener for the PopupMenu
        popupMenu.setOnMenuItemClickListener(item -> {
            // Handle item selection here
            S_blood_g = item.getTitle().toString();
            //((TextInputLayout) blood_g.getChildAt(0)).getEditText().setText(selectedText);
            B_G.setText(S_blood_g);
            return true;
        });
        popupMenu.show();
    }
}