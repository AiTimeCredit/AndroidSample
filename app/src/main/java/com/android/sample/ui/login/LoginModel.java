package com.android.sample.ui.login;

import com.aitime.android.security.MD5;
import com.aitime.android.security.Security;
import com.android.common.http.ObservableTransformerAsync;
import com.android.common.http.ResponseEntity;
import com.android.common.mvvm.BaseModel;
import com.android.sample.entity.LoginRecord;
import com.android.sample.entity.VerifyCode;

import java.util.HashMap;

import io.reactivex.Observable;

/**
 * 登录Model
 */
public class LoginModel extends BaseModel {

    /**
     * 发送短信验证码
     */
    public Observable<ResponseEntity<VerifyCode>> sendSmsCode(String phone) {
        HashMap<String, String> params = getParams();
        params.put("mobileNo", Security.encrypt(phone));
        params.put("captchaType", "2");
        params.put("sign", MD5.md5Hex("woyouyudiandidaiqazwsxedc" + Security.encrypt(phone)));
        return createService(LoginApiService.class)
                .sendSmsCode(params)
                .compose(new ObservableTransformerAsync<>());
    }

    /**
     * 手机号快速登录
     */
    public Observable<ResponseEntity<LoginRecord>> loginWithQuick(String phone, String smsCode) {
        HashMap<String, String> params = getParams();
        params.put("mobileNo", Security.encrypt(phone));
        params.put("captcha", getNotNullString(smsCode));
        return createService(LoginApiService.class)
                .loginWithQuick(params)
                .compose(new ObservableTransformerAsync<>());
    }

}
