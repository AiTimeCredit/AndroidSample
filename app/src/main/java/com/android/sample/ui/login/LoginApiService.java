package com.android.sample.ui.login;

import com.android.common.http.ResponseEntity;
import com.android.sample.entity.LoginRecord;
import com.android.sample.entity.VerifyCode;

import java.util.Map;

import io.reactivex.Observable;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

/**
 * 用户登录相关 API
 */
public interface LoginApiService {

    /**
     * 短信验证码
     */
    @FormUrlEncoded
    @POST("/notcontrol/public/sendSms")
    Observable<ResponseEntity<VerifyCode>> sendSmsCode(@FieldMap Map<String, String> map);

    /**
     * 手机号快速登录
     */
    @FormUrlEncoded
    @POST("notcontrol/user/quickLogin")
    Observable<ResponseEntity<LoginRecord>> loginWithQuick(@FieldMap Map<String, String> params);

}
