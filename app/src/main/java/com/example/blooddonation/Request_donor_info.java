package com.example.blooddonation;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

public class Request_donor_info extends AppCompatActivity {

    ImageView back_req;
    TextView Blood_group_req,name_req,city_req,reason_req;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_request_donor_info);

        back_req=findViewById(R.id.back_req);
        Blood_group_req=findViewById(R.id.Blood_group_req);
        name_req=findViewById(R.id.name_req);
        city_req=findViewById(R.id.city_req);
        reason_req=findViewById(R.id.reason_req);


        Intent intent= getIntent();
        String name=intent.getStringExtra("Name");
        String blood=intent.getStringExtra("Blood");
        String location= intent.getStringExtra("Location");
        String TXT=intent.getStringExtra("Txt");
        Blood_group_req.setText(blood);
        name_req.setText(name);
        city_req.setText(location);
        reason_req.setText(TXT);

        back_req.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });


    }
}