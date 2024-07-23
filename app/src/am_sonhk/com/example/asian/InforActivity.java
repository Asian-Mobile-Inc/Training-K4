package com.example.asian;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.CheckBox;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class InforActivity extends AppCompatActivity {

    private EditText mEtName, mEtID, mEtAdditionalInfo;
    private RadioGroup mRgDegree;
    private CheckBox mCbReadingNews, mCbReadingBooks, mCbReadingCoding;
    private Button mBtnSubmit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main3);

        mEtName = findViewById(R.id.etName);
        mEtID = findViewById(R.id.etID);
        mEtAdditionalInfo = findViewById(R.id.etAdditionalInfo);
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
        String name = mEtName.getText().toString().trim();
        String id = mEtID.getText().toString().trim();
        String additionalInfo = mEtAdditionalInfo.getText().toString().trim();

        if (name.isEmpty() || name.length() < 3) {
            mEtName.setError("Tên phải >=3 ký tự");
            return false;
        }

        if (id.isEmpty() || id.length() != 9) {
            mEtID.setError("CMND phải đúng 9 chữ số");
            return false;
        }

        if (additionalInfo.isEmpty() || additionalInfo.length() < 10) {
            mEtAdditionalInfo.setError("Thông tin bổ sung phải >= 100 ký tự");
            return false;
        }

        return true;
    }
}
