package com.android.sample.ui.mvp;

import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.common.base.BaseFragment;
import com.android.common.entity.BannerInfo;
import com.android.common.entity.GankEntity;
import com.android.common.http.ResponseEntity;
import com.android.common.utils.SpaceDividerItemDecoration;
import com.android.common.utils.Utility;
import com.android.sample.R;
import com.android.sample.adapter.GankEntityAdapter;
import com.bumptech.glide.Glide;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;
import cn.bingoogolapple.bgabanner.BGABanner;

public class MVPFragment extends BaseFragment implements GankDataContract.View {

    private BGABanner banner;
    private GankEntityAdapter mAdapter;

    @Override
    public int getContentLayoutId() {
        return R.layout.fragment_mvp;
    }

    @Override
    public int getViewModelVariableId() {
        return 0;
    }

    @Override
    public void initViews(@NonNull View rootView) {
        GankDataPresenter presenter = new GankDataPresenter();
        presenter.attachView(this);

        banner = rootView.findViewById(R.id.banner_content);
        banner.setAdapter((BGABanner.Adapter<ImageView, BannerInfo>) (banner, itemView, model, position) ->
                Glide.with(MVPFragment.this)
                        .load(model == null ? null : model.getImage())
                        .centerCrop()
                        .dontAnimate()
                        .into(itemView)
        );
        banner.setDelegate((BGABanner.Delegate<ImageView, BannerInfo>) (banner, itemView, model, position) ->
                Toast.makeText(banner.getContext(), "点击了" + position, Toast.LENGTH_SHORT).show()
        );

        mAdapter = new GankEntityAdapter();
        RecyclerView recyclerView = rootView.findViewById(R.id.recycler_view);
        recyclerView.setNestedScrollingEnabled(false);
        recyclerView.setLayoutManager(new StaggeredGridLayoutManager(
                2, StaggeredGridLayoutManager.VERTICAL
        ));
        recyclerView.addItemDecoration(new SpaceDividerItemDecoration(
                Utility.dp2px(getContext(), 6),
                Utility.dp2px(getContext(), 3)
        ));
        recyclerView.setAdapter(mAdapter);

        presenter.getGankBanners();
        presenter.getGankCategoryDatas(1, 20);
    }

    @Override
    public void handleGankBannersResult(ResponseEntity<List<BannerInfo>> entity) {
        banner.setData(entity.getData(), null);
    }

    @Override
    public void handleGankCategoryDatasResult(ResponseEntity<List<GankEntity>> entity) {
        mAdapter.refresh(entity.getData());
    }

}
