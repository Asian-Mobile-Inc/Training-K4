package com.example.asian;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    Button exercise1, exercise2, exercise3, btn_issues4;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initUI();
    }

    private void initUI() {
        exercise1 = findViewById(R.id.btn_exercise1);
        exercise2 = findViewById(R.id.btn_exercise2);
        exercise3 = findViewById(R.id.btn_exercise3);
        btn_issues4 = findViewById(R.id.btn_issues4);
        exercise1.setOnClickListener(v -> {
            startActivity(new Intent(MainActivity.this, Exercise1Activity.class));
        });
        exercise2.setOnClickListener(v -> {
            startActivity(new Intent(MainActivity.this, Exercise2Activity.class));
        });
        exercise3.setOnClickListener(v -> {
            startActivity(new Intent(MainActivity.this, Exercise3Activity.class));
        });
        btn_issues4.setOnClickListener(v -> {
            startActivity(new Intent(MainActivity.this, Issues4Activity.class));
        });
    }
}
