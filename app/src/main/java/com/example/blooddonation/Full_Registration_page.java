package com.example.blooddonation;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.PopupMenu;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.util.Calendar;

public class Full_Registration_page extends AppCompatActivity {


    Calendar myCalendar=Calendar.getInstance();
    Button submit;
    TextInputLayout name, email, blood_g, address, city, state, phone, emergency, dob;//dob not included need to see on that
    RadioButton male, female;
    String S_DOB,S_name,S_email,S_blood_g,S_address,S_city,S_state,S_phone,S_emergency,S_gender,form,Password,Name;
    TextInputEditText E_dob,B_G;
    RadioGroup group;
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
        group=findViewById(R.id.Group);
        male=findViewById(R.id.male);
        female=findViewById(R.id.female);
        E_dob=findViewById(R.id.E_dob);
        B_G=findViewById(R.id.B_g);
        B_G.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showPopupMenu();
            }
        });
        E_dob.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDatePicker();
            }
        });

        group.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                // Check which radio button is selected
                RadioButton selectedRadioButton = findViewById(i);

                if (selectedRadioButton != null) {
                    S_gender= selectedRadioButton.getText().toString();
                }
            }
        });

        EditText E_name=name.getEditText();
        EditText E_email=email.getEditText();
        EditText E_Address=address.getEditText();
        EditText E_city=city.getEditText();
        EditText E_state=state.getEditText();
        EditText E_phone=phone.getEditText();
        EditText E_emergency=emergency.getEditText();
        assert E_name != null;
        assert E_email != null;
        assert E_Address != null;
        assert E_city != null;
        assert E_state != null;
        assert E_phone != null;
        assert E_emergency != null;
        TextWatcher login=new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                S_name=E_name.getText().toString().trim();
                S_email=E_email.getText().toString().trim();
                S_address=E_Address.getText().toString().trim();
                S_city=E_city.getText().toString().trim();
                S_state=E_state.getText().toString().trim();
                S_phone=E_phone.getText().toString().trim();
                S_emergency=E_emergency.getText().toString().trim();
            }
            @Override
            public void afterTextChanged(Editable s) {}
        };
        E_email.addTextChangedListener(login);
        E_name.addTextChangedListener(login);
        E_Address.addTextChangedListener(login);
        E_city.addTextChangedListener(login);
        E_state.addTextChangedListener(login);
        E_phone.addTextChangedListener(login);
        E_emergency.addTextChangedListener(login);



        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i=getIntent();
                form=i.getStringExtra("form");
                Password=i.getStringExtra("Password");
                Name=i.getStringExtra("Name");
                System.out.println(S_name+"\n"+S_email+"\n"+S_DOB+"\n"+S_blood_g+"\n"+S_address+"\n"+S_city+"\n"+S_state+"\n"+S_phone+"\n"+S_emergency+"\n"+S_gender+"\n"+form+"\n"+Name+"\n"+Password);
                Intent intent=new Intent(Full_Registration_page.this, home_page.class);
                startActivity(intent);
            }
        });
        //insert the data's into firebase database


    }
    //For Date
    private void showDatePicker() {
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(Full_Registration_page.this,
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        S_DOB = dayOfMonth + "/" + (monthOfYear + 1) + "/" + year;
                        E_dob.setText(S_DOB);
                    }
                }, year, month, day);
        datePickerDialog.show();
    }

    //For Blood_g
    private void showPopupMenu() {
        PopupMenu popupMenu = new PopupMenu(this, blood_g);
        popupMenu.getMenu().add("O+");
        popupMenu.getMenu().add("O-");
        popupMenu.getMenu().add("A+");
        popupMenu.getMenu().add("A-");
        popupMenu.getMenu().add("B+");
        popupMenu.getMenu().add("B-");
        popupMenu.getMenu().add("AB+");
        popupMenu.getMenu().add("Ab-");

        // Set an item click listener for the PopupMenu
        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                // Handle item selection here
                S_blood_g = item.getTitle().toString();
                //((TextInputLayout) blood_g.getChildAt(0)).getEditText().setText(selectedText);
                B_G.setText(S_blood_g);
                return true;
            }
        });
        popupMenu.show();
    }
}