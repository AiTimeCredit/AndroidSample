package com.android.common.mvvm;

import android.content.Intent;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.StringRes;

/**
 * ViewModel抽象接口：负责处理数据和业务逻辑
 * <ul>
 * <li>只做业务逻辑操作，不持有任何UI控件的引用
 * <li>持有Model层提供的数据接口对象，可通过依赖注入解耦此部分
 * <li>一个ViewModel可以复用到多个View中，同样的一份数据，可以提供给不同的UI去做展示
 * </ul>
 */
public interface IViewModel<Model extends IModel> {

    /**
     * 返回Model对象
     */
    Model getModel();

    /**
     * 根据字符串资源ID返回对应的字符串
     */
    @NonNull
    String getString(@StringRes int resId);

    /**
     * {@link android.app.Activity#onActivityResult(int, int, Intent)}
     */
    void onActivityResult(int requestCode, int resultCode, @Nullable Intent data);

}
