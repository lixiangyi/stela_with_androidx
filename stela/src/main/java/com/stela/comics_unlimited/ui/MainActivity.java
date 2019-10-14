package com.stela.comics_unlimited.ui;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.PersistableBundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.tabs.TabLayout;
import com.lxy.baselibs.app.AppConstants;
import com.lxy.baselibs.rx.RxBus;
import com.lxy.baselibs.utils.DateUtils;
import com.lxy.baselibs.utils.LogUtils;
import com.stela.comics_unlimited.Base.BaseMvpActivity;
import com.stela.comics_unlimited.R;
import com.stela.comics_unlimited.app.DataStore;
import com.stela.comics_unlimited.data.entity.DialyInfo;
import com.stela.comics_unlimited.data.entity.NotificationEntity;
import com.stela.comics_unlimited.data.entity.PersonEntity;
import com.stela.comics_unlimited.data.entity.VersionEntity;
import com.stela.comics_unlimited.event.BrowesNotifyEvent;
import com.stela.comics_unlimited.event.DrawerBackEvent;
import com.stela.comics_unlimited.event.HomeNotifyEvent;
import com.stela.comics_unlimited.event.MainPositionNotify;
import com.stela.comics_unlimited.event.MessageNeedLoadNotifyEvent;
import com.stela.comics_unlimited.event.MessageNotifyEvent;
import com.stela.comics_unlimited.event.UserInfoUpdataNotifyEvent;
import com.stela.comics_unlimited.event.UserNotifyEvent;
import com.stela.comics_unlimited.pay.Inventory;
import com.stela.comics_unlimited.pay.Purchase;
import com.stela.comics_unlimited.ui.browse.BrowseFragment;
import com.stela.comics_unlimited.ui.home.activity.HomeInnerActivity;
import com.stela.comics_unlimited.ui.home.fragment.HomeFragment;
import com.stela.comics_unlimited.ui.mylibrary.MyLibraryFragment;
import com.stela.comics_unlimited.ui.person.SelectTitleActivity;
import com.stela.comics_unlimited.ui.series.SeriesActivity;
import com.stela.comics_unlimited.ui.web.CommonWebActivity;
import com.stela.comics_unlimited.util.FirebaseRemoteConfigHelper;
import com.stela.comics_unlimited.util.SharedPreferencesTool;
import com.stela.comics_unlimited.util.VersionUtil;
import com.stela.comics_unlimited.util.google_subs.GoogleSubscribeHelper;
import com.stela.comics_unlimited.util.stela_sdk.StelaAnalyticsUtil;
import com.stela.comics_unlimited.widget.BobbleView;
import com.stela.comics_unlimited.widget.NavUserLayout;

import java.util.List;

import butterknife.BindView;
import io.reactivex.functions.Consumer;


public class MainActivity extends BaseMvpActivity<MainPresenter> implements MainContract.View, GoogleSubscribeHelper.GoogleSubsInventoryInterface {

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.drawer_layout)
    DrawerLayout drawerLayout;
    @BindView(R.id.tb_mian)
    TabLayout tbMian;
    @BindView(R.id.fl_container)
    FrameLayout flContainer;
    @BindView(R.id.iv_right_icon)
    AppCompatImageView ivRightIcon;
    @BindView(R.id.nav_layout)
    NavUserLayout navLayout;
    @BindView(R.id.view_shadow)
    View viewShadow;

    private HomeFragment mHomeFragment;
    private BrowseFragment mBrowseFragment;
    private MyLibraryFragment mMyLibraryFragment;
    String[] mTitles = new String[]{"HOME", "BROWSE", "MY LIBRARY"};
    private int mCurrIndex;
    private boolean isExit = false;
    private int mPosition;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_main;
    }

    @Override
    protected void getIntent(Intent intent) {
//        DeepLinkHelper.deferredJump(this);

    }

    public static void start(Context context) {
        Intent starter = new Intent(context, MainActivity.class);
        context.startActivity(starter);
    }

    @Override
    protected void initView() {
        setSupportActionBar(toolbar);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();
        // 禁止手势滑动
        drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
        //修改图标
        toggle.setDrawerIndicatorEnabled(false);
        toolbar.setNavigationIcon(null);
        ivRightIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Crashlytics.getInstance().crash(); // Force a crash
                drawerLayout.openDrawer(GravityCompat.END);
                StelaAnalyticsUtil.click("profile");
                mPresenter.updataUser();
//                  测试 notification
//                StelaAnalyticsUtil.sendNotification(MainActivity.this);
            }
        });
        //
        initTab();
        switchFragment(mCurrIndex, "", "");
        mPresenter.updataUser();
        mPresenter.loadUnReadMessageNum();
        mPresenter.updateVersion(this);
        getNotificationJump(getIntent());
        FirebaseRemoteConfigHelper.initFirebaseRemoteConfig();
    }


    @Override
    protected void onNewIntent(Intent intent) {
        mPresenter.updataUser();
        RxBus.getDefault().post(new HomeNotifyEvent());
        getNotificationJump(intent);
    }

    /**
     * notification 跳转
     */
    private void getNotificationJump(Intent intent) {
        String type = intent.getStringExtra(AppConstants.NOTIFICATION_TYPE);
        if (TextUtils.isEmpty(type)) {
            return;
        }
        String notificationValueId = intent.getStringExtra(AppConstants.NOTIFICATION_VALUE_ID);
        String notificationId = intent.getStringExtra(AppConstants.NOTIFICATION_ID);
        String seriesId = intent.getStringExtra(AppConstants.NOTIFICATION_VALUE_ID_1);
        String homeTitle = intent.getStringExtra(AppConstants.HOME_PAGE_TITLE);
        switch (type) {
            case NotificationEntity.PLACEMENTVALUE_PAGE://Home page
                HomeInnerActivity.start(this, notificationValueId, homeTitle);
                break;
            case NotificationEntity.PLACEMENTVALUE_SERIES://seriespage
                SeriesActivity.start(this, notificationValueId);
                break;
            case NotificationEntity.PLACEMENTVALUE_CHAPTER://chapter page
                SeriesActivity.start(this, seriesId, notificationValueId, type);
                break;
            case NotificationEntity.PLACEMENTVALUE_WEB://web url
                CommonWebActivity.start(this, notificationValueId, false);
                break;
            case NotificationEntity.PLACEMENTVALUE_NO:
                switchFragment(2, type, notificationId);
                break;
            default:
                break;
        }
    }

    private void initTab() {
        for (int i = 0; i < mTitles.length; i++) {
            tbMian.addTab(tbMian.newTab().setText(mTitles[i]));
        }
        for (int i = 0; i < mTitles.length; i++) {
            TabLayout.Tab tab = tbMian.getTabAt(i);
            //注意！！！这里就是添加我们自定义的布局
            tab.setCustomView(getCustomView(i));
        }
        tbMian.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                //切换Fragment
                switchFragment(tab.getPosition(), "", "");

            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }

    //注意！！！这里就是我们自定义的布局tab_item
    public View getCustomView(int position) {
        View view = LayoutInflater.from(this).inflate(R.layout.tab_item_first, null);
        TextView tv = (TextView) view.findViewById(R.id.tv_tab);
        tv.setText(mTitles[position]);
        return view;
    }

    /**
     * 原理  去除Super 切断原有恢复逻辑 保存位置
     *
     * @param outState
     */
    @SuppressLint("MissingSuperCall")
    @Override
    protected void onSaveInstanceState(Bundle outState) {
//        super.onSaveInstanceState(outState);
        //解决fragment 重影
        /* 记录当前的position */
        outState.putInt("position", mPosition);
    }

    //
    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
//        super.onRestoreInstanceState(savedInstanceState);
        //解决fragment 重影
        mPosition = savedInstanceState.getInt("position");
        switchFragment(mPosition, "", "");
    }

    private void switchFragment(int position, String type,/*0 什么都不做  4 显示notification*/String notificationId) {

        viewShadow.setVisibility(position == 0 ? View.VISIBLE : View.GONE);
        // Fragment事务管理器
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        hideFragments(transaction);
        LogUtils.d("current position tab" + position);
        switch (position) {
            case 0: //首页
                if (mHomeFragment == null) {
                    mHomeFragment = HomeFragment.getInstance("");
                    transaction.add(R.id.fl_container, mHomeFragment, "home");
                } else {
                    transaction.show(mHomeFragment);
                }
                StelaAnalyticsUtil.click("home");
                RxBus.getDefault().post(new HomeNotifyEvent());
                break;
            case 1: //browse
                if (mBrowseFragment == null) {
                    mBrowseFragment = BrowseFragment.getInstance(mTitles[1]);
                    transaction.add(R.id.fl_container, mBrowseFragment, "browse");
                } else {
                    transaction.show(mBrowseFragment);
                }
                StelaAnalyticsUtil.click("browse");
                RxBus.getDefault().post(new BrowesNotifyEvent());
                break;
            case 2: //library
                if (mMyLibraryFragment == null) {
                    mMyLibraryFragment = MyLibraryFragment.getInstance(mTitles[2], type, notificationId);
                    transaction.add(R.id.fl_container, mMyLibraryFragment, "mylibrary");
                } else {
                    transaction.show(mMyLibraryFragment);
                }
                StelaAnalyticsUtil.click("library");
                break;
            default:
                if (mHomeFragment == null) {
                    mHomeFragment = HomeFragment.getInstance(mTitles[0]);
                    transaction.add(R.id.fl_container, mHomeFragment, "home");
                } else {
                    transaction.show(mHomeFragment);
                }
                break;
        }
        mCurrIndex = position;
        tbMian.getTabAt(mCurrIndex).select();
        transaction.commitAllowingStateLoss();
    }

    /**
     * 隐藏所有的Fragment
     *
     * @param transaction transaction
     */
    private void hideFragments(FragmentTransaction transaction) {
        if (null != mHomeFragment) {
            transaction.hide(mHomeFragment);
        }
        if (null != mBrowseFragment) {
            transaction.hide(mBrowseFragment);
        }
        if (null != mMyLibraryFragment) {
            transaction.hide(mMyLibraryFragment);
        }

    }

    @Override
    public void onSaveInstanceState(Bundle outState, PersistableBundle outPersistentState) {
        super.onSaveInstanceState(outState, outPersistentState);
        if (tbMian != null) {
            outState.putInt("currTabIndex", mCurrIndex);
        }
    }

    @Override
    protected void initListener() {
        mPresenter.addDispose(RxBus.getDefault().toObservable(MainPositionNotify.class)
                .subscribe(new Consumer<MainPositionNotify>() {
                    @Override
                    public void accept(MainPositionNotify mainPositionNotify) throws Exception {
                        if (mainPositionNotify != null) {
                            switchFragment(mainPositionNotify.postion, "", "");
                        }
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
//                            ToastUtils.showShort(throwable.getMessage());
                    }
                }));
        //接口个人信息更新
        mPresenter.addDispose(RxBus.getDefault().toObservable(UserInfoUpdataNotifyEvent.class)
                .subscribe(new Consumer<UserInfoUpdataNotifyEvent>() {
                    @Override
                    public void accept(UserInfoUpdataNotifyEvent userInfoUpdataNotifyEvent) throws Exception {
                        if (userInfoUpdataNotifyEvent != null) {
                            mPresenter.updataUser();
                        }
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
//                            ToastUtils.showShort(throwable.getMessage());
                    }
                }));
        //接受消息更新
        mPresenter.addDispose(RxBus.getDefault().toObservable(MessageNotifyEvent.class)
                .subscribe(new Consumer<MessageNotifyEvent>() {
                    @Override
                    public void accept(MessageNotifyEvent messageUnReadNotifyEvent) throws Exception {
                        if (messageUnReadNotifyEvent != null) {
                            // 刷新
                            if (tbMian != null) {
                                TabLayout.Tab tab = tbMian.getTabAt(2);
                                BobbleView bobbleView = tab.getCustomView().findViewById(R.id.bubble_num);
                                bobbleView.setNum(0);
                                if (messageUnReadNotifyEvent.num > 0) {
                                    bobbleView.setVisibility(View.VISIBLE);
                                } else {
                                    bobbleView.setVisibility(View.GONE);
                                }
                            }
                        }
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {

                    }
                }));
        //请求消息数量
        mPresenter.addDispose(RxBus.getDefault().toObservable(MessageNeedLoadNotifyEvent.class)
                .subscribe(new Consumer<MessageNeedLoadNotifyEvent>() {
                    @Override
                    public void accept(MessageNeedLoadNotifyEvent messageUnReadNotifyEvent) throws Exception {
                        if (messageUnReadNotifyEvent != null) {
                            // 刷新
                            mPresenter.loadUnReadMessageNum();
                        }
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {

                    }
                }));
        //个人信息更新
        mPresenter.addDispose(RxBus.getDefault().toObservable(UserNotifyEvent.class)
                .subscribe(new Consumer<UserNotifyEvent>() {
                    @Override
                    public void accept(UserNotifyEvent userNotifyEvent) throws Exception {
                        if (userNotifyEvent != null) {
                          navLayout.reload();
                        }
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
//                        ToastUtils.showShort(throwable.getMessage());
                    }
                }));
        //关闭抽屉
        mPresenter.addDispose(RxBus.getDefault().toObservable(DrawerBackEvent.class)
                .subscribe(new Consumer<DrawerBackEvent>() {
                    @Override
                    public void accept(DrawerBackEvent drawerBackEvent) throws Exception {
                        if (drawerBackEvent != null) {
                            drawerLayout.closeDrawer(GravityCompat.END);
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
    protected MainPresenter createPresenter() {
        return new MainPresenter();
    }

    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.END)) {
            drawerLayout.closeDrawer(GravityCompat.END);
        } else if (!isExit) {
            isExit = true;
            Toast.makeText(this, "Click again to exit the Stela!", Toast.LENGTH_SHORT).show();
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    isExit = false;
                }
            }, 2000);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public void showError(String msg) {
//        ToastUtils.showShort(msg);
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (navLayout != null) {
            navLayout.unbind();
        }
        if (DataStore.getUserInfo().subscriptionStatus == 0) {
            GoogleSubscribeHelper.onDestroy(this);
        }
        StelaAnalyticsUtil.close();
    }

    @Override
    protected void onResume() {
        super.onResume();
        DialyInfo dialyInfo = (DialyInfo) SharedPreferencesTool.getObj(this, SharedPreferencesTool.USERACTIVE_DIALY);
        if (dialyInfo == null || dialyInfo != null && !dialyInfo.userId.equals(DataStore.getUserInfo().id) ||
                dialyInfo != null && !DateUtils.isSameData(dialyInfo.saveTime, System.currentTimeMillis())) {
            mPresenter.userActive(this);
        }

    }

    @Override
    public void updateSuccess(PersonEntity result) {
        if (result.ifInterest == 0) {
            SelectTitleActivity.start(this);
        }
        DataStore.setUserInfo(result);
        navLayout.reload();
        if (TextUtils.isEmpty(result.notificationToken)) {
            //
            mPresenter.updateNotificationToken();
        }


        //---------------------------------sub 掉单------------------------
        if (result.subscriptionStatus == 0) {
            GoogleSubscribeHelper.initSubcribe(this);
            GoogleSubscribeHelper.setGoogleSubsInventoryInterface(this);
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (GoogleSubscribeHelper.onActivityResult(requestCode, resultCode, data)) {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    @Override
    public void verifyGooglePaySuccess(String s, Purchase purchase) {
        com.stela.analytics.model.Purchase p = new com.stela.analytics.model.Purchase();
        p.setProduct(purchase.getSku());
        p.setStatus("success");
        StelaAnalyticsUtil.purchase(p);
        // 更新个人接口
        // 更新个人界面
        RxBus.getDefault().post(new UserInfoUpdataNotifyEvent());
        //更新 read
        RxBus.getDefault().post(new HomeNotifyEvent());
    }

    @Override
    public void notificationCount(NotificationEntity notificationEntity) {
        RxBus.getDefault().postSticky(new MessageNotifyEvent(notificationEntity.notificationCount));
    }

    @Override
    public void versionResult(VersionEntity versionEntity) {
        VersionUtil.doVersionUpdate(this, versionEntity);
    }

    @Override
    public void updateNotificationSuccess(String s) {

    }

    @Override
    public void subInvSuccess(Inventory inventory) {
        List<Purchase> purchases = inventory.getAllPurchases();
        for (int i = 0; i < purchases.size(); i++) {
            mPresenter.verifyGooglePay(purchases.get(i));
            LogUtils.d(TAG, "purchases verifyGooglePaySuccess getSku: " + purchases.get(i).getSku());
        }
    }
}
