package com.example.asian;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class Exercise3Activity extends AppCompatActivity {
    Button btn_send_info;
    EditText edt_name, edt_cmnd, edt_more_info;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exercise3);
        initUI();
    }
    private void initUI() {
        btn_send_info = findViewById(R.id.btn_send_info);
        edt_name = findViewById(R.id.edt_name);
        edt_cmnd = findViewById(R.id.edt_cmnd);
        edt_more_info = findViewById(R.id.edt_more_info);
        btn_send_info.setOnClickListener(v -> {
            if (validate(edt_name.getText().toString().trim(), edt_cmnd.getText().toString().trim(), edt_more_info.getText().toString().trim())) {
                Toast.makeText(this, "Send info success", Toast.LENGTH_SHORT).show();
            }
        });
    }
    private boolean validate(String name, String cmnd, String moreInfo) {
        if (name == null || name.isEmpty()) {
            edt_name.setError("Name is invalid");
            return false;
        }
        if (cmnd == null || cmnd.isEmpty())  {
            edt_cmnd.setError("CMND is invalid");
            return false;
        }
        if (moreInfo == null || moreInfo.isEmpty() || moreInfo.length() < 100) {
            edt_more_info.setError("More info is invalid");
            return false;
        }
        return true;
    }
}