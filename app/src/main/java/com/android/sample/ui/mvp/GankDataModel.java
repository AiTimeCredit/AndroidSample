package com.android.sample.ui.mvp;

import com.android.common.entity.BannerInfo;
import com.android.common.entity.GankEntity;
import com.android.common.http.ObservableTransformerAsync;
import com.android.common.http.ResponseEntity;
import com.android.common.http.RetrofitManage;

import java.util.List;

import io.reactivex.Observable;

/**
 * Gank data model
 */
public class GankDataModel implements GankDataContract.Model {

    @Override
    public Observable<ResponseEntity<List<BannerInfo>>> getGankBanners() {
        return RetrofitManage.getGankApiService()
                .getGankBanners()
                .compose(new ObservableTransformerAsync<>());
    }

    @Override
    public Observable<ResponseEntity<List<GankEntity>>> getGankCategoryDatas(int page, int size) {
        return RetrofitManage.getGankApiService()
                .getCategoryDatas(page, size)
                .compose(new ObservableTransformerAsync<>());
    }

}
