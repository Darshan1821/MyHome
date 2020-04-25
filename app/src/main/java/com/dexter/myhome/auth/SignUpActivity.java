package com.dexter.myhome.auth;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import com.dexter.myhome.R;
import com.dexter.myhome.util.ToastUtil;

public class SignUpActivity extends AppCompatActivity {

    private TextView loginPage;
    private AppCompatButton signup;
    private EditText firstName;
    private EditText lastName;
    private EditText mobileNo;
    private EditText password;
    private EditText confirmPassword;
    private RadioGroup gender;
    private LinearLayout signUpLayout;
    private Animation shakeAnimation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        loginPage = findViewById(R.id.account_exists);
        signup = findViewById(R.id.sign_up);
        firstName = findViewById(R.id.first_name);
        lastName = findViewById(R.id.last_name);
        mobileNo = findViewById(R.id.mobile);
        password = findViewById(R.id.password);
        confirmPassword = findViewById(R.id.confirm_password);
        gender = findViewById(R.id.gender);
        signUpLayout = findViewById(R.id.sign_up_layout);
        shakeAnimation = AnimationUtils.loadAnimation(this, R.anim.shake);

        loginPage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(SignUpActivity.this, LoginActivity.class));
                finish();
            }
        });

        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String firstNameVal = firstName.getText().toString();
                String lastNameVal = lastName.getText().toString();
                String mobileNoVal = mobileNo.getText().toString();
                String passwordVal = password.getText().toString();
                String confirmPasswordVal = confirmPassword.getText().toString();
                int genderVal = gender.getCheckedRadioButtonId();

                if (validateSignUpFields(firstNameVal, lastNameVal, mobileNoVal, passwordVal, confirmPasswordVal, genderVal, signUpLayout)) {
                    Toast.makeText(getApplicationContext(), "Registered Successfully!", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(SignUpActivity.this, LoginActivity.class));
                }
            }
        });

        confirmPassword.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {

                String passwordVal = password.getText().toString();
                String confirmPasswordVal = confirmPassword.getText().toString();

                if (!hasFocus && !TextUtils.isEmpty(passwordVal)) {
                    if (!confirmPasswordVal.equals(passwordVal)) {
                        ToastUtil.showTopToastLong(getApplicationContext(), "Password doesn't match");
                        signUpLayout.startAnimation(shakeAnimation);
                    }
                }
            }
        });
    }

    private Boolean validateSignUpFields(String firstNameVal, String lastNameVal, String mobileNoVal, String passwordval, String confirmPasswordVal, int genderVal, LinearLayout signUpLayout) {
        Boolean isValid = Boolean.FALSE;

        if (TextUtils.isEmpty(firstNameVal)) {
            isValid = Boolean.FALSE;
            ToastUtil.showTopToastLong(this, "Enter First Name");
            signUpLayout.startAnimation(shakeAnimation);
        } else if (TextUtils.isEmpty(lastNameVal)) {
            isValid = Boolean.FALSE;
            ToastUtil.showTopToastLong(this, "Enter Last Name");
            signUpLayout.startAnimation(shakeAnimation);
        } else if (TextUtils.isEmpty(mobileNoVal)) {
            isValid = Boolean.FALSE;
            ToastUtil.showTopToastLong(this, "Enter Mobile Number");
            signUpLayout.startAnimation(shakeAnimation);
        } else if (TextUtils.isEmpty(passwordval)) {
            isValid = Boolean.FALSE;
            ToastUtil.showTopToastLong(this, "Enter Password");
            signUpLayout.startAnimation(shakeAnimation);
        } else if (TextUtils.isEmpty(confirmPasswordVal)) {
            isValid = Boolean.FALSE;
            ToastUtil.showTopToastLong(this, "Confirm Password");
            signUpLayout.startAnimation(shakeAnimation);
        } else if (genderVal == -1) {
            isValid = Boolean.FALSE;
            ToastUtil.showTopToastLong(this, "Select Gender");
            signUpLayout.startAnimation(shakeAnimation);
        } else {
            isValid = Boolean.TRUE;
        }

        return isValid;
    }
}
