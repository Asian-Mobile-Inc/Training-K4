package com.example.asian.issue3;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.asian.R;

public class UpdateInfoActivity extends AppCompatActivity {

    private static final int MIN_NAME_LENGTH = 3;
    private static final int ID_LENGTH = 9;
    private static final int MIN_ADDITIONAL_INFO_LENGTH = 10;

    private EditText mEdtName;
    private EditText mEdtID;
    private EditText mEdtAdditionalInfo;
    private Button mBtnSubmit;
    private RadioButton mRbIntermediate;
    private RadioButton mRbCollege;
    private RadioButton mRbUniversity;
    private CheckBox mCbReadingNew;
    private CheckBox mCbReadingBook;
    private CheckBox mCbReadingCode;

    public static final String KEY_NAME = "keyName";
    public static final String KEY_CARD = "keyCard";
    public static final String KEY_MORE_INFORMATION = "keyMoreInformation";
    public static final String KEY_DEGREE = "keyDegree";
    public static final String KEY_INTEREST = "keyInterest";
    public static final String KEY_DATA_FORM = "fromData";

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
        mRbIntermediate = findViewById(R.id.rbIntermediate);
        mRbCollege = findViewById(R.id.rbCollege);
        mRbUniversity = findViewById(R.id.rbUniversity);
        mCbReadingNew = findViewById(R.id.cbReadingNew);
        mCbReadingBook = findViewById(R.id.cbReadingBook);
        mCbReadingCode = findViewById(R.id.cbReadingCode);
    }

    private void initListener() {
        mBtnSubmit.setOnClickListener(v -> {
            if (validate()) {
                Toast.makeText(this, getString(R.string.submit_succes), Toast.LENGTH_SHORT).show();
                sendDataToSendDataActivity();
            }
        });
    }

    private boolean validate() {
        String name = mEdtName.getText().toString().trim();
        String id = mEdtID.getText().toString().trim();
        String additionalInfo = mEdtAdditionalInfo.getText().toString().trim();
        boolean isValid = true;

        if (name.isEmpty() || name.length() < MIN_NAME_LENGTH) {
            mEdtName.setError(getString(R.string.length_name_must_more_than_3_character));
            isValid = false;
        } else if (id.length() != ID_LENGTH) {
            mEdtID.setError(getString(R.string.length_id_must_be_9_character));
            isValid = false;
        } else if (additionalInfo.isEmpty() || additionalInfo.length() < MIN_ADDITIONAL_INFO_LENGTH) {
            mEdtAdditionalInfo.setError(getString(R.string.info_must_more_than_100_character));
            isValid = false;
        }
        return isValid;
    }

    private void sendDataToSendDataActivity() {
        Bundle bundle = new Bundle();
        bundle.putString(KEY_NAME, mEdtName.getText().toString().trim());
        bundle.putString(KEY_CARD, mEdtID.getText().toString().trim());
        bundle.putString(KEY_MORE_INFORMATION, mEdtAdditionalInfo.getText().toString().trim());

        if (mRbIntermediate.isChecked()) {
            bundle.putString(UpdateInfoActivity.KEY_DEGREE, getString(R.string.intermediate_level));
        } else if (mRbCollege.isChecked()) {
            bundle.putString(UpdateInfoActivity.KEY_DEGREE, getString(R.string.college_level));
        } else if (mRbUniversity.isChecked()) {
            bundle.putString(UpdateInfoActivity.KEY_DEGREE, getString(R.string.university_level));
        }

        StringBuilder interests = new StringBuilder();
        if (mCbReadingNew.isChecked()) {
            interests.append(getString(R.string.read_news)).append(", ");
        }
        if (mCbReadingBook.isChecked()) {
            interests.append(getString(R.string.read_book)).append(", ");
        }
        if (mCbReadingCode.isChecked()) {
            interests.append(getString(R.string.read_code)).append(", ");
        }
        bundle.putString(KEY_INTEREST, interests.toString());
        Intent intent = new Intent(this, SendDataActivity.class);
        intent.putExtra(KEY_DATA_FORM, bundle);
        startActivity(intent);
    }
}
