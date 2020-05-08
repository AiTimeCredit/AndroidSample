package com.android.sample;

import com.android.common.BaseApplication;

/**
 * Application
 */
public class SampleApplication extends BaseApplication {

    @Override
    public void onCreate() {
        setDebugModel(BuildConfig.DEBUG);
        super.onCreate();
    }

}
