package com.stela.comics_unlimited.widget;

import android.content.Context;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatTextView;

import com.lxy.baselibs.app.AppConstants;
import com.lxy.baselibs.base.BaseActivity;
import com.lxy.baselibs.glide.GlideUtils;
import com.lxy.baselibs.rx.RxBus;
import com.lxy.baselibs.utils.AppUtils;
import com.makeramen.roundedimageview.RoundedImageView;
import com.stela.comics_unlimited.R;
import com.stela.comics_unlimited.app.DataStore;
import com.stela.comics_unlimited.data.entity.PersonEntity;
import com.stela.comics_unlimited.event.DrawerBackEvent;
import com.stela.comics_unlimited.ui.parental_controls.ParentalControlsActivity;
import com.stela.comics_unlimited.ui.person.PersonInfoActivity;
import com.stela.comics_unlimited.ui.subscribe.SubscribeActivity;
import com.stela.comics_unlimited.ui.web.CommonWebActivity;
import com.stela.comics_unlimited.util.SupportHelper;
import com.stela.comics_unlimited.util.stela_sdk.StelaAnalyticsUtil;
import com.stela.comics_unlimited.widget.ScaleView.ScaleLinearLayout;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * Created by LXY on 2018/8/15 0015.
 */
public class NavUserLayout extends LinearLayout {

    @BindView(R.id.iv_header)
    RoundedImageView ivHeader;
    @BindView(R.id.ll_faq)
    LinearLayout llFaq;
    @BindView(R.id.ll_terms_of_service)
    LinearLayout llTermsOfService;
    @BindView(R.id.ll_privacy_policy)
    LinearLayout llPrivacyPolicy;
    @BindView(R.id.ll_contact_support)
    LinearLayout llContactSupport;
    @BindView(R.id.ll_logout)
    LinearLayout llLogout;
    @BindView(R.id.tv_version)
    TextView tvVersion;
    @BindView(R.id.tv_subscribe)
    TextView tvSubscribe;
    @BindView(R.id.iv_back)
    ImageView ivBack;
    @BindView(R.id.tv_username)
    TextView tvUsername;
    @BindView(R.id.rl_head)
    ScaleLinearLayout rlHead;
    @BindView(R.id.tv_email)
    AppCompatTextView tvEmail;
    @BindView(R.id.tv_profile)
    AppCompatTextView tvProfile;
    @BindView(R.id.ll_account_settings)
    LinearLayout llAccountSettings;
    @BindView(R.id.ll_parental)
    LinearLayout llParental;
    @BindView(R.id.tv_parental_state)
    TextView tvParentalState;
    private CallBack mCallBack;
    private BaseActivity baseActivity;
    private Unbinder unbinder;
    private boolean isSub;

    public void setCallBack(CallBack callBack) {
        this.mCallBack = callBack;
    }


    public interface CallBack {
        void onGoclick(int position);

    }

    public NavUserLayout(Context context) {
        super(context);
    }

    public NavUserLayout(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        baseActivity = (BaseActivity) context;
        init();
    }

    public NavUserLayout(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        View itemView = LayoutInflater.from(baseActivity).inflate(R.layout.userinfo_layout, this, true);
        unbinder = ButterKnife.bind(this, itemView);
        tvVersion.setText("V " + AppUtils.getVersionName(baseActivity));
        tvEmail.setVisibility(VISIBLE);
        tvProfile.setVisibility(VISIBLE);
        tvUsername.setVisibility(VISIBLE);
        reload();
    }

    public void reload() {
        PersonEntity personEntity = DataStore.getUserInfo();
        if (personEntity != null && !TextUtils.isEmpty(personEntity.imgPortrait)) {
            GlideUtils.loadCircleImageWithBoder(baseActivity, ivHeader, personEntity.imgPortrait, R.drawable.avatar_bg_1);
            tvUsername.setText(personEntity.nickname);
            tvEmail.setText(personEntity.email);
        }
        if (personEntity.comp == 1) {
            //  超级用户
            tvSubscribe.setVisibility(GONE);
        } else {
            if (personEntity.subscriptionStatus == 1) {
                //已经订阅
                tvSubscribe.setVisibility(GONE);
            } else {
                //未订阅
                tvSubscribe.setVisibility(VISIBLE);
            }
        }
        if (personEntity.childrenState == 1) {
            llAccountSettings.setVisibility(GONE);
            tvProfile.setVisibility(GONE);
            tvParentalState.setText("On");
        } else {
            llAccountSettings.setVisibility(VISIBLE);
            tvParentalState.setText("Off");
            tvProfile.setVisibility(VISIBLE);
        }
        ivBack.setVisibility(VISIBLE);
    }


    @OnClick({R.id.iv_back,R.id.iv_header, R.id.rl_head, R.id.ll_faq, R.id.ll_account_settings, R.id.ll_parental, R.id.tv_profile,
            R.id.ll_terms_of_service, R.id.ll_privacy_policy, R.id.ll_contact_support,R.id.tv_username,R.id.tv_email,
            R.id.ll_logout, R.id.tv_subscribe})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case  R.id.iv_back:
                RxBus.getDefault().post(new DrawerBackEvent());
                break;
            case R.id.iv_header:
            case R.id.tv_profile:
            case R.id.tv_username:
            case R.id.tv_email:
            case R.id.ll_account_settings:
                if (DataStore.getUserInfo().childrenState != 1) {
                    PersonInfoActivity.start(baseActivity);
                }
                break;
            case R.id.ll_parental:
                ParentalControlsActivity.start(baseActivity);
                break;
            case R.id.rl_head:
                break;
            case R.id.ll_faq:
                StelaAnalyticsUtil.click("faq");
                CommonWebActivity.start(baseActivity, AppConstants.FQA_URL, false);
                break;
            case R.id.ll_terms_of_service:
                StelaAnalyticsUtil.click("term");
                CommonWebActivity.start(baseActivity, AppConstants.TERMS_AND_SERVICE_URL, false);
                break;
            case R.id.ll_privacy_policy:
                StelaAnalyticsUtil.click("privacy");
                CommonWebActivity.start(baseActivity, AppConstants.PRIVACY_POLICY_URL, false);
                break;
            case R.id.ll_contact_support:
                StelaAnalyticsUtil.click("support");
                SupportHelper.goEmail(baseActivity);
                break;
            case R.id.ll_logout:
                StelaAnalyticsUtil.click("logout");
                DataStore.logout(baseActivity);
                break;
            case R.id.tv_subscribe:
                SubscribeActivity.start(baseActivity, "");
                StelaAnalyticsUtil.click("subscribe");
                break;
        }
    }

    public void unbind() {
        unbinder.unbind();
    }

}
