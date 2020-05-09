package com.android.common.mvvm;

import com.android.common.BaseApplication;
import com.android.common.http.RetrofitManage;

import java.util.HashMap;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

/**
 * Model基类
 */
public class BaseModel implements IModel {

    @Override
    public <T> T createService(Class<T> service) {
        return RetrofitManage.getLoginRetrofit().create(service);
    }

    @Nullable
    @Override
    public String getString(int resId) {
        return BaseApplication.getAppContext().getString(resId);
    }

    @NonNull
    protected HashMap<String, String> getParams() {
        return new HashMap<>();
    }

    @NonNull
    protected String getNotNullString(String value) {
        return value == null ? "" : value;
    }

}
