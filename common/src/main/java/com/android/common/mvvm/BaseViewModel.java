package com.android.common.mvvm;

import android.app.Application;
import android.content.Intent;

import com.android.common.utils.ReflexUtil;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.StringRes;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.DefaultLifecycleObserver;

/**
 * ViewModel基类
 */
public abstract class BaseViewModel<Model extends IModel> extends AndroidViewModel implements IViewModel<Model>, DefaultLifecycleObserver {

    private Model mModel;

    public BaseViewModel(@NonNull Application application) {
        super(application);
    }

    @Override
    public Model getModel() {
        if (mModel == null) {
            mModel = ReflexUtil.getTypeInstance(this, IModel.class);
        }
        return mModel;
    }

    @NonNull
    @Override
    public String getString(@StringRes int resId) {
        return getApplication().getString(resId);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {

    }

}
