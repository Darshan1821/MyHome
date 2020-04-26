package com.dexter.myhome;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.dexter.myhome.auth.LoginActivity;
import com.dexter.myhome.model.Country;
import com.dexter.myhome.model.GsonRequest;
import com.dexter.myhome.util.AppConstants;
import com.dexter.myhome.util.RequestHandler;
import com.google.gson.Gson;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        new Handler().postDelayed(() -> {
            GsonRequest<Country[]> countriesRequest = new GsonRequest<Country[]>(AppConstants.COUNTRIES_URL,Country[].class,null, success -> {
                Intent loginIntent = new Intent(SplashActivity.this, LoginActivity.class);
                loginIntent.putExtra("countries", success);
                startActivity(loginIntent);
                finish();
            }, error -> {
                error.printStackTrace();
                Toast.makeText(getApplicationContext(),"Failed To Fetch Countries",Toast.LENGTH_LONG).show();
            });

            RequestHandler.getInstance(getApplicationContext()).addToRequestQueue(countriesRequest);
        }, 3000);
    }
}
