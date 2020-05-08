package com.android.sample.ui.mvp;

import com.android.common.entity.BannerInfo;
import com.android.common.entity.GankEntity;
import com.android.common.http.ResponseEntity;
import com.android.common.mvp.BasePresenter;
import com.android.common.mvp.IModel;
import com.android.common.mvp.IView;

import java.util.List;

import io.reactivex.Observable;

/**
 * Gank data contract
 */
public interface GankDataContract {

    interface View extends IView {

        void handleGankBannersResult(ResponseEntity<List<BannerInfo>> entity);

        void handleGankCategoryDatasResult(ResponseEntity<List<GankEntity>> entity);
    }

    interface Model extends IModel {

        Observable<ResponseEntity<List<BannerInfo>>> getGankBanners();

        Observable<ResponseEntity<List<GankEntity>>> getGankCategoryDatas(int page, int size);
    }

    abstract class Presenter<Model extends IModel> extends BasePresenter<Model, View> {

        public abstract void getGankBanners();

        public abstract void getGankCategoryDatas(int page, int size);

    }

}
