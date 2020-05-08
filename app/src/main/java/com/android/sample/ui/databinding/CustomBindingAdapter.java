package com.android.sample.ui.databinding;

import android.content.Context;
import android.graphics.Color;
import android.text.TextUtils;
import android.text.method.LinkMovementMethod;
import android.widget.TextView;

import com.android.sample.R;
import com.android.sample.entity.Agreement;
import com.android.common.utils.Utility;

import java.util.List;

import androidx.core.content.ContextCompat;
import androidx.databinding.BindingAdapter;
import androidx.recyclerview.widget.RecyclerView;
import cn.bingoogolapple.bgabanner.BGABanner;
import cn.iwgang.simplifyspan.SimplifySpanBuild;
import cn.iwgang.simplifyspan.unit.SpecialClickableUnit;
import cn.iwgang.simplifyspan.unit.SpecialTextUnit;

/**
 * DataBinding自定义BindingAdapter
 */
public class CustomBindingAdapter {

    /**
     * 设置{@link BGABanner}的数据
     */
    @BindingAdapter(value = {"models", "tips"}, requireAll = false)
    public static void setBGABannerData(BGABanner banner, List<?> models, List<String> tips) {
        banner.setData(models, tips);
    }

    /**
     * 设置{@link RecyclerView}的{@link RecyclerView.Adapter}
     */
    @BindingAdapter("adapter")
    public static void setRecyclerViewAdapter(RecyclerView recyclerView, RecyclerView.Adapter adapter) {
        recyclerView.setAdapter(adapter);
    }

    @BindingAdapter(value = {"agreementPrefix", "agreementSuffix", "agreements"}, requireAll = false)
    public static void setAgreements(TextView textView, CharSequence agreementPrefix, CharSequence agreementSuffix, List<Agreement> agreements) {
        SimplifySpanBuild simplifySpanBuild = new SimplifySpanBuild();
        if (agreements != null && !agreements.isEmpty()) {
            int size = agreements.size();
            Context context = textView.getContext();
            if (!TextUtils.isEmpty(agreementPrefix)) {
                simplifySpanBuild.append(agreementPrefix.toString());
            }
            for (int i = 0; i < size; i++) {
                Agreement agreement = agreements.get(i);
                if (agreement == null) {
                    continue;
                }
                SpecialClickableUnit specialClickableUnit = new SpecialClickableUnit(textView, (tv, clickableSpan) ->
                        Utility.openWebKit(context, agreement.getUrl())
                );
                specialClickableUnit.setNormalTextColor(ContextCompat.getColor(context, R.color.colorHighlight));
                simplifySpanBuild.append(new SpecialTextUnit(agreement.getName()).setClickableUnit(specialClickableUnit));
                if (i < size - 2) {
                    simplifySpanBuild.append("、");
                } else if (i == size - 2) {
                    simplifySpanBuild.append(" ").append(context.getString(R.string.and)).append(" ");
                }
            }
        }
        if (!TextUtils.isEmpty(agreementSuffix)) {
            simplifySpanBuild.append(agreementSuffix.toString());
        }
        textView.setText(simplifySpanBuild.build());
        textView.setHighlightColor(Color.TRANSPARENT);
        textView.setMovementMethod(LinkMovementMethod.getInstance());
    }

}
