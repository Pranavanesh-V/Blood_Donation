package com.example.blooddonation;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

public class Login_page extends AppCompatActivity {

    TextView register,signup;
    private SharedPreferences sharedPreferences;
    private static final String PREFS_NAME = "MyPrefs";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_page);

        Log.d("page","Login page which displays options to register new user and sign in existing user");

        register=findViewById(R.id.register);
        signup=findViewById(R.id.sign_up);

        if (!isNetworkConnected(this)) {
            showNoInternetDialog(this);
            Log.d("error","No internet access available");
        }

        if (isUserLoggedIn()) {
            // User is already logged in, navigate to the main activity
            startActivity(new Intent(Login_page.this, home_page.class));
            finish(); // Finish the LoginActivity to prevent going back to it
        }

        //Existing User Then Sign In
        signup.setOnClickListener(view -> {

            Intent intent=new Intent(Login_page.this, sign_in.class);
            startActivity(intent);

        });

        //If new user is using
        //Then Register option is clicked
        register.setOnClickListener(view -> {

            Intent intent=new Intent(Login_page.this, Register.class);
            startActivity(intent);

        });

    }
    public static boolean isNetworkConnected(Context context) {
        ConnectivityManager connectivityManager =
                (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivityManager != null) {
            NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
            return networkInfo != null && networkInfo.isConnected();
        }
        return false;
    }

    public void showNoInternetDialog(Context context) {
        new AlertDialog.Builder(context)
                .setTitle("No Internet Connection")
                .setMessage("Please check your internet connection and try again.")
                .setPositiveButton("OK", (dialog, which) -> {
                    dialog.dismiss();
                    onBackPressed();
                })
                .show();
    }

    //If User Cancels Close the Application
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finishAffinity();
    }

    //See if the user is logged in or not
    private boolean isUserLoggedIn() {
        sharedPreferences = getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        String savedUsername = sharedPreferences.getString("username", "");
        String savedPassword = sharedPreferences.getString("password", "");

        return !savedUsername.isEmpty() && !savedPassword.isEmpty();
    }

}