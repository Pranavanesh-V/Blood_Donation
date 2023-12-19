package com.example.blooddonation;

import android.telephony.SmsManager;

import androidx.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Sms_sender
{
    public void send(String blood_group,String name)
    {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("Donars");
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot datasnapshot) {
                //gets all the children
                for (DataSnapshot snapshot : datasnapshot.getChildren()) {
                    //checks if the blood group exists
                    //there are cases where some won't register fully
                    if (snapshot.child("Blood Group").exists())
                    {
                        String check_bg=snapshot.child("Blood Group").getValue(String.class);
                        if (blood_group.equals(check_bg))
                        {
                            String number = snapshot.child("Phone No").getValue(String.class);
                            //every time this function is called a new otp is generated
                            SmsManager smsManager = SmsManager.getDefault();
                            String MESSAGE = "Requesting for "+ blood_group +" blood from Blood Donation App for the patient "+name;

                            smsManager.sendTextMessage(number, null, MESSAGE, null, null);
                        }
                    }
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}
