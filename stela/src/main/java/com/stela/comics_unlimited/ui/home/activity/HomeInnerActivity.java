package com.stela.comics_unlimited.ui.home.activity;

import android.content.Context;
import android.content.Intent;
import androidx.core.content.ContextCompat;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.lxy.baselibs.app.AppConstants;
import com.lxy.baselibs.utils.ToastUtils;
import com.stela.analytics.model.Page;
import com.stela.comics_unlimited.Base.BaseMvpActivity;
import com.stela.comics_unlimited.R;
import com.stela.comics_unlimited.data.entity.HomePageEntity;
import com.stela.comics_unlimited.ui.home.adapter.HomeAdapter;
import com.stela.comics_unlimited.ui.home.contract.HomeContract;
import com.stela.comics_unlimited.ui.home.presenter.HomePresenter;
import com.stela.comics_unlimited.util.stela_sdk.StelaAnalyticsUtil;
import com.stela.comics_unlimited.widget.HomeCoverView;

import butterknife.BindView;

public class HomeInnerActivity extends BaseMvpActivity<HomePresenter> implements HomeContract.View {

    @BindView(R.id.rv_home)
    RecyclerView rvHome;
    @BindView(R.id.srl_home)
    SwipeRefreshLayout srlHome;
    private String mPageId;
    private String mTitle;
    private HomeAdapter myAdapter;

    public static void start(Context context, String pageid, String title) {
        Intent starter = new Intent(context, HomeInnerActivity.class);
        starter.putExtra(AppConstants.HOME_PAGE_ID, pageid);
        starter.putExtra(AppConstants.HOME_PAGE_TITLE, title);
        context.startActivity(starter);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_home;
    }

    @Override
    protected void getIntent(Intent intent) {
        mPageId = intent.getStringExtra(AppConstants.HOME_PAGE_ID);
        mTitle = intent.getStringExtra(AppConstants.HOME_PAGE_TITLE);
    }

    @Override
    protected void initView() {
        showBack();
        setTitle(mTitle);
        setAWSPage();
    }

    private void setAWSPage() {
        //埋点
        Page page = new Page();
        page.setPageId(mPageId);
        page.setPageName(mTitle);
        StelaAnalyticsUtil.page(page);
    }


    @Override
    protected void initListener() {

    }

    @Override
    protected void initData() {
        srlHome.setRefreshing(true);
        mPresenter.HomeData(mPageId);
        srlHome.setColorSchemeColors(ContextCompat.getColor(this, R.color.stela_blue));
        srlHome.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mPresenter.HomeData(mPageId);
            }
        });
    }

    @Override
    protected HomePresenter createPresenter() {
        return new HomePresenter();
    }


    @Override
    public void showError(String msg) {
        if (srlHome.isRefreshing()) {
            srlHome.setRefreshing(false);
        }
        ToastUtils.showShort(msg);
        myAdapter.setEmptyView(R.layout.empty_normal, rvHome);
    }


    @Override
    public void showData(HomePageEntity homePageEntity) {
        if (srlHome.isRefreshing()) {
            srlHome.setRefreshing(false);
        }
        myAdapter = new HomeAdapter(homePageEntity.childList);
        LinearLayoutManager layout = new LinearLayoutManager(this);
        rvHome.setNestedScrollingEnabled(false);
        rvHome.setLayoutManager(layout);
        rvHome.setAdapter(myAdapter);
        rvHome.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                LinearLayoutManager layout = (LinearLayoutManager) rvHome.getLayoutManager();
                final int firstVisible = layout.findFirstVisibleItemPosition();
                final int lastVisible = layout.findLastVisibleItemPosition();
                for (int i = firstVisible; i <= lastVisible; i++) {
                    RecyclerView.ViewHolder vh = rvHome.findViewHolderForAdapterPosition(i);
                    if (vh != null && vh.itemView != null) {
                        RecyclerView innerRecyclerview = (RecyclerView) (vh.itemView.findViewById(R.id.rv_inner_home));
                        if (innerRecyclerview != null) {
                            for (int k = 0; k < innerRecyclerview.getChildCount(); k++) {
                                RecyclerView.ViewHolder vhInner = innerRecyclerview.findViewHolderForAdapterPosition(k);
                                if (vhInner != null && vhInner.itemView != null) {
                                    if (vhInner.itemView.findViewById(R.id.home_cover_a) instanceof HomeCoverView) {
                                        ((HomeCoverView) (vhInner.itemView.findViewById(R.id.home_cover_a))).updateScroll(recyclerView);
                                    }
                                    if (vhInner.itemView.findViewById(R.id.hiv_d1) instanceof HomeCoverView) {
                                        ((HomeCoverView) (vhInner.itemView.findViewById(R.id.hiv_d1))).updateScroll(recyclerView);
                                    }
                                    if (vhInner.itemView.findViewById(R.id.hiv_e3) instanceof HomeCoverView) {
                                        ((HomeCoverView) (vhInner.itemView.findViewById(R.id.hiv_e3))).updateScroll(recyclerView);
                                    }
                                }
                            }
                        }
                    }
                }
            }
        });
    }
}
