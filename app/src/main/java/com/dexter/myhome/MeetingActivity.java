package com.dexter.myhome;

import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class MeetingActivity extends AppCompatActivity {

    private String societyName;
    private String apartmentName;
    private String flatName;
    private TextView society;
    private TextView apartment;
    private TextView flat;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_meeting);

        getSupportActionBar().setTitle("Meetings");

        societyName = getIntent().getExtras().get("societyName").toString();

        society = findViewById(R.id.society);

        society.setText(societyName);

    }
}
