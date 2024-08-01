//package com.example.asian;
//
//import android.os.Bundle;
//import android.widget.TextView;
//
//import androidx.appcompat.app.AppCompatActivity;
//
//public class SendDataActivity extends AppCompatActivity {
//

//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.send_data_activity);
//        initView();
//        getData();
//    }
////    private void getData() {

////    }
//
//    private void initView() {

//    }
//}

package com.example.asian;

import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class SendDataActivity extends AppCompatActivity {

    private TextView mTvName;
    private TextView mTvID;
    private TextView mTvDegree;
    private TextView mTvInterests;
    private TextView mTvAdditionalInfo;
    private TextView mTvEmail;
    private TextView mTvPassword;
    private LinearLayout mLlDataFromLogin;
    private LinearLayout mLlDataFromUpdateInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.send_data_activity);
        initView();
        getData();
    }

    private void initView() {
        mTvName = findViewById(R.id.tvName);
        mTvID = findViewById(R.id.tvIdCard);
        mTvDegree = findViewById(R.id.tvDegree);
        mTvInterests = findViewById(R.id.tvInterest);
        mTvAdditionalInfo = findViewById(R.id.tvMoreInfo);
        mTvEmail = findViewById(R.id.tvEmail);
        mTvPassword = findViewById(R.id.tvPassword);
        mLlDataFromLogin = findViewById(R.id.llDataFromLogin);
        mLlDataFromUpdateInfo = findViewById(R.id.llDataFromUpdateInfo);
    }

    private void getData() {
        Bundle bundleFromData = getIntent().getBundleExtra(UpdateInfoActivity.KEY_DATA_FORM);
        Bundle bundleFormLogin = getIntent().getBundleExtra(LoginActivity.KEY_FROM_LOGIN);
        if (bundleFormLogin != null) {
            mTvPassword.setText(getString(R.string.password_string_param, bundleFormLogin.get(LoginActivity.KEY_PASSWORD)));
            mTvEmail.setText(getString(R.string.email_string_param, bundleFormLogin.get(LoginActivity.KEY_EMAIL)));
            mLlDataFromLogin.setVisibility(View.VISIBLE);
            mLlDataFromUpdateInfo.setVisibility(View.GONE);
        } else if (bundleFromData != null) {
            mTvName.setText(getString(R.string.name_string_param, bundleFromData.get(UpdateInfoActivity.KEY_NAME)));
            mTvID.setText(getString(R.string.id_string_param, bundleFromData.get(UpdateInfoActivity.KEY_CARD)));
            mTvDegree.setText(getString(R.string.degree_string_param, bundleFromData.get(UpdateInfoActivity.KEY_DEGREE)));
            mTvInterests.setText(getString(R.string.interest_string_param, bundleFromData.get(UpdateInfoActivity.KEY_INTEREST)));
            mTvAdditionalInfo.setText(getString(R.string.additional_info_string_param, bundleFromData.get(UpdateInfoActivity.KEY_MORE_INFORMATION)));
            mLlDataFromUpdateInfo.setVisibility(View.VISIBLE);
            mLlDataFromLogin.setVisibility(View.GONE);
        }
    }
}



