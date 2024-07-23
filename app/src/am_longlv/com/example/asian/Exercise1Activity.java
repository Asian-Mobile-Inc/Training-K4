package com.example.asian;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class Exercise1Activity extends AppCompatActivity {
    Button btn_Login;
    EditText edt_Email, edt_Password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exercise1);
        initUI();
    }

    private void initUI() {
        btn_Login = findViewById(R.id.btn_login);
        edt_Email = findViewById(R.id.edt_email);
        edt_Password = findViewById(R.id.edt_password);
        btn_Login.setOnClickListener(v -> {
            if (validate(edt_Email.getText().toString().trim(), edt_Password.getText().toString().trim())) {
                Toast.makeText(this, "Login success", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private boolean validate(String email, String password) {
        if (email.split("@").length != 2) {
            edt_Email.setError("Email is invalid");
            return false;
        }
        if (!email.split("@")[1].equals("gmail.com")) {
            edt_Email.setError("Email is invalid");
            return false;
        }
        if (password.length() < 8) {
            edt_Password.setError("Password is invalid");
            return false;
        }
        if (!(password.matches(".*[a-z].*") && password.matches(".*[0-9].*") && password.matches(".*[!@#$%^&*].*"))) {
            edt_Password.setError("Password is invalid");
            return false;
        }
        return true;
    }
}