package com.example.asian.ui;

import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.asian.R;
import com.example.asian.constants.Constants;

public class DataInformationActivity extends AppCompatActivity {
    private TextView mTvName;
    private TextView mTvCard;
    private TextView mTvDegree;
    private TextView mTvInterest;
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
        mTvDegree = findViewById(R.id.tvDataDegree);
        mTvInterest = findViewById(R.id.tvDataInterest);
        mTvMoreInformation = findViewById(R.id.tvDataMoreInformation);
    }

    private void getData() {
        String name = getString(R.string.name) + getIntent().getStringExtra(Constants.KEY_NAME);
        String card = getString(R.string.card) + getIntent().getStringExtra(Constants.KEY_CARD);
        String moreInformation = getString(R.string.more_information_value) + getIntent().getStringExtra(Constants.KEY_MORE_INFORMATION);
        String degree = getString(R.string.degree_value) + getIntent().getStringExtra(Constants.KEY_DEGREE);
        if (getIntent().getStringExtra(Constants.KEY_INTEREST) != null) {
            String interest = getString(R.string.interest_value) + getIntent().getStringExtra(Constants.KEY_INTEREST);
            mTvInterest.setText(interest);
        }
        mTvName.setText(name);
        mTvCard.setText(card);
        mTvDegree.setText(degree);
        mTvMoreInformation.setText(moreInformation);
    }
}
