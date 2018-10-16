package com.hwl.beta.ui;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.widget.Toast;

import com.hwl.beta.ui.ebus.EventBusUtil;
import com.hwl.beta.ui.immsg.IMClientEntry;

public class NetworkBroadcastReceiver extends BroadcastReceiver {

    public static final String ACTION_TAG = "android.net.conn.CONNECTIVITY_CHANGE";

    @Override
    public void onReceive(Context context, Intent intent) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService
                (Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isAvailable()) {
//            Toast.makeText(context, "当前网络可用", Toast.LENGTH_SHORT).show();
            EventBusUtil.sendNetworkConnectEvent();
            IMClientEntry.connectServer();
        } else {
//            Toast.makeText(context, "当前网络不可用", Toast.LENGTH_SHORT).show();
            EventBusUtil.sendNetworkBreakEvent();
            IMClientEntry.stopHeartbeat();
        }
    }


}
