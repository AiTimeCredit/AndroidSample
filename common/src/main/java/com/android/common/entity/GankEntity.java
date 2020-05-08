package com.android.common.entity;

import com.google.gson.annotations.SerializedName;

import java.util.List;

import androidx.annotation.NonNull;

/**
 * Gank entity
 */
public class GankEntity {

    @SerializedName("_id")
    private String id;                  // ID
    @SerializedName("category")
    private String category;            // 分类
    @SerializedName("desc")
    private String title;               // 标题
    @SerializedName(value = "images")
    private List<String> images;        // 图片
    @SerializedName("source")
    private String source;              // 来源
    @SerializedName("type")
    private String type;                // 类型
    @SerializedName("url")
    private String url;                 // 链接
    @SerializedName("views")
    private int views;                  // 查看次数
    @SerializedName("who")
    private String author;              // 发布人
    @SerializedName("createdAt")
    private String createdTime;         // 创建时间
    @SerializedName("publishedAt")
    private String publishedTime;       // 发布时间

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public List<String> getImages() {
        return images;
    }

    public void setImages(List<String> images) {
        this.images = images;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public int getViews() {
        return views;
    }

    public void setViews(int views) {
        this.views = views;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getCreatedTime() {
        return createdTime;
    }

    public void setCreatedTime(String createdTime) {
        this.createdTime = createdTime;
    }

    public String getPublishedTime() {
        return publishedTime;
    }

    public void setPublishedTime(String publishedTime) {
        this.publishedTime = publishedTime;
    }

    @NonNull
    @Override
    public String toString() {
        return "GankEntity{" +
                "id='" + id + '\'' +
                ", category='" + category + '\'' +
                ", title='" + title + '\'' +
                ", images=" + images +
                ", source='" + source + '\'' +
                ", type='" + type + '\'' +
                ", url='" + url + '\'' +
                ", views=" + views +
                ", author='" + author + '\'' +
                ", createdTime='" + createdTime + '\'' +
                ", publishedTime='" + publishedTime + '\'' +
                '}';
    }

}
