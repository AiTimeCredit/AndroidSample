package com.android.sample;

import android.content.Intent;
import android.os.Message;
import android.view.View;

import com.android.common.base.BaseActivity;
import com.android.common.entity.UIOptions;
import com.android.common.utils.Constants;
import com.android.common.utils.PermissionUtil;
import com.android.common.utils.SafetyHandler;
import com.android.sample.utils.MainHelper;

import androidx.annotation.NonNull;

/**
 * 启动页面
 */
public class SplashActivity extends BaseActivity implements SafetyHandler.Delegate {

    private static final int JUMP_MAIN_ACTIVITY = 0;

    private Runnable jumpTask;
    private SafetyHandler mHandler = SafetyHandler.create(this);

    @Override
    public CharSequence title() {
        return null;
    }

    @Override
    public int getContentLayoutId() {
        return R.layout.activity_splash;
    }

    @Override
    public int getViewModelVariableId() {
        return 0;
    }

    @Override
    public void initUIOptions(@NonNull UIOptions options) {
        super.initUIOptions(options);
        options.setHideToolbar(true)
                .setFitsSystemWindows(false);
    }

    @Override
    public void initViews(@NonNull View rootView) {
        mHandler.sendEmptyMessageDelayed(JUMP_MAIN_ACTIVITY, 2 * 1000L);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (jumpTask != null) {
            mHandler.post(jumpTask);
        }
    }

    @Override
    public void handleMessage(@NonNull Message msg) {
        if (msg.what == JUMP_MAIN_ACTIVITY) {
            jumpTask = () -> {
                handleJumpMain();
                finish();
            };
            if (isActivityResumed()) {
                mHandler.post(jumpTask);
            }
        }
    }

    private void handleJumpMain() {
        boolean hasPermissions = PermissionUtil.hasPermissions(getContext(), Constants.PERMISSIONS);
        if (!hasPermissions) {
            Intent intent = new Intent(getContext(), PermissionActivity.class);
            startActivity(intent);
            return;
        }
        MainHelper.start();
        MainHelper.gotoMainActivity(getContext());
    }

}
