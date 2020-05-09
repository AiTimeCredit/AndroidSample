package com.android.sample.entity;

import com.google.gson.annotations.SerializedName;

/**
 * 验证码信息
 */
public class VerifyCode {

    @SerializedName(value = "statusCode")
    private String statusCode;
    @SerializedName(value = "base64Img")
    private String base64Img;

    public String getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(String statusCode) {
        this.statusCode = statusCode;
    }

    public String getBase64Img() {
        return base64Img;
    }

    public void setBase64Img(String base64Img) {
        this.base64Img = base64Img;
    }

}
