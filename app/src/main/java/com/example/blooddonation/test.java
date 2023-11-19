package com.example.blooddonation;

import android.annotation.SuppressLint;
import android.net.Uri;
import android.os.Build;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class test {
    DatabaseReference databaseReference;
    String imageUrl;
    public String downloadImage(String text)  {
        // Get a reference to the Firebase Storage

        //System.out.println(text);

        databaseReference = FirebaseDatabase.getInstance().getReference("Request");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.child(text).child("Profile").exists())
                {
                    String savedU=dataSnapshot.child(text).child("Profile").getValue(String.class);
                    System.out.println(savedU);
                    if (!savedU.equals("")) {
                        StorageReference storageRef = FirebaseStorage.getInstance().getReference().child("Profile/" + savedU + "/" + savedU + ".jpg");
                        storageRef.getMetadata().addOnSuccessListener(metadata -> {
                            // Get download URL for the image
                            storageRef.getDownloadUrl().addOnCompleteListener(new OnCompleteListener<Uri>() {
                                @Override
                                public void onComplete(@NonNull Task<Uri> task) {
                                    if (task.isSuccessful()) {
                                        // Get the URI for the image
                                        imageUrl = task.getResult().toString();
                                        //System.out.println(imageUrl);
                                        // Use the URI to load the image into the ImageView using Glide or any other library
                                    } else {
                                        // Handle the error
                                        imageUrl="jkl";
                                        System.out.println("error");
                                    }
                                }
                            });
                        }).addOnFailureListener(exception -> {
                                // Object does not exist or an error occurred
                                // You can handle this case accordingly
                                System.out.println("error no");
                                imageUrl="jkl";
                        });

                    }
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
        return imageUrl;
    }
}
/*
if (!Objects.isNull(savedU)) {
                        // Reference to the image file
                        StorageReference imageRef = storageRef.child("Profile/" + savedU + "/" + savedU + ".jpg");
                        String img_u=imageRef.getDownloadUrl().getResult().toString();
                        System.out.println(savedU+" hello");
                        Glide.with(context).load(img_u)
                                .into(viewHolder.recImage);

                    }
                    else
                    {
                        System.out.println(savedU+"shit");
                    }
 */

