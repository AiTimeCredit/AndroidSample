package com.android.common.utils;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.provider.Settings;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import java.io.File;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import androidx.core.content.FileProvider;

/**
 * 工具类
 */
public class Utility {

    /**
     * dp转px
     */
    public static int dp2px(Context context, float dp) {
        if (context == null) {
            return -1;
        }
        float scale = context.getResources().getDisplayMetrics().density;
        return (int) ((dp * scale) + 0.5f);
    }

    /**
     * 获得设备屏幕宽度(单位：px)
     */
    public static int getScreenWidth(Context context) {
        WindowManager windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics displayMetrics = new DisplayMetrics();
        windowManager.getDefaultDisplay().getMetrics(displayMetrics);
        return displayMetrics.widthPixels;
    }

    /**
     * 在浏览器中打开一个Url
     */
    public static void openWebKit(Context context, String url) {
        try {
            Uri uri = Uri.parse(url);
            Intent intent = new Intent(Intent.ACTION_VIEW, uri);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(intent);
        } catch (NullPointerException e) {
            Toast.makeText(context, "Url为空", Toast.LENGTH_SHORT).show();
        } catch (ActivityNotFoundException e) {
            Toast.makeText(context, "您的设备没有安装浏览器", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * 启动应用设置页面
     */
    public static void startPackageSettings(Context context) {
        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        intent.setData(Uri.parse("package:" + context.getPackageName()));
        intent.addCategory(Intent.CATEGORY_DEFAULT);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }

    /**
     * 获取String的MD5值
     */
    public static String getMd5(String content) throws NoSuchAlgorithmException {
        MessageDigest md5 = MessageDigest.getInstance("MD5");
        byte[] bufs = md5.digest(content.getBytes());
        StringBuilder builder = new StringBuilder(40);
        for (byte buf : bufs) {
            if ((buf & 0XFF) >> 4 == 0) {
                builder.append("0").append(Integer.toHexString(buf & 0xff));
            } else {
                builder.append(Integer.toHexString(buf & 0xff));
            }
        }
        return builder.toString();
    }

    /**
     * 分享图片
     */
    public static void shareImage(Context context, Uri uri) {
        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.setType("image/*");
        shareIntent.putExtra(Intent.EXTRA_STREAM, uri);
        shareIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(Intent.createChooser(shareIntent, "分享到"));
    }

    /**
     * 根据{@link File}对象通过{@link FileProvider}生成对应的{@link Uri}对象
     *
     * @param context 上下文
     * @param file    File对象
     */
    public static Uri getUriForFileProvider(Context context, File file) {
        String authority = String.format("%s.fileprovider", context.getPackageName());
        return FileProvider.getUriForFile(context, authority, file);
    }

    /**
     * 关闭软键盘
     */
    public static boolean hideSoftInput(View view) {
        InputMethodManager inputMethodManager = (InputMethodManager) view.getContext().getApplicationContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        if (inputMethodManager != null) {
            return inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
        return false;
    }

    /**
     * 关闭软键盘
     */
    public static boolean hideSoftInput(Activity activity) {
        View currentFocus = activity.getCurrentFocus();
        if (currentFocus == null) {
            currentFocus = activity.getWindow().getDecorView();
        }
        InputMethodManager inputMethodManager = (InputMethodManager) activity.getApplicationContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        if (inputMethodManager != null) {
            return inputMethodManager.hideSoftInputFromWindow(currentFocus.getWindowToken(), 0);
        }
        return false;
    }

}
