package com.example.asian;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.asian.issuethree.IssueThreeActivity;

public class ExerciseLoginActivity extends AppCompatActivity {
    private Button mBtnLogin;
    private EditText mEdtEmail;
    private EditText mEdtPassword;

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
                putDataToIssueThreeActivity();
            }
        });
    }

    private boolean validate(String email, String password) {
        if (!checkEmailAdress(email)) {
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
        return password.matches(Constant.REGEX_NUMBER)
                && password.matches(Constant.REGEX_NORMAL_CHARACTER)
                && password.matches(Constant.REGEX_SPECIAL_CHARACTER)
                && password.length() >= Constant.MIN_LENGTH_PASSWORD;
    }

    private boolean checkEmailAdress(String email) {
        if (email == null || email.isEmpty()) {
            return false;
        }
        if (email.split(Constant.CHAR_SPLIT_EMAIL).length != Constant.COUNT_EMAIL_SENTENCES) {
            return false;
        }
        if (email.split(Constant.CHAR_SPLIT_EMAIL)[0].trim().isEmpty()) {
            return false;
        }
        return email.split(Constant.CHAR_SPLIT_EMAIL)[1].equals(Constant.EMAIL_ADDRESS);
    }

    private void putDataToIssueThreeActivity() {
        Bundle bundle = new Bundle();
        bundle.putString(Constant.KEY_EMAIL, mEdtEmail.getText().toString().trim());
        bundle.putString(Constant.KEY_PASSWORD, mEdtPassword.getText().toString().trim());
        Intent intent = new Intent(this, IssueThreeActivity.class);
        intent.putExtras(bundle);
        startActivity(intent);
        Toast.makeText(this, getString(R.string.login_success), Toast.LENGTH_SHORT).show();
    }
}
