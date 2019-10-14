package com.stela.comics_unlimited.ui.parental_controls;

import android.content.Context;
import android.content.Intent;

import androidx.appcompat.widget.AppCompatTextView;
import androidx.appcompat.widget.SwitchCompat;

import com.lxy.baselibs.app.AppConstants;
import com.lxy.baselibs.base.BaseActivity;
import com.stela.comics_unlimited.R;
import com.stela.comics_unlimited.app.DataStore;

import butterknife.BindView;
import butterknife.OnClick;

public class ParentalControlsActivity extends BaseActivity {
    public static final int CHILDSTATE = 1;
    public static final int NORMAL = 0;

    @BindView(R.id.title)
    AppCompatTextView title;
    @BindView(R.id.content)
    AppCompatTextView content;
    @BindView(R.id.switch_)
    SwitchCompat switch_parental;
    private int mChildrenState;

    public static void start(Context context) {
        Intent starter = new Intent(context, ParentalControlsActivity.class);
        context.startActivity(starter);
    }

    public static void start(Context context, int childrenState) {
        Intent starter = new Intent(context, ParentalControlsActivity.class);
        starter.putExtra(AppConstants.CHILDREN_STATE, childrenState);
        context.startActivity(starter);
    }


    @Override
    protected int getLayoutId() {
        return R.layout.activity_parental_controls;
    }

    @Override
    protected void getIntent(Intent intent) {

    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        mChildrenState = intent.getIntExtra(AppConstants.CHILDREN_STATE, -1);
        if (mChildrenState == -1) {
            finish();
            return;
        }

    }

    @Override
    protected void initView() {
        showBack();
        setTitle("Parental Controls");
        mChildrenState = DataStore.getUserInfo().childrenState;
    }

    @Override
    protected void initListener() {
    }

    @Override
    protected void initData() {

    }

    @Override
    protected void onResume() {
        super.onResume();
        switch_parental.setChecked(mChildrenState == CHILDSTATE);
    }


    @OnClick(R.id.switch_)
    public void onViewClicked() {
        if (mChildrenState == CHILDSTATE) {
            //处于少儿模式
            EnterPINActivity.start(ParentalControlsActivity.this);
        } else if (mChildrenState == NORMAL) {
            //非少儿模式时
            SetPINActivity.start(ParentalControlsActivity.this);
        }

    }
}
