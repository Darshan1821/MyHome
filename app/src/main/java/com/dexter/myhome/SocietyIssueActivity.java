package com.dexter.myhome;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

public class SocietyIssueActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_society_issue);

        getSupportActionBar().setTitle("Society Issues");
    }
}
