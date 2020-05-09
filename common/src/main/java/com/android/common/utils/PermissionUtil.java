package com.android.common.utils;

import android.app.AppOpsManager;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;
import android.text.TextUtils;

import java.util.Arrays;
import java.util.Collection;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;

/**
 * 权限请求工具类
 */
public class PermissionUtil {

    /**
     * 判断当前系统版本是否为Android 6.0及以上系统
     */
    public static boolean isOverMarshmallow() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.M;
    }

    /**
     * 检查应用是否拥有该权限(被授权返回{@link PackageManager#PERMISSION_GRANTED}，否则返回{@link PackageManager#PERMISSION_DENIED})
     * <br>{@link Build.VERSION_CODES#M}之前的版本调用该方法将直接回调{@link android.app.Activity#onRequestPermissionsResult(int, String[], int[])}
     *
     * @see ContextCompat#checkSelfPermission
     */
    public static int checkSelfPermission(@NonNull Context context, @NonNull String permission) {
        return ContextCompat.checkSelfPermission(context, permission);
    }
    /**
     * 检查指定的权限集中的权限是否都被授予
     *
     * @param context     上下文对象
     * @param permissions 指定的权限集合
     * @return 指定的权限集中的权限是否都被授予
     */
    public static boolean hasPermissions(Context context, String... permissions) {
        return hasPermissions(context, Arrays.asList(permissions));
    }

    /**
     * 检查指定的权限集中的权限是否都被授予
     *
     * @param context     上下文对象
     * @param permissions 指定的权限集合
     * @return 指定的权限集中的权限是否都被授予
     */
    public static boolean hasPermissions(Context context, Collection<String> permissions) {
        if (permissions != null && !permissions.isEmpty()) {
            for (String permission : permissions) {
                if (!hasPermission(context, permission)) {
                    return false;
                }
            }
        }
        return true;
    }

    /**
     * 检查指定的权限是否被授予
     *
     * @param context    上下文对象
     * @param permission 指定的权限
     * @return 检查指定的权限是否被授予
     */
    public static boolean hasPermission(Context context, String permission) {
        if (!isOverMarshmallow()) {
            return true;
        }
        int permissionState = checkSelfPermission(context, permission);
        if (permissionState != PackageManager.PERMISSION_GRANTED) {
            return false;
        }
        String op = AppOpsManager.permissionToOp(permission);
        AppOpsManager appOpsManager = context.getSystemService(AppOpsManager.class);
        if (appOpsManager == null || TextUtils.isEmpty(op)) {
            return true;
        }
        int result = appOpsManager.noteProxyOp(op, context.getPackageName());
        return result == AppOpsManager.MODE_ALLOWED;
    }

}
