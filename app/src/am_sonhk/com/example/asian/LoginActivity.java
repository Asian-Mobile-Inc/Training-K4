package com.example.asian;

import android.content.Intent;
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
    public static final String KEY_EMAIL = "keyEmail";
    public static final String KEY_PASSWORD = "keyPassword";
    public static final String KEY_FROM_LOGIN = "fromLogin";

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
            String email = mEdtEmail.getText().toString().trim();
            String password = mEdtPassword.getText().toString().trim();
            if (validate(email, password)) {
                Toast.makeText(this, R.string.login_success, Toast.LENGTH_SHORT).show();
                sendDataToSendDataActivity();
            }
        });
    }

    private void sendDataToSendDataActivity() {
        Bundle bundle = new Bundle();
        bundle.putString(KEY_EMAIL, mEdtEmail.getText().toString().trim());
        bundle.putString(KEY_PASSWORD, mEdtPassword.getText().toString().trim());
        Intent intent = new Intent(this, SendDataActivity.class);
        intent.putExtra(KEY_FROM_LOGIN, bundle);
        startActivity(intent);
    }

    private boolean validate(String email, String password) {
        return isValidEmail(email) && isValidPassword(password);
    }

    private boolean isValidEmail(String email) {
        boolean isValid = true;

        if (email.isEmpty()) {
            mEdtEmail.setError(getString(R.string.this_field_cant_empty));
            isValid = false;
        } else if (!email.contains(VALID_EMAIL_DOMAIN)) {
            mEdtEmail.setError(getString(R.string.email_invalid));
            isValid = false;
        }
        return isValid;
    }

    private boolean isValidPassword(String password) {
        boolean isValid = true;

        if (password.isEmpty()) {
            mEdtPassword.setError(getString(R.string.this_field_cant_empty));
            isValid = false;
        } else if (password.length() <= MIN_PASSWORD_LENGTH) {
            mEdtPassword.setError(getString(R.string.password_must_more_than_8));
            isValid = false;
        } else if (!password.matches(REGEX_NORMAL_CHARACTER)) {
            mEdtPassword.setError(getString(R.string.password_missing_normal_character));
            isValid = false;
        } else if (!password.matches(REGEX_SPECIAL_CHARACTER)) {
            mEdtPassword.setError(getString(R.string.password_missing_special_character));
            isValid = false;
        } else if (!password.matches(REGEX_NUMBER)) {
            mEdtPassword.setError(getString(R.string.password_missing_number));
            isValid = false;
        }
        return isValid;
    }
}
