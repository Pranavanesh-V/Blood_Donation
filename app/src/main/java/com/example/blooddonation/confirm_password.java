package com.example.blooddonation;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextWatcher;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Objects;

public class confirm_password extends AppCompatActivity {
    Button submit;
    TextInputLayout confirm_pass, new_pass,user_name;
    String S_new_pass,S_conf_pass,S_username;
    Boolean oP,Cp;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirm_password);

        Log.d("page","Password confirmation page is viewed");

        submit=findViewById(R.id.submit);
        user_name=findViewById(R.id.user_name);
        confirm_pass=findViewById(R.id.confirm_pass);
        new_pass=findViewById(R.id.new_pass);

        //Get the Username of the user
        Intent intent=getIntent();
        String Username=intent.getStringExtra("Otp_username");

        //Assign the edittext to get the string value of the new password
        //This is for New Password
        EditText E_pass_new= new_pass.getEditText();
        assert E_pass_new != null;
        E_pass_new.setFilters(new InputFilter[]{new InputFilter.LengthFilter(10)});
        TextWatcher login=new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                S_new_pass =E_pass_new.getText().toString().trim();
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.length() > 10) {
                    s.delete(10, s.length());
                }
            }
        };
        E_pass_new.addTextChangedListener(login);

        //Assign the edittext to get the string value of the new password
        //This is for Confirm Password
        EditText E_conf_pass= confirm_pass.getEditText();
        assert E_conf_pass != null;
        E_conf_pass.setFilters(new InputFilter[]{new InputFilter.LengthFilter(10)});
        TextWatcher login2=new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                S_conf_pass=E_conf_pass.getText().toString().trim();
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.length() > 10) {
                    s.delete(10, s.length());
                }
            }
        };
        E_conf_pass.addTextChangedListener(login2);

        //Assign the edittext to get the string value of the Username
        //Get Username
        EditText E_Username= user_name.getEditText();
        assert E_Username != null;
        TextWatcher login3=new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                S_username=E_Username.getText().toString().trim();
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        };
        E_Username.addTextChangedListener(login3);

        //This submit button is used to change the password
        submit.setOnClickListener(view -> {
            // Initialize Firebase reference
            DatabaseReference reference= FirebaseDatabase.getInstance().getReference().child("Donars");
            reference.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot datasnapshot) {

                    //Check if the values or null or not
                    if (!(Objects.isNull(S_username) || Objects.isNull(S_conf_pass) || Objects.isNull(S_new_pass))) {

                        //Check if the user exists and the username is correct
                        if (datasnapshot.child(S_username).exists()) {

                            if (S_username.equals(Username)) {
                                //The new password and confirm password must be same
                                if (S_new_pass.equals(S_conf_pass)) {

                                    // Replace data using set value method
                                    reference.child(S_username).child("Password").setValue(S_conf_pass);

                                    //Message confirmation
                                    Toast.makeText(confirm_password.this, "Password Updated", Toast.LENGTH_SHORT).show();

                                    //To sign in page
                                    Intent intent = new Intent(confirm_password.this, sign_in.class);
                                    startActivity(intent);
                                    finishAffinity();

                                }else {
                                    //Password mismatch message
                                    Log.d("output","Password doesn't Mismatch with the user's credentials");
                                    Toast.makeText(confirm_password.this, "Password MisMatch", Toast.LENGTH_SHORT).show();
                                }
                            }else {
                                //User not found message
                                Log.d("output","Not correct username provided");
                                Toast.makeText(confirm_password.this, "Enter respective username", Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            //User not found message
                            Log.d("output","The User is not found");
                            Toast.makeText(confirm_password.this, "User not found", Toast.LENGTH_SHORT).show();
                        }
                    }
                    else
                    {
                        //Empty credentials
                        Log.d("output","Credentials are not provided");
                        Toast.makeText(confirm_password.this, "Empty Credentials", Toast.LENGTH_SHORT).show();
                    }
                }
                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                    //if the process is canceled
                    Log.d("output","Process is canceled and the password is not changed");
                    Toast.makeText(confirm_password.this, "Password Not Changed", Toast.LENGTH_SHORT).show();
                    Intent intent1=new Intent(confirm_password.this, sign_in.class);
                    startActivity(intent1);
                    finish();

                }
            });
        });
    }
}