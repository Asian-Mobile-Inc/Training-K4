package com.example.asian;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Patterns;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;

import com.google.android.material.snackbar.Snackbar;

import java.util.regex.Pattern;

public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button btnLogin = findViewById(R.id.btnLogin);
        EditText edt_email = findViewById(R.id.edtEmail);
        EditText edtPassword = findViewById(R.id.edtPassword);

        btnLogin.setEnabled(false);
        validatorEmailAndPassword(edt_email, edtPassword,btnLogin);

        btnLogin.setOnClickListener(view -> {
            showSnackBar(view, "Đăng nhập thành công", 200);
        });
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return super.onTouchEvent(event);
    }

    private void validatorEmailAndPassword(EditText edtEmail, EditText edtPassword, Button btn) {
        final String PASSWORD_PATTERN = "^(?=.*[A-Z])(?=.*[@#$%^&+=!])(?=\\S+$).{4,}$";

        edtEmail.addTextChangedListener(
                new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                    }

                    @Override
                    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                        if (!Patterns.EMAIL_ADDRESS.matcher(edtEmail.getText().toString()).matches()) {
                            btn.setEnabled(false);
                            edtEmail.setError("Email không đúng định dạng");
                        }else {
                            btn.setEnabled(true);
                        }
                    }

                    @Override
                    public void afterTextChanged(Editable editable) {

                    }
                }
        );


        edtPassword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                final String PASSWORD_TEXT = edtPassword.getText().toString();

                if (PASSWORD_TEXT.length() < 8) {
                    edtPassword.setError("Mật khẩu có nhiều hơn 8 ký tự");
                    btn.setEnabled(false);
                } else if (!Pattern.compile(PASSWORD_PATTERN).matcher(PASSWORD_TEXT).matches()) {
                    edtPassword.setError("Mật khẩu có ít nhất 1 ký tự viết hoa và ký tự đặt biệt");
                    btn.setEnabled(false);
                }else {
                    btn.setEnabled(true);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }

    public void showSnackBar(View view, String message, int duration) {
        Snackbar.make(view, message, duration).show();
    }

    public static void hideKeyboard(Activity activity) {
        View view = activity.findViewById(android.R.id.content);
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }
}
