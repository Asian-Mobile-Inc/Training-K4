package com.example.asian;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MainActivity2 extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        TextView tv = findViewById(R.id.tv_3);
        Button btnAddition = findViewById(R.id.btn_addition);
        Button btnSubtraction = findViewById(R.id.btn_subtraction);
        Button btnMultiplication = findViewById(R.id.btn_multiplication);
        Button btnDivision = findViewById(R.id.btn_division);
        EditText edtNumber1 = findViewById(R.id.edt_number1);
        EditText edtNumber2 = findViewById(R.id.edt_number2);
        btnAddition.setOnClickListener(view -> {
            if (isNumber(edtNumber1.getText().toString()) && isNumber(edtNumber2.getText().toString())) {
                double number1 = Double.parseDouble(edtNumber1.getText().toString());
                double number2 = Double.parseDouble(edtNumber2.getText().toString());
                tv.setText("Ket qua: " + (number1 + number2));
            }else {
                tv.setText("Ket qua: Vui lòng nhập số");
            }
        });

        btnSubtraction.setOnClickListener(view -> {
            if (isNumber(edtNumber1.getText().toString()) && isNumber(edtNumber2.getText().toString())) {
                double number1 = Double.parseDouble(edtNumber1.getText().toString());
                double number2 = Double.parseDouble(edtNumber2.getText().toString());
                tv.setText("Ket qua: " + (number1 - number2));
            }else {
                tv.setText("Ket qua: Vui lòng nhập số");
            }
        });

        btnMultiplication.setOnClickListener(view -> {
            if (isNumber(edtNumber1.getText().toString()) && isNumber(edtNumber2.getText().toString())) {
                double number1 = Double.parseDouble(edtNumber1.getText().toString());
                double number2 = Double.parseDouble(edtNumber2.getText().toString());
                tv.setText("Ket qua: " + (number1 * number2));
            }else {
                tv.setText("Ket qua: Vui lòng nhập số");
            }
        });


        btnDivision.setOnClickListener(view -> {
            if (isNumber(edtNumber1.getText().toString()) && numberGreaterZero(edtNumber2.getText().toString())) {
                double number1 = Double.parseDouble(edtNumber1.getText().toString());
                double number2 = Double.parseDouble(edtNumber2.getText().toString());
                tv.setText("Ket qua: " + (number1 / number2));
            }else {
                tv.setText("Ket qua: Vui lòng nhập số và số hạng 2 lớn hơn 0");
            }
        });
    }

    private boolean isNumber(String value) {
        try {
            Double.parseDouble(value);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    private boolean numberGreaterZero(String value) {
        try {
            double num = Double.parseDouble(value);
            if (num == 0) {
                return false;
            }
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}