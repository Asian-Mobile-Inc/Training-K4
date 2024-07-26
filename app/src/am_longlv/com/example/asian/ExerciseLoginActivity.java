package com.example.asian;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class ExerciseLoginActivity extends AppCompatActivity {
    private Button mBtnLogin;
    private EditText mEdtEmail;
    private EditText mEdtPassword;
    private static final String EMAIL_ADDRESS = "@gmail.com";
    private static final String REGEX_NUMBER = ".*\\d.*";
    private static final String REGEX_NORMAL_CHARACTER = ".*[a-z].*";
    private static final String REGEX_SPECIAL_CHARACTER = ".*[!@#$%^&*].*";
    private static final int MIN_LENGTH_PASSWORD = 8;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exercise_login);
        initUI();
        initListener();
    }

    private void initUI() {
        mBtnLogin = findViewById(R.id.btnLogin);
        mEdtEmail = findViewById(R.id.edtEmail);
        mEdtPassword = findViewById(R.id.edtPassword);
    }

    private void initListener() {
        mBtnLogin.setOnClickListener(v -> {
            if (validate(mEdtEmail.getText().toString().trim(), mEdtPassword.getText().toString().trim())) {
                Toast.makeText(this, getString(R.string.login_success), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private boolean validate(String email, String password) {
        if (!email.contains(EMAIL_ADDRESS)) {
            mEdtEmail.setError(getString(R.string.email_invalid));
            return false;
        }
        if (!checkPassword(password)) {
            mEdtPassword.setError(getString(R.string.password_invalid));
            return false;
        }
        return true;
    }

    private boolean checkPassword(String password) {
        return password.matches(REGEX_NUMBER) && password.matches(REGEX_NORMAL_CHARACTER) && password.matches(REGEX_SPECIAL_CHARACTER) && password.length() >= MIN_LENGTH_PASSWORD;
    }
}
