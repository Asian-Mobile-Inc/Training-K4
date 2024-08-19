package com.example.asian.ui;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import com.example.asian.R;
import com.example.asian.constants.Constants;

public class FacebookActivity extends AppCompatActivity {
    private EditText mEdtEmail;
    private EditText mEdtPassword;
    private Button mBtnLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_facebook);
        initView();
        initListener();
    }

    private void initView() {
        mEdtEmail = findViewById(R.id.edtEmail);
        mEdtPassword = findViewById(R.id.edtPassword);
        mBtnLogin = findViewById(R.id.btnLogin);
    }

    private void initListener() {
        mBtnLogin.setOnClickListener(view -> {
            String email = mEdtEmail.getText().toString().trim();
            String password = mEdtPassword.getText().toString().trim();
            if (!validateEmail(email) || !validatePassword(password)) {
                return;
            }

            Intent intent = new Intent(this, DataActivity.class);
            Bundle bundle = new Bundle();
            bundle.putString(Constants.KEY_EMAIL, email);
            bundle.putString(Constants.KEY_PASSWORD, password);
            intent.putExtra(Constants.KEY_BUNDLE_LOGIN, bundle);
            startActivity(intent);
        });
    }

    private boolean validateEmail(String value) {
        boolean isValidEmail;
        if (value.isEmpty()) {
            mEdtEmail.setError(getString(R.string.field_can_not_empty));
            isValidEmail = false;
        } else if (!Patterns.EMAIL_ADDRESS.matcher(value).matches()) {
            mEdtEmail.setError(getString(R.string.invalid_email));
            isValidEmail = false;
        } else {
            mEdtEmail.setError(null);
            isValidEmail = true;
        }
        return isValidEmail;
    }

    private boolean validatePassword(String value) {
        boolean isValidPassword;
        if (value.isEmpty()) {
            mEdtPassword.setError(getString(R.string.field_can_not_empty));
            isValidPassword = false;
        } else if (value.length() < Constants.MIN_LENGTH_PASSWORD) {
            mEdtPassword.setError(getString(R.string.password_more_than_eight_characters));
            isValidPassword = false;
        } else if (!value.matches(Constants.REGEX_INCLUDE_NUMBER)) {
            mEdtPassword.setError(getString(R.string.password_must_include_number));
            isValidPassword = false;
        } else if (!value.matches(Constants.REGEX_NORMAL_CHARACTER)) {
            mEdtPassword.setError(getString(R.string.password_must_include_normal_characters));
            isValidPassword = false;
        } else if (!value.matches(Constants.REGEX_SPECIAL_CHARACTER)) {
            mEdtPassword.setError(getString(R.string.password_must_include_special_characters));
            isValidPassword = false;
        } else {
            mEdtPassword.setError(null);
            isValidPassword = true;
        }
        return isValidPassword;
    }
}
