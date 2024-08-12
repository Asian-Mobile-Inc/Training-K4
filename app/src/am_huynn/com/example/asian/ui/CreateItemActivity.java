package com.example.asian.ui;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import com.example.asian.R;

public class CreateItemActivity extends AppCompatActivity {
    private EditText mEdtCreateItem;
    private Button mBtnCancel;
    private Button mBtnCreate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_item);
        initView();
        initListener();
    }

    private void initView() {
        mEdtCreateItem = findViewById(R.id.edtCreateItem);
        mBtnCancel = findViewById(R.id.btnCancel);
        mBtnCreate = findViewById(R.id.btnCreate);
    }

    private void initListener() {
        mBtnCancel.setOnClickListener(view -> finish());

        mBtnCreate.setOnClickListener(view -> {
            if (!validatorName()) {
                return;
            }
            Intent intent = new Intent();
            intent.putExtra("keyNameBack", mEdtCreateItem.getText().toString());
            setResult(1000, intent);
            finish();
        });
    }

    private boolean validatorName() {
        if (mEdtCreateItem.getText().toString().isEmpty()) {
            mEdtCreateItem.setError(getString(R.string.name_is_not_empty));
            return false;
        } else {
            mEdtCreateItem.setError(null);
            return true;
        }
    }
}
