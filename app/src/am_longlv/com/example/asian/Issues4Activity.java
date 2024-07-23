package com.example.asian;

import android.os.Bundle;
import android.util.Log;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.asian.fragment.Issues4_1Fragment;
import com.example.asian.fragment.Issues4_2Fragment;

public class Issues4Activity extends AppCompatActivity {

    Button btn_replace_fragment,btn_add_fragment;
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
            Log.e("androidruntime", "Add Fragment " + clickBtn);
            Bundle bundle = new Bundle();
            bundle.putString("colorCode",edt_colorCode.getText().toString().trim());
            Issues4_2Fragment issues4_2Fragment = new Issues4_2Fragment();
            issues4_2Fragment.setArguments(bundle);
            if (clickBtn == 1) {
                getSupportFragmentManager().beginTransaction().add(R.id.fr_layout, issues4_2Fragment).commit();
            }else{
                clickBtn = 1;
                getSupportFragmentManager().beginTransaction().add(R.id.fr_layout, issues4_2Fragment).addToBackStack(null).commit();
            }
        });
        btn_replace_fragment.setOnClickListener(v -> {
            Bundle bundle = new Bundle();
            bundle.putString("colorCode",edt_colorCode.getText().toString().trim());
            Issues4_1Fragment issues4_1Fragment = new Issues4_1Fragment();
            issues4_1Fragment.setArguments(bundle);
            Log.e("androidruntime", "Replace Fragment " + clickBtn);
            if (clickBtn == 2) {
                getSupportFragmentManager().beginTransaction().replace(R.id.fr_layout, issues4_1Fragment).commit();
            }else{
                clickBtn = 2;
                getSupportFragmentManager().beginTransaction().replace(R.id.fr_layout, issues4_1Fragment).addToBackStack(null).commit();
            }
        });
    }
}