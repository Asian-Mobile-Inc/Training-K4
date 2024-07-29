package com.example.asian.ui;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.asian.R;

public class MathActivity extends AppCompatActivity {
    private TextView mTvResult;
    private EditText mEdtNumberOne;
    private EditText mEdtNumberTwo;
    private Button mBtnAddition;
    private Button mBtnSubtraction;
    private Button mBtnMultiplication;
    private Button mBtnDivision;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_math);
        initView();
        initListener();
    }

    private void initView() {
        mTvResult = findViewById(R.id.tvResult);
        mEdtNumberOne = findViewById(R.id.edtNumberOne);
        mEdtNumberTwo = findViewById(R.id.edtNumberTwo);
        mBtnAddition = findViewById(R.id.btnAddition);
        mBtnSubtraction = findViewById(R.id.btnSubtraction);
        mBtnMultiplication = findViewById(R.id.btnMultiplication);
        mBtnDivision = findViewById(R.id.btnDivision);
    }

    private void initListener() {
        mBtnAddition.setOnClickListener(view -> {
            String textNumberOne = mEdtNumberOne.getText().toString();
            String textNumberTwo = mEdtNumberTwo.getText().toString();
            if (isNumber(textNumberOne) && isNumber(textNumberTwo)) {
                double numberOne = Double.parseDouble(textNumberOne);
                double numberTwo = Double.parseDouble(textNumberTwo);
                String result = getString(R.string.result) + (numberOne + numberTwo);
                mTvResult.setText(result);
            } else {
                mTvResult.setText(getString(R.string.please_input_number));
            }
        });

        mBtnSubtraction.setOnClickListener(view -> {
            String textNumberOne = mEdtNumberOne.getText().toString();
            String textNumberTwo = mEdtNumberTwo.getText().toString();
            if (isNumber(textNumberOne) && isNumber(textNumberTwo)) {
                double numberOne = Double.parseDouble(textNumberOne);
                double numberTwo = Double.parseDouble(textNumberTwo);
                String result = getString(R.string.result) + (numberOne - numberTwo);
                mTvResult.setText(result);
            } else {
                mTvResult.setText(getString(R.string.please_input_number));
            }
        });

        mBtnMultiplication.setOnClickListener(view -> {
            String textNumberOne = mEdtNumberOne.getText().toString();
            String textNumberTwo = mEdtNumberTwo.getText().toString();
            if (isNumber(textNumberOne) && isNumber(textNumberTwo)) {
                double numberOne = Double.parseDouble(textNumberOne);
                double numberTwo = Double.parseDouble(textNumberTwo);
                String result = getString(R.string.result) + (numberOne * numberTwo);
                mTvResult.setText(result);
            } else {
                mTvResult.setText(getString(R.string.please_input_number));
            }
        });


        mBtnDivision.setOnClickListener(view -> {
            String textNumberOne = mEdtNumberOne.getText().toString();
            String textNumberTwo = mEdtNumberTwo.getText().toString();
            if (isNumber(textNumberOne) && isNumberOtherZero(textNumberTwo)) {
                double numberOne = Double.parseDouble(textNumberOne);
                double numberTwo = Double.parseDouble(textNumberTwo);
                String result = getString(R.string.result) + (numberOne / numberTwo);
                mTvResult.setText(result);
            } else {
                mTvResult.setText(getString(R.string.please_input_number_other_zero));
            }
        });
    }

    private boolean isNumber(String value) {
        return !value.isEmpty();
    }

    private boolean isNumberOtherZero(String value) {
        if (value.isEmpty()) {
            return false;
        } else return Double.parseDouble(value) != 0;
    }
}
