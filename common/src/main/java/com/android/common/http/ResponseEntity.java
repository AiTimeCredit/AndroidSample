package com.android.common.http;

import com.google.gson.annotations.SerializedName;

import androidx.annotation.NonNull;

/**
 * Response entity
 */
public class ResponseEntity<T> {

    @SerializedName(value = "status")
    private int status;
    @SerializedName(value = "code")
    private int code;
    @SerializedName(value = "desc")
    private String desc;
    @SerializedName(value = "data")
    private T data;
    @SerializedName(value = "page")
    private int page;
    @SerializedName(value = "page_count")
    private int pageCount;
    @SerializedName(value = "total_counts")
    private int totalCounts;

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public int getPageCount() {
        return pageCount;
    }

    public void setPageCount(int pageCount) {
        this.pageCount = pageCount;
    }

    public int getTotalCounts() {
        return totalCounts;
    }

    public void setTotalCounts(int totalCounts) {
        this.totalCounts = totalCounts;
    }

    /**
     * 判断是否成功
     */
    public boolean isSuccessful() {
        return code == 1 || status == 100;
    }

    @NonNull
    @Override
    public String toString() {
        return "ResponseEntity{" +
                "status=" + status +
                ", desc='" + desc + '\'' +
                ", data=" + data +
                '}';
    }

}
