package com.android.sample.entity;

import android.content.Context;

import com.android.sample.R;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * 协议信息
 */
public class Agreement {

    @SerializedName(value = "agreementName")
    private String name;
    @SerializedName(value = "agreementUrl")
    private String url;

    public Agreement() {
    }

    public Agreement(String name, String url) {
        this.name = name;
        this.url = url;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public static String getAgreementsString(Context context, List<Agreement> agreements) {
        if (agreements == null || agreements.isEmpty()) {
            return "";
        }
        int size = agreements.size();
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < size; i++) {
            builder.append("《").append(agreements.get(i).getName()).append("》");
            if (i < size - 2) {
                builder.append("、");
            } else if (i == size - 2) {
                builder.append(context.getString(R.string.and));
            }
        }
        return builder.toString();
    }

}
