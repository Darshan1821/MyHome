package com.dexter.myhome.auth;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import com.dexter.myhome.MainActivity;
import com.dexter.myhome.R;
import com.dexter.myhome.util.ToastUtil;

public class LoginActivity extends AppCompatActivity {

    private TextView signUpPage;
    private EditText mobileNumber;
    private EditText password;
    private Animation shakeAnimation;
    private AppCompatButton login;
    private LinearLayout loginLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        signUpPage = findViewById(R.id.no_account);
        mobileNumber = findViewById(R.id.mobile);
        password = findViewById(R.id.password);
        login = findViewById(R.id.login);
        shakeAnimation = AnimationUtils.loadAnimation(this, R.anim.shake);
        loginLayout = findViewById(R.id.login_layout);

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String mobile = mobileNumber.getText().toString();
                String pass = password.getText().toString();

                if (validateLoginFields(mobile, pass, loginLayout)) {
                    startActivity(new Intent(LoginActivity.this, MainActivity.class));
                }
            }
        });

        signUpPage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getBaseContext(), SignUpActivity.class));
                finish();
            }
        });
    }

    private Boolean validateLoginFields(String mobile, String pass, LinearLayout loginLayout) {
        Boolean isValid = Boolean.FALSE;

        if (!TextUtils.isEmpty(mobile) && !TextUtils.isEmpty(pass)) {
            isValid = Boolean.TRUE;
        } else if (TextUtils.isEmpty(mobile) && TextUtils.isEmpty(pass)) {
            isValid = Boolean.FALSE;
            ToastUtil.showTopToastLong(this, "Invalid Mobile No and Password !");
            loginLayout.startAnimation(shakeAnimation);
        } else if (TextUtils.isEmpty(mobile)) {
            isValid = Boolean.FALSE;
            ToastUtil.showTopToastLong(this, "Invalid Mobile No !");
            loginLayout.startAnimation(shakeAnimation);
        } else if (TextUtils.isEmpty(pass)) {
            isValid = Boolean.FALSE;
            ToastUtil.showTopToastLong(this, "Invalid Password !");
            loginLayout.startAnimation(shakeAnimation);
        }

        return isValid;
    }

}
