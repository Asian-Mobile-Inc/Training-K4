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

public class LoginActivity extends AppCompatActivity {
    Button btnLogin;
    EditText edtEmail, edtPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main1);
        initUI();

    }

    private void initUI() {
        btnLogin = findViewById(R.id.btnLogin);
        edtEmail = findViewById(R.id.edtEmail);
        edtPassword = findViewById(R.id.edtPassword);

        btnLogin.setOnClickListener(v -> {
            if (validate(edtEmail.getText().toString().trim(), edtPassword.getText().toString().trim())){
                Toast.makeText(this, "Login success", Toast.LENGTH_SHORT).show();
            }
        });
    }
    private boolean validate(String email,String password){
        if (email.split("@").length != 2){
            edtEmail.setError("Email is invalid");
            return false;
        }
        if (!email.split("@")[1].equals("gmail.com")){
            edtEmail.setError("Email is invalid");
            return false;
        }
        if (password.length() < 8){
            edtPassword.setError("Password is invalid");
            return false;
        }
        if (!(password.matches(".*[a-z].*") && password.matches(".*[0-9].*") && password.matches(".*[!@#$%^&*].*"))){
            edtPassword.setError("Password is invalid");
            return false;
        }
        return true;
    }
}