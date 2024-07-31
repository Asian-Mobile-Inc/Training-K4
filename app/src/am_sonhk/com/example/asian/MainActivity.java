package com.example.asian;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {
    private Button mBtnDatabase;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);
        initUI();
        initListener();
    }

    private void initUI() {
        mBtnDatabase = findViewById(R.id.btnDatabase);
    }

    private void initListener() {
        mBtnDatabase.setOnClickListener(v -> startActivity(new Intent(MainActivity.this, DatabaseEx.class)));
    }
}
