////package com.example.asian;
////
////public class test {
////    private void putDataToIssueThreeActivity() {
////        Bundle bundle = new Bundle();
////        bundle.putString(Constant.KEY_EMAIL, mEdtEmail.getText().toString().trim());
////        bundle.putString(Constant.KEY_PASSWORD, mEdtPassword.getText().toString().trim());
////        Intent intent = new Intent(this, IssueThreeActivity.class);
////        intent.putExtra(Constant.KEY_FROM_LOGIN, bundle);
////        startActivity(intent);
////        Toast.makeText(this, getString(R.string.login_success), Toast.LENGTH_SHORT).show();
////
////        private void initData() {
////            Bundle bundleFromLogin = getIntent().getBundleExtra(Constant.KEY_FROM_LOGIN);
////            Bundle bundleFromUpdateInfo = getIntent().getBundleExtra(Constant.KEY_FROM_UPDATE_INFO);
////            if (bundleFromLogin != null) {
////                mTvEmail.setText(getString(R.string.email_str_param, bundleFromLogin.get(Constant.KEY_EMAIL)));
////                mTvPassword.setText(getString(R.string.password_str_param, bundleFromLogin.get(Constant.KEY_PASSWORD)));
////                mLlDataFromLogin.setVisibility(View.VISIBLE);
////                mLlDataFromUpdateInfo.setVisibility(View.GONE);
////            } else if (bundleFromUpdateInfo != null) {
////                mTvName.setText(getString(R.string.name_str_param, bundleFromUpdateInfo.get(Constant.KEY_NAME)));
////                mTvIdCard.setText(getString(R.string.id_card_str_param, bundleFromUpdateInfo.get(Constant.KEY_ID_CARD)));
////                mTvDegree.setText(getString(R.string.degree_str_param, bundleFromUpdateInfo.get(Constant.KEY_DEGREE)));
////                mTvInterest.setText(getString(R.string.interest_str_param, bundleFromUpdateInfo.get(Constant.KEY_INTEREST)));
////                mTvMoreInfo.setText((getString(R.string.more_info_str_param, bundleFromUpdateInfo.get(Constant.KEY_MORE_INFO))));
////                mLlDataFromLogin.setVisibility(View.GONE);
////                mLlDataFromUpdateInfo.setVisibility(View.VISIBLE);
////            }
////
////}
////
////<string name="result_float_param">Ket qua: %f</string>
////    <string name="result_int_param">Ket qua: %d</string>
////    <string name="more_info_str_param">Thông tin bổ sung: %s</string>
////    <string name="interest_str_param">Sở thích: %s</string>
////    <string name="name_str_param">Họ tên: %s</string>
////    <string name="id_card_str_param">Cmnd: %s</string>
////    <string name="degree_str_param">Bằng cấp: %s</string>
////    <string name="email_str_param">Email: %s</string>
////    <string name="password_str_param">Password: %s</string>
//
//package com.example.asian;
//
//import android.content.Intent;
//import android.os.Bundle;
//import android.widget.TextView;
//
//import androidx.appcompat.app.AppCompatActivity;
//
//public class SendDataActivity extends AppCompatActivity {
//
//    private TextView mTvName;
//    private TextView mTvID;
//    private TextView mTvDegree;
//    private TextView mTvInterests;
//    private TextView mTvAdditionalInfo;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.send_data_activity);
//        initView();
//        getData();
//    }
//
//    private void initView() {
//        mTvName = findViewById(R.id.tvName);
//        mTvID = findViewById(R.id.tvID);
//        mTvDegree = findViewById(R.id.tvDegree);
//        mTvInterests = findViewById(R.id.tvInterest);
//        mTvAdditionalInfo = findViewById(R.id.tvAdditionalInfo);
//    }
//
//    private void getData() {
//        Intent intent = getIntent();
//        Bundle bundle = intent.getExtras();
//        if (bundle != null) {
//            mTvName.setText(bundle.getString(UpdateInfoActivity.KEY_NAME));
//            mTvID.setText(bundle.getString(UpdateInfoActivity.KEY_CARD));
//            mTvDegree.setText(bundle.getString(UpdateInfoActivity.KEY_DEGREE));
//            mTvInterests.setText(bundle.getString(UpdateInfoActivity.KEY_INTEREST));
//            mTvAdditionalInfo.setText(bundle.getString(UpdateInfoActivity.KEY_MORE_INFORMATION));
//        }
//    }
//}
//
