package com.dexter.myhome.event;

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
import com.dexter.myhome.model.Event;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Calendar;
import java.util.Date;

public class AddEventActivity extends AppCompatActivity {

    private EditText title;
    private EditText description;
    private ImageView selectDate;
    private TextView date;
    private int year;
    private int month;
    private int day;
    private Date dateVal;
    private AppCompatButton addEvent;
    private String userName;
    private String societyName;
    private DatabaseReference eventReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_event);

        getSupportActionBar().setTitle("Add Meeting");

        title = findViewById(R.id.event_title);
        description = findViewById(R.id.event_description);
        date = findViewById(R.id.event_date);
        dateVal = new Date();

        setDefaultDate();

        userName = getIntent().getExtras().get("userName").toString();
        societyName = getIntent().getExtras().get("societyName").toString();

        eventReference = FirebaseDatabase.getInstance().getReference("Societies")
                .child(societyName).child("Events");

        selectDate = findViewById(R.id.select_event_date);
        addEvent = findViewById(R.id.add_event);

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

        addEvent.setOnClickListener(v -> {

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

            Event meeting = new Event();
            meeting.setTitle(title.getText().toString());
            meeting.setDescription(description.getText().toString());
            meeting.setEventDate(dateVal);
            meeting.setOrganizer(userName);
            eventReference.push().setValue(meeting);
            Toast.makeText(getApplicationContext(), "Event Added !", Toast.LENGTH_LONG).show();

            Intent meetingIntent = new Intent(AddEventActivity.this, EventActivity.class);
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
