package com.example.blooddonation;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.PopupWindow;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.FileProvider;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.google.firebase.Timestamp;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.yalantis.ucrop.UCrop;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class Edit_profile_page extends AppCompatActivity {

    private SharedPreferences sharedPreferences;
    private static final String PREFS_NAME = "MyPrefs";
    DatabaseReference databaseReference;
    EditText blood_Group2,mail_id3,address2;
    String S_blood_g="";
    TextView name3;
    Boolean res=true;
    Button back10,save,cancel,f_device,camera;
    String savedUsername;
    ImageView profile2;
    private static final int PICK_IMAGE_REQUEST=1;
    private static final int REQUEST_IMAGE_CAPTURE = 2;
    private Uri imageUri;
    ConstraintLayout layout;
    ProgressBar progressBar1;
    int[] data={0,0,0};
    String blood, mail, Address;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile_page);

        blood_Group2=findViewById(R.id.blood_Group2);
        name3=findViewById(R.id.name3);
        mail_id3=findViewById(R.id.mail_id3);
        address2=findViewById(R.id.address2);
        back10=findViewById(R.id.back10);
        save=findViewById(R.id.Save);
        layout=findViewById(R.id.R_layout);
        profile2=findViewById(R.id.profile2);
        progressBar1=findViewById(R.id.progressBar1);

        Intent intent=getIntent();
        int integer=intent.getIntExtra("Flag",0);
        try {
            downloadImage();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        sharedPreferences = getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        savedUsername = sharedPreferences.getString("username", "");
        name3.setText(savedUsername);

        if (integer==1)
        {
            blood_Group2.setEnabled(true);
            address2.setEnabled(true);
            mail_id3.setEnabled(true);
        }
        else
        {
            blood_Group2.setEnabled(false);
            address2.setEnabled(false);
            mail_id3.setEnabled(true);
        }
        blood_Group2.setOnClickListener(view -> {
            PopupMenu popupMenu = new PopupMenu(Edit_profile_page.this, blood_Group2);
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
                S_blood_g = item.getTitle().toString();
                //((TextInputLayout) blood_g.getChildAt(0)).getEditText().setText(selectedText);
                blood_Group2.setText(S_blood_g);
                return true;
            });
            popupMenu.show();
        });
        save.setOnClickListener(view -> {
            if(blood_Group2.getText().toString().trim().isEmpty())
            {
                if (mail_id3.getText().toString().trim().isEmpty())
                {
                    if (address2.getText().toString().trim().isEmpty())
                    {
                        System.out.println("no change");
                    }
                    else
                    {
                        data[2]=1;
                        System.out.println("Address only");
                    }
                }
                else
                {
                    if (address2.getText().toString().trim().isEmpty())
                    {
                        data[1]=1;
                        System.out.println("only mail_id");
                    }
                    else
                    {
                        data[1]=1;
                        data[2]=1;
                        System.out.println("mail_id and address");
                    }
                }
            }
            else
            {
                if (mail_id3.getText().toString().trim().isEmpty())
                {
                    if (address2.getText().toString().trim().isEmpty())
                    {
                        data[0]=1;
                        System.out.println("only blood_g"+blood_Group2.getText().toString());
                    }
                    else
                    {
                        data[0]=1;
                        data[2]=1;
                        System.out.println("blood_g and address only");
                    }
                }
                else
                {
                    if (address2.getText().toString().trim().isEmpty())
                    {
                        data[0]=1;
                        data[1]=1;
                        System.out.println("blood_g and mail_id");
                    }
                    else
                    {
                        data[0]=1;
                        data[1]=1;
                        data[2]=1;
                        System.out.println("all options");
                    }
                }
            }
            boolean res=fetch(data);
            if (res)
            {
                Toast.makeText(Edit_profile_page.this,"Unsuccessfully updated",Toast.LENGTH_SHORT).show();
                finish();
            }
            else
            {
                Intent intent1=new Intent();
                setResult(RESULT_OK);
                onBackPressed();
                Toast.makeText(Edit_profile_page.this, "Successfully updated", Toast.LENGTH_SHORT).show();
                //Edit_profile_page.this.finish();
            }
        });
        back10.setOnClickListener(view -> {
            Intent intent1=new Intent();
            setResult(RESULT_CANCELED);
            Edit_profile_page.this.finish();
        });
        profile2.setOnClickListener(view -> CreatePopUp());
    }

    public void CreatePopUp()
    {
        LayoutInflater inflater=(LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
        @SuppressLint("InflateParams") View popUpView=inflater.inflate(R.layout.mainpopup,null);

        int width= ViewGroup.LayoutParams.MATCH_PARENT;
        int height=ViewGroup.LayoutParams.WRAP_CONTENT;
        boolean focusable=true;
        PopupWindow popupWindow=new PopupWindow(popUpView,width,height,focusable);
        layout.post(() -> popupWindow.showAtLocation(layout, Gravity.BOTTOM,0,0));
        Button from_device,Delete_pro,cancel;
        from_device=popUpView.findViewById(R.id.from_device);
        Delete_pro=popUpView.findViewById(R.id.Delete_pro);
        cancel=popUpView.findViewById(R.id.cancel_button);
        from_device.setOnClickListener(view -> {
            onChooseImageClick(view);
            popupWindow.dismiss();
        });
        cancel.setOnClickListener(view -> popupWindow.dismiss());
        Delete_pro.setOnClickListener(view -> {
            delete_profile();
            popupWindow.dismiss();
        });
    }

    public void delete_profile()
    {
        String val="https://firebasestorage.googleapis.com/v0/b/mysql-3bcb9.appspot.com/o/Profile%2Fuser_admin%2Fuser_admin.jpg?alt=media&token=806038cd-611b-49fd-b37b-7dd707043ba8";
        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference storageReference = storage.getReference();
        String imageName = savedUsername + ".jpg";
        //path
        System.out.println(savedUsername);
        String S_path="Profile/"+savedUsername+"/";
        // Assume storageReference is the StorageReference to your image in Firebase Storage
        StorageReference imageRef = storageReference.child(S_path + imageName);

        // Delete the file
        imageRef.delete().addOnSuccessListener(aVoid -> {
            // File deleted successfully
            Log.d("FirebaseStorage", "File deleted successfully");
            Toast.makeText(Edit_profile_page.this, "Profile Removed", Toast.LENGTH_SHORT).show();

            DatabaseReference reference= FirebaseDatabase.getInstance().getReference().child("Donars");
            reference.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot datasnapshot) {
                    // Push data to a new unique key
                    reference.child(savedUsername).child("Profile").setValue("No");
                    reference.child(savedUsername).child("Profile Value").setValue("No");
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putString("url","No").apply();
                    Glide.with(Edit_profile_page.this).load(val).into(profile2);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                }
            });
        }).addOnFailureListener(exception -> {
            // Uh-oh, an error occurred!
            Log.e("FirebaseStorage", "Error deleting file: " + exception.getMessage());
            Toast.makeText(Edit_profile_page.this, "Profile can't be Removed", Toast.LENGTH_SHORT).show();
        });
    }
    public Boolean fetch(int[] d){
        final boolean []flag = {false};

        Timestamp firebaseTimestamp = Timestamp.now();

        // Convert Firebase Timestamp to java.util.Date
        Date date = firebaseTimestamp.toDate();

        // Add 8 hours to the time
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.HOUR_OF_DAY, 8);

        // Convert the updated time back to a Date
        Date updatedDate = calendar.getTime();

        // Create a formatter for a readable date and time format
        @SuppressLint("SimpleDateFormat") SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        // Format and display the original and updated timestamps
        String formattedOriginalTimestamp = formatter.format(date);
        String formattedUpdatedTimestamp = formatter.format(updatedDate);

        // Initialize Firebase Realtime Database
        databaseReference = FirebaseDatabase.getInstance().getReference().child("Donars");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot)
            {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    if (snapshot.child("Blood Group").exists() && snapshot.child("Address").exists() && snapshot.child("Email").exists()) {
                        //to check if the user doesn't changes the existing data but changes the profile
                        //if he changes the data handel it else just update the profile
                        Address = address2.getText().toString().trim();
                        mail = mail_id3.getText().toString().trim();
                        blood = blood_Group2.getText().toString().trim();
                        if (d[0] == 0 && d[1] == 0 && d[2] == 1)
                        {
                            databaseReference.child(savedUsername).child("Address").setValue(Address);
                            databaseReference.child(savedUsername).child("Time uploaded").setValue(formattedOriginalTimestamp);
                            databaseReference.child(savedUsername).child("Time Remove").setValue(formattedUpdatedTimestamp);
                            flag[0] = true;
                            break;
                        }
                        else if (d[0] == 0 && d[1] == 1 && d[2] == 0)
                        {
                            databaseReference.child(savedUsername).child("Email").setValue(mail);
                            databaseReference.child(savedUsername).child("Time uploaded").setValue(formattedOriginalTimestamp);
                            databaseReference.child(savedUsername).child("Time Remove").setValue(formattedUpdatedTimestamp);
                            flag[0] = true;
                            break;
                        }
                        else if (d[0] == 0 && d[1] == 1 && d[2] == 1)
                        {
                            databaseReference.child(savedUsername).child("Email").setValue(mail);
                            databaseReference.child(savedUsername).child("Address").setValue(Address);
                            databaseReference.child(savedUsername).child("Time uploaded").setValue(formattedOriginalTimestamp);
                            databaseReference.child(savedUsername).child("Time Remove").setValue(formattedUpdatedTimestamp);
                            flag[0] = true;
                            break;
                        }
                        else if (d[0] == 1 && d[1] == 0 && d[2] == 0)
                        {
                            databaseReference.child(savedUsername).child("Blood Group").setValue(blood);
                            databaseReference.child(savedUsername).child("Time uploaded").setValue(formattedOriginalTimestamp);
                            databaseReference.child(savedUsername).child("Time Remove").setValue(formattedUpdatedTimestamp);
                            flag[0] = true;
                            break;
                        }
                        else if (d[0] == 1 && d[1] == 0 && d[2] == 1)
                        {
                            databaseReference.child(savedUsername).child("Blood Group").setValue(blood);
                            databaseReference.child(savedUsername).child("Address").setValue(Address);
                            databaseReference.child(savedUsername).child("Time uploaded").setValue(formattedOriginalTimestamp);
                            databaseReference.child(savedUsername).child("Time Remove").setValue(formattedUpdatedTimestamp);
                            flag[0] = true;
                            break;
                        }
                        else if (d[0] == 1 && d[1] == 1 && d[2] == 0)
                        {
                            databaseReference.child(savedUsername).child("Blood Group").setValue(blood);
                            databaseReference.child(savedUsername).child("Email").setValue(mail);
                            databaseReference.child(savedUsername).child("Time uploaded").setValue(formattedOriginalTimestamp);
                            databaseReference.child(savedUsername).child("Time Remove").setValue(formattedUpdatedTimestamp);
                            flag[0] = true;
                            break;
                        }
                        else if (d[0] == 1 && d[1] == 1 && d[2] == 1)
                        {
                            databaseReference.child(savedUsername).child("Blood Group").setValue(blood);
                            databaseReference.child(savedUsername).child("Email").setValue(mail);
                            databaseReference.child(savedUsername).child("Address").setValue(Address);
                            databaseReference.child(savedUsername).child("Time uploaded").setValue(formattedOriginalTimestamp);
                            databaseReference.child(savedUsername).child("Time Remove").setValue(formattedUpdatedTimestamp);
                            flag[0] = true;
                            break;
                        }
                    } else {
                        if (d[1] == 1) {
                            String mail = mail_id3.getText().toString().trim();
                            databaseReference.child(savedUsername).child("Email").setValue(mail);
                            databaseReference.child(savedUsername).child("Time uploaded").setValue(formattedOriginalTimestamp);
                            databaseReference.child(savedUsername).child("Time Remove").setValue(formattedUpdatedTimestamp);
                            flag[0] = true;
                            System.out.println("Email_id");
                        }
                    }
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(Edit_profile_page.this, "Changes Not Made", Toast.LENGTH_SHORT).show();
            }
        });
    return flag[0];
    }
    private void initSharedPreferences() {
        sharedPreferences = getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
    }

    // Open image picker when a button is clicked
    public void onChooseImageClick(View view) {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
    }
    public void onCameraClick(View view)
    {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            // Create a file to save the image
            File photoFile = createImageFile();

            if (photoFile != null) {
                imageUri = FileProvider.getUriForFile(this, "com.example.blooddonation.Edit_profile_page", photoFile);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
                startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
            }
        }
    }

    // Create a file to save the image
    private File createImageFile() {
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        try {
            return File.createTempFile(imageFileName, ".jpg", storageDir);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    // Handle the result of the image picker
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
             imageUri= data.getData();

            // Start UCrop for image cropping
            startCropActivity(imageUri);
        } else if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            // Image captured from camera successfully
            // Start UCrop for image cropping
            startCropActivity(imageUri);
        } else if (requestCode == UCrop.REQUEST_CROP && resultCode == RESULT_OK) {
            // Image cropped successfully, get the cropped URI
            Uri resultUri = UCrop.getOutput(data);
            // Now you can proceed with uploading the cropped image
            if (resultUri != null) {
                profile2.setImageURI(resultUri);
                uploadImage(resultUri);
            }
        }
    }
    // Continue from the previous code...

    // Upload the selected image to Firebase Storage
    private void uploadImage(@NonNull Uri imageUri) {
        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference storageRef = storage.getReference();

        // Generate a unique name for the image
        String imageName = savedUsername + ".jpg";
        //path
        System.out.println(savedUsername);
        String S_path="Profile/"+savedUsername+"/";
        // Get a reference to the image in the "users" folder (replace "user1" with the actual user identifier)
        StorageReference userRef = storageRef.child(S_path + imageName);

        // Upload the image
        userRef.putFile(imageUri)
                .addOnSuccessListener(taskSnapshot -> {
                    // Image uploaded successfully
                    // You can get the download URL of the image if needed:
                    userRef.getDownloadUrl().addOnSuccessListener(uri -> {
                        String downloadUrl = uri.toString();
                        res=true;
                        // Now you can store the downloadUrl in your database or use it as needed
                        if (res)
                        {
                            DatabaseReference reference= FirebaseDatabase.getInstance().getReference().child("Donars");
                            reference.addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot datasnapshot) {
                                    // Push data to a new unique key
                                    reference.child(savedUsername).child("Profile").setValue("Yes");
                                    reference.child(savedUsername).child("Profile Value").setValue(downloadUrl);
                                    setResult(RESULT_OK);
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {
                                    reference.child(savedUsername).child("Profile").setValue("No");
                                    reference.child(savedUsername).child("Profile Value").setValue("No");
                                    setResult(RESULT_CANCELED);
                                }

                            });
                        }
                    });
                })
                .addOnFailureListener(e -> {
                    res=false;
                    setResult(RESULT_CANCELED);
                    // Handle any errors during upload
                });

    }
    private void startCropActivity(Uri sourceUri) {
        // UCrop configuration
        UCrop.Options options = new UCrop.Options();
        options.setCompressionFormat(Bitmap.CompressFormat.JPEG);
        options.setCompressionQuality(80);

        // Start UCrop activity
        UCrop.of(sourceUri, Uri.fromFile(new File(getCacheDir(), "cropped_image.jpg")))
                .withOptions(options)
                .start(this);
    }
    public void downloadImage() throws IOException {
        // Get a reference to the Firebase Storage
        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference storageRef = storage.getReference();

        String savedU;
        sharedPreferences = getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        savedU = sharedPreferences.getString("username", "");
        // Reference to the image file
        StorageReference imageRef = storageRef.child("Profile/"+savedU+"/"+savedU + ".jpg");
        System.out.println(savedU);
        // Download the image into a local file
        File localFile = File.createTempFile("images", "jpg");
        imageRef.getFile(localFile)
                .addOnSuccessListener(taskSnapshot -> {
                    // Image downloaded successfully
                    // Now, load the image into ImageView using Glide
                    Glide.with(this).load(localFile)
                            .listener(new RequestListener<Drawable>() {
                                @Override
                                public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                                    // Hide the loading indicator in case of a failure
                                    progressBar1.setVisibility(View.GONE);
                                    return false;
                                }

                                @Override
                                public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                                    // Hide the loading indicator when the image is ready
                                    progressBar1.setVisibility(View.GONE);
                                    return false;
                                }
                            }).into(profile2);
                })
                .addOnFailureListener(exception -> {
                    // Handle errors
                    Log.e("Firebase", "Error downloading image: " + exception.getMessage());
                    profile2.setImageResource(R.drawable.user);
                    progressBar1.setVisibility(View.INVISIBLE);
                });

    }
    private void showPopupMenu() {
        PopupMenu popupMenu = new PopupMenu(this, blood_Group2);
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
            S_blood_g = item.getTitle().toString();
            //((TextInputLayout) blood_g.getChildAt(0)).getEditText().setText(selectedText);
            blood_Group2.setText(S_blood_g);
            return true;
        });
        popupMenu.show();
    }
}