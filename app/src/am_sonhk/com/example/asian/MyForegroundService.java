package com.example.asian;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Intent;
import android.os.Build;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;

public class MyForegroundService extends Service {

    private Handler mHandler;
    private Runnable mLocationRunnable;
    private GpsTracker mGpsTracker;
    private static final String CHANNEL_ID = "LocationServiceChannel";

    @Override
    public void onCreate() {
        super.onCreate();
        mHandler = new Handler();
        mGpsTracker = new GpsTracker(this);

        mLocationRunnable = new Runnable() {
            @Override
            public void run() {
                logLocation();
                mHandler.postDelayed(this, 2000); // Repeat every 20 seconds
            }
        };

        // Start location logging
        mHandler.post(mLocationRunnable);

        // Create a notification for the foreground service
        createNotificationChannel();
        Notification notification = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setContentTitle("Location Service")
                .setContentText("Logging location in the background")
                .build();

        // Start the service in the foreground
        startForeground(1, notification);
    }

    private void logLocation() {
        if (mGpsTracker != null && mGpsTracker.canGetLocation()) {
            double latitude = mGpsTracker.getLatitude();
            double longitude = mGpsTracker.getLongitude();
            Log.d("TAG", "Background Service Location - Latitude: " + latitude + " Longitude: " + longitude);
        }
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return START_STICKY; // Restart the service if it gets killed
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mHandler.removeCallbacks(mLocationRunnable);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel serviceChannel = new NotificationChannel(
                    CHANNEL_ID,
                    "Location Service Channel",
                    NotificationManager.IMPORTANCE_DEFAULT
            );
            NotificationManager manager = getSystemService(NotificationManager.class);
            if (manager != null) {
                manager.createNotificationChannel(serviceChannel);
            }
        }
    }
}
