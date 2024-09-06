package com.example.asian;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;


public class MainActivity extends AppCompatActivity {
    private Button mBtnDatabase;
    private Button mBtnKotlin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);
        initUI();
        initListener();
    }

    private void initUI() {
        mBtnDatabase = findViewById(R.id.btnDatabase);
        mBtnKotlin = findViewById(R.id.btnKotlin);
    }

    private void initListener() {
        mBtnDatabase.setOnClickListener(view -> startActivity(new Intent(MainActivity.this, MainActivityIssueNine.class)));
//        mBtnKotlin.setOnClickListener(v -> startActivity(new Intent(MainActivity.this, MainActivityKotlin.class)));
    }
}
