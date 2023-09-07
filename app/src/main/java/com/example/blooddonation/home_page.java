package com.example.blooddonation;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.PopupMenu;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

public class home_page extends AppCompatActivity {

    TextInputLayout Search;
    TextInputEditText E_search;
    Button button,button2;
    ImageView Filter,Menu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page);

        button=findViewById(R.id.button);
        button2=findViewById(R.id.button2);
        Filter=findViewById(R.id.Filter);
        Menu=findViewById(R.id.Menu);
        Search=findViewById(R.id.Search);
        E_search=findViewById(R.id.E_search);


        button.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("UseCompatLoadingForDrawables")
            @Override
            public void onClick(View view) {
                button.setBackground(getDrawable(R.drawable.cus_join11));
                button2.setBackground(getDrawable(R.drawable.cus_join2));
            }
        });
        button2.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("UseCompatLoadingForDrawables")
            @Override
            public void onClick(View view) {
                button.setBackground(getDrawable(R.drawable.cus_join1));
                button2.setBackground(getDrawable(R.drawable.cus_join22));
            }
        });
        Filter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(home_page.this, Filter_page.class);
                startActivityForResult(intent,1);
            }
        });
        Menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Create the floating window
                //Menu.isFocused()
                showPopupMenuForGender();
            }
        });


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode==1)
        {
            if (resultCode==RESULT_OK)
            {
                String Age=data.getStringExtra("Age");
                String Blood_G=data.getStringExtra("Blood_group");
                String Gender=data.getStringExtra("Gender");
                E_search.setText(Gender);
            }
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finishAffinity();
    }

    private void showPopupMenuForGender() {
        PopupMenu popupMenu = new PopupMenu(this, Menu);
        popupMenu.getMenu().add("Profile");
        popupMenu.getMenu().add("Personalisation");
        popupMenu.getMenu().add("Security");
        popupMenu.getMenu().add("About");


        // Set an item click listener for the PopupMenu
        popupMenu.setOnMenuItemClickListener(item -> {
            // Handle item selection here
            return true;
        });
        popupMenu.show();
    }
}