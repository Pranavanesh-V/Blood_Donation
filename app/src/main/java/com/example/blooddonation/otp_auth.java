package com.example.blooddonation;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputFilter;
import android.text.InputType;
import android.text.TextWatcher;
import android.text.method.DigitsKeyListener;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.material.textfield.TextInputLayout;
import java.util.ArrayList;

public class otp_auth extends AppCompatActivity {


    private static String S_otp1,S_otp2,S_otp3,S_otp4;

    Button submit;
    TextInputLayout otp1,otp2,otp3,otp4;
    String OTP;
    ArrayList<String> flag=new ArrayList<>(3);
    //String flag[]={"null","null","null","null"};
    ArrayList<String> MESSAGE=new ArrayList<>(3);
    Boolean res=false;
    Boolean ot=false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_otp_auth);

        otp1=findViewById(R.id.otp1);
        otp2=findViewById(R.id.otp2);
        otp3=findViewById(R.id.otp3);
        otp4=findViewById(R.id.otp4);
        submit=findViewById(R.id.submit);

        flag.add("null");
        flag.add("null");
        flag.add("null");
        flag.add("null");

        MESSAGE.add("null");
        MESSAGE.add("null");
        MESSAGE.add("null");
        MESSAGE.add("null");

        Intent intent=getIntent();
        String number=intent.getStringExtra("number");
        OTP=intent.getStringExtra("OTP");
        String O_Username=intent.getStringExtra("U_name");

        //Edit text objects
        EditText E_otp1=otp1.getEditText();
        EditText E_otp2=otp2.getEditText();
        EditText E_otp3=otp3.getEditText();
        EditText E_otp4=otp4.getEditText();

        //otp 1
        assert E_otp1 != null;
        E_otp1.setInputType(InputType.TYPE_CLASS_NUMBER);
        E_otp1.setFilters(new InputFilter[]{new InputFilter.LengthFilter(1)});
        E_otp1.setKeyListener(DigitsKeyListener.getInstance("1234567890"));
        TextWatcher l1=new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                S_otp1=E_otp1.getText().toString().trim();
                if (s.length() == 1) { // Example condition: move focus after 3 characters
                    E_otp2.requestFocus();
                }
            }
            @Override
            public void afterTextChanged(Editable s) {
                if (s.length() > 1) {
                    s.delete(1, s.length());
                }
                if (s.length()==1)
                {
                    flag.set(0,"1");
                    MESSAGE.set(0,S_otp1);
                }
                if (s.length()==0)
                {
                    flag.set(0,"null");
                    MESSAGE.set(0,"null");
                }
            }
        };
        E_otp1.addTextChangedListener(l1);

        //otp2
        assert E_otp2 != null;
        E_otp2.setInputType(InputType.TYPE_CLASS_NUMBER);
        E_otp2.setFilters(new InputFilter[]{new InputFilter.LengthFilter(1)});
        E_otp2.setKeyListener(DigitsKeyListener.getInstance("1234567890"));
        TextWatcher l2=new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                S_otp2=E_otp2.getText().toString().trim();
                if (s.length() == 1) { // Example condition: move focus after 3 characters
                    E_otp3.requestFocus();
                }
            }
            @Override
            public void afterTextChanged(Editable s) {
                if (s.length() > 1) {
                    s.delete(1, s.length());
                }
                if (s.length()==1)
                {
                    flag.set(1,"1");
                    MESSAGE.set(1,S_otp2);
                }
                if (s.length()==0)
                {
                    flag.set(1,"null");
                    MESSAGE.set(1,"null");
                }
            }
        };
        E_otp2.addTextChangedListener(l2);

        //otp3
        assert E_otp3 != null;
        E_otp3.setInputType(InputType.TYPE_CLASS_NUMBER);
        E_otp3.setFilters(new InputFilter[]{new InputFilter.LengthFilter(1)});
        E_otp3.setKeyListener(DigitsKeyListener.getInstance("1234567890"));
        TextWatcher l3=new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                S_otp3=E_otp3.getText().toString().trim();
                if (s.length() == 1) { // Example condition: move focus after 3 characters
                    E_otp4.requestFocus();
                }
            }
            @Override
            public void afterTextChanged(Editable s) {
                if (s.length() > 1) {
                    s.delete(1, s.length());
                }
                if (s.length()==1)
                {
                    flag.set(2,"1");
                    MESSAGE.set(2,S_otp3);
                }
                if (s.length()==0)
                {
                    flag.set(2,"null");
                    MESSAGE.set(2,"null");
                }
            }
        };
        E_otp3.addTextChangedListener(l3);

        //otp4
        assert E_otp4 != null;
        E_otp4.setInputType(InputType.TYPE_CLASS_NUMBER);
        E_otp4.setFilters(new InputFilter[]{new InputFilter.LengthFilter(1)});
        E_otp4.setKeyListener(DigitsKeyListener.getInstance("1234567890"));
        TextWatcher l4=new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                S_otp4=E_otp4.getText().toString().trim();
            }
            @Override
            public void afterTextChanged(Editable s) {
                if (s.length() > 1) {
                    s.delete(1, s.length());
                }
                if (s.length()==1)
                {
                    flag.set(3,"1");
                    MESSAGE.set(3,S_otp4);
                }
                if (s.length()==0)
                {
                    flag.set(3,"null");
                    MESSAGE.set(3,"null");
                }
            }
        };
        E_otp4.setImeOptions(EditorInfo.IME_ACTION_DONE);
        E_otp4.addTextChangedListener(l4);



        submit.setOnClickListener(view -> {

            //setting the flags
            for (String i:flag)
            {
                if (!i.equals("1"))
                {
                    Toast.makeText(otp_auth.this,"Empty Credentials",Toast.LENGTH_SHORT).show();
                    res=false;
                    break;
                }
                else
                {
                    res=true;
                }
            }


            if (res)
            {
                String C_Message="";
                for(String o:MESSAGE)
                {
                    C_Message+=o;
                }
                if (!C_Message.equals(OTP))
                {
                    Toast.makeText(otp_auth.this,"Invalid OTP",Toast.LENGTH_SHORT).show();
                }
                else
                {
                    Toast.makeText(otp_auth.this,"Authentication Successful",Toast.LENGTH_SHORT).show();
                    Intent i=new Intent(otp_auth.this,confirm_password.class);
                    System.out.println(O_Username+" Otp auth");
                    i.putExtra("Otp_username",O_Username);
                    startActivity(i);
                }
            }
        });
    }
}