package com.stela.comics_unlimited.ui.mylibrary;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;
import com.lxy.baselibs.app.AppConstants;
import com.lxy.baselibs.rx.RxBus;
import com.lxy.baselibs.utils.AppUtils;
import com.lxy.baselibs.utils.ToastUtils;
import com.stela.comics_unlimited.Base.BaseFragment;
import com.stela.comics_unlimited.R;
import com.stela.comics_unlimited.data.entity.NotificationEntity;
import com.stela.comics_unlimited.event.MessageNotifyEvent;
import com.stela.comics_unlimited.ui.browse.MyViewPageAdapter;
import com.stela.comics_unlimited.ui.mylibrary.innerLibrary.CollectionFragment;
import com.stela.comics_unlimited.ui.mylibrary.innerLibrary.NotificationFragment;
import com.stela.comics_unlimited.ui.mylibrary.innerLibrary.RecentReadFragment;
import com.stela.comics_unlimited.util.stela_sdk.StelaAnalyticsUtil;
import com.stela.comics_unlimited.widget.BobbleView;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.Unbinder;
import io.reactivex.functions.Consumer;


/**
 * @author LXY
 * @date 2019/1/23 22:57
 * @desc
 */
public class MyLibraryFragment extends BaseFragment<MyLibraryPresenter> implements MyLibraryContract.View {

    @BindView(R.id.tb_library)
    TabLayout tbLibrary;
    @BindView(R.id.vp_library)
    ViewPager vpLibrary;
    Unbinder unbinder;
    private String[] mTitles = new String[]{"Collection", "Recently Read", "Notifications"};
    private ArrayList<BaseFragment> baseFragmentList = new ArrayList<>();
    private MyViewPageAdapter mAdapter;
    private String mTitle;
    private String notificationType;
    private String notificationId;

    public static MyLibraryFragment getInstance(String title, String type, String notificationId) {
        MyLibraryFragment fragment = new MyLibraryFragment();
        Bundle bundle = new Bundle();
        bundle.putString(AppConstants.TITLE, title);
        bundle.putString(AppConstants.NOTIFICATION_TYPE, type);
        bundle.putString(AppConstants.NOTIFICATION_ID, notificationId);
        fragment.setArguments(bundle);
        fragment.mTitle = title;
        return fragment;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_my_library;
    }

    @Override
    protected MyLibraryPresenter createPresenter() {
        return new MyLibraryPresenter();
    }

    @Override
    protected void initView() {
        AppUtils.setScale(getBaseActivity());
        notificationType = getArguments().getString(AppConstants.NOTIFICATION_TYPE);
        notificationId = getArguments().getString(AppConstants.NOTIFICATION_ID);
        initTab();
    }

    @Override
    protected void initListener() {
        //界面更新
        mPresenter.addDispose(RxBus.getDefault().toObservableSticky(MessageNotifyEvent.class)
                .subscribe(new Consumer<MessageNotifyEvent>() {
                    @Override
                    public void accept(MessageNotifyEvent messageUnReadNotifyEvent) throws Exception {
                        if (messageUnReadNotifyEvent != null) {
                            // 刷新
                            if (tbLibrary != null && mAdapter != null) {
                                TabLayout.Tab tab = tbLibrary.getTabAt(2);
                                BobbleView bobbleView = tab.getCustomView().findViewById(R.id.bubble_num);
                                if (messageUnReadNotifyEvent.num > 0) {
                                    bobbleView.setVisibility(View.VISIBLE);
                                    bobbleView.setNum(messageUnReadNotifyEvent.num);
                                } else {
                                    bobbleView.setVisibility(View.GONE);
                                    bobbleView.setNum(0);
                                }
                            }
                            RxBus.getDefault().removeStickyEvent(MessageNotifyEvent.class);
                        }
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {

                    }
                }));
    }

    @Override
    protected void initData() {

    }

    private void initTab() {
        for (int i = 0; i < mTitles.length; i++) {
            tbLibrary.addTab(tbLibrary.newTab().setText(mTitles[i]));
        }
        baseFragmentList.add(CollectionFragment.getInstance(mTitles[0]));
        baseFragmentList.add(RecentReadFragment.getInstance(mTitles[1]));
        baseFragmentList.add(NotificationFragment.getInstance(mTitles[2], notificationType, notificationId));
        mAdapter = new MyViewPageAdapter(getFragmentManager(), getContext(), mTitles, baseFragmentList);
        vpLibrary.setAdapter(mAdapter);
        tbLibrary.setupWithViewPager(vpLibrary);
        for (int i = 0; i < mTitles.length; i++) {
            TabLayout.Tab tab = tbLibrary.getTabAt(i);
            //注意！！！这里就是添加我们自定义的布局
            tab.setCustomView(mAdapter.getCustomView(i));
        }
        tbLibrary.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                ((TextView) tab.getCustomView().findViewById(R.id.tv_tab)).setSelected(true);
                vpLibrary.setCurrentItem(tab.getPosition());
                switch (tab.getPosition()) {
                    case 0:
                        StelaAnalyticsUtil.click("collection");
                        break;
                    case 1:
                        StelaAnalyticsUtil.click("recently_read");
                        break;
                    case 2:
                        StelaAnalyticsUtil.click("notification");
                        break;
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                ((TextView) tab.getCustomView().findViewById(R.id.tv_tab)).setSelected(false);
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
        if (!TextUtils.isEmpty(notificationType)&& notificationType.equals(NotificationEntity.PLACEMENTVALUE_NO)) {
            tbLibrary.getTabAt(2).select();
//            vpLibrary.setCurrentItem(2);
        }
    }

    @Override
    protected boolean useEventBus() {
        return false;
    }

    @Override
    public void showError(String msg) {
        ToastUtils.showShort(msg);
    }

}
