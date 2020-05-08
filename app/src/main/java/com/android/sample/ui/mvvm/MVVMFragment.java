package com.android.sample.ui.mvvm;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.sample.R;
import com.android.sample.adapter.GankEntityAdapter;
import com.android.common.entity.BannerInfo;
import com.android.common.utils.SpaceDividerItemDecoration;
import com.android.common.utils.Utility;
import com.bumptech.glide.Glide;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;
import cn.bingoogolapple.bgabanner.BGABanner;

public class MVVMFragment extends Fragment {

    private GankDataViewModel viewModel;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        viewModel = new ViewModelProvider(this).get(GankDataViewModel.class);
        View root = inflater.inflate(R.layout.fragment_mvvm, container, false);
        BGABanner banner = root.findViewById(R.id.banner_content);
        banner.setAdapter((BGABanner.Adapter<ImageView, BannerInfo>) (bgaBanner, itemView, model, position) ->
                Glide.with(MVVMFragment.this)
                        .load(model == null ? null : model.getImage())
                        .centerCrop()
                        .dontAnimate()
                        .into(itemView)
        );
        banner.setDelegate((BGABanner.Delegate<ImageView, BannerInfo>) (bgaBanner, itemView, model, position) ->
                Toast.makeText(bgaBanner.getContext(), "点击了" + position, Toast.LENGTH_SHORT).show()
        );

        GankEntityAdapter adapter = new GankEntityAdapter();
        RecyclerView recyclerView = root.findViewById(R.id.recycler_view);
        recyclerView.setNestedScrollingEnabled(false);
        recyclerView.setLayoutManager(new StaggeredGridLayoutManager(
                2, StaggeredGridLayoutManager.VERTICAL
        ));
        recyclerView.addItemDecoration(new SpaceDividerItemDecoration(
                Utility.dp2px(getContext(), 6),
                Utility.dp2px(getContext(), 3)
        ));
        recyclerView.setAdapter(adapter);

        viewModel.banners.observe(getViewLifecycleOwner(), datas -> banner.setData(datas, null));
        viewModel.datas.observe(getViewLifecycleOwner(), adapter::refresh);

        viewModel.getGankBanners();
        viewModel.getGankCategoryDatas(1, 20);
        return root;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (viewModel != null) {
            viewModel.onActivityResult(requestCode, resultCode, data);
        }
    }

}
