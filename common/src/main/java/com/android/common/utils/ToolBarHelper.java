package com.android.common.utils;

import android.annotation.SuppressLint;
import android.os.Build;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.common.R;

import androidx.annotation.DrawableRes;
import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.ViewCompat;

/**
 * ToolBar辅助类
 */
public class ToolBarHelper {

    /**
     * 初始化ActionBar
     *
     * @param hideHomeUp    是否隐藏左上角的图标
     */
    public static void initActionBar(@NonNull ActionBar actionBar, boolean hideHomeUp, boolean hideToolbar) {
        actionBar.setDisplayShowTitleEnabled(false);        // 设置是否标题/副标题(默认为true)
        actionBar.setHomeButtonEnabled(!hideHomeUp);        // 设置是否启用左侧的图标是否可以点击(默认为false)
        actionBar.setDisplayShowHomeEnabled(!hideHomeUp);   // 设置是否显示应用程序图标(默认为true)
        actionBar.setDisplayHomeAsUpEnabled(!hideHomeUp);   // 设置是否给左侧添加一个返回的图标(默认为false)
        if (hideToolbar) {
            actionBar.hide();
        } else {
            actionBar.show();
        }
    }

    /**
     * 初始化NavigationIcon
     *
     * @param navigationResId 导航按钮图标资源ID
     */
    public static void initNavigation(@NonNull Toolbar toolbar, int navigationResId) {
        if (navigationResId != 0) {
            toolbar.setNavigationIcon(navigationResId); // 设置导航按钮图标
        } else {
            toolbar.setNavigationIcon(R.drawable.ic_back_material); // 设置默认导航按钮图标
        }
        toolbar.setContentInsetStartWithNavigation(0);
    }

    /**
     * 初始化标题栏(居中显示)
     *
     * @param title 标题
     */
    public static void initTitle(@NonNull Toolbar toolbar, CharSequence title) {
        initTitle(toolbar, title, null);
    }

    /**
     * 初始化标题栏(居中显示)
     *
     * @param title    标题
     * @param subtitle 副标题
     */
    @SuppressLint("InflateParams")
    public static void initTitle(@NonNull Toolbar toolbar, CharSequence title, CharSequence subtitle) {
        if (TextUtils.isEmpty(title) && TextUtils.isEmpty(subtitle)) {
            return;
        }
        View titleContainer = toolbar.findViewById(R.id.title_container);
        if (titleContainer == null) {
            titleContainer = LayoutInflater.from(toolbar.getContext()).inflate(R.layout.layout_toolbar_title, null);
            addToolBarCustomView(toolbar, titleContainer, Gravity.START);
        }
        TextView tvTitle = titleContainer.findViewById(R.id.title);
        TextView tvSubTitle = titleContainer.findViewById(R.id.subtitle);
        tvTitle.setText(title);
        tvSubTitle.setText(subtitle);
        tvSubTitle.setVisibility(TextUtils.isEmpty(subtitle) ? View.GONE : View.VISIBLE);
    }

    /**
     * 设置Toolbar的自定义View
     */
    public static void addToolBarCustomView(@NonNull Toolbar toolbar, View view, int gravity) {
        if (view == null) {
            return;
        }
        Toolbar.LayoutParams params = getToolBarLayoutParams();
        if (gravity <= 0) {
            gravity = Gravity.NO_GRAVITY;
        }
        params.gravity = gravity;
        params.setMargins(10, 10, 10, 10);
        toolbar.addView(view, params);
    }

    /**
     * 设置Toolbar的Logo(居中显示)
     */
    public static void setToolBarLogo(@NonNull Toolbar toolbar, @DrawableRes int logoResId) {
        if (logoResId == 0) {
            return;
        }
        ImageView ivLogo = new ImageView(toolbar.getContext());
        ivLogo.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
        ivLogo.setImageResource(logoResId);
        addToolBarCustomView(toolbar, ivLogo, Gravity.CENTER);
    }

    /**
     * 设置Toolbar平滑过渡(去掉阴影)
     */
    public static void setToolbarSmooth(@NonNull Toolbar toolbar) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            toolbar.setElevation(0);
            toolbar.setTranslationZ(0);
        } else {
            ViewCompat.setElevation(toolbar, 0);
            ViewCompat.setTranslationZ(toolbar, 0);
        }
    }

    /**
     * 返回Toolbar的LayoutParams对象
     */
    @NonNull
    private static Toolbar.LayoutParams getToolBarLayoutParams() {
        return new Toolbar.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
    }

}
