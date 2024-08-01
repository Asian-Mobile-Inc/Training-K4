package com.example.asian;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class CalculateActivity extends AppCompatActivity {

    private EditText mEdtNumberOne;
    private EditText mEdtNumberTwo;
    private Button mBtnAdd;
    private Button mBtnSubtract;
    private Button mBtnMultiply;
    private Button mBtnDivide;
    private TextView mTvResult;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.calculate_activity);
        initView();
        initListener();
    }

    private void initView() {
        mEdtNumberOne = findViewById(R.id.edtNumberOne);
        mEdtNumberTwo = findViewById(R.id.edtNumberTwo);
        mBtnAdd = findViewById(R.id.btnAdd);
        mBtnSubtract = findViewById(R.id.btnSubtract);
        mBtnMultiply = findViewById(R.id.btnMultiply);
        mBtnDivide = findViewById(R.id.btnDivide);
        mTvResult = findViewById(R.id.tvResult);
    }

    private void initListener() {
        mBtnAdd.setOnClickListener(v -> {
            if (!validateInput()) {
                return;
            }
            int numberOne = Integer.parseInt(mEdtNumberOne.getText().toString().trim());
            int numberTwo = Integer.parseInt(mEdtNumberTwo.getText().toString().trim());
            mTvResult.setText(getString(R.string.result_float_param, (float) (numberOne + numberTwo)));
        });

        mBtnSubtract.setOnClickListener(v -> {
            if (!validateInput()) {
                return;
            }
            int numberOne = Integer.parseInt(mEdtNumberOne.getText().toString().trim());
            int numberTwo = Integer.parseInt(mEdtNumberTwo.getText().toString().trim());
            mTvResult.setText(getString(R.string.result_float_param, (float) (numberOne - numberTwo)));
        });

        mBtnMultiply.setOnClickListener(v -> {
            if (!validateInput()) {
                return;
            }
            int numberOne = Integer.parseInt(mEdtNumberOne.getText().toString().trim());
            int numberTwo = Integer.parseInt(mEdtNumberTwo.getText().toString().trim());
            mTvResult.setText(getString(R.string.result_float_param, (float) (numberOne * numberTwo)));
        });

        mBtnDivide.setOnClickListener(v -> {
            if (!validateInput()) {
                return;
            }
            int numberOne = Integer.parseInt(mEdtNumberOne.getText().toString().trim());
            int numberTwo = Integer.parseInt(mEdtNumberTwo.getText().toString().trim());
            if (numberTwo == 0) {
                mEdtNumberTwo.setError(getString(R.string.cant_divide_by_zero));
                Toast.makeText(this, getString(R.string.cant_divide_by_zero), Toast.LENGTH_SHORT).show();
                return;
            }
            float result = (float) numberOne / numberTwo;
            mTvResult.setText(getString(R.string.result_float_param, result));
        });
    }


    private boolean validateInput() {
        boolean isValid = true;

        String numberOne = mEdtNumberOne.getText().toString().trim();
        String numberTwo = mEdtNumberTwo.getText().toString().trim();

        if (numberOne.isEmpty()) {
            mEdtNumberOne.setError(getString(R.string.pls_enter_the_first_number));
            isValid = false;
        }
        if (numberTwo.isEmpty()) {
            mEdtNumberTwo.setError(getString(R.string.pls_enter_the_second_number));
            isValid = false;
        }

        return isValid;
    }

}
