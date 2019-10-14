package com.stela.comics_unlimited.ui.mylibrary.innerLibrary;

import android.graphics.Typeface;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.LayoutRes;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.SimpleItemAnimator;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.lxy.baselibs.app.AppConstants;
import com.lxy.baselibs.glide.GlideUtils;
import com.lxy.baselibs.rx.RxBus;
import com.lxy.baselibs.utils.ToastUtils;
import com.stela.analytics.model.Page;
import com.stela.comics_unlimited.Base.BaseFragment;
import com.stela.comics_unlimited.R;
import com.stela.comics_unlimited.data.entity.NotificationEntity;
import com.stela.comics_unlimited.data.entity.PageEntity;
import com.stela.comics_unlimited.event.MessageNeedLoadNotifyEvent;
import com.stela.comics_unlimited.event.NotificationListNotifyEvent;
import com.stela.comics_unlimited.ui.home.activity.HomeInnerActivity;
import com.stela.comics_unlimited.ui.series.SeriesActivity;
import com.stela.comics_unlimited.ui.web.CommonWebActivity;
import com.stela.comics_unlimited.util.stela_sdk.StelaAnalyticsUtil;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import io.reactivex.functions.Consumer;


/**
 * @author LXY
 * @date 2019/1/23 22:57
 * @desc
 */
public class  NotificationFragment extends BaseFragment<NotificationPresenter> implements NotificationContract.View {


    @BindView(R.id.rv_notification)
    RecyclerView rvNotification;
    @BindView(R.id.srl_notification)
    SwipeRefreshLayout swipeRefreshLayout;
    @BindView(R.id.tv_delete_all)
    AppCompatTextView tvDeleteAll;
    private int mPageNo = 1;
    private ArrayList<NotificationEntity> mNotifycationEntity = new ArrayList<>();
    private MyAdapter myAdapter;
    private View emptyView;
    private boolean isLastPage = false;
    private String notificationType;
    private String notificationId;

    public static NotificationFragment getInstance(String title, String notificationType, String notificationId) {
        NotificationFragment fragment = new NotificationFragment();
        Bundle bundle = new Bundle();
        bundle.putString(AppConstants.TITLE, title);
        bundle.putString(AppConstants.NOTIFICATION_TYPE, notificationType);
        bundle.putString(AppConstants.NOTIFICATION_ID, notificationId);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_notification;
    }

    @Override
    protected NotificationPresenter createPresenter() {
        return new NotificationPresenter();
    }

    @Override
    protected void initView() {
        notificationType = getArguments().getString(AppConstants.NOTIFICATION_TYPE);
        notificationId = getArguments().getString(AppConstants.NOTIFICATION_ID);
        initEmptyView("No notifications yet.");
        initRecyclerview();
    }

    private void setAWSPage() {
        //埋点
        Page page = new Page();
        page.setPageName("notification");
        StelaAnalyticsUtil.page(page);
    }


    @Override
    protected void LazyloadData() {
        setAWSPage();
    }

    @Override
    protected void initListener() {
        //界面更新
        mPresenter.addDispose(RxBus.getDefault().toObservable(NotificationListNotifyEvent.class)
                .subscribe(new Consumer<NotificationListNotifyEvent>() {
                    @Override
                    public void accept(NotificationListNotifyEvent notificationListNotifyEvent) throws Exception {
                        if (notificationListNotifyEvent != null) {
                            // 刷新
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
        swipeRefreshLayout.setRefreshing(true);
        mPresenter.requestData(mPageNo);
    }

    @Override
    protected boolean useEventBus() {
        return false;
    }

    @Override
    public void showError(String msg) {
        if (swipeRefreshLayout.isRefreshing()) {
            swipeRefreshLayout.setRefreshing(false);
        }
        ToastUtils.showShort(msg);
    }


    @Override
    public void showData(PageEntity<NotificationEntity> pageEntity) {
        if (swipeRefreshLayout.isRefreshing()) {
            swipeRefreshLayout.setRefreshing(false);
        }
        isLastPage = pageEntity.isLastPage;
        if (mPageNo == 1) {
            mNotifycationEntity.clear();
            mNotifycationEntity = pageEntity.list;
            myAdapter.setNewData(mNotifycationEntity);
//            myAdapter.setEmptyView(ViewUtil.getEmptyView(this, "消息列表空荡荡的~", false));
//            mPageNo++;
        } else {
            List<NotificationEntity> seriesEntities1 = pageEntity.list;
            if (seriesEntities1 != null) {
                if (seriesEntities1.size() > 0) {
                    mNotifycationEntity.addAll(seriesEntities1);
                    myAdapter.setNewData(mNotifycationEntity);
                    myAdapter.loadMoreComplete();
//                    mPageNo++;
                } else {
                    myAdapter.loadMoreEnd();
                }
            }
        }
        if (mNotifycationEntity.size() > 0) {
            tvDeleteAll.setVisibility(View.VISIBLE);
        } else {
            tvDeleteAll.setVisibility(View.GONE);
        }

        if (!TextUtils.isEmpty(notificationType) && notificationType.equals(NotificationEntity.PLACEMENTVALUE_NO)) {
            for (int i = 0; i < mNotifycationEntity.size(); i++) {
                if (mNotifycationEntity.get(i).id.equals(notificationId)) {
                    rvNotification.scrollToPosition(i);
                    break;
                }
            }
        }
    }

    @Override
    public void deleteSuccess(Object s, int position) {
        myAdapter.getData().remove(position);
        myAdapter.notifyItemRemoved(position);
        if (mNotifycationEntity.size() > 0) {
            tvDeleteAll.setVisibility(View.VISIBLE);
        } else {
            tvDeleteAll.setVisibility(View.GONE);
        }
        //消息数量提醒刷新修改
        RxBus.getDefault().post(new MessageNeedLoadNotifyEvent());
    }

    @Override
    public void deleteAllSuccess(String s) {
        myAdapter.getData().clear();
        myAdapter.notifyDataSetChanged();
        tvDeleteAll.setVisibility(View.GONE);
        //消息数量提醒刷新修改
        RxBus.getDefault().post(new MessageNeedLoadNotifyEvent());
    }

    @Override
    public void updateSuccess(Object s, int position) {
        myAdapter.getData().get(position).state = 1;
        myAdapter.notifyItemChanged(position);
        //消息数量提醒刷新修改
        RxBus.getDefault().post(new MessageNeedLoadNotifyEvent());
    }

    @Override
    public void updateAllSuccess(String s) {
        for (int i = 0; i < myAdapter.getData().size(); i++) {
            myAdapter.getData().get(i).state = 1;
        }
        myAdapter.notifyDataSetChanged();
        //消息数量提醒刷新修改
        RxBus.getDefault().post(new MessageNeedLoadNotifyEvent());
    }

    private void initRecyclerview() {
        myAdapter = new MyAdapter(R.layout.item_notification, mNotifycationEntity);
        LinearLayoutManager layout = new LinearLayoutManager(getContext());
        rvNotification.setLayoutManager(layout);
        ((SimpleItemAnimator) rvNotification.getItemAnimator()).setSupportsChangeAnimations(false);
        rvNotification.setAdapter(myAdapter);
        swipeRefreshLayout.setColorSchemeColors(ContextCompat.getColor(getContext(), R.color.stela_blue));
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mPageNo = 1;
                mPresenter.requestData(mPageNo);
            }
        });
        myAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                NotificationEntity item = (NotificationEntity) adapter.getData().get(position);
                if (item.placementValue != null) {
                    switch (item.placementValue) {
                        case NotificationEntity.PLACEMENTVALUE_PAGE:
                            HomeInnerActivity.start(getActivity(), item.destinationValue, item.messageTitle);
                            break;
                        case NotificationEntity.PLACEMENTVALUE_SERIES://seriespage
                            SeriesActivity.start(getActivity(), item.destinationValue);
                            break;
                        case NotificationEntity.PLACEMENTVALUE_CHAPTER://chapterpage
                            SeriesActivity.start(getActivity(), item.seriesValue, item.destinationValue, item.placementValue);
                            break;
                        case NotificationEntity.PLACEMENTVALUE_WEB://web url
                            CommonWebActivity.start(getActivity(), item.destinationValue, false);
                            break;
                        default:
                            break;
                    }
                }
                if (item.state == 0) {//未读的更新状态
                    // TODO: 2019/9/12 页面跳转之后接口失败
                    mPresenter.updateNotification(item.userNotificationId, position);
                }
            }
        });
        myAdapter.setEnableLoadMore(true);
        myAdapter.setOnLoadMoreListener(new BaseQuickAdapter.RequestLoadMoreListener() {
            @Override
            public void onLoadMoreRequested() {
                if (!isLastPage) {
                    mPageNo++;
                    mPresenter.requestData(mPageNo);
                } else {
                    myAdapter.loadMoreEnd(true);
                }
            }
        }, rvNotification);
        myAdapter.setEmptyView(emptyView);
    }

    private void initEmptyView(String msg) {
        emptyView = LayoutInflater.from(getContext()).inflate(R.layout.empty_refresh, null);
        emptyView.findViewById(R.id.btn_refresh).setVisibility(View.GONE);
        ((TextView) (emptyView.findViewById(R.id.tv_empty_msg))).setText(msg);
    }

    @OnClick(R.id.tv_delete_all)
    public void onViewClicked() {
        mPresenter.deleteAll();
    }


    public class MyAdapter extends BaseQuickAdapter<NotificationEntity, BaseViewHolder> {

        MyAdapter(@LayoutRes int layoutResId, @Nullable List<NotificationEntity> data) {
            super(layoutResId, data);
        }

        @Override
        protected void convert(BaseViewHolder helper, NotificationEntity item) {
            ((TextView) (helper.getView(R.id.tv_notification_title))).setText(item.messageTitle);
            ((TextView) (helper.getView(R.id.tv_notification_content))).setText(item.messageBody);
            ((TextView) (helper.getView(R.id.tv_notification_time))).setText(item.createdAtStr);
            ((TextView) (helper.getView(R.id.tv_see_more))).setText(item.destinationName);
            //状态
            if (item.state == 0) {//未读
                helper.setGone(R.id.bobble_unread, true);
                ((TextView) (helper.getView(R.id.tv_notification_title))).setTypeface(Typeface.DEFAULT_BOLD);
            } else {//已读
                helper.setGone(R.id.bobble_unread, false);
                ((TextView) (helper.getView(R.id.tv_notification_title))).setTypeface(Typeface.DEFAULT);
            }

            //图片
            if (!TextUtils.isEmpty(item.url)) {
                helper.setGone(R.id.iv_notification, true);
                GlideUtils.loadImageHttps(mContext, helper.getView(R.id.iv_notification), item.url, R.color.stela_blue);
            } else {
                helper.setGone(R.id.iv_notification, false);
            }
            //删除
            helper.getView(R.id.iv_delete_notification).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mPresenter.delete(item.userNotificationId, helper.getAdapterPosition());
                }
            });
            //see more
            if (item.placementValue != null) {
                helper.setGone(R.id.tv_see_more, true);
            } else {
                helper.setGone(R.id.tv_see_more, false);
            }
        }

    }
}
