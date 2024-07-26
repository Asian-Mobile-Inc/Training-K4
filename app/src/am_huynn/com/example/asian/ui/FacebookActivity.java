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
            if (!validateEmail() || !validatePassword()) {
                return;
            }

            String email = mEdtEmail.getText().toString();
            String password = mEdtPassword.getText().toString();

            Intent intent = new Intent(this, DataActivity.class);
            intent.putExtra(Constants.KEY_EMAIL, email);
            intent.putExtra(Constants.KEY_PASSWORD, password);
            startActivity(intent);
        });
    }

    private boolean validateEmail() {
        String value = mEdtEmail.getText().toString();

        if (value.isEmpty()) {
            mEdtEmail.setError(getString(R.string.field_can_not_empty));
            return false;
        } else if (!Patterns.EMAIL_ADDRESS.matcher(value).matches()) {
            mEdtEmail.setError(getString(R.string.invalid_email));
            return false;
        } else {
            mEdtEmail.setError(null);
            return true;
        }
    }

    private boolean validatePassword() {
        String value = mEdtPassword.getText().toString();

        if (value.isEmpty()) {
            mEdtPassword.setError(getString(R.string.field_can_not_empty));
            return false;
        } else if (value.length() < Constants.MIN_LENGTH_PASSWORD) {
            mEdtPassword.setError(getString(R.string.password_more_than_eight_characters));
            return false;
        } else if (!value.matches(Constants.REGEX_INCLUDE_NUMBER)) {
            mEdtPassword.setError(getString(R.string.password_must_include_number));
            return false;
        } else if (!value.matches(Constants.REGEX_NORMAL_CHARACTER)) {
            mEdtPassword.setError(getString(R.string.password_must_include_normal_characters));
            return false;
        } else if (!value.matches(Constants.REGEX_SPECIAL_CHARACTER)) {
            mEdtPassword.setError(getString(R.string.password_must_include_special_characters));
            return false;
        } else {
            mEdtPassword.setError(null);
            return true;
        }
    }
}
