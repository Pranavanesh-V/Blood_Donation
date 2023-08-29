package com.example.blooddonation;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.RadioButton;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.util.Calendar;

public class Full_Registration_page extends AppCompatActivity {


    Calendar myCalendar=Calendar.getInstance();
    Button submit;
    TextInputLayout name, email, blood_g, address, city, state, phone, emergency, dob;//dob not included need to see on that
    RadioButton male, female;
    String selectedDate;

    TextInputEditText E_dob;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_full_registration_page);

        submit = findViewById(R.id.submit);
        dob = findViewById(R.id.Dob);
        name = findViewById(R.id.name);
        email = findViewById(R.id.Mail);
        blood_g = findViewById(R.id.blood_G);
        address = findViewById(R.id.Add1);
        city = findViewById(R.id.city);
        state = findViewById(R.id.state);
        phone = findViewById(R.id.Phone_no);
        emergency = findViewById(R.id.Emergency);
        E_dob=findViewById(R.id.E_dob);


        E_dob.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDatePicker();
            }
        });

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Date is working
                Toast.makeText(Full_Registration_page.this,selectedDate,Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void showDatePicker() {
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(Full_Registration_page.this,
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                         selectedDate= dayOfMonth + "/" + (monthOfYear + 1) + "/" + year;
                        E_dob.setText(selectedDate);
                    }
                }, year, month, day);

        datePickerDialog.show();
    }

}