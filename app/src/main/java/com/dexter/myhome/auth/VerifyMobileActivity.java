package com.dexter.myhome.auth;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import com.dexter.myhome.MainActivity;
import com.dexter.myhome.R;
import com.dexter.myhome.admin.AdminActivity;
import com.dexter.myhome.model.Country;
import com.dexter.myhome.model.GsonRequest;
import com.dexter.myhome.model.Profile;
import com.dexter.myhome.util.AppConstants;
import com.dexter.myhome.util.RequestHandler;
import com.google.android.gms.tasks.TaskExecutors;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Map;
import java.util.concurrent.TimeUnit;

public class VerifyMobileActivity extends AppCompatActivity {

    private String mobile;
    private EditText code;
    private String verificationId;
    private String otp;
    private FirebaseAuth mAuth;
    private DatabaseReference userReference;
    private AppCompatButton logIn;
    private AppCompatButton changeMobile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verify_mobile);

        mobile = getIntent().getExtras().get("mobile").toString();
        mAuth = FirebaseAuth.getInstance();
        code = findViewById(R.id.code);
        logIn = findViewById(R.id.log_in);
        changeMobile = findViewById(R.id.change_mobile);
        userReference = FirebaseDatabase.getInstance(AppConstants.FIREBASE_DB_URL).getReference("Users");

        getSupportActionBar().setTitle("Verify Mobile No");

        sendVerificationCode(mobile);

        logIn.setOnClickListener(v -> {
            String codeVal = code.getText().toString();

            if(TextUtils.isEmpty(codeVal) || codeVal.length() < 6){
                code.setError("Enter Code...");
                code.requestFocus();
                return;
            }

            verifyCode(codeVal);
        });

        changeMobile.setOnClickListener(v -> {
            GsonRequest<Country[]> countriesRequest = new GsonRequest<Country[]>(AppConstants.COUNTRIES_URL,Country[].class,null, success -> {
                Intent loginIntent = new Intent(VerifyMobileActivity.this, LoginActivity.class);
                loginIntent.putExtra("countries", success);
                startActivity(loginIntent);
                finish();
            }, error -> {
                Toast.makeText(getApplicationContext(),"Failed To Fetch Countries",Toast.LENGTH_LONG).show();
            });
            RequestHandler.getInstance(getApplicationContext()).addToRequestQueue(countriesRequest);
        });
    }

    private void sendVerificationCode(String mobile) {
        PhoneAuthProvider.getInstance().verifyPhoneNumber(mobile, AppConstants.OTP_TIMEOUT, TimeUnit.SECONDS, TaskExecutors.MAIN_THREAD, mCallback);
    }

    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallback = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

        @Override
        public void onCodeSent(@NonNull String s, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
            super.onCodeSent(s, forceResendingToken);
            verificationId = s;
            Log.d("VerificationId", s);
        }

        @Override
        public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {
            otp = phoneAuthCredential.getSmsCode();
            if(otp != null){
                verifyCode(otp);
                code.setText(otp);
            }
        }

        @Override
        public void onVerificationFailed(@NonNull FirebaseException e) {
            e.printStackTrace();
            Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_LONG).show();
        }
    };

    private void verifyCode(String code){
        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(verificationId, code);
        signInWithCredentials(credential);
    }

    private void signInWithCredentials(PhoneAuthCredential credential) {
        mAuth.signInWithCredential(credential).addOnCompleteListener(task -> {
            if(task.isSuccessful()){
                saveUser();
            } else {
                Toast.makeText(getApplicationContext(),"Failed to verify code !", Toast.LENGTH_LONG).show();
            }
        }).addOnFailureListener(e -> Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_LONG).show());
    }

    private void saveUser() {
        userReference.child(mAuth.getCurrentUser().getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (!dataSnapshot.exists()) {
                    Profile profile = new Profile();
                    profile.setName("");
                    profile.setMobile(mAuth.getCurrentUser().getPhoneNumber());
                    profile.setEmail("");
                    profile.setGender("");
                    profile.setAdmin(Boolean.FALSE);
                    profile.setUserId(mAuth.getCurrentUser().getUid());
                    userReference.child(mAuth.getCurrentUser().getUid()).child("Profile").setValue(profile);

                    Intent mainActivityIntent = new Intent(VerifyMobileActivity.this, MainActivity.class);
                    startActivity(mainActivityIntent);
                } else {
                    Map<String, Object> userMap = (Map<String, Object>) dataSnapshot.getValue();
                    Map<String, Object> profileMap = (Map<String, Object>) userMap.get("Profile");
                    Boolean isAdmin = (Boolean) profileMap.get("admin");
                    if (!isAdmin) {
                        Intent mainActivityIntent = new Intent(VerifyMobileActivity.this, MainActivity.class);
                        startActivity(mainActivityIntent);
                    } else {
                        Intent adminActivityIntent = new Intent(VerifyMobileActivity.this, AdminActivity.class);
                        startActivity(adminActivityIntent);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

}
