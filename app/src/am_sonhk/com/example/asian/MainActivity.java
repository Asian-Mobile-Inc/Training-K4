package com.example.asian;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    private GpsTracker mGpsTracker;
    private TextView tvLatitude, tvLongitude;
    private NetworkChangeReceiver mNetworkChangeReceiver;
    private Handler mHandler;
    private Runnable mLocationRunnable;
    private boolean isNetworkConnected = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tvLatitude = findViewById(R.id.latitude);
        tvLongitude = findViewById(R.id.longitude);

        mHandler = new Handler(Looper.getMainLooper());
        mNetworkChangeReceiver = new NetworkChangeReceiver();
        // Set up the location logging task
        mLocationRunnable = new Runnable() {
            @Override
            public void run() {
                if (isNetworkConnected) {
                    logLocation();
                    mHandler.postDelayed(this, 2000); // Repeat every 20 seconds
                }
            }
        };
    }

    public void getLocation(View view) {
        mGpsTracker = new GpsTracker(MainActivity.this);
        if (mGpsTracker.canGetLocation()) {
            double latitude = mGpsTracker.getLatitude();
            double longitude = mGpsTracker.getLongitude();
            tvLatitude.setText(String.valueOf(latitude));
            tvLongitude.setText(String.valueOf(longitude));
        } else {
            mGpsTracker.showSettingsAlert();
        }
    }

    private void logLocation() {
        if (mGpsTracker != null && mGpsTracker.canGetLocation()) {
            double latitude = mGpsTracker.getLatitude();
            double longitude = mGpsTracker.getLongitude();
            Log.d("TAG", "Location - Latitude: " + latitude + " Longitude: " + longitude);
            tvLatitude.setText(String.valueOf(latitude));
            tvLongitude.setText(String.valueOf(longitude));
        }
    }

    private final BroadcastReceiver mNetworkStatusReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            boolean isConnected = intent.getBooleanExtra("isConnected", false);
            if (isConnected) {
                if (!isNetworkConnected) {
                    isNetworkConnected = true;
                    mHandler.post(mLocationRunnable);
                }
            } else {
                if (isNetworkConnected) {
                    isNetworkConnected = false;
                    mHandler.removeCallbacks(mLocationRunnable);
                }
            }
        }
    };

    @Override
    protected void onStart() {
        super.onStart();
        IntentFilter intentFilter = new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
        registerReceiver(mNetworkChangeReceiver, intentFilter);
        registerReceiver(mNetworkStatusReceiver, new IntentFilter("NETWORK_STATUS"));
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(mNetworkChangeReceiver);
        unregisterReceiver(mNetworkStatusReceiver);
        mHandler.removeCallbacks(mLocationRunnable);
    }
}
