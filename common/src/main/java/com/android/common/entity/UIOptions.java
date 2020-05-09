package com.android.common.entity;

import com.android.common.R;

/**
 * UI选项配置信息
 */
public class UIOptions {

    private boolean hideHomeUp;             // 是否隐藏返回按钮
    private boolean hideToolbar;            // 是否隐藏Toolbar
    private boolean keyboardEnable;         // 是否解决软键盘与输入框冲突问题
    private boolean statusBarDarkFont;      // 是否设置状态栏深色字体
    private boolean fitsSystemWindows;      // 是否解决布局与状态栏重叠问题
    private int statusBarColor;             // 状态栏颜色资源ID
    private int navigationIcon;             // 导航图标资源ID

    public UIOptions() {
        hideHomeUp = false;
        hideToolbar = false;
        keyboardEnable = false;
        statusBarDarkFont = false;
        fitsSystemWindows = true;
        statusBarColor = R.color.colorPrimaryDark;
    }

    /****************************** Getter ******************************/

    public boolean isHideHomeUp() {
        return hideHomeUp;
    }

    public boolean isHideToolbar() {
        return hideToolbar;
    }

    public boolean isKeyboardEnable() {
        return keyboardEnable;
    }

    public boolean isStatusBarDarkFont() {
        return statusBarDarkFont;
    }

    public boolean isFitsSystemWindows() {
        return fitsSystemWindows;
    }

    public int getStatusBarColor() {
        return statusBarColor;
    }

    public int getNavigationIcon() {
        return navigationIcon;
    }

    /****************************** Setter ******************************/

    public UIOptions setHideHomeUp(boolean hideHomeUp) {
        this.hideHomeUp = hideHomeUp;
        return this;
    }

    public UIOptions setHideToolbar(boolean hideToolbar) {
        this.hideToolbar = hideToolbar;
        return this;
    }

    public UIOptions setStatusBarDarkFont(boolean statusBarDarkFont) {
        this.statusBarDarkFont = statusBarDarkFont;
        return this;
    }

    public UIOptions setKeyboardEnable(boolean keyboardEnable) {
        this.keyboardEnable = keyboardEnable;
        return this;
    }

    public UIOptions setFitsSystemWindows(boolean fitsSystemWindows) {
        this.fitsSystemWindows = fitsSystemWindows;
        return this;
    }

    public UIOptions setStatusBarColor(int statusBarColor) {
        this.statusBarColor = statusBarColor;
        return this;
    }

    public UIOptions setNavigationIcon(int navigationIcon) {
        this.navigationIcon = navigationIcon;
        return this;
    }

}
