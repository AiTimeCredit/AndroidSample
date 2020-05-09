package com.android.common.base.listener;

import android.view.View;

import com.android.common.entity.UIOptions;
import com.android.common.mvvm.BaseViewModel;

import androidx.annotation.NonNull;
import androidx.databinding.ViewDataBinding;

/**
 * UI公共抽象接口
 */
public interface ICommonUI<DataBinding extends ViewDataBinding, ViewModel extends BaseViewModel> {

    /**
     * 返回UIOptions
     */
    @NonNull
    UIOptions getUIOptions();

    /**
     * 返回根布局View
     */
    @NonNull
    View getRootView();

    /**
     * 返回DataBinding
     */
    DataBinding getDataBinding();

    /**
     * 返回ViewModel
     */
    ViewModel getViewModel();

    /**
     * 返回页面布局文件资源ID
     */
    int getContentLayoutId();

    /**
     * 返回ViewModel的变量ID
     */
    int getViewModelVariableId();

    /**
     * 初始化UIOptions
     */
    void initUIOptions(@NonNull UIOptions options);

    /**
     * 初始化View
     */
    void initViews(@NonNull View rootView);

    /**
     * 加载数据
     */
    void loadData();

    /**
     * 生成ViewModel
     */
    ViewModel obtainViewModel();

}
