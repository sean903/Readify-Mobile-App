package com.mobileapllication;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.NetworkInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.widget.Toast;

//Message shows if wifi connects or disconnects
public class checkWifi  extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {

        NetworkInfo info = intent.getParcelableExtra(WifiManager.EXTRA_NETWORK_INFO);
        if(info != null && info.isConnected()) {
            Toast.makeText(context.getApplicationContext(), "You are connected to the internet. You can now see your messages and search books!", Toast.LENGTH_SHORT).show();

        }else{
            Toast.makeText(context.getApplicationContext(), "You are not connected to the internet. " +
                    " Please connect to see your messages and search books.", Toast.LENGTH_LONG).show();
            }



    }
}
