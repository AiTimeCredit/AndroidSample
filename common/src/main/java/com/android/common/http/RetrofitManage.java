package com.android.common.http;

import com.android.common.GankApiService;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Retrofit管理器
 */
public class RetrofitManage {

    private static final String BASE_URL = "http://gank.io/";
    private static final String BASE_URL_LOGIN = "http://mobile.abcash.test.youyuwo.com/";

    private static Retrofit retrofit;
    private static Retrofit loginRetrofit;
    private static GankApiService gankApiService;

    public static OkHttpClient getOkHttpClient() {
        return new OkHttpClient.Builder()
                .readTimeout(10, TimeUnit.SECONDS)
                .writeTimeout(10, TimeUnit.SECONDS)
                .connectTimeout(10, TimeUnit.SECONDS)
                .addInterceptor(new HttpLoggingInterceptor()
                        .setLevel(HttpLoggingInterceptor.Level.BODY)
                )
                .build();
    }

    public static Retrofit getRetrofit() {
        if (retrofit == null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .client(getOkHttpClient())
                    .addConverterFactory(GsonConverterFactory.create())
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                    .build();
        }
        return retrofit;
    }

    public static Retrofit getLoginRetrofit() {
        if (loginRetrofit == null) {
            loginRetrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL_LOGIN)
                    .client(getOkHttpClient())
                    .addConverterFactory(GsonConverterFactory.create())
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                    .build();
        }
        return loginRetrofit;
    }

    public static GankApiService getGankApiService() {
        if (gankApiService == null) {
            gankApiService = getRetrofit().create(GankApiService.class);
        }
        return gankApiService;
    }

}
