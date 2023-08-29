package com.example.blooddonation;

import android.content.Intent;
import android.os.Bundle;
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

        submit.setOnClickListener(view -> {
            if (decline.isChecked())
            {
                Intent intent=new Intent(Declaration_page.this, Full_Registration_page.class);
                form="no";
                intent.putExtra("form",form);
                startActivity(intent);
            }
            else if (agree.isChecked())
            {
                Intent intent=new Intent(Declaration_page.this, Full_Registration_page.class);
                form="yes";
                intent.putExtra("form",form);
                startActivity(intent);
            }
            else
            {
                Toast.makeText(Declaration_page.this, "Choose the option", Toast.LENGTH_SHORT).show();
            }
        });

    }

}