package com.stela.comics_unlimited.ui.person;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.widget.AppCompatTextView;

import com.lxy.baselibs.app.AppConstants;
import com.lxy.baselibs.glide.GlideUtils;
import com.lxy.baselibs.rx.RxBus;
import com.lxy.baselibs.utils.AppUtils;
import com.makeramen.roundedimageview.RoundedImageView;
import com.stela.comics_unlimited.Base.BaseMvpActivity;
import com.stela.comics_unlimited.R;
import com.stela.comics_unlimited.app.DataStore;
import com.stela.comics_unlimited.event.UserNotifyEvent;
import com.stela.comics_unlimited.ui.web.CommonWebActivity;
import com.stela.comics_unlimited.util.stela_sdk.StelaAnalyticsUtil;
import com.stela.comics_unlimited.widget.ScaleView.ScaleLinearLayout;

import butterknife.BindView;
import butterknife.OnClick;
import io.reactivex.functions.Consumer;

import static com.lxy.baselibs.app.BaseApplication.getContext;

/**
 * @author LXY
 */
public class PersonInfoActivity extends BaseMvpActivity<PersonInfoPresenter> implements PersonInfoContract.View {


    @BindView(R.id.iv_back)
    ImageView ivBack;
    @BindView(R.id.iv_header)
    RoundedImageView ivHeader;
    @BindView(R.id.tv_username)
    AppCompatTextView tvUsername;
    @BindView(R.id.tv_change_avatar)
    AppCompatTextView tvChangeAvatar;
    @BindView(R.id.tv_email)
    AppCompatTextView tvEmail;
    @BindView(R.id.tv_profile)
    AppCompatTextView tvProfile;
    @BindView(R.id.rl_head)
    ScaleLinearLayout rlHead;
    @BindView(R.id.tv_change_nickname)
    TextView tvChangeNickname;
    @BindView(R.id.tv_change_password)
    TextView tvChangePassword;
    @BindView(R.id.tv_subcribe_info)
    TextView tvSubcribeInfo;
    @BindView(R.id.tv_version)
    TextView tvVersion;

    public static void start(Context context) {
        Intent starter = new Intent(context, PersonInfoActivity.class);
        context.startActivity(starter);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_person_info;
    }

    @Override
    protected void getIntent(Intent intent) {

    }

    @Override
    protected void initView() {
        ivBack.setVisibility(View.VISIBLE);
        tvChangeAvatar.setVisibility(View.VISIBLE);
        GlideUtils.loadCircleImageWithBoder(this, ivHeader, DataStore.getUserInfo().imgPortrait, R.drawable.avatar_bg_1);
        tvVersion.setText("V " + AppUtils.getVersionName(this));
        tvChangePassword.setVisibility(DataStore.getUserInfo().source==0?View.VISIBLE:View.GONE);
    }

    @Override
    protected void initListener() {
        //个人信息更新
        mPresenter.addDispose(RxBus.getDefault().toObservable(UserNotifyEvent.class)
                .subscribe(new Consumer<UserNotifyEvent>() {
                    @Override
                    public void accept(UserNotifyEvent userNotifyEvent) throws Exception {
                        if (userNotifyEvent != null) {
                            GlideUtils.loadCircleImageWithBoder(getContext(), ivHeader, DataStore.getUserInfo().imgPortrait, R.drawable.avatar_bg_1);
                        }
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
//                        ToastUtils.showShort(throwable.getMessage());
                    }
                }));

    }

    @Override
    protected void initData() {
    }

    @Override
    protected PersonInfoPresenter createPresenter() {
        return new PersonInfoPresenter();
    }

    @OnClick({R.id.iv_back, R.id.tv_change_avatar,R.id.iv_header, R.id.tv_change_nickname, R.id.tv_change_password, R.id.tv_subcribe_info})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                finish();
                break;
            case R.id.tv_change_avatar:
            case R.id.iv_header:
                ChooseAvatarActivity.start(this);
                break;
            case R.id.tv_change_nickname:
                ChangeNicknameActivity.start(this);
                break;
            case R.id.tv_change_password:
                ResetPasswordActivity.start(this);
                break;
            case R.id.tv_subcribe_info:
//                MySubscribeInfoActivity.start(this);
                StelaAnalyticsUtil.click("subscription_info_" + AppConstants.SUBSCRIPTION_INFO_URL);
                CommonWebActivity.start(this, AppConstants.SUBSCRIPTION_INFO_URL, false);
                break;
        }
    }

    @Override
    public void showError(String msg) {
    }
}
