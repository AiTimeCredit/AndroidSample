package com.android.common.receiver;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.telephony.TelephonyManager;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

/**
 * 网络状态广播接收器
 * <ul>
 * <strong>注意：</strong>
 * <li>需要权限{@link Manifest.permission#ACCESS_WIFI_STATE}
 * <li>需要权限{@link Manifest.permission#ACCESS_NETWORK_STATE}
 * </ul>
 */
public class NetStateChangedReceiver extends BroadcastReceiver {

    private static final String TAG = "NetStateChangedReceiver";
    private volatile static BroadcastReceiver sBroadcastReceiver;
    private static List<NetStateChangedObserver> sNetChangeObservers = new ArrayList<>();

    public enum NetworkType {

        NONE("NONE"),
        UNKNOWN("UNKNOWN"),
        WIFI("WIFI"),
        MOBILE_2G("2G"),
        MOBILE_3G("3G"),
        MOBILE_4G("4G");

        private String name;

        NetworkType(String name) {
            this.name = name;
        }

        public String getName() {
            return name;
        }
    }

    public interface NetStateChangedObserver {

        void onNetStateChanged(boolean isAvailable, NetworkType type);

    }

    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent == null) {
            return;
        }
        String action = intent.getAction();
        if (ConnectivityManager.CONNECTIVITY_ACTION.equals(action)) {
            ConnectivityManager manager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo networkInfo = manager.getActiveNetworkInfo();
            NetworkType networkType = NetworkType.NONE;
            boolean isNetworkAvailable = false;
            if (networkInfo != null && networkInfo.isAvailable()) {
                isNetworkAvailable = true;
                networkType = getNetType(networkInfo);
            }
            notifyObserver(isNetworkAvailable, networkType);
        }
    }

    private NetworkType getNetType(NetworkInfo networkInfo) {
        NetworkType networkType = NetworkType.UNKNOWN;
        if (networkInfo.getType() == ConnectivityManager.TYPE_WIFI) {
            networkType = NetworkType.WIFI;
        } else if (networkInfo.getType() == ConnectivityManager.TYPE_MOBILE) {
            String subTypeName = networkInfo.getSubtypeName();
            int subType = networkInfo.getSubtype();
            switch (subType) {
                case TelephonyManager.NETWORK_TYPE_GPRS: // 联通2G
                case TelephonyManager.NETWORK_TYPE_EDGE: // 移动2G
                case TelephonyManager.NETWORK_TYPE_CDMA: // 电信2G
                case TelephonyManager.NETWORK_TYPE_1xRTT:// 电信2G
                case TelephonyManager.NETWORK_TYPE_IDEN: // api<8 : replace by 11
                    networkType = NetworkType.MOBILE_2G;
                    break;
                case TelephonyManager.NETWORK_TYPE_EVDO_0:// 电信3G
                case TelephonyManager.NETWORK_TYPE_EVDO_A:// 电信3G
                case TelephonyManager.NETWORK_TYPE_EVDO_B: // 电信3G
                case TelephonyManager.NETWORK_TYPE_UMTS: // 联通3G
                case TelephonyManager.NETWORK_TYPE_HSDPA: // 联通3G
                case TelephonyManager.NETWORK_TYPE_HSUPA:
                case TelephonyManager.NETWORK_TYPE_HSPA:
                case TelephonyManager.NETWORK_TYPE_EHRPD: // api<11 : replace by 12
                case TelephonyManager.NETWORK_TYPE_HSPAP: // api<13 : replace by 15
                    networkType = NetworkType.MOBILE_3G;
                    break;
                case TelephonyManager.NETWORK_TYPE_LTE:  //3G到4G的一个过渡，称为准4G
                    networkType = NetworkType.MOBILE_4G;
                    break;
                default:
                    //中国移动 联通 电信 三种3G制式
                    if (subTypeName.equalsIgnoreCase("TD-SCDMA") || subTypeName.equalsIgnoreCase("WCDMA") || subTypeName.equalsIgnoreCase("CDMA2000")) {
                        networkType = NetworkType.MOBILE_3G;
                    } else {
                        networkType = NetworkType.UNKNOWN;
                    }
                    break;
            }
        }
        return networkType;
    }

    private void notifyObserver(boolean isNetworkAvailable, NetworkType networkType) {
        if (!sNetChangeObservers.isEmpty()) {
            for (NetStateChangedObserver observer : sNetChangeObservers) {
                observer.onNetStateChanged(isNetworkAvailable, networkType);
                Log.i(TAG, isNetworkAvailable ? "Network connected!" : "Network disconnected!");
            }
        }
    }

    private static BroadcastReceiver getReceiver() {
        if (null == sBroadcastReceiver) {
            synchronized (NetStateChangedReceiver.class) {
                if (null == sBroadcastReceiver) {
                    sBroadcastReceiver = new NetStateChangedReceiver();
                }
            }
        }
        return sBroadcastReceiver;
    }

    public static void registerNetworkStateReceiver(Context context) {
        IntentFilter filter = new IntentFilter();
        filter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
        context.registerReceiver(getReceiver(), filter);
    }

    public static void unregisterNetworkStateReceiver(Context context) {
        if (sBroadcastReceiver != null) {
            context.unregisterReceiver(sBroadcastReceiver);
        }
    }

    public static void registerNetStateChangedObserver(NetStateChangedObserver observer) {
        if (sNetChangeObservers == null) {
            sNetChangeObservers = new ArrayList<>();
        }
        if (!sNetChangeObservers.contains(observer)) {
            sNetChangeObservers.add(observer);
        }
    }

    public static void removeNetStateChangedObserver(NetStateChangedObserver observer) {
        if (sNetChangeObservers != null) {
            sNetChangeObservers.remove(observer);
        }
    }

}
