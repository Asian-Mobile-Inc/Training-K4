package com.example.asian;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

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
            if (!validateInput()) {
                return;
            }
            int firstNumber = Integer.parseInt((mEdtFirstNumber.getText()).toString().trim());
            int secondNumber = Integer.parseInt((mEdtSecondNumber.getText()).toString().trim());
            mTvResult.setText(getString(R.string.result) + (firstNumber + secondNumber));
        });
        mBtnSub.setOnClickListener(v -> {
            if (!validateInput()) {
                return;
            }
            int firstNumber = Integer.parseInt((mEdtFirstNumber.getText()).toString().trim());
            int secondNumber = Integer.parseInt((mEdtSecondNumber.getText()).toString().trim());
            mTvResult.setText(getString(R.string.result) + (firstNumber - secondNumber));
        });
        mBtnMul.setOnClickListener(v -> {
            if (!validateInput()) {
                return;
            }
            int firstNumber = Integer.parseInt((mEdtFirstNumber.getText()).toString().trim());
            int secondNumber = Integer.parseInt((mEdtSecondNumber.getText()).toString().trim());
            mTvResult.setText(getString(R.string.result) + (firstNumber * secondNumber));
        });
        mBtnDiv.setOnClickListener(v -> {
            if (!validateInput()) {
                return;
            }
            int firstNumber = Integer.parseInt((mEdtFirstNumber.getText()).toString().trim());
            int secondNumber = Integer.parseInt((mEdtSecondNumber.getText()).toString().trim());
            if (secondNumber == 0) {
                mEdtSecondNumber.setError(getString(R.string.error_divided_zero));
                Toast.makeText(this, getString(R.string.error_divided_zero), Toast.LENGTH_SHORT).show();
                return;
            }
            float result = (float) firstNumber / secondNumber;
            mTvResult.setText(getString(R.string.result) + (result));
        });
    }

    private boolean validateInput() {
        return !mEdtFirstNumber.getText().toString().trim().isEmpty() && !mEdtSecondNumber.getText().toString().trim().isEmpty();
    }
}
