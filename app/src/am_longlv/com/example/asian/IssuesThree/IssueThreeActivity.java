package com.example.asian.IssuesThree;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.asian.Constant;
import com.example.asian.ExerciseCalculateActivity;
import com.example.asian.ExerciseLoginActivity;
import com.example.asian.ExerciseUpdateInfoActivity;
import com.example.asian.IssuesFour.IssuesFourActivity;
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
        setContentView(R.layout.activity_issues_three);
        initUI();
        initListener();
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

    private void initListener() {

    }

    private void initData() {
        Bundle bundle = getIntent().getExtras();
        if (bundle.size() == Constant.SIZE_DATA_FROM_LOGIN) {
            mTvEmail.setText(getString(R.string.email) + bundle.get(Constant.KEY_EMAIL).toString());
            mTvPassword.setText(getString(R.string.password) + bundle.get(Constant.KEY_PASSWORD).toString());
            mLlDataFromLogin.setVisibility(LinearLayout.VISIBLE);
            mLlDataFromUpdateInfo.setVisibility(LinearLayout.GONE);

        }
        if (bundle.size() == Constant.SIZE_DATA_FROM_UPDATE_INFO) {
            mTvName.setText(getString(R.string.name) + Constant.CHARACTER_SPACE + bundle.get(Constant.KEY_NAME).toString());
            mTvIdCard.setText(getString(R.string.id_card) + Constant.CHARACTER_SPACE + bundle.get(Constant.KEY_ID_CARD).toString());
            mTvDegree.setText(getString(R.string.degree) + Constant.CHARACTER_SPACE + bundle.get(Constant.KEY_DEGREE).toString());
            mTvInterest.setText(getString(R.string.interest) + Constant.CHARACTER_SPACE + bundle.get(Constant.KEY_INTEREST).toString());
            mTvMoreInfo.setText(getString(R.string.more_info) + Constant.CHARACTER_SPACE + bundle.get(Constant.KEY_MORE_INFO).toString());
            mLlDataFromLogin.setVisibility(LinearLayout.GONE);
            mLlDataFromUpdateInfo.setVisibility(LinearLayout.VISIBLE);
        }
    }
}
