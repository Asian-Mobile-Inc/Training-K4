package com.example.asian.issueseven.service;

import android.Manifest;
import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.IBinder;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;

import com.example.asian.issueseven.IssueSevenActivity;
import com.example.asian.R;
import com.example.asian.issueseven.broadcast.BoadcastInternet;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;

public class MyLocationService extends Service {
    private static final String EXTRA_STARTED_FROM_NOTIFICATION = "started_from_notification";
    private static final String TITLE_NOTIFICATION = "Location Service";
    private static final String CONTENT_NOTIFICATION = "Location Service is running...";
    private static final String ACTION_STOP_SERVICE = "Stop Service";
    private static final String ACTION_CONNECTIVITY_CHANGE = "android.net.conn.CONNECTIVITY_CHANGE";
    private static final int NOTIFICATION_ID = 111;
    private static final int TIME_INTERVAL = 20000;
    private static final String TAG_LOG = "androidruntime";
    private LocationRequest mLocationRequest;
    private FusedLocationProviderClient mFusedLocationProviderClient;
    private boolean mIsInternetAvailable = false;
    private static MyLocationService INSTANCE = null;

    public MyLocationService() {
    }

    public static synchronized MyLocationService getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new MyLocationService();
        }
        return (INSTANCE);
    }

    public void setIsInternetChange(Context context, boolean internetStatus) {
        mIsInternetAvailable = internetStatus;
        if (mIsInternetAvailable) {
            Log.d(TAG_LOG, "Internet connected");
            requestLocationUpdates(context);
        } else {
            Log.d(TAG_LOG, "Internet disconnected");
            removeLocationUpdates(context);
        }
    }

    private final LocationCallback mLocationCallBack = new LocationCallback() {
        @Override
        public void onLocationResult(LocationResult locationResult) {
            super.onLocationResult(locationResult);
            int locationIndex = locationResult.getLocations().size() - 1;
            double latitude = locationResult.getLocations().get(locationIndex).getLatitude();
            double longitude = locationResult.getLocations().get(locationIndex).getLongitude();
            Log.d(TAG_LOG, latitude + " - " + longitude);
        }
    };

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        INSTANCE = this;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        boolean startedFromNotification = intent.getBooleanExtra(EXTRA_STARTED_FROM_NOTIFICATION, false);
        if (startedFromNotification) {
            stopService();
        } else {
            startService();
        }
        return START_STICKY;
    }

    private Notification createNotification() {
        Intent intent = new Intent(this, MyLocationService.class);
        intent.putExtra(EXTRA_STARTED_FROM_NOTIFICATION, true);
        PendingIntent servicePendingIntent;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            servicePendingIntent = PendingIntent.getService(this,
                    0, intent, PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);

        } else {
            servicePendingIntent = PendingIntent.getService(this,
                    0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            return new Notification.Builder(this, IssueSevenActivity.CHANNEL_ID)
                    .setContentTitle(TITLE_NOTIFICATION)
                    .setContentText(CONTENT_NOTIFICATION)
                    .setSmallIcon(android.R.drawable.ic_menu_mylocation)
                    .addAction(R.mipmap.ic_launcher, ACTION_STOP_SERVICE, servicePendingIntent)
                    .setOngoing(true)
                    .build();
        }
        return null;
    }

    private void startService() {
        BoadcastInternet boadcastInternet = new BoadcastInternet();
        IntentFilter intentFilter = new IntentFilter(ACTION_CONNECTIVITY_CHANGE);
        registerReceiver(boadcastInternet, intentFilter);
        setUpLocationRequest();
        requestLocationUpdates(this);
        startForeground(NOTIFICATION_ID, createNotification());
    }

    private void stopService() {
        removeLocationUpdates(this);
        stopForeground(true);
        if (mFusedLocationProviderClient != null) {
            mFusedLocationProviderClient = null;
        }
        stopSelf();
    }

    private void setUpLocationRequest() {
        mLocationRequest = new LocationRequest.Builder(TIME_INTERVAL)
                .build();
        if (mFusedLocationProviderClient == null) {
            mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        }
    }

    private void requestLocationUpdates(Context context) {
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        if (mFusedLocationProviderClient == null) {
            mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(context);
        }
        mFusedLocationProviderClient.requestLocationUpdates(mLocationRequest, mLocationCallBack, null);
    }

    private void removeLocationUpdates(Context context) {
        if (mFusedLocationProviderClient == null) {
            mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(context);
        }
        mFusedLocationProviderClient.removeLocationUpdates(mLocationCallBack);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        INSTANCE = null;
        stopService();
    }
}
