package com.example.asian;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {
    private Button mBtnTab1, mBtnTab2, mBtnTab3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initUI();
        initListener();
    }
    private void initUI() {
        mBtnTab1 = findViewById(R.id.btnTab1);
        mBtnTab2 = findViewById(R.id.btnTab2);
        mBtnTab3 = findViewById(R.id.btnTab3);
    }
    private void initListener() {
        mBtnTab1.setOnClickListener(v -> {
            startActivity(new Intent(MainActivity.this, TabOneActivity.class));
        });
        mBtnTab2.setOnClickListener(v -> {
            startActivity(new Intent(MainActivity.this, TabTwoActivity.class));
        });
        mBtnTab3.setOnClickListener(v -> {
            startActivity(new Intent(MainActivity.this, TabThreeActivity.class));
        });
    }
}