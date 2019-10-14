package com.stela.comics_unlimited.ui.login;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.widget.AppCompatTextView;

import com.lxy.baselibs.app.AppConstants;
import com.lxy.baselibs.utils.AppUtils;
import com.lxy.baselibs.utils.ToastUtils;
import com.stela.comics_unlimited.Base.BaseMvpActivity;
import com.stela.comics_unlimited.R;
import com.stela.comics_unlimited.ui.web.CommonWebActivity;
import com.stela.comics_unlimited.util.stela_sdk.StelaAnalyticsUtil;
import com.stela.comics_unlimited.util.SupportHelper;

import butterknife.BindView;
import butterknife.OnClick;

public class AccountActivationActivity extends BaseMvpActivity<AccountActivationPresenter> implements AccountActivationContract.View {

    @BindView(R.id.tv_email)
    TextView tvEmail;
    @BindView(R.id.btn_resend)
    TextView btnResend;
    @BindView(R.id.tv_back_sign_in)
    AppCompatTextView tvBackSignIn;
    @BindView(R.id.acb_register_faq)
    AppCompatTextView acbRegisterFaq;
    @BindView(R.id.aciv_register_bottom_left)
    View acivRegisterBottomLeft;
    @BindView(R.id.acb_register_cs)
    AppCompatTextView acbRegisterCs;
    @BindView(R.id.aciv_register_bottom_right)
    View acivRegisterBottomRight;
    @BindView(R.id.actv_register_version)
    AppCompatTextView actvRegisterVersion;
    private String mEmail;

    public static void start(Context context, String email) {
        Intent starter = new Intent(context, AccountActivationActivity.class);
        starter.putExtra(AppConstants.EMAIL, email);
        context.startActivity(starter);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_account_activation;
    }

    @Override
    protected void getIntent(Intent intent) {
        mEmail = intent.getStringExtra(AppConstants.EMAIL);
    }

    @Override
    protected void initView() {
        setTitle("Account Activation");
        actvRegisterVersion.setText(AppUtils.getVersionName(this));
        tvEmail.setText(mEmail);
    }

    @Override
    protected void initListener() {

    }

    @Override
    protected void initData() {

    }

    @Override
    protected AccountActivationPresenter createPresenter() {
        return new AccountActivationPresenter();
    }


    @Override
    public void showError(String msg) {
        ToastUtils.showLong(msg);
        hideLoading();
    }

    @OnClick({R.id.btn_resend, R.id.tv_back_sign_in, R.id.acb_register_faq, R.id.acb_register_cs})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_resend:
                mPresenter.register(mEmail);
                break;
            case R.id.tv_back_sign_in:
                LoginActivity.start(this);
                break;
            case R.id.acb_register_faq:
                CommonWebActivity.start(this, AppConstants.FQA_URL, false);
                StelaAnalyticsUtil.click("faq");
                break;
            case R.id.acb_register_cs:
                StelaAnalyticsUtil.click("support");
                SupportHelper.goEmail(this);
                break;
        }
    }

    @Override
    public void reSendSuccess(String result) {
        hideLoading();
        ToastUtils.showLong(result);
    }
}
