package com.example.asian;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import com.example.asian.fragment.Issues4f1Fragment;
import com.example.asian.fragment.Issues4f2Fragment;

public class Issues4Activity extends AppCompatActivity {
    Button btn_replace_fragment, btn_add_fragment;
    EditText edt_colorCode;
    int clickBtn = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_issues4);
        initUI();
    }

    private void initUI() {
        btn_replace_fragment = findViewById(R.id.btn_replace_fragment);
        btn_add_fragment = findViewById(R.id.btn_add_fragment);
        edt_colorCode = findViewById(R.id.edt_color);
        btn_add_fragment.setOnClickListener(v -> {
            addFragment();
        });
        btn_replace_fragment.setOnClickListener(v -> {
            replaceFragment();
        });
    }

    private void replaceFragment() {
        this.setTitle("Fragment One");
        Bundle bundle = new Bundle();
        bundle.putString("colorCode", edt_colorCode.getText().toString().trim());
        Issues4f1Fragment issues4f1Fragment = new Issues4f1Fragment();
        issues4f1Fragment.setArguments(bundle);
        if (clickBtn == 1) {
            getSupportFragmentManager().beginTransaction().replace(R.id.fr_layout, issues4f1Fragment).commit();
        } else {
            clickBtn = 1;
            getSupportFragmentManager().beginTransaction().replace(R.id.fr_layout, issues4f1Fragment).addToBackStack(null).commit();
        }
    }

    private void addFragment() {
        this.setTitle("Fragment Two");
        Bundle bundle = new Bundle();
        bundle.putString("colorCode", edt_colorCode.getText().toString().trim());
        Issues4f2Fragment issues4f2Fragment = new Issues4f2Fragment();
        issues4f2Fragment.setArguments(bundle);
        if (clickBtn == 2) {
            getSupportFragmentManager().beginTransaction().add(R.id.fr_layout, issues4f2Fragment).commit();
        } else {
            clickBtn = 2;
            getSupportFragmentManager().beginTransaction().add(R.id.fr_layout, issues4f2Fragment).addToBackStack(null).commit();
        }
    }
}