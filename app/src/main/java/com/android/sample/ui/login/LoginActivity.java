package com.android.sample.ui.login;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.MenuItem;

import com.android.common.utils.Utility;
import com.android.sample.BR;
import com.android.sample.R;
import com.android.sample.databinding.ActivityLoginBinding;
import com.gyf.immersionbar.ImmersionBar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;

/**
 * 登录页面
 */
public class LoginActivity extends AppCompatActivity {

    private ProgressDialog dialog;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityLoginBinding loginBinding = DataBindingUtil.setContentView(this, R.layout.activity_login);
        LoginViewModel viewModel = new ViewModelProvider(this).get(LoginViewModel.class);
        loginBinding.setVariable(BR.loginViewModel, viewModel);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        ImmersionBar.with(this)
                .statusBarColor(R.color.window_background)
                .keyboardEnable(true)
                .statusBarDarkFont(true)
                .init();

        dialog = new ProgressDialog(this);
        dialog.setMessage("Loading...");

        viewModel.finish.observe(this, value -> onBackPressed());
        viewModel.hideSoftInput.observe(this, value -> Utility.hideSoftInput(this));
        viewModel.loading.observe(this, showFlag -> {
            if (showFlag) {
                if (!dialog.isShowing()) {
                    dialog.show();
                }
            } else {
                dialog.dismiss();
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

}
