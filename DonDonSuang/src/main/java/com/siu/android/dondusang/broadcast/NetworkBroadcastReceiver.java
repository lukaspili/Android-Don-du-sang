package com.siu.android.dondusang.broadcast;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/**
 * Created by lukas on 8/11/13.
 */
public class NetworkBroadcastReceiver extends BroadcastReceiver {

    private boolean mConnected;
    private ConnectivityManager mConnectivityManager;
    private NetworkBroadcastReceiverCallback mCallback;

    public NetworkBroadcastReceiver(Activity activity, NetworkBroadcastReceiverCallback callback) {
        mConnectivityManager = (ConnectivityManager) activity.getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = mConnectivityManager.getActiveNetworkInfo();
        mConnected = activeNetwork != null && activeNetwork.isConnectedOrConnecting();

        mCallback = callback;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        NetworkInfo activeNetwork = mConnectivityManager.getActiveNetworkInfo();
        boolean connected = activeNetwork != null && activeNetwork.isConnectedOrConnecting();

        if (mConnected != connected) {
            mCallback.onNetworkConnectivityChange(connected);
            mConnected = connected;
        }
    }
}
