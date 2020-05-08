package com.android.sample.ui.mvvm;

import android.app.Application;
import android.widget.Toast;

import com.android.common.entity.BannerInfo;
import com.android.common.entity.GankEntity;
import com.android.common.http.BaseObserver;
import com.android.common.http.ResponseEntity;
import com.android.common.mvvm.BaseViewModel;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

public class GankDataViewModel extends BaseViewModel<GankDataModel> {

    public MutableLiveData<List<BannerInfo>> banners = new MutableLiveData<>();
    public MutableLiveData<List<GankEntity>> datas = new MutableLiveData<>();

    public GankDataViewModel(@NonNull Application application) {
        super(application);
    }

    public void getGankBanners() {
        getModel()
                .getGankBanners()
                .subscribe(new BaseObserver<ResponseEntity<List<BannerInfo>>>() {
                    @Override
                    protected void doOnNext(@NonNull ResponseEntity<List<BannerInfo>> entity) {
                        if (entity.isSuccessful()) {
                            banners.setValue(entity.getData());
                        } else {
                            Toast.makeText(getApplication(), entity.getDesc(), Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    protected void doOnError(@NonNull Throwable t) {
                        Toast.makeText(getApplication(), t.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

    public void getGankCategoryDatas(int page, int size) {
        getModel()
                .getGankCategoryDatas(page, size)
                .subscribe(new BaseObserver<ResponseEntity<List<GankEntity>>>() {
                    @Override
                    protected void doOnNext(@NonNull ResponseEntity<List<GankEntity>> entity) {
                        if (entity.isSuccessful()) {
                            datas.setValue(entity.getData());
                        } else {
                            Toast.makeText(getApplication(), entity.getDesc(), Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    protected void doOnError(@NonNull Throwable t) {
                        Toast.makeText(getApplication(), t.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

}