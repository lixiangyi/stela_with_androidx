package com.stela.comics_unlimited.ui.series;

import android.app.ActivityOptions;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Pair;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.Toolbar;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.tabs.TabLayout;
import com.lxy.baselibs.app.AppConstants;
import com.lxy.baselibs.base.BaseActivity;
import com.lxy.baselibs.glide.GlideUtils;
import com.lxy.baselibs.rx.RxBus;
import com.lxy.baselibs.utils.LogUtils;
import com.lxy.baselibs.utils.ToastUtils;
import com.stela.analytics.model.Chapter;
import com.stela.comics_unlimited.Base.BaseFragment;
import com.stela.comics_unlimited.Base.BaseMvpActivity;
import com.stela.comics_unlimited.R;
import com.stela.comics_unlimited.app.DataStore;
import com.stela.comics_unlimited.data.entity.ChapterEntity;
import com.stela.comics_unlimited.data.entity.NotificationEntity;
import com.stela.comics_unlimited.data.entity.PersonEntity;
import com.stela.comics_unlimited.data.entity.SeriesEntity;
import com.stela.comics_unlimited.event.ContinueReadEvent;
import com.stela.comics_unlimited.event.DrawerBackEvent;
import com.stela.comics_unlimited.event.SeriesNotifyEvent;
import com.stela.comics_unlimited.event.UserInfoUpdataNotifyEvent;
import com.stela.comics_unlimited.event.UserNotifyEvent;
import com.stela.comics_unlimited.ui.chapter.ChapterActivity;
import com.stela.comics_unlimited.ui.innerSeries.ChapterListFragment;
import com.stela.comics_unlimited.ui.innerSeries.SeriesInfoFragment;
import com.stela.comics_unlimited.ui.subscribe.SubscribeActivity;
import com.stela.comics_unlimited.util.AppBarStateChangeListener;
import com.stela.comics_unlimited.util.stela_sdk.StelaAnalyticsUtil;
import com.stela.comics_unlimited.widget.NavUserLayout;
import com.stela.comics_unlimited.widget.ScaleView.ScaleImage;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.reactivex.functions.Consumer;

public class SeriesActivity extends BaseMvpActivity<SeriesPresenter> implements SeriesContract.View {
    private static String AWS_EVENT_READ = "";
    @BindView(R.id.tb_series)
    TabLayout tbSeries;
    @BindView(R.id.nav_layout)
    NavUserLayout navLayout;
    @BindView(R.id.drawer_layout)
    DrawerLayout drawerLayout;
    @BindView(R.id.vp_series)
    ViewPager vpSeries;
    @BindView(R.id.iv_series)
    ScaleImage ivSeries;
    @BindView(R.id.tv_chapter_read)
    TextView tvChapterRead;
    @BindView(R.id.appbar)
    AppBarLayout appbar;
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.iv_right_icon)
    AppCompatImageView ivRightIcon;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    String[] mSeriesTitles = new String[]{"SERIES INFO", "CHAPTERS"};
    @BindView(R.id.iv_title)
    AppCompatImageView ivTitle;
    private ArrayList<BaseFragment> baseFragmentList;
    private MySeriesPageAdapter mAdapter;
    private String mSeriesId;
    private SeriesEntity mSeriesEntity;
    private int MyContinueReadPosition = -1;
    private int mViewPagerState;
    private String mSeriesUrl;
    private boolean isLoaded;
    private boolean firstSelet;
    private String mNotificationType = "";
    private String mChapterId;

    public static void start(Context context, String id) {
        Intent starter = new Intent(context, SeriesActivity.class);
        starter.putExtra(AppConstants.SERIES_ID, id);
        context.startActivity(starter);
    }

    public static void start(Context context, String series_id, String chapter_id, String type) {
        Intent starter = new Intent(context, SeriesActivity.class);
        starter.putExtra(AppConstants.SERIES_ID, series_id);
        starter.putExtra(AppConstants.CHAPTER_ID, chapter_id);
        starter.putExtra(AppConstants.NOTIFICATION_TYPE, type);
        context.startActivity(starter);
    }

    public static void startShare(Context context, String id, View view, String url) {
        Intent starter = new Intent(context, SeriesActivity.class);
        starter.putExtra(AppConstants.SERIES_ID, id);
        starter.putExtra(AppConstants.SERIES_IMAGE, url);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            context.startActivity(starter, ActivityOptions.makeSceneTransitionAnimation((BaseActivity) context,
                    Pair.create(view, "image"))
                    .toBundle());
        } else {
            context.startActivity(starter);
        }
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_series;
    }

    @Override
    protected void getIntent(Intent intent) {
        getWindow().setBackgroundDrawableResource(R.color.transparent);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            postponeEnterTransition();
        }
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        mSeriesId = intent.getStringExtra(AppConstants.SERIES_ID);
        mSeriesUrl = intent.getStringExtra(AppConstants.SERIES_IMAGE);
        mNotificationType = intent.getStringExtra(AppConstants.NOTIFICATION_TYPE);
        mChapterId = intent.getStringExtra(AppConstants.CHAPTER_ID);
    }

    @Override
    protected void initView() {
        initToolbar();
        vpSeries.setCurrentItem(0);
        if (!TextUtils.isEmpty(mSeriesUrl)) {
            isLoaded = true;
            GlideUtils.loadImageShareHttps(this, ivSeries, mSeriesUrl, R.color.stela_blue);
        }
    }

    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.END)) {
            drawerLayout.closeDrawer(GravityCompat.END);
        } else {
            supportFinishAfterTransition();
        }
    }


    private void initToolbar() {
        showIcon();
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                supportFinishAfterTransition();
            }
        });
        appbar.addOnOffsetChangedListener(new AppBarStateChangeListener() {
            @Override
            public void onStateChanged(AppBarLayout appBarLayout, State state) {
                if (state == State.EXPANDED) {
                    //展开状态
                    showIcon();
                } else if (state == State.COLLAPSED) {
                    //折叠状态
                    showTitle();
                } else {
                    //中间状态

                }
            }
        });
        //防止AppBarLayout头部滑动不了，需要在数据加载出来后调用该方法
        appbar.post(new Runnable() {
            @Override
            public void run() {
                CoordinatorLayout.LayoutParams layoutParams = (CoordinatorLayout.LayoutParams) appbar.getLayoutParams();
                AppBarLayout.Behavior behavior = (AppBarLayout.Behavior) layoutParams.getBehavior();
                behavior.setDragCallback(new AppBarLayout.Behavior.DragCallback() {
                    @Override
                    public boolean canDrag(@NonNull AppBarLayout appBarLayout) {
                        return true;
                    }
                });
            }
        });

    }

    private void showIcon() {
        tvTitle.setVisibility(View.GONE);
        ivTitle.setVisibility(View.VISIBLE);
    }

    private void showTitle() {
        if (mSeriesEntity != null) {
            tvTitle.setText(mSeriesEntity.title);
            tvTitle.setVisibility(View.VISIBLE);
            ivTitle.setVisibility(View.GONE);
        }
    }

    private void initTab(SeriesEntity seriesEntity) {
        tbSeries.removeAllTabs();
        baseFragmentList = new ArrayList<>();
        for (int i = 0; i < mSeriesTitles.length; i++) {
            tbSeries.addTab(tbSeries.newTab().setText(mSeriesTitles[i]));
        }
        baseFragmentList.add(SeriesInfoFragment.getInstance(seriesEntity));
        baseFragmentList.add(ChapterListFragment.getInstance(seriesEntity.chapterList));
        mAdapter = new MySeriesPageAdapter(getSupportFragmentManager(), this, mSeriesTitles, baseFragmentList);
        vpSeries.setAdapter(mAdapter);
        tbSeries.setupWithViewPager(vpSeries);
        tbSeries.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                //切换Fragment
                vpSeries.setCurrentItem(tab.getPosition());
                switch (tab.getPosition()) {
                    case 0:
                        StelaAnalyticsUtil.click("series_info");
                        break;
                    case 1:
                        if (firstSelet) {
                            firstSelet = false;
                        } else {
                            StelaAnalyticsUtil.click("chapter_list");
                        }
                        break;
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
        tbSeries.getTabAt(1).select();
        firstSelet = true;
        vpSeries.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                if (position == 0 && positionOffset <= 0f && positionOffsetPixels == 0 && mViewPagerState == ViewPager.SCROLL_STATE_DRAGGING) {
                    appbar.setExpanded(false, true);
                }
            }

            @Override
            public void onPageSelected(int position) {
            }

            @Override
            public void onPageScrollStateChanged(int state) {
                mViewPagerState = state;
            }
        });
    }

    @Override
    protected void initListener() {
        //继续阅读更新
        mPresenter.addDispose(RxBus.getDefault().toObservable(ContinueReadEvent.class)
                .subscribe(new Consumer<ContinueReadEvent>() {
                    @Override
                    public void accept(ContinueReadEvent continueReadEvent) throws Exception {
                        if (continueReadEvent != null) {
                            // 刷新状态
                            if (mSeriesEntity != null) {
                                for (int i = 0; i < mSeriesEntity.chapterList.size(); i++) {
                                    if (i == continueReadEvent.mChapterPosition) {
                                        mSeriesEntity.chapterList.get(i).browseState = ChapterEntity.LASTREADSTATE_YES;
                                    } else {
                                        mSeriesEntity.chapterList.get(i).browseState = ChapterEntity.LASTREADSTATE_NO;
                                    }

                                }
                            }
                            initContinueRead();
                        }
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {

                    }
                }));
        //界面更新
        mPresenter.addDispose(RxBus.getDefault().toObservable(SeriesNotifyEvent.class)
                .subscribe(new Consumer<SeriesNotifyEvent>() {
                    @Override
                    public void accept(SeriesNotifyEvent seriesNotifyEvent) throws Exception {
                        if (seriesNotifyEvent != null) {
                            // 刷新状态
                            mPresenter.requestData(mSeriesId);
                            LogUtils.i("SeriesNotifyEvent requestData");
                        }
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {

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
        mPresenter.requestData(mSeriesId);
    }

    @Override
    protected SeriesPresenter createPresenter() {
        return new SeriesPresenter();
    }


    @Override
    public void showError(String msg) {
        ToastUtils.showShort(msg);
    }


    @Override
    public void showData(SeriesEntity seriesEntity) {
        mSeriesEntity = seriesEntity;
        if (!isLoaded) {
            if (seriesEntity.assets != null && seriesEntity.assets.size() > 0) {
                GlideUtils.loadImageHttps(this, ivSeries, seriesEntity.assets.get(0).url, R.color.stela_blue);
            } else {
                GlideUtils.loadImageHttps(this, ivSeries, mSeriesUrl, R.color.stela_blue);
            }
        }
        initTab(seriesEntity);
        initContinueRead();
        //直接跳转chapter
        if (!TextUtils.isEmpty(mNotificationType) && mNotificationType.equals(NotificationEntity.PLACEMENTVALUE_CHAPTER)) {
            int position = 0;
            if (mSeriesEntity != null && mSeriesEntity.chapterList != null && mSeriesEntity.chapterList.size() > 0) {
                for (int i = 0; i < mSeriesEntity.chapterList.size(); i++) {
                    if (mChapterId == mSeriesEntity.chapterList.get(i).id) {
                        position = i;
                        break;
                    }
                }
                ChapterActivity.start(this, mSeriesEntity.chapterList, position);
            }
        }
    }


    @Override
    public void updateSuccess(PersonEntity result) {
        DataStore.setUserInfo(result);
        navLayout.reload();
    }

    private void initContinueRead() {
        if (mSeriesEntity != null && mSeriesEntity.chapterList != null) {
            for (int i = 0; i < mSeriesEntity.chapterList.size(); i++) {
                if (mSeriesEntity.chapterList.get(i).browseState == ChapterEntity.LASTREADSTATE_YES) {
                    MyContinueReadPosition = i;
                    break;
                }
            }
        }
        tvChapterRead.setVisibility(View.VISIBLE);
        if (MyContinueReadPosition == -1) {
            MyContinueReadPosition = 0;
            tvChapterRead.setText("START READING");
            AWS_EVENT_READ = "start_reading";
        } else {
            tvChapterRead.setText("CONTINUE READING");
            AWS_EVENT_READ = "continue";
        }

    }


    @OnClick({R.id.iv_right_icon, R.id.tv_chapter_read})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_right_icon:
                drawerLayout.openDrawer(GravityCompat.END);
                StelaAnalyticsUtil.click("profile");
                mPresenter.updataUser();
                break;
            case R.id.tv_chapter_read:
                StelaAnalyticsUtil.click(AWS_EVENT_READ);
                if (mSeriesEntity.chapterList != null && mSeriesEntity.chapterList.size() > 0) {
                    switch (mSeriesEntity.chapterList.get(MyContinueReadPosition).chapterState) {
                        case ChapterEntity.CHAPTERSTATE_LOCK:
                            SubscribeActivity.start(this, mSeriesEntity.chapterList.get(MyContinueReadPosition).seriesId);
                            break;
                        case ChapterEntity.CHAPTERSTATE_DATA:
//                            ToastUtils.showShort("You can read At " + mSeriesEntity.chapterList.get(MyContinueReadPosition).expectedDate + "");
                            break;
                        case ChapterEntity.CHAPTERSTATE_FREE:
                        case ChapterEntity.CHAPTERSTATE_PAYED:
                            //埋点
                            Chapter chapter = new Chapter();
                            chapter.setChapterId(mSeriesEntity.chapterList.get(MyContinueReadPosition).id);
                            chapter.setChapterName(mSeriesEntity.chapterList.get(MyContinueReadPosition).title);
                            chapter.setSeriesId(mSeriesEntity.chapterList.get(MyContinueReadPosition).seriesId);
                            chapter.setSeriesName(mSeriesEntity.chapterList.get(MyContinueReadPosition).seriesTitle);
                            StelaAnalyticsUtil.chapter(chapter);
                            ChapterActivity.start(this, mSeriesEntity.chapterList, MyContinueReadPosition);
                            break;
                        default:
                            break;
                    }
                } else {
                    ToastUtils.showShort("no chapter data!");
                }
                break;
            default:
                break;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
    }
}