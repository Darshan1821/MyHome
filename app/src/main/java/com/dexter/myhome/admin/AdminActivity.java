package com.dexter.myhome.admin;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;

import com.dexter.myhome.R;
import com.dexter.myhome.auth.LoginActivity;
import com.dexter.myhome.menu.AboutActivity;
import com.dexter.myhome.menu.ApartmentInfoActivity;
import com.dexter.myhome.menu.ProfileActivity;
import com.dexter.myhome.model.Country;
import com.dexter.myhome.model.GsonRequest;
import com.dexter.myhome.util.AppConstants;
import com.dexter.myhome.util.RequestHandler;
import com.google.firebase.auth.FirebaseAuth;
import com.surveymonkey.surveymonkeyandroidsdk.SurveyMonkey;

public class AdminActivity extends AppCompatActivity {

    public static final String MY_APP = "My Home";
    public static final String SURVEY_HASH = "7K5WGDL";
    public static final int SM_REQUEST_CODE = 0;
    private CardView addMaintenance;
    private FirebaseAuth mAuth;
    private AlertDialog.Builder builder;
    private SurveyMonkey surveyMonkey = new SurveyMonkey();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mAuth = FirebaseAuth.getInstance();
        addMaintenance = findViewById(R.id.add_maintenance);

        addMaintenance.setOnClickListener(v -> {
            startActivity(new Intent(AdminActivity.this, AddMaintenance.class));
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        surveyMonkey.onStart(this, MY_APP, SM_REQUEST_CODE, SURVEY_HASH);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        int id = item.getItemId();

        switch (id) {
            case R.id.help:
                Intent emailIntent = new Intent(android.content.Intent.ACTION_SEND);
                emailIntent.setType("message/rfc822");
                emailIntent.putExtra(Intent.EXTRA_EMAIL, new String[]{"drashtirb@gmail.com", "avanimvanpariya01@gmail.com",
                        "shyamkaila1998@gmail.com", "harshchudasama4106@gmail.com"});
                emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Help");
                startActivity(emailIntent);
                return true;
            case R.id.feedback:
                surveyMonkey.startSMFeedbackActivityForResult(this, SM_REQUEST_CODE, SURVEY_HASH);
                break;
            case R.id.about:
                startActivity(new Intent(AdminActivity.this, AboutActivity.class));
                return true;
            case R.id.profile:
                startActivity(new Intent(AdminActivity.this, ProfileActivity.class));
                return true;
            case R.id.apartment_info:
                startActivity(new Intent(AdminActivity.this, ApartmentInfoActivity.class));
                return true;
            case R.id.logout:
                builder = new AlertDialog.Builder(this);
                builder.setMessage("Are You Sure?");
                builder.setPositiveButton("Yes", (dialog, which) -> {
                    mAuth.signOut();
                    GsonRequest<Country[]> countriesRequest = new GsonRequest<Country[]>(AppConstants.COUNTRIES_URL, Country[].class, null, success -> {
                        Intent loginIntent = new Intent(AdminActivity.this, LoginActivity.class);
                        loginIntent.putExtra("countries", success);
                        startActivity(loginIntent);
                        finish();
                    }, error -> {
                        error.printStackTrace();
                        Toast.makeText(getApplicationContext(), "Failed To Fetch Countries", Toast.LENGTH_LONG).show();
                    });

                    RequestHandler.getInstance(getApplicationContext()).addToRequestQueue(countriesRequest);
                }).setNegativeButton("No", (dialog, which) -> {

                }).show();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
