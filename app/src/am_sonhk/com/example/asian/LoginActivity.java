package com.example.asian;
import android.annotation.SuppressLint;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class LoginActivity extends AppCompatActivity {

    private EditText etEmail, etPassword;
    private Button btnLogin;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        etEmail = findViewById(R.id.etEmail);
        etPassword = findViewById(R.id.etPassword);
        btnLogin = findViewById(R.id.btnLogin);

        btnLogin.setOnClickListener(v -> validateInputs());
    }

    private void validateInputs() {
        String email = etEmail.getText().toString().trim();
        String password = etPassword.getText().toString().trim();

        if (isValidEmail(email) && isValidPassword(password)) {
            // Proceed with login
            Toast.makeText(this, "Login successful", Toast.LENGTH_SHORT).show();
        } else {
            // Show error message
            Toast.makeText(this, "Invalid email or password", Toast.LENGTH_SHORT).show();
        }
    }

    private boolean isValidEmail(String email) {
        return !TextUtils.isEmpty(email) && Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    private boolean isValidPassword(String password) {
        if (password.length() < 8) return false;

        boolean hasNumber = false;
        boolean hasUpper = false;
        boolean hasLower = false;
        boolean hasSpecial = false;

        for (char c : password.toCharArray()) {
            if (Character.isDigit(c)) hasNumber = true;
            else if (Character.isUpperCase(c)) hasUpper = true;
            else if (Character.isLowerCase(c)) hasLower = true;
            else if (!Character.isLetterOrDigit(c)) hasSpecial = true;
        }

        // Ensure all required conditions are met
        return hasNumber && (hasUpper || hasLower) && hasSpecial;
    }
}

