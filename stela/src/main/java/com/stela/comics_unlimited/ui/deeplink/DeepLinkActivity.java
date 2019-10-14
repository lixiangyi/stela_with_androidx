package com.stela.comics_unlimited.ui.deeplink;

import android.animation.Animator;
import android.animation.AnimatorInflater;
import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.FrameLayout;
import android.widget.ImageView;

import androidx.annotation.LayoutRes;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.core.app.ActivityCompat;
import androidx.core.app.ActivityOptionsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.lxy.baselibs.app.AppConstants;
import com.lxy.baselibs.glide.GlideUtils;
import com.lxy.baselibs.utils.DisplayUtils;
import com.stela.analytics.model.Page;
import com.stela.comics_unlimited.Base.BaseMvpActivity;
import com.stela.comics_unlimited.R;
import com.stela.comics_unlimited.app.DataStore;
import com.stela.comics_unlimited.data.entity.DeeplinkEntity;
import com.stela.comics_unlimited.data.entity.SeriesAsset;
import com.stela.comics_unlimited.ui.MainActivity;
import com.stela.comics_unlimited.ui.login.LoginActivity;
import com.stela.comics_unlimited.util.stela_sdk.StelaAnalyticsUtil;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

public class DeepLinkActivity extends BaseMvpActivity<DeepLinkPresenter> implements DeepLinkContract.View {


    @BindView(R.id.rv_deeplink)
    RecyclerView rvRead;
    @BindView(R.id.iv_close)
    ImageView ivClose;
    @BindView(R.id.iv_scroll)
    ImageView ivScroll;
    @BindView(R.id.iv_sign_up)
    ImageView ivSignUp;
    private MyAdapter myAdapter;
    private ArrayList<SeriesAsset> list = new ArrayList<>();
    private int firstVisible;
    private int lastVisible;
    private String deeplinkId;
    private Animator animator;

    public static void start(Context context, String deeplinkId) {
        Intent starter = new Intent(context, DeepLinkActivity.class);
        starter.putExtra(AppConstants.DEEPLINK_ID, deeplinkId);
        context.startActivity(starter);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_deeplink;
    }

    @Override
    protected void getIntent(Intent intent) {
        // 禁止截屏
        if (DataStore.isLogin()) {
            MainActivity.start(this);
            finish();
        }
        deeplinkId = intent.getStringExtra(AppConstants.DEEPLINK_ID);
    }

    @Override
    protected void initView() {
        initRecyclerview();
        initAnimator();

    }

    private void initAnimator() {
        animator = AnimatorInflater.loadAnimator(this, R.animator.scroll_animator);
        animator.setTarget(ivScroll);
        animator.start();
        animator.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                animator.start();
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
    }


    private void initRecyclerview() {
        myAdapter = new MyAdapter(R.layout.item_read, list);
        LinearLayoutManager layout = new LinearLayoutManager(this);
        rvRead.setLayoutManager(layout);
        rvRead.setAdapter(myAdapter);
        rvRead.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                LinearLayoutManager layout = (LinearLayoutManager) rvRead.getLayoutManager();
                firstVisible = layout.findFirstVisibleItemPosition();
                lastVisible = layout.findLastVisibleItemPosition();

                //显示遮层
                if (dy < 0) {
                    //手指向下滑动，列表向上滚动
                } else if (dy > 0) {
                    //手指向上滑动，列表向下滚动
                    if (animator != null && animator.isRunning()) {
                        animator.cancel();
                    }
                    ivScroll.setVisibility(View.GONE);

                }
                if (list.size() > 0 && lastVisible == list.size() - 1) {
                    if (ivSignUp.getVisibility() != View.VISIBLE) {
                        loadAnimation(ivSignUp, R.anim.bottom_in_anim, View.VISIBLE);
                    }
                    mPresenter.closeOut();
                }

            }

            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);

            }
        });

    }

    private void loadAnimation(View view, int animationid, int visible) {
        //使用AnimationUtils类的静态方法loadAnimation()来加载XML中的动画XML文件
        Animation animation = AnimationUtils.loadAnimation(this, animationid);
        view.startAnimation(animation);
        view.setVisibility(visible);
    }

    @Override
    protected void beforeSetView() {
        getWindow().requestFeature(Window.FEATURE_CONTENT_TRANSITIONS);
    }

    @Override
    protected void initListener() {

    }

    @Override
    protected void initData() {
        mPresenter.getDeeplinkList(deeplinkId);
    }

    @Override
    protected DeepLinkPresenter createPresenter() {
        return new DeepLinkPresenter();
    }


    @Override
    public void showChapter(DeeplinkEntity deeplinkEntity) {
        list = deeplinkEntity.deepLinkAssetsList;
        myAdapter.setNewData(deeplinkEntity.deepLinkAssetsList);
        //埋点
        Page page = new Page();
        page.setPageId(deeplinkId);
        page.setPageName("deeplink_page_" + deeplinkEntity.name);
        StelaAnalyticsUtil.page(page);
    }

    @Override
    public void closeOut() {
        ivClose.setVisibility(View.VISIBLE);
    }


    @Override
    public void showError(String msg) {
//        ToastUtils.showShort(msg);
        gotoLogin();
        //埋点
        Page page = new Page();
        page.setPageId(deeplinkId);
        page.setPageName("deeplink_page_fail");
        StelaAnalyticsUtil.page(page);
    }


    @OnClick({R.id.iv_close, R.id.iv_sign_up})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_close:
                gotoLogin();
                break;
            case R.id.iv_sign_up:
                gotoLogin();
                StelaAnalyticsUtil.click("deeplink_sign_up_to_read_more");
                break;
        }
    }

    private void gotoLogin() {

//                R.anim.translate_in, R.anim.translate_none);
        //中心放大动画
        ActivityOptionsCompat compat = ActivityOptionsCompat.makeScaleUpAnimation(getWindow().getDecorView(),
                getWindow().getDecorView().getWidth() / 2, getWindow().getDecorView().getHeight() / 2,
                0, 0);
        ActivityCompat.startActivity(this, new Intent(this, LoginActivity.class), compat.toBundle());
        //底部
//        ActivityOptionsCompat options11 = ActivityOptionsCompat.makeSceneTransitionAnimation(this);
//        ActivityCompat.startActivity(this, new Intent(this, LoginActivityOld.class), options11.toBundle());
        finish();
    }


    public class MyAdapter extends BaseQuickAdapter<SeriesAsset, BaseViewHolder> {

        MyAdapter(@LayoutRes int layoutResId, @Nullable List<SeriesAsset> data) {
            super(layoutResId, data);
        }

        @Override
        protected void convert(BaseViewHolder helper, SeriesAsset item) {
            AppCompatImageView imageView = helper.getView(R.id.iv_read);
            int width = DisplayUtils.getScreenWidth(mContext);
            int height = width * item.height / item.width;
            FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, height);
            imageView.setLayoutParams(params);
            imageView.setScaleType(ImageView.ScaleType.FIT_XY);
            GlideUtils.loadImageHttps(mContext, imageView, item.url, R.drawable.stela_loading_96);
        }

    }
}
