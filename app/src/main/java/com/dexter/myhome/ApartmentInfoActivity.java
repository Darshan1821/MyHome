package com.dexter.myhome;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatSpinner;

import com.dexter.myhome.model.ApartmentInfo;
import com.dexter.myhome.util.AppConstants;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Arrays;

public class ApartmentInfoActivity extends AppCompatActivity {

    private AppCompatSpinner societies;
    private AppCompatSpinner apartments;
    private AppCompatSpinner flats;
    private AppCompatButton save;
    private String societyName;
    private String apartmentName;
    private String flatName;
    private FirebaseAuth mAuth;
    private DatabaseReference apartmentInfoReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_apartment_info);

        societies = findViewById(R.id.societies);
        apartments = findViewById(R.id.apartments);
        flats = findViewById(R.id.flats);
        save = findViewById(R.id.save_apartment_info);
        mAuth = FirebaseAuth.getInstance();
        apartmentInfoReference = FirebaseDatabase.getInstance(AppConstants.FIREBASE_DB_URL).getReference("Users")
                .child(mAuth.getCurrentUser().getUid()).child("ApartmentInfo");

        getSupportActionBar().setTitle("Apartment Info");

        apartments.setEnabled(Boolean.FALSE);
        flats.setEnabled(Boolean.FALSE);

        societies.setAdapter(new ArrayAdapter<>(this,android.R.layout.simple_spinner_dropdown_item, AppConstants.SOCITIES));
        societies.setSelection(0);

        apartments.setAdapter(new ArrayAdapter<>(this,android.R.layout.simple_spinner_dropdown_item, AppConstants.APARTMENTS));
        apartments.setSelection(0);

        flats.setAdapter(new ArrayAdapter<>(this,android.R.layout.simple_spinner_dropdown_item, AppConstants.FLATS));
        flats.setSelection(0);

        societies.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(position != 0){
                    societyName = AppConstants.SOCITIES[position];
                    apartments.setEnabled(Boolean.TRUE);
                } else {
                    apartments.setEnabled(Boolean.FALSE);
                    flats.setEnabled(Boolean.FALSE);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        apartments.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(position != 0){
                    apartmentName = AppConstants.APARTMENTS[position];
                    flats.setEnabled(Boolean.TRUE);
                } else {
                    flats.setEnabled(Boolean.FALSE);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        flats.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(position != 0){
                    flatName = AppConstants.FLATS[position];
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        getApartmentInfo();

        save.setOnClickListener(v -> {
            ApartmentInfo apartmentInfo = new ApartmentInfo();
            apartmentInfo.setSocietyName(societyName);
            apartmentInfo.setApartmentName(apartmentName);
            apartmentInfo.setFlatName(flatName);
            apartmentInfoReference.setValue(apartmentInfo);
            Toast.makeText(getApplicationContext(),"Apartment Info Saved !", Toast.LENGTH_LONG).show();
        });
    }

    private void getApartmentInfo() {
        apartmentInfoReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    ApartmentInfo apartmentInfo = dataSnapshot.getValue(ApartmentInfo.class);
                    societies.setSelection(Arrays.asList(AppConstants.SOCITIES).indexOf(apartmentInfo.getSocietyName()));
                    apartments.setSelection(Arrays.asList(AppConstants.APARTMENTS).indexOf(apartmentInfo.getApartmentName()));
                    flats.setSelection(Arrays.asList(AppConstants.FLATS).indexOf(apartmentInfo.getFlatName()));
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
