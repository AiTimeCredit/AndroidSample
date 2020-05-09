package com.android.sample;

import android.view.View;

import com.android.common.base.BaseActivity;
import com.android.common.entity.UIOptions;
import com.android.common.http.BaseObserver;
import com.android.common.utils.Constants;
import com.android.common.utils.PermissionUtil;
import com.android.common.utils.ToastUtil;
import com.android.common.utils.Utility;
import com.android.sample.utils.MainHelper;
import com.tbruyelle.rxpermissions2.Permission;
import com.tbruyelle.rxpermissions2.RxPermissions;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;

/**
 * 权限请求页面
 */
public class PermissionActivity extends BaseActivity {

    @Override
    public CharSequence title() {
        return "Permission";
    }

    @Override
    public int getContentLayoutId() {
        return R.layout.activity_permission;
    }

    @Override
    public int getViewModelVariableId() {
        return 0;
    }

    @Override
    public void initUIOptions(@NonNull UIOptions options) {
        super.initUIOptions(options);
        options.setHideHomeUp(true);
    }

    @Override
    public void initViews(@NonNull View rootView) {
        findViewById(R.id.btn_permission_agree).setOnClickListener(v -> requestPermissions());
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (PermissionUtil.hasPermissions(getContext(), Constants.PERMISSIONS)) {
            agreePermissions();
        }
    }

    /**
     * 请求权限
     */
    private void requestPermissions() {
        new RxPermissions(this)
                .requestEachCombined(Constants.PERMISSIONS.toArray(new String[0]))
                .subscribe(new BaseObserver<Permission>() {
                    @Override
                    protected void doOnNext(@NonNull Permission permission) {
                        if (permission.granted) { // 用户已经同意该权限
                            agreePermissions();
                        } else if (permission.shouldShowRequestPermissionRationale) {
                            new AlertDialog.Builder(getContext())
                                    .setTitle(R.string.permission_title)
                                    .setMessage(R.string.permission_dialog_message_start_up)
                                    .setNegativeButton(android.R.string.cancel, null)
                                    .setPositiveButton(android.R.string.ok, (dialog, which) -> requestPermissions())
                                    .create()
                                    .show();
                        } else {// 用户拒绝了该权限
                            new AlertDialog.Builder(getContext())
                                    .setTitle(R.string.permission_title)
                                    .setMessage(R.string.permission_dialog_tips)
                                    .setNegativeButton(android.R.string.cancel, (dialog, which) -> ToastUtil.showToast(R.string.permission_request_canceled))
                                    .setPositiveButton(android.R.string.ok, (dialog, which) -> Utility.startPackageSettings(getContext()))
                                    .create()
                                    .show();
                        }
                    }

                    @Override
                    protected void doOnError(@NonNull Throwable t) {

                    }

                });
    }

    /**
     * 同意权限请求
     */
    private void agreePermissions() {
        MainHelper.start();
        MainHelper.gotoMainActivity(getContext(), getIntent().getExtras());
        finish();
    }

}
