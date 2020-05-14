package com.dexter.myhome.auth;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import com.dexter.myhome.MainActivity;
import com.dexter.myhome.R;
import com.dexter.myhome.admin.AdminActivity;
import com.dexter.myhome.model.Country;
import com.dexter.myhome.util.AppConstants;
import com.dexter.myhome.util.CommonUtil;
import com.dexter.myhome.util.ToastUtil;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Map;

public class LoginActivity extends AppCompatActivity {

    private EditText mobileNumber;
    private Animation shakeAnimation;
    private AppCompatButton getOtp;
    private LinearLayout loginLayout;
    private Spinner countriesDropdown;
    private String[] countryNames;
    private final HashMap<String,String> countryMap = new HashMap<>();
    private Country[] countries;
    private int countrySelected = AppConstants.DEFAULT_COUNTRY_SELECTION;
    private FirebaseAuth mAuth;
    private DatabaseReference userReference;
    private ProgressBar progress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mobileNumber = findViewById(R.id.mobile);
        getOtp = findViewById(R.id.get_otp);
        shakeAnimation = AnimationUtils.loadAnimation(this, R.anim.shake);
        loginLayout = findViewById(R.id.login_layout);
        countriesDropdown = findViewById(R.id.countries);
        countries = (Country[]) getIntent().getExtras().get("countries");
        mAuth = FirebaseAuth.getInstance();
        userReference = FirebaseDatabase.getInstance(AppConstants.FIREBASE_DB_URL).getReference("Users");
        progress = findViewById(R.id.progress);

        generateCountryCodes(countries);
        countriesDropdown.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, countryNames));
        countriesDropdown.setSelection(AppConstants.DEFAULT_COUNTRY_SELECTION);

        getOtp.setOnClickListener(v -> {
            String mobile = mobileNumber.getText().toString();

            if (validateLoginFields(mobile, loginLayout)) {
                String country = countryNames[countrySelected];
                String callingCode = countryMap.get(country);
                String mobileNumber = "+" + callingCode + mobile;

                Intent verifyMobileIntent = new Intent(LoginActivity.this, VerifyMobileActivity.class);
                verifyMobileIntent.putExtra("mobile", mobileNumber);
                startActivity(verifyMobileIntent);
            }
        });

        countriesDropdown.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                countrySelected = position;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private void generateCountryCodes(Country[] countries) {
        int count = 0;
        countryNames = new String[countries.length];
        for(Country country : countries) {
            countryNames[count] = country.getName();
            countryMap.put(country.getName(),country.getCallingCodes().get(0));
            ++count;
        }
    }

    private Boolean validateLoginFields(String mobile, LinearLayout loginLayout) {
        Boolean isValid = Boolean.FALSE;

        if (!TextUtils.isEmpty(mobile) && mobile.length() == 10) {
            isValid = Boolean.TRUE;
        } else if (TextUtils.isEmpty(mobile) || mobile.length() < 10) {
            isValid = Boolean.FALSE;
            ToastUtil.showTopToastLong(this, "Invalid Mobile No and Password !");
            loginLayout.startAnimation(shakeAnimation);
        }

        return isValid;
    }

    @Override
    protected void onStart() {
        super.onStart();
        if(mAuth.getCurrentUser() != null){
            progress.setVisibility(View.VISIBLE);
            CommonUtil.setWindowNotClickable(getWindow());
            userReference.child(mAuth.getCurrentUser().getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists()) {
                        Map<String, Object> userMap = (Map<String, Object>) dataSnapshot.getValue();
                        Map<String, Object> profileMap = (Map<String, Object>) userMap.get("Profile");
                        Boolean isAdmin = (Boolean) profileMap.get("admin");
                        if (!isAdmin) {
                            Intent mainActivityIntent = new Intent(LoginActivity.this, MainActivity.class);
                            startActivity(mainActivityIntent);
                        } else {
                            Intent adminActivityIntent = new Intent(LoginActivity.this, AdminActivity.class);
                            startActivity(adminActivityIntent);
                        }
                    } else {
                        Toast.makeText(getApplicationContext(), "No User Found!", Toast.LENGTH_LONG).show();
                    }
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
