package com.example.asian.broadcast;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkCapabilities;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Handler;
import android.widget.Toast;

import com.example.asian.GpsTracker;

public class BroadcastInternetConnectorReceiver extends BroadcastReceiver {
    private Context mContext;
    private GpsTracker gpsTracker;

    public BroadcastInternetConnectorReceiver(Context context) {
        this.mContext = context;
        gpsTracker = new GpsTracker(mContext);
    }

    final Handler handler = new Handler();
    final Runnable runnable = new Runnable() {
        public void run() {
            gpsTracker.getLocation();
            System.out.println(gpsTracker.getLatitude());
            System.out.println(gpsTracker.getLongitude());
            runLog();
        }
    };

    public void runLog() {
        if (isNetworkAvailable(mContext) && gpsTracker.getIsGPSEnable()) {
            handler.postDelayed(runnable, 2000);
        }
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        if (ConnectivityManager.CONNECTIVITY_ACTION.equals(intent.getAction())) {
            if (isNetworkAvailable(context)) {
                if (gpsTracker.getIsGPSEnable()) {
                    if (gpsTracker.canGetLocation()) {
                        runLog();
                    } else {
                        Toast.makeText(context, "Permission Denied", Toast.LENGTH_LONG).show();
                    }
                } else {
                    Toast.makeText(context, "GPS disable", Toast.LENGTH_LONG).show();
                }
            } else {
                Toast.makeText(context, "Internet disconnected", Toast.LENGTH_LONG).show();
            }
        }
    }

    private boolean isNetworkAvailable(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(context.CONNECTIVITY_SERVICE);
        if (connectivityManager == null) {
            return false;
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            Network network = connectivityManager.getActiveNetwork();

            if (network == null) {
                return false;
            } else {
                NetworkCapabilities networkCapabilities = connectivityManager.getNetworkCapabilities(network);
                return networkCapabilities != null && networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI);
            }
        } else {
            NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
            return networkInfo != null && networkInfo.isConnected();
        }
    }
}
