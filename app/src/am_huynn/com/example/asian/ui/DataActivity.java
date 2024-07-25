package com.example.asian.ui;

import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.asian.R;

public class DataActivity extends AppCompatActivity {
    private TextView mTvEmail, mTvPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_data);
        initView();
        getData();
    }

    private void getData() {
        String email = getString(R.string.email) + getIntent().getStringExtra("keyEmail");
        String password = getString(R.string.password) + getIntent().getStringExtra("keyPassword");

        mTvEmail.setText(email);
        mTvPassword.setText(password);
    }

    private void initView() {
        mTvEmail = findViewById(R.id.tvEmail);
        mTvPassword = findViewById(R.id.tvPassword);
    }
}
