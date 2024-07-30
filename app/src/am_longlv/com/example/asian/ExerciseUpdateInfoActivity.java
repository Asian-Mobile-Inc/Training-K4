package com.example.asian;

import static com.example.asian.Constant.MIN_LENGTH_MORE_INFO;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioButton;

import androidx.appcompat.app.AppCompatActivity;

import com.example.asian.issuethree.IssueThreeActivity;

public class ExerciseUpdateInfoActivity extends AppCompatActivity {
    private Button mBtnSendInfo;
    private EditText mEdtName;
    private EditText mEdtIdCard;
    private EditText mEdtMoreInfo;
    private RadioButton mRbIntermediate;
    private RadioButton mRbCollege;
    private RadioButton mRbUniversity;
    private CheckBox mCbReadBook;
    private CheckBox mCbReadNews;
    private CheckBox mCbCoding;

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
        mCbReadNews = findViewById(R.id.cbReadNews);
        mCbReadBook = findViewById(R.id.cbReadBook);
        mCbCoding = findViewById(R.id.cbCoding);
        mRbIntermediate = findViewById(R.id.rbIntermediate);
        mRbCollege = findViewById(R.id.rbCollege);
        mRbUniversity = findViewById(R.id.rbUniversity);
    }

    private void initListener() {
        mBtnSendInfo.setOnClickListener(v -> {
            if (validate(mEdtName.getText().toString().trim(),
                    mEdtIdCard.getText().toString().trim(),
                    mEdtMoreInfo.getText().toString().trim())) {
                putDataToIssueThreeActivity();
            }
        });
    }

    private boolean validate(String name, String idCard, String moreInfo) {
        return validateName(name) && validateIdCard(idCard) && validateMoreInfo(moreInfo);
    }

    private boolean validateName(String name) {
        if (name == null || name.isEmpty()) {
            mEdtName.setError(getString(R.string.name_invalid));
            return false;
        }
        return true;
    }

    private boolean validateIdCard(String idCard) {
        if (idCard== null || idCard.isEmpty() || idCard.trim().length() != Constant.LENGTH_ID_CARD){
            mEdtIdCard.setError(getString(R.string.id_card_invalid));
            return false;
        }
        return true;
    }

    private boolean validateMoreInfo(String moreInfo) {
        if (moreInfo == null || moreInfo.isEmpty() || moreInfo.length() < MIN_LENGTH_MORE_INFO) {
            mEdtMoreInfo.setError(getString(R.string.more_info_invalid));
            return false;
        }
        return true;
    }

    private void putDataToIssueThreeActivity() {
        Bundle bundle = new Bundle();
        bundle.putString(Constant.KEY_NAME, mEdtName.getText().toString().trim());
        bundle.putString(Constant.KEY_ID_CARD, mEdtIdCard.getText().toString().trim());
        bundle.putString(Constant.KEY_MORE_INFO, mEdtMoreInfo.getText().toString().trim());
        if (mRbIntermediate.isChecked()) {
            bundle.putString(Constant.KEY_DEGREE, getString(R.string.intermediate));
        } else if (mRbCollege.isChecked()) {
            bundle.putString(Constant.KEY_DEGREE, getString(R.string.college));
        } else if (mRbUniversity.isChecked()) {
            bundle.putString(Constant.KEY_DEGREE, getString(R.string.university));
        }
        StringBuilder interest = new StringBuilder();
        if (mCbReadBook.isChecked()) {
            interest.append(getString(R.string.read_book)).append(Constant.CHARACTER_SPLIT);
        }
        if (mCbReadNews.isChecked()) {
            interest.append(getString(R.string.read_news)).append(Constant.CHARACTER_SPLIT);
        }
        if (mCbCoding.isChecked()) {
            interest.append(getString(R.string.coding)).append(Constant.CHARACTER_SPLIT);
        }
        bundle.putString(Constant.KEY_INTEREST, interest.toString());
        Intent intent = new Intent(ExerciseUpdateInfoActivity.this, IssueThreeActivity.class);
        intent.putExtras(bundle);
        startActivity(intent);
    }
}
