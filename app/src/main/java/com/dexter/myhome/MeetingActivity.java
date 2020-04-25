package com.dexter.myhome;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

public class MeetingActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_meeting);

        getSupportActionBar().setTitle("Meetings");
    }
}
