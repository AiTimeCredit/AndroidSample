package com.android.common.base;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;

import com.android.common.base.listener.IFragment;
import com.android.common.entity.UIOptions;
import com.android.common.mvvm.BaseViewModel;
import com.android.common.utils.ReflexUtil;

import androidx.annotation.IdRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.ViewModelProvider;

/**
 * {@link Fragment}基类
 */
public abstract class BaseFragment<DataBinding extends ViewDataBinding, ViewModel extends BaseViewModel> extends Fragment implements IFragment<DataBinding, ViewModel> {

    protected final String TAG = this.getClass().getSimpleName();
    private View rootView;
    private DataBinding mDataBinding;
    private ViewModel mViewModel;
    private boolean isViewCreated;
    private Context mContext;
    private UIOptions uiOptions = new UIOptions();

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = getArguments();
        if (bundle != null) {
            // 处理Bundle(主要用来获取其中携带的参数)
            handleBundle(bundle);
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // 避免多次从xml中加载布局文件
        if (rootView != null) {
            // 缓存的rootView需要判断是否已经被加过parent，如果有parent需要从parent删除，要不然会发生这个rootview已经有parent的错误
            // java.lang.IllegalStateException: The specified child already has a parent. You must call removeView() on the child's parent first.
            ViewParent parent = rootView.getParent();
            if (parent instanceof ViewGroup) {
                ((ViewGroup) parent).removeView(rootView);
            }
        } else {
            mDataBinding = DataBindingUtil.inflate(inflater, getContentLayoutId(), container, false);
            if (mDataBinding != null) {
                rootView = mDataBinding.getRoot();
            } else {
                rootView = inflater.inflate(getContentLayoutId(), container, false);
            }
            mContext = rootView != null ? rootView.getContext() : inflater.getContext();
            initUIOptions(uiOptions);
            initViewModel();
            // 初始化titleBar
            initTitleBar(rootView);
            // 初始化View
            initViews(rootView);
        }
        return rootView;

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (!isViewCreated) {
            isViewCreated = true;
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.i("BaseFragment", this.getClass().getName());
    }

    protected void initViewModel() {
        mViewModel = obtainViewModel();
        if (mViewModel != null) {
            // 让ViewModel拥有View的生命周期感应
            getLifecycle().addObserver(mViewModel);
            if (mDataBinding != null) {
                mDataBinding.setVariable(getViewModelVariableId(), mViewModel);
            }
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (getViewModel() != null) {
            getViewModel().onActivityResult(requestCode, resultCode, data);
        }
    }

    @Nullable
    @Override
    public Context getContext() {
        Context context = super.getContext();
        return context != null ? context : mContext;
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
        return mDataBinding;
    }

    @Override
    public ViewModel getViewModel() {
        return mViewModel;
    }

    @Override
    public boolean isViewCreated() {
        return isViewCreated;
    }

    @Override
    public void handleBundle(@NonNull Bundle bundle) {

    }

    @Override
    public void initUIOptions(@NonNull UIOptions options) {

    }

    @Override
    public void initTitleBar(@NonNull View rootView) {

    }

    @Override
    public void loadData() {

    }

    /**
     * 查找View
     */
    public final <T extends View> T findViewById(@IdRes int id) {
        if (rootView == null) {
            return null;
        }
        return rootView.findViewById(id);
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
    public void onDestroy() {
        super.onDestroy();
    }

}
