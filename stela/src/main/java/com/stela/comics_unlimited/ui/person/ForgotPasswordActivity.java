package com.stela.comics_unlimited.ui.person;

import android.content.Context;
import android.content.Intent;
import androidx.appcompat.widget.AppCompatTextView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.lxy.baselibs.utils.DisplayUtils;
import com.lxy.baselibs.utils.RegularUtils;
import com.stela.comics_unlimited.Base.BaseMvpActivity;
import com.stela.comics_unlimited.R;
import com.stela.comics_unlimited.app.DataStore;

import butterknife.BindView;
import butterknife.OnClick;

public class ForgotPasswordActivity extends BaseMvpActivity<ForgotPasswordPresenter> implements ForgotPasswordContract.View {


    @BindView(R.id.et_email_fp)
    EditText etEmailFp;
    @BindView(R.id.ll_normal)
    LinearLayout llNormal;
    @BindView(R.id.ll_success)
    LinearLayout llSuccess;
    @BindView(R.id.tv_submit_forgot_password)
    TextView tvSubmitForgotPassword;
    @BindView(R.id.tv_error_info)
    AppCompatTextView tvErrorInfo;
    @BindView(R.id.rl_error_info)
    RelativeLayout rlErrorInfo;
    @BindView(R.id.rl_bottom)
    RelativeLayout rlBottom;
    private boolean isSuccess;

    public static void start(Context context) {
        Intent starter = new Intent(context, ForgotPasswordActivity.class);
        context.startActivity(starter);
    }


    @Override
    protected int getLayoutId() {
        return R.layout.activity_forgot_password;
    }

    @Override
    protected void getIntent(Intent intent) {
        addLayoutListener(rlBottom, tvSubmitForgotPassword);
    }

    @Override
    protected void initView() {
        showBack();
        setTitle("Forgot Password");
        etEmailFp.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (RegularUtils.isEmail(etEmailFp.getText().toString().trim())) {
                    tvSubmitForgotPassword.setEnabled(true);
                } else {
                    tvSubmitForgotPassword.setEnabled(false);
                }
                if (TextUtils.isEmpty(etEmailFp.getText().toString().trim())) {
                    etEmailFp.setActivated(false);
                    rlErrorInfo.setVisibility(View.GONE);
                }

            }
        });
        if (!TextUtils.isEmpty(DataStore.getUserInfo().email)) {
            etEmailFp.setHint(DataStore.getUserInfo().email);
        }
        etEmailFp.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (etEmailFp != null) {
                    etEmailFp.setActivated(false);
                }
            }
        });

    }

    @Override
    protected void initListener() {

    }

    @Override
    protected void initData() {


    }

    @Override
    protected ForgotPasswordPresenter createPresenter() {
        return new ForgotPasswordPresenter();
    }


    @Override
    public void showError(String msg) {
        etEmailFp.setActivated(true);
        rlErrorInfo.setVisibility(View.VISIBLE);
        tvErrorInfo.setText(msg);
    }


    @OnClick(R.id.tv_submit_forgot_password)
    public void onViewClicked() {
        if (isSuccess) {
            finish();
        } else {
            DisplayUtils.closeInputMethod(this, etEmailFp);
            mPresenter.submitpassword(etEmailFp.getText().toString().trim());
        }
    }

    @Override
    public void sendSuccess(String s) {
        etEmailFp.setActivated(false);
        rlErrorInfo.setVisibility(View.GONE);
        llNormal.setVisibility(View.GONE);
        llSuccess.setVisibility(View.VISIBLE);
        isSuccess = true;
        tvSubmitForgotPassword.setText("Done");
    }
}
