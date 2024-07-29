package com.example.asian.issueseven.broadcast;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;

import java.util.Objects;

public class BroadcastInternet extends BroadcastReceiver {
    private OnInternetChange mOnInternetChange;

    public interface OnInternetChange {
        void onInternetChange(boolean isInternetAvailable);
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        mOnInternetChange = (OnInternetChange) context;
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
                mOnInternetChange.onInternetChange(true);
            } else {
                mOnInternetChange.onInternetChange(false);
            }
        }
    }
}
