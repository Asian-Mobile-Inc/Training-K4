package com.example.asian.ui;

import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.asian.R;
import com.example.asian.constants.Constants;

public class DataInformationActivity extends AppCompatActivity {
    private TextView mTvName;
    private TextView mTvCard;
    private TextView mTvMoreInformation;

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
        String name = getIntent().getStringExtra(Constants.KEY_NAME);
        String card = getIntent().getStringExtra(Constants.KEY_CARD);
        String moreInformation = getIntent().getStringExtra(Constants.KEY_MORE_INFORMATION);

        mTvName.setText(name);
        mTvCard.setText(card);
        mTvMoreInformation.setText(moreInformation);
    }
}
