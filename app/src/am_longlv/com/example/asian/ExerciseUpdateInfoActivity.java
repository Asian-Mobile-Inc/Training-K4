package com.example.asian;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class ExerciseUpdateInfoActivity extends AppCompatActivity {
    private Button mBtnSendInfo;
    private EditText mEdtName, mEdtCmnd, mEdtMoreInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exercise_update_info);
        initUI();
        initListener();
    }

    private void initUI() {
        mBtnSendInfo = findViewById(R.id.btnSendInfo);
        mEdtName = findViewById(R.id.edtName);
        mEdtCmnd = findViewById(R.id.edtCmnd);
        mEdtMoreInfo = findViewById(R.id.edtMoreInfo);
    }

    private void initListener() {
        mBtnSendInfo.setOnClickListener(v -> {
            if (validate(mEdtName.getText().toString().trim(), mEdtCmnd.getText().toString().trim(), mEdtMoreInfo.getText().toString().trim())) {
                Toast.makeText(this, getString(R.string.error_password_invalid), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private boolean validate(String name, String cmnd, String moreInfo) {
        if (name == null || name.isEmpty()) {
            mEdtName.setError(getString(R.string.error_name_invalid));
            return false;
        }
        if (cmnd == null || cmnd.isEmpty()) {
            mEdtCmnd.setError(getString(R.string.error_cmnd_invalid));
            return false;
        }
        if (moreInfo == null || moreInfo.isEmpty() || moreInfo.length() < 100) {
            mEdtMoreInfo.setError(getString(R.string.error_more_info_invalid));
            return false;
        }
        return true;
    }
}
