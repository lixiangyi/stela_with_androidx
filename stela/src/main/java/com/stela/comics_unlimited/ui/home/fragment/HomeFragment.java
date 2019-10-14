package com.stela.comics_unlimited.ui.home.fragment;

import android.os.Bundle;
import androidx.core.content.ContextCompat;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.lxy.baselibs.app.AppConstants;
import com.stela.analytics.model.Page;
import com.stela.comics_unlimited.Base.BaseFragment;
import com.lxy.baselibs.rx.RxBus;
import com.lxy.baselibs.utils.ToastUtils;
import com.stela.comics_unlimited.R;
import com.stela.comics_unlimited.data.entity.HomeGroupEntity;
import com.stela.comics_unlimited.data.entity.HomePageEntity;
import com.stela.comics_unlimited.event.HomeNotifyEvent;
import com.stela.comics_unlimited.ui.home.adapter.HomeAdapter;
import com.stela.comics_unlimited.ui.home.contract.HomeContract;
import com.stela.comics_unlimited.ui.home.presenter.HomePresenter;
import com.stela.comics_unlimited.util.stela_sdk.StelaAnalyticsUtil;
import com.stela.comics_unlimited.widget.HomeCoverView;

import java.util.ArrayList;

import butterknife.BindView;
import io.reactivex.functions.Consumer;

/**
 * @author LXY
 * @date 2019/1/23 22:57
 * @desc 首页
 */
public class HomeFragment extends BaseFragment<HomePresenter> implements HomeContract.View {


    @BindView(R.id.rv_home)
    RecyclerView rvHome;
    @BindView(R.id.srl_home)
    SwipeRefreshLayout srlHome;
    private HomeAdapter myAdapter;
    private ArrayList<HomeGroupEntity> mPageCompents = new ArrayList<>();
    private String id = "";

    public static HomeFragment getInstance(String id) {
        HomeFragment fragment = new HomeFragment();
        Bundle bundle = new Bundle();
        bundle.putString(AppConstants.ID, id);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_home;
    }

    @Override
    protected HomePresenter createPresenter() {
        return new HomePresenter();
    }


    @Override
    protected void initView() {
        id = getArguments().getString(AppConstants.ID);
        srlHome.setRefreshing(true);
        initRecyclerview();
        mPresenter.HomeData(id);
    }

    @Override
    protected void initListener() {
        //界面更新
        mPresenter.addDispose(RxBus.getDefault().toObservable(HomeNotifyEvent.class)
                .subscribe(new Consumer<HomeNotifyEvent>() {
                    @Override
                    public void accept(HomeNotifyEvent continueReadEvent) throws Exception {
                        if (continueReadEvent != null) {
                            // 刷新
                            mPresenter.HomeData(id);
                        }
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {

                    }
                }));
    }

    @Override
    protected void initData() {

    }


    private void initRecyclerview() {
        myAdapter = new HomeAdapter(mPageCompents);
        LinearLayoutManager layout = new LinearLayoutManager(getContext());
        rvHome.setLayoutManager(layout);
        rvHome.setNestedScrollingEnabled(false);
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
        srlHome.setColorSchemeColors(ContextCompat.getColor(getContext(), R.color.stela_blue));
        srlHome.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mPresenter.HomeData("");
            }
        });
    }


    @Override
    protected boolean useEventBus() {
        return false;
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
    public void showData(HomePageEntity homePageEntitie) {
        if (srlHome.isRefreshing()) {
            srlHome.setRefreshing(false);
        }
        myAdapter.setNewData(homePageEntitie.childList);
        //埋点
        Page page = new Page();
        page.setPageId(homePageEntitie.id);
        page.setPageName("home");
        StelaAnalyticsUtil.page(page);
    }
}
