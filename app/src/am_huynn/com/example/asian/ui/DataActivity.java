package com.example.asian.ui;

import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.asian.R;
import com.example.asian.constants.Constants;

public class DataActivity extends AppCompatActivity {
    private TextView mTvEmail;
    private TextView mTvPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_data);
        initView();
        getData();
    }

    private void getData() {
        Bundle bundle = getIntent().getBundleExtra(Constants.KEY_BUNDLE_LOGIN);
        String email = getString(R.string.email).concat(bundle.getString(Constants.KEY_EMAIL));
        String password = getString(R.string.password).concat(bundle.getString(Constants.KEY_PASSWORD));

        mTvEmail.setText(email);
        mTvPassword.setText(password);
    }

    private void initView() {
        mTvEmail = findViewById(R.id.tvEmail);
        mTvPassword = findViewById(R.id.tvPassword);
    }
}
