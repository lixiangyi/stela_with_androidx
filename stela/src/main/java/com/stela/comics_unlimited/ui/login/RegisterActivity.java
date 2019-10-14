package com.stela.comics_unlimited.ui.login;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.LinkMovementMethod;
import android.text.method.PasswordTransformationMethod;
import android.text.style.ClickableSpan;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatTextView;

import com.etiennelawlor.trestle.library.Span;
import com.etiennelawlor.trestle.library.Trestle;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.common.SignInButton;
import com.lxy.baselibs.app.AppConstants;
import com.lxy.baselibs.utils.AppUtils;
import com.lxy.baselibs.utils.DisplayUtils;
import com.lxy.baselibs.utils.RegularUtils;
import com.stela.analytics.model.Register;
import com.stela.comics_unlimited.Base.BaseMvpActivity;
import com.stela.comics_unlimited.R;
import com.stela.comics_unlimited.app.DataStore;
import com.stela.comics_unlimited.data.entity.LoginInfo;
import com.stela.comics_unlimited.data.entity.PersonEntity;
import com.stela.comics_unlimited.ui.MainActivity;
import com.stela.comics_unlimited.ui.person.SelectTitleActivity;
import com.stela.comics_unlimited.ui.web.CommonWebActivity;
import com.stela.comics_unlimited.util.PasswordHelper;
import com.stela.comics_unlimited.util.RsaUtils;
import com.stela.comics_unlimited.util.SharedPreferencesTool;
import com.stela.comics_unlimited.util.SupportHelper;
import com.stela.comics_unlimited.util.banner.BannerImageLoaderRegister;
import com.stela.comics_unlimited.util.facebook.FaceBookLogUtil;
import com.stela.comics_unlimited.util.google_login.GoogleLoginHelper;
import com.stela.comics_unlimited.util.permission.PermissionHelper;
import com.stela.comics_unlimited.util.stela_sdk.StelaAnalyticsUtil;
import com.youth.banner.Banner;
import com.youth.banner.BannerConfig;
import com.youth.banner.Transformer;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;


public class RegisterActivity extends BaseMvpActivity<RegisterPresenter> implements RegisterContract.View, GoogleLoginHelper.GoogleLoginInterface,
        PermissionHelper.PermissionInterface {

    @BindView(R.id.sign_in_button_google)
    SignInButton signInButton;
    @BindView(R.id.banner_register)
    Banner banner;
    @BindView(R.id.et_email)
    EditText etRegisterEmail;
    @BindView(R.id.et_password)
    EditText etRegisterPassword;
    @BindView(R.id.iv_password_eye)
    ImageView ivPasswordEye;
    @BindView(R.id.tv_error_info)
    AppCompatTextView tvErrorInfo;
    @BindView(R.id.ll_login_edit)
    LinearLayout llLoginEdit;
    @BindView(R.id.tv_submit)
    AppCompatTextView tvRegister;
    @BindView(R.id.tv_register_desc)
    AppCompatTextView tvRegisterDesc;
    @BindView(R.id.acb_register_faq)
    AppCompatTextView acbRegisterFaq;
    @BindView(R.id.acb_register_cs)
    AppCompatTextView acbRegisterCs;
    @BindView(R.id.actv_register_version)
    AppCompatTextView actvRegisterVersion;
    @BindView(R.id.rl_error_info)
    RelativeLayout rvErrorInfo;
    @BindView(R.id.tv_password_rules)
    AppCompatTextView tvPasswordRules;
    private boolean isOpen;
    PermissionHelper permissionHelper = new PermissionHelper();
    private int mLoginType;

    public static void start(Context context) {
        Intent starter = new Intent(context, RegisterActivity.class);
        context.startActivity(starter);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_register;
    }

    @Override
    protected void getIntent(Intent intent) {

    }

    @Override
    protected void initView() {
        setTitle("Create an Account");
        showBack();
        initEdit();
        initResgisterRes();
        //  注册页面自动填充账号密码
        setEditHint();
        actvRegisterVersion.setText(AppUtils.getVersionName(this));
        signInButton.setSize(SignInButton.SIZE_WIDE);
    }

    private void setEditHint() {
        LoginInfo loginInfo = (LoginInfo) SharedPreferencesTool.getObj(this, SharedPreferencesTool.KEY_LOGIN_INFO);
        if (loginInfo != null) {
            etRegisterEmail.setText(loginInfo.email);
//            etRegisterPassword.setText(loginInfo.password);
        }
    }

    @Override
    protected void initListener() {
    }

    @Override
    protected void initData() {
        initBanner();
        GoogleLoginHelper.init(this);
        permissionHelper.setPermissionInterface(this);
    }

    @Override
    protected RegisterPresenter createPresenter() {
        return new RegisterPresenter();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        GoogleLoginHelper.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        permissionHelper.dissPermissionInterface();
    }

    @Override
    public void GoogleLoginSuccess(GoogleSignInAccount acct) {
        if (acct != null) {
            String personEmail = acct.getEmail();
            String familyName = acct.getFamilyName();
            mPresenter.google_login(personEmail, familyName);
        }
    }


    @OnClick({R.id.iv_password_eye, R.id.tv_submit, R.id.acb_register_faq, R.id.acb_register_cs, R.id.sign_in_button_google})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_password_eye:
                if (isOpen) {
                    ivPasswordEye.setImageResource(R.mipmap.eye_close);
                    etRegisterPassword.setTransformationMethod(PasswordTransformationMethod.getInstance());
                } else {
                    ivPasswordEye.setImageResource(R.mipmap.eye);
                    etRegisterPassword.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                }
                isOpen = !isOpen;
                break;
            case R.id.sign_in_button_google:
                mLoginType = PermissionHelper.GOOGLE_LOGIN;
                permissionHelper.configAndroid6Permission(RegisterActivity.this, true, PermissionHelper.GOOGLE_LOGIN);
                break;
            case R.id.acb_register_faq:
                CommonWebActivity.start(this, AppConstants.FQA_URL, false);
                StelaAnalyticsUtil.click("faq");
                break;
            case R.id.acb_register_cs:
                StelaAnalyticsUtil.click("support");
                SupportHelper.goEmail(this);
                break;
            case R.id.tv_submit:
                mLoginType = PermissionHelper.NORMAL;
                permissionHelper.configAndroid6Permission(RegisterActivity.this, true, PermissionHelper.NORMAL);
                break;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        permissionHelper.onRequestPermissionsResult(this, requestCode, permissions, grantResults, mLoginType);
    }


    @Override
    public void register(String result) {
        Register register = new Register();
        register.setStatus("success");
        StelaAnalyticsUtil.register(register, ((LoginInfo) SharedPreferencesTool.getObj(this, SharedPreferencesTool.KEY_LOGIN_INFO)).email);
        FaceBookLogUtil.logCompleteRegistrationEvent();
        AccountActivationActivity.start(this, etRegisterEmail.getText().toString().trim());
    }

    @Override
    public void google_login(PersonEntity result) {
        if (result != null && !TextUtils.isEmpty(result.id)) {
            //保存推送 authorization
            getNotification(result);
        }
    }

    @Override
    public void google_exist(String msg, String email) {
        LoginActivity.start(this, email);
        finish();
    }


    private void getNotification(PersonEntity result) {
        gotoNext(result);
    }

    private void gotoNext(PersonEntity result) {
        DataStore.setUserInfo(result);
        StelaAnalyticsUtil.login();
        if (result.ifInterest == 1) {
            MainActivity.start(RegisterActivity.this);
        } else {
            SelectTitleActivity.start(RegisterActivity.this);
        }
        finish();
    }

    @Override
    public void showError(String msg) {
        hideLoading();
        rvErrorInfo.setVisibility(View.VISIBLE);
        tvErrorInfo.setText(msg);
    }


    @Override
    public void permissionSuccess(int type) {
        if (type == PermissionHelper.NORMAL) {
            doRegister();
        } else if (type == PermissionHelper.GOOGLE_LOGIN) {
            GoogleLoginHelper.signIn(this);
        }


    }

    private void doRegister() {
        //关闭键盘
        DisplayUtils.closeInputMethod(this, etRegisterEmail);
        //get edit info
        LoginInfo loginInfo = new LoginInfo();
        loginInfo.email = etRegisterEmail.getText().toString().trim();
        loginInfo.password = etRegisterPassword.getText().toString().trim();
        //save data
        SharedPreferencesTool.saveObj(RegisterActivity.this, SharedPreferencesTool.KEY_LOGIN_INFO, loginInfo);

        // keep view state normal tips
        rvErrorInfo.setVisibility(View.GONE);
        etRegisterPassword.setActivated(false);
        etRegisterEmail.setActivated(false);
        // 判断条件处理
        if (TextUtils.isEmpty(loginInfo.email) || !RegularUtils.isEmail(loginInfo.email)) {
            etRegisterEmail.setActivated(true);
            rvErrorInfo.setVisibility(View.VISIBLE);
            tvErrorInfo.setText("Please enter a valid email address");
            return;
        }
        if (PasswordHelper.initPassword(tvPasswordRules, loginInfo.password)) {
            etRegisterPassword.setActivated(true);
            return;
        }
        //register
        try {
            mPresenter.register(loginInfo.email, RsaUtils.encrypt(loginInfo.password.getBytes(),
                    RsaUtils.loadPublicKey(RsaUtils.publicKey)),
                    AppUtils.getVersionCode(RegisterActivity.this) + "");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void initEdit() {
        setEditActivitedFalse(etRegisterEmail);
        setEditActivitedFalse(etRegisterPassword);
    }

    private void setEditActivitedFalse(EditText editText) {
        editText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                etRegisterPassword.setActivated(false);
                etRegisterEmail.setActivated(false);
            }
        });
    }

    private void initResgisterRes() {

        List<Span> spans1 = new ArrayList<>();
        spans1.add(new Span.Builder("By creating an account you agree to our ")
                .foregroundColor(this, R.color.color_73)
                .build());
        ClickableSpan clickableSpan = new ClickableSpan() {
            @Override
            public void onClick(View view) {
                CommonWebActivity.start(RegisterActivity.this, AppConstants.TERMS_AND_SERVICE_URL, false);
            }
        };
        ClickableSpan clickableSpan1 = new ClickableSpan() {
            @Override
            public void onClick(View view) {
                CommonWebActivity.start(RegisterActivity.this, AppConstants.PRIVACY_POLICY_URL, false);
            }
        };
        spans1.add(new Span.Builder("Terms of Service")
                .foregroundColor(this, R.color.stela_blue)
                .clickableSpan(clickableSpan)
                .build());
        spans1.add(new Span.Builder(" and ")
                .foregroundColor(this, R.color.color_73)
                .build());
        spans1.add(new Span.Builder("Privacy Policy")
                .foregroundColor(this, R.color.stela_blue)
                .clickableSpan(clickableSpan1)
                .build());
        CharSequence formattedText2 = Trestle.getFormattedText(spans1);
        tvRegisterDesc.setMovementMethod(LinkMovementMethod.getInstance());
        tvRegisterDesc.setText(formattedText2);
    }

    private void initBanner() {
        Integer[] mBannerImgs = new Integer[]{R.mipmap.setup_banners_1, R.mipmap.setup_banners_2, R.mipmap.setup_banners_3};
        //设置banner样式
        banner.setBannerStyle(BannerConfig.CIRCLE_INDICATOR);
        //设置图片加载器
        banner.setImageLoader(new BannerImageLoaderRegister());
        //设置图片集合
        banner.setImages(Arrays.asList(mBannerImgs));
        //设置banner动画效果
        banner.setBannerAnimation(Transformer.Default);
        //设置自动轮播，默认为true
        banner.isAutoPlay(true);
        banner.setDelayTime(4000);
        //设置指示器位置（当banner模式中有指示器时）
        banner.setIndicatorGravity(BannerConfig.CENTER);
        //banner设置方法全部调用完毕时最后调用
        banner.start();
    }
}
