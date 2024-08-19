package com.example.asian;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {
    private Button mBtnExerciseLogin;
    private Button mBtnExerciseCal;
    private Button mBtnExerciseUpdateInfo;
    private Button mBtnExerciseFragmentArgument;
    private Button mBtnTablayoutRecyclerview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);
        initUI();
        initListener();
    }

    private void initUI() {
        mBtnExerciseLogin = findViewById(R.id.btnExerciseLogin);
        mBtnExerciseCal = findViewById(R.id.btnExerciseCal);
        mBtnExerciseUpdateInfo = findViewById(R.id.btnExerciseUpdateInfo);
        mBtnExerciseFragmentArgument = findViewById(R.id.btnFragmentArgument);
        mBtnTablayoutRecyclerview = findViewById(R.id.btnTablayoutRecyclerview);
    }

    private void initListener() {
        mBtnExerciseLogin.setOnClickListener(v -> {
            startActivity(new Intent(MainActivity.this, LoginActivity.class));
        });
        mBtnExerciseCal.setOnClickListener(v -> {
            startActivity(new Intent(MainActivity.this, CalculateActivity.class));
        });
        mBtnExerciseUpdateInfo.setOnClickListener(v -> {
            startActivity(new Intent(MainActivity.this, UpdateInfoActivity.class));
        });
        mBtnExerciseFragmentArgument.setOnClickListener(v -> {
            startActivity(new Intent(MainActivity.this, FragmentActivity.class));
        });
        mBtnTablayoutRecyclerview.setOnClickListener(view -> {
            startActivity(new Intent(MainActivity.this, MainActivityIssueFive.class));
        });
    }
}

