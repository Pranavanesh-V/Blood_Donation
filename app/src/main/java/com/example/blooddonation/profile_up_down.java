package com.example.blooddonation;

import android.content.Context;
import android.net.Uri;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.bumptech.glide.request.RequestOptions;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class profile_up_down
{
    public void uploadImage(Uri imageUri,String username) {
        if (imageUri != null) {
            // Create a reference to "images" folder
            StorageReference storageRef = FirebaseStorage.getInstance().getReference().child("Profile");

            // Create a reference to "images/subfolder/<FILENAME>"
            StorageReference subfolderRef = storageRef.child(username);
            //upload the file using put_file method
            subfolderRef.putFile(imageUri);
        }
    }
    public void downloadImage(String username, Context context, ImageView imageView)
    {//there is problem in download look for it
        StorageReference storageRef = FirebaseStorage.getInstance().getReference().child("Profile");

        // Create a reference to "images/subfolder/<FILENAME>"
        StorageReference subfolderRef = storageRef.child(username);
        subfolderRef.getDownloadUrl().addOnCompleteListener(task -> {
            if(task.isSuccessful())
            {
                Uri uri=task.getResult();
                System.out.println(uri+"hello");
                Glide.with(context).load(uri).apply(RequestOptions.circleCropTransform()).transition(DrawableTransitionOptions.withCrossFade()).into(imageView);
            }else
            {
                System.out.println("error");
            }
        });
    }
}