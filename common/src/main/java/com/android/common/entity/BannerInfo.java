package com.android.common.entity;

import com.google.gson.annotations.SerializedName;

import androidx.annotation.NonNull;

/**
 * Banner info
 */
public class BannerInfo {

    @SerializedName(value = "title")
    private String title;
    @SerializedName(value = "image")
    private String image;
    @SerializedName(value = "url")
    private String url;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    @NonNull
    @Override
    public String toString() {
        return "BannerInfo{" +
                "title='" + title + '\'' +
                ", image='" + image + '\'' +
                ", url='" + url + '\'' +
                '}';
    }

}
