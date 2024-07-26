package com.example.asian.IssueSeven.Service;

import android.Manifest;
import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.location.LocationManager;
import android.os.Binder;
import android.os.Build;
import android.os.IBinder;
import android.os.Looper;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;

import com.example.asian.IssueSeven.IssueSevenActivity;
import com.example.asian.R;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.ResolvableApiException;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResponse;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

public class MyLocationService extends Service {
    private static final String EXTRA_STARTED_FROM_NOTIFICATION = "started_from_notification",
            TITLE_NOTIFICATION = "Location Service", CONTENT_NOTIFICATION = "Location Service is running...",
            ACTION_START_ACTIVITY = "Start Activity", ACTION_STOP_SERVICE = "Stop Service";
    private static final int NOTIFICATION_ID = 111;
    private static final int TIME_INTERVAL = 20000, FASTEST_INTERVAL = 10000;
    private static final String TAG_LOG = "androidruntime";
    private LocationRequest mLocationRequest;
    private FusedLocationProviderClient mFusedLocationProviderClient;
    private final LocationCallback mLocationCallBack = new LocationCallback() {
        @Override
        public void onLocationResult(LocationResult locationResult) {
            super.onLocationResult(locationResult);
            if (locationResult != null && locationResult.getLocations() != null) {
                int locationIndex = locationResult.getLocations().size() - 1;
                double latitude = locationResult.getLocations().get(locationIndex).getLatitude();
                double longitude = locationResult.getLocations().get(locationIndex).getLongitude();
                Log.d(TAG_LOG, latitude + " - " + longitude);
            }
        }
    };

    public class LocalBinder extends Binder {
        public MyLocationService getService() {
            return MyLocationService.this;
        }
    }

    private final IBinder mLocalBinder = new LocalBinder();

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        stopForeground(true);
        return mLocalBinder;
    }

    @Override
    public void onRebind(Intent intent) {
        stopForeground(true);
        super.onRebind(intent);
    }

    @Override
    public boolean onUnbind(Intent intent) {
        removeLocationUpdates();
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.O) {
            startService();
        }
        return super.onUnbind(intent);
    }

    @Override
    public void onCreate() {
        sendInfoLocation();
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
        return START_NOT_STICKY;
    }

    private Notification notification() {
        Intent intent = new Intent(this, MyLocationService.class);
        intent.putExtra(EXTRA_STARTED_FROM_NOTIFICATION, true);
        PendingIntent servicePendingIntent;
        PendingIntent activityPendingIntent;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            activityPendingIntent = PendingIntent.getActivity(this,
                    0, new Intent(this, IssueSevenActivity.class), PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);
            servicePendingIntent = PendingIntent.getService(this,
                    0, intent, PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);

        } else {
            activityPendingIntent = PendingIntent.getActivity(this,
                    0, new Intent(this, IssueSevenActivity.class), PendingIntent.FLAG_UPDATE_CURRENT);
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
        startForeground(NOTIFICATION_ID, notification());
    }

    private void stopService() {
        removeLocationUpdates();
        stopForeground(true);
        stopSelf();
    }
    private void sendInfoLocation(){
        if (mFusedLocationProviderClient == null) {
            mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        }
        mLocationRequest = LocationRequest.create();
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        mLocationRequest.setInterval(TIME_INTERVAL);
        mLocationRequest.setFastestInterval(FASTEST_INTERVAL);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        mFusedLocationProviderClient.requestLocationUpdates(mLocationRequest, mLocationCallBack, null);
    }
    private void removeLocationUpdates() {
        if (mFusedLocationProviderClient == null) {
            return;
        }
        mFusedLocationProviderClient.removeLocationUpdates(mLocationCallBack);
    }
}
