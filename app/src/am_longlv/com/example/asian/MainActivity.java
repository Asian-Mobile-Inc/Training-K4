package com.example.asian;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import com.example.asian.issuefour.IssueFourActivity;
import com.example.asian.issuefive.IssueFiveActivity;
import com.example.asian.retrofit.RetrofitActivity;

public class MainActivity extends AppCompatActivity {
    private Button mBtnExerciseLogin;
    private Button mBtnExerciseCal;
    private Button mBtnExerciseUpdateInfo;
    private Button mBtnIssuesFour;
    private Button mBtnIssueFive;
    private Button mBtnIssueRetrofit;

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
        mBtnIssueRetrofit = findViewById(R.id.btnIssueRetrofit);
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
        mBtnIssueRetrofit.setOnClickListener(v -> {
            startActivity(new Intent(MainActivity.this, RetrofitActivity.class));
        });
    }
}
