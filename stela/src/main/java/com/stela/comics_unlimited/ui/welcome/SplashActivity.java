package com.stela.comics_unlimited.ui.welcome;

import android.content.Intent;
import android.net.Uri;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.lxy.baselibs.app.AppConstants;
import com.lxy.baselibs.utils.LogUtils;
import com.stela.comics_unlimited.Base.BaseMvpActivity;
import com.stela.comics_unlimited.R;
import com.stela.comics_unlimited.app.DataStore;
import com.stela.comics_unlimited.app.MyApp;
import com.stela.comics_unlimited.data.entity.NotificationEntity;
import com.stela.comics_unlimited.ui.MainActivity;
import com.stela.comics_unlimited.ui.login.LoginActivity;
import com.stela.comics_unlimited.util.SharedPreferencesTool;
import com.stela.comics_unlimited.util.ShortcutUtil;
import com.stela.comics_unlimited.util.deeplink.DeepLinkHelper;
import com.stela.comics_unlimited.util.facebook.FaceBookLogUtil;
import com.stela.comics_unlimited.util.stela_sdk.StelaAnalyticsUtil;
import com.stela.comics_unlimited.widget.CircleIndicator;
import com.stela.comics_unlimited.widget.GuideVideoView;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.OnClick;

public class SplashActivity extends BaseMvpActivity<SplashPresenter> implements SplashContract.View {

    @BindView(R.id.rl_normal)
    RelativeLayout rlNormal;
    @BindView(R.id.pager)
    ViewPager pager;
    @BindView(R.id.indicator)
    CircleIndicator indicator;
    @BindView(R.id.rl_guide)
    RelativeLayout rlGuide;
    @BindView(R.id.iv_colse)
    ImageView ivColse;
    private boolean mCanExit;
    private boolean mIsJump;
    private Intent mIntent;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_splash;
    }

    @Override
    protected void getIntent(Intent intent) {
        mIntent = intent;
        StelaAnalyticsUtil.appOpenPost();
    }

    @Override
    protected void beforeSetView() {
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
//            GoogleDiscut.setNotchWindow(this);
//        }
    }

    private void initDeeplink() {
        //facebook deeplink
        if (DeepLinkHelper.getAppLinkData() == null) {
            //非 facebook  deeplink首次安装
            if (DeepLinkHelper.AppLinkDataJump(this, mIntent)) {
                //这边要自己走跳转，不走默认的跳转
                mIsJump = true;
            }
        } else {
            // facebook  deeplink首次安装
            DeepLinkHelper.deferredJump(this);
        }
        //firebase deeplink
        DeepLinkHelper.getFirebaseDeeplinks(this, mIntent);
    }

    @Override
    protected void initView() {
        mCanExit = false;
        // 生成桌面快捷启动图标
        if (!ShortcutUtil.isShortcutInstalled(this)) {
            ShortcutUtil.addShortcutToDesktop(this);
        }
        mPresenter.jump2Login();

    }

    @Override
    protected void initListener() {

    }

    @Override
    protected void initData() {

    }

    @Override
    protected SplashPresenter createPresenter() {
        return new SplashPresenter();
    }


    @Override
    public void showError(String msg) {

    }

    @Override
    public void jump2Login() {
        initGuidePager();
    }

    @Override
    public void onBackPressed() {
        if (mCanExit) {
            super.onBackPressed();
        }
    }


    /**
     * 初始化引导图
     */
    private void initGuidePager() {
        if (TextUtils.isEmpty(SharedPreferencesTool.getString(this, SharedPreferencesTool.KEY_IS_FIRST_IN))) {
            StelaAnalyticsUtil.install();
        }
        if (!TextUtils.isEmpty(MyApp.NEED_UPDATE) && !MyApp.NEED_UPDATE.equals(SharedPreferencesTool.getString(this, SharedPreferencesTool.KEY_IS_FIRST_IN))) {
            mCanExit = true;
            rlGuide.setVisibility(View.VISIBLE);
            rlNormal.setVisibility(View.GONE);
            //判断是否展示引导图
//            Uri[] guideUri = {Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.step1), Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.step2), Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.step3)};
//            WelcomePagerAdapter mAdaper = new WelcomePagerAdapter(guideUri);
            ArrayList<Fragment> fragments = new ArrayList<>();
            fragments.add(VideoGudieFragment.getInstance(1));
            fragments.add(VideoGudieFragment.getInstance(2));
            fragments.add(VideoGudieFragment.getInstance(3));
            fragments.add(VideoGudieFragment.getInstance(0));
            MyWelcomeAdapter myWelcomeAdapter = new MyWelcomeAdapter(getSupportFragmentManager(), this, fragments);
            pager.setAdapter(myWelcomeAdapter);
            pager.setOffscreenPageLimit(2);
            pager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
                @Override
                public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                    if (position == 2 && positionOffset >= 0.2) {
                        FaceBookLogUtil.logCompleteTutorialEvent();
                        GuideToWelcome();
                    }
                }

                @Override
                public void onPageSelected(int position) {


                }

                @Override
                public void onPageScrollStateChanged(int state) {

                }
            });
            indicator.setViewPager(pager);
        } else {
            rlGuide.setVisibility(View.GONE);
            rlNormal.setVisibility(View.VISIBLE);
            ToWelcomePage();
        }

    }

    private void GuideToWelcome() {
        SharedPreferencesTool.putString(this, SharedPreferencesTool.KEY_IS_FIRST_IN, MyApp.NEED_UPDATE);
        ToWelcomePage();
    }

    private void ToWelcomePage() {
        initPushJump();
        initDeeplink();
        if (!mIsJump) {
            LoginActivity.start(this);
        }
        if (!mCanExit) {
            overridePendingTransition(0, 0); // 去除跳转动画
        }
        finish();
    }

    private void initPushJump() {
        if (!DataStore.isLogin()) {
            //走默认的跳转
            mIsJump = false;
            return;
        }
        if (getIntent().getExtras() != null) {
            // 在官网的发送notification 使用高级选项可以自定义 键值对，最终会在getIntent().getExtras()中获取到
            for (String s : getIntent().getExtras().keySet()) {
                LogUtils.d("initPushJump ", s + "--" + getIntent().getExtras().get(s));
            }
            NotificationEntity notificationEntity = new NotificationEntity();
            notificationEntity.destinationName = getIntent().getStringExtra("destinationName");
            notificationEntity.destinationValue = getIntent().getStringExtra("destinationValue");
            notificationEntity.placementValue = getIntent().getStringExtra("placementValue");
            notificationEntity.seriesValue = getIntent().getStringExtra("seriesValue");
            notificationEntity.messageTitle = getIntent().getStringExtra("messageTitle");
            notificationEntity.id = getIntent().getStringExtra("id");
            if (!TextUtils.isEmpty(notificationEntity.id)) {
                //这边要自己走跳转，不走默认的跳转
                mIsJump = true;
            }else {
                //走默认的跳转
                mIsJump = false;
                return;
            }
            //跳转处理
            Intent intent = new Intent(this, MainActivity.class);
            if (!TextUtils.isEmpty(notificationEntity.placementValue)) {
                switch (notificationEntity.placementValue) {
                    case NotificationEntity.PLACEMENTVALUE_PAGE://Home page
                        intent.putExtra(AppConstants.NOTIFICATION_VALUE_ID, notificationEntity.destinationValue);
                        intent.putExtra(AppConstants.HOME_PAGE_TITLE, notificationEntity.messageTitle);
                        intent.putExtra(AppConstants.NOTIFICATION_TYPE, notificationEntity.placementValue);
                        break;
                    case NotificationEntity.PLACEMENTVALUE_SERIES://seriespage
                    case NotificationEntity.PLACEMENTVALUE_WEB://web url
                        intent.putExtra(AppConstants.NOTIFICATION_VALUE_ID, notificationEntity.destinationValue);
                        intent.putExtra(AppConstants.NOTIFICATION_TYPE, notificationEntity.placementValue);
                        break;
                    case NotificationEntity.PLACEMENTVALUE_CHAPTER://chapter page
                        intent.putExtra(AppConstants.NOTIFICATION_VALUE_ID, notificationEntity.destinationValue);
                        intent.putExtra(AppConstants.NOTIFICATION_VALUE_ID_1, notificationEntity.seriesValue);
                        intent.putExtra(AppConstants.NOTIFICATION_TYPE, notificationEntity.placementValue);
                        break;
                    default:
                        break;
                }
            } else {
                intent.putExtra(AppConstants.NOTIFICATION_ID, notificationEntity.id);
                intent.putExtra(AppConstants.NOTIFICATION_TYPE, NotificationEntity.PLACEMENTVALUE_NO);
            }
            startActivity(intent);
        }

    }


    @OnClick(R.id.iv_colse)
    public void onViewClicked() {
        GuideToWelcome();
    }

    private class WelcomePagerAdapter extends PagerAdapter {
        Uri[] uris;

        public WelcomePagerAdapter(Uri[] uris) {
            this.uris = uris;
        }

        @Override
        public int getCount() {
            if (uris != null) {
                return uris.length;
            }
            return 0;
        }

        @Override
        public boolean isViewFromObject(View arg0, Object arg1) {
            return arg0 == arg1;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            View itemView = LayoutInflater.from(SplashActivity.this).inflate(R.layout.stela_guide_fragment_layout, container, false);
            GuideVideoView gvvPlayer = (GuideVideoView) itemView.findViewById(R.id.gvv_player);
            gvvPlayer.playVideo(uris[position]);//播放
            if (position == getCount() - 1) {
                itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        GuideToWelcome();
                    }
                });
            }
            container.addView(itemView);
            return itemView;
        }
    }
}
