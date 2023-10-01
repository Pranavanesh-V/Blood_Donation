package com.example.blooddonation;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class home_page extends AppCompatActivity implements OnItemClickListener{

    TextInputLayout Search;
    TextInputEditText E_search;
    Button request_btn, donate_btn;
    ImageView Filter,Menu;
    RecyclerView recyclerView,recyclerView1;
    DatabaseReference databaseReference;
    String S_Blood_G,Age,Gender;
    String inputText="";
    ImageView empty_res;
    private RecyclerViewAdapter adapter;
    private RecyclerViewAdapter1 adapter1;
    private List<DataClass> itemList;
    private List<DataClass2> itemList1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page);

        empty_res=findViewById(R.id.empty_res);
        request_btn =findViewById(R.id.request_btn);
        donate_btn =findViewById(R.id.donate_btn);
        Filter=findViewById(R.id.Filter);
        Menu=findViewById(R.id.Menu);
        Search=findViewById(R.id.Search);
        E_search=findViewById(R.id.E_search);
        //recycler views
        recyclerView=findViewById(R.id.recyclerView);
        recyclerView1=findViewById(R.id.recyclerView1);

        GridLayoutManager gridLayoutManager=new GridLayoutManager(home_page.this,1);
        recyclerView.setLayoutManager(gridLayoutManager);

        GridLayoutManager gridLayoutManager1=new GridLayoutManager(home_page.this,1);
        recyclerView1.setLayoutManager(gridLayoutManager1);

        Intent intent=getIntent();
        String username=intent.getStringExtra("username");

        request_btn.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("UseCompatLoadingForDrawables")
            @Override
            public void onClick(View view) {
                request_btn.setBackground(getDrawable(R.drawable.cus_join11));
                donate_btn.setBackground(getDrawable(R.drawable.cus_join2));
                Search.setVisibility(View.VISIBLE);
                Filter.setVisibility(View.VISIBLE);
                recyclerView.setVisibility(View.VISIBLE);
            }
        });
        donate_btn.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("UseCompatLoadingForDrawables")
            @Override
            public void onClick(View view) {
                request_btn.setBackground(getDrawable(R.drawable.cus_join1));
                donate_btn.setBackground(getDrawable(R.drawable.cus_join22));
                Search.setVisibility(View.INVISIBLE);
                Filter.setVisibility(View.INVISIBLE);
                recyclerView.setVisibility(View.INVISIBLE);
            }
        });

        E_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        Filter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(home_page.this, Filter_page.class);
                startActivityForResult(intent,1);
            }
        });
        Menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Create the floating window
                //Menu.isFocused()
                showPopupMenuForMenu();
            }
        });

        // Add a TextWatcher to monitor changes in the text
        E_search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                // Do nothing
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                // Do nothing while typing
                inputText= E_search.getText().toString().trim();
            }

            @Override
            public void afterTextChanged(Editable editable) {
                // The function you want to execute when text changes
                // This function will be called when the user types in the EditText
                //inputText= editable.toString().trim();
                // Check if the end icon was clicked (usually for clearing text)
            }
        });

        Search.setEndIconOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                fetchDataFromFirebase();
            }
        });


        // Initialize Firebase Realtime Database
        databaseReference = FirebaseDatabase.getInstance().getReference("Donars");


        //Recycler View
        itemList = new ArrayList<>();
        itemList1=new ArrayList<>();
        adapter = new RecyclerViewAdapter(itemList,this);
        adapter1= new RecyclerViewAdapter1(itemList1,this);
        recyclerView.setAdapter(adapter);
        recyclerView1.setAdapter(adapter1);

        DataClass2 item = new DataClass2("pranav", "S_Location", "Blood_g","txt");
        itemList1.add(item);
        itemList1.add(item);
        itemList1.add(item);
        itemList1.add(item);
        itemList1.add(item);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode==1)
        {
            if (resultCode==RESULT_OK)
            {
                Age=data.getStringExtra("Age");
                S_Blood_G=data.getStringExtra("Blood_group");
                Gender=data.getStringExtra("Gender");
                //E_search.setText(Gender);
                //call method
                //fetchDataFromFirebase();
            }
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finishAffinity();
    }

    private void showPopupMenuForMenu() {
        PopupMenu popupMenu = new PopupMenu(this, Menu);
        popupMenu.getMenu().add("Profile");
        popupMenu.getMenu().add("Personalisation");
        popupMenu.getMenu().add("Security");
        popupMenu.getMenu().add("About");


        // Set an item click listener for the PopupMenu
        popupMenu.setOnMenuItemClickListener(item -> {
            // Handle item selection here
            return true;
        });
        popupMenu.show();
    }

    private void fetchDataFromFirebase() {
        databaseReference.addValueEventListener(new ValueEventListener() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                itemList.clear(); // Clear the list to avoid duplicates
                empty_res.setVisibility(View.INVISIBLE);

                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    // Parse data from the snapshot
                    String name = snapshot.getKey();

                    if(snapshot.child("Blood Group").exists() && snapshot.child("Phone No").exists()) {
                        String Blood_g = snapshot.child("Blood Group").getValue(String.class);
                        String S_Gender=snapshot.child("Gender").getValue(String.class);
                        String S_Location=snapshot.child("City").getValue(String.class);

                        if (!inputText.equals(""))
                        {
                            if (inputText.equals(S_Location))
                            {
                                if (!Objects.isNull(Age)) {
                                    if (!Objects.isNull(S_Blood_G)) {
                                        if (!Objects.isNull(Gender)) {
                                            System.out.println("Everything is chosen");
                                        } else {
                                            System.out.println("Only opt1 and opt2 is chosen");
                                        }
                                    } else {
                                        if (!Objects.isNull(Gender)) {
                                            System.out.println("opt1 and opt3 is chosen");
                                        } else {
                                            System.out.println("Only opt1 is chosen");
                                        }
                                    }
                                } else {
                                    if (!Objects.isNull(S_Blood_G)) {
                                        if (!Objects.isNull(Gender)) {
                                            System.out.println("opt2 and opt3 is chosen");
                                            if (Blood_g.equals(S_Blood_G) && S_Gender.equals(Gender))
                                            {
                                                DataClass item = new DataClass(name, S_Location, Blood_g);
                                                itemList.add(item);
                                            }
                                        } else {
                                            System.out.println("Only opt2 is chosen");
                                            if (Blood_g.equals(S_Blood_G)) {
                                                DataClass item = new DataClass(name, S_Location, Blood_g);
                                                itemList.add(item);
                                            }
                                        }
                                    } else {
                                        if (!Objects.isNull(Gender)) {
                                            System.out.println("Opt3 is only chosen");
                                            if (Gender.equals(S_Gender)) {
                                                DataClass item = new DataClass(name, S_Location, Blood_g);
                                                itemList.add(item);
                                            }
                                        } else {
                                            DataClass item = new DataClass(name, S_Location, Blood_g);
                                            itemList.add(item);
                                            System.out.println("No filter is chosen\n no opt chosen");
                                        }
                                    }
                                }
                            }
                        }
                        else
                        {
                            if (!Objects.isNull(Age)) {
                                if (!Objects.isNull(S_Blood_G)) {
                                    if (!Objects.isNull(Gender)) {
                                        System.out.println("Everything is chosen");
                                    } else {
                                        System.out.println("Only opt1 and opt2 is chosen");
                                    }
                                } else {
                                    if (!Objects.isNull(Gender)) {
                                        System.out.println("opt1 and opt3 is chosen");
                                    } else {
                                        System.out.println("Only opt1 is chosen");
                                    }
                                }
                            } else {
                                if (!Objects.isNull(S_Blood_G)) {
                                    if (!Objects.isNull(Gender)) {
                                        System.out.println("opt2 and opt3 is chosen");
                                        if (Blood_g.equals(S_Blood_G) && S_Gender.equals(Gender))
                                        {
                                            DataClass item = new DataClass(name, S_Location, Blood_g);
                                            itemList.add(item);
                                        }
                                    } else {
                                        System.out.println("Only opt2 is chosen");
                                        if (Blood_g.equals(S_Blood_G)) {
                                            DataClass item = new DataClass(name, S_Location, Blood_g);
                                            itemList.add(item);
                                        }
                                    }
                                } else {
                                    if (!Objects.isNull(Gender)) {
                                        System.out.println("Opt3 is only chosen");
                                        if (Gender.equals(S_Gender)) {
                                            DataClass item = new DataClass(name, S_Location, Blood_g);
                                            itemList.add(item);
                                        }
                                    } else {
                                        DataClass item = new DataClass(name, S_Location, Blood_g);
                                        itemList.add(item);
                                        System.out.println("No filter is chosen\n no opt chosen");
                                    }
                                }
                            }
                        }
                    }
                    else
                    {
                    }
                }
                if (itemList.isEmpty()) {
                    empty_res.setVisibility(View.VISIBLE);
                    Toast.makeText(home_page.this, "Empty results", Toast.LENGTH_SHORT).show();
                }

                // Notify the adapter that data has changed
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Handle errors
            }
        });
    }
    @Override
    public void onItemClick(int position) {
        DataClass data=itemList.get(position);
        String name=data.getDataName();
        String blood= data.getDataBlood();
        Intent intent=new Intent(home_page.this,Donor_info_pg.class);
        intent.putExtra("Name",name);
        intent.putExtra("Blood",blood);
        startActivity(intent);

    }
}