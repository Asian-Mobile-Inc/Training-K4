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
        if (email.split("@").length != 2) {
            mEdtEmail.setError(getString(R.string.email_invalid));
            return false;
        }
        if (!email.split("@")[1].equals("gmail.com")) {
            mEdtPassword.setError(getString(R.string.email_invalid));
            return false;
        }
        if (password.length() < 8) {
            mEdtPassword.setError(getString(R.string.password_invalid));
            return false;
        }
        if (!(password.matches(".*[a-z].*") && password.matches(".*[0-9].*") && password.matches(".*[!@#$%^&*].*"))) {
            mEdtPassword.setError(getString(R.string.password_invalid));
            return false;
        }
        return true;
    }
}
