package com.example.asian.issuethree;

import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.asian.Constant;
import com.example.asian.R;

public class IssueThreeActivity extends AppCompatActivity {
    private TextView mTvEmail;
    private TextView mTvPassword;
    private TextView mTvName;
    private TextView mTvIdCard;
    private TextView mTvMoreInfo;
    private TextView mTvDegree;
    private TextView mTvInterest;
    private LinearLayout mLlDataFromLogin;
    private LinearLayout mLlDataFromUpdateInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_issue_three);
        initUI();
        initData();
    }

    private void initUI() {
        mTvEmail = findViewById(R.id.tvEmail);
        mTvPassword = findViewById(R.id.tvPassword);
        mTvName = findViewById(R.id.tvName);
        mTvIdCard = findViewById(R.id.tvIdCard);
        mTvMoreInfo = findViewById(R.id.tvMoreInfo);
        mTvDegree = findViewById(R.id.tvDegree);
        mTvInterest = findViewById(R.id.tvInterest);
        mLlDataFromLogin = findViewById(R.id.llDataFromLogin);
        mLlDataFromUpdateInfo = findViewById(R.id.llDataFromUpdateInfo);
    }

    private void initData() {
        Bundle bundleFromLogin = getIntent().getBundleExtra(Constant.KEY_FROM_LOGIN);
        Bundle bundleFromUpdateInfo = getIntent().getBundleExtra(Constant.KEY_FROM_UPDATE_INFO);
        if (bundleFromLogin != null) {
            mTvEmail.setText(getString(R.string.email_str_param, bundleFromLogin.get(Constant.KEY_EMAIL)));
            mTvPassword.setText(getString(R.string.password_str_param, bundleFromLogin.get(Constant.KEY_PASSWORD)));
            mLlDataFromLogin.setVisibility(View.VISIBLE);
            mLlDataFromUpdateInfo.setVisibility(View.GONE);
        } else if (bundleFromUpdateInfo != null) {
            mTvName.setText(getString(R.string.name_str_param, bundleFromUpdateInfo.get(Constant.KEY_NAME)));
            mTvIdCard.setText(getString(R.string.id_card_str_param, bundleFromUpdateInfo.get(Constant.KEY_ID_CARD)));
            mTvDegree.setText(getString(R.string.degree_str_param, bundleFromUpdateInfo.get(Constant.KEY_DEGREE)));
            mTvInterest.setText(getString(R.string.interest_str_param, bundleFromUpdateInfo.get(Constant.KEY_INTEREST)));
            mTvMoreInfo.setText((getString(R.string.more_info_str_param, bundleFromUpdateInfo.get(Constant.KEY_MORE_INFO))));
            mLlDataFromLogin.setVisibility(View.GONE);
            mLlDataFromUpdateInfo.setVisibility(View.VISIBLE);
        }
    }
}
