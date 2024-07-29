package com.example.asian;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class UpdateInfoActivity extends AppCompatActivity {

    private static final int MIN_NAME_LENGTH = 3;
    private static final int ID_LENGTH = 9;
    private static final int MIN_ADDITIONAL_INFO_LENGTH = 10;

    private EditText mEdtName;
    private EditText mEdtID;
    private EditText mEdtAdditionalInfo;
    private Button mBtnSubmit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.update_info_activity);
        initUI();
        initListener();
    }

    private void initUI() {
        mEdtName = findViewById(R.id.edtName);
        mEdtID = findViewById(R.id.edtID);
        mEdtAdditionalInfo = findViewById(R.id.edtAdditionalInfo);
        mBtnSubmit = findViewById(R.id.btnSubmit);
    }

    private void initListener() {
        mBtnSubmit.setOnClickListener(v -> {
            if (validate()) {
                Toast.makeText(this, "Submit Success", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private boolean validate() {
        String name = mEdtName.getText().toString().trim();
        String id = mEdtID.getText().toString().trim();
        String additionalInfo = mEdtAdditionalInfo.getText().toString().trim();

        if (name.isEmpty() || name.length() < MIN_NAME_LENGTH) {
            mEdtName.setError(getString(R.string.length_name_must_more_than_3_character));
            return false;
        }

        if (id.length() != ID_LENGTH) {
            mEdtID.setError(getString(R.string.length_id_must_be_9_character));
            return false;
        }

        if (additionalInfo.isEmpty() || additionalInfo.length() < MIN_ADDITIONAL_INFO_LENGTH) {
            mEdtAdditionalInfo.setError(getString(R.string.info_must_more_than_100_character));
            return false;
        }

        return true;
    }
}
