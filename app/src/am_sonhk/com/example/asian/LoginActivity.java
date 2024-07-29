package com.example.asian;


import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class LoginActivity extends AppCompatActivity {

    private static final String VALID_EMAIL_DOMAIN = "gmail.com";
    private static final int MIN_PASSWORD_LENGTH = 8;
    private static final String REGEX_NUMBER = ".*\\d.*";
    private static final String REGEX_NORMAL_CHARACTER = ".*[a-z].*";
    private static final String REGEX_SPECIAL_CHARACTER = ".*[!@#$%^&*].*";
    private Button mBtnLogin;
    private EditText mEdtEmail;
    private EditText mEdtPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_activity);
        initUI();
        initListener();

    }

    private void initUI() {
        mEdtEmail = findViewById(R.id.edtEmail);
        mEdtPassword = findViewById(R.id.edtPassword);
        mBtnLogin = findViewById(R.id.btnLogin);
    }

    private void initListener() {
        mBtnLogin.setOnClickListener(v -> {
            if (validate(mEdtEmail.getText().toString().trim(), mEdtPassword.getText().toString().trim())) {
                Toast.makeText(this, R.string.login_success, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private boolean validate(String email, String password) {
        if (email.isEmpty()) {
            mEdtEmail.setError(getString(R.string.this_field_cant_empty));
            return false;
        }
        if (!email.contains(VALID_EMAIL_DOMAIN)) {
            mEdtEmail.setError(getString(R.string.email_invalid));
            return false;
        }
        if (password.isEmpty()) {
            mEdtPassword.setError(getString(R.string.this_field_cant_empty));
            return false;
        }
        if (!password.matches(REGEX_NORMAL_CHARACTER)) {
            mEdtPassword.setError(getString(R.string.password_missing_normal_character));
            return false;
        }
        if (!password.matches(REGEX_SPECIAL_CHARACTER)) {
            mEdtPassword.setError(getString(R.string.password_missing_special_character));
            return false;
        }
        if (!password.matches(REGEX_NUMBER)) {
            mEdtPassword.setError(getString(R.string.password_missing_number));
            return false;
        }
        if (!(password.length() > MIN_PASSWORD_LENGTH)) {
            mEdtPassword.setError(getString(R.string.password_must_more_than_8));
            return false;
        }
        return true;
    }
}
