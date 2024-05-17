package com.example.blooddonation;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class Declaration_page extends AppCompatActivity {

    RadioButton agree,decline;
    Button submit;
    String form;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_declaration_page);

        agree=findViewById(R.id.Agree);
        decline=findViewById(R.id.Decline);
        submit=findViewById(R.id.pro);

        Log.d("page","Declaration page after the user registration");

        //Get the password and username from the intent
        Intent i=getIntent();
        String pass=i.getStringExtra("Password");
        String name=i.getStringExtra("Name");

        submit.setOnClickListener(view -> {
            //check if the user has checked the form and accepted the declaration
            Log.d("output","See if the user has accepted the declaration");
            if (decline.isChecked())
            {
                Intent intent=new Intent(Declaration_page.this, Full_Registration_page.class);
                form="no";
                intent.putExtra("form",form);
                intent.putExtra("Password",pass);
                intent.putExtra("Name",name);
                startActivity(intent);
            }
            else if (agree.isChecked())
            {
                Intent intent=new Intent(Declaration_page.this, Full_Registration_page.class);
                form="yes";
                intent.putExtra("form",form);
                intent.putExtra("Password",pass);
                intent.putExtra("Name",name);
                startActivity(intent);
            }
            else
            {
                Toast.makeText(Declaration_page.this, "Choose the option", Toast.LENGTH_SHORT).show();
            }
        });
    }
}