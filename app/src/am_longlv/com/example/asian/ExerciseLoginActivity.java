package com.example.asian;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class ExerciseLoginActivity extends AppCompatActivity {
    private Button mBtnLogin;
    private EditText mEdtEmail, mEdtPassword;

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
        final String EMAIL_ADDRESS = "@gmail.com";
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
        final String REGEX_NUMBER = ".*[0-9].*";
        final String REGEX_NORMAL_CHARACTER = ".*[a-z].*";
        final String REGEX_SPECIAL_CHARACTER = ".*[@#$%^&+=].*";
        return password.matches(REGEX_NUMBER) && password.matches(REGEX_NORMAL_CHARACTER) && password.matches(REGEX_SPECIAL_CHARACTER) && password.length() >= 8;
    }
}
