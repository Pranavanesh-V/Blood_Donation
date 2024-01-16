package com.example.blooddonation;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Register extends AppCompatActivity {

    //This class is for the user who is not willing to become a donor
    Button submit;
    TextInputLayout name,email_id,password,conf_pass;
    @SuppressLint("UseSwitchCompatOrMaterialCode")
    Switch yes_or_no;
    String S_email_id,S_password,S_name,S_conf_pass;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        submit = findViewById(R.id.submit);
        email_id = findViewById(R.id.mail_id);
        password = findViewById(R.id.password);
        name=findViewById(R.id.name);
        conf_pass=findViewById(R.id.conf_pass);
        yes_or_no=findViewById(R.id.yes_or_no);

        EditText E_email_id = email_id.getEditText();
        EditText E_password = password.getEditText();
        EditText E_name=name.getEditText();
        EditText E_conf_pass=conf_pass.getEditText();
        assert E_email_id != null;
        assert E_password != null;
        TextWatcher login = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                S_name=E_name.getText().toString().trim();
                S_email_id = E_email_id.getText().toString().trim();
                S_password = E_password.getText().toString().trim();
                S_conf_pass=E_conf_pass.getText().toString().trim();

            }

            @Override
            public void afterTextChanged(Editable s) {

            }

        };
        E_password.addTextChangedListener(login);
        E_email_id.addTextChangedListener(login);
        E_name.addTextChangedListener(login);
        E_conf_pass.addTextChangedListener(login);

        submit.setOnClickListener(view -> {

            //Gets the details and checks if empty or not to create account
            if (E_email_id.getText().toString().trim().isEmpty() || E_password.getText().toString().trim().isEmpty() || E_conf_pass.getText().toString().trim().isEmpty() || E_name.getText().toString().trim().isEmpty()) {
                Toast.makeText(Register.this, "Empty Credentials", Toast.LENGTH_SHORT).show();
            }
            else {
                if (S_conf_pass.equals(S_password))
                {
                    //For person who wishes to become a Donor
                    if (yes_or_no.isChecked())
                    {
                        //Make database reference to firebase
                        DatabaseReference reference= FirebaseDatabase.getInstance().getReference().child("Donars");
                        reference.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot datasnapshot) {

                                //Check if the user already exists
                                if (datasnapshot.child(S_name).exists())
                                {
                                    Toast.makeText(Register.this,"\t\tThe user \n Already Exists",Toast.LENGTH_SHORT).show();
                                }
                                else
                                {
                                    //data must be passed here to next activity
                                    //putExtra method to be used
                                    Intent intent=new Intent(Register.this, Declaration_page.class);
                                    intent.putExtra("Password",S_password);
                                    intent.putExtra("Name",S_name);
                                    startActivity(intent);
                                }
                            }
                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {
                            }
                        });
                    }
                    //This is for person who doesn't want to become a donor
                    else
                    {
                        //Make database reference to firebase
                        DatabaseReference reference= FirebaseDatabase.getInstance().getReference().child("Donars");
                        reference.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot datasnapshot) {

                                if (datasnapshot.child(S_name).exists())
                                {
                                    Toast.makeText(Register.this,"\t\tThe user \n Already Exists",Toast.LENGTH_SHORT).show();
                                }
                                else
                                {
                                    // Push data to a new unique key
                                    reference.child(S_name).child("Password").setValue(S_password);
                                    reference.child(S_name).child("Email").setValue(S_email_id);
                                    reference.child(S_name).child("Profile").setValue("No");
                                    reference.child(S_name).child("Profile Value").setValue("No");
                                    Toast.makeText(Register.this,"Account Created",Toast.LENGTH_SHORT).show();

                                    Intent intent=new Intent(Register.this, Login_page.class);
                                    startActivity(intent);

                                }
                            }
                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });
                    }
                }
                else {
                    Toast.makeText(Register.this,"Password MisMatch",Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}