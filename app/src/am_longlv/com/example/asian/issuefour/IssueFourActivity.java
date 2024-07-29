package com.example.asian.issuefour;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.asian.Constant;
import com.example.asian.issuefour.fragment.IssueFourFirstFragment;
import com.example.asian.issuefour.fragment.IssueFourSecondFragment;
import com.example.asian.R;

public class IssueFourActivity extends AppCompatActivity implements IssueFourFirstFragment.OnFragmentFirstChange, IssueFourSecondFragment.OnFragmentSecondChange {
    private Button mBtnReplaceFragment;
    private Button mBtnAddFragment;
    private EditText mEdtColorCode;
    private TextView mTvStatusClick;
    private int mClickBtnReplace = 0;
    private int mClickBtnAdd = 0;
    private static final int MAX_COUNT_CLICK = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_issue_four);
        initUI();
        initListener();
    }

    private void initUI() {
        mBtnReplaceFragment = findViewById(R.id.btnReplaceFragment);
        mBtnAddFragment = findViewById(R.id.btnAddFragment);
        mEdtColorCode = findViewById(R.id.edtChangeColor);
        mTvStatusClick = findViewById(R.id.tvStatusClick);
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
        Bundle bundle = new Bundle();
        bundle.putString(Constant.KEY_COLOR_CODE, mEdtColorCode.getText().toString().trim());
        IssueFourFirstFragment issueFourFirstFragment = new IssueFourFirstFragment();
        issueFourFirstFragment.setArguments(bundle);
        mClickBtnAdd = 0;
        mClickBtnReplace++;
        if (mClickBtnReplace > MAX_COUNT_CLICK) {
            getSupportFragmentManager().beginTransaction().replace(R.id.frLayout, issueFourFirstFragment).commit();
            mTvStatusClick.setText(getString(R.string.replace_fragment) + Constant.CHARACTER_SPLIT + getString(R.string.no_add_to_back_stack));
        } else {
            mTvStatusClick.setText(getString(R.string.replace_fragment) + Constant.CHARACTER_SPLIT + getString(R.string.add_to_back_stack));
            getSupportFragmentManager().beginTransaction().replace(R.id.frLayout, issueFourFirstFragment).addToBackStack(null).commit();
        }
    }

    private void addFragment() {
        Bundle bundle = new Bundle();
        bundle.putString(Constant.KEY_COLOR_CODE, mEdtColorCode.getText().toString().trim());
        IssueFourSecondFragment issueFourSecondFragment = new IssueFourSecondFragment();
        issueFourSecondFragment.setArguments(bundle);
        mClickBtnReplace = 0;
        mClickBtnAdd++;
        if (mClickBtnAdd > MAX_COUNT_CLICK) {
            mTvStatusClick.setText(getString(R.string.add_fragment) + Constant.CHARACTER_SPLIT + getString(R.string.no_add_to_back_stack));
            getSupportFragmentManager().beginTransaction().add(R.id.frLayout, issueFourSecondFragment).commit();
        } else {
            mTvStatusClick.setText(getString(R.string.add_fragment) + Constant.CHARACTER_SPLIT + getString(R.string.add_to_back_stack));
            getSupportFragmentManager().beginTransaction().add(R.id.frLayout, issueFourSecondFragment).addToBackStack(null).commit();
        }
    }

    @Override
    public void onFragmentSecondChange(String colorCode) {
        this.setTitle(getString(R.string.fragment_two) + " " + colorCode);
    }

    @Override
    public void onFragmentFirstChange(String colorCode) {
        this.setTitle(getString(R.string.fragment_one) + " " + colorCode);
    }
}
