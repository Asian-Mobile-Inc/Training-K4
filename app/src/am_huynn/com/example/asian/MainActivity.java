package com.example.asian;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.example.asian.R;
import com.example.asian.ui.FacebookActivity;
import com.example.asian.ui.InformationActivity;
import com.example.asian.ui.MathActivity;

public class MainActivity extends AppCompatActivity {
    private Button mBtnToFacebook, mBtnToMath, mBtnToInformation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mBtnToFacebook = findViewById(R.id.btnFacebook);
        mBtnToMath = findViewById(R.id.btnMath);
        mBtnToInformation = findViewById(R.id.btnInformation);
        setOnClickButton();
    }

    private void setOnClickButton() {
        mBtnToFacebook.setOnClickListener(view -> {
            Intent intent = new Intent(this, FacebookActivity.class);
            startActivity(intent);
        });

        mBtnToMath.setOnClickListener(view -> {
            Intent intent = new Intent(this, MathActivity.class);
            startActivity(intent);
        });

        mBtnToInformation.setOnClickListener(view -> {
            Intent intent = new Intent(this, InformationActivity.class);
            startActivity(intent);
        });
    }
}
