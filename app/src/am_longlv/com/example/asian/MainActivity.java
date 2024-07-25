package com.example.asian;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import com.example.asian.IssuesFour.IssuesFourActivity;

public class MainActivity extends AppCompatActivity {
    private Button mBtnExerciseLogin, mBtnExerciseCal, mBtnExerciseUpdateInfo,mBtnIssuesFour;

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
            startActivity(new Intent(MainActivity.this, IssuesFourActivity.class));
        });
    }
}
