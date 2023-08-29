package com.example.blooddonation;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

public class Login_page extends AppCompatActivity {

    TextView register,signup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_page);

        register=findViewById(R.id.register);
        signup=findViewById(R.id.sign_up);

        signup.setOnClickListener(view -> {

            Intent intent=new Intent(Login_page.this, sign_up.class);
            startActivity(intent);

        });

        register.setOnClickListener(view -> {

            Intent intent=new Intent(Login_page.this, Register.class);
            startActivity(intent);

        });


    }
    @Override
    public void onBackPressed() {
        finishAffinity();
        System.exit(0);
    }



}