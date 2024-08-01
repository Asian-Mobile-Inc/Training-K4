package com.example.asian.issueseven.service;

import android.Manifest;
import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.core.app.ActivityCompat;

import com.example.asian.issueseven.IssueSevenActivity;
import com.example.asian.R;
import com.example.asian.issueseven.broadcast.BroadcastInternet;
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
    private static final String LOG_INTERNET_CONNECTED = "Internet connected";
    private static final String LOG_INTERNET_DISCONNECTED = "Internet disconnected";
    private static final int NOTIFICATION_ID = 111;
    private static final int TIME_INTERVAL = 20000;
    private static final String TAG_LOG = "androidRuntime";
    private LocationRequest mLocationRequest;
    private FusedLocationProviderClient mFusedLocationProviderClient;
    private final BroadcastReceiver mBroadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction() != null && intent.getAction().equals(BroadcastInternet.ACTION_INTERNET_CHANGE)) {
                setIsInternetChange(context, intent.getBooleanExtra(BroadcastInternet.KEY_INTERNET_CHANGE, false));
            }
        }
    };

    public void setIsInternetChange(Context context, boolean internetStatus) {
        if (internetStatus) {
            Log.d(TAG_LOG, LOG_INTERNET_CONNECTED);
            requestLocationUpdates(context);
        } else {
            Log.d(TAG_LOG, LOG_INTERNET_DISCONNECTED);
            removeLocationUpdates();
        }
    }

    private final LocationCallback mLocationCallBack = new LocationCallback() {
        @Override
        public void onLocationResult(@NonNull LocationResult locationResult) {
            super.onLocationResult(locationResult);
            int locationIndex = locationResult.getLocations().size() - 1;
            double latitude = locationResult.getLocations().get(locationIndex).getLatitude();
            double longitude = locationResult.getLocations().get(locationIndex).getLongitude();
            Log.d(TAG_LOG, latitude + " - " + longitude);
            Toast.makeText(MyLocationService.this, latitude + " - " + longitude, Toast.LENGTH_SHORT).show();
        }
    };

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @RequiresApi(api = Build.VERSION_CODES.TIRAMISU)
    @Override
    public void onCreate() {
        BroadcastInternet broadcastInternet = new BroadcastInternet();
        IntentFilter intentFilter = new IntentFilter(ACTION_CONNECTIVITY_CHANGE);
        registerReceiver(broadcastInternet, intentFilter);
        IntentFilter iFActionInternet = new IntentFilter(BroadcastInternet.ACTION_INTERNET_CHANGE);
        registerReceiver(mBroadcastReceiver, iFActionInternet, Context.RECEIVER_NOT_EXPORTED);

        super.onCreate();
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
        Notification.Action action = new Notification.Action.Builder(R.mipmap.ic_launcher, ACTION_STOP_SERVICE, servicePendingIntent).build();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            return new Notification.Builder(this, IssueSevenActivity.CHANNEL_ID)
                    .setContentTitle(TITLE_NOTIFICATION)
                    .setContentText(CONTENT_NOTIFICATION)
                    .setSmallIcon(android.R.drawable.ic_menu_mylocation)
                    .addAction(action)
                    .setOngoing(true)
                    .build();
        }
        return null;
    }

    private void startService() {
        setUpLocationRequest();
        requestLocationUpdates(this);
        startForeground(NOTIFICATION_ID, createNotification());
    }

    private void stopService() {
        removeLocationUpdates();
        stopForeground(true);
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

    private void removeLocationUpdates() {
        if (mFusedLocationProviderClient == null) {
            return;
        }
        mFusedLocationProviderClient.removeLocationUpdates(mLocationCallBack);
        mFusedLocationProviderClient = null;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        stopService();
    }
}
