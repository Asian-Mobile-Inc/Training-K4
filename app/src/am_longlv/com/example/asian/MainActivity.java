package com.example.asian;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {
    private Button mBtnExerciseLogin, mBtnExerciseCal, mBtnExerciseUpdateInfo;

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
    }
}
