package com.dexter.myhome;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;

import com.dexter.myhome.auth.LoginActivity;
import com.dexter.myhome.event.EventActivity;
import com.dexter.myhome.issue.IssueActivity;
import com.dexter.myhome.maintenance.MaintenanceActivity;
import com.dexter.myhome.meeting.MeetingActivity;
import com.dexter.myhome.menu.AboutActivity;
import com.dexter.myhome.menu.ApartmentInfoActivity;
import com.dexter.myhome.menu.ProfileActivity;
import com.dexter.myhome.model.ApartmentInfo;
import com.dexter.myhome.model.Country;
import com.dexter.myhome.model.GsonRequest;
import com.dexter.myhome.util.AppConstants;
import com.dexter.myhome.util.CommonUtil;
import com.dexter.myhome.util.RequestHandler;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.surveymonkey.surveymonkeyandroidsdk.SurveyMonkey;

public class MainActivity extends AppCompatActivity {

    private CardView maintenance;
    private CardView meetings;
    private CardView events;
    private CardView societyIssues;
    private FirebaseAuth mAuth;
    private DatabaseReference apartmentInfoReference;
    private ProgressBar progress;
    public static final String MY_APP = "My Home";
    public static final String SURVEY_HASH = "7K5WGDL";
    public static final int SM_REQUEST_CODE = 0;
    private AlertDialog.Builder builder;
    private SurveyMonkey surveyMonkey = new SurveyMonkey();

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
        progress = findViewById(R.id.progress);
        mAuth = FirebaseAuth.getInstance();
        apartmentInfoReference = FirebaseDatabase.getInstance(AppConstants.FIREBASE_DB_URL).getReference("Users")
                .child(mAuth.getCurrentUser().getUid()).child("ApartmentInfo");

        maintenance.setOnClickListener(v -> {
            progress.setVisibility(View.VISIBLE);
            CommonUtil.setWindowNotClickable(getWindow());
            apartmentInfoReference.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists()) {
                        Intent meetingIntent = new Intent(MainActivity.this, MaintenanceActivity.class);
                        ApartmentInfo apartmentInfo = dataSnapshot.getValue(ApartmentInfo.class);
                        meetingIntent.putExtra("societyName", apartmentInfo.getSocietyName());
                        startActivity(meetingIntent);
                        progress.setVisibility(View.INVISIBLE);
                    } else {
                        progress.setVisibility(View.INVISIBLE);
                        CommonUtil.setWindowClickable(getWindow());
                        Toast.makeText(getApplicationContext(), "Save Apartment Info !", Toast.LENGTH_LONG).show();
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    Toast.makeText(getApplicationContext(), databaseError.toString(), Toast.LENGTH_LONG).show();
                }
            });
        });

        meetings.setOnClickListener(v -> {
            progress.setVisibility(View.VISIBLE);
            CommonUtil.setWindowNotClickable(getWindow());
            apartmentInfoReference.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if(dataSnapshot.exists()){
                        Intent meetingIntent = new Intent(MainActivity.this, MeetingActivity.class);
                        ApartmentInfo apartmentInfo = dataSnapshot.getValue(ApartmentInfo.class);
                        meetingIntent.putExtra("societyName", apartmentInfo.getSocietyName());
                        startActivity(meetingIntent);
                        progress.setVisibility(View.INVISIBLE);
                    } else {
                        progress.setVisibility(View.INVISIBLE);
                        CommonUtil.setWindowClickable(getWindow());
                        Toast.makeText(getApplicationContext(), "Save Apartment Info !", Toast.LENGTH_LONG).show();
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    Toast.makeText(getApplicationContext(), databaseError.toString(), Toast.LENGTH_LONG).show();
                }
            });
        });

        events.setOnClickListener(v -> {
            progress.setVisibility(View.VISIBLE);
            CommonUtil.setWindowNotClickable(getWindow());
            apartmentInfoReference.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists()) {
                        Intent eventIntent = new Intent(MainActivity.this, EventActivity.class);
                        ApartmentInfo apartmentInfo = dataSnapshot.getValue(ApartmentInfo.class);
                        eventIntent.putExtra("societyName", apartmentInfo.getSocietyName());
                        startActivity(eventIntent);
                        progress.setVisibility(View.INVISIBLE);
                    } else {
                        progress.setVisibility(View.INVISIBLE);
                        CommonUtil.setWindowClickable(getWindow());
                        Toast.makeText(getApplicationContext(), "Save Apartment Info !", Toast.LENGTH_LONG).show();
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    Toast.makeText(getApplicationContext(), databaseError.toString(), Toast.LENGTH_LONG).show();
                }
            });
        });

        societyIssues.setOnClickListener(v -> {
            progress.setVisibility(View.VISIBLE);
            CommonUtil.setWindowNotClickable(getWindow());
            apartmentInfoReference.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists()) {
                        Intent issueIntent = new Intent(MainActivity.this, IssueActivity.class);
                        ApartmentInfo apartmentInfo = dataSnapshot.getValue(ApartmentInfo.class);
                        issueIntent.putExtra("societyName", apartmentInfo.getSocietyName());
                        startActivity(issueIntent);
                        progress.setVisibility(View.INVISIBLE);
                    } else {
                        progress.setVisibility(View.INVISIBLE);
                        CommonUtil.setWindowClickable(getWindow());
                        Toast.makeText(getApplicationContext(), "Save Apartment Info !", Toast.LENGTH_LONG).show();
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    Toast.makeText(getApplicationContext(), databaseError.toString(), Toast.LENGTH_LONG).show();
                }
            });
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        surveyMonkey.onStart(this, MY_APP, SM_REQUEST_CODE, SURVEY_HASH);
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
                Intent emailIntent = new Intent(android.content.Intent.ACTION_SEND);
                emailIntent.setType("message/rfc822");
                emailIntent.putExtra(Intent.EXTRA_EMAIL, new String[]{"drashtirb@gmail.com", "avanimvanpariya01@gmail.com",
                        "shyamkaila1998@gmail.com", "harshchudasama4106@gmail.com"});
                emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Help");
                startActivity(emailIntent);
                return true;
            case R.id.feedback:
                surveyMonkey.startSMFeedbackActivityForResult(this, SM_REQUEST_CODE, SURVEY_HASH);
                break;
            case R.id.about:
                startActivity(new Intent(MainActivity.this, AboutActivity.class));
                return true;
            case R.id.profile:
                startActivity(new Intent(MainActivity.this, ProfileActivity.class));
                return true;
            case R.id.apartment_info:
                startActivity(new Intent(MainActivity.this, ApartmentInfoActivity.class));
                return true;
            case R.id.logout:
                builder = new AlertDialog.Builder(this);
                builder.setMessage("Are You Sure?");
                builder.setPositiveButton("Yes", (dialog, which) -> {
                    mAuth.signOut();
                    GsonRequest<Country[]> countriesRequest = new GsonRequest<Country[]>(AppConstants.COUNTRIES_URL, Country[].class, null, success -> {
                        Intent loginIntent = new Intent(MainActivity.this, LoginActivity.class);
                        loginIntent.putExtra("countries", success);
                        startActivity(loginIntent);
                        finish();
                    }, error -> {
                        error.printStackTrace();
                        Toast.makeText(getApplicationContext(), "Failed To Fetch Countries", Toast.LENGTH_LONG).show();
                    });

                    RequestHandler.getInstance(getApplicationContext()).addToRequestQueue(countriesRequest);
                }).setNegativeButton("No", (dialog, which) -> {

                }).show();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

}
