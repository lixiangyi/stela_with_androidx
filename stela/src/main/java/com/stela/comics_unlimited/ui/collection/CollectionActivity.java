package com.stela.comics_unlimited.ui.collection;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;

import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.example.zhouwei.library.CustomPopWindow;
import com.lxy.baselibs.app.AppConstants;
import com.lxy.baselibs.utils.CommonUtils;
import com.lxy.baselibs.utils.DisplayUtils;
import com.lxy.baselibs.utils.ToastUtils;
import com.stela.analytics.model.Series;
import com.stela.comics_unlimited.Base.BaseMvpActivity;
import com.stela.comics_unlimited.R;
import com.stela.comics_unlimited.data.entity.PageEntity;
import com.stela.comics_unlimited.data.entity.SeriesEntity;
import com.stela.comics_unlimited.ui.collection.adapter.CollectionAllAdapter;
import com.stela.comics_unlimited.ui.series.SeriesActivity;
import com.stela.comics_unlimited.util.stela_sdk.StelaAnalyticsUtil;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

import static com.lxy.baselibs.app.BaseApplication.getContext;

/**
 * @author LXY
 */
public class CollectionActivity extends BaseMvpActivity<CollectionPresenter> implements CollectionContract.View {

    @BindView(R.id.rv_collection)
    RecyclerView rvCollection;
    @BindView(R.id.srl_ac_collection)
    SwipeRefreshLayout srlAcCollection;
    private String mCollectionId;
    private String mTitle;
    private CollectionAllAdapter myAdapter;
    private int mPageNo = 1;
    private List<SeriesEntity> mSeriesEntity = new ArrayList<>();
    private boolean isLastPage;

    public static void start(Context context, String id, String name) {
        Intent starter = new Intent(context, CollectionActivity.class);
        starter.putExtra(AppConstants.COLLECTION_ID, id);
        starter.putExtra(AppConstants.COLLECTION_NAME, name);
        context.startActivity(starter);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_collcation;
    }

    @Override
    protected void getIntent(Intent intent) {
        mCollectionId = intent.getStringExtra(AppConstants.COLLECTION_ID);
        mTitle = intent.getStringExtra(AppConstants.COLLECTION_NAME);

    }

    @Override
    protected void initView() {
        showBack();
        setTitle(mTitle);
        initRecyclerview();
    }

    @Override
    protected void initListener() {

    }

    @Override
    protected void initData() {
        srlAcCollection.setRefreshing(true);
        mPresenter.requestData(mCollectionId, mPageNo);
    }

    @Override
    protected CollectionPresenter createPresenter() {
        return new CollectionPresenter();
    }


    @Override
    public void showError(String msg) {
        if (srlAcCollection.isRefreshing()) {
            srlAcCollection.setRefreshing(false);
        }
        ToastUtils.showShort(msg);

    }

    @Override
    public void showData(PageEntity<SeriesEntity> seriesEntities) {
        if (srlAcCollection.isRefreshing()) {
            srlAcCollection.setRefreshing(false);
        }
        isLastPage = seriesEntities.isLastPage;
        if (mPageNo == 1) {
            mSeriesEntity.clear();
            mSeriesEntity = seriesEntities.list;
            myAdapter.setNewData(mSeriesEntity);
//            myAdapter.setEmptyView(ViewUtil.getEmptyView(this, "消息列表空荡荡的~", false));
            mPageNo++;
        } else {
            List<SeriesEntity> seriesEntities1 = seriesEntities.list;
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
    public void cancelSuccess(String data, int position) {
        myAdapter.getData().remove(position);
        myAdapter.notifyItemRemoved(position);
    }

    private void initRecyclerview() {
        myAdapter = new CollectionAllAdapter(R.layout.item_inner_browse, mSeriesEntity);
        LinearLayoutManager layout = new LinearLayoutManager(this);
        rvCollection.setLayoutManager(layout);
        rvCollection.setAdapter(myAdapter);
        srlAcCollection.setColorSchemeColors(ContextCompat.getColor(getContext(), R.color.stela_blue));
        srlAcCollection.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mPageNo = 1;
                mPresenter.requestData(mCollectionId, mPageNo);
            }
        });
        myAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                SeriesActivity.start(CollectionActivity.this,
                        mSeriesEntity.get(position).id);
                Series series = new Series();
                series.setSeriesId(mSeriesEntity.get(position).id);
                series.setSeriesName(mSeriesEntity.get(position).title);
                series.setGroupName("");
                series.setGroupId("");
                series.setPageName("collection");
                series.setPageId("");
                StelaAnalyticsUtil.click("series_view");
                StelaAnalyticsUtil.series(series);
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
                    mPresenter.requestData(mCollectionId, mPageNo);
                } else {
                    myAdapter.loadMoreEnd(true);
                }
            }
        }, rvCollection);
        myAdapter.setEmptyView(R.layout.empty_normal, rvCollection);
    }

    private void showPop(View v, int position) {
        View contentView = LayoutInflater.from(this).inflate(R.layout.pop_delete_layout, null);
        CustomPopWindow popWindow = new CustomPopWindow.PopupWindowBuilder(this)
                .setView(contentView)
                .setFocusable(false)
                .create();
        popWindow.showAsDropDown(v, DisplayUtils.getScreenWidth(this) / 2 - 100, -(v.getHeight() + popWindow.getHeight()) + CommonUtils.dp2px(30));
        contentView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SeriesEntity seriesEntity = myAdapter.getData().get(position);
                mPresenter.cancelCollection(seriesEntity.id, seriesEntity.seriesType, position);
                popWindow.dissmiss();
            }
        });
    }

}
