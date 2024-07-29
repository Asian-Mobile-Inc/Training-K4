package com.example.asian.issueseven.broadcast;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.os.Build;
import android.util.Log;
import android.widget.Toast;

import com.example.asian.issueseven.service.MyLocationService;

import java.util.Objects;

public class BoadcastInternet extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        if (Objects.equals(intent.getAction(), ConnectivityManager.CONNECTIVITY_ACTION)) {
            checkInternet(context);
        }
    }

    private void checkInternet(Context context) {
        final ConnectivityManager connMgr = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        final android.net.NetworkInfo wifi = connMgr
                .getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        final android.net.NetworkInfo mobile = connMgr
                .getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
        if (wifi != null && mobile != null) {
            if (wifi.isConnectedOrConnecting() || mobile.isConnectedOrConnecting()) {
                MyLocationService.getInstance().setIsInternetChange(context, true);
            } else {
                MyLocationService.getInstance().setIsInternetChange(context, false);
            }
        }
    }
}
