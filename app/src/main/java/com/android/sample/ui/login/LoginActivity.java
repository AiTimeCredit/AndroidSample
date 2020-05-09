package com.android.sample.ui.login;

import android.app.ProgressDialog;
import android.view.View;

import com.android.common.base.BaseActivity;
import com.android.common.entity.UIOptions;
import com.android.common.utils.Utility;
import com.android.sample.BR;
import com.android.sample.R;
import com.android.sample.databinding.ActivityLoginBinding;

import androidx.annotation.NonNull;

/**
 * 登录页面
 */
public class LoginActivity extends BaseActivity<ActivityLoginBinding, LoginViewModel> {

    private ProgressDialog dialog;

    @Override
    public CharSequence title() {
        return null;
    }

    @Override
    public int getContentLayoutId() {
        return R.layout.activity_login;
    }

    @Override
    public int getViewModelVariableId() {
        return BR.loginViewModel;
    }

    @Override
    public void initUIOptions(@NonNull UIOptions options) {
        super.initUIOptions(options);
        options.setHideToolbar(true)
                .setKeyboardEnable(true)
                .setStatusBarDarkFont(true)
                .setStatusBarColor(R.color.window_background);
    }

    @Override
    public void initViews(@NonNull View rootView) {
        dialog = new ProgressDialog(this);
        dialog.setMessage("Loading...");

        getViewModel().finish.observe(this, value -> onBackClick());
        getViewModel().hideSoftInput.observe(this, value -> Utility.hideSoftInput(this));
        getViewModel().loading.observe(this, showFlag -> {
            if (showFlag) {
                if (!dialog.isShowing()) {
                    dialog.show();
                }
            } else {
                dialog.dismiss();
            }
        });
    }

}
