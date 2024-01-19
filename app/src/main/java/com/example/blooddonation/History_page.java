package com.example.blooddonation;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class History_page extends AppCompatActivity implements History_interface {

    private History_RecyclerViewAdapter adapter;
    public List<History_dataClass> itemList;
    RecyclerView historyREC;
    Button history_back;
    ProgressBar load_history;
    private static final String PREFS_NAME = "MyPrefs";
    private SharedPreferences sharedPreferences;
    String savedUsername;
    DatabaseReference databaseReference;
    @SuppressLint("NotifyDataSetChanged")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history_page);

        historyREC=findViewById(R.id.historyREC);
        history_back=findViewById(R.id.history_back);
        load_history=findViewById(R.id.load_history);

        GridLayoutManager gridLayoutManager=new GridLayoutManager(History_page.this,1);
        historyREC.setLayoutManager(gridLayoutManager);

        sharedPreferences = getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        savedUsername = sharedPreferences.getString("username", "");

        history_back.setOnClickListener(v -> onBackPressed());

        fetch();
    }

    private void fetch() {

        load_history.setVisibility(View.VISIBLE);

        // Initialize Firebase Realtime Database
        databaseReference = FirebaseDatabase.getInstance().getReference("Donars").child(savedUsername).child("History");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                //itemList.clear();
                load_history.setVisibility(View.INVISIBLE);
                itemList = new ArrayList<>();
                adapter = new History_RecyclerViewAdapter(itemList,History_page.this,History_page.this);
                historyREC.setAdapter(adapter);

                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {

                    String Name = snapshot.getKey();
                    String Blood=snapshot.child("Blood Group").getValue(String.class);
                    History_dataClass data = new History_dataClass(Name, Blood);
                    itemList.add(data);

                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
        // Notify the adapter that data has changed
    }


    @Override
    public void ItemOnClick(int position) {

    }
}