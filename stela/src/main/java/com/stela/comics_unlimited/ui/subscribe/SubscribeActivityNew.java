package com.stela.comics_unlimited.ui.subscribe;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.core.widget.NestedScrollView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.lxy.baselibs.app.AppConstants;
import com.lxy.baselibs.glide.GlideUtils;
import com.lxy.baselibs.rx.RxBus;
import com.lxy.baselibs.utils.LogUtils;
import com.lxy.baselibs.utils.ToastUtils;
import com.stela.comics_unlimited.Base.BaseMvpActivity;
import com.stela.comics_unlimited.R;
import com.stela.comics_unlimited.app.DataStore;
import com.stela.comics_unlimited.data.entity.SubscriptionEntity;
import com.stela.comics_unlimited.event.ChapterNotifyEvent;
import com.stela.comics_unlimited.event.HomeNotifyEvent;
import com.stela.comics_unlimited.event.SeriesNotifyEvent;
import com.stela.comics_unlimited.event.UserInfoUpdataNotifyEvent;
import com.stela.comics_unlimited.pay.IabResult;
import com.stela.comics_unlimited.pay.Inventory;
import com.stela.comics_unlimited.pay.Purchase;
import com.stela.comics_unlimited.ui.web.CommonWebActivity;
import com.stela.comics_unlimited.util.FirebaseRemoteConfigHelper;
import com.stela.comics_unlimited.util.JsonHp;
import com.stela.comics_unlimited.util.facebook.FaceBookLogUtil;
import com.stela.comics_unlimited.util.google_subs.GoogleSubscribeHelper;
import com.stela.comics_unlimited.util.stela_sdk.StelaAnalyticsUtil;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

public class SubscribeActivityNew extends BaseMvpActivity<SubscribePresenter> implements SubscribeContract.View, GoogleSubscribeHelper.GoogleSubsInventoryInterface,
        GoogleSubscribeHelper.GoogleSubsPurchaseInterface {
    private static final int PICK_ACCOUNT_REQUEST = 10010;
    //    private static final String UNFORGOTTEN =
//            Why.why +
//                    "/9P4MbAEZvoqOcan/1zKuEZkOi034j3GJ6ZP4BsAPdlcXteGUKbxQvSECFYSP6bRoGo382Jg6252C83OtpkXpGs5q7Pnl9OX8+" +
//                    What.what +
//                    "4AbwktrsWTv3YNYGcEmbQPP/L/1y5hy3k6bOCu+QswWCmBcxRzHvhBrb25Xjpn3elF4A5CpYCBlZdfpsc+0ZGsL58/hNdpYHeQIDAQAB";
    @BindView(R.id.iv_colse)
    ImageView ivColse;
    @BindView(R.id.iv_head_img)
    ImageView mIvHeadImg;
    @BindView(R.id.rv_subcribe)
    RecyclerView rvSubcribe;
    @BindView(R.id.iv_foot_img)
    ImageView mIvFootImg;
    @BindView(R.id.tv_faq)
    TextView tvFaq;
    @BindView(R.id.tv_ts)
    TextView tvTs;
    @BindView(R.id.tv_p_p)
    AppCompatTextView tvPP;
    @BindView(R.id.tv_cs)
    AppCompatTextView tvCs;
    @BindView(R.id.tv_subcribe)
    TextView tvSubcribe;
    @BindView(R.id.ll_subscribe_bg)
    LinearLayout llSubscribeBg;
    @BindView(R.id.nsv_sub)
    NestedScrollView nsvSub;
    private List<SubscriptionEntity> mSubscriptionEntitys = new ArrayList<>();
    private MySubscribeAdapter myAdapter;
    private String mSelectedSubscriptionPeriod = "";
    private Inventory mInventory;
    private boolean mCannotSubs = false;
    private String mSeriesId;
    private int mChapterPosition = -1;

    public static void start(Context context, String seriesId) {
        Intent starter = new Intent(context, SubscribeActivityNew.class);
        starter.putExtra(AppConstants.SERIES_ID, seriesId);
        context.startActivity(starter);
    }

    public static void start(Context context, String seriesId, int mPosition) {
        Intent starter = new Intent(context, SubscribeActivityNew.class);
        starter.putExtra(AppConstants.SERIES_ID, seriesId);
        starter.putExtra(AppConstants.CHAPTER_POSITION, mPosition);
        context.startActivity(starter);
    }


    @Override
    protected int getLayoutId() {
        return R.layout.activity_subcribe;
    }

    @Override
    protected void getIntent(Intent intent) {
        mSeriesId = intent.getStringExtra(AppConstants.SERIES_ID);
        mChapterPosition = intent.getIntExtra(AppConstants.CHAPTER_POSITION, -1);
    }

    @Override
    protected void initView() {
        myAdapter = new MySubscribeAdapter(R.layout.item_subcribe, mSubscriptionEntitys);
        LinearLayoutManager layout = new LinearLayoutManager(this) {
            @Override
            public boolean canScrollVertically() {
                return false;
            }
        };
        rvSubcribe.setLayoutManager(layout);
        rvSubcribe.setAdapter(myAdapter);
        myAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                SubscriptionEntity subscriptionEntity = mSubscriptionEntitys.get(position);
                for (int i = 0; i < mSubscriptionEntitys.size(); i++) {
                    mSubscriptionEntitys.get(i).isSelect = false;
                }
                subscriptionEntity.isSelect = true;
                tvSubcribe.setText(subscriptionEntity.productName);
                adapter.notifyDataSetChanged();
                mSelectedSubscriptionPeriod = subscriptionEntity.productIdentifier;
                FaceBookLogUtil.logAddToCartEvent(Double.parseDouble(subscriptionEntity.price));
                if (subscriptionEntity.bestDeal == 1) {
                    FaceBookLogUtil.logFBSDKAppEventNameStartTrialEvent();
                }
            }

        });
    }

    @Override
    protected void initListener() {


    }

    @Override
    protected void initData() {
        getData();


        GoogleSubscribeHelper.initSubcribe(this);
        GoogleSubscribeHelper.setGoogleSubsInventoryInterface(this);
        GoogleSubscribeHelper.setGoogleSubsPurchaseInterface(this);
    }

    private void getData() {

        showLoading();
        FirebaseRemoteConfigHelper.mFirebaseRemoteConfig.fetchAndActivate()
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                    }
                })
                .addOnSuccessListener(new OnSuccessListener<Boolean>() {
                    @Override
                    public void onSuccess(Boolean aBoolean) {

                    }
                })
                .addOnCompleteListener(this, new OnCompleteListener<Boolean>() {
                    @Override
                    public void onComplete(@NonNull Task<Boolean> task) {
                        if (task.isSuccessful()) {
                            boolean updated = task.getResult();

                            LogUtils.d(TAG, "Config params updated: " + updated);
//                            Toast.makeText(SubscribeActivity.this, "Fetch and activate succeeded",
//                                    Toast.LENGTH_SHORT).show();


                            hideLoading();
                            SubscriptionEntity subscriptionEntity = JsonHp.getGsonConverter().fromJson(FirebaseRemoteConfigHelper.mFirebaseRemoteConfig.getString("ios_paywall")
                                    , SubscriptionEntity.class);
                            showData(subscriptionEntity);

                        } else {
//                            Toast.makeText(SubscribeActivity.this, "Fetch failed",
//                                    Toast.LENGTH_SHORT).show();
                            mPresenter.getSubpageInfo(TextUtils.isEmpty(mSeriesId) ? "" : mSeriesId);

                        }
                    }
                });

    }

    @Override
    protected SubscribePresenter createPresenter() {
        return new SubscribePresenter();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        GoogleSubscribeHelper.onDestroy(this);
    }

    @Override
    public void showError(String msg) {
        hideLoading();
        ToastUtils.showShort(msg);
    }


    @Override
    public void showData(SubscriptionEntity subscriptionEntity) {
        hideLoading();
        if (!TextUtils.isEmpty(subscriptionEntity.backgroundColor)) {
            nsvSub.setBackgroundColor(Color.parseColor(subscriptionEntity.backgroundColor));
        }
        if (!TextUtils.isEmpty(subscriptionEntity.headerImageUrl)) {
            mIvHeadImg.setVisibility(View.VISIBLE);
            GlideUtils.loadImageHttps(this, mIvHeadImg, subscriptionEntity.headerImageUrl, R.color.white);
        } else {
            mIvHeadImg.setVisibility(View.GONE);
        }
//        if (!TextUtils.isEmpty(subscriptionEntity.footerImageUrl)) {
//            mIvFootImg.setVisibility(View.VISIBLE);
//            GlideUtils.loadImageHttps(this, mIvFootImg, subscriptionEntity.footerImageUrl, R.color.white);
//        } else {
        mIvFootImg.setVisibility(View.GONE);
//        }
        mSubscriptionEntitys = subscriptionEntity.subscriptionList;
        if (mSubscriptionEntitys != null && mSubscriptionEntitys.size() > 0) {
            for (int i = 0; i < mSubscriptionEntitys.size(); i++) {
                if (mSubscriptionEntitys.get(i).bestDeal == 1) {
                    mSubscriptionEntitys.get(i).isSelect = true;
                    tvSubcribe.setText(mSubscriptionEntitys.get(i).productName);
                    tvSubcribe.setVisibility(View.VISIBLE);
                    mSelectedSubscriptionPeriod = mSubscriptionEntitys.get(i).productIdentifier;
                    break;
                }
            }
            myAdapter.setNewData(mSubscriptionEntitys);
        }
        if (mInventory != null) {
            List<Purchase> purchases = mInventory.getAllPurchases();
            if (purchases != null && purchases.size() > 0) {
                mCannotSubs = true;
            }
        }

    }

    @OnClick({R.id.iv_colse, R.id.tv_faq, R.id.tv_ts, R.id.tv_p_p, R.id.tv_cs, R.id.tv_subcribe})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_colse:
                finish();
                break;
            case R.id.tv_faq:
                StelaAnalyticsUtil.click("faq");
                CommonWebActivity.start(this, AppConstants.FQA_URL, false);
                break;
            case R.id.tv_ts:
                StelaAnalyticsUtil.click("term");
                CommonWebActivity.start(this, AppConstants.TERMS_AND_SERVICE_URL, false);
                break;
            case R.id.tv_p_p:
                StelaAnalyticsUtil.click("privacy");
                CommonWebActivity.start(this, AppConstants.PRIVACY_POLICY_URL, false);
                break;
            case R.id.tv_cs:
                StelaAnalyticsUtil.click("subscription_info_" + AppConstants.SUBSCRIPTION_INFO_URL);
                CommonWebActivity.start(this, AppConstants.SUBSCRIPTION_INFO_URL, false);
                break;
            case R.id.tv_subcribe:
//                DataStore.getEmail8(this,PICK_ACCOUNT_REQUEST);
                GoogleSubscribeHelper.subscriptionsSupported();
                if (!mCannotSubs) {
                    GoogleSubscribeHelper.doPay(SubscribeActivityNew.this, mInventory, mSubscriptionEntitys, mSelectedSubscriptionPeriod);
                    StelaAnalyticsUtil.click("subscribe");
                } else {
                    ToastUtils.showShort("You had subscribed already");
                }
                break;
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        LogUtils.d(TAG, "onActivityResult(" + requestCode + "," + resultCode + "," + data);
        if (requestCode == PICK_ACCOUNT_REQUEST && resultCode == RESULT_OK) {
            DataStore.handleActivityResult(this, requestCode, resultCode, data);
        }
        if (GoogleSubscribeHelper.onActivityResult(requestCode, resultCode, data)) {
            super.onActivityResult(requestCode, resultCode, data);
        }

    }


    private void verifyDeveloperPayloadNew(Purchase p) {
        mPresenter.verifyGooglePay(p);
    }


    @Override
    public void verifyGooglePaySuccess(String s, Purchase purchase) {
        hideLoading();
        LogUtils.d(TAG, "Purchase successful.");
        ToastUtils.showShort("Purchase successful");
        //facebook 埋点
        for (int i = 0; i < mSubscriptionEntitys.size(); i++) {
            if (mSubscriptionEntitys.get(i).isSelect) {
                FaceBookLogUtil.logPurchaseEvent(BigDecimal.valueOf(Double.parseDouble(mSubscriptionEntitys.get(i).price)));
            }
        }
        //aws埋点
        com.stela.analytics.model.Purchase p = new com.stela.analytics.model.Purchase();
        p.setProduct(purchase.getSku());
        p.setStatus("success");
        StelaAnalyticsUtil.purchase(p);

        // 更新个人接口
        // 更新个人界面
        RxBus.getDefault().post(new UserInfoUpdataNotifyEvent());
        //更新 seriesInfo
        RxBus.getDefault().post(new SeriesNotifyEvent());
        //更新 chapter
        RxBus.getDefault().post(new ChapterNotifyEvent(mChapterPosition));
        RxBus.getDefault().post(new HomeNotifyEvent());
        finish();
    }

    @Override
    public void subInvSuccess(Inventory inventory) {
        mInventory = inventory;
    }

    @Override
    public void subSuccess(Purchase purchase) {
        verifyDeveloperPayloadNew(purchase);
    }

    @Override
    public void subFailure(IabResult result) {
        LogUtils.d(TAG, "Error purchasing: " + result);
        //facebook 埋点
        for (int i = 0; i < mSubscriptionEntitys.size(); i++) {
            if (mSubscriptionEntitys.get(i).isSelect) {
                FaceBookLogUtil.log_fb_purchase_failedEvent(Double.parseDouble(mSubscriptionEntitys.get(i).price));
            }
        }
        hideLoading();
    }

    public class MySubscribeAdapter extends BaseQuickAdapter<SubscriptionEntity, BaseViewHolder> {

        public MySubscribeAdapter(@LayoutRes int layoutResId, @Nullable List<SubscriptionEntity> data) {
            super(layoutResId, data);
        }

        @Override
        protected void convert(BaseViewHolder helper, SubscriptionEntity item) {
            ((TextView) (helper.getView(R.id.tv_subscribe_title))).setText(item.productName);
            if (TextUtils.isEmpty(item.notes)) {
                helper.setGone(R.id.tv_subscribe_subtitle, false);
            } else {
                ((TextView) (helper.getView(R.id.tv_subscribe_subtitle))).setText(item.notes);
            }
            helper.getView(R.id.ll_subscribe).setSelected(item.isSelect);
            helper.getView(R.id.iv_select_subscribe).setSelected(item.isSelect);
            helper.getView(R.id.tv_subscribe_title).setSelected(item.isSelect);
//            GlideUtils.loadImageHttps(mContext, helper.getView(R.id.iv_bg), item.picAssetsUrl, R.color.transparent);
        }

    }

}
