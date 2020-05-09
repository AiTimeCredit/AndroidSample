package com.android.sample.adapter;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.sample.R;
import com.android.common.entity.GankEntity;
import com.android.common.utils.Utility;
import com.android.sample.PictureActivity;
import com.bumptech.glide.Glide;
import com.bumptech.glide.Priority;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.bitmap.BitmapTransitionOptions;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.BitmapImageViewTarget;
import com.bumptech.glide.request.transition.Transition;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

/**
 * GankEntity适配器
 */
public class GankEntityAdapter extends RecyclerView.Adapter<GankEntityAdapter.GankEntityViewHolder> {

    private List<GankEntity> mDatas = new ArrayList<>();
    private Context context;

    public GankEntityAdapter() {
    }

    public void refresh(List<GankEntity> datas) {
        mDatas.clear();
        if (datas != null) {
            mDatas.addAll(datas);
        }
        this.notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return mDatas.size();
    }

    @NonNull
    @Override
    public GankEntityViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (context == null) {
            context = parent.getContext();
        }
        View itemView = LayoutInflater.from(context).inflate(R.layout.layout_item_gank_data_girl, parent, false);
        return new GankEntityViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull GankEntityViewHolder holder, int position) {
        GankEntity data = mDatas.get(position);
        holder.tvDate.setText(data.getPublishedTime());
        ImageView ivPicture = holder.ivPicture;
        int itemWidth = (Utility.getScreenWidth(context) - 4 * Utility.dp2px(context, 3)) / 2;
        Glide.with(context)
                .asBitmap()
                .load(data.getUrl())
                .apply(new RequestOptions()
                        .centerInside()
                        .placeholder(R.drawable.ic_image_placeholder)
                        .error(R.drawable.ic_image_placeholder)
                        .priority(Priority.HIGH)
                        .override(itemWidth, Integer.MAX_VALUE)
                        .diskCacheStrategy(DiskCacheStrategy.DATA)
                )
                .transition(BitmapTransitionOptions.withCrossFade())
                .into(new BitmapImageViewTarget(ivPicture) {
                    @Override
                    public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                        super.onResourceReady(resource, transition);
                        CardView.LayoutParams layoutParams = (CardView.LayoutParams) ivPicture.getLayoutParams();
                        layoutParams.width = resource.getWidth();
                        layoutParams.height = resource.getHeight();
                        ivPicture.setLayoutParams(layoutParams);
                        if (!ivPicture.isShown()) {
                            ivPicture.setVisibility(View.VISIBLE);
                        }
                    }
                });
        holder.itemView.setOnClickListener(v -> {
            if (context instanceof Activity) {
                PictureActivity.startActivity((Activity) context, data.getPublishedTime(), data.getUrl(), ivPicture);
            }
        });
    }

    static class GankEntityViewHolder extends RecyclerView.ViewHolder {

        private final TextView tvDate;
        private final ImageView ivPicture;

        GankEntityViewHolder(@NonNull View itemView) {
            super(itemView);
            tvDate = itemView.findViewById(R.id.gank_data_date);
            ivPicture = itemView.findViewById(R.id.gank_data_picture);
        }

    }

}
