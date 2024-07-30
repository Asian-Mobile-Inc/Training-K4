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
            if (validate(mEdtEmail.getText().toString().trim(),
                    mEdtPassword.getText().toString().trim())) {
                putDataToIssueThreeActivity();
            }
        });
    }

    private boolean validate(String email, String password) {
        if (!checkEmailAddress(email)) {
            mEdtEmail.setError(getString(R.string.email_invalid));
        }
        return checkEmailAddress(email) && checkPassword(password);
    }

    private boolean checkPassword(String password) {
        if (password.length() < Constant.MIN_LENGTH_PASSWORD) {
            mEdtPassword.setError(getString(R.string.password_invalid));
        } else if (!password.matches(Constant.REGEX_NORMAL_CHARACTER)) {
            mEdtPassword.setError(getString(R.string.password_match_normal));
        } else if (!password.matches(Constant.REGEX_NUMBER)) {
            mEdtPassword.setError(getString(R.string.password_match_number));
        } else if (!password.matches(Constant.REGEX_SPECIAL_CHARACTER)) {
            mEdtPassword.setError(getString(R.string.password_match_special));
        }
        return password.matches(Constant.REGEX_NUMBER)
                && password.matches(Constant.REGEX_NORMAL_CHARACTER)
                && password.matches(Constant.REGEX_SPECIAL_CHARACTER)
                && password.length() >= Constant.MIN_LENGTH_PASSWORD;
    }

    private boolean checkEmailAddress(String email) {
        String[] emailArray = email.split(Constant.CHAR_SPLIT_EMAIL);
        return emailArray.length == Constant.COUNT_EMAIL_SENTENCES
                && !emailArray[0].trim().isEmpty()
                && emailArray[1].equals(Constant.EMAIL_ADDRESS);
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
