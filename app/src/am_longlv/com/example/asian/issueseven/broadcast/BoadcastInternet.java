package com.example.asian.issueseven.broadcast;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.util.Log;
import android.widget.Toast;

import com.example.asian.issueseven.service.MyLocationService;

public class BoadcastInternet extends BroadcastReceiver {
    private static final String INTERNET_DISCONNECTED = "Internet disconnected";
    private static final String INTERNET_CONNECTED = "Internet connected";

    @Override
    public void onReceive(Context context, Intent intent) {
        final ConnectivityManager connMgr = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);

        final android.net.NetworkInfo wifi = connMgr
                .getNetworkInfo(ConnectivityManager.TYPE_WIFI);

        final android.net.NetworkInfo mobile = connMgr
                .getNetworkInfo(ConnectivityManager.TYPE_MOBILE);

        if (wifi.isConnected() || mobile.isConnected()) {
            Toast.makeText(context, INTERNET_CONNECTED, Toast.LENGTH_SHORT).show();
            MyLocationService myLocationService = new MyLocationService();
            myLocationService.setIsInternetChange(context, true);
        } else {
            Toast.makeText(context, INTERNET_DISCONNECTED, Toast.LENGTH_SHORT).show();
            MyLocationService myLocationService = new MyLocationService();
            myLocationService.setIsInternetChange(context,false);
        }
    }
}
