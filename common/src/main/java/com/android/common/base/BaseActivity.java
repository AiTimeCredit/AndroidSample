package com.android.common.base;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.android.common.R;
import com.android.common.base.listener.IActivity;
import com.android.common.entity.UIOptions;
import com.android.common.mvvm.BaseViewModel;
import com.android.common.utils.ReflexUtil;
import com.android.common.utils.ToolBarHelper;
import com.gyf.immersionbar.ImmersionBar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.ViewModelProvider;

/**
 * {@link android.app.Activity} 基类
 */
public abstract class BaseActivity<DataBinding extends ViewDataBinding, ViewModel extends BaseViewModel>
        extends AppCompatActivity implements IActivity<DataBinding, ViewModel> {

    protected final String TAG = this.getClass().getSimpleName();
    private DataBinding dataBinding;
    private ViewModel viewModel;
    private BaseActivity context;
    private boolean isResumed;
    private boolean isWindowAttached;
    private Menu menu;
    private View rootView;
    private Toolbar toolbar;
    private UIOptions uiOptions = new UIOptions();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        context = this;
        beforeSuper();
        super.onCreate(savedInstanceState);

        init(savedInstanceState);                   // 初始化
        Intent intent = getIntent();
        if (intent != null) {
            handleIntent(intent);                   // 处理Intent(主要用来获取其中携带的参数)
        }
        initUIOptions(uiOptions);
        initImmersionBar();
        if (getContentLayoutId() != 0) {
            dataBinding = DataBindingUtil.setContentView(this, getContentLayoutId());
            if (dataBinding != null) {
                rootView = dataBinding.getRoot();
            } else {
                setContentView(getContentLayoutId());
                rootView = findViewById(android.R.id.content);
            }
        }
        initViewModel();
        initToolbar();
        initViews(rootView);
        loadData();
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
        if (intent != null) {
            handleIntent(intent);
        }
    }

    @Override
    protected void onResume() {
        isResumed = true;
        super.onResume();
        Log.i("BaseActivity", this.getClass().getName());
    }

    @Override
    protected void onPause() {
        isResumed = false;
        super.onPause();
    }

    @Override
    public void onAttachedToWindow() {
        isWindowAttached = true;
        super.onAttachedToWindow();
    }

    @Override
    public void onDetachedFromWindow() {
        isWindowAttached = false;
        super.onDetachedFromWindow();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (getViewModel() != null) {
            getViewModel().onActivityResult(requestCode, resultCode, data);
        }
    }

    @NonNull
    @Override
    public BaseActivity getContext() {
        return context;
    }

    @NonNull
    @Override
    public UIOptions getUIOptions() {
        return uiOptions;
    }

    @NonNull
    @Override
    public View getRootView() {
        return rootView;
    }

    @Override
    public DataBinding getDataBinding() {
        return dataBinding;
    }

    @Override
    public ViewModel getViewModel() {
        return viewModel;
    }

    @Override
    public Menu getMenu() {
        return menu;
    }

    @Override
    public Toolbar getToolBar() {
        if (toolbar == null && !uiOptions.isHideToolbar()) {
            @SuppressLint("InflateParams")
            View view = getLayoutInflater().inflate(R.layout.layout_toolbar, null);
            toolbar = view.findViewById(R.id.toolbar);
            int toolbarHeight = getResources().getDimensionPixelSize(R.dimen.actionBarSize);
            FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(-1, -1);
            params.height = toolbarHeight;
            addContentView(view, params);
            View childAt = ((ViewGroup) view.getParent()).getChildAt(0);
            FrameLayout.LayoutParams childParams = (FrameLayout.LayoutParams) childAt.getLayoutParams();
            childParams.height = ViewGroup.LayoutParams.MATCH_PARENT;
            childParams.width = ViewGroup.LayoutParams.MATCH_PARENT;
            childParams.topMargin = toolbarHeight;
            childAt.setLayoutParams(childParams);
        }
        return toolbar;
    }

    @Override
    public boolean isActivityResumed() {
        return isResumed;
    }

    @Override
    public boolean isWindowAttached() {
        return isWindowAttached;
    }

    @Override
    public void beforeSuper() {

    }

    @Override
    public void init(Bundle savedInstanceState) {

    }

    @Override
    public void initImmersionBar() {
        ImmersionBar.with(this)
                .statusBarColor(uiOptions.getStatusBarColor())
                .keyboardEnable(uiOptions.isKeyboardEnable())
                .statusBarDarkFont(uiOptions.isStatusBarDarkFont())
                .fitsSystemWindows(uiOptions.isFitsSystemWindows())
                .init();
    }

    @Override
    public void initUIOptions(@NonNull UIOptions options) {

    }

    protected void initViewModel() {
        viewModel = obtainViewModel();
        if (viewModel != null) {
            // 让ViewModel拥有View的生命周期感应
            getLifecycle().addObserver(viewModel);
            if (dataBinding != null) {
                dataBinding.setVariable(getViewModelVariableId(), viewModel);
            }
        }
    }

    @Override
    public void initToolbar() {
        Toolbar toolBar = getToolBar();
        if (toolBar != null) {
            setSupportActionBar(toolBar);
            ActionBar actionBar = getSupportActionBar();
            if (actionBar != null) {
                ToolBarHelper.initActionBar(actionBar, uiOptions.isHideHomeUp(), uiOptions.isHideToolbar());
            }
            ToolBarHelper.initTitle(toolBar, title());
            if (!uiOptions.isHideHomeUp()) {
                ToolBarHelper.initNavigation(toolBar, uiOptions.getNavigationIcon());
            }
        }
    }

    @Override
    public void handleIntent(@NonNull Intent intent) {

    }

    @Override
    public int getMenuRes() {
        return 0;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (getMenuRes() != 0) {
            getMenuInflater().inflate(getMenuRes(), menu);
            this.menu = menu;
            return true;
        }
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int itemId = item.getItemId();
        if (itemId != 0) {
            if (itemId == android.R.id.home) {
                onBackClick();
            } else {
                return onMenuItemSelected(item, itemId);
            }
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * 菜单项选择事件
     */
    protected boolean onMenuItemSelected(MenuItem menuItem, int itemId) {
        return false;
    }

    @Override
    public void loadData() {

    }

    @SuppressWarnings("unchecked")
    @Override
    public ViewModel obtainViewModel() {
        Class<AndroidViewModel> targetClass = ReflexUtil.getTargetClass(this, AndroidViewModel.class);
        if (targetClass != null) {
            return (ViewModel) new ViewModelProvider(this).get(targetClass);
        }
        return null;
    }

    @Override
    public final void onBackPressed() {
        onBackClick();
    }

    /**
     * 返回事件处理
     */
    public void onBackClick() {
        super.onBackPressed();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

}
