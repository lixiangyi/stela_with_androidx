package com.stela.comics_unlimited.ui.person;

import android.content.Context;
import android.content.Intent;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.appcompat.widget.AppCompatTextView;

import com.lxy.baselibs.rx.RxBus;
import com.stela.comics_unlimited.Base.BaseMvpActivity;
import com.stela.comics_unlimited.R;
import com.stela.comics_unlimited.event.UserNotifyEvent;
import com.stela.comics_unlimited.util.PasswordHelper;

import butterknife.BindView;
import butterknife.OnClick;

public class ResetPasswordActivity extends BaseMvpActivity<ResetPasswordPresenter> implements ResetPasswordContract.View {


    @BindView(R.id.et_current_password)
    EditText etCurrentPassword;
    @BindView(R.id.iv_current_password_eye)
    ImageView ivCurrentPasswordEye;
    @BindView(R.id.et_new_password)
    EditText etNewPassword;
    @BindView(R.id.iv_new_password_eye)
    ImageView ivNewPasswordEye;
    @BindView(R.id.iv_error)
    ImageView ivError;
    @BindView(R.id.tv_error_info)
    AppCompatTextView tvErrorInfo;
    @BindView(R.id.rl_error_info)
    RelativeLayout rlErrorInfo;
    @BindView(R.id.tv_change_password)
    TextView tvChangePassword;
    @BindView(R.id.tv_password_rules)
    AppCompatTextView tvPasswordRules;
    private boolean isOpen;

    public static void start(Context context) {
        Intent starter = new Intent(context, ResetPasswordActivity.class);
        context.startActivity(starter);
    }


    @Override
    protected int getLayoutId() {
        return R.layout.activity_reset_password;
    }

    @Override
    protected void getIntent(Intent intent) {
    }

    @Override
    protected void initView() {
        showBack();
        setTitle("Change Password");
    }

    @Override
    protected void initListener() {
    }

    @Override
    protected void initData() {

    }


    @Override
    protected ResetPasswordPresenter createPresenter() {
        return new ResetPasswordPresenter();
    }


    @Override
    public void showError(String msg) {
        etCurrentPassword.setActivated(true);
        rlErrorInfo.setVisibility(View.VISIBLE);
        tvErrorInfo.setText(msg);
    }


    @Override
    public void updataSuccess(String s) {
        etCurrentPassword.setActivated(false);
        etNewPassword.setActivated(false);
        rlErrorInfo.setVisibility(View.GONE);
        RxBus.getDefault().post(new UserNotifyEvent());
        finish();
    }


    @OnClick({R.id.iv_current_password_eye, R.id.iv_new_password_eye, R.id.tv_change_password})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_current_password_eye:
            case R.id.iv_new_password_eye:
                if (isOpen) {
                    ivCurrentPasswordEye.setImageResource(R.mipmap.eye_close);
                    etCurrentPassword.setTransformationMethod(PasswordTransformationMethod.getInstance());
                    ivNewPasswordEye.setImageResource(R.mipmap.eye_close);
                    etNewPassword.setTransformationMethod(PasswordTransformationMethod.getInstance());
                } else {
                    ivCurrentPasswordEye.setImageResource(R.mipmap.eye);
                    etCurrentPassword.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                    ivNewPasswordEye.setImageResource(R.mipmap.eye);
                    etNewPassword.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                }
                isOpen = !isOpen;
                break;
            case R.id.tv_change_password:
                String newPassword = etNewPassword.getText().toString().trim();
                if (PasswordHelper.initPassword(tvPasswordRules, newPassword)) {
                    etNewPassword.setActivated(true);
                    return;
                }
                mPresenter.updatePasswordOldAndNew(PasswordHelper.setPasswordSecret(etCurrentPassword.getText().toString().trim()), PasswordHelper.setPasswordSecret(newPassword));
                break;
        }
    }

}
