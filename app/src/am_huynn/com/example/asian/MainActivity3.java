package com.example.asian;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.android.material.snackbar.Snackbar;

public class MainActivity3 extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main3);
        EditText edtName = findViewById(R.id.edt_name);
        EditText edtCard = findViewById(R.id.edt_cmnd);
        EditText edtInformation = findViewById(R.id.edt_more_information);
        Button btnSend = findViewById(R.id.btn_send);

        validatorEmpty(edtName, btnSend);
        validatorEmpty(edtCard, btnSend);
        validatorMoreInformation(edtInformation,btnSend);

        btnSend.setOnClickListener(view -> {
            showSnackBar(view, "Gửi thành công", 200);
        });
    }

    private void validatorEmpty(EditText editText, Button btn) {
        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (editText.getText().toString().isEmpty()) {
                    editText.setError("Không để trống giá trị này");
                    btn.setEnabled(false);
                } else {
                    btn.setEnabled(true);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }

    private void validatorMoreInformation(EditText edtInformation, Button btn) {
        edtInformation.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (edtInformation.getText().toString().isEmpty()) {
                    edtInformation.setError("Nhập thông tin bổ sung");
                    btn.setEnabled(false);
                } else if (edtInformation.getText().toString().length() < 100) {
                    edtInformation.setError("Thông tin nhiều hơn 100 ký tự");
                    btn.setEnabled(false);
                }else {
                    btn.setEnabled(true);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }

    public void showSnackBar(View view, String message, int duration) {
        Snackbar.make(view, message, duration).show();
    }
}


