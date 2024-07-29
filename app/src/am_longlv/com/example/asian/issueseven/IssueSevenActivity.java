package com.example.asian.issueseven;

import android.Manifest;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.example.asian.issueseven.service.MyLocationService;
import com.example.asian.R;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.ResolvableApiException;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResponse;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

public class IssueSevenActivity extends AppCompatActivity {
    private Button mBtnStartService;
    private Button mBtnStopService;
    private static final int REQUEST_CODE_LOCATION = 1;
    private static final int REQUEST_CODE_NOTIFICATION = 2;
    public static final String CHANNEL_ID = "channel_service_location";
    public static final String CHANNEL_NAME = "channel_service_location";
    private LocationRequest mLocationRequest;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_issue_seven);
        initUI();
        initListener();
        askPermission();
        setupLocationRequest();
        createChannelNotification();
    }

    private void initUI() {
        mBtnStartService = findViewById(R.id.btnStartService);
        mBtnStopService = findViewById(R.id.btnStopService);
    }

    private void initListener() {
        mBtnStartService.setOnClickListener(v -> {
            startFgrService();
        });
        mBtnStopService.setOnClickListener(v -> {
            Intent intent = new Intent(this, MyLocationService.class);
            stopService(intent);
        });
    }

    private void askPermission() {
        requestRuntimePermission(Manifest.permission.ACCESS_FINE_LOCATION);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            requestRuntimePermission(Manifest.permission.POST_NOTIFICATIONS);
        }
    }

    private void setupLocationRequest() {
        mLocationRequest = LocationRequest.create();
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        mLocationRequest.setInterval(5000);
        mLocationRequest.setFastestInterval(2000);
    }

    private void createChannelNotification() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, CHANNEL_NAME, NotificationManager.IMPORTANCE_DEFAULT);
            NotificationManager manager = getSystemService(NotificationManager.class);
            if (manager != null) {
                manager.createNotificationChannel(channel);
            }
        }
    }

    private void requestRuntimePermission(String idPermission) {
        switch (idPermission) {
            case Manifest.permission.ACCESS_FINE_LOCATION:
                if (!checkPermissionGranted(Manifest.permission.ACCESS_FINE_LOCATION) && !checkPermissionGranted(Manifest.permission.ACCESS_COARSE_LOCATION)) {
                    ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, REQUEST_CODE_LOCATION);
                } else {
                    if (!isGPSEnabled()) {
                        turnOnGPS();
                    }
                }
                break;
            case Manifest.permission.POST_NOTIFICATIONS:
                if (!(checkPermissionGranted(Manifest.permission.POST_NOTIFICATIONS)) && Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                    ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.POST_NOTIFICATIONS}, REQUEST_CODE_NOTIFICATION);
                } else {
                }
                break;
        }
    }

    private boolean isGPSEnabled() {
        LocationManager locationManager = null;
        boolean isEnable;
        if (locationManager == null) {
            locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        }
        isEnable = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        return isEnable;
    }

    private boolean checkPermissionGranted(String permission) {
        return ActivityCompat.checkSelfPermission(this, permission) == PackageManager.PERMISSION_GRANTED;
    }

    private void turnOnGPS() {
        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder()
                .addLocationRequest(mLocationRequest);
        builder.setAlwaysShow(true);
        Task<LocationSettingsResponse> result = LocationServices.getSettingsClient(getApplicationContext())
                .checkLocationSettings(builder.build());
        result.addOnCompleteListener(new OnCompleteListener<LocationSettingsResponse>() {
            @Override
            public void onComplete(@NonNull Task<LocationSettingsResponse> task) {
                try {
                    LocationSettingsResponse response = task.getResult(ApiException.class);
                } catch (ApiException e) {

                    switch (e.getStatusCode()) {
                        case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
                            try {
                                ResolvableApiException resolvableApiException = (ResolvableApiException) e;
                                resolvableApiException.startResolutionForResult(IssueSevenActivity.this, 2);
                            } catch (IntentSender.SendIntentException ex) {
                                ex.printStackTrace();
                            }
                            break;
                        case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
                            break;
                    }
                }
            }
        });
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
}
