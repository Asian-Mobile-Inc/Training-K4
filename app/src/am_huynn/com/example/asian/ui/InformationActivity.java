package com.example.asian.ui;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import androidx.appcompat.app.AppCompatActivity;

import com.example.asian.R;
import com.example.asian.constants.Constants;

import java.util.ArrayList;

public class InformationActivity extends AppCompatActivity {
    private EditText mEdtName;
    private EditText mEdtCard;
    private EditText mEdtMoreInformation;
    private RadioGroup mRgDegree;
    private CheckBox mCbReadPaper;
    private CheckBox mCbReadBook;
    private CheckBox mCbReadCoding;
    private Button mBtnSend;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_information);
        initView();
        initListener();
    }

    public void initView() {
        mEdtName = findViewById(R.id.edtName);
        mEdtCard = findViewById(R.id.edtCard);
        mEdtMoreInformation = findViewById(R.id.edtMoreInformation);
        mRgDegree = findViewById(R.id.rgDegree);
        mBtnSend = findViewById(R.id.btnSend);
        mCbReadPaper = findViewById(R.id.cbReadPaper);
        mCbReadBook = findViewById(R.id.cbReadBook);
        mCbReadCoding = findViewById(R.id.cbReadCoding);
    }

    public void initListener() {
        mBtnSend.setOnClickListener(view -> {
            String name = mEdtName.getText().toString().trim();
            String card = mEdtCard.getText().toString().trim();
            String moreInformation = mEdtMoreInformation.getText().toString().trim();
            if (!validateName(name) || !validateCard(card) || !validateMoreInformation(moreInformation)) {
                return;
            }
            int idRadioCheck = mRgDegree.getCheckedRadioButtonId();
            RadioButton rbCheck = findViewById(idRadioCheck);
            String degree = rbCheck.getText().toString();
            String interest = valueCheckBox();
            Intent intent = new Intent(this, DataInformationActivity.class);
            Bundle bundle = new Bundle();
            bundle.putString(Constants.KEY_NAME, name);
            bundle.putString(Constants.KEY_CARD, card);
            bundle.putString(Constants.KEY_MORE_INFORMATION, moreInformation);
            bundle.putString(Constants.KEY_DEGREE, degree);
            if (!interest.isEmpty()) {
                bundle.putString(Constants.KEY_INTEREST, interest);
            }
            intent.putExtra(Constants.KEY_BUNDLE_MORE_INFORMATION, bundle);
            startActivity(intent);
        });

        mRgDegree.check(R.id.rbHighSchool);
    }

    public boolean validateName(String value) {
        boolean isValid;
        if (value.isEmpty()) {
            mEdtName.setError(getText(R.string.please_not_empty));
            isValid = false;
        } else {
            mEdtName.setError(null);
            isValid = true;
        }
        return isValid;
    }

    public boolean validateCard(String value) {
        if (value.isEmpty()) {
            mEdtCard.setError(getText(R.string.please_not_empty));
            return false;
        } else {
            mEdtCard.setError(null);
            return true;
        }
    }

    public boolean validateMoreInformation(String value) {
        boolean isValid;
        if (value.isEmpty()) {
            mEdtMoreInformation.setError(getText(R.string.please_not_empty));
            isValid = false;
        } else if (value.length() < Constants.MIN_LENGTH_MORE_INFORMATION) {
            mEdtMoreInformation.setError(getText(R.string.please_greater_hundred_character));
            isValid = false;
        } else {
            mEdtMoreInformation.setError(null);
            isValid = true;
        }
        return isValid;
    }

    private String valueCheckBox() {
        ArrayList<String> values = new ArrayList<>();
        StringBuilder value = new StringBuilder();
        if (mCbReadPaper.isChecked()) {
            values.add(mCbReadPaper.getText().toString());
        }

        if (mCbReadBook.isChecked()) {
            values.add(mCbReadBook.getText().toString());
        }

        if (mCbReadCoding.isChecked()) {
            values.add(mCbReadCoding.getText().toString());
        }

        for (int i = 0; i < values.size(); i++) {
            if (i != values.size() - 1) {
                value.append(values.get(i)).append(getString(R.string.separate));
            } else {
                value.append(values.get(i));
            }
        }
        return value.toString();
    }
}
