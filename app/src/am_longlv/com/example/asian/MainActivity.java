package com.example.asian;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import com.example.asian.IssueSix.IssueSixActivity;
import com.example.asian.issuefour.IssueFourActivity;
import com.example.asian.issuefive.IssueFiveActivity;

public class MainActivity extends AppCompatActivity {
    private Button mBtnExerciseLogin;
    private Button mBtnExerciseCal;
    private Button mBtnExerciseUpdateInfo;
    private Button mBtnIssuesFour;
    private Button mBtnIssueFive;
    private Button mBtnViewIssuesSix;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initUI();
        initListener();
    }

    private void initUI() {
        mBtnExerciseLogin = findViewById(R.id.btnExerciseLogin);
        mBtnExerciseCal = findViewById(R.id.btnExerciseCal);
        mBtnExerciseUpdateInfo = findViewById(R.id.btnExerciseUpdateInfo);
        mBtnIssuesFour = findViewById(R.id.btnIssuesFour);
        mBtnIssueFive = findViewById(R.id.btnIssueFive);
        mBtnViewIssuesSix = findViewById(R.id.btnViewIssuesSix);
    }

    private void initListener() {
        mBtnExerciseLogin.setOnClickListener(v -> {
            startActivity(new Intent(MainActivity.this, ExerciseLoginActivity.class));
        });
        mBtnExerciseCal.setOnClickListener(v -> {
            startActivity(new Intent(MainActivity.this, ExerciseCalculateActivity.class));
        });
        mBtnExerciseUpdateInfo.setOnClickListener(v -> {
            startActivity(new Intent(MainActivity.this, ExerciseUpdateInfoActivity.class));
        });
        mBtnIssuesFour.setOnClickListener(v -> {
            startActivity(new Intent(MainActivity.this, IssueFourActivity.class));
        });
        mBtnIssueFive.setOnClickListener(v -> {
            startActivity(new Intent(MainActivity.this, IssueFiveActivity.class));
        });
        mBtnViewIssuesSix.setOnClickListener(v -> {
            startActivity(new Intent(MainActivity.this, IssueSixActivity.class));
        });
    }
}
