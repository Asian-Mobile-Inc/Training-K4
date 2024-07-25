package com.example.asian;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Objects;

public class ExerciseCalculateActivity extends AppCompatActivity implements View.OnClickListener {
    private Button mBtnPlus, mBtnSub, mBtnMul, mBtnDiv;
    private EditText mEdtFirstNumber, mEdtSecondNumber;
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
        mBtnPlus.setOnClickListener(this);
        mBtnSub.setOnClickListener(this);
        mBtnMul.setOnClickListener(this);
        mBtnDiv.setOnClickListener(this);
    }

    private boolean validateInput() {
        return !mEdtFirstNumber.getText().toString().trim().isEmpty() && !mEdtSecondNumber.getText().toString().trim().isEmpty();
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public void onClick(View v) {
        if (!validateInput()) {
            return;
        }
        int firstNumber = 0, secondNumber = 0;
        try {
            firstNumber = Integer.parseInt(mEdtFirstNumber.getText().toString().trim());
            secondNumber = Integer.parseInt(mEdtSecondNumber.getText().toString().trim());
            if (v.getId() == R.id.btnDiv) {
                if (secondNumber == 0) {
                    mTvResult.setError(getString(R.string.error_divided_zero));
                    return;
                }
            }
            calculate(v.getId(), firstNumber, secondNumber);
        } catch (Exception e) {
            mTvResult.setError(getString(R.string.error_divided_zero));
        }
    }

    @SuppressLint({"SetTextI18n"})
    private void calculate(int id, int firstNumber, int secondNumber) {
        switch (id) {
            case (R.id.btnAdd):
                mTvResult.setText(getString(R.string.result) + (firstNumber + secondNumber));
                break;
            case (R.id.btnSub):
                mTvResult.setText(getString(R.string.result) + (firstNumber - secondNumber));
                break;
            case (R.id.btnMul):
                mTvResult.setText(getString(R.string.result) + firstNumber * secondNumber);
                break;
            case (R.id.btnDiv):
                mTvResult.setText(getString(R.string.result) + firstNumber / secondNumber);
                break;
        }
    }
}
