package com.example.asian;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {
    private Button mBtnThread;
    private Button mBtnAsyncTask;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);
        initUI();
        initListener();
    }

    private void initUI() {
        mBtnThread = findViewById(R.id.btnThread);
        mBtnAsyncTask = findViewById(R.id.btnAsyncTaskMain);
    }

    private void initListener() {
        mBtnThread.setOnClickListener(v -> startActivity(new Intent(MainActivity.this, ThreadTask.class)));
        mBtnAsyncTask.setOnClickListener(v -> startActivity(new Intent(MainActivity.this, AsyncTask.class)));
    }
}
