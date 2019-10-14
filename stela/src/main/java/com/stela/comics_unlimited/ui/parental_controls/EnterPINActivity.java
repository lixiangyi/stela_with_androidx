package com.stela.comics_unlimited.ui.parental_controls;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;

import androidx.appcompat.widget.AppCompatTextView;
import androidx.vectordrawable.graphics.drawable.AnimatedVectorDrawableCompat;

import com.chaos.view.PinView;
import com.lxy.baselibs.rx.RxBus;
import com.lxy.baselibs.utils.ToastUtils;
import com.stela.comics_unlimited.Base.BaseMvpActivity;
import com.stela.comics_unlimited.R;
import com.stela.comics_unlimited.app.DataStore;
import com.stela.comics_unlimited.event.BrowesNotifyEvent;
import com.stela.comics_unlimited.event.CollectionNotifyEvent;
import com.stela.comics_unlimited.event.HomeNotifyEvent;
import com.stela.comics_unlimited.event.MessageNeedLoadNotifyEvent;
import com.stela.comics_unlimited.event.NotificationListNotifyEvent;
import com.stela.comics_unlimited.event.RecentReadNotifyEvent;
import com.stela.comics_unlimited.event.UserInfoUpdataNotifyEvent;
import com.stela.comics_unlimited.util.PasswordHelper;
import com.stela.comics_unlimited.util.Tools;

import butterknife.BindView;
import butterknife.OnClick;

public class EnterPINActivity extends BaseMvpActivity<EnterPINPresenter> implements EnterPINContract.View {

    @BindView(R.id.content)
    AppCompatTextView content;
    @BindView(R.id.pin_view)
    PinView pinView;
    @BindView(R.id.btn_pin_submit)
    AppCompatTextView btnPin;
    @BindView(R.id.tv_pin_forget)
    AppCompatTextView tvPinForget;

    public static void start(Context context) {
        Intent starter = new Intent(context, EnterPINActivity.class);
        context.startActivity(starter);
    }


    @Override
    protected int getLayoutId() {
        return R.layout.activity_pin_enter;
    }

    @Override
    protected void getIntent(Intent intent) {

    }

    @Override
    protected void initView() {
        showBack();
        setTitle("Enter PIN");
        Drawable avd = AnimatedVectorDrawableCompat.create(this, R.drawable.anim_pin_input);
        pinView.setItemBackground(avd);
        Tools.showSoftInputFromWindow(this, pinView);
    }

    @Override
    protected void initListener() {
        pinView.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length()==4) {
                    btnPin.setEnabled(true);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                Log.i(TAG, "afterTextChanged: "+s);

            }
        });
    }

    @Override
    protected void initData() {

    }


    @Override
    protected EnterPINPresenter createPresenter() {
        return new EnterPINPresenter();
    }


    @Override
    public void showError(String msg) {
        hideLoading();
        ToastUtils.showLong(msg);
    }


    @Override
    public void updataSuccess(String s) {
        ToastUtils.showShort(s);
        ParentalControlsActivity.start(this);
        RxBus.getDefault().post(new HomeNotifyEvent());
        RxBus.getDefault().post(new BrowesNotifyEvent());
        RxBus.getDefault().post(new CollectionNotifyEvent());
        RxBus.getDefault().post(new RecentReadNotifyEvent());
        RxBus.getDefault().post(new UserInfoUpdataNotifyEvent());
        RxBus.getDefault().post(new NotificationListNotifyEvent());
        RxBus.getDefault().post(new MessageNeedLoadNotifyEvent());
    }

    @Override
    public void forgotPinSendSuccess(String s) {
        SendEmailSuccessPINActivity.start(this);
    }

    @OnClick({R.id.btn_pin_submit, R.id.tv_pin_forget})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_pin_submit:
                mPresenter.updatePin(PasswordHelper.setPasswordSecret(pinView.getText().toString().trim()));
                break;
            case R.id.tv_pin_forget:
                mPresenter.forgetPin(DataStore.getUserInfo().email);
                break;
        }
    }
}
