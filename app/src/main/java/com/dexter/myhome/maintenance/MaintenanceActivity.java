package com.dexter.myhome.maintenance;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.dexter.myhome.R;

public class MaintenanceActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maintenance);

        getSupportActionBar().setTitle("Maintenance");
    }
}
