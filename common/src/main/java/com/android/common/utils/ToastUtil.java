package com.android.common.utils;

import android.content.Context;
import android.text.TextUtils;
import android.widget.Toast;

import com.android.common.BaseApplication;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.StringRes;

/**
 * Toast工具类
 */
public class ToastUtil {

    public static void showToast(@StringRes int resId) {
        showToast(getContext().getString(resId));
    }

    public static void showToast(@Nullable CharSequence text) {
        if (text == null || TextUtils.isEmpty(text)) {
            return;
        }
        Toast.makeText(getContext(), text, Toast.LENGTH_SHORT).show();
    }

    @NonNull
    private static Context getContext() {
        return BaseApplication.getAppContext();
    }

}
