package com.example.blooddonation;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
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

import com.bumptech.glide.Glide;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.Timestamp;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

public class home_page extends AppCompatActivity implements OnItemClickListener{

    TextInputLayout Search;
    Integer age;
    String uri="",savedUsername,register,S_Blood_G,Age,Gender,inputText="";
    private static final String PREFS_NAME = "MyPrefs";
    private SharedPreferences sharedPreferences;
    TextInputEditText E_search;
    Button request_btn, donate_btn;
    Boolean res=false;
    ImageView Filter,Menu,user,heading,heading1,empty_res;
    RecyclerView recyclerView,recyclerView1;
    DatabaseReference databaseReference,databaseReference1,databaseReference2;
    private RecyclerViewAdapter adapter;
    private RecyclerViewAdapter1 adapter1;
    private List<DataClass> itemList;
    private List<DataClass2> itemList1;
    TextView Disp,Disp1;

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
        heading1=findViewById(R.id.heading1);
        //recycler views
        recyclerView=findViewById(R.id.recyclerView);
        recyclerView1=findViewById(R.id.recyclerView1);

        GridLayoutManager gridLayoutManager=new GridLayoutManager(home_page.this,1);
        recyclerView.setLayoutManager(gridLayoutManager);

        GridLayoutManager gridLayoutManager1=new GridLayoutManager(home_page.this,1);
        recyclerView1.setLayoutManager(gridLayoutManager1);

        sharedPreferences = getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        savedUsername = sharedPreferences.getString("username", "");

        //Download the image for profile only if it exists
        try {
            downloadImage();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        //Get the data from the intent
        Intent intent=getIntent();
        String username=intent.getStringExtra("username");
        Disp.setText(getResources().getText(R.string.hi)+" "+savedUsername);

        //Fetch the Requester details
        request_fetch();

        //These below buttons alternatively change the details in the screen
        //Request button is for searching Donors
        request_btn.setOnClickListener(view -> {
            request_btn.setBackground(getDrawable(R.drawable.cus_join11));
            donate_btn.setBackground(getDrawable(R.drawable.cus_join2));
            Search.setVisibility(View.VISIBLE);
            Filter.setVisibility(View.VISIBLE);
            heading1.setVisibility(View.VISIBLE);
            recyclerView.setVisibility(View.VISIBLE);
            user.setVisibility(View.INVISIBLE);
            Disp.setVisibility(View.INVISIBLE);
            Disp1.setVisibility(View.INVISIBLE);
            recyclerView1.setVisibility(View.INVISIBLE);
            heading.setVisibility(View.INVISIBLE);
            empty_res.setVisibility(View.INVISIBLE);

        });

        //This button is to see Requester details
        donate_btn.setOnClickListener(view -> {
            request_btn.setBackground(getDrawable(R.drawable.cus_join1));
            donate_btn.setBackground(getDrawable(R.drawable.cus_join22));
            Search.setVisibility(View.INVISIBLE);
            Filter.setVisibility(View.INVISIBLE);
            heading1.setVisibility(View.INVISIBLE);
            recyclerView.setVisibility(View.INVISIBLE);
            user.setVisibility(View.VISIBLE);
            Disp.setVisibility(View.VISIBLE);
            Disp1.setVisibility(View.VISIBLE);
            recyclerView1.setVisibility(View.VISIBLE);
            heading.setVisibility(View.VISIBLE);
            Disp.setText(getResources().getText(R.string.hi)+" "+savedUsername);
            request_fetch();
        });

        //Navigate to the filter page
        Filter.setOnClickListener(view -> {
            Intent intent1 =new Intent(home_page.this, Filter_page.class);
            startActivityForResult(intent1,1);
        });

        //Show the list of menu options
        Menu.setOnClickListener(view -> {
            // Create the floating window
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

            }

            @Override
            public void afterTextChanged(Editable editable) {
                //Get the string value when text is changed
                inputText= E_search.getText().toString().trim().toLowerCase();
            }
        });

        //End Icon in search TextInputLayout
        Search.setEndIconOnClickListener(view -> fetchDataFromFirebase());


        // Initialize Firebase Realtime Database
        databaseReference = FirebaseDatabase.getInstance().getReference("Donars");


        //Recycler View
        itemList = new ArrayList<>();
        itemList1=new ArrayList<>();
        adapter = new RecyclerViewAdapter(itemList,this,this);
        adapter1= new RecyclerViewAdapter1(itemList1,this,this);
        recyclerView.setAdapter(adapter);
        recyclerView1.setAdapter(adapter1);
    }

    //Fetch Requesters Details from Firebase and display
    public void request_fetch()
    {
        // Initialize Firebase Realtime Database
        databaseReference1 = FirebaseDatabase.getInstance().getReference("Request");
        databaseReference1.addValueEventListener(new ValueEventListener() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                itemList1.clear(); // Clear the list to avoid duplicates
                //Empty Result Image set to invisible in default
                empty_res.setVisibility(View.INVISIBLE);
                boolean flag=false;

                //The time function are used to see if the time limit is crossed or not
                //If crossed the node is deleted
                //Else it is shown
                Timestamp firebaseTimestamp = Timestamp.now();

                // Convert Firebase Timestamp to java.util.Date
                Date date = firebaseTimestamp.toDate();
                // Create a formatter for a readable date and time format
                @SuppressLint("SimpleDateFormat") SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                // Format and display the original and updated timestamps
                String formattedOriginalTimestamp = formatter.format(date);
                //Toast.makeText(home_page.this, formattedOriginalTimestamp, Toast.LENGTH_SHORT).show();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    // Parse data from the snapshot
                    String requesterName = snapshot.getKey();
                    String requesterBloodGroup=snapshot.child("RequesterBloodGroup").getValue(String.class);
                    String requesterLocation=snapshot.child("RequesterLocation").getValue(String.class);
                    String requesterReason=snapshot.child("RequesterReason").getValue(String.class);
                    if (snapshot.child("Received").exists() && snapshot.child("Profile").exists())      //remember to change the condition
                    {
                            String time = snapshot.child("Time Remove").getValue(String.class);
                            //getting the result to check if the requester to be deleted or not
                            int res = time.compareTo(formattedOriginalTimestamp);
                            //check the condition
                            if (res > 0) {
                                String url = snapshot.child("Profile").getValue(String.class);
                                DataClass2 item = new DataClass2(requesterName, requesterLocation, requesterBloodGroup, requesterReason, url);
                                itemList1.add(item);
                            }
                            if (res < 0) {
                                DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Request");
                                DatabaseReference nodeToDeleteRef = databaseReference.child(requesterName);

                                // Delete the node
                                nodeToDeleteRef.removeValue()
                                        .addOnSuccessListener(aVoid -> {
                                            // Node deleted successfully
                                        })
                                        .addOnFailureListener(e -> {
                                        });
                            }
                        flag = true;
                    }
                }
                //check the flags to set empty result image
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
    //This is for filter activity and the filter options are got here
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
        else
        {
            try {
                downloadImage();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    //Download the image from firebase
    public void downloadImage() throws IOException {

        //check if profile is present or not
        DatabaseReference reference= FirebaseDatabase.getInstance().getReference().child("Donars");
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot datasnapshot) {
                // Check for the existence
                if (datasnapshot.child(savedUsername).child("Profile").exists())
                {
                    String val=datasnapshot.child(savedUsername).child("Profile").getValue(String.class);
                    res= val.equals("Yes");
                    //If it is present save it in shared preference for later use
                    if (res) {
                        String val_url=datasnapshot.child(savedUsername).child("Profile Value").getValue(String.class);
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.putString("url",val_url).apply();
                        Glide.with(home_page.this).load(val_url).into(user);
                    }
                    //Else set the default user image
                    else
                    {
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.putString("url","No").apply();
                        String val1="https://firebasestorage.googleapis.com/v0/b/mysql-3bcb9.appspot.com/o/Profile%2Fuser_admin%2Fuser_admin.jpg?alt=media&token=806038cd-611b-49fd-b37b-7dd707043ba8";
                        Glide.with(home_page.this).load(val1).into(user);
                    }
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    //Close the app when in home screen
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finishAffinity();
    }

    //PopUp Menu for Menu options
    private void showPopupMenuForMenu() {

        sharedPreferences = getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        register=sharedPreferences.getString("Register","no");
        PopupMenu popupMenu = new PopupMenu(this, Menu);
        popupMenu.getMenu().add("Profile");
        popupMenu.getMenu().add("Request");
        //This is to check if the user is a donor or not
        //If not show the declaration page and full registration page
        if (register.equals("no"))
        {
            popupMenu.getMenu().add("Registration");
        }
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
                startActivityForResult(intent,2);
            }
            else if (option.equals("About"))
            {
                Intent intent=new Intent(home_page.this, About_page.class);
                startActivity(intent);
            }
            else if (option.equals("Registration"))
            {
                Intent intent=new Intent(home_page.this, home_page_Declaration.class);
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

    //Fetch list of donars based on the filter options
    private void fetchDataFromFirebase() {
        databaseReference.addValueEventListener(new ValueEventListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                itemList.clear(); // Clear the list to avoid duplicates
                //Set the empty Image Invisible in default
                empty_res.setVisibility(View.INVISIBLE);
                boolean flag=true;

                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    // Parse data from the snapshot
                    String name = snapshot.getKey();

                    if(snapshot.child("Blood Group").exists() && snapshot.child("Phone No").exists() && snapshot.child("DOB").exists() && snapshot.child("Profile Value").exists()) {
                        //fetch the information of each donor
                        String Blood_g = snapshot.child("Blood Group").getValue(String.class);
                        String S_Gender=snapshot.child("Gender").getValue(String.class);
                        String S_Location=snapshot.child("City").getValue(String.class).toLowerCase();
                        String S_dob=snapshot.child("DOB").getValue(String.class);
                        String S_profile=snapshot.child("Profile Value").getValue(String.class);
                        age_finder age_finder=new age_finder();
                        age=age_finder.age_Cals(S_dob);

                        //display the donors based on the filter options
                        //The Below if consists of Locations
                        if (!inputText.equals(""))
                        {
                            boolean res23=S_Location.toLowerCase().contains(inputText);
                            if (res23)
                            {
                                if (!Objects.isNull(Age)) {
                                    if (!Objects.isNull(S_Blood_G)) {
                                        if (!Objects.isNull(Gender)) {
                                            boolean res=age_finder.equals_rt(age,Age);
                                            if (Blood_g.equals(S_Blood_G) && res && S_Gender.equals(Gender)) {
                                                Log.d("e","Every option is chosen");
                                                DataClass item = new DataClass(name, S_Location, Blood_g, S_profile);
                                                itemList.add(item);
                                            }
                                        } else {
                                            boolean res=age_finder.equals_rt(age,Age);
                                            if (Blood_g.equals(S_Blood_G) && res)
                                            {
                                                System.out.println("Only opt1 and opt2 is chosen");
                                                Log.d("e","Only Age and Blood Group is chosen");
                                                DataClass item = new DataClass(name, S_Location, Blood_g, S_profile);
                                                itemList.add(item);
                                            }
                                        }
                                    } else {
                                        if (!Objects.isNull(Gender)) {
                                            boolean res=age_finder.equals_rt(age,Age);
                                            if (S_Gender.equals(Gender) && res)
                                            {
                                                Log.d("e","Only Age and Gender is chosen");
                                                DataClass item = new DataClass(name, S_Location, Blood_g, S_profile);
                                                itemList.add(item);
                                            }

                                        } else {
                                            boolean res=age_finder.equals_rt(age,Age);
                                            if (res)
                                            {
                                                Log.d("e","Only Age is chosen");
                                                DataClass item = new DataClass(name, S_Location, Blood_g, S_profile);
                                                itemList.add(item);
                                            }
                                        }
                                    }
                                } else {
                                    if (!Objects.isNull(S_Blood_G)) {
                                        if (!Objects.isNull(Gender)) {
                                            Log.d("e","Only Blood Group and Gender is Chosen");
                                            if (Blood_g.equals(S_Blood_G) && S_Gender.equals(Gender))
                                            {
                                                DataClass item = new DataClass(name, S_Location, Blood_g, S_profile);
                                                itemList.add(item);
                                            }
                                        } else {
                                            Log.d("e","Only Blood Group is chosen");
                                            if (Blood_g.equals(S_Blood_G)) {
                                                DataClass item = new DataClass(name, S_Location, Blood_g, S_profile);
                                                itemList.add(item);
                                            }
                                        }
                                    } else {
                                        if (!Objects.isNull(Gender)) {
                                            Log.d("e","Only Gender is chosen");
                                            if (Gender.equals(S_Gender)) {
                                                DataClass item = new DataClass(name, S_Location, Blood_g, S_profile);
                                                itemList.add(item);
                                            }
                                        } else {
                                            DataClass item = new DataClass(name, S_Location, Blood_g, S_profile);
                                            itemList.add(item);
                                            Log.d("e","No option is Chosen");
                                        }
                                    }
                                }
                            }
                        }
                        //The Else Condition contains only the filter options
                        //Not the locations
                        else
                        {
                            if (!Objects.isNull(Age)) {
                                if (!Objects.isNull(S_Blood_G)) {
                                    if (!Objects.isNull(Gender)) {
                                        boolean res=age_finder.equals_rt(age,Age);
                                        if (Blood_g.equals(S_Blood_G) && res && S_Gender.equals(Gender)) {
                                            Log.d("e","Every option is chosen");
                                            DataClass item = new DataClass(name, S_Location, Blood_g, S_profile);
                                            itemList.add(item);
                                        }
                                    } else {
                                        boolean res=age_finder.equals_rt(age,Age);
                                        if (Blood_g.equals(S_Blood_G) && res)
                                        {
                                            Log.d("e","Age and Blood Group is chosen");
                                            DataClass item = new DataClass(name, S_Location, Blood_g, S_profile);
                                            itemList.add(item);
                                        }
                                    }
                                } else {
                                    if (!Objects.isNull(Gender)) {
                                        boolean res=age_finder.equals_rt(age,Age);
                                        if (S_Gender.equals(Gender) && res)
                                        {
                                            Log.d("e","Age and Gender is Chosen");
                                            DataClass item = new DataClass(name, S_Location, Blood_g, S_profile);
                                            itemList.add(item);
                                        }
                                    } else {
                                        boolean res=age_finder.equals_rt(age,Age);
                                        if (res)
                                        {
                                            Log.d("e","Only Age is Chosen");
                                            DataClass item = new DataClass(name, S_Location, Blood_g, S_profile);
                                            itemList.add(item);
                                        }
                                    }
                                }
                            } else {
                                if (!Objects.isNull(S_Blood_G)) {
                                    if (!Objects.isNull(Gender)) {
                                        Log.d("e","Only Blood Group and Gender us chosen");
                                        if (Blood_g.equals(S_Blood_G) && S_Gender.equals(Gender))
                                        {
                                            DataClass item = new DataClass(name, S_Location, Blood_g, S_profile);
                                            itemList.add(item);
                                        }
                                    } else {
                                        Log.d("e","Only Blood Group is chosen");
                                        if (Blood_g.equals(S_Blood_G)) {
                                            DataClass item = new DataClass(name, S_Location, Blood_g, S_profile);
                                            itemList.add(item);
                                        }
                                    }
                                } else {
                                    if (!Objects.isNull(Gender)) {
                                        Log.d("e","Only Gender is Chosen");
                                        if (Gender.equals(S_Gender)) {
                                            DataClass item = new DataClass(name, S_Location, Blood_g, S_profile);
                                            itemList.add(item);
                                        }
                                    } else {
                                        flag=false;
                                        Log.d("e","No Option is chosen");
                                    }
                                }
                            }
                        }
                    }
                    //The below does nothing as we only take donors to display
                    else
                    {
                        Log.d("e","The person is not a donor");
                    }
                }
                //Set the empty image visibility based on the flags
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

    //This is for the Donor info page
    //Each item clicked will consists of details and send it to donor info page
    @Override
    public void onItemClick(int position) {
        DataClass data=itemList.get(position);
        String name=data.getDataName();
        String blood= data.getDataBlood();
        String profile_url=data.getDataURL();
        Intent intent=new Intent(home_page.this,Donor_info_pg.class);
        intent.putExtra("Name",name);
        intent.putExtra("Blood",blood);
        intent.putExtra("URL_P",profile_url);
        startActivity(intent);

    }

    //This is for the Requester info page
    //Each item clicked will consists of details and send it to Requester info page
    @Override
    public void onItemClick1(int position) {
        DataClass2 data=itemList1.get(position);
        String name=data.getDataName();
        String blood= data.getDataBlood();
        String location=data.getDataLocation();
        String txt= data.getTxt();
        String profile_url=data.getUri();
        Intent intent=new Intent(home_page.this, Request_donor_info.class);
        intent.putExtra("Name",name);
        intent.putExtra("Blood",blood);
        intent.putExtra("Location",location);
        intent.putExtra("Txt",txt);
        intent.putExtra("URL_P",profile_url);
        startActivity(intent);
        System.out.println(name+"\n"+blood+"\n"+location+"\n"+txt);
    }

    //For Logout
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