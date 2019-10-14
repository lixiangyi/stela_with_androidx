package com.stela.comics_unlimited.ui.mylibrary.innerLibrary;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.example.zhouwei.library.CustomPopWindow;
import com.lxy.baselibs.app.AppConstants;
import com.lxy.baselibs.rx.RxBus;
import com.lxy.baselibs.utils.CommonUtils;
import com.lxy.baselibs.utils.DisplayUtils;
import com.lxy.baselibs.utils.ToastUtils;
import com.stela.analytics.model.Page;
import com.stela.analytics.model.Series;
import com.stela.comics_unlimited.Base.BaseFragment;
import com.stela.comics_unlimited.R;
import com.stela.comics_unlimited.data.entity.PageEntity;
import com.stela.comics_unlimited.data.entity.SeriesEntity;
import com.stela.comics_unlimited.event.MainPositionNotify;
import com.stela.comics_unlimited.event.RecentReadNotifyEvent;
import com.stela.comics_unlimited.ui.collection.adapter.CollectionAllAdapter;
import com.stela.comics_unlimited.ui.series.SeriesActivity;
import com.stela.comics_unlimited.util.stela_sdk.StelaAnalyticsUtil;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import io.reactivex.functions.Consumer;


/**
 * @author LXY
 * @date 2019/1/23 22:57
 * @desc
 */
public class RecentReadFragment extends BaseFragment<RecentReadPresenter> implements RecentReadContract.View {

    @BindView(R.id.rv_recent_read)
    RecyclerView rvRecentRead;
    @BindView(R.id.srl_recent_read)
    SwipeRefreshLayout srlRecentRead;
    private int mPageNo = 1;
    private List<SeriesEntity> mSeriesEntity = new ArrayList<>();
    private CollectionAllAdapter myAdapter;
    private View emptyView;
    private boolean isLastPage = false;

    public static RecentReadFragment getInstance(String title) {
        RecentReadFragment fragment = new RecentReadFragment();
        Bundle bundle = new Bundle();
        bundle.putString(AppConstants.TITLE, title);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_recent_read;
    }

    @Override
    protected RecentReadPresenter createPresenter() {
        return new RecentReadPresenter();
    }

    @Override
    protected void initView() {
        initEmptyView("You haven't read any stories yet");
        initRecyclerview();
    }

    private void setAWSPage() {
        //埋点
        Page page = new Page();
        page.setPageName("recently_read");
        StelaAnalyticsUtil.page(page);
    }

    @Override
    protected void LazyloadData() {
        setAWSPage();
    }

    @Override
    protected void initListener() {
        mPresenter.addDispose(RxBus.getDefault().toObservable(RecentReadNotifyEvent.class)
                .subscribe(new Consumer<RecentReadNotifyEvent>() {
                    @Override
                    public void accept(RecentReadNotifyEvent collectionNotifyEvent) throws Exception {
                        if (collectionNotifyEvent != null) {
                            mPageNo = 1;
                            mPresenter.requestData(mPageNo);
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
        srlRecentRead.setRefreshing(true);
        mPresenter.requestData(mPageNo);
    }

    @Override
    protected boolean useEventBus() {
        return false;
    }

    @Override
    public void showError(String msg) {
        if (srlRecentRead.isRefreshing()) {
            srlRecentRead.setRefreshing(false);
        }
        ToastUtils.showShort(msg);
    }


    @Override
    public void showData(PageEntity<SeriesEntity> pageEntity) {
        if (srlRecentRead.isRefreshing()) {
            srlRecentRead.setRefreshing(false);
        }
        isLastPage = pageEntity.isLastPage;
        if (mPageNo == 1) {
            mSeriesEntity.clear();
            mSeriesEntity = pageEntity.list;
            myAdapter.setNewData(mSeriesEntity);
//            myAdapter.setEmptyView(ViewUtil.getEmptyView(this, "消息列表空荡荡的~", false));
            mPageNo++;
        } else {
            List<SeriesEntity> seriesEntities1 = pageEntity.list;
            if (seriesEntities1 != null) {
                if (seriesEntities1.size() > 0) {
                    mSeriesEntity.addAll(seriesEntities1);
                    myAdapter.setNewData(mSeriesEntity);
                    myAdapter.loadMoreComplete();
                    mPageNo++;
                } else {
                    myAdapter.loadMoreEnd(true);
                }
            }
        }

    }

    @Override
    public void deleteSuccess(String s, int position) {
        myAdapter.getData().remove(position);
        myAdapter.notifyItemRemoved(position);
    }

    private void initRecyclerview() {
        myAdapter = new CollectionAllAdapter(R.layout.item_inner_browse, mSeriesEntity);
        LinearLayoutManager layout = new LinearLayoutManager(getContext());
        rvRecentRead.setLayoutManager(layout);
        rvRecentRead.setAdapter(myAdapter);
        srlRecentRead.setColorSchemeColors(ContextCompat.getColor(getContext(), R.color.stela_blue));
        srlRecentRead.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mPageNo = 1;
                mPresenter.requestData(mPageNo);
            }
        });
        myAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                stelaAnalyticsSeries(mSeriesEntity.get(position).id, mSeriesEntity.get(position).title);
                SeriesActivity.start(getActivity(), mSeriesEntity.get(position).id);
            }
        });
        myAdapter.setOnItemLongClickListener(new BaseQuickAdapter.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(BaseQuickAdapter adapter, View view, int position) {
                showPop(view, position);
                return true;
            }
        });
        myAdapter.setEnableLoadMore(true);
        myAdapter.setOnLoadMoreListener(new BaseQuickAdapter.RequestLoadMoreListener() {
            @Override
            public void onLoadMoreRequested() {
                if (!isLastPage) {
                    mPresenter.requestData(mPageNo);
                } else {
                    myAdapter.loadMoreEnd(true);
                }
            }
        }, rvRecentRead);
        myAdapter.setEmptyView(emptyView);
    }

    private void stelaAnalyticsSeries(String seriesId, String title) {
        //埋点
        Series series = new Series();
        series.setPageId("");
        series.setGroupId("");
        series.setGroupName("");
        series.setPageName("recently_read");
        series.setSeriesId(seriesId);
        series.setSeriesName(title);
        StelaAnalyticsUtil.click("series_view");
        StelaAnalyticsUtil.series(series);
    }

    private void initEmptyView(String msg) {
        emptyView = LayoutInflater.from(getContext()).inflate(R.layout.empty_refresh, null);
        emptyView.findViewById(R.id.btn_refresh).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RxBus.getDefault().post(new MainPositionNotify(0));
            }
        });
        ((TextView) (emptyView.findViewById(R.id.tv_empty_msg))).setText(msg);
        ((TextView) (emptyView.findViewById(R.id.btn_refresh))).setText("Discover Stories Now");
    }

    private void showPop(View v, int position) {
        View contentView = LayoutInflater.from(getActivity()).inflate(R.layout.pop_delete_layout, null);
        CustomPopWindow popWindow = new CustomPopWindow.PopupWindowBuilder(getActivity())
                .setView(contentView)
                .setFocusable(false)
                .create();
        popWindow.showAsDropDown(v, DisplayUtils.getScreenWidth(getActivity()) / 2 - 100, -(v.getHeight() + popWindow.getHeight()) + CommonUtils.dp2px(30));
        contentView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SeriesEntity seriesEntity = myAdapter.getData().get(position);
                mPresenter.delete(seriesEntity.id, position);
                popWindow.dissmiss();
            }
        });
    }

}
