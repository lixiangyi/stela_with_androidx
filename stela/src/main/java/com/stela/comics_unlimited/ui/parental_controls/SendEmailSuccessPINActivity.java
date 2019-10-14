package com.stela.comics_unlimited.ui.parental_controls;

import android.content.Context;
import android.content.Intent;

import androidx.appcompat.widget.AppCompatTextView;

import com.lxy.baselibs.base.BaseActivity;
import com.stela.comics_unlimited.R;
import com.stela.comics_unlimited.app.DataStore;

import butterknife.BindView;

public class SendEmailSuccessPINActivity extends BaseActivity {


    @BindView(R.id.tv_pin_forgot_title)
    AppCompatTextView tvPinForgotTitle;
    @BindView(R.id.tv_pin_forgot_content)
    AppCompatTextView tvPinForgotContent;

    public static void start(Context context) {
        Intent starter = new Intent(context, SendEmailSuccessPINActivity.class);
        context.startActivity(starter);
    }


    @Override
    protected int getLayoutId() {
        return R.layout.activity_pin_forget_send_success;
    }

    @Override
    protected void getIntent(Intent intent) {
    }

    @Override
    protected void initView() {
        showBack();
        setTitle("Reset PIN");
        tvPinForgotContent.setText("Head to your inbox ( " + DataStore.getUserInfo().email + " ) and follow the instructions to reset your PIN.");
    }

    @Override
    protected void initListener() {
    }

    @Override
    protected void initData() {

    }

}
