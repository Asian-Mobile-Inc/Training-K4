package com.example.asian.ui;

import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.asian.R;

public class DataInformationActivity extends AppCompatActivity {
    private TextView mTvName, mTvCard, mTvMoreInformation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_data_information);
        initView();
        getData();
    }

    private void initView() {
        mTvName = findViewById(R.id.tvDataName);
        mTvCard = findViewById(R.id.tvDataCard);
        mTvMoreInformation = findViewById(R.id.tvDataMoreInformation);
    }

    private void getData() {
        String name = getIntent().getStringExtra("keyName");
        String card = getIntent().getStringExtra("keyCard");
        String moreInformation = getIntent().getStringExtra("keyMoreInformation");

        mTvName.setText(name);
        mTvCard.setText(card);
        mTvMoreInformation.setText(moreInformation);
    }
}
