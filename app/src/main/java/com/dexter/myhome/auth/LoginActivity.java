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
import android.widget.Spinner;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import com.dexter.myhome.MainActivity;
import com.dexter.myhome.R;
import com.dexter.myhome.model.Country;
import com.dexter.myhome.util.AppConstants;
import com.dexter.myhome.util.ToastUtil;
import com.google.firebase.auth.FirebaseAuth;

import java.util.HashMap;

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
            Intent mainActivityIntent = new Intent(LoginActivity.this, MainActivity.class);
            startActivity(mainActivityIntent);
        }
    }


}
