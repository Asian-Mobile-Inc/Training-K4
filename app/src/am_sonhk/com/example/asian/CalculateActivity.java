package com.example.asian;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class CalculateActivity extends AppCompatActivity {

    private EditText mEdSohang1, mEtSohang2;
    private Button mBtnAdd, mBtnSubtract, mBtnMultiply, mBtnDivide;
    private TextView mTvResult;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);  // Ensure this matches your XML layout file name

        mEdSohang1 = findViewById(R.id.edSohang1);
        mEtSohang2 = findViewById(R.id.etSohang2);
        mBtnAdd = findViewById(R.id.btnAdd);
        mBtnSubtract = findViewById(R.id.btnSubtract);
        mBtnMultiply = findViewById(R.id.btnMultiply);
        mBtnDivide = findViewById(R.id.btnDivide);
        mTvResult = findViewById(R.id.tvResult);
        mBtnDivide.setOnClickListener(this::performOperation);
        mBtnSubtract.setOnClickListener(this::performOperation);
        mBtnMultiply.setOnClickListener(this::performOperation);
        mBtnDivide.setOnClickListener(this::performOperation);
    }

    private void performOperation(View view) {
        String sohang1Str = mEdSohang1.getText().toString();
        String sohang2Str = mEtSohang2.getText().toString();

        if (sohang1Str.isEmpty() || sohang2Str.isEmpty()) {
            Toast.makeText(this, "Please enter both numbers", Toast.LENGTH_SHORT).show();
            return;
        }

        double sohang1 = Double.parseDouble(sohang1Str);
        double sohang2 = Double.parseDouble(sohang2Str);
        double result = 0;

        switch (view.getId()) {
            case R.id.btnAdd:
                result = sohang1 + sohang2;
                break;
            case R.id.btnSubtract:
                result = sohang1 - sohang2;
                break;
            case R.id.btnMultiply:
                result = sohang1 * sohang2;
                break;
            case R.id.btnDivide:
                if (sohang2 == 0) {
                    Toast.makeText(this, "Cannot divide by zero", Toast.LENGTH_SHORT).show();
                    return;
                }
                result = sohang1 / sohang2;
                break;
        }

        mTvResult.setText("Kết quả: " + result);
    }
}
