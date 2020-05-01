package com.dexter.myhome.menu;

import android.os.Bundle;
import android.text.InputType;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.dexter.myhome.R;
import com.dexter.myhome.model.Profile;
import com.dexter.myhome.util.AppConstants;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class ProfileActivity extends AppCompatActivity {

    private EditText name;
    private EditText mobile;
    private EditText email;
    private RadioGroup gender;
    private FloatingActionButton editProfile;
    private Boolean editMode = Boolean.FALSE;
    private FirebaseAuth mAuth;
    private DatabaseReference profileReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        getSupportActionBar().setTitle("Profile");

        name = findViewById(R.id.name);
        mobile = findViewById(R.id.mobile);
        email = findViewById(R.id.email);
        gender = findViewById(R.id.gender);
        editProfile = findViewById(R.id.editProfile);
        mAuth = FirebaseAuth.getInstance();
        profileReference = FirebaseDatabase.getInstance(AppConstants.FIREBASE_DB_URL).getReference("Users")
                .child(mAuth.getCurrentUser().getUid()).child("Profile");

        editProfile.setOnClickListener(v -> {
            if (!editMode) {
                editMode = Boolean.TRUE;
                enableAllEditText();
                editProfile.setImageResource(R.drawable.baseline_done_white_48);
            } else if (editMode) {
                editMode = Boolean.FALSE;
                editProfile.setImageResource(R.drawable.baseline_create_white_48);
                disableAllEditText();
                saveProfile();
                Toast.makeText(getApplicationContext(), "Profile Saved !", Toast.LENGTH_LONG).show();
            }
        });

        getProfile();

        disableAllEditText();
    }

    private void getProfile() {
        profileReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Profile profile = dataSnapshot.getValue(Profile.class);
                name.setText(profile.getName());
                mobile.setText(profile.getMobile());
                email.setText(profile.getEmail());
                if (profile.getGender().equals("Male")) {
                    gender.getChildAt(0).setSelected(Boolean.TRUE);
                } else {
                    gender.getChildAt(1).setSelected(Boolean.TRUE);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void saveProfile() {
        Profile profile = new Profile();
        profile.setName(name.getText().toString());
        profile.setMobile(mAuth.getCurrentUser().getPhoneNumber());
        profile.setEmail(email.getText().toString());
        String genderVal = gender.getCheckedRadioButtonId() == R.id.male ? "Male" : "Female";
        profile.setGender(genderVal);
        profileReference.setValue(profile);
    }

    private void disableAllEditText() {
        name.setInputType(InputType.TYPE_NULL);
        mobile.setInputType(InputType.TYPE_NULL);
        email.setInputType(InputType.TYPE_NULL);
        gender.getChildAt(0).setEnabled(Boolean.FALSE);
        gender.getChildAt(1).setEnabled(Boolean.FALSE);
    }

    private void enableAllEditText() {
        name.setInputType(InputType.TYPE_CLASS_TEXT);
        mobile.setInputType(InputType.TYPE_NULL);
        mobile.setEnabled(Boolean.FALSE);
        email.setInputType(InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS);
        gender.getChildAt(0).setEnabled(Boolean.TRUE);
        gender.getChildAt(1).setEnabled(Boolean.TRUE);
        name.requestFocus();
    }
}
