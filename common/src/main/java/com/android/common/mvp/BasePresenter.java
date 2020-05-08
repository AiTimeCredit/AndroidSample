package com.android.common.mvp;

import com.android.common.utils.ReflexUtil;

/**
 * Presenter基类
 */
public class BasePresenter<Model extends IModel, View extends IView> implements IPresenter<Model, View> {

    private View mView;
    private Model mModel;

    @Override
    public void attachView(View view) {
        this.mView = view;
    }

    @Override
    public Model getModel() {
        if (mModel == null) {
            mModel = ReflexUtil.getTypeInstance(this, IModel.class);
        }
        return mModel;
    }

    @Override
    public View getView() {
        if (mView == null) {
            throw new IllegalArgumentException("Please call Presenter.attachView(mvpView) before requesting data to the Presenter.");
        }
        return mView;
    }

    @Override
    public void detachView() {
        mView = null;
        mModel = null;
    }

}
