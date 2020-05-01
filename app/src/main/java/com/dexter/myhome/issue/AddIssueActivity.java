package com.dexter.myhome.issue;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import com.dexter.myhome.R;
import com.dexter.myhome.model.Issue;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Date;

public class AddIssueActivity extends AppCompatActivity {

    private EditText title;
    private EditText description;
    private AppCompatButton addIssue;
    private String userName;
    private String societyName;
    private DatabaseReference issueReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_issue);

        getSupportActionBar().setTitle("Add Issue");

        title = findViewById(R.id.issue_title);
        description = findViewById(R.id.issue_description);

        userName = getIntent().getExtras().get("userName").toString();
        societyName = getIntent().getExtras().get("societyName").toString();

        issueReference = FirebaseDatabase.getInstance().getReference("Societies")
                .child(societyName).child("Issues");

        addIssue = findViewById(R.id.add_issue);

        addIssue.setOnClickListener(v -> {

            if (TextUtils.isEmpty(title.getText().toString())) {
                title.setError("Enter Title !");
                title.requestFocus();
                return;
            }

            if (TextUtils.isEmpty(description.getText().toString())) {
                description.setError("Enter Description !");
                description.requestFocus();
                return;
            }

            Issue issue = new Issue();
            issue.setTitle(title.getText().toString());
            issue.setDescription(description.getText().toString());
            issue.setIssueDate(new Date());
            issue.setRaisedBy(userName);
            issueReference.push().setValue(issue);
            Toast.makeText(getApplicationContext(), "Issue Added !", Toast.LENGTH_LONG).show();

            Intent meetingIntent = new Intent(AddIssueActivity.this, IssueActivity.class);
            meetingIntent.putExtra("societyName", societyName);
            startActivity(meetingIntent);
            finish();
        });
    }
}
