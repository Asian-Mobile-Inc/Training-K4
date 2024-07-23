package com.example.asian;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Patterns;
import android.widget.Button;
import android.widget.EditText;

import java.util.regex.Pattern;

public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final Button BTN_LOGIN = findViewById(R.id.btn_login);
        final EditText EDT_EMAIL = findViewById(R.id.edt_email);
        final EditText EDT_PASSWORD = findViewById(R.id.edt_password);

        validatorEmail(EDT_EMAIL);
        validatorPassword(EDT_PASSWORD);
    }

    private void validatorEmail(EditText edtEmail) {
        edtEmail.addTextChangedListener(
                new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                    }

                    @Override
                    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                        if (!Patterns.EMAIL_ADDRESS.matcher(edtEmail.getText().toString()).matches()) {
                            edtEmail.setError("Email không đúng định dạng");
                        }
                    }

                    @Override
                    public void afterTextChanged(Editable editable) {

                    }
                }
        );
    }

    private void validatorPassword(EditText edtPassword) {

        final String PASSWORD_PATTERN = "^(?=.*[A-Z])(?=.*[@#$%^&+=!])(?=\\S+$).{4,}$";

        edtPassword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                final String PASSWORD_TEXT = edtPassword.getText().toString();

                if (PASSWORD_TEXT.length() < 8) {
                    edtPassword.setError("Mật khẩu có nhiều hơn 8 ký tự");
                } else if (!Pattern.compile(PASSWORD_PATTERN).matcher(PASSWORD_TEXT).matches()) {
                    edtPassword.setError("Mật khẩu có ít nhất 1 ký tự viết hoa và ký tự đặt biệt");
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }
}