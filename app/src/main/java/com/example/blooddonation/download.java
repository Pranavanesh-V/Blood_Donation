package com.example.blooddonation;

import android.annotation.SuppressLint;
import android.content.Context;
import android.net.Uri;
import android.os.Build;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class download
{
    DatabaseReference databaseReference1;
    public void down(Context context, ImageView imageView,String savedUsername)
    {
        databaseReference1 = FirebaseDatabase.getInstance().getReference("Donars");
        databaseReference1.addValueEventListener(new ValueEventListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.child(savedUsername).child("Profile").exists()) {
                    String S_uri = dataSnapshot.child(savedUsername).child("Profile").getValue(String.class);
                    Uri uri = Uri.parse(S_uri);
                    Glide.with(context).load(uri)
                            .apply(RequestOptions.circleCropTransform())
                            .into(imageView);
                    //imageView.setImageURI(uri);
                    System.out.println(uri);
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}
