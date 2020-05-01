package com.dexter.myhome.issue;

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
import com.dexter.myhome.adapter.IssueAdapter;
import com.dexter.myhome.model.Issue;
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

public class IssueActivity extends AppCompatActivity {

    private String societyName;
    private TextView society;
    private RecyclerView issueList;
    private Date fromDate;
    private Date toDate;
    private EditText fromDateView;
    private EditText toDateView;
    private ImageView fromDatePicker;
    private ImageView toDatePicker;
    private TextView noIssues;
    private DatabaseReference profileReference;
    private List<Issue> issues;
    private RecyclerView.Adapter issueAdapter;
    private RecyclerView.LayoutManager layoutManager;
    private FloatingActionButton addSocietyIssue;
    private DatabaseReference issueReference;
    private FirebaseAuth mAuth;
    private int year;
    private int month;
    private int day;
    private ProgressBar progress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_issue);

        getSupportActionBar().setTitle("Issues");

        societyName = getIntent().getExtras().get("societyName").toString();
        society = findViewById(R.id.society);
        society.setText(societyName);
        fromDate = new Date();
        toDate = new Date();
        issues = new ArrayList<>();
        addSocietyIssue = findViewById(R.id.add_society_issue);
        mAuth = FirebaseAuth.getInstance();
        fromDateView = findViewById(R.id.selected_from_date);
        toDateView = findViewById(R.id.selected_to_date);
        fromDatePicker = findViewById(R.id.select_from_date);
        toDatePicker = findViewById(R.id.select_to_date);
        noIssues = findViewById(R.id.no_issue);
        progress = findViewById(R.id.progress);

        fromDateView.setInputType(InputType.TYPE_NULL);
        toDateView.setInputType(InputType.TYPE_NULL);

        setDefaultDates();

        issueReference = FirebaseDatabase.getInstance().getReference("Societies")
                .child(societyName).child("Issues");
        profileReference = FirebaseDatabase.getInstance(AppConstants.FIREBASE_DB_URL).getReference("Users")
                .child(mAuth.getCurrentUser().getUid()).child("Profile");

        issueList = findViewById(R.id.issues);
        issueList.setHasFixedSize(Boolean.TRUE);

        layoutManager = new LinearLayoutManager(this);

        issueList.setLayoutManager(layoutManager);

        issueAdapter = new IssueAdapter(this, issues);
        issueList.setAdapter(issueAdapter);

        fetchEventsByDate(fromDate, toDate);

        issueAdapter.registerAdapterDataObserver(new RecyclerView.AdapterDataObserver() {
            @Override
            public void onItemRangeChanged(int positionStart, int itemCount) {
                super.onItemRangeChanged(positionStart, itemCount);
                issueReference.removeValue();
                issues.forEach(e -> {
                    issueReference.push().setValue(e);
                });
            }
        });

        addSocietyIssue.setOnClickListener(v -> {
            profileReference.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists()) {
                        Profile profile = dataSnapshot.getValue(Profile.class);
                        if (profile.getName() != null && !profile.getName().isEmpty()) {
                            Intent addIssueIntent = new Intent(IssueActivity.this, AddIssueActivity.class);
                            addIssueIntent.putExtra("userName", profile.getName());
                            addIssueIntent.putExtra("societyName", societyName);
                            startActivity(addIssueIntent);
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
                        fetchEventsByDate(fromDate, toDate);
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
                        fetchEventsByDate(fromDate, toDate);
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

    private void fetchEventsByDate(Date fromDate, Date toDate) {
        issues.clear();
        progress.setVisibility(View.VISIBLE);
        issueReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    for (DataSnapshot data : dataSnapshot.getChildren()) {
                        Issue issue = data.getValue(Issue.class);
                        if (DateUtil.isDateBetween(fromDate, toDate, issue.getIssueDate())) {
                            issues.add(issue);
                            issueAdapter.notifyDataSetChanged();
                        }
                    }
                    if (issues.isEmpty()) {
                        noIssues.setVisibility(View.VISIBLE);
                    } else {
                        noIssues.setVisibility(View.INVISIBLE);
                    }
                }
                if (issues.isEmpty()) {
                    noIssues.setVisibility(View.VISIBLE);
                } else {
                    noIssues.setVisibility(View.INVISIBLE);
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
