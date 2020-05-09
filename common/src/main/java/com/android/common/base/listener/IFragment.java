package com.android.common.base.listener;

import android.os.Bundle;
import android.view.View;

import com.android.common.mvvm.BaseViewModel;

import androidx.annotation.NonNull;
import androidx.databinding.ViewDataBinding;

/**
 * Fragment抽象接口
 *
 * @author liyunlong
 * @date 2019/3/25 18:48
 */
public interface IFragment<DataBinding extends ViewDataBinding, ViewModel extends BaseViewModel> extends ICommonUI<DataBinding, ViewModel> {

    /**
     * 判断当前Fragment的根布局是否初始化
     */
    boolean isViewCreated();

    /**
     * 处理Bundle(主要用来获取其中携带的参数)
     */
    void handleBundle(@NonNull Bundle bundle);

    /**
     * 初始化titleBar
     */
    void initTitleBar(@NonNull View rootView);

}
