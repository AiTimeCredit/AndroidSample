package com.android.common;

import com.android.common.entity.BannerInfo;
import com.android.common.entity.GankEntity;
import com.android.common.http.ResponseEntity;

import java.util.List;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Path;

/**
 * Gank Api
 */
public interface GankApiService {

    @GET("api/v2/banners")
    Observable<ResponseEntity<List<BannerInfo>>> getGankBanners();

    @GET("api/v2/data/category/Girl/type/Girl/page/{page}/count/{size}")
    Observable<ResponseEntity<List<GankEntity>>> getCategoryDatas(@Path("page") int page, @Path("size") int size);

}
