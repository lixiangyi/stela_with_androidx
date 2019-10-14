package com.stela.comics_unlimited.ui.innerBrowse;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.LayoutRes;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.SimpleItemAnimator;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.lxy.baselibs.app.AppConstants;
import com.lxy.baselibs.glide.GlideUtils;
import com.lxy.baselibs.utils.ToastUtils;
import com.stela.analytics.model.Page;
import com.stela.comics_unlimited.Base.BaseFragment;
import com.stela.comics_unlimited.R;
import com.stela.comics_unlimited.data.entity.HomeNewBrowseEntity;
import com.stela.comics_unlimited.data.entity.HomePageEntity;
import com.stela.comics_unlimited.data.entity.ImgEntity;
import com.stela.comics_unlimited.data.entity.SeriesAsset;
import com.stela.comics_unlimited.ui.series.SeriesActivity;
import com.stela.comics_unlimited.util.stela_sdk.StelaAnalyticsUtil;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.Unbinder;


/**
 * @author LXY
 * @date 2019/1/23 22:57
 * @desc
 */
public class InnerBrowseFragment extends BaseFragment<InnerBrowsePresenter> implements InnerBrowseContract.View {


    @BindView(R.id.rv_inner_browse)
    RecyclerView rvInnerBrowse;
    @BindView(R.id.srl_inner_browse)
    SwipeRefreshLayout srlInnerBrowse;
    Unbinder unbinder;
    private ImgEntity mImgEntity;
    private int mPageNo = 1;
    private ArrayList<SeriesAsset> mSeriesAsset = new ArrayList<>();
    private MyAdapter myAdapter;
    private HomePageEntity mHomePageEntity = new HomePageEntity();

    public static InnerBrowseFragment getInstance(ImgEntity tabEntity) {
        InnerBrowseFragment fragment = new InnerBrowseFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable(AppConstants.HOME_BROWN_TAB, tabEntity);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_browse_inner;
    }

    @Override
    protected InnerBrowsePresenter createPresenter() {
        return new InnerBrowsePresenter();
    }

    @Override
    protected void initView() {
        mImgEntity = (ImgEntity) getArguments().getSerializable(AppConstants.HOME_BROWN_TAB);
        initRecyclerview();
//        setAWSPage();
    }

    private void setAWSPage() {
        //埋点
        Page page = new Page();
        page.setPageId(mImgEntity.id);
        page.setPageName(mImgEntity.title);
        StelaAnalyticsUtil.page(page);
    }

    @Override
    protected void initListener() {

    }

    @Override
    protected void initData() {
        srlInnerBrowse.setRefreshing(true);
//        mPresenter.getBrowseList(mPageNo, mImgEntity.id, mImgEntity.name);
        mPresenter.getBrowseListNew(mImgEntity.id);
    }


    @Override
    protected boolean useEventBus() {
        return false;
    }

    @Override
    public void showError(String msg) {
        ToastUtils.showShort(msg);
        if (srlInnerBrowse.isRefreshing()) {
            srlInnerBrowse.setRefreshing(false);
        }
    }


    @Override
    public void showData(HomeNewBrowseEntity homeBrowseEntities) {
//        if (srlInnerBrowse.isRefreshing()) {
////            srlInnerBrowse.setRefreshing(false);
////        }
//        if (mPageNo == 1) {
//            mSeriesAsset.clear();
//            mSeriesAsset = homeBrowseEntities.list;
//            myAdapter.setNewData(mSeriesAsset);
//            myAdapter.setEmptyView(ViewUtil.getEmptyView(this, "消息列表空荡荡的~", false));
//            mPageNo++;
//        } else {
//            List<HomeBrowseEntity> mMessageLists = homeBrowseEntities.list;
//            if (mMessageLists != null) {
//                if (mMessageLists.size() > 0) {
//                    mSeriesAsset.addAll(mMessageLists);
//                    myAdapter.setNewData(mSeriesAsset);
//                    myAdapter.loadMoreComplete();
//                    mPageNo++;
//                } else {
//                    myAdapter.loadMoreEnd();
//                }
//            }
//        }

    }

    @Override
    public void showNewData(ArrayList<SeriesAsset> seriesEntities, HomePageEntity result) {
        if (srlInnerBrowse.isRefreshing()) {
            srlInnerBrowse.setRefreshing(false);
        }
        mSeriesAsset.clear();
        mSeriesAsset = seriesEntities;
        myAdapter.setNewData(mSeriesAsset);
        mHomePageEntity = result;
    }

    private void initRecyclerview() {
        myAdapter = new MyAdapter(R.layout.item_inner_browse, mSeriesAsset);
//        myAdapter.setHasStableIds(true);
        LinearLayoutManager layout = new LinearLayoutManager(getContext());
        rvInnerBrowse.setLayoutManager(layout);
        rvInnerBrowse.setAdapter(myAdapter);
        ((SimpleItemAnimator) rvInnerBrowse.getItemAnimator()).setSupportsChangeAnimations(false);
        srlInnerBrowse.setColorSchemeColors(ContextCompat.getColor(getContext(), R.color.stela_blue));
        srlInnerBrowse.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
//                mPageNo = 1;
                mPresenter.getBrowseListNew(mImgEntity.id);
            }
        });
        myAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {

//                SeriesActivity.startShare(getActivity(), mSeriesAsset.get(position).seriesId,view.findViewById(R.id.iv_browse), mSeriesAsset.get(position).url);
                StelaAnalyticsUtil.stelaAnalyticsSeries("", "browse", mSeriesAsset.get(position).group_id,
                        mSeriesAsset.get(position).group_id, mSeriesAsset.get(position).seriesId, mSeriesAsset.get(position).title);
                SeriesActivity.start(getActivity(), mSeriesAsset.get(position).seriesId);
            }
        });
//        myAdapter.setEnableLoadMore(true);
//        myAdapter.setOnLoadMoreListener(new BaseQuickAdapter.RequestLoadMoreListener() {
//            @Override
//            public void onLoadMoreRequested() {
//                mPresenter.getBrowseList(mPageNo, mImgEntity.id, mImgEntity.name);
//            }
//        }, rvInnerBrowse);
    }


    public class MyAdapter extends BaseQuickAdapter<SeriesAsset, BaseViewHolder> {

        MyAdapter(@LayoutRes int layoutResId, @Nullable List<SeriesAsset> data) {
            super(layoutResId, data);
        }
//        @Override
//        public long getItemId(int position) {
//            return position;
//        }

        @Override
        protected void convert(BaseViewHolder helper, SeriesAsset item) {
            ((TextView) (helper.getView(R.id.tv_browse_title))).setText(item.title);
            ((TextView) (helper.getView(R.id.tv_browse_subtitle))).setText(item.description);
            ImageView mIvBrowse = helper.getView(R.id.iv_browse);
            GlideUtils.loadImageHttps(getActivity(), mIvBrowse, item.url, R.color.stela_blue);
        }
    }

}
