package com.example.blooddonation;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

public class Login_page extends AppCompatActivity {

    TextView register,signup;
    private SharedPreferences sharedPreferences;
    private static final String PREFS_NAME = "MyPrefs";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_page);

        register=findViewById(R.id.register);
        signup=findViewById(R.id.sign_up);

        if (isUserLoggedIn()) {
            // User is already logged in, navigate to the main activity
            startActivity(new Intent(Login_page.this, home_page.class));
            finish(); // Finish the LoginActivity to prevent going back to it
        }

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

    private boolean isUserLoggedIn() {
        sharedPreferences = getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        String savedUsername = sharedPreferences.getString("username", "");
        String savedPassword = sharedPreferences.getString("password", "");

        return !savedUsername.isEmpty() && !savedPassword.isEmpty();
    }



}