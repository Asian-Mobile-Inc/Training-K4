package com.example.asian.issueseven;

import android.Manifest;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.widget.Button;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.example.asian.issueseven.service.MyLocationService;
import com.example.asian.R;
import com.google.android.material.snackbar.Snackbar;

public class IssueSevenActivity extends AppCompatActivity {
    private Button mBtnStartService;
    private Button mBtnStopService;
    private static final int REQUEST_CODE_PERMISSION = 1;
    public static final String CHANNEL_ID = "channel_service_location";
    public static final String CHANNEL_NAME = "channel_service_location";
    private boolean mIsGranted = true;
    private String[] mPermissions;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_issue_seven);
        initUI();
        initListener();
        askPermission();
        createChannelNotification();
    }

    private void initUI() {
        mBtnStartService = findViewById(R.id.btnStartService);
        mBtnStopService = findViewById(R.id.btnStopService);
    }

    private void initListener() {
        mBtnStartService.setOnClickListener(v -> {
            if (!mIsGranted) {
                showSnakeBarAskPermission();
            } else {
                startFgrService();
            }
        });
        mBtnStopService.setOnClickListener(v -> {
            Intent intent = new Intent(this, MyLocationService.class);
            stopService(intent);
        });
    }

    private void askPermission() {
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.TIRAMISU) {
            mPermissions = new String[]{
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION,
                    Manifest.permission.POST_NOTIFICATIONS
            };
        } else {
            mPermissions = new String[]{
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION
            };
        }
        if (!hasPermissions(this, mPermissions)) {
            ActivityCompat.requestPermissions(this, mPermissions, REQUEST_CODE_PERMISSION);
        }
    }

    private void createChannelNotification() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, CHANNEL_NAME,
                    NotificationManager.IMPORTANCE_DEFAULT);
            NotificationManager manager = getSystemService(NotificationManager.class);
            if (manager != null) {
                manager.createNotificationChannel(channel);
            }
        }
    }

    public static boolean hasPermissions(Context context, String... permissions) {
        if (context != null && permissions != null) {
            for (String permission : permissions) {
                if (ActivityCompat.checkSelfPermission(context, permission) !=
                        PackageManager.PERMISSION_GRANTED) {
                    return false;
                }
            }
        }
        return true;
    }

    private void startFgrService() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            startForegroundService(new Intent(this, MyLocationService.class));
        } else {
            startService(new Intent(this, MyLocationService.class));
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_CODE_PERMISSION) {
            for (int i = 0; i < permissions.length; i++) {
                if (grantResults[i] != PackageManager.PERMISSION_GRANTED) {
                    showSnakeBarAskPermission();
                    mIsGranted = false;
                    break;
                }
            }
        }
    }

    private void showSnakeBarAskPermission() {
        Snackbar snackbar = Snackbar.make(findViewById(R.id.llMainIssueSeven),
                getResources().getString(
                        R.string.message_no_storage_permission_snackbar),
                Snackbar.LENGTH_LONG);
        snackbar.setAction(getResources().getString(R.string.setting), v -> {
            Intent intent = new Intent();
            intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
            Uri uri = Uri.fromParts("package",
                    IssueSevenActivity.this.getPackageName(), null);
            intent.setData(uri);
            permissionActivityResultLauncher.launch(intent);
        });
        snackbar.show();
    }

    ActivityResultLauncher<Intent> permissionActivityResultLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    mIsGranted = hasPermissions(IssueSevenActivity.this, mPermissions);
                }
            });
}
