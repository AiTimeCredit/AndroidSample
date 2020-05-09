package com.android.common.base.listener;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;

import com.android.common.base.BaseActivity;
import com.android.common.mvvm.BaseViewModel;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;
import androidx.databinding.ViewDataBinding;

/**
 * Activity抽象接口
 *
 * @author liyunlong
 * @date 2019/3/25 18:42
 */
public interface IActivity<DataBinding extends ViewDataBinding, ViewModel extends BaseViewModel> extends ICommonUI<DataBinding, ViewModel> {

    /**
     * 返回上下文
     */
    @NonNull
    BaseActivity getContext();

    /**
     * 返回Menu对象
     */
    Menu getMenu();

    /**
     * 返回Toolbar对象
     */
    Toolbar getToolBar();

    /**
     * 设置标题
     */
    CharSequence title();

    /**
     * 判断{@link android.app.Activity}是否可见
     */
    boolean isActivityResumed();

    /**
     * 判断{@link android.app.Activity}的窗口是否已关联到窗口管理器
     */
    boolean isWindowAttached();

    /**
     * 初始化(在{@code super.onCreate(savedInstanceState)}之前调用)
     */
    void beforeSuper();

    /**
     * 初始化
     */
    void init(Bundle savedInstanceState);

    /**
     * 初始化ImmersionBar
     */
    void initImmersionBar();

    /**
     * 处理Intent(主要用来获取其中携带的参数，不要在此方法中操作UI)
     */
    void handleIntent(@NonNull Intent intent);

    /**
     * 加载菜单布局
     */
    int getMenuRes();

    /**
     * 初始化Toolbar
     */
    void initToolbar();

}
