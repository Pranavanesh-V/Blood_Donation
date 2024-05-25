package com.example.blooddonation;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.text.Editable;
import android.text.InputFilter;
import android.text.InputType;
import android.text.TextWatcher;
import android.text.method.DigitsKeyListener;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class forgot_page extends AppCompatActivity {

    TextInputLayout phone;
    String S_phone;
    private static final int SMS_PERMISSION_REQUEST_CODE = 1;
    Button submit;
    boolean flag;
    private static String MESSAGE;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_page);

        Log.d("page","forgot page is viewed for changing the password");

        phone=findViewById(R.id.phone);
        submit=findViewById(R.id.submit);

        //get the otp here and send it
        otp_generator otp_generator=new otp_generator();
        MESSAGE = otp_generator.generate();
        //get the username from the previous page for later use
        Intent intent=getIntent();
        String username=intent.getStringExtra("Username");

        //Edittext to get the string value of the number of the user
        EditText E_phone=phone.getEditText();
        assert E_phone != null;
        E_phone.setInputType(InputType.TYPE_CLASS_NUMBER);
        E_phone.setFilters(new InputFilter[]{new InputFilter.LengthFilter(10)});
        E_phone.setKeyListener(DigitsKeyListener.getInstance("1234567890"));
        TextWatcher login=new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                S_phone=E_phone.getText().toString().trim();
            }

            @Override
            public void afterTextChanged(Editable s) {
                //To make user the number is not greater or less than 10 digits
                if (s.length() > 10) {
                    s.delete(10, s.length());
                }
                if (s.length()==10)
                {
                    submit.setEnabled(true);
                }
                if (s.length()<10)
                {
                    submit.setEnabled(false);
                }
            }
        };
        E_phone.addTextChangedListener(login);

        flag=false;
        // Check SMS permission
        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.SEND_SMS) == PackageManager.PERMISSION_GRANTED) {
            // Permission already granted, send SMS
            flag=true;
        } else {
            // Request SMS permission
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.SEND_SMS}, SMS_PERMISSION_REQUEST_CODE);
        }

        //Button check if the number is of the respective user
        //And sends the otp to that number and navigates to the next activity
        submit.setOnClickListener(view -> {

            //Reference of the database
            DatabaseReference reference= FirebaseDatabase.getInstance().getReference().child("Donars");
            reference.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot datasnapshot) {
                    //Phone number written in the database
                    String num=datasnapshot.child(username).child("Phone No").getValue(String.class);

                    //check if the number is same
                    //if same send the otp
                    if (num.equals(S_phone))
                    {
                        //send otp in sms

                        if (flag) {
                            sendSms();
                            Intent intent1 = new Intent(forgot_page.this, otp_auth.class);
                            intent1.putExtra("number", S_phone);
                            intent1.putExtra("OTP", MESSAGE);
                            intent1.putExtra("U_name", username);
                            startActivity(intent1);
                        }
                        else
                        {
                            Toast.makeText(forgot_page.this, "Provide sms permission", Toast.LENGTH_SHORT).show();
                        }
                    }
                    else
                    {
                        //Invalid number so remove the number written in the TextInputLayout
                        E_phone.setText("");
                        Toast.makeText(forgot_page.this, "Invalid Number", Toast.LENGTH_SHORT).show();
                    }

                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                    //Error Message
                    Log.d("output","error while fetching data from database");
                    Toast.makeText(forgot_page.this, "Password Change Failed", Toast.LENGTH_SHORT).show();
                    finish();

                }
            });

        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == SMS_PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission granted, send SMS
            } else {
                // Permission denied, show a message or handle the situation
                Toast.makeText(this, "SMS permission denied", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void sendSms()
    {
        //every time this function is called a new otp is generated
        SmsManager smsManager = SmsManager.getDefault();
        smsManager.sendTextMessage(S_phone, null, MESSAGE, null, null);
        Toast.makeText(this, "SMS sent successfully", Toast.LENGTH_SHORT).show();
        Log.d("output","Otp is "+MESSAGE);
    }
}