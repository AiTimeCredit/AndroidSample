package com.android.sample.entity;

import com.google.gson.annotations.SerializedName;

/**
 * 用户登录记录
 */
public class LoginRecord {

    @SerializedName(value = "userId")
    private String userId;           // userId

    @SerializedName(value = "phone")
    private String phone;           // 手机号
    @SerializedName(value = "appId")
    private String appId;           // appId
    @SerializedName(value = "accessToken")
    private String accessToken;     // accessToken

    @SerializedName(value = "isRegister")
    private String isRegister;      // 是否为新用户注册

    private long loginTime;         // 最近登录时间

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getAppId() {
        return appId;
    }

    public void setAppId(String appId) {
        this.appId = appId;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public String getIsRegister() {
        return isRegister;
    }

    public void setIsRegister(String isRegister) {
        this.isRegister = isRegister;
    }

    public long getLoginTime() {
        return loginTime;
    }

    public void setLoginTime(long loginTime) {
        this.loginTime = loginTime;
    }

}
