package com.example.asian.IssueSeven;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import com.example.asian.ExerciseCalculateActivity;
import com.example.asian.ExerciseLoginActivity;
import com.example.asian.ExerciseUpdateInfoActivity;
import com.example.asian.R;

public class IssueSevenActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_issue_seven);
        initUI();
        initListener();
        initPermission();
    }

    private void initUI() {
    }

    private void initListener() {

    }

    private void initPermission() {

    }
}
