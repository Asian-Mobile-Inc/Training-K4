package com.example.asian.IssuesFour;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import com.example.asian.IssuesFour.fragment.IssuesFourFirstFragment;
import com.example.asian.IssuesFour.fragment.IssuesFourSecondFragment;
import com.example.asian.R;

public class IssuesFourActivity extends AppCompatActivity {

    private Button mBtnReplaceFragment, mBtnAddFragment;
    private EditText mEdtColorCode;
    private int mClickBtn = 0;
    private static final int ID_BTN_REPLACE_FRAGMENT = 1, ID_BTN_ADD_FRAGMENT = 2;
    private static final String KEY_COLOR_CODE = "colorCode";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_issues_four);
        initUI();
        initListener();
    }

    private void initUI() {
        mBtnReplaceFragment = findViewById(R.id.btnReplaceFragment);
        mBtnAddFragment = findViewById(R.id.btnAddFragment);
        mEdtColorCode = findViewById(R.id.edtChangeColor);
    }

    private void initListener() {
        mBtnAddFragment.setOnClickListener(v -> {
            addFragment();
        });
        mBtnReplaceFragment.setOnClickListener(v -> {
            replaceFragment();
        });
    }

    private void replaceFragment() {
        this.setTitle(getString(R.string.fragment_one));
        Bundle bundle = new Bundle();
        bundle.putString(KEY_COLOR_CODE, mEdtColorCode.getText().toString().trim());
        IssuesFourFirstFragment issuesFourFirstFragment = new IssuesFourFirstFragment();
        issuesFourFirstFragment.setArguments(bundle);
        if (mClickBtn == ID_BTN_REPLACE_FRAGMENT) {
            getSupportFragmentManager().beginTransaction().replace(R.id.frLayout, issuesFourFirstFragment).commit();
        } else {
            mClickBtn = ID_BTN_REPLACE_FRAGMENT;
            getSupportFragmentManager().beginTransaction().replace(R.id.frLayout, issuesFourFirstFragment).addToBackStack(null).commit();
        }
    }

    private void addFragment() {
        this.setTitle(getString(R.string.fragment_two));
        Bundle bundle = new Bundle();
        bundle.putString(KEY_COLOR_CODE, mEdtColorCode.getText().toString().trim());
        IssuesFourSecondFragment issuesFourSecondFragment = new IssuesFourSecondFragment();
        issuesFourSecondFragment.setArguments(bundle);
        if (mClickBtn == ID_BTN_ADD_FRAGMENT) {
            getSupportFragmentManager().beginTransaction().add(R.id.frLayout, issuesFourSecondFragment).commit();
        } else {
            mClickBtn = ID_BTN_ADD_FRAGMENT;
            getSupportFragmentManager().beginTransaction().add(R.id.frLayout, issuesFourSecondFragment).addToBackStack(null).commit();
        }
    }
}
