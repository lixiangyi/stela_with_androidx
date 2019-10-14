package com.stela.comics_unlimited.ui.mylibrary.innerLibrary;

import android.os.Bundle;
import androidx.core.content.ContextCompat;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.example.zhouwei.library.CustomPopWindow;
import com.lxy.baselibs.app.AppConstants;
import com.lxy.baselibs.rx.RxBus;
import com.lxy.baselibs.utils.CommonUtils;
import com.lxy.baselibs.utils.DisplayUtils;
import com.lxy.baselibs.utils.ToastUtils;
import com.stela.analytics.model.Page;
import com.stela.comics_unlimited.Base.BaseFragment;
import com.stela.comics_unlimited.R;
import com.stela.comics_unlimited.data.entity.CollectionEntity;
import com.stela.comics_unlimited.data.entity.SeriesEntity;
import com.stela.comics_unlimited.event.CollectionNotifyEvent;
import com.stela.comics_unlimited.event.MainPositionNotify;
import com.stela.comics_unlimited.ui.mylibrary.innerLibrary.adapter.CollectionAdapter;
import com.stela.comics_unlimited.util.stela_sdk.StelaAnalyticsUtil;
import com.stela.comics_unlimited.widget.MyContentLinearLayoutManager;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import io.reactivex.functions.Consumer;


/**
 * @author LXY
 * @date 2019/1/23 22:57
 * @desc
 */
public class CollectionFragment extends BaseFragment<CollectionInnerPresenter> implements CollectionInnerContract.View {


    @BindView(R.id.rv_collection)
    RecyclerView rvCollection;
    @BindView(R.id.srl_fg_collection)
    SwipeRefreshLayout srlFgCollection;
    private String mTitle;
    private CollectionAdapter myAdapter;
    private View emptyView;
    private List<CollectionEntity> mCollectionEntitys = new ArrayList<>();

    public static CollectionFragment getInstance(String title) {
        CollectionFragment fragment = new CollectionFragment();
        Bundle bundle = new Bundle();
        bundle.putString(AppConstants.TITLE, title);
        fragment.setArguments(bundle);
        fragment.mTitle = title;
        return fragment;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_collcation;
    }

    @Override
    protected CollectionInnerPresenter createPresenter() {
        return new CollectionInnerPresenter();
    }

    @Override
    protected void initView() {
        mTitle = getArguments().getString(AppConstants.TITLE);
        initEmptyView("You don't have any stories in your collection");
        initRecycleview();

    }

    private void setAWSPage() {
        //埋点
        Page page = new Page();
        page.setPageName("collection");
        StelaAnalyticsUtil.page(page);
    }

    @Override
    protected void LazyloadData() {
        setAWSPage();
    }

    private void initRecycleview() {
        myAdapter = new CollectionAdapter(R.layout.collection_layout, mCollectionEntitys);
        MyContentLinearLayoutManager layout = new MyContentLinearLayoutManager(getContext());
        rvCollection.setLayoutManager(layout);
        rvCollection.setNestedScrollingEnabled(false);
        rvCollection.setAdapter(myAdapter);
        myAdapter.setEmptyView(emptyView);
        srlFgCollection.setColorSchemeColors(ContextCompat.getColor(getContext(), R.color.stela_blue));
        srlFgCollection.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mPresenter.requestData();
            }
        });
        myAdapter.setOnDeleteListenter(new CollectionAdapter.ICallBack() {
            @Override
            public void callBackMethod(SeriesEntity seriesEntity, View view) {
                showPop(view, seriesEntity);
            }
        });
    }

    @Override
    protected void initListener() {
        mPresenter.addDispose(RxBus.getDefault().toObservable(CollectionNotifyEvent.class)
                .subscribe(new Consumer<CollectionNotifyEvent>() {
                    @Override
                    public void accept(CollectionNotifyEvent collectionNotifyEvent) throws Exception {
                        if (collectionNotifyEvent != null) {
                            mPresenter.requestData();
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
        srlFgCollection.setRefreshing(true);
        mPresenter.requestData();
    }

    @Override
    protected boolean useEventBus() {
        return false;
    }

    @Override
    public void showError(String msg) {
        ToastUtils.showShort(msg);
        if (srlFgCollection.isRefreshing()) {
            srlFgCollection.setRefreshing(false);
        }
    }


    @Override
    public void showListData(List<SeriesEntity> seriesEntities) {


    }

    @Override
    public void cancelSuccess(String s, int position) {
        initData();
//        myAdapter.getData().remove(position);
//        myAdapter.notifyItemRemoved(position);
    }

    @Override
    public void showListDataNew(List<CollectionEntity> data) {
        if (srlFgCollection.isRefreshing()) {
            srlFgCollection.setRefreshing(false);
        }
        myAdapter.setNewData(data);
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
        ((TextView) (emptyView.findViewById(R.id.btn_refresh))).setText("Collection Stories Now");
    }

    private void showPop(View v, SeriesEntity seriesEntity) {
        View contentView = LayoutInflater.from(getActivity()).inflate(R.layout.pop_delete_layout, null);
        CustomPopWindow popWindow = new CustomPopWindow.PopupWindowBuilder(getContext())
                .setView(contentView)
                .setFocusable(false)
                .create();
        popWindow.showAsDropDown(v, DisplayUtils.getScreenWidth(getContext()) / 2 - 100, -(v.getHeight() + popWindow.getHeight()) + CommonUtils.dp2px(30));
        contentView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPresenter.cancelCollection(seriesEntity.id, seriesEntity.seriesType, 0);
                popWindow.dissmiss();
            }
        });
    }


}
