package com.example.asian;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class CalculateActivity extends AppCompatActivity {

    private EditText edSohang1, etSohang2;
    private Button btnAdd, btnSubtract, btnMultiply, btnDivide;
    private TextView tvResult;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);  // Ensure this matches your XML layout file name

        edSohang1 = findViewById(R.id.edSohang1);
        etSohang2 = findViewById(R.id.etSohang2);
        btnAdd = findViewById(R.id.btnAdd);
        btnSubtract = findViewById(R.id.btnSubtract);
        btnMultiply = findViewById(R.id.btnMultiply);
        btnDivide = findViewById(R.id.btnDivide);
        tvResult = findViewById(R.id.tvResult);

        btnAdd.setOnClickListener(this::performOperation);
        btnSubtract.setOnClickListener(this::performOperation);
        btnMultiply.setOnClickListener(this::performOperation);
        btnDivide.setOnClickListener(this::performOperation);
    }

    private void performOperation(View view) {
        String sohang1Str = edSohang1.getText().toString();
        String sohang2Str = etSohang2.getText().toString();

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

        tvResult.setText("Kết quả: " + result);
    }
}
