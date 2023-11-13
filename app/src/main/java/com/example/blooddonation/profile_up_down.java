package com.example.blooddonation;

import android.net.Uri;

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
    public Uri downloadImage(String username)
    {//there is problem in download look for it
        final Uri[] uri = {null};
        if(username!=null)
        {
            // Create a reference to "images" folder
            StorageReference storageRef = FirebaseStorage.getInstance().getReference().child("Profile");
            // Create a reference to "images/subfolder/<FILENAME>"
            StorageReference subfolderRef = storageRef.child(username);
            //upload the file using put_file method
            subfolderRef.getDownloadUrl().addOnCompleteListener(task->{
                if (task.isSuccessful())
                {
                    uri[0] =task.getResult();
                }
            });
        }
        return uri[0];
    }
}