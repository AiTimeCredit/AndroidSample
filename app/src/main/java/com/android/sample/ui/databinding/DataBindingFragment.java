package com.android.sample.ui.databinding;

import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.common.base.BaseFragment;
import com.android.common.entity.BannerInfo;
import com.android.common.utils.SpaceDividerItemDecoration;
import com.android.common.utils.Utility;
import com.android.sample.R;
import com.android.sample.databinding.FragmentDatabindingBinding;
import com.bumptech.glide.Glide;

import androidx.annotation.NonNull;
import androidx.databinding.library.baseAdapters.BR;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;
import cn.bingoogolapple.bgabanner.BGABanner;

public class DataBindingFragment extends BaseFragment<FragmentDatabindingBinding, GankDataViewModel> {

    @Override
    public int getContentLayoutId() {
        return R.layout.fragment_databinding;
    }

    @Override
    public int getViewModelVariableId() {
        return BR.gankDataViewModel;
    }

    @Override
    public void initViews(@NonNull View rootView) {
        getDataBinding().bannerContent.setAdapter((BGABanner.Adapter<ImageView, BannerInfo>) (bgaBanner, itemView, model, position) ->
                Glide.with(DataBindingFragment.this)
                        .load(model == null ? null : model.getImage())
                        .centerCrop()
                        .dontAnimate()
                        .into(itemView)
        );
        getDataBinding().bannerContent.setDelegate((BGABanner.Delegate<ImageView, BannerInfo>) (bgaBanner, itemView, model, position) ->
                Toast.makeText(bgaBanner.getContext(), "点击了" + position, Toast.LENGTH_SHORT).show()
        );

        getDataBinding().recyclerView.setNestedScrollingEnabled(false);
        getDataBinding().recyclerView.setLayoutManager(new StaggeredGridLayoutManager(
                2, StaggeredGridLayoutManager.VERTICAL
        ));
        getDataBinding().recyclerView.addItemDecoration(new SpaceDividerItemDecoration(
                Utility.dp2px(getContext(), 6),
                Utility.dp2px(getContext(), 3)
        ));

        loadData();
    }

    @Override
    public void loadData() {
        super.loadData();
        getViewModel().getGankBanners();
        getViewModel().getGankCategoryDatas(1, 20);
    }

}
