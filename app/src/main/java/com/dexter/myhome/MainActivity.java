package com.dexter.myhome;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;

import com.dexter.myhome.auth.LoginActivity;
import com.dexter.myhome.model.ApartmentInfo;
import com.dexter.myhome.model.Country;
import com.dexter.myhome.model.GsonRequest;
import com.dexter.myhome.model.Profile;
import com.dexter.myhome.util.AppConstants;
import com.dexter.myhome.util.RequestHandler;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

public class MainActivity extends AppCompatActivity {

    private CardView maintenance;
    private CardView meetings;
    private CardView events;
    private CardView societyIssues;
    private FirebaseAuth mAuth;
    private DatabaseReference userReference;
    private String userID;
    private DatabaseReference apartmentInfoReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        maintenance = findViewById(R.id.maintenance);
        meetings = findViewById(R.id.meetings);
        events = findViewById(R.id.events);
        societyIssues = findViewById(R.id.society_issues);
        mAuth = FirebaseAuth.getInstance();
        userID = mAuth.getCurrentUser().getUid();
        userReference = FirebaseDatabase.getInstance(AppConstants.FIREBASE_DB_URL).getReference("Users");
        apartmentInfoReference = FirebaseDatabase.getInstance(AppConstants.FIREBASE_DB_URL).getReference("Users")
                .child(mAuth.getCurrentUser().getUid()).child("ApartmentInfo");

        maintenance.setOnClickListener(v -> startActivity(new Intent(MainActivity.this, MaintenanceActivity.class)));

        meetings.setOnClickListener(v -> {
            apartmentInfoReference.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if(dataSnapshot.exists()){
                        Intent meetingIntent = new Intent(MainActivity.this, MeetingActivity.class);
                        HashMap<String,String> apartmentMap = (HashMap<String, String>) dataSnapshot.getValue();
                        meetingIntent.putExtra("societyName", apartmentMap.get("societyName"));
                        startActivity(meetingIntent);
                    } else {
                        Toast.makeText(getApplicationContext(), "Save Apartment Info !", Toast.LENGTH_LONG).show();
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        });

        events.setOnClickListener(v -> startActivity(new Intent(MainActivity.this, EventActivity.class)));

        societyIssues.setOnClickListener(v -> startActivity(new Intent(MainActivity.this, SocietyIssueActivity.class)));

        userReference.child(userID).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(!dataSnapshot.exists()){
                    saveUser();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void saveUser() {
        Profile profile = new Profile();
        profile.setName("");
        profile.setMobile(mAuth.getCurrentUser().getPhoneNumber());
        profile.setEmail("");
        profile.setGender("");
        userReference.child(userID).child("Profile").setValue(profile);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        int id = item.getItemId();

        switch (id) {
            case R.id.help:
                Toast.makeText(getApplicationContext(), "Help", Toast.LENGTH_LONG).show();
                return true;
            case R.id.about:
                Toast.makeText(getApplicationContext(), "About", Toast.LENGTH_LONG).show();
                return true;
            case R.id.profile:
                startActivity(new Intent(MainActivity.this, ProfileActivity.class));
                return true;
            case R.id.apartment_info:
                startActivity(new Intent(MainActivity.this, ApartmentInfoActivity.class));
                return true;
            case R.id.logout:
                mAuth.signOut();
                GsonRequest<Country[]> countriesRequest = new GsonRequest<Country[]>(AppConstants.COUNTRIES_URL,Country[].class,null, success -> {
                    Intent loginIntent = new Intent(MainActivity.this, LoginActivity.class);
                    loginIntent.putExtra("countries", success);
                    startActivity(loginIntent);
                    finish();
                }, error -> {
                    error.printStackTrace();
                    Toast.makeText(getApplicationContext(),"Failed To Fetch Countries",Toast.LENGTH_LONG).show();
                });

                RequestHandler.getInstance(getApplicationContext()).addToRequestQueue(countriesRequest);
                return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
