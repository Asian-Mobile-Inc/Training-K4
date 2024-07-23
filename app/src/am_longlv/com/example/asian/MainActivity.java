package com.example.asian;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {
    private Button mBtnExercise1, mBtnExercise2, mBtnExercise3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initUI();
        initListener();
    }
    private void initUI() {
        mBtnExercise1 = findViewById(R.id.btn_exercise1);
        mBtnExercise2 = findViewById(R.id.btn_exercise2);
        mBtnExercise3 = findViewById(R.id.btn_exercise3);
    }
    private void initListener() {
        mBtnExercise1.setOnClickListener(v -> {
            startActivity(new Intent(MainActivity.this, ExerciseLoginActivity.class));
        });
        mBtnExercise2.setOnClickListener(v -> {
            startActivity(new Intent(MainActivity.this, ExerciseCalculateActivity.class));
        });
        mBtnExercise3.setOnClickListener(v -> {
            startActivity(new Intent(MainActivity.this, ExerciseUpdateInfoActivity.class));
        });
    }
}
