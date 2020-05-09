package com.android.common.utils;

import android.Manifest;

import java.util.ArrayList;
import java.util.List;

/**
 * 常量类
 */
public class Constants {

    /**
     * 权限集合
     */
    public static final List<String> PERMISSIONS = new ArrayList<String>() {
        {
            add(Manifest.permission.READ_PHONE_STATE);           // 访问手机状态
            add(Manifest.permission.CAMERA);                     // 访问相机
            add(Manifest.permission.READ_CONTACTS);              // 访问联系人
            add(Manifest.permission.ACCESS_WIFI_STATE);          // 访问WiFi状态
            add(Manifest.permission.ACCESS_NETWORK_STATE);       // 访问网络状态
            add(Manifest.permission.READ_EXTERNAL_STORAGE);      // 访问外部存储
            add(Manifest.permission.WRITE_EXTERNAL_STORAGE);     // 访问外部存储
            add(Manifest.permission.ACCESS_FINE_LOCATION);       // 访问精确定位
            add(Manifest.permission.ACCESS_COARSE_LOCATION);     // 访问基础定位
//            add(Manifest.permission.READ_SMS);                   // 读取短信
//            add(Manifest.permission.RECEIVE_SMS);                // 接受短信
//            add(Manifest.permission.READ_CALL_LOG);             // 访问通话记录
        }
    };

}
