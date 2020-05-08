package com.android.common.mvp;

import android.content.Context;

import androidx.annotation.NonNull;

/**
 * 负责绘制UI元素、与用户进行交互(在Android中体现为Activity/Fragment)
 * <ul>
 * <li>UI层，包含所有UI相关组件
 * <li>持有对应的Presenter的对象，可通过依赖注入解耦此部分
 * <li>由Presenter来负责更新UI
 * </ul>
 */
public interface IView {

    @NonNull
    Context getContext();

}
