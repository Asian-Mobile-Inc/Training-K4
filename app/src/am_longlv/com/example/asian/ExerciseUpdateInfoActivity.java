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
    }

    private void initUI() {
        mBtnSendInfo = findViewById(R.id.btn_send_info);
        mEdtName = findViewById(R.id.edt_name);
        mEdtCmnd = findViewById(R.id.edt_cmnd);
        mEdtMoreInfo = findViewById(R.id.edt_more_info);
        mBtnSendInfo.setOnClickListener(v -> {
            if (validate(mEdtName.getText().toString().trim(), mEdtCmnd.getText().toString().trim(), mEdtMoreInfo.getText().toString().trim())) {
                Toast.makeText(this, "Send info success", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private boolean validate(String name, String cmnd, String moreInfo) {
        if (name == null || name.isEmpty()) {
            mEdtName.setError("Name is invalid");
            return false;
        }
        if (cmnd == null || cmnd.isEmpty()) {
            mEdtCmnd.setError("CMND is invalid");
            return false;
        }
        if (moreInfo == null || moreInfo.isEmpty() || moreInfo.length() < 100) {
            mEdtMoreInfo.setError("More info is invalid");
            return false;
        }
        return true;
    }
}
