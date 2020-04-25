package com.dexter.myhome;

import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class ProfileActivity extends AppCompatActivity {

    private EditText name;
    private EditText mobile;
    private EditText email;
    private RadioGroup gender;
    private FloatingActionButton editProfile;
    private Boolean editMode = Boolean.FALSE;

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

        editProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!editMode) {
                    editMode = Boolean.TRUE;
                    enableAllEditText();
                    editProfile.setImageResource(R.drawable.baseline_done_white_48);
                } else if (editMode) {
                    editMode = Boolean.FALSE;
                    editProfile.setImageResource(R.drawable.baseline_create_white_48);
                    disableAllEditText();
                    Toast.makeText(getApplicationContext(), "Profile Saved !", Toast.LENGTH_LONG).show();
                }
            }
        });

        disableAllEditText();
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
        email.setInputType(InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS);
        gender.getChildAt(0).setEnabled(Boolean.TRUE);
        gender.getChildAt(1).setEnabled(Boolean.TRUE);
        name.setFocusable(Boolean.TRUE);
    }
}
