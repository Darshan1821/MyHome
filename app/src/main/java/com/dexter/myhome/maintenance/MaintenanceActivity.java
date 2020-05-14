package com.dexter.myhome.maintenance;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatSpinner;

import com.dexter.myhome.R;
import com.dexter.myhome.model.Maintenance;
import com.dexter.myhome.util.AppConstants;
import com.dexter.myhome.util.CommonUtil;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Calendar;

public class MaintenanceActivity extends AppCompatActivity {

    private String societyName;
    private TextView society;
    private AppCompatSpinner months;
    private String monthName;
    private AppCompatButton payMaintenance;
    private TextView tenantMaintenance;
    private DatabaseReference maintenanceReference;
    private FirebaseAuth mAuth;
    private ProgressBar progress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maintenance);

        getSupportActionBar().setTitle("Maintenance");

        societyName = getIntent().getExtras().get("societyName").toString();
        society = findViewById(R.id.society);
        society.setText(societyName);
        payMaintenance = findViewById(R.id.pay_maintenance);
        tenantMaintenance = findViewById(R.id.tenant_maintenance);
        months = findViewById(R.id.months);
        mAuth = FirebaseAuth.getInstance();
        maintenanceReference = FirebaseDatabase.getInstance(AppConstants.FIREBASE_DB_URL).getReference("Users")
                .child(mAuth.getCurrentUser().getUid()).child("Maintenance");
        progress = findViewById(R.id.progress);

        months.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, AppConstants.MONTHS));
        months.setSelection(0);
        payMaintenance.setEnabled(Boolean.FALSE);

        months.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position != 0) {
                    monthName = AppConstants.MONTHS[position];
                    getMaintenance();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private void getMaintenance() {
        progress.setVisibility(View.VISIBLE);
        CommonUtil.setWindowNotClickable(getWindow());

        maintenanceReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                Boolean isFound = Boolean.FALSE;
                Double charges = 0.0;
                String year = Integer.valueOf(Calendar.getInstance().get(Calendar.YEAR)).toString();

                if (dataSnapshot.exists()) {
                    for (DataSnapshot data : dataSnapshot.getChildren()) {
                        Maintenance maintenance = data.getValue(Maintenance.class);
                        if (maintenance.getMonth().equals(monthName)
                                && year.equals(maintenance.getYear())) {
                            isFound = Boolean.TRUE;
                            charges = maintenance.getCharge();
                        }
                    }
                }

                if (isFound) {
                    payMaintenance.setEnabled(Boolean.TRUE);
                } else {
                    Toast.makeText(getApplicationContext(), "No charges found !", Toast.LENGTH_LONG).show();
                }

                tenantMaintenance.setText(charges.toString());
                progress.setVisibility(View.INVISIBLE);
                CommonUtil.setWindowClickable(getWindow());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                progress.setVisibility(View.INVISIBLE);
                CommonUtil.setWindowClickable(getWindow());
            }
        });
    }
}
