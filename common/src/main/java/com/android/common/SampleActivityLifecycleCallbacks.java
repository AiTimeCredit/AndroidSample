package com.android.common;

import android.app.Activity;
import android.app.Application;
import android.os.Bundle;

import com.henley.logger.Logger;

import java.util.TreeSet;

import androidx.annotation.NonNull;

/**
 * {@link Activity}生命周期回调
 */
public class SampleActivityLifecycleCallbacks implements Application.ActivityLifecycleCallbacks {

    private static final String TAG = "ActivityLifecycleCallbacks";
    /**
     * 回到后台的时间戳
     */
    private long backgroundStamp = 0;
    /**
     * 正在运行的{@link Activity}的数量
     */
    private int activityCount = 0;
    /**
     * 标记是否首次从后台回到前台
     */
    private boolean isFirstFromBackground = true;
    /**
     * 已经启动的{@link Activity}的名称集合
     */
    private TreeSet<String> treeSet = new TreeSet<>();

    @Override
    public void onActivityCreated(Activity activity, Bundle savedInstanceState) {
        Logger.i(TAG, activity.getClass().getName() + " Created!");
        treeSet.add(activity.getClass().getName());
    }

    @Override
    public void onActivityStarted(Activity activity) {
        Logger.i(TAG, activity.getClass().getName() + " Started!");
        if (activityCount == 0) {
            Logger.i(TAG, "App切到前台...");
            if (backgroundStamp > 0) {
                long timeInterval = System.currentTimeMillis() - backgroundStamp;
                Logger.i(TAG, "App在后台停留时间为：" + timeInterval + " ms");
            }
            executeTaskFromBackground();
        }
        activityCount++;
    }

    @Override
    public void onActivityResumed(Activity activity) {
        Logger.i(TAG, activity.getClass().getName() + " Resumed!");
    }

    @Override
    public void onActivityPaused(Activity activity) {
        Logger.i(TAG, activity.getClass().getName() + " Paused!");
    }

    @Override
    public void onActivityStopped(Activity activity) {
        Logger.i(TAG, activity.getClass().getName() + " Stopped!");
        activityCount--;
        if (activityCount == 0) {
            Logger.i(TAG, "App切到后台...");
            backgroundStamp = System.currentTimeMillis();
        }
    }

    @Override
    public void onActivitySaveInstanceState(@NonNull Activity activity, @NonNull Bundle outState) {

    }

    @Override
    public void onActivityDestroyed(Activity activity) {
        Logger.i(TAG, activity.getClass().getName() + " Destroyed!");
        treeSet.remove(activity.getClass().getName());
        if (treeSet.isEmpty()) {
            isFirstFromBackground = true;
        }
    }

    /**
     * 获取正在运行的{@link Activity}的数量
     */
    public int getActivityCount() {
        return activityCount;
    }

    /**
     * 判断指定名称的{@link Activity}是否启动
     */
    public boolean checkActivityExists(String className) {
        return treeSet.contains(className);
    }

    /**
     * 执行从后台切到前台的任务
     */
    private void executeTaskFromBackground() {
        if (isFirstFromBackground) {
            isFirstFromBackground = false;
            return;
        }
        Logger.i(TAG, "执行从后台切到前台的任务...");
    }

}
