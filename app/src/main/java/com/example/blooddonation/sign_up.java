package com.example.blooddonation;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
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

public class sign_up extends AppCompatActivity {

    Button submit;
    private SharedPreferences sharedPreferences;
    private static final String PREFS_NAME = "MyPrefs";
    TextInputLayout email_id,password;
    String S_Username,S_password;
    int time = 0;


    TextView forgot;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        submit=findViewById(R.id.submit);
        email_id=findViewById(R.id.mail_id);
        password=findViewById(R.id.password);
        forgot=findViewById(R.id.forgot);

        initSharedPreferences();


        EditText E_email_id=email_id.getEditText();
        EditText E_password=password.getEditText();
        assert E_email_id != null;
        assert E_password != null;
        TextWatcher login=new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                S_Username =E_email_id.getText().toString().trim();
                S_password=E_password.getText().toString().trim();
            }

            @Override
            public void afterTextChanged(Editable s) {}

        };
        E_password.addTextChangedListener(login);
        E_email_id.addTextChangedListener(login);


        submit.setOnClickListener(view -> {

            if (E_email_id.getText().toString().trim().isEmpty() || E_password.getText().toString().trim().isEmpty())
            {
                Toast.makeText(sign_up.this,"Empty Credentials",Toast.LENGTH_SHORT).show();
            }
            else
            {

                DatabaseReference reference= FirebaseDatabase.getInstance().getReference().child("Donars");
                reference.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot datasnapshot) {

                        String S_password1;
                        if (datasnapshot.child(S_Username).exists())
                        {
                            S_password1 = datasnapshot.child(S_Username).child("Password").getValue(String.class);
                            if (S_password1.equals(S_password))
                            {
                                SharedPreferences.Editor editor = sharedPreferences.edit();
                                editor.putString("username", S_Username);
                                editor.putString("password", S_password1);
                                String val;
                                if (datasnapshot.child(S_Username).child("Form").exists())
                                {
                                    val="yes";
                                }
                                else
                                {
                                    val="no";
                                }
                                editor.putString("Register", val).apply();
                                editor.apply();
                                Intent intent=new Intent(sign_up.this, home_page.class);
                                intent.putExtra("username", S_Username);
                                startActivity(intent);
                                finish();
                            }
                            else
                            {
                                E_password.setText("");
                                if (time==3)
                                {
                                    Toast.makeText(sign_up.this,"\tTry \nForgot password",Toast.LENGTH_SHORT).show();
                                    time=0;
                                }
                                else {
                                    Toast.makeText(sign_up.this,"InCorrect Password",Toast.LENGTH_SHORT).show();
                                    time+=1;
                                }
                            }
                        }
                        else
                        {
                            Toast.makeText(sign_up.this,"\tNew User \n Create Account",Toast.LENGTH_SHORT).show();
                            E_email_id.setText("");
                            finish();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            }
        });

        forgot.setOnClickListener(view -> {
            if (!Objects.isNull(S_Username)) {
                Intent intent = new Intent(sign_up.this, forgot_page.class);
                System.out.println(S_Username+" sign in page");
                intent.putExtra("Username", S_Username);
                startActivity(intent);
            }
            else
            {
                Toast.makeText(this, "Username Required", Toast.LENGTH_SHORT).show();
            }
        });





    }
    private void initSharedPreferences() {
        sharedPreferences = getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
    }

}