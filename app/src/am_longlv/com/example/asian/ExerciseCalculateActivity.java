package com.example.asian;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class ExerciseCalculateActivity extends AppCompatActivity {
    private Button mBtnCong, mBtnTru, mBtnNhan, mBtnChia;
    private EditText mEdtSo1, mEdtSo2;
    private TextView mTvKetqua;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exercise_calculate);
        initUI();
        initListener();
    }

    private void initUI() {
        mBtnCong = findViewById(R.id.btn_cong);
        mBtnTru = findViewById(R.id.btn_tru);
        mBtnNhan = findViewById(R.id.btn_nhan);
        mBtnChia = findViewById(R.id.btn_chia);
        mEdtSo1 = findViewById(R.id.edt_so1);
        mEdtSo2 = findViewById(R.id.edt_so2);
        mTvKetqua = findViewById(R.id.tv_ketqua);
    }

    private void initListener() {
        mBtnCong.setOnClickListener(v -> {
            int so1 = Integer.parseInt(mEdtSo1.getText().toString().trim());
            int so2 = Integer.parseInt(mEdtSo2.getText().toString().trim());
            mTvKetqua.setText("Ket qua: " + (so1 + so2));
        });
        mBtnTru.setOnClickListener(v -> {
            int so1 = Integer.parseInt(mEdtSo1.getText().toString().trim());
            int so2 = Integer.parseInt(mEdtSo2.getText().toString().trim());
            mTvKetqua.setText("Ket qua: " + (so1 - so2));
        });
        mBtnNhan.setOnClickListener(v -> {
            int so1 = Integer.parseInt(mEdtSo1.getText().toString().trim());
            int so2 = Integer.parseInt(mEdtSo2.getText().toString().trim());
            mTvKetqua.setText("Ket qua: " + (so1 * so2));
        });
        mBtnChia.setOnClickListener(v -> {
            int so1 = Integer.parseInt(mEdtSo1.getText().toString().trim());
            int so2 = Integer.parseInt(mEdtSo2.getText().toString().trim());
            if (so2 == 0) {
                mTvKetqua.setError("Khong the chia cho 0");
                return;
            }
            mTvKetqua.setText("Ket qua: " + (so1 / so2));
        });
    }
}
