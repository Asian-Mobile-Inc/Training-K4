package com.example.asian;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class ExerciseCalculateActivity extends AppCompatActivity implements View.OnClickListener {
    private Button mBtnCong, mBtnTru, mBtnNhan, mBtnChia;
    private EditText mEdtSoDau, mEdtSoSau;
    private TextView mTvKetqua;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exercise_calculate);
        initUI();
        initListener();
    }

    private void initUI() {
        mBtnCong = findViewById(R.id.btnCong);
        mBtnTru = findViewById(R.id.btnTru);
        mBtnNhan = findViewById(R.id.btnNhan);
        mBtnChia = findViewById(R.id.btnChia);
        mEdtSoDau = findViewById(R.id.edtSoDau);
        mEdtSoSau = findViewById(R.id.edtSoSau);
        mTvKetqua = findViewById(R.id.tvKetQua);
    }

    private void initListener() {
        mBtnCong.setOnClickListener(this);
        mBtnTru.setOnClickListener(this);
        mBtnNhan.setOnClickListener(this);
        mBtnChia.setOnClickListener(this);
    }

    private boolean validateInput() {
        if (mEdtSoSau.getText().toString().trim() == "" || mEdtSoSau.getText().toString().trim() == "") {
            return false;
        }
        return true;
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public void onClick(View v) {
        if (!validateInput()) {
            return;
        }
        int soDau = 0, soSau = 0;
        try {
            soDau = Integer.parseInt(mEdtSoDau.getText().toString().trim());
            soSau = Integer.parseInt(mEdtSoSau.getText().toString().trim());
            if (v.getId() == R.id.btnChia) {
                if (soSau == 0) {
                    mTvKetqua.setError("Khong the chia cho 0");
                    return;
                }
            }
            calculate(v.getId(), soDau, soSau);
        } catch (Exception e) {
            Log.e("androidruntime", e.getMessage());
            Toast.makeText(this, "Loi", Toast.LENGTH_SHORT).show();
        }
    }

    private void calculate(int id, int soDau, int soSau) {
        switch (id) {
            case R.id.btnCong:
                mTvKetqua.setText(getString(R.string.textview_text_ket_qua) + String.valueOf(soDau + soSau));
                break;
            case R.id.btnTru:
                mTvKetqua.setText(getString(R.string.textview_text_ket_qua) + String.valueOf(soDau - soSau));
                break;
            case R.id.btnNhan:
                mTvKetqua.setText(getString(R.string.textview_text_ket_qua) + String.valueOf(soDau * soSau));
                break;
            case R.id.btnChia:
                mTvKetqua.setText(getString(R.string.textview_text_ket_qua) + String.valueOf(soDau / soSau));
                break;
        }
    }
}
