package com.stela.comics_unlimited.ui.home.adapter;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.SimpleItemAnimator;
import android.text.TextUtils;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.chad.library.adapter.base.util.MultiTypeDelegate;
import com.stela.comics_unlimited.R;
import com.stela.comics_unlimited.data.entity.HomeGroupEntity;
import com.stela.comics_unlimited.ui.home.activity.HomeInnerActivity;
import com.stela.comics_unlimited.widget.InnerRecyclerview;

import java.util.List;

/**
 * @author Administrator
 */
public class HomeAdapter extends BaseQuickAdapter<HomeGroupEntity, BaseViewHolder> {

    public HomeAdapter(@Nullable List<HomeGroupEntity> data) {
        super(data);
        setMultiTypeDelegate(new MultiTypeDelegate<HomeGroupEntity>() {
            @Override
            protected int getItemType(HomeGroupEntity entity) {
                //根据你的实体类来判断布局类型
                return entity.shadowState;
            }
        });
        //Step.2
        getMultiTypeDelegate()
                .registerItemType(HomeGroupEntity.cardYes, R.layout.item_home_card1)
                .registerItemType(HomeGroupEntity.cardNo, R.layout.item_home_card2);
    }

    @Override
    protected void convert(BaseViewHolder helper, HomeGroupEntity item) {
        TextView mTvTitle = helper.getView(R.id.tv_title);
        mTvTitle.setText(item.title);
        TextView mTvSubTitle = helper.getView(R.id.tv_subtitle);
        mTvSubTitle.setText(item.subTitle);
        TextView mTvSeeMore = helper.getView(R.id.tv_see_more);
        mTvSeeMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                HomeInnerActivity.start(mContext, item.seeMoreLinkTo, item.title);
            }
        });
        mTvSeeMore.setVisibility(TextUtils.isEmpty(item.seeMoreLinkTo) ? View.GONE : View.VISIBLE);
        RelativeLayout mRvTitle = helper.getView(R.id.rl_title);
        if (TextUtils.isEmpty(item.title) && TextUtils.isEmpty(item.subTitle) && TextUtils.isEmpty(item.seeMoreLinkTo)) {
            mRvTitle.setVisibility(View.GONE);
        } else {
            mRvTitle.setVisibility(View.VISIBLE);
        }
        HomeInnerAdapter myAdapter = new HomeInnerAdapter(item.childList);
        LinearLayoutManager layout = new LinearLayoutManager(mContext);

//        LinearLayoutManager layout = new LinearLayoutManager(mContext) {
//            @Override
//            public boolean canScrollVertically() {
//                return false;
//            }
//        };
//            layout.setAutoMeasureEnabled(true);
        InnerRecyclerview innerRv = helper.getView(R.id.rv_inner_home);
        ((SimpleItemAnimator) innerRv.getItemAnimator()).setSupportsChangeAnimations(false);
        innerRv.setLayoutManager(layout);
        innerRv.setAdapter(myAdapter);
        innerRv.requestFocus();
        innerRv.setFocusableInTouchMode(false);
        innerRv.setNestedScrollingEnabled(false);
    }

//    @Override
//    public void onViewRecycled(@NonNull BaseViewHolder holder) {
//        super.onViewRecycled(holder);
//        if (holder.getView(R.id.home_cover) instanceof HomeCoverView) {
//            ((HomeCoverView) (holder.getView(R.id.home_cover))).release();
//        }
//    }
}
