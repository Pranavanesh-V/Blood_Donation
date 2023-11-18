package com.example.blooddonation;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
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
    Integer age;
    private static final String PREFS_NAME = "MyPrefs";
    private SharedPreferences sharedPreferences;
    String savedUsername;
    TextInputEditText E_search;
    Button request_btn, donate_btn;
    ImageView Filter,Menu;
    RecyclerView recyclerView,recyclerView1;
    DatabaseReference databaseReference,databaseReference1,databaseReference2;
    String S_Blood_G,Age,Gender;
    String inputText="";
    ImageView empty_res;
    private RecyclerViewAdapter adapter;
    private RecyclerViewAdapter1 adapter1;
    private List<DataClass> itemList;
    private List<DataClass2> itemList1;
    TextView Disp,Disp1;
    ImageView user,heading;

    @SuppressLint({"SetTextI18n", "UseCompatLoadingForDrawables"})
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
        user=findViewById(R.id.user);
        Disp=findViewById(R.id.Disp);
        Disp1=findViewById(R.id.Disp1);
        heading=findViewById(R.id.heading);
        //recycler views
        recyclerView=findViewById(R.id.recyclerView);
        recyclerView1=findViewById(R.id.recyclerView1);

        GridLayoutManager gridLayoutManager=new GridLayoutManager(home_page.this,1);
        recyclerView.setLayoutManager(gridLayoutManager);

        GridLayoutManager gridLayoutManager1=new GridLayoutManager(home_page.this,1);
        recyclerView1.setLayoutManager(gridLayoutManager1);

        sharedPreferences = getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        savedUsername = sharedPreferences.getString("username", "");

        test();

        Intent intent=getIntent();

        String username=intent.getStringExtra("username");
        Disp.setText(getResources().getText(R.string.hi)+" "+savedUsername);
        request_fetch();

        request_btn.setOnClickListener(view -> {
            request_btn.setBackground(getDrawable(R.drawable.cus_join11));
            donate_btn.setBackground(getDrawable(R.drawable.cus_join2));
            Search.setVisibility(View.VISIBLE);
            Filter.setVisibility(View.VISIBLE);
            recyclerView.setVisibility(View.VISIBLE);
            user.setVisibility(View.INVISIBLE);
            Disp.setVisibility(View.INVISIBLE);
            Disp1.setVisibility(View.INVISIBLE);
            recyclerView1.setVisibility(View.INVISIBLE);
            heading.setVisibility(View.INVISIBLE);
            empty_res.setVisibility(View.INVISIBLE);

        });
        donate_btn.setOnClickListener(view -> {
            request_btn.setBackground(getDrawable(R.drawable.cus_join1));
            donate_btn.setBackground(getDrawable(R.drawable.cus_join22));
            Search.setVisibility(View.INVISIBLE);
            Filter.setVisibility(View.INVISIBLE);
            recyclerView.setVisibility(View.INVISIBLE);
            user.setVisibility(View.VISIBLE);
            Disp.setVisibility(View.VISIBLE);
            Disp1.setVisibility(View.VISIBLE);
            recyclerView1.setVisibility(View.VISIBLE);
            heading.setVisibility(View.VISIBLE);
            //Disp.setText(R.string.hi + savedUsername);
            Disp.setText(getResources().getText(R.string.hi)+" "+savedUsername);
            request_fetch();
        });
        E_search.setOnClickListener(view -> {

        });

        Filter.setOnClickListener(view -> {
            Intent intent1 =new Intent(home_page.this, Filter_page.class);
            startActivityForResult(intent1,1);
        });
        Menu.setOnClickListener(view -> {
            // Create the floating window
            //Menu.isFocused()
            showPopupMenuForMenu();
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

        Search.setEndIconOnClickListener(view -> fetchDataFromFirebase());


        // Initialize Firebase Realtime Database
        databaseReference = FirebaseDatabase.getInstance().getReference("Donars");


        //Recycler View
        itemList = new ArrayList<>();
        itemList1=new ArrayList<>();
        adapter = new RecyclerViewAdapter(itemList,this);
        adapter1= new RecyclerViewAdapter1(itemList1,this);
        recyclerView.setAdapter(adapter);
        recyclerView1.setAdapter(adapter1);
    }

    public void request_fetch()
    {
        // Initialize Firebase Realtime Database
        databaseReference1 = FirebaseDatabase.getInstance().getReference("Request");

        databaseReference1.addValueEventListener(new ValueEventListener() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                itemList1.clear(); // Clear the list to avoid duplicates
                empty_res.setVisibility(View.INVISIBLE);
                boolean flag=false;

                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    // Parse data from the snapshot
                    String requesterName = snapshot.getKey();
                    String requesterBloodGroup=snapshot.child("RequesterBloodGroup").getValue(String.class);
                    String requesterLocation=snapshot.child("RequesterLocation").getValue(String.class);
                    String requesterReason=snapshot.child("RequesterReason").getValue(String.class);
                    if (snapshot.child("Received").exists() && snapshot.child("Profile").exists())
                    {
                        if (snapshot.child("Received").getValue(String.class).equals("No") && !snapshot.child("Profile").getValue(String.class).isEmpty())
                        {
                            String S_uri=snapshot.child("Profile").getValue(String.class);
                            Uri uri=Uri.parse(S_uri);
                            DataClass2 item = new DataClass2(requesterName,requesterLocation,requesterBloodGroup,requesterReason,uri);
                            itemList1.add(item);
                        }
                        else {
                            Uri uri=null;
                            DataClass2 item = new DataClass2(requesterName,requesterLocation,requesterBloodGroup,requesterReason,uri);
                            itemList1.add(item);
                        }
                        flag=true;
                    }
                }
                if (!flag)
                {
                    empty_res.setVisibility(View.VISIBLE);
                }
                else
                {
                    empty_res.setVisibility(View.INVISIBLE);
                }
                // Notify the adapter that data has changed
                adapter1.notifyDataSetChanged();
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
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
        popupMenu.getMenu().add("Request");
        popupMenu.getMenu().add("Registration");
        popupMenu.getMenu().add("Security");
        popupMenu.getMenu().add("About");
        popupMenu.getMenu().add("Logout");


        // Set an item click listener for the PopupMenu
        popupMenu.setOnMenuItemClickListener(item -> {
            // Handle item selection here
            String option=item.getTitle().toString();
            if (option.equals("Request"))
            {
                Intent intent=new Intent(home_page.this, Request_page.class);
                startActivity(intent);
            }
            else if (option.equals("Logout"))
            {
                logout();
            }
            else if (option.equals("Profile"))
            {
                Intent intent=new Intent(home_page.this, Profile_page.class);
                startActivity(intent);
            }
            else if (option.equals("About"))
            {
                Intent intent=new Intent(home_page.this, About_page.class);
                startActivity(intent);
            }
            else if (option.equals("Registration"))
            {
                Intent intent=new Intent(home_page.this, Full_Registration_page.class);
                startActivity(intent);
            }
            else if (option.equals("Security"))
            {
                Intent intent=new Intent(home_page.this, Security_page.class);
                startActivity(intent);
            }

            return true;
        });
        popupMenu.show();
    }
    public void test() {
        databaseReference2 = FirebaseDatabase.getInstance().getReference("Donars");
        databaseReference2.addValueEventListener(new ValueEventListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    if (snapshot.getKey().equals(savedUsername)) {
                        if (!snapshot.child("Profile").getValue(String.class).isEmpty()) {
                            download d = new download();
                            d.down(home_page.this, user, savedUsername);
                        }
                    }
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void fetchDataFromFirebase() {
        databaseReference.addValueEventListener(new ValueEventListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                itemList.clear(); // Clear the list to avoid duplicates
                empty_res.setVisibility(View.INVISIBLE);
                boolean flag=true;

                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    // Parse data from the snapshot
                    String name = snapshot.getKey();


                    if(snapshot.child("Blood Group").exists() && snapshot.child("Phone No").exists() && snapshot.child("DOB").exists() && snapshot.child("Profile").exists()) {
                        String Blood_g = snapshot.child("Blood Group").getValue(String.class);
                        String S_Gender=snapshot.child("Gender").getValue(String.class);
                        String S_Location=snapshot.child("City").getValue(String.class);
                        String S_dob=snapshot.child("DOB").getValue(String.class);
                        age_finder age_finder=new age_finder();
                        age=age_finder.age_Cals(S_dob);
                        Uri uri=null;
                        if (!snapshot.child("Profile").getValue(String.class).isEmpty())
                        {
                            String S_uri=snapshot.child("Profile").getValue(String.class);
                            uri=Uri.parse(S_uri);
                        }

                        if (!inputText.equals(""))
                        {
                            if (inputText.equals(S_Location))
                            {
                                if (!Objects.isNull(Age)) {
                                    if (!Objects.isNull(S_Blood_G)) {
                                        if (!Objects.isNull(Gender)) {
                                            boolean res=age_finder.equals_rt(age,Age);
                                            if (Blood_g.equals(S_Blood_G) && res && S_Gender.equals(Gender)) {
                                                System.out.println("Everything is chosen");
                                                DataClass item = new DataClass(name, S_Location, Blood_g,uri);
                                                itemList.add(item);
                                            }
                                        } else {
                                            boolean res=age_finder.equals_rt(age,Age);
                                            if (Blood_g.equals(S_Blood_G) && res)
                                            {
                                                System.out.println("Only opt1 and opt2 is chosen");
                                                DataClass item = new DataClass(name, S_Location, Blood_g,uri);
                                                itemList.add(item);
                                            }
                                        }
                                    } else {
                                        if (!Objects.isNull(Gender)) {
                                            boolean res=age_finder.equals_rt(age,Age);
                                            if (S_Gender.equals(Gender) && res)
                                            {
                                                System.out.println("opt1 and opt3 is chosen");
                                                DataClass item = new DataClass(name, S_Location, Blood_g,uri);
                                                itemList.add(item);
                                            }

                                        } else {
                                            boolean res=age_finder.equals_rt(age,Age);
                                            if (res)
                                            {
                                                System.out.println("Only opt1 is chosen");
                                                DataClass item = new DataClass(name, S_Location, Blood_g,uri);
                                                itemList.add(item);
                                            }
                                        }
                                    }
                                } else {
                                    if (!Objects.isNull(S_Blood_G)) {
                                        if (!Objects.isNull(Gender)) {
                                            System.out.println("opt2 and opt3 is chosen");
                                            if (Blood_g.equals(S_Blood_G) && S_Gender.equals(Gender))
                                            {
                                                DataClass item = new DataClass(name, S_Location, Blood_g,uri);
                                                itemList.add(item);
                                            }
                                        } else {
                                            System.out.println("Only opt2 is chosen");
                                            if (Blood_g.equals(S_Blood_G)) {
                                                DataClass item = new DataClass(name, S_Location, Blood_g,uri);
                                                itemList.add(item);
                                            }
                                        }
                                    } else {
                                        if (!Objects.isNull(Gender)) {
                                            System.out.println("Opt3 is only chosen");
                                            if (Gender.equals(S_Gender)) {
                                                DataClass item = new DataClass(name, S_Location, Blood_g,uri);
                                                itemList.add(item);
                                            }
                                        } else {
                                            DataClass item = new DataClass(name, S_Location, Blood_g,uri);
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
                                        boolean res=age_finder.equals_rt(age,Age);
                                        if (Blood_g.equals(S_Blood_G) && res && S_Gender.equals(Gender)) {
                                            System.out.println("Everything is chosen");
                                            DataClass item = new DataClass(name, S_Location, Blood_g,uri);
                                            itemList.add(item);
                                        }
                                    } else {
                                        boolean res=age_finder.equals_rt(age,Age);
                                        if (Blood_g.equals(S_Blood_G) && res)
                                        {
                                            System.out.println("Only opt1 and opt2 is chosen");
                                            DataClass item = new DataClass(name, S_Location, Blood_g,uri);
                                            itemList.add(item);
                                        }
                                    }
                                } else {
                                    if (!Objects.isNull(Gender)) {
                                        boolean res=age_finder.equals_rt(age,Age);
                                        if (S_Gender.equals(Gender) && res)
                                        {
                                            System.out.println("opt1 and opt3 is chosen");
                                            DataClass item = new DataClass(name, S_Location, Blood_g,uri);
                                            itemList.add(item);
                                        }
                                    } else {
                                        boolean res=age_finder.equals_rt(age,Age);
                                        if (res)
                                        {
                                            System.out.println("Only opt1 is chosen");
                                            DataClass item = new DataClass(name, S_Location, Blood_g,uri);
                                            itemList.add(item);
                                        }
                                    }
                                }
                            } else {
                                if (!Objects.isNull(S_Blood_G)) {
                                    if (!Objects.isNull(Gender)) {
                                        System.out.println("opt2 and opt3 is chosen");
                                        if (Blood_g.equals(S_Blood_G) && S_Gender.equals(Gender))
                                        {
                                            DataClass item = new DataClass(name, S_Location, Blood_g,uri);
                                            itemList.add(item);
                                        }
                                    } else {
                                        System.out.println("Only opt2 is chosen");
                                        if (Blood_g.equals(S_Blood_G)) {
                                            DataClass item = new DataClass(name, S_Location, Blood_g,uri);
                                            itemList.add(item);
                                        }
                                    }
                                } else {
                                    if (!Objects.isNull(Gender)) {
                                        System.out.println("Opt3 is only chosen");
                                        if (Gender.equals(S_Gender)) {
                                            DataClass item = new DataClass(name, S_Location, Blood_g,uri);
                                            itemList.add(item);
                                        }
                                    } else {
                                        flag=false;
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
                if (itemList.isEmpty() && flag) {
                    empty_res.setVisibility(View.VISIBLE);
                    Toast.makeText(home_page.this, "Empty results", Toast.LENGTH_SHORT).show();
                }
                if(!flag)
                {
                    Toast.makeText(home_page.this,"Empty Inputs",Toast.LENGTH_SHORT).show();
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
        Uri uri=data.getUri();
        String S_uri=uri.toString();
        intent.putExtra("Image_uri",S_uri);
        startActivity(intent);

    }

    @Override
    public void onItemClick1(int position) {
        String S_uri="";
        DataClass2 data=itemList1.get(position);
        String name=data.getDataName();
        String blood= data.getDataBlood();
        String location=data.getDataLocation();
        String txt= data.getTxt();
        if (!Objects.isNull(data.getUri())) {
            Uri uri = data.getUri();
            S_uri = uri.toString();
        }
        Intent intent=new Intent(home_page.this, Request_donor_info.class);
        intent.putExtra("Name",name);
        intent.putExtra("Blood",blood);
        intent.putExtra("Location",location);
        intent.putExtra("Txt",txt);
        intent.putExtra("Image_uri",S_uri);
        startActivity(intent);
        System.out.println(name+"\n"+blood+"\n"+location+"\n"+txt);
    }

    private void logout() {
        // Clear the saved credentials
        SharedPreferences sharedPreferences = getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.remove("username");
        editor.remove("password");
        editor.apply();
        Intent intent=new Intent(home_page.this, Login_page.class);
        startActivity(intent);
    }

}