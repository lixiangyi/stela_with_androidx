package com.stela.comics_unlimited.ui.login;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.LinkMovementMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.view.Window;
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
import com.stela.comics_unlimited.Base.BaseMvpActivity;
import com.stela.comics_unlimited.R;
import com.stela.comics_unlimited.app.DataStore;
import com.stela.comics_unlimited.data.entity.LoginInfo;
import com.stela.comics_unlimited.data.entity.PersonEntity;
import com.stela.comics_unlimited.ui.MainActivity;
import com.stela.comics_unlimited.ui.person.ForgotPasswordActivity;
import com.stela.comics_unlimited.ui.person.SelectTitleActivity;
import com.stela.comics_unlimited.ui.web.CommonWebActivity;
import com.stela.comics_unlimited.util.PasswordHelper;
import com.stela.comics_unlimited.util.SharedPreferencesTool;
import com.stela.comics_unlimited.util.SupportHelper;
import com.stela.comics_unlimited.util.google_login.GoogleLoginHelper;
import com.stela.comics_unlimited.util.permission.PermissionHelper;
import com.stela.comics_unlimited.util.stela_sdk.StelaAnalyticsUtil;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

public class LoginActivity extends BaseMvpActivity<LoginPresenter> implements LoginContract.View, GoogleLoginHelper.GoogleLoginInterface, PermissionHelper.PermissionInterface {

    @BindView(R.id.et_email)
    EditText etLoginEmail;
    @BindView(R.id.et_password)
    EditText etLoginPassword;
    @BindView(R.id.acb_register_forgot_password)
    AppCompatTextView acbRegisterForgotPassword;
    @BindView(R.id.acb_register_faq)
    AppCompatTextView acbRegisterFaq;
    @BindView(R.id.acb_register_cs)
    AppCompatTextView acbRegisterCs;
    @BindView(R.id.actv_register_version)
    AppCompatTextView actvRegisterVersion;
    @BindView(R.id.tv_login)
    AppCompatTextView tvLogin;
    @BindView(R.id.iv_password_eye)
    ImageView ivPasswordEye;
    @BindView(R.id.tv_error_info)
    AppCompatTextView tvErrorInfo;
    @BindView(R.id.rl_error_info)
    RelativeLayout rvErrorInfo;
    @BindView(R.id.ll_login_edit)
    LinearLayout llLoginEdit;
    @BindView(R.id.sign_in_button_google)
    SignInButton signInButton;
    @BindView(R.id.tv_go_register)
    AppCompatTextView tvGoRegister;
    private boolean isOpen = false;
    PermissionHelper permissionHelper = new PermissionHelper();
    private String mEmail;
    private int mLoginType;

    @Override
    protected void beforeSetView() {
        getWindow().requestFeature(Window.FEATURE_CONTENT_TRANSITIONS);
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//            Transition slide = TransitionInflater.from(this).inflateTransition(android.R.transition.slide_bottom);
//            getWindow().setEnterTransition(slide);
//        }
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_login;
    }

    @Override
    protected void getIntent(Intent intent) {
        mEmail = intent.getStringExtra(AppConstants.EMAIL);
        if (DataStore.isLogin()) {
            MainActivity.start(this);
            finish();
        }
    }

    public static void start(Context context) {
        Intent starter = new Intent(context, LoginActivity.class);
        context.startActivity(starter);
    }

    public static void start(Context context, String email) {
        Intent starter = new Intent(context, LoginActivity.class);
        starter.putExtra(AppConstants.EMAIL, email);
        context.startActivity(starter);
    }

    @Override
    protected void initView() {
        setTitle("Sign in");
        //跳转注册页面修饰
        initGoRegisterText();
        //  注册页面自动填充账号密码
        setEditHint();
        actvRegisterVersion.setText(AppUtils.getVersionName(this) + "");
        signInButton.setSize(SignInButton.SIZE_WIDE);

    }

    private void setEditHint() {
        LoginInfo loginInfo = (LoginInfo) SharedPreferencesTool.getObj(this, SharedPreferencesTool.KEY_LOGIN_INFO);
        if (loginInfo != null) {
            etLoginEmail.setText(loginInfo.email);
//            etLoginPassword.setText(loginInfo.password);
        }
        if (!TextUtils.isEmpty(mEmail)) {
            etLoginEmail.setText(mEmail);
        }
    }

    @Override
    protected void initListener() {
        initEdit();
//        addLayoutListener(rlBottom, tvLogin);
    }

    private void initEdit() {
        setEditActivitedFalse(etLoginEmail);
        setEditActivitedFalse(etLoginPassword);
    }

    private void setEditActivitedFalse(EditText editText) {
        editText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                etLoginPassword.setActivated(false);
                etLoginEmail.setActivated(false);
            }
        });
    }

    @Override
    protected void initData() {
        GoogleLoginHelper.init(this);
        permissionHelper.setPermissionInterface(this);
    }

    @Override
    protected LoginPresenter createPresenter() {
        return new LoginPresenter();
    }

    @OnClick({R.id.iv_password_eye, R.id.acb_register_faq, R.id.acb_register_cs, R.id.tv_login,
            R.id.acb_register_forgot_password, R.id.sign_in_button_google, R.id.tv_go_register})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_password_eye:
                if (isOpen) {
                    ivPasswordEye.setImageResource(R.mipmap.eye_close);
                    etLoginPassword.setTransformationMethod(PasswordTransformationMethod.getInstance());
                } else {
                    ivPasswordEye.setImageResource(R.mipmap.eye);
                    etLoginPassword.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                }
                isOpen = !isOpen;
                break;
            case R.id.acb_register_faq:
                CommonWebActivity.start(this, AppConstants.FQA_URL, false);
                StelaAnalyticsUtil.click("faq");
                break;

            case R.id.acb_register_cs:
                StelaAnalyticsUtil.click("support");
                SupportHelper.goEmail(this);
            case R.id.tv_login:
                mLoginType = PermissionHelper.NORMAL;
                permissionHelper.configAndroid6Permission(LoginActivity.this, true, PermissionHelper.NORMAL);
                break;
            case R.id.acb_register_forgot_password:
                ForgotPasswordActivity.start(this);
                StelaAnalyticsUtil.click("forgot_password");
                break;
            case R.id.sign_in_button_google:
                mLoginType = PermissionHelper.GOOGLE_LOGIN;
                permissionHelper.configAndroid6Permission(LoginActivity.this, true, PermissionHelper.GOOGLE_LOGIN);
                break;
            case R.id.tv_go_register:
                RegisterActivity.start(this);
                break;
            default:
                break;
        }
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
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        permissionHelper.onRequestPermissionsResult(this, requestCode, permissions, grantResults, mLoginType);
    }

    @Override
    public void GoogleLoginSuccess(GoogleSignInAccount acct) {
        if (acct != null) {
            String personEmail = acct.getEmail();
            String familyName = acct.getFamilyName();
            mPresenter.google_login(personEmail, familyName);
        }
    }

    @Override
    public void permissionSuccess(int type) {
        if (type == PermissionHelper.NORMAL) {
            doLogin();
        } else if (type == PermissionHelper.GOOGLE_LOGIN) {
            GoogleLoginHelper.signIn(this);
        }
    }

    private void doLogin() {
        DisplayUtils.closeInputMethod(this, etLoginEmail);
        //save data
        LoginInfo loginInfo = new LoginInfo();
        loginInfo.email = etLoginEmail.getText().toString().trim();
        loginInfo.password = etLoginPassword.getText().toString().trim();
        SharedPreferencesTool.saveObj(LoginActivity.this, SharedPreferencesTool.KEY_LOGIN_INFO, loginInfo);
        // keep view state normal tips
        rvErrorInfo.setVisibility(View.GONE);
        etLoginPassword.setActivated(false);
        etLoginEmail.setActivated(false);
        // 判断条件处理
        if (TextUtils.isEmpty(loginInfo.email) || !RegularUtils.isEmail(loginInfo.email)) {
            etLoginEmail.setActivated(true);
            rvErrorInfo.setVisibility(View.VISIBLE);
            tvErrorInfo.setText("Please enter a valid email address");
            return;
        }
        if (TextUtils.isEmpty(loginInfo.password)) {
            etLoginPassword.setActivated(true);
            rvErrorInfo.setVisibility(View.VISIBLE);
            tvErrorInfo.setText("password cannot be empty");
            return;
        }
        //login
        try {
            mPresenter.login(loginInfo.email, PasswordHelper.setPasswordSecret(loginInfo.password));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void login(PersonEntity result) {
        if (result != null && !TextUtils.isEmpty(result.id)) {
            //保存推送 authorization
            getNotification(result);
        }
    }

    @Override
    public void google_login(PersonEntity result) {
        if (result != null && !TextUtils.isEmpty(result.id)) {
            //保存推送 authorization
            getNotification(result);
            StelaAnalyticsUtil.login();
        }
    }

    @Override
    public void google_exist(String msg, String email) {
        etLoginEmail.setText(email);
    }


    private void getNotification(PersonEntity result) {
        gotoNext(result);
    }

    private void gotoNext(PersonEntity result) {
        DataStore.setUserInfo(result);
        StelaAnalyticsUtil.login();
        if (result.ifInterest == 1) {
            MainActivity.start(LoginActivity.this);
        } else {
            SelectTitleActivity.start(LoginActivity.this);
        }
        finish();
    }

    @Override
    public void showError(String msg) {
        rvErrorInfo.setVisibility(View.VISIBLE);
        tvErrorInfo.setText(msg);
    }

    private void initGoRegisterText() {
        List<Span> spans = new ArrayList<>();
        spans.add(new Span.Builder("No account? ")
                .foregroundColor(this, R.color.body_text_color_59)
                .build());
        spans.add(new Span.Builder("Create one")
                .foregroundColor(this, R.color.stela_blue)
                .build());
        CharSequence formattedText2 = Trestle.getFormattedText(spans);
        tvGoRegister.setMovementMethod(LinkMovementMethod.getInstance());
        tvGoRegister.setText(formattedText2);
    }

}
