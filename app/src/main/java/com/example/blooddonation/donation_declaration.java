package com.example.blooddonation;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class donation_declaration extends AppCompatActivity {
    Button donate2,back_req2;
    RadioButton agree;
    String Phone_number,Name;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_donation_declaration);

        donate2=findViewById(R.id.donate2);
        back_req2=findViewById(R.id.back9);
        agree=findViewById(R.id.Agree);

        Intent intent=getIntent();
        Phone_number=intent.getStringExtra("Phone");
        Name=intent.getStringExtra("Name");
        back_req2.setOnClickListener(view -> finish());
        donate2.setOnClickListener(view -> {
            if (agree.isChecked())
            {
                System.out.println(Phone_number);
                Intent intent1 =new Intent(Intent.ACTION_DIAL,
                        Uri.parse("tel:"+Phone_number));
                startActivity(intent1);

                /*// Get a reference to the node you want to delete
                DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Request");
                DatabaseReference nodeToDeleteRef = databaseReference.child(Name);

                // Delete the node
                nodeToDeleteRef.removeValue()
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                // Node deleted successfully
                                Toast.makeText(donation_declaration.this, "Node deleted successfully", Toast.LENGTH_SHORT).show();
                                Intent intent1= new Intent(donation_declaration.this,home_page.class);
                                startActivity(intent1);
                                finish();
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                // Handle failure
                                Toast.makeText(donation_declaration.this, "Failed to delete node", Toast.LENGTH_SHORT).show();
                            }
                        });*/
            }
            else
            {
                Toast.makeText(donation_declaration.this, R.string.please_agree_to_our_declaration, Toast.LENGTH_SHORT).show();
            }
        });
    }
}