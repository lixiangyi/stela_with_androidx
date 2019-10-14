package com.stela.comics_unlimited.ui.home.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.chad.library.adapter.base.util.MultiTypeDelegate;
import com.lxy.baselibs.glide.GlideUtils;
import com.stela.comics_unlimited.R;
import com.stela.comics_unlimited.data.entity.HomeItemEntity;
import com.stela.comics_unlimited.data.entity.SeriesAsset;
import com.stela.comics_unlimited.ui.series.SeriesActivity;
import com.stela.comics_unlimited.util.banner.BannerImageLoaderHome;
import com.stela.comics_unlimited.util.stela_sdk.StelaAnalyticsUtil;
import com.stela.comics_unlimited.widget.HomeBanner;
import com.stela.comics_unlimited.widget.HomeCoverView;
import com.stela.comics_unlimited.widget.HomeImageView;
import com.youth.banner.Banner;
import com.youth.banner.BannerConfig;
import com.youth.banner.Transformer;
import com.youth.banner.listener.OnBannerListener;

import java.util.ArrayList;
import java.util.List;

import static com.lxy.baselibs.app.BaseApplication.getContext;

public class HomeInnerAdapter extends BaseQuickAdapter<HomeItemEntity, BaseViewHolder> {
    private int[] b = new int[]{R.id.hiv_b1, R.id.hiv_b2, R.id.hiv_b3};

    HomeInnerAdapter(@Nullable List<HomeItemEntity> data) {
        super(data);
        setMultiTypeDelegate(new MultiTypeDelegate<HomeItemEntity>() {
            @Override
            protected int getItemType(HomeItemEntity entity) {
                //根据你的实体类来判断布局类型
                return entity.rowType;
            }
        });
        //Step.2
        getMultiTypeDelegate()
                .registerItemType(HomeItemEntity.a, R.layout.item_home_a)
                .registerItemType(HomeItemEntity.b, R.layout.item_home_b)
                .registerItemType(HomeItemEntity.c, R.layout.item_home_c)
                .registerItemType(HomeItemEntity.d, R.layout.item_home_d)
                .registerItemType(HomeItemEntity.e, R.layout.item_home_e)
                .registerItemType(HomeItemEntity.f, R.layout.item_home_f)
                .registerItemType(HomeItemEntity.g, R.layout.item_home_g)
                .registerItemType(HomeItemEntity.h, R.layout.item_home_h)
                .registerItemType(HomeItemEntity.i, R.layout.item_home_i);
    }

    @Override
    protected void convert(BaseViewHolder helper, HomeItemEntity item) {
        if (item.rowAssets != null && item.rowAssets.size() > 0) {
            //Step.3
            switch (helper.getItemViewType()) {
                case HomeItemEntity.a:
                    HomeCoverView homeCoverView = helper.getView(R.id.home_cover_a);
                    initCover(homeCoverView, item.rowAssets.get(0));
                    break;
                case HomeItemEntity.b:
                    if (item.rowAssets.size() > 0 && item.rowAssets.size() < 4) {
                        for (int i = 0; i < item.rowAssets.size(); i++) {
                            if (item.rowAssets.get(i).size() > 0 && item.rowAssets.get(i).get(0) != null) {
                                initImg(helper.getView(b[i]), item.rowAssets.get(i).get(0), false);
                            }
                        }
                    } else {
                        for (int i = 0; i < b.length; i++) {
                            if (item.rowAssets.get(i).size() > 0 && item.rowAssets.get(i).get(0) != null) {
                                initImg(helper.getView(b[i]), item.rowAssets.get(i).get(0), false);
                            }
                        }
                    }
                    break;
                case HomeItemEntity.c:
                    if (item.rowAssets.size() > 0 && item.rowAssets.get(0).size() > 0 && item.rowAssets.get(0).get(0) != null) {
                        initImg(helper.getView(R.id.hiv_c1), item.rowAssets.get(0).get(0), false);
                    }
                    if (item.rowAssets.size() > 1 && item.rowAssets.get(1).size() > 0 && item.rowAssets.get(1).get(0) != null) {
                        initImg(helper.getView(R.id.hiv_c2), item.rowAssets.get(1).get(0), false);
                    }
                    break;
                case HomeItemEntity.d:
                    if (item.rowAssets.size() > 0 && item.rowAssets.get(0).size() > 0 && item.rowAssets.get(0).get(0) != null) {
                        HomeCoverView homeCoverViewD = helper.getView(R.id.hiv_d1);
                        initCover(homeCoverViewD, item.rowAssets.get(0));
                    }
                    if (item.rowAssets.size() > 1 && item.rowAssets.get(1).size() > 0 && item.rowAssets.get(1).get(0) != null) {
                        initImg(helper.getView(R.id.hiv_d2), item.rowAssets.get(1).get(0), false);
                    }
                    if (item.rowAssets.size() > 2 && item.rowAssets.get(2).size() > 0 && item.rowAssets.get(2).get(0) != null) {
                        initImg(helper.getView(R.id.hiv_d3), item.rowAssets.get(2).get(0), false);
                    }
                    break;
                case HomeItemEntity.e:
                    if (item.rowAssets.size() > 0 && item.rowAssets.get(0).size() > 0 && item.rowAssets.get(0).get(0) != null) {
                        initImg(helper.getView(R.id.hiv_e1), item.rowAssets.get(0).get(0), false);
                    }
                    if (item.rowAssets.size() > 1 && item.rowAssets.get(1).size() > 0 && item.rowAssets.get(1).get(0) != null) {
                        HomeCoverView homeCoverViewE = helper.getView(R.id.hiv_e3);
                        initCover(homeCoverViewE, item.rowAssets.get(1));
                    }
                    if (item.rowAssets.size() > 2 && item.rowAssets.get(2).size() > 0 && item.rowAssets.get(2).get(0) != null) {
                        initImg(helper.getView(R.id.hiv_e2), item.rowAssets.get(2).get(0), false);
                    }

                    // do something
                    break;
                case HomeItemEntity.f:
                    if (item.rowAssets.get(0).size() > 0 && item.rowAssets.get(0).get(0) != null) {
                        initImg(helper.getView(R.id.hiv_f), item.rowAssets.get(0).get(0), true);
                    }
                    break;
                case HomeItemEntity.g:
//                    HomeBanner bannerg = helper.getView(R.id.banner_g);
//                    initBannerNew(bannerg, item);
                    // do something
                    Banner bannerg = helper.getView(R.id.banner_g);
                    initBanner(bannerg, item);
                    break;
                case HomeItemEntity.h:
                    // do something
                    Banner bannerh = helper.getView(R.id.banner_h);
                    initBanner(bannerh, item);
                    break;
                case HomeItemEntity.i:
                    // do something
                    LinearLayout mLinearLayout = helper.getView(R.id.ll_home_i);
                    helper.addOnClickListener(R.id.ll_home_i);
                    mLinearLayout.removeAllViews();
                    if (item.rowAssets != null && item.rowAssets.size() > 0) {
                        mLinearLayout.setVisibility(View.VISIBLE);
                        for (int i = 0; i < item.rowAssets.size(); i++) {
                            if (item.rowAssets.get(i) != null && item.rowAssets.get(i).size() > 0 && item.rowAssets.get(i).get(0) != null) {
                                View relativeLayout = LayoutInflater.from(getContext()).inflate(R.layout.item_inner_browse, null, false);
                                ImageView imageView = relativeLayout.findViewById(R.id.iv_browse);
                                GlideUtils.loadImageHttps(getContext(), imageView, item.rowAssets.get(i).get(0).url, R.color.stela_blue);
                                int finalI = i;
                                relativeLayout.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
//                                        SeriesActivity.startShare(mContext, item.rowAssets.get(finalI).get(0).seriesId, imageView , item.rowAssets.get(finalI).get(0).url);
                                        SeriesActivity.start(mContext, item.rowAssets.get(finalI).get(0).seriesId);
                                        StelaAnalyticsUtil.    stelaAnalyticsSeries(item.rowAssets.get(finalI).get(0).page_id, item.rowAssets.get(finalI).get(0).page_name,
                                                item.rowAssets.get(finalI).get(0).group_id + "",item.rowAssets.get(finalI).get(0).group_name + "",
                                                item.rowAssets.get(finalI).get(0).seriesId, item.rowAssets.get(finalI).get(0).title);
                                    }
                                });
                                TextView mTvTitle = relativeLayout.findViewById(R.id.tv_browse_title);
                                mTvTitle.setText(item.rowAssets.get(i).get(0).title);
                                TextView mTvSubTitle = relativeLayout.findViewById(R.id.tv_browse_subtitle);
                                mTvSubTitle.setText(item.rowAssets.get(i).get(0).description);
                                mLinearLayout.addView(relativeLayout);
                            }
                        }
                    } else {
                        mLinearLayout.setVisibility(View.GONE);
                    }
                    break;
                default:

                    break;
            }

        }
    }



    private void initCover(HomeCoverView homeCoverView, ArrayList<SeriesAsset> seriesAssets) {
        // 图片重新刷新问题
        homeCoverView.setRowAssets(seriesAssets);
        homeCoverView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (seriesAssets.size() > 0 && seriesAssets.get(0) != null) {
                 StelaAnalyticsUtil.   stelaAnalyticsSeries(seriesAssets.get(0).page_id, seriesAssets.get(0).page_name,
                            seriesAssets.get(0).group_id + "", seriesAssets.get(0).group_name + "",seriesAssets.get(0).seriesId, seriesAssets.get(0).title);
//                    SeriesActivity.startShare(mContext, seriesAssets.get(0).seriesId,homeCoverView.getImageView(),seriesAssets.get(0).url);
                    SeriesActivity.start(mContext, seriesAssets.get(0).seriesId);
                }
            }
        });

    }

    private void initImg(HomeImageView view, SeriesAsset item, boolean isbig) {
        GlideUtils.loadImageHttps(mContext, view.getImageView(), item.url, R.color.stela_blue);
        view.setLock(item.isLock == 1, isbig);
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //  2019/4/16 id修改
//                SeriesActivity.startShare(mContext, item.seriesId, view.getImageView(), item.url);
                SeriesActivity.start(mContext, item.seriesId);
                StelaAnalyticsUtil.   stelaAnalyticsSeries(item.page_id, item.page_name,
                        item.group_id , item.group_name , item.seriesId, item.title);
            }
        });
    }

//    @Override
//    public void onViewRecycled(@NonNull BaseViewHolder holder) {
//        super.onViewRecycled(holder);
//        if (holder.getView(R.id.home_cover) instanceof HomeCoverView) {
//            ((HomeCoverView) (holder.getView(R.id.home_cover))).release();
//        }
//    }

    private void initBannerNew(HomeBanner banner, HomeItemEntity item) {
        banner.setData(item.rowAssets);
        banner.setOnBannerListener(new OnBannerListener() {
            @Override
            public void OnBannerClick(int position) {
                // 2019/4/16 id修改
                SeriesActivity.start(mContext, item.rowAssets.get(position).get(0).seriesId);
            }
        });
    }

    private void initBanner(Banner banner, HomeItemEntity item) {
        //h 修改比例
        if (item.rowType == HomeItemEntity.h) {
            for (int i = 0; i < item.rowAssets.size(); i++) {
                for (int k = 0; k < item.rowAssets.get(i).size(); k++) {
                    item.rowAssets.get(i).get(k).scale = 0.71f;
                }
            }
        }
        //设置banner样式
        banner.setBannerStyle(BannerConfig.CIRCLE_INDICATOR);
        //设置图片加载器
        banner.setImageLoader(new BannerImageLoaderHome());
        //设置图片集合
        banner.setImages(item.rowAssets);
        //设置banner动画效果
        banner.setBannerAnimation(Transformer.Default);
        //设置自动轮播，默认为true
        banner.isAutoPlay(true);
        banner.setDelayTime(4000);
        //设置指示器位置（当banner模式中有指示器时）
        banner.setIndicatorGravity(BannerConfig.CENTER);
        //banner设置方法全部调用完毕时最后调用
        banner.start();
        banner.setOnBannerListener(new OnBannerListener() {
            @Override
            public void OnBannerClick(int position) {
                //  id修改
                if (item.rowAssets.get(position).size() > 0 && item.rowAssets.get(position).get(0) != null) {
                    SeriesActivity.start(mContext, item.rowAssets.get(position).get(0).seriesId);
                    StelaAnalyticsUtil.  stelaAnalyticsSeries(item.rowAssets.get(position).get(0).page_id, item.rowAssets.get(position).get(0).page_name,
                            item.rowAssets.get(position).get(0).group_id + "",item.rowAssets.get(position).get(0).group_name + "", item.rowAssets.get(position).get(0).seriesId, item.rowAssets.get(position).get(0).title);
                }
            }
        });
    }
}