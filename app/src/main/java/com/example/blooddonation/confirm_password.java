package com.example.blooddonation;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextWatcher;
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

public class confirm_password extends AppCompatActivity {

    Button submit;
    TextInputLayout confirm_pass, new_pass,user_name;
    String S_new_pass,S_conf_pass,S_username;
    Boolean oP,Cp;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirm_password);

        submit=findViewById(R.id.submit);
        user_name=findViewById(R.id.user_name);
        confirm_pass=findViewById(R.id.confirm_pass);
        new_pass=findViewById(R.id.new_pass);


        EditText E_pass_new= new_pass.getEditText();
        assert E_pass_new != null;
        E_pass_new.setFilters(new InputFilter[]{new InputFilter.LengthFilter(10)});
        //E_pass_new.setKeyListener(DigitsKeyListener.getInstance("1234567890"));
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


        //confirm password
        EditText E_conf_pass= confirm_pass.getEditText();
        assert E_conf_pass != null;
        E_conf_pass.setFilters(new InputFilter[]{new InputFilter.LengthFilter(10)});
        //E_conf_pass.setKeyListener(DigitsKeyListener.getInstance("1234567890"));
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


        //Get username
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


        submit.setOnClickListener(view -> {

            // Initialize Firebase
            DatabaseReference reference= FirebaseDatabase.getInstance().getReference().child("Donars");
            reference.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot datasnapshot) {

                    if (datasnapshot.child(S_username).exists())
                    {
                        if (S_new_pass.equals(S_conf_pass)) {
                            // Replace data using set value method
                            reference.child(S_username).child("Password").setValue(S_conf_pass);
                            //Message confirmation
                            Toast.makeText(confirm_password.this,"Password Updated",Toast.LENGTH_SHORT).show();
                            //To sign in page
                            Intent intent=new Intent(confirm_password.this, sign_up.class);
                            startActivity(intent);
                            finishAffinity();
                        }
                        else {
                            Toast.makeText(confirm_password.this,"Password MisMatch",Toast.LENGTH_SHORT).show();
                        }
                    }
                    else
                    {
                        Toast.makeText(confirm_password.this,"User Not found",Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        });


    }
}