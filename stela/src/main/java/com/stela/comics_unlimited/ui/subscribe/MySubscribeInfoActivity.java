package com.stela.comics_unlimited.ui.subscribe;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
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
import com.lxy.baselibs.utils.ToastUtils;
import com.makeramen.roundedimageview.RoundedImageView;
import com.stela.comics_unlimited.Base.BaseMvpActivity;
import com.stela.comics_unlimited.R;
import com.stela.comics_unlimited.app.DataStore;
import com.stela.comics_unlimited.data.entity.PageEntity;
import com.stela.comics_unlimited.data.entity.SubscriptionEntity;
import com.stela.comics_unlimited.ui.web.CommonWebActivity;
import com.stela.comics_unlimited.util.stela_sdk.StelaAnalyticsUtil;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

import static com.lxy.baselibs.app.BaseApplication.getContext;

/**
 * @author LXY
 */
public class MySubscribeInfoActivity extends BaseMvpActivity<MySubscribeInfoPresenter> implements MySubscribeInfoContract.View {


    @BindView(R.id.iv_header)
    RoundedImageView ivHeader;
    @BindView(R.id.tv_faq)
    TextView tvFaq;
    @BindView(R.id.tv_ts)
    TextView tvTs;
    @BindView(R.id.tv_p_p)
    AppCompatTextView tvPP;
    @BindView(R.id.tv_cs)
    AppCompatTextView tvCs;
    @BindView(R.id.iv_back)
    ImageView ivBack;
    @BindView(R.id.rv_my_sublist)
    RecyclerView rvMySublist;
    @BindView(R.id.srl_my_sublist)
    SwipeRefreshLayout srlMySublist;
    @BindView(R.id.tv_username)
    TextView tvUsername;
    @BindView(R.id.iv_bg)
    ImageView ivBg;
    @BindView(R.id.iv_select_subscribe)
    ImageView ivSelectSubscribe;
    @BindView(R.id.tv_subscribe_title)
    TextView tvSubscribeTitle;
    @BindView(R.id.ll_subscribe)
    LinearLayout llSubscribe;
    @BindView(R.id.tv_subscribe_subtitle)
    TextView tvSubscribeExp;
    @BindView(R.id.tv_subs_info)
    TextView tvSubsInfo;
    private MyAdapter myAdapter;
    private int mPageNo = 1;
    private ArrayList<SubscriptionEntity> mSubscriptionEntity = new ArrayList<>();
    private boolean isLastPage;

    public static void start(Context context) {
        Intent starter = new Intent(context, MySubscribeInfoActivity.class);
        context.startActivity(starter);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_my_subscribe;
    }

    @Override
    protected void getIntent(Intent intent) {

    }

    @Override
    protected void initView() {
        ivBack.setVisibility(View.VISIBLE);
        GlideUtils.loadCircleImageWithBoder(this, ivHeader, DataStore.getUserInfo().imgPortrait, R.drawable.avatar_bg_1);
        tvUsername.setText(DataStore.getUserInfo().nickname + "");
        ivSelectSubscribe.setSelected(true);
        llSubscribe.setSelected(true);
    }

    @Override
    protected void initListener() {

    }

    @Override
    protected void initData() {
//        initRecyclerview();
//        srlMySublist.setRefreshing(true);
        mPresenter.getMySubInfo();
    }

    @Override
    protected MySubscribeInfoPresenter createPresenter() {
        return new MySubscribeInfoPresenter();
    }


    @Override
    public void showError(String msg) {
        ToastUtils.showShort(msg);
        if (srlMySublist.isRefreshing()) {
            srlMySublist.setRefreshing(false);
        }
    }

    private void initRecyclerview() {
        myAdapter = new MyAdapter(R.layout.item_my_subinfo_list, mSubscriptionEntity);
        LinearLayoutManager layout = new LinearLayoutManager(getContext());
        rvMySublist.setLayoutManager(layout);
        rvMySublist.setAdapter(myAdapter);
        ((SimpleItemAnimator) rvMySublist.getItemAnimator()).setSupportsChangeAnimations(false);
        myAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {

            }
        });
        myAdapter.setEmptyView(R.layout.empty_normal, rvMySublist);
        srlMySublist.setColorSchemeColors(ContextCompat.getColor(getContext(), R.color.stela_blue));
        srlMySublist.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mPageNo = 1;
//                mPresenter.getMySubInfo(mPageNo);
            }
        });
        myAdapter.setEnableLoadMore(true);
        myAdapter.setOnLoadMoreListener(new BaseQuickAdapter.RequestLoadMoreListener() {
            @Override
            public void onLoadMoreRequested() {
                if (!isLastPage) {
//                    mPresenter.getMySubInfo(mPageNo);
                } else {
                    myAdapter.loadMoreEnd(true);
                }
            }
        }, rvMySublist);
    }


    @OnClick({R.id.tv_faq, R.id.tv_ts, R.id.tv_p_p, R.id.tv_cs, R.id.iv_back,R.id.tv_subs_info})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_faq:
                StelaAnalyticsUtil.click( "faq" );
                CommonWebActivity.start(this, AppConstants.FQA_URL, false);
                break;
            case R.id.tv_ts:
                StelaAnalyticsUtil.click( "term");
                CommonWebActivity.start(this, AppConstants.TERMS_AND_SERVICE_URL, false);
                break;
            case R.id.tv_p_p:
                StelaAnalyticsUtil.click( "privacy");
                CommonWebActivity.start(this, AppConstants.PRIVACY_POLICY_URL, false);
                break;
            case R.id.tv_subs_info:
                CommonWebActivity.start(this,AppConstants.SUBSCRIPTION_INFO_URL,false);
                StelaAnalyticsUtil.click("subscription_info_"+AppConstants.SUBSCRIPTION_INFO_URL);
                break;
            case R.id.iv_back:
                finish();
                break;
            default:
                break;
        }
    }

    public void showListData(PageEntity<SubscriptionEntity> pageEntity) {
        if (srlMySublist.isRefreshing()) {
            srlMySublist.setRefreshing(false);
        }
        isLastPage = pageEntity.isLastPage;
        if (mPageNo == 1) {
            mSubscriptionEntity.clear();
            mSubscriptionEntity = pageEntity.list;
            myAdapter.setNewData(mSubscriptionEntity);
//            myAdapter.setEmptyView(ViewUtil.getEmptyView(this, "消息列表空荡荡的~", false));
            mPageNo++;
        } else {
            List<SubscriptionEntity> commentsEntityList = pageEntity.list;
            if (commentsEntityList != null) {
                if (commentsEntityList.size() > 0) {
                    mSubscriptionEntity.addAll(commentsEntityList);
                    myAdapter.setNewData(mSubscriptionEntity);
                    myAdapter.loadMoreComplete();
                    mPageNo++;
                } else {
                    myAdapter.loadMoreEnd(true);
                }
            }
        }
    }


    @Override
    public void showListData(SubscriptionEntity subscriptionEntity) {
        if (!TextUtils.isEmpty(subscriptionEntity.productName)) {
            llSubscribe.setVisibility(View.VISIBLE);
            tvSubscribeTitle.setText(subscriptionEntity.productName + "/" + subscriptionEntity.price);
            if (!TextUtils.isEmpty(subscriptionEntity.expiresDateStr)) {
                tvSubscribeExp.setText(subscriptionEntity.expiresDateStr);
            } else {
                tvSubscribeExp.setVisibility(View.GONE);
            }
        } else {
            llSubscribe.setVisibility(View.GONE);
        }
    }

    public class MyAdapter extends BaseQuickAdapter<SubscriptionEntity, BaseViewHolder> {

        MyAdapter(@LayoutRes int layoutResId, @Nullable List<SubscriptionEntity> data) {
            super(layoutResId, data);
        }

        @Override
        protected void convert(BaseViewHolder helper, SubscriptionEntity item) {
            TextView name = helper.getView(R.id.tv_subscribe_name);
            name.setText(item.productName);
            TextView price = helper.getView(R.id.tv_subscribe_cost);
            price.setText(item.price + "");

        }
    }
}
