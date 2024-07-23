package com.example.asian;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class Exercise2Activity extends AppCompatActivity {
    Button btn_cong, btn_tru, btn_nhan, btn_chia;
    EditText edt_so1, edt_so2;
    TextView tv_ketqua;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exercise2);
        initUI();
    }
    private void initUI() {
        btn_cong = findViewById(R.id.btn_cong);
        btn_tru = findViewById(R.id.btn_tru);
        btn_nhan = findViewById(R.id.btn_nhan);
        btn_chia = findViewById(R.id.btn_chia);
        edt_so1 = findViewById(R.id.edt_so1);
        edt_so2 = findViewById(R.id.edt_so2);
        tv_ketqua = findViewById(R.id.tv_ketqua);
        btn_cong.setOnClickListener(v -> {
            int so1 = Integer.parseInt(edt_so1.getText().toString().trim());
            int so2 = Integer.parseInt(edt_so2.getText().toString().trim());
            tv_ketqua.setText("Ket qua: " + (so1 + so2));
        });
        btn_tru.setOnClickListener(v -> {
            int so1 = Integer.parseInt(edt_so1.getText().toString().trim());
            int so2 = Integer.parseInt(edt_so2.getText().toString().trim());
            tv_ketqua.setText("Ket qua: " + (so1 - so2));
        });
        btn_nhan.setOnClickListener(v -> {
            int so1 = Integer.parseInt(edt_so1.getText().toString().trim());
            int so2 = Integer.parseInt(edt_so2.getText().toString().trim());
            tv_ketqua.setText("Ket qua: " + (so1 * so2));
        });
        btn_chia.setOnClickListener(v -> {
            int so1 = Integer.parseInt(edt_so1.getText().toString().trim());
            int so2 = Integer.parseInt(edt_so2.getText().toString().trim());
            if (so2 == 0) {
                tv_ketqua.setError("Khong the chia cho 0");
                return;
            }
            tv_ketqua.setText("Ket qua: " + (so1 / so2));
        });
    }
}