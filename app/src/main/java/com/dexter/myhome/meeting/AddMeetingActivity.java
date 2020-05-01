package com.dexter.myhome.meeting;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import com.dexter.myhome.R;
import com.dexter.myhome.model.Meeting;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Calendar;
import java.util.Date;

public class AddMeetingActivity extends AppCompatActivity {

    private EditText title;
    private EditText description;
    private ImageView selectDate;
    private TextView date;
    private int year;
    private int month;
    private int day;
    private Date dateVal;
    private AppCompatButton addMeeting;
    private String userName;
    private String societyName;
    private DatabaseReference meetingReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_meeting);

        getSupportActionBar().setTitle("Add Meeting");

        title = findViewById(R.id.meeting_title);
        description = findViewById(R.id.meeting_description);
        date = findViewById(R.id.meeting_date);
        dateVal = new Date();

        setDefaultDate();

        userName = getIntent().getExtras().get("userName").toString();
        societyName = getIntent().getExtras().get("societyName").toString();

        meetingReference = FirebaseDatabase.getInstance().getReference("Societies")
                .child(societyName).child("Meetings");

        selectDate = findViewById(R.id.select_meeting_date);
        addMeeting = findViewById(R.id.add_meeting);

        selectDate.setOnClickListener(v -> {
            Calendar cal = Calendar.getInstance();
            year = cal.get(Calendar.YEAR);
            month = cal.get(Calendar.MONTH);
            day = cal.get(Calendar.DAY_OF_MONTH);

            DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                    (view, year, monthOfYear, dayOfMonth) -> {
                        date.setText(dayOfMonth + "-" + (monthOfYear + 1) + "-" + year);
                        cal.set(Calendar.YEAR, year);
                        cal.set(Calendar.MONTH, monthOfYear);
                        cal.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                        dateVal = cal.getTime();
                    }, year, month, day);
            datePickerDialog.show();
        });

        addMeeting.setOnClickListener(v -> {

            if (TextUtils.isEmpty(title.getText().toString())) {
                title.setError("Enter Title !");
                title.requestFocus();
                return;
            }

            if (TextUtils.isEmpty(description.getText().toString())) {
                description.setError("Enter Description !");
                description.requestFocus();
                return;
            }

            Meeting meeting = new Meeting();
            meeting.setTitle(title.getText().toString());
            meeting.setDescription(description.getText().toString());
            meeting.setMeetingDate(dateVal);
            meeting.setOrganizer(userName);
            meetingReference.push().setValue(meeting);
            Toast.makeText(getApplicationContext(), "Meeting Added !", Toast.LENGTH_LONG).show();

            Intent meetingIntent = new Intent(AddMeetingActivity.this, MeetingActivity.class);
            meetingIntent.putExtra("societyName", societyName);
            startActivity(meetingIntent);
            finish();
        });
    }

    private void setDefaultDate() {
        Calendar cal = Calendar.getInstance();
        year = cal.get(Calendar.YEAR);
        month = cal.get(Calendar.MONTH);
        day = cal.get(Calendar.DAY_OF_MONTH);

        date.setText(day + "-" + (month + 1) + "-" + year);
    }
}
