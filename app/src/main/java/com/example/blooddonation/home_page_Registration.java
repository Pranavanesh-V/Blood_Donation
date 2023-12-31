package com.example.blooddonation;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.Button;
import android.widget.EditText;
import android.widget.PopupMenu;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.Timestamp;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class home_page_Registration extends AppCompatActivity {

    private SharedPreferences sharedPreferences;
    private static final String PREFS_NAME = "MyPrefs";
    Calendar myCalendar=Calendar.getInstance();
    Button submit;
    TextInputLayout name, email, blood_g, address, city, state, phone, emergency, dob;//dob not included need to see on that
    RadioButton male, female;
    String S_DOB="",S_name="",S_email="",S_blood_g="",S_address="",S_city="",S_state="",S_phone="",S_emergency="",S_gender="",form="",Password="",Name="";
    TextInputEditText E_dob,B_G;
    RadioGroup group;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page_registration);

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
        B_G.setOnClickListener(view -> showPopupMenu());
        E_dob.setOnClickListener(view -> showDatePicker());

        group.setOnCheckedChangeListener((radioGroup, i) -> {
            // Check which radio button is selected
            RadioButton selectedRadioButton = findViewById(i);

            if (selectedRadioButton != null) {
                S_gender= selectedRadioButton.getText().toString();
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



        submit.setOnClickListener(view -> {
            Intent i=getIntent();
            form=i.getStringExtra("form");
            sharedPreferences = getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
            Name = sharedPreferences.getString("username", "");
            Password = sharedPreferences.getString("password","");

            //insert it into database
            if (S_name.isEmpty() || S_email.isEmpty() || S_DOB.isEmpty() || S_blood_g.isEmpty() || S_address.isEmpty() || S_city.isEmpty() || S_state.isEmpty() || S_phone.isEmpty() || S_emergency.isEmpty() || S_gender.isEmpty())
            {
                Toast.makeText(home_page_Registration.this,"Empty Credentials",Toast.LENGTH_SHORT).show();
            }
            else
            {

                Timestamp firebaseTimestamp = Timestamp.now();

                // Convert Firebase Timestamp to java.util.Date
                Date date = firebaseTimestamp.toDate();

                // Add 8 hours to the time
                Calendar calendar = Calendar.getInstance();
                calendar.setTime(date);
                calendar.add(Calendar.HOUR_OF_DAY, 24);

                // Convert the updated time back to a Date
                Date updatedDate = calendar.getTime();

                // Create a formatter for a readable date and time format
                @SuppressLint("SimpleDateFormat") SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                // Format and display the original and updated timestamps
                String formattedOriginalTimestamp = formatter.format(date);
                String formattedUpdatedTimestamp = formatter.format(updatedDate);

                DatabaseReference reference= FirebaseDatabase.getInstance().getReference().child("Donars");
                reference.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot datasnapshot) {
                        // Push data to a new unique key
                        reference.child(Name).child("User name").setValue(S_name);
                        reference.child(Name).child("Password").setValue(Password);
                        reference.child(Name).child("Email").setValue(S_email);
                        reference.child(Name).child("DOB").setValue(S_DOB);
                        reference.child(Name).child("Blood Group").setValue(S_blood_g);
                        reference.child(Name).child("Address").setValue(S_address);
                        reference.child(Name).child("City").setValue(S_city);
                        reference.child(Name).child("State").setValue(S_state);
                        reference.child(Name).child("Phone No").setValue(S_phone);
                        reference.child(Name).child("Emergency").setValue(S_emergency);
                        reference.child(Name).child("Gender").setValue(S_gender);
                        reference.child(Name).child("Form").setValue(form);
                        reference.child(Name).child("Profile").setValue("No");
                        reference.child(Name).child("Profile Value").setValue("No");
                        reference.child(Name).child("Time uploaded").setValue(formattedOriginalTimestamp);
                        reference.child(Name).child("Time Remove").setValue(formattedUpdatedTimestamp);

                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.putString("Register", "yes").apply();
                        editor.apply();

                        //display it
                        Toast.makeText(home_page_Registration.this, "Updated Successfully", Toast.LENGTH_SHORT).show();
                        //Navigate to login page
                        finish();

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        //display it
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.putString("Register", "no").apply();
                        editor.apply();
                        Toast.makeText(home_page_Registration.this, "Updated UnSuccessfully", Toast.LENGTH_SHORT).show();
                        finish();
                    }
                });
            }
            System.out.println(S_name+"\n"+S_email+"\n"+S_DOB+"\n"+S_blood_g+"\n"+S_address+"\n"+S_city+"\n"+S_state+"\n"+S_phone+"\n"+S_emergency+"\n"+S_gender+"\n"+form+"\n"+Name+"\n"+Password);

        });
        //insert the data's into firebase database


    }
    //For Date
    private void showDatePicker() {
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(home_page_Registration.this,
                (view, year1, monthOfYear, dayOfMonth) -> {
                    S_DOB = year1 + "/" + (monthOfYear + 1) + "/" + dayOfMonth;
                    E_dob.setText(S_DOB);
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
        popupMenu.setOnMenuItemClickListener(item -> {
            // Handle item selection here
            S_blood_g = item.getTitle().toString();
            //((TextInputLayout) blood_g.getChildAt(0)).getEditText().setText(selectedText);
            B_G.setText(S_blood_g);
            return true;
        });
        popupMenu.show();
    }
}