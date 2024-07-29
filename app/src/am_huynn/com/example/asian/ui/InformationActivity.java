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
            if (!validateName() || !validateCard() || !validateMoreInformation()) {
                return;
            }
            int idRadioCheck = mRgDegree.getCheckedRadioButtonId();
            RadioButton rbCheck = findViewById(idRadioCheck);
            String name = mEdtName.getText().toString();
            String card = mEdtCard.getText().toString();
            String moreInformation = mEdtMoreInformation.getText().toString();
            String degree = rbCheck.getText().toString();
            String interest = valueCheckBox();


            Intent intent = new Intent(this, DataInformationActivity.class);
            intent.putExtra(Constants.KEY_NAME, name);
            intent.putExtra(Constants.KEY_CARD, card);
            intent.putExtra(Constants.KEY_MORE_INFORMATION, moreInformation);
            intent.putExtra(Constants.KEY_DEGREE, degree);
            if (!interest.isEmpty()) {
                intent.putExtra(Constants.KEY_INTEREST, interest);
            }

            startActivity(intent);
        });

        mRgDegree.check(R.id.rbHighSchool);
    }

    public boolean validateName() {
        String value = mEdtName.getText().toString();

        if (value.isEmpty()) {
            mEdtName.setError(getText(R.string.please_not_empty));
            return false;
        } else {
            mEdtName.setError(null);
            return true;
        }
    }

    public boolean validateCard() {
        String value = mEdtCard.getText().toString();

        if (value.isEmpty()) {
            mEdtCard.setError(getText(R.string.please_not_empty));
            return false;
        } else {
            mEdtCard.setError(null);
            return true;
        }
    }

    public boolean validateMoreInformation() {
        String value = mEdtMoreInformation.getText().toString();

        if (value.isEmpty()) {
            mEdtMoreInformation.setError(getText(R.string.please_not_empty));
            return false;
        } else if (value.length() < Constants.MIN_LENGTH_MORE_INFORMATION) {
            mEdtMoreInformation.setError(getText(R.string.please_greater_hundred_character));
            return false;
        } else {
            mEdtMoreInformation.setError(null);
            return true;
        }
    }

    private String valueCheckBox() {
        ArrayList<String> values = new ArrayList<>();
        StringBuilder value = new StringBuilder();
        if (mCbReadPaper.isChecked()) values.add(mCbReadPaper.getText().toString());
        if (mCbReadBook.isChecked()) values.add(mCbReadBook.getText().toString());
        if (mCbReadCoding.isChecked()) values.add(mCbReadCoding.getText().toString());

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
