package com.example.asian;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;

public class MainActivity3 extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main3);
        validatorEmpty(findViewById(R.id.edt_name));
        validatorEmpty(findViewById(R.id.edt_cmnd));
        validatorMoreInformation(findViewById(R.id.edt_more_information));
    }

    private void validatorEmpty(EditText editText) {
        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (editText.getText().toString().isEmpty()) {
                    editText.setError("Không để trống giá trị này");
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }

    private void validatorMoreInformation(EditText edtInformation) {
        edtInformation.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (edtInformation.getText().toString().isEmpty()) {
                    edtInformation.setError("Nhập thông tin bổ sung");
                } else if (edtInformation.getText().toString().length() < 100) {
                    edtInformation.setError("Thông tin nhiều hơn 100 ký tự");
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }
}