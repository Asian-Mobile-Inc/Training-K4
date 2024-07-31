package com.example.asian.ui;

import android.Manifest;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.asian.R;
import com.example.asian.broadcast.BroadcastInternetConnectorReceiver;

public class RequestPermissionActivity extends AppCompatActivity {
    private BroadcastInternetConnectorReceiver mBroadcastInternetConnectorReceiver;

    public static int REQUEST_PERMISSION_CODE = 100;
    private Button mBtnRequestPermission;
    private Button mBtnOpenAppSetting;
    private Button mBtnOpenSettingGps;
    private Button mBtnStart;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_request_permission);
        initView();
        initReceiver();
        initListener();
    }

    private void initView() {
        mBtnRequestPermission = findViewById(R.id.btnRequestPermission);
        mBtnOpenAppSetting = findViewById(R.id.btnOpenAppSetting);
        mBtnOpenSettingGps = findViewById(R.id.btnSettingLocation);
        mBtnStart = findViewById(R.id.btnStart);
    }

    private void initReceiver() {
        mBroadcastInternetConnectorReceiver = new BroadcastInternetConnectorReceiver(this);
    }

    private void initListener() {
        mBtnRequestPermission.setOnClickListener(view -> clickRequestPermission());
        mBtnOpenAppSetting.setOnClickListener(view -> clickOpenAppSetting());
        mBtnOpenSettingGps.setOnClickListener(view -> clickOpenSettingGps());
        mBtnStart.setOnClickListener(view -> mBroadcastInternetConnectorReceiver.runLog());
    }

    private void clickOpenAppSetting() {
        Intent intent = new Intent(android.provider.Settings.ACTION_APPLICATION_DETAILS_SETTINGS, Uri.fromParts("package", getPackageName(), null));
        startActivity(intent);
    }

    private void clickOpenSettingGps() {
        Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
        startActivity(intent);
    }

    private void clickRequestPermission() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            return;
        }

        if (checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            Toast.makeText(this, "Permission location granted", Toast.LENGTH_LONG).show();
        } else {
            String[] permission = {Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION};
            requestPermissions(permission, REQUEST_PERMISSION_CODE);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == REQUEST_PERMISSION_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[1] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "Permission location granted", Toast.LENGTH_LONG).show();
            } else {
                boolean requestAgain = Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && shouldShowRequestPermissionRationale(permissions[0]);
                if (requestAgain) {
                    Toast.makeText(this, "Permission denied", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(this, "Go to settings and enable the permission", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }


    @Override
    protected void onStart() {
        super.onStart();
        IntentFilter intentFilter = new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
        registerReceiver(mBroadcastInternetConnectorReceiver, intentFilter);
    }

    @Override
    protected void onStop() {
        super.onStop();
        unregisterReceiver(mBroadcastInternetConnectorReceiver);
    }
}
