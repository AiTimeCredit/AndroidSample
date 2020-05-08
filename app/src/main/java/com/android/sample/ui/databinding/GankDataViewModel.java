package com.android.sample.ui.databinding;

import android.app.Application;
import android.widget.Toast;

import com.android.sample.adapter.GankEntityAdapter;
import com.android.common.entity.BannerInfo;
import com.android.common.entity.GankEntity;
import com.android.common.http.BaseObserver;
import com.android.common.http.ResponseEntity;
import com.android.common.mvvm.BaseViewModel;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.databinding.ObservableField;

public class GankDataViewModel extends BaseViewModel<GankDataModel> {

    public ObservableField<List<BannerInfo>> models = new ObservableField<>();
    public ObservableField<GankEntityAdapter> adapter = new ObservableField<>();

    public GankDataViewModel(@NonNull Application application) {
        super(application);
        adapter.set(new GankEntityAdapter());
    }

    public void getGankBanners() {
        getModel()
                .getGankBanners()
                .subscribe(new BaseObserver<ResponseEntity<List<BannerInfo>>>() {
                    @Override
                    protected void doOnNext(@NonNull ResponseEntity<List<BannerInfo>> entity) {
                        if (entity.isSuccessful()) {
                            models.set(entity.getData());
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
                            GankEntityAdapter entityAdapter = adapter.get();
                            if (entityAdapter != null) {
                                entityAdapter.refresh(entity.getData());
                            }
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