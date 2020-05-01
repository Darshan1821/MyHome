package com.dexter.myhome.meeting;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.dexter.myhome.R;
import com.dexter.myhome.adapter.MeetingAdapter;
import com.dexter.myhome.model.Meeting;
import com.dexter.myhome.model.Profile;
import com.dexter.myhome.util.AppConstants;
import com.dexter.myhome.util.DateUtil;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class MeetingActivity extends AppCompatActivity {

    private String societyName;
    private TextView society;
    private RecyclerView meetingList;
    private Date fromDate;
    private Date toDate;
    private EditText fromDateView;
    private EditText toDateView;
    private ImageView fromDatePicker;
    private ImageView toDatePicker;
    private TextView noMeeting;
    private DatabaseReference meetingReference;
    private List<Meeting> meetings;
    private RecyclerView.Adapter meetingAdapter;
    private RecyclerView.LayoutManager layoutManager;
    private FloatingActionButton addSocietyMeeting;
    private DatabaseReference profileReference;
    private FirebaseAuth mAuth;
    private int year;
    private int month;
    private int day;
    private ProgressBar progress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_meeting);

        getSupportActionBar().setTitle("Meetings");

        societyName = getIntent().getExtras().get("societyName").toString();
        society = findViewById(R.id.society);
        society.setText(societyName);
        fromDate = new Date();
        toDate = new Date();
        meetings = new ArrayList<>();
        addSocietyMeeting = findViewById(R.id.add_society_meeting);
        mAuth = FirebaseAuth.getInstance();
        fromDateView = findViewById(R.id.selected_from_date);
        toDateView = findViewById(R.id.selected_to_date);
        fromDatePicker = findViewById(R.id.select_from_date);
        toDatePicker = findViewById(R.id.select_to_date);
        noMeeting = findViewById(R.id.no_meeting);
        progress = findViewById(R.id.progress);

        fromDateView.setInputType(InputType.TYPE_NULL);
        toDateView.setInputType(InputType.TYPE_NULL);

        setDefaultDates();

        meetingReference = FirebaseDatabase.getInstance().getReference("Societies")
                .child(societyName).child("Meetings");
        profileReference = FirebaseDatabase.getInstance(AppConstants.FIREBASE_DB_URL).getReference("Users")
                .child(mAuth.getCurrentUser().getUid()).child("Profile");

        meetingList = findViewById(R.id.meetings);
        meetingList.setHasFixedSize(Boolean.TRUE);

        layoutManager = new LinearLayoutManager(this);

        meetingList.setLayoutManager(layoutManager);

        meetingAdapter = new MeetingAdapter(this, meetings);
        meetingList.setAdapter(meetingAdapter);

        fetchMeetingsByDate(fromDate, toDate);

        meetingAdapter.registerAdapterDataObserver(new RecyclerView.AdapterDataObserver() {
            @Override
            public void onItemRangeChanged(int positionStart, int itemCount) {
                super.onItemRangeChanged(positionStart, itemCount);
                meetingReference.removeValue();
                meetings.forEach(m -> {
                    meetingReference.push().setValue(m);
                });
            }
        });

        addSocietyMeeting.setOnClickListener(v -> {
            profileReference.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists()) {
                        Profile profile = dataSnapshot.getValue(Profile.class);
                        if (profile.getName() != null && !profile.getName().isEmpty()) {
                            Intent addMeetingIntent = new Intent(MeetingActivity.this, AddMeetingActivity.class);
                            addMeetingIntent.putExtra("userName", profile.getName());
                            addMeetingIntent.putExtra("societyName", societyName);
                            startActivity(addMeetingIntent);
                        } else {
                            Toast.makeText(getApplicationContext(), "Save Name in Profile !", Toast.LENGTH_LONG).show();
                        }
                    } else {
                        Toast.makeText(getApplicationContext(), "Save Profile Info !", Toast.LENGTH_LONG).show();
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        });

        fromDatePicker.setOnClickListener(v -> {
            Calendar cal = Calendar.getInstance();
            year = cal.get(Calendar.YEAR);
            month = cal.get(Calendar.MONTH);
            day = cal.get(Calendar.DAY_OF_MONTH);

            DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                    (view, year, monthOfYear, dayOfMonth) -> {
                        fromDateView.setText(dayOfMonth + "-" + (monthOfYear + 1) + "-" + year);
                        cal.set(Calendar.YEAR, year);
                        cal.set(Calendar.MONTH, monthOfYear);
                        cal.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                        fromDate = cal.getTime();
                        fetchMeetingsByDate(fromDate, toDate);
                    }, year, month, day);
            datePickerDialog.show();
        });

        toDatePicker.setOnClickListener(v -> {
            Calendar cal = Calendar.getInstance();
            year = cal.get(Calendar.YEAR);
            month = cal.get(Calendar.MONTH);
            day = cal.get(Calendar.DAY_OF_MONTH);

            DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                    (view, year, monthOfYear, dayOfMonth) -> {
                        toDateView.setText(dayOfMonth + "-" + (monthOfYear + 1) + "-" + year);
                        cal.set(Calendar.YEAR, year);
                        cal.set(Calendar.MONTH, monthOfYear);
                        cal.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                        toDate = cal.getTime();
                        fetchMeetingsByDate(fromDate, toDate);
                    }, year, month, day);
            datePickerDialog.show();
        });
    }

    private void setDefaultDates() {
        Calendar cal = Calendar.getInstance();
        year = cal.get(Calendar.YEAR);
        month = cal.get(Calendar.MONTH);
        day = cal.get(Calendar.DAY_OF_MONTH);

        fromDateView.setText(day + "-" + (month + 1) + "-" + year);
        toDateView.setText(day + "-" + (month + 1) + "-" + year);
    }

    private void fetchMeetingsByDate(Date fromDate, Date toDate) {
        progress.setVisibility(View.VISIBLE);
        meetings.clear();
        meetingReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    for (DataSnapshot data : dataSnapshot.getChildren()) {
                        Meeting meeting = data.getValue(Meeting.class);
                        if (DateUtil.isDateBetween(fromDate, toDate, meeting.getMeetingDate())) {
                            meetings.add(meeting);
                            meetingAdapter.notifyDataSetChanged();
                        }
                    }
                    if (meetings.isEmpty()) {
                        meetingList.setVisibility(View.INVISIBLE);
                        noMeeting.setVisibility(View.VISIBLE);
                    } else {
                        meetingList.setVisibility(View.VISIBLE);
                        noMeeting.setVisibility(View.INVISIBLE);
                    }
                }
                if (meetings.isEmpty()) {
                    meetingList.setVisibility(View.INVISIBLE);
                    noMeeting.setVisibility(View.VISIBLE);
                } else {
                    meetingList.setVisibility(View.VISIBLE);
                    noMeeting.setVisibility(View.INVISIBLE);
                }
                progress.setVisibility(View.INVISIBLE);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                progress.setVisibility(View.INVISIBLE);
            }
        });
    }
}
