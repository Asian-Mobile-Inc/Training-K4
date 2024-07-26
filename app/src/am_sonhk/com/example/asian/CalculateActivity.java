package com.example.asian;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class CalculateActivity extends AppCompatActivity {

    private EditText mEdtNumberOne, mEdtNumberTwo;
    private Button mBtnAdd, mBtnSubtract, mBtnMultiply, mBtnDivide;
    private TextView mTvResult;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.calculate_activity);
        mEdtNumberOne = findViewById(R.id.edtNumberOne);
        mEdtNumberTwo = findViewById(R.id.edtNumberTwo);
        mBtnAdd = findViewById(R.id.btnAdd);
        mBtnSubtract = findViewById(R.id.btnSubtract);
        mBtnMultiply = findViewById(R.id.btnMultiply);
        mBtnDivide = findViewById(R.id.btnDivide);
        mTvResult = findViewById(R.id.tvResult);

        mBtnAdd.setOnClickListener(this::performOperation);
        mBtnSubtract.setOnClickListener(this::performOperation);
        mBtnMultiply.setOnClickListener(this::performOperation);
        mBtnDivide.setOnClickListener(this::performOperation);
    }


    private void performOperation(View view) {
        String numberoneStr = mEdtNumberOne.getText().toString();
        String numbertwoStr = mEdtNumberTwo.getText().toString();

        if (numberoneStr.isEmpty() || numbertwoStr.isEmpty()) {
            Toast.makeText(this, "Please enter both numbers", Toast.LENGTH_SHORT).show();
            return;
        }

        double numberone = Double.parseDouble(numberoneStr);
        double numbertwo = Double.parseDouble(numbertwoStr);
        double result = 0;

        switch (view.getId()) {
            case R.id.btnAdd:
                result = numberone + numbertwo;
                break;
            case R.id.btnSubtract:
                result = numberone - numbertwo;
                break;
            case R.id.btnMultiply:
                result = numberone * numbertwo;
                break;
            case R.id.btnDivide:
                if (numbertwo == 0) {
                    Toast.makeText(this, "Cannot divide by zero", Toast.LENGTH_SHORT).show();
                    return;
                }
                result = numberone / numbertwo;
                break;
        }

        mTvResult.setText("Kết quả: " + result);
    }
}
