package com.example.blooddonation;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.PopupMenu;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class home_page extends AppCompatActivity {

    TextInputLayout Search;
    TextInputEditText E_search;
    Button button,button2;
    ImageView Filter,Menu;
    RecyclerView recyclerView;
    List<DataClass> dataList;
    DatabaseReference databaseReference;
    ValueEventListener eventListener;
    private RecyclerViewAdapter adapter;
    private List<DataClass> itemList;

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
        //recycler views
        recyclerView=findViewById(R.id.recyclerView);
        GridLayoutManager gridLayoutManager=new GridLayoutManager(home_page.this,1);
        recyclerView.setLayoutManager(gridLayoutManager);

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

        E_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {}
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
                showPopupMenuForMenu();
            }
        });


        // Initialize Firebase Realtime Database
        databaseReference = FirebaseDatabase.getInstance().getReference("Donars");


        //Recycler View
        itemList = new ArrayList<>();
        adapter = new RecyclerViewAdapter(itemList);
        recyclerView.setAdapter(adapter);
        /*itemList.add(new DataClass("Item 1", "Description 1","test"));
        itemList.add(new DataClass("Item 2", "Description 2","test"));
        // Add more items as needed
        // Initialize the RecyclerViewAdapter with the list of items*/

        //call method
        fetchDataFromFirebase();

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

    private void showPopupMenuForMenu() {
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

    private void fetchDataFromFirebase() {
        databaseReference.addValueEventListener(new ValueEventListener() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                itemList.clear(); // Clear the list to avoid duplicates

                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    // Parse data from the snapshot
                    String name = snapshot.getKey();    
                    String Blood_g = snapshot.child("Blood Group").getValue(String.class);
                    String phone = snapshot.child("Phone No").getValue(String.class);

                    if (Blood_g.equals("B+"))
                    {
                        DataClass item = new DataClass(name,phone,Blood_g);
                        itemList.add(item);
                    }
                }

                // Notify the adapter that data has changed
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Handle errors
            }
        });
    }

}