package com.example.asian.issueseven.broadcast;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;

import java.util.Objects;

public class BroadcastInternet extends BroadcastReceiver {
    public static final String KEY_INTERNET_CHANGE = "isInternetChange";
    public static final String ACTION_INTERNET_CHANGE = "com.example.asian.issueseven.broadcast.BroadcastInternet";

    @Override
    public void onReceive(Context context, Intent intent) {
        if (Objects.equals(intent.getAction(), ConnectivityManager.CONNECTIVITY_ACTION)) {
            checkInternet(context);
        }
    }
    private void putData(Context context, boolean isInternetAvailable) {
        Intent intent = new Intent();
        intent.setAction(ACTION_INTERNET_CHANGE);
        intent.putExtra(KEY_INTERNET_CHANGE, isInternetAvailable);
        context.sendBroadcast(intent);
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
                putData(context, true);
            } else {
                putData(context, false);
            }
        }
    }
}
