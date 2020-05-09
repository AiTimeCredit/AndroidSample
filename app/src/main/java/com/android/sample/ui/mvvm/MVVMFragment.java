package com.android.sample.ui.mvvm;

import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.common.base.BaseFragment;
import com.android.common.entity.BannerInfo;
import com.android.common.utils.SpaceDividerItemDecoration;
import com.android.common.utils.Utility;
import com.android.sample.R;
import com.android.sample.adapter.GankEntityAdapter;
import com.bumptech.glide.Glide;

import androidx.annotation.NonNull;
import androidx.databinding.ViewDataBinding;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;
import cn.bingoogolapple.bgabanner.BGABanner;

public class MVVMFragment extends BaseFragment<ViewDataBinding, GankDataViewModel> {

    @Override
    public int getContentLayoutId() {
        return R.layout.fragment_mvvm;
    }

    @Override
    public int getViewModelVariableId() {
        return 0;
    }

    @Override
    public void initViews(@NonNull View rootView) {
        BGABanner banner = rootView.findViewById(R.id.banner_content);
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
        RecyclerView recyclerView = rootView.findViewById(R.id.recycler_view);
        recyclerView.setNestedScrollingEnabled(false);
        recyclerView.setLayoutManager(new StaggeredGridLayoutManager(
                2, StaggeredGridLayoutManager.VERTICAL
        ));
        recyclerView.addItemDecoration(new SpaceDividerItemDecoration(
                Utility.dp2px(getContext(), 6),
                Utility.dp2px(getContext(), 3)
        ));
        recyclerView.setAdapter(adapter);

        getViewModel().banners.observe(getViewLifecycleOwner(), datas -> banner.setData(datas, null));
        getViewModel().datas.observe(getViewLifecycleOwner(), adapter::refresh);

        loadData();
    }

    @Override
    public void loadData() {
        super.loadData();
        getViewModel().getGankBanners();
        getViewModel().getGankCategoryDatas(1, 20);
    }

}
