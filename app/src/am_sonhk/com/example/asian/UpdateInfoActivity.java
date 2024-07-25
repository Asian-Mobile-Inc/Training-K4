package com.example.asian;

import android.os.Bundle;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class UpdateInfoActivity extends AppCompatActivity {

    private EditText mEdtName, mEdtID, mEdtAdditionalInfo;
    private RadioGroup mRgDegree;
    private CheckBox mCbReadingNews, mCbReadingBooks, mCbReadingCoding;
    private Button mBtnSubmit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.update_info_activity);

        mEdtName = findViewById(R.id.edtName);
        mEdtID = findViewById(R.id.edtID);
        mEdtAdditionalInfo = findViewById(R.id.edtAdditionalInfo);
        mRgDegree = findViewById(R.id.rgDegree);
        mCbReadingNews = findViewById(R.id.cbReadingNews);
        mCbReadingBooks = findViewById(R.id.cbReadingBooks);
        mCbReadingCoding = findViewById(R.id.cbReadingCoding);
        mBtnSubmit = findViewById(R.id.btnSubmit);
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

        if (name.isEmpty() || name.length() < 3) {
            mEdtName.setError("Tên phải >=3 ký tự");
            return false;
        }

        if (id.length() != 9) {
            mEdtID.setError("CMND phải đúng 9 chữ số");
            return false;
        }

        if (additionalInfo.isEmpty() || additionalInfo.length() < 10) {
            mEdtAdditionalInfo.setError("Thông tin bổ sung phải >= 100 ký tự");
            return false;
        }

        return true;
    }
}
