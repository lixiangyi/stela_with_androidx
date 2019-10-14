package com.stela.comics_unlimited.ui.browse;

import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;
import com.lxy.baselibs.app.AppConstants;
import com.lxy.baselibs.rx.RxBus;
import com.lxy.baselibs.utils.ToastUtils;
import com.stela.analytics.model.Page;
import com.stela.comics_unlimited.Base.BaseFragment;
import com.stela.comics_unlimited.R;
import com.stela.comics_unlimited.data.entity.ImgEntity;
import com.stela.comics_unlimited.event.BrowesNotifyEvent;
import com.stela.comics_unlimited.ui.innerBrowse.InnerBrowseFragment;
import com.stela.comics_unlimited.util.stela_sdk.StelaAnalyticsUtil;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import io.reactivex.functions.Consumer;


/**
 * @author LXY
 * @date 2019/1/23 22:57
 * @desc
 */
public class BrowseFragment extends BaseFragment<BrowsePresenter> implements BrowseContract.View {


    @BindView(R.id.tb_browse)
    TabLayout tbBrowse;
    @BindView(R.id.vp_browse)
    ViewPager vpBrowse;
    @BindView(R.id.tv_empty_msg)
    TextView tvEmptyMsg;
    @BindView(R.id.btn_refresh)
    TextView btnRefresh;
    @BindView(R.id.ll_empty_refresh)
    LinearLayout llEmptyRefresh;
    private String mTitle;
    private String[] mTitles;
    private ArrayList<BaseFragment> baseFragmentList = new ArrayList<>();
    private MyViewPageAdapter mAdapter;
    private List<ImgEntity> mTabEntitys = new ArrayList<>();
    TabLayout.OnTabSelectedListener tablistener;

    public static BrowseFragment getInstance(String title) {
        BrowseFragment fragment = new BrowseFragment();
        Bundle bundle = new Bundle();
        bundle.putString(AppConstants.TITLE, title);
        fragment.setArguments(bundle);
        fragment.mTitle = title;
        return fragment;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_browse;
    }

    @Override
    protected BrowsePresenter createPresenter() {
        return new BrowsePresenter();
    }

    @Override
    protected void initView() {
        llEmptyRefresh.setVisibility(View.GONE);
        tbBrowse.setVisibility(View.VISIBLE);
        vpBrowse.setVisibility(View.VISIBLE);
    }

    private void initTab() {
        tbBrowse.removeAllTabs();
        if (tablistener != null) {
            tbBrowse.removeOnTabSelectedListener(tablistener);
        }
        baseFragmentList.clear();
        for (int i = 0; i < mTitles.length; i++) {
            tbBrowse.addTab(tbBrowse.newTab().setText(mTitles[i]));
            baseFragmentList.add(InnerBrowseFragment.getInstance(mTabEntitys.get(i)));
        }
        mAdapter = new MyViewPageAdapter(getChildFragmentManager(), getContext(), mTitles, baseFragmentList);
        vpBrowse.setAdapter(mAdapter);
        tbBrowse.setupWithViewPager(vpBrowse);
        for (int i = 0; i < mTitles.length; i++) {
            TabLayout.Tab tab = tbBrowse.getTabAt(i);
            //注意！！！这里就是添加我们自定义的布局
            tab.setCustomView(mAdapter.getCustomView(i));
        }
        tablistener = new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                ((TextView) tab.getCustomView().findViewById(R.id.tv_tab)).setSelected(true);
                vpBrowse.setCurrentItem(tab.getPosition());
                StelaAnalyticsUtil.click("browse_" + mTitles[tab.getPosition()]);
                setAWSPage(mTabEntitys.get(tab.getPosition()));

            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                ((TextView) tab.getCustomView().findViewById(R.id.tv_tab)).setSelected(false);
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        };
        tbBrowse.addOnTabSelectedListener(tablistener);
        vpBrowse.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    private void setAWSPage(ImgEntity mImgEntity) {
        //埋点
        Page page = new Page();
        page.setPageId(mImgEntity.id);
        page.setPageName("browse_" + mImgEntity.title);
        StelaAnalyticsUtil.page(page);
    }


    @Override
    protected void initListener() {
        //界面更新
        mPresenter.addDispose(RxBus.getDefault().toObservable(BrowesNotifyEvent.class)
                .subscribe(new Consumer<BrowesNotifyEvent>() {
                    @Override
                    public void accept(BrowesNotifyEvent continueReadEvent) throws Exception {
                        if (continueReadEvent != null) {
                            // 刷新
                            mPresenter.requestData(false);
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
        mPresenter.requestData(true);
    }

    @Override
    protected boolean useEventBus() {
        return false;
    }

    @Override
    public void showError(String msg) {
        if (tbBrowse != null && tbBrowse.getVisibility() == View.VISIBLE && tbBrowse.getTabCount() > 0) {
            ToastUtils.showShort(msg);
        } else {
            initEmptyView(msg);
        }
    }



    private void initEmptyView(String msg) {
        llEmptyRefresh.setVisibility(View.VISIBLE);
        tbBrowse.setVisibility(View.GONE);
        vpBrowse.setVisibility(View.GONE);
        tvEmptyMsg.setText(msg);
    }


    @Override
    public void showData(List<ImgEntity> tabEntitys) {
        if (tabEntitys != null && tabEntitys.size() > 0) {
            boolean isChange = tbBrowse != null && tbBrowse.getVisibility() == View.VISIBLE && tbBrowse.getTabCount() > 0 ? false : true;
            boolean isFirse = true;
            if (mTabEntitys != null && mTabEntitys.size() > 0) {
                isFirse = false;
                for (int i = 0; i < tabEntitys.size(); i++) {
                    if (!mTabEntitys.get(i).id.equals(tabEntitys.get(i).id)) {
                        isChange = true;
                        break;
                    }
                }
            }
            if (isFirse || isChange) {
                llEmptyRefresh.setVisibility(View.GONE);
                tbBrowse.setVisibility(View.VISIBLE);
                vpBrowse.setVisibility(View.VISIBLE);
                mTabEntitys.clear();
                mTabEntitys.addAll(tabEntitys);
                mTitles = new String[tabEntitys.size()];
                for (int i = 0; i < tabEntitys.size(); i++) {
                    mTitles[i] = tabEntitys.get(i).title;
                }
                initTab();
            }
        } else {
            llEmptyRefresh.setVisibility(View.VISIBLE);
            tbBrowse.setVisibility(View.GONE);
            vpBrowse.setVisibility(View.GONE);
            tvEmptyMsg.setText("pages is gone,please try again later!");
        }

    }


    @OnClick(R.id.btn_refresh)
    public void onViewClicked() {
        mPresenter.requestData(true);
    }
}
