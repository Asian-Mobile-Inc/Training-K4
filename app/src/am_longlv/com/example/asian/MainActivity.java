package com.example.asian;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import com.example.asian.issuenine.IssueNineActivity;

public class MainActivity extends AppCompatActivity {
    private Button mBtnExerciseLogin;
    private Button mBtnExerciseCal;
    private Button mBtnExerciseUpdateInfo;
    private Button mBtnIssueNine;

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
        mBtnIssueNine = findViewById(R.id.btnIssueNine);
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
        mBtnIssueNine.setOnClickListener(v -> {
            startActivity(new Intent(MainActivity.this, IssueNineActivity.class));
        });
    }
}
