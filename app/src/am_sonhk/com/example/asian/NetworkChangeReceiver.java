package com.example.asian;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;
import android.widget.Toast;

public class NetworkChangeReceiver extends BroadcastReceiver {

    private static final String LOG_TAG = "NetworkChangeReceiver";
    private boolean isConnected = false;

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.v(LOG_TAG, "Received notification about network status");
        isNetworkAvailable(context);
    }

    private void isNetworkAvailable(Context context) {
        ConnectivityManager connectivity = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivity != null) {
            NetworkInfo[] info = connectivity.getAllNetworkInfo();
            if (info != null) {
                for (NetworkInfo networkInfo : info) {
                    if (networkInfo.getState() == NetworkInfo.State.CONNECTED) {
                        if (!isConnected) {
                            Log.v(LOG_TAG, "Now you are connected to the Internet!");
                            Toast.makeText(context, "Internet availablle via Broadcast receiver", Toast.LENGTH_SHORT).show();
                            Intent networkStatusIntent = new Intent("NETWORK_STATUS");
                            networkStatusIntent.putExtra("isConnected", true);
                            context.sendBroadcast(networkStatusIntent);
                            isConnected = true;
                        }
                        return;
                    }
                }
            }
        }
        Toast.makeText(context, "Internet NOT availablle via Broadcast receiver", Toast.LENGTH_SHORT).show();
        Log.v(LOG_TAG, "You are not connected to the Internet!");
        if (isConnected) {
            Intent networkStatusIntent = new Intent("NETWORK_STATUS");
            networkStatusIntent.putExtra("isConnected", false);
            context.sendBroadcast(networkStatusIntent);
            isConnected = false;
        }
    }
}
