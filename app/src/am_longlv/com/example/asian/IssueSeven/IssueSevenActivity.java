package com.example.asian.IssueSeven;

import android.Manifest;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.example.asian.R;

public class IssueSevenActivity extends AppCompatActivity {
    private Button mBtnReqLocation, mBtnReqNotification, mBtnStartService, mBtnStopService;
    private final int REQUEST_CODE_LOCATION = 1, REQUEST_CODE_NOTIFICATION = 2;
    private final String CHANNEL_ID = "channel_service_location", CHANNEL_NAME = "channel_service_location";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_issue_seven);
        initUI();
        initListener();
        initPermission();
        createChannelNotification();
    }

    private void createChannelNotification() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, CHANNEL_NAME, NotificationManager.IMPORTANCE_DEFAULT);
            NotificationManager manager = getSystemService(NotificationManager.class);
            if (manager != null){
                manager.createNotificationChannel(channel);
            }
        }
    }

    private void initUI() {
        mBtnReqLocation = findViewById(R.id.btnReqLocation);
        mBtnReqNotification = findViewById(R.id.btnReqNotification);
        mBtnStartService = findViewById(R.id.btnStartService);
        mBtnStopService = findViewById(R.id.btnStopService);
    }

    private void initListener() {
        mBtnReqLocation.setOnClickListener(v -> requestRuntimePermission(Manifest.permission.ACCESS_FINE_LOCATION));
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            mBtnReqNotification.setOnClickListener(v -> requestRuntimePermission(Manifest.permission.POST_NOTIFICATIONS));
        }
    }

    private void requestRuntimePermission(String accessFineLocation) {
        switch (accessFineLocation) {
            case Manifest.permission.ACCESS_FINE_LOCATION:
                if (!checkPermissionGranted(Manifest.permission.ACCESS_FINE_LOCATION)) {
                    ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_CODE_LOCATION);
                } else {
                    sendLocationInfo();
                }
                break;
            case Manifest.permission.POST_NOTIFICATIONS:
                if (!(checkPermissionGranted(Manifest.permission.POST_NOTIFICATIONS)) && Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                    ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.POST_NOTIFICATIONS}, REQUEST_CODE_NOTIFICATION);
                } else {
                    sendLocationInfo();
                }
                break;
        }
    }

    private boolean checkPermissionGranted(String permission) {
        return ActivityCompat.checkSelfPermission(this, permission) == PackageManager.PERMISSION_GRANTED;
    }

    private void sendLocationInfo() {

    }

    private void initPermission() {

    }
}
