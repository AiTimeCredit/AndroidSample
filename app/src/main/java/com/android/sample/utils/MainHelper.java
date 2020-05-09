package com.android.sample.utils;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.aitime.android.deviceid.DeviceIdentifier;
import com.android.common.BaseApplication;
import com.android.sample.MainActivity;
import com.henley.logger.Logger;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

/**
 * 主界面辅助类
 */
public class MainHelper {

    public static void start() {
        Context context = BaseApplication.getAppContext();
        Logger.i("TAG", "设备唯一标识 = " + DeviceIdentifier.getUniqueIdentifier(context));
        // 执行启动任务(APP启动)
    }

    /**
     * 跳转到主界面
     */
    public static void gotoMainActivity(@NonNull Context context) {
        gotoMainActivity(context, null);
    }

    /**
     * 跳转到主界面
     */
    public static void gotoMainActivity(@NonNull Context context, @Nullable Bundle extras) {
        try {
            Intent intent = new Intent(context, MainActivity.class);
            intent.setPackage(context.getPackageName());
            if (extras != null && !extras.isEmpty()) {
                intent.putExtras(extras);
            }
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            context.startActivity(intent);
        } catch (ActivityNotFoundException e) {
            e.printStackTrace();
        }
    }

    /**
     * 退出APP
     */
    public static void exitApp() {
    }

}