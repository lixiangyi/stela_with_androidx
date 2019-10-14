package com.stela.comics_unlimited.ui.parental_controls;

import android.content.Context;
import android.content.Intent;

import androidx.appcompat.widget.AppCompatTextView;

import com.chaos.view.PinView;
import com.lxy.baselibs.rx.RxBus;
import com.lxy.baselibs.utils.ToastUtils;
import com.stela.comics_unlimited.Base.BaseMvpActivity;
import com.stela.comics_unlimited.R;
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

import static com.stela.comics_unlimited.ui.parental_controls.ParentalControlsActivity.CHILDSTATE;

public class SetPINActivity extends BaseMvpActivity<SetPINPresenter> implements SetPINContract.View {


    @BindView(R.id.content)
    AppCompatTextView content;
    @BindView(R.id.pin_view)
    PinView pinView;
    @BindView(R.id.btn_pin)
    AppCompatTextView btnPin;

    public static void start(Context context) {
        Intent starter = new Intent(context, SetPINActivity.class);
        context.startActivity(starter);
    }


    @Override
    protected int getLayoutId() {
        return R.layout.activity_pin_set;
    }

    @Override
    protected void getIntent(Intent intent) {
    }

    @Override
    protected void initView() {
        showBack();
        setTitle("Set PIN");
        pinView.setAnimationEnable(true);// start animation when adding text
        Tools.showSoftInputFromWindow(this, pinView);
    }

    @Override
    protected void initListener() {
    }

    @Override
    protected void initData() {

    }


    @Override
    protected SetPINPresenter createPresenter() {
        return new SetPINPresenter();
    }


    @Override
    public void showError(String msg) {
        hideLoading();
        ToastUtils.showShort(msg);
    }


    @Override
    public void updataSuccess(String s) {
        ToastUtils.showShort(s);
        ParentalControlsActivity.start(this, CHILDSTATE);
        RxBus.getDefault().post(new HomeNotifyEvent());
        RxBus.getDefault().post(new BrowesNotifyEvent());
        RxBus.getDefault().post(new CollectionNotifyEvent());
        RxBus.getDefault().post(new RecentReadNotifyEvent());
        RxBus.getDefault().post(new UserInfoUpdataNotifyEvent());
        RxBus.getDefault().post(new NotificationListNotifyEvent());
        RxBus.getDefault().post(new MessageNeedLoadNotifyEvent());
    }

    @OnClick(R.id.btn_pin)
    public void onViewClicked() {
        mPresenter.updatePin( PasswordHelper.setPasswordSecret(pinView.getText().toString().trim()));
    }
}
