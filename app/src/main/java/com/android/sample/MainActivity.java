package com.android.sample;

import android.content.Intent;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;

import com.android.common.base.BaseActivity;
import com.android.common.entity.UIOptions;
import com.android.common.mvvm.EmptyViewModel;
import com.android.common.receiver.NetStateChangedReceiver;
import com.android.sample.databinding.ActivityMainBinding;
import com.android.sample.ui.login.LoginActivity;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.annotation.NonNull;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

public class MainActivity extends BaseActivity<ActivityMainBinding, EmptyViewModel> implements NetStateChangedReceiver.NetStateChangedObserver {

    @Override
    public CharSequence title() {
        return getString(R.string.app_name);
    }

    @Override
    public int getContentLayoutId() {
        return R.layout.activity_main;
    }

    @Override
    public int getViewModelVariableId() {
        return 0;
    }

    @Override
    public int getMenuRes() {
        return R.menu.menu_login;
    }

    @Override
    public void initUIOptions(@NonNull UIOptions options) {
        super.initUIOptions(options);
        options.setHideToolbar(false);
    }

    @Override
    public void initViews(@NonNull View rootView) {
        BottomNavigationView navView = findViewById(R.id.nav_view);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_home, R.id.navigation_dashboard, R.id.navigation_notifications)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(navView, navController);

        NetStateChangedReceiver.registerNetworkStateReceiver(this);
        NetStateChangedReceiver.registerNetStateChangedObserver(this);
    }

    @Override
    protected boolean onMenuItemSelected(MenuItem menuItem, int itemId) {
        if (itemId == R.id.login) {
            Intent intent = new Intent(getContext(), LoginActivity.class);
            startActivity(intent);
            return true;
        }
        return super.onMenuItemSelected(menuItem, itemId);
    }

    @Override
    public void onNetStateChanged(boolean isAvailable, NetStateChangedReceiver.NetworkType type) {
        Log.i("TAG", isAvailable ? "Network connected!" : "Network disconnected!");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        NetStateChangedReceiver.removeNetStateChangedObserver(this);
        NetStateChangedReceiver.unregisterNetworkStateReceiver(this);
    }

}
