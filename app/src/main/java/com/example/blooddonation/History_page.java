package com.example.blooddonation;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
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
    ImageView empty_res;
    DatabaseReference databaseReference;
    @SuppressLint("NotifyDataSetChanged")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history_page);

        Log.d("page","History page which displays the people who the user has donated to");

        historyREC=findViewById(R.id.historyREC);
        history_back=findViewById(R.id.history_back);
        load_history=findViewById(R.id.load_history);
        empty_res=findViewById(R.id.empty_res);

        if (!isNetworkConnected(this)) {
            showNoInternetDialog(this);
            Log.d("error","No internet access available");
        }

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
        databaseReference = FirebaseDatabase.getInstance().getReference("Donars").child(savedUsername);
        databaseReference.addValueEventListener(new ValueEventListener() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                //itemList.clear();
                load_history.setVisibility(View.INVISIBLE);
                empty_res.setVisibility(View.INVISIBLE);
                itemList = new ArrayList<>();
                adapter = new History_RecyclerViewAdapter(itemList,History_page.this,History_page.this);
                historyREC.setAdapter(adapter);

                if (dataSnapshot.child("History").exists()) {
                    if (dataSnapshot.child("History").getChildren() != null) {
                        for (DataSnapshot snapshot : dataSnapshot.child("History").getChildren()) {

                            String Name = snapshot.getKey();
                            String Blood = snapshot.child("Blood Group").getValue(String.class);
                            History_dataClass data = new History_dataClass(Name, Blood);
                            itemList.add(data);

                        }
                    } else {
                        empty_res.setVisibility(View.VISIBLE);
                    }
                }
                else
                {
                    empty_res.setVisibility(View.VISIBLE);
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
        // Notify the adapter that data has changed
    }

    public static boolean isNetworkConnected(Context context) {
        ConnectivityManager connectivityManager =
                (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivityManager != null) {
            NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
            return networkInfo != null && networkInfo.isConnected();
        }
        return false;
    }

    public void showNoInternetDialog(Context context) {
        new AlertDialog.Builder(context)
                .setTitle("No Internet Connection")
                .setMessage("Please check your internet connection and try again.")
                .setPositiveButton("OK", (dialog, which) -> {
                    dialog.dismiss();
                    onBackPressed();
                })
                .show();
    }


    @Override
    public void ItemOnClick(int position) {

    }
}