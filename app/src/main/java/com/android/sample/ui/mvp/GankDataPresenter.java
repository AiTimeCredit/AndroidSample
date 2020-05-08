package com.android.sample.ui.mvp;

import android.widget.Toast;

import com.android.common.entity.BannerInfo;
import com.android.common.entity.GankEntity;
import com.android.common.http.BaseObserver;
import com.android.common.http.ResponseEntity;

import java.util.List;

import androidx.annotation.NonNull;

/**
 * Gank data presenter
 */
public class GankDataPresenter extends GankDataContract.Presenter<GankDataModel> {

    @Override
    public void getGankBanners() {
        getModel()
                .getGankBanners()
                .subscribe(new BaseObserver<ResponseEntity<List<BannerInfo>>>() {
                    @Override
                    protected void doOnNext(@NonNull ResponseEntity<List<BannerInfo>> entity) {
                        if (entity.isSuccessful()) {
                            getView().handleGankBannersResult(entity);
                        } else {
                            Toast.makeText(getView().getContext(), entity.getDesc(), Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    protected void doOnError(@NonNull Throwable t) {
                        Toast.makeText(getView().getContext(), t.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

    @Override
    public void getGankCategoryDatas(int page, int size) {
        getModel()
                .getGankCategoryDatas(page, size)
                .subscribe(new BaseObserver<ResponseEntity<List<GankEntity>>>() {
                    @Override
                    protected void doOnNext(@NonNull ResponseEntity<List<GankEntity>> entity) {
                        if (entity.isSuccessful()) {
                            getView().handleGankCategoryDatasResult(entity);
                        } else {
                            Toast.makeText(getView().getContext(), entity.getDesc(), Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    protected void doOnError(@NonNull Throwable t) {
                        Toast.makeText(getView().getContext(), t.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

}
