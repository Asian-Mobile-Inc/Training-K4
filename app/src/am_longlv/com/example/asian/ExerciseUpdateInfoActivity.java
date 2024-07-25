package com.example.asian;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class ExerciseUpdateInfoActivity extends AppCompatActivity {
    private Button mBtnSendInfo;
    private EditText mEdtName, mEdtIdCard, mEdtMoreInfo;

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
        mEdtIdCard = findViewById(R.id.edtIdCard);
        mEdtMoreInfo = findViewById(R.id.edtMoreInfo);
    }

    private void initListener() {
        mBtnSendInfo.setOnClickListener(v -> {
            if (validate(mEdtName.getText().toString().trim(), mEdtIdCard.getText().toString().trim(), mEdtMoreInfo.getText().toString().trim())) {
                Toast.makeText(this, getString(R.string.send_success), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private boolean validate(String name, String idCard, String moreInfo) {
        if (name == null || name.isEmpty()) {
            mEdtName.setError(getString(R.string.name_invalid));
            return false;
        }
        if (idCard == null || idCard.isEmpty()) {
            mEdtIdCard.setError(getString(R.string.id_card_invalid));
            return false;
        }
        if (moreInfo == null || moreInfo.isEmpty() || moreInfo.length() < 100) {
            mEdtMoreInfo.setError(getString(R.string.more_info_invalid));
            return false;
        }
        return true;
    }
}
