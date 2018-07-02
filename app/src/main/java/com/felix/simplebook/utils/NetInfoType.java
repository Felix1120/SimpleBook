package com.felix.simplebook.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiManager;

/**
 * Created by Felix on 2018/7/1.
 */

public class NetInfoType {

    private Context context;

    public NetInfoType(Context context) {
        this.context = context;
    }

    /**
     * 判断移动网络是否开启
     *
     * @param context
     * @return
     */
    public boolean isNetEnabled(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivityManager == null) {
        } else {
            NetworkInfo[] networkInfo = connectivityManager.getAllNetworkInfo();
            if (networkInfo != null && networkInfo.length > 0) {
                for (int i = 0; i < networkInfo.length; i++) {
                    if (networkInfo[i].getState() == NetworkInfo.State.CONNECTED) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    /**
     * 判断WIFI网络是否开启
     *
     * @param context
     * @return
     */
    public boolean isWifiEnabled(Context context) {
        WifiManager wm = (WifiManager) context
                .getSystemService(Context.WIFI_SERVICE);
        if (wm != null && wm.isWifiEnabled()) {
            return true;
        }
        return false;
    }

    /**
     * 判断移动网络是否连接成功
     *
     * @param context
     * @return
     */
    public boolean isNetConnected(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo info = cm.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
        if (cm != null && info != null && info.isConnected()) {
            return true;
        }
        return false;
    }

    /**
     * 判断WIFI是否连接成功
     *
     * @param context
     * @return
     */
    public boolean isWifiConnected(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo info = cm.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        if (info != null && info.isConnected()) {
            return true;
        }
        return false;
    }

    /**
     * 判断WIFI 或 移动网络是否连接成功
     *
     * @return
     */
    public boolean isNetConnected() {
        if (isNetConnected(context) || isWifiConnected(context)) {
            return true;
        }
        return false;
    }
}
