package com.example.asian;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class ExerciseCalculateActivity extends AppCompatActivity {
    private Button mBtnPlus;
    private Button mBtnSub;
    private Button mBtnMul;
    private Button mBtnDiv;
    private EditText mEdtFirstNumber;
    private EditText mEdtSecondNumber;
    private TextView mTvResult;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exercise_calculate);
        initUI();
        initListener();
    }

    private void initUI() {
        mBtnPlus = findViewById(R.id.btnAdd);
        mBtnSub = findViewById(R.id.btnSub);
        mBtnMul = findViewById(R.id.btnMul);
        mBtnDiv = findViewById(R.id.btnDiv);
        mEdtFirstNumber = findViewById(R.id.edtFirstNumber);
        mEdtSecondNumber = findViewById(R.id.edtSecondNumber);
        mTvResult = findViewById(R.id.tvResult);
    }

    private void initListener() {
        mBtnPlus.setOnClickListener(v -> {
            if (validate()) {
                return;
            }
            int firstNumber = Integer.parseInt((mEdtFirstNumber.getText()).toString().trim());
            int secondNumber = Integer.parseInt((mEdtSecondNumber.getText()).toString().trim());
            mTvResult.setText(
                    getString(R.string.result_float_param, (float) (firstNumber + secondNumber)));
        });
        mBtnSub.setOnClickListener(v -> {
            if (validate()) {
                return;
            }
            int firstNumber = Integer.parseInt((mEdtFirstNumber.getText()).toString().trim());
            int secondNumber = Integer.parseInt((mEdtSecondNumber.getText()).toString().trim());
            mTvResult.setText(
                    getString(R.string.result_float_param, (float) (firstNumber - secondNumber)));
        });
        mBtnMul.setOnClickListener(v -> {
            if (validate()) {
                return;
            }
            int firstNumber = Integer.parseInt((mEdtFirstNumber.getText()).toString().trim());
            int secondNumber = Integer.parseInt((mEdtSecondNumber.getText()).toString().trim());
            mTvResult.setText(
                    getString(R.string.result_float_param, (float) (firstNumber * secondNumber)));
        });
        mBtnDiv.setOnClickListener(v -> {
            if (validate()) {
                return;
            }
            int firstNumber = Integer.parseInt((mEdtFirstNumber.getText()).toString().trim());
            int secondNumber = Integer.parseInt((mEdtSecondNumber.getText()).toString().trim());
            if (secondNumber == 0) {
                mEdtSecondNumber.setError(getString(R.string.error_divided_zero));
                return;
            }
            float result = (float) firstNumber / secondNumber;
            mTvResult.setText(getString(R.string.result_float_param, result));
        });
    }

    private boolean validate() {
        if (mEdtFirstNumber.getText().toString().trim().isEmpty()) {
            mEdtFirstNumber.setError(getString(R.string.error_empty_input));
        }
        if (mEdtSecondNumber.getText().toString().trim().isEmpty()) {
            mEdtSecondNumber.setError(getString(R.string.error_empty_input));
        }
        try {
            Integer.parseInt((mEdtFirstNumber.getText()).toString().trim());
        } catch (Exception e) {
            mEdtFirstNumber.setError(getString(R.string.number_error));
            return true;
        }
        try {
            Integer.parseInt((mEdtFirstNumber.getText()).toString().trim());
        } catch (Exception e) {
            mEdtSecondNumber.setError(getString(R.string.number_error));
            return true;
        }
        return mEdtFirstNumber.getText().toString().trim().isEmpty()
                || mEdtSecondNumber.getText().toString().trim().isEmpty();
    }
}
