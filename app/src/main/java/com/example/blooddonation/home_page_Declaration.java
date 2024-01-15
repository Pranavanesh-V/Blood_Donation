package com.example.blooddonation;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class home_page_Declaration extends AppCompatActivity {

    RadioButton agree,decline;
    Button submit;
    String form;

    //This class is to see if the user is a donor or not
    //If he is a donor then the Registration option is not show
    //Else the option is shown
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page_declaration);

        agree=findViewById(R.id.Agree);
        decline=findViewById(R.id.Decline);
        submit=findViewById(R.id.pro);

        Intent i=getIntent();
        String pass=i.getStringExtra("Password");
        String name=i.getStringExtra("Name");

        submit.setOnClickListener(view -> {
            if (decline.isChecked())
            {
                Intent intent=new Intent(home_page_Declaration.this, home_page_Registration.class);
                form="no";
                intent.putExtra("form",form);
                intent.putExtra("Password",pass);
                intent.putExtra("Name",name);
                startActivity(intent);
                finish();
            }
            else if (agree.isChecked())
            {
                Intent intent=new Intent(home_page_Declaration.this, home_page_Registration.class);
                form="yes";
                intent.putExtra("form",form);
                intent.putExtra("Password",pass);
                intent.putExtra("Name",name);
                startActivity(intent);
                finish();
            }
            else
            {
                Toast.makeText(home_page_Declaration.this, "Choose the option", Toast.LENGTH_SHORT).show();
            }
        });

    }

}