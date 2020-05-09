package com.android.common;

import android.app.Application;
import android.content.Context;
import android.content.res.Configuration;
import android.os.Build;
import android.os.Process;
import android.text.TextUtils;
import android.webkit.WebView;

import com.android.common.utils.Utility;
import com.henley.logger.Logger;
import com.henley.logger.printer.LogcatPrinter;

import androidx.appcompat.app.AppCompatDelegate;
import androidx.multidex.MultiDex;
import io.reactivex.functions.Consumer;
import io.reactivex.plugins.RxJavaPlugins;

/**
 * Application基类
 * <ul>
 * <strong>注意：</strong>
 * <li>重写{@link Application}的生命周期方法时必须调用父类的事件处理程序
 * <li>如果需要使用MultiDex解决Dex超出方法数的限制问题，可以让应用程序的{@link Application}继承{@link androidx.multidex.MultiDexApplication}或者重写{@link Application}的{@link #attachBaseContext(Context)}方法并添加{@link MultiDex#install(Context)}
 * <li>如果需要使用MultiDex解决Dex超出方法数的限制问题，必须在build.gradle文件的defaultConfig模块中将multiDexEnabled设置为true开启分包模式，并在dependencies模块中添加'com.android.support:multidex:1.0.1'依赖
 * </ul>
 * <ul>
 * <strong>生命周期方法介绍：</strong>
 * <li>{@link #onCreate()}：在创建应用程序时调用这个方法。可以重写这个方法来实例化应用程序单态，也可以创建和实例化任何应用程序状态变量或共享资源。
 * <li>{@link #onLowMemory()}：一般只会在后台进程已经终止，但是前台应用程序仍然缺少内存时调用。可以重写这个处理程序来清空缓存或者释放不必要的资源。
 * <li>{@link #onTerminate()}：当终止应用程序对象时调用，不保证一定被调用，当程序是被内核终止以便为其他应用程序释放资源，将不调用此方法而直接终止进程。
 * <li>{@link #onTrimMemory(int)}：当运行时决定当前应用程序应该尝试减少其内存开销时(通常在它进入后台时)调用。它包含一个level参数，用于提供请求的上下文。
 * <li>{@link #onConfigurationChanged(Configuration)}：如果应用程序使用的值依赖于特定的配置，则重写这个方法来重新加载这个值，或者在应用程序级别处理配置改变，应用程序对象不会被终止和重启。
 * </ul>
 */
public class BaseApplication extends Application {

    static {
        // 设置启用矢量图片资源向后兼容
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);

        // 设置全局异常处理(处理无法传递的异常)
        RxJavaPlugins.setErrorHandler(new Consumer<Throwable>() {
            @Override
            public void accept(Throwable throwable) {
                Logger.e(TAG, throwable, "");
            }
        });
    }

    /**
     * TAG
     */
    private static final String TAG = "BaseApplication";
    /**
     * {@link Application}实例
     */
    private static BaseApplication sInstance;
    /**
     * 是否是debug 环境
     */
    private static boolean isDebugModel;

    public static BaseApplication getInstance() {
        return sInstance;
    }

    public static Context getAppContext() {
        return sInstance.getApplicationContext();
    }

    public static boolean isDebugModel() {
        return isDebugModel;
    }

    public static void setDebugModel(boolean DEBUG) {
        BaseApplication.isDebugModel = DEBUG;
    }

    @Override
    public void onCreate() {
        sInstance = this;
        super.onCreate();
        initLogger();                           // 初始化日志输出
        initARouter();                          // 初始化路由
        String processName = Utility.getProcessName(this, Process.myPid());
        Logger.i(TAG, "当前进程为：" + processName);
        initWebView(processName);               // 初始化WebView
        regisiterActivityLifecycle();           // 注册Activity生命周期监听
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }

    /**
     * 初始化日志输出
     */
    protected void initLogger() {
        Logger.getLogConfig()
                .setLogEnabled(isDebugModel)
                .setShowMethodInfo(true)
                .setShowThreadInfo(false);
        Logger.addPrinter(new LogcatPrinter(false));
        // Logger.addPrinter(new FilePrinter(this));
    }

    /**
     * 初始化路由
     */
    protected void initARouter() {

    }

    /**
     * 初始化WebView
     */
    protected void initWebView(String processName) {
        if (!TextUtils.isEmpty(processName) && Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            int indexOf = processName.indexOf(":");
            if (indexOf >= 0 && indexOf < processName.length()) {
                WebView.setDataDirectorySuffix(processName.substring(indexOf + 1));
            }
        }
    }

    /**
     * 注册Activity生命周期监听
     */
    protected void regisiterActivityLifecycle() {
        registerActivityLifecycleCallbacks(new SampleActivityLifecycleCallbacks());
    }

}
