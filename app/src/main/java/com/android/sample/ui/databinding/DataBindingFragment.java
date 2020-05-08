package com.android.sample.ui.databinding;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.sample.R;
import com.android.sample.databinding.FragmentDatabindingBinding;
import com.android.common.entity.BannerInfo;
import com.android.common.utils.SpaceDividerItemDecoration;
import com.android.common.utils.Utility;
import com.bumptech.glide.Glide;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.library.baseAdapters.BR;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;
import cn.bingoogolapple.bgabanner.BGABanner;

public class DataBindingFragment extends Fragment {

    private GankDataViewModel viewModel;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        viewModel = new ViewModelProvider(this).get(GankDataViewModel.class);
        FragmentDatabindingBinding dataBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_databinding, container, false);
        dataBinding.setVariable(BR.gankDataViewModel, viewModel);

        dataBinding.bannerContent.setAdapter((BGABanner.Adapter<ImageView, BannerInfo>) (bgaBanner, itemView, model, position) ->
                Glide.with(DataBindingFragment.this)
                        .load(model == null ? null : model.getImage())
                        .centerCrop()
                        .dontAnimate()
                        .into(itemView)
        );
        dataBinding.bannerContent.setDelegate((BGABanner.Delegate<ImageView, BannerInfo>) (bgaBanner, itemView, model, position) ->
                Toast.makeText(bgaBanner.getContext(), "点击了" + position, Toast.LENGTH_SHORT).show()
        );

        dataBinding.recyclerView.setNestedScrollingEnabled(false);
        dataBinding.recyclerView.setLayoutManager(new StaggeredGridLayoutManager(
                2, StaggeredGridLayoutManager.VERTICAL
        ));
        dataBinding.recyclerView.addItemDecoration(new SpaceDividerItemDecoration(
                Utility.dp2px(getContext(), 6),
                Utility.dp2px(getContext(), 3)
        ));

        viewModel.getGankBanners();
        viewModel.getGankCategoryDatas(1, 20);
        return dataBinding.getRoot();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (viewModel != null) {
            viewModel.onActivityResult(requestCode, resultCode, data);
        }
    }

}
