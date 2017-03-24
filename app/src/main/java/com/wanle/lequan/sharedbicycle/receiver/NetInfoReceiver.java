package com.wanle.lequan.sharedbicycle.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

import com.wanle.lequan.sharedbicycle.event.MyEvent;

import org.greenrobot.eventbus.EventBus;

/**
 * autor:Jerry
 * fuction:
 * Date: 2017/3/24.
 */

public class NetInfoReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        Log.i("netinfo","netinfo执行了....");
        NetworkInfo.State wifiState = null;
        NetworkInfo.State mobileState = null;
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        wifiState = cm.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState();
        mobileState = cm.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState();
        if (wifiState != null && mobileState != null
                && NetworkInfo.State.CONNECTED != wifiState
                && NetworkInfo.State.CONNECTED == mobileState) {
            // 手机网络连接成功
            EventBus.getDefault().post(new MyEvent("网络连接成功"));
        } else if (wifiState != null && mobileState != null
                && NetworkInfo.State.CONNECTED != wifiState
                && NetworkInfo.State.CONNECTED != mobileState) {
            // 手机没有任何的网络
        } else if (wifiState != null && NetworkInfo.State.CONNECTED == wifiState) {
            // 无线网络连接成功
            EventBus.getDefault().post(new MyEvent("网络连接成功"));
        }
    }
}
