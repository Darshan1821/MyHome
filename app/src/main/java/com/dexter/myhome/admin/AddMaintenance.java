package com.dexter.myhome.admin;

import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatSpinner;

import com.dexter.myhome.R;
import com.dexter.myhome.model.Maintenance;
import com.dexter.myhome.util.AppConstants;
import com.dexter.myhome.util.CommonUtil;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Map;

public class AddMaintenance extends AppCompatActivity {

    private AppCompatSpinner societies;
    private AppCompatSpinner apartments;
    private String societyName;
    private String apartmentName;
    private EditText maintenance;
    private AppCompatButton addMaintenance;
    private DatabaseReference userReference;
    private List<String> userIds;
    private AppCompatSpinner months;
    private String monthName;
    private DatabaseReference maintenanceReference;
    private List<Maintenance> maintenances;
    private ProgressBar progress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_maintenance);

        societies = findViewById(R.id.societies);
        apartments = findViewById(R.id.apartments);
        maintenance = findViewById(R.id.maintenance);
        addMaintenance = findViewById(R.id.add_maintenance);
        userIds = new ArrayList<>();
        months = findViewById(R.id.months);
        maintenances = new ArrayList<>();
        progress = findViewById(R.id.progress);

        userReference = FirebaseDatabase.getInstance(AppConstants.FIREBASE_DB_URL).getReference("Users");

        getSupportActionBar().setTitle("Add Maintenance");

        apartments.setEnabled(Boolean.FALSE);
        months.setEnabled(Boolean.FALSE);
        maintenance.setInputType(InputType.TYPE_NULL);

        societies.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, AppConstants.SOCITIES));
        societies.setSelection(0);

        apartments.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, AppConstants.APARTMENTS));
        apartments.setSelection(0);

        months.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, AppConstants.MONTHS));
        months.setSelection(0);

        societies.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position != 0) {
                    societyName = AppConstants.SOCITIES[position];
                    apartments.setEnabled(Boolean.TRUE);
                } else {
                    apartments.setEnabled(Boolean.FALSE);
                    months.setEnabled(Boolean.FALSE);
                    maintenance.setInputType(InputType.TYPE_NULL);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        apartments.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position != 0) {
                    apartmentName = AppConstants.APARTMENTS[position];
                    months.setEnabled(Boolean.TRUE);
                } else {
                    months.setEnabled(Boolean.FALSE);
                    maintenance.setInputType(InputType.TYPE_NULL);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        months.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position != 0) {
                    monthName = AppConstants.MONTHS[position];
                    maintenance.setInputType(InputType.TYPE_CLASS_NUMBER);
                } else {
                    maintenance.setInputType(InputType.TYPE_NULL);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        addMaintenance.setOnClickListener(v -> {
            progress.setVisibility(View.VISIBLE);
            CommonUtil.setWindowNotClickable(getWindow());

            userReference.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists()) {
                        for (DataSnapshot data : dataSnapshot.getChildren()) {
                            Map<String, Object> userMap = (Map<String, Object>) data.getValue();
                            Map<String, Object> apartmentMap = (Map<String, Object>) userMap.get("ApartmentInfo");
                            Map<String, Object> profileMap = (Map<String, Object>) userMap.get("Profile");
                            Boolean isAdmin = (Boolean) profileMap.get("admin");

                            if (!isAdmin) {
                                String society = (String) apartmentMap.get("societyName");
                                String apartment = (String) apartmentMap.get("apartmentName");
                                if (society.equals(societyName)
                                        && apartment.equals(apartmentName)) {
                                    userIds.add((String) profileMap.get("userId"));
                                }
                            }
                        }

                        if (!userIds.isEmpty()) {
                            addMaintenanceForTenants();
                        } else {
                            progress.setVisibility(View.INVISIBLE);
                            CommonUtil.setWindowClickable(getWindow());
                        }
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    progress.setVisibility(View.INVISIBLE);
                    CommonUtil.setWindowClickable(getWindow());
                }
            });
        });
    }

    private void addMaintenanceForTenants() {
        for (String userId : userIds) {
            maintenanceReference = userReference.child(userId).child("Maintenance");

            maintenanceReference.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists()) {
                        dataSnapshot.getChildren().forEach(d -> {
                            Maintenance maintenance = d.getValue(Maintenance.class);
                            maintenances.add(maintenance);
                        });
                        maintenanceReference.removeValue().addOnCompleteListener(task -> {
                            Maintenance userMaintenance = new Maintenance();
                            userMaintenance.setMonth(monthName);
                            userMaintenance.setYear(Integer.valueOf(Calendar.getInstance().get(Calendar.YEAR)).toString());
                            userMaintenance.setCharge(Double.parseDouble(maintenance.getText().toString()));
                            maintenances.add(userMaintenance);
                            maintenances.forEach(m -> {
                                maintenanceReference.push().setValue(m);
                            });
                            progress.setVisibility(View.INVISIBLE);
                            CommonUtil.setWindowClickable(getWindow());
                            startActivity(new Intent(AddMaintenance.this, AdminActivity.class));
                        });
                    } else {
                        Maintenance userMaintenance = new Maintenance();
                        userMaintenance.setMonth(monthName);
                        userMaintenance.setYear(Integer.valueOf(Calendar.getInstance().get(Calendar.YEAR)).toString());
                        userMaintenance.setCharge(Double.parseDouble(maintenance.getText().toString()));
                        maintenanceReference.push().setValue(userMaintenance);
                        progress.setVisibility(View.INVISIBLE);
                        CommonUtil.setWindowClickable(getWindow());
                        startActivity(new Intent(AddMaintenance.this, AdminActivity.class));
                    }
                    Toast.makeText(getApplicationContext(), "Maintenance Added !", Toast.LENGTH_LONG).show();
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    progress.setVisibility(View.INVISIBLE);
                    CommonUtil.setWindowClickable(getWindow());
                }
            });
        }
    }
}
