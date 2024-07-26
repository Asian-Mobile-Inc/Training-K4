package com.example.asian.ui;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import com.example.asian.R;
import com.example.asian.constants.Constants;

public class InformationActivity extends AppCompatActivity {
    private EditText mEdtName;
    private EditText mEdtCard;
    private EditText mEdtMoreInformation;
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
        mBtnSend = findViewById(R.id.btnSend);
    }

    public void initListener() {
        mBtnSend.setOnClickListener(view -> {
            if (!validateEmpty(mEdtName) || !validateEmpty(mEdtCard) || !validateGreaterHundredChar(mEdtMoreInformation)) {
                return;
            }
            String name = mEdtName.getText().toString();
            String card = mEdtCard.getText().toString();
            String moreInformation = mEdtMoreInformation.getText().toString();

            Intent intent = new Intent(this, DataInformationActivity.class);
            intent.putExtra(Constants.KEY_NAME, name);
            intent.putExtra(Constants.KEY_CARD, card);
            intent.putExtra(Constants.KEY_MORE_INFORMATION, moreInformation);
            startActivity(intent);
        });
    }

    public boolean validateEmpty(EditText edt) {
        String value = edt.getText().toString();

        if (value.isEmpty()) {
            edt.setError(getText(R.string.please_not_empty));
            return false;
        } else {
            edt.setError(null);
            return true;
        }
    }

    public boolean validateGreaterHundredChar(EditText edt) {
        String value = edt.getText().toString();

        if (value.isEmpty()) {
            edt.setError(getText(R.string.please_not_empty));
            return false;
        } else if (value.length() < Constants.MIN_LENGTH_MORE_INFORMATION) {
            edt.setError(getText(R.string.please_greater_hundred_character));
            return false;
        } else {
            edt.setError(null);
            return true;
        }
    }
}
