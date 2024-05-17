package com.example.blooddonation;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.PopupMenu;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

public class Filter_page extends AppCompatActivity {

    TextInputLayout Gender,Blood_group,Age;
    String S_Gender,S_Blood_group,S_Age;
    TextInputEditText E_Gender,E_Age,E_B_G;
    Button Done;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_filter_page);

        Log.d("page","Filter page for filtering the list of donors is viewed");

        Gender=findViewById(R.id.Gender);
        E_Gender=findViewById(R.id.E_Gender);
        //blood group
        Blood_group=findViewById(R.id.Blood_group);
        E_B_G=findViewById(R.id.E_bg);
        //Age
        Age=findViewById(R.id.Age);
        E_Age=findViewById(R.id.E_age);
        //Done button
        Done=findViewById(R.id.Done);

        E_B_G.setOnClickListener(view -> showPopupMenu());

        E_Gender.setOnClickListener(view -> showPopupMenuForGender());

        E_Age.setOnClickListener(view -> showPopupMenuForAge());

        //When done is clicked the Data is transferred to the previous activity
        Done.setOnClickListener(view -> {
            Intent intent=new Intent();
            intent.putExtra("Age",S_Age);
            intent.putExtra("Gender",S_Gender);
            intent.putExtra("Blood_group",S_Blood_group);
            setResult(RESULT_OK,intent);
            finish();
        });

    }

    //For blood group
    private void showPopupMenu() {
        PopupMenu popupMenu = new PopupMenu(this,Blood_group);
        popupMenu.getMenu().add("None");
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
            S_Blood_group = item.getTitle().toString();
            if (S_Blood_group.equals("None"))
            {
                E_B_G.setText("");
                S_Blood_group=null;
            }
            else {
                E_B_G.setText(S_Blood_group);
            }
            return true;
        });
        popupMenu.show();
    }

    //For Age
    private void showPopupMenuForAge() {
        PopupMenu popupMenu = new PopupMenu(this, Age);
        popupMenu.getMenu().add("None");
        popupMenu.getMenu().add("18-24");
        popupMenu.getMenu().add("25-34");
        popupMenu.getMenu().add("35-44");
        popupMenu.getMenu().add("45-54");
        popupMenu.getMenu().add("55 Above");

        // Set an item click listener for the PopupMenu
        popupMenu.setOnMenuItemClickListener(item -> {
            // Handle item selection here
            S_Age = item.getTitle().toString();
            if (S_Age.equals("None"))
            {
                E_Age.setText("");
                S_Age=null;
            }
            else {
                E_Age.setText(S_Age);
            }
            return true;
        });
        popupMenu.show();
    }
    //For Gender
    private void showPopupMenuForGender() {
        PopupMenu popupMenu = new PopupMenu(this, Age);
        popupMenu.getMenu().add("None");
        popupMenu.getMenu().add("Male");
        popupMenu.getMenu().add("Female");

        // Set an item click listener for the PopupMenu
        popupMenu.setOnMenuItemClickListener(item -> {
            // Handle item selection here
            S_Gender = item.getTitle().toString();
            if (S_Gender.equals("None"))
            {
                E_Gender.setText("");
                S_Gender=null;
            }
            else {
                E_Gender.setText(S_Gender);
            }
            return true;
        });
        popupMenu.show();
    }
}