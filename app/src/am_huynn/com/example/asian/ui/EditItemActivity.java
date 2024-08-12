package com.example.asian.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import com.example.asian.R;
import com.example.asian.constants.Constants;

public class EditItemActivity extends AppCompatActivity {
    private EditText mEdtItem;
    private Button mBtnCancel;
    private Button mBtnEdit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_item);
        initView();
        initValue();
        initListener();
    }

    private void initView() {
        mEdtItem = findViewById(R.id.edtEditItem);
        mBtnEdit = findViewById(R.id.btnEdit);
        mBtnCancel = findViewById(R.id.btnCancel);
    }

    private void initValue() {
        String name = getIntent().getStringExtra(Constants.KEY_NAME_ITEM);
        mEdtItem.setText(name);
    }

    private void initListener() {
        mBtnCancel.setOnClickListener(view -> {
            finish();
        });

        mBtnEdit.setOnClickListener(view -> {
            if (!validatorName()) {
                return;
            }
            Intent intent = new Intent();
            intent.putExtra(Constants.KEY_NAME_BACK, mEdtItem.getText().toString());
            intent.putExtra(Constants.KEY_ID_BACK, getIntent().getIntExtra(Constants.KEY_ID_ITEM, 0));
            setResult(Constants.RESULT_CODE_EDIT, intent);
            finish();
        });
    }

    private boolean validatorName() {
        if (mEdtItem.getText().toString().isEmpty()) {
            mEdtItem.setError(getString(R.string.name_is_not_empty));
            return false;
        } else {
            mEdtItem.setError(null);
            return true;
        }
    }
}