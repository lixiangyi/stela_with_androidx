package com.stela.comics_unlimited.ui.chapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.LayoutRes;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.example.zhouwei.library.CustomPopWindow;
import com.github.piasy.rxscreenshotdetector.RxScreenshotDetector;
import com.lxy.baselibs.app.AppConstants;
import com.lxy.baselibs.glide.GlideUtils;
import com.lxy.baselibs.rx.RxBus;
import com.lxy.baselibs.utils.CommonUtils;
import com.lxy.baselibs.utils.DeleteFileUtil;
import com.lxy.baselibs.utils.DisplayUtils;
import com.lxy.baselibs.utils.ToastUtils;
import com.stela.analytics.model.Page;
import com.stela.comics_unlimited.Base.BaseMvpActivity;
import com.stela.comics_unlimited.R;
import com.stela.comics_unlimited.app.MyApp;
import com.stela.comics_unlimited.data.entity.ChapterEntity;
import com.stela.comics_unlimited.data.entity.ScreenShotrEntity;
import com.stela.comics_unlimited.data.entity.SeriesAsset;
import com.stela.comics_unlimited.event.ChapterNotifyEvent;
import com.stela.comics_unlimited.event.CommentsNotifyEvent;
import com.stela.comics_unlimited.event.ContinueReadEvent;
import com.stela.comics_unlimited.event.ReadRecordEvent;
import com.stela.comics_unlimited.event.RecentReadNotifyEvent;
import com.stela.comics_unlimited.ui.comments.CommentsListActivity;
import com.stela.comics_unlimited.ui.subscribe.SubscribeActivity;
import com.stela.comics_unlimited.util.SharedPreferencesTool;
import com.stela.comics_unlimited.util.Tools;
import com.stela.comics_unlimited.util.stela_sdk.StelaAnalyticsUtil;
import com.stela.comics_unlimited.widget.SingleTapRecyclerView;
import com.stela.comics_unlimited.widget.VerticalSeekBar;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.OnClick;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

public class ChapterActivity extends BaseMvpActivity<ChapterPresenter> implements ChapterContract.View {

    @BindView(R.id.rv_read)
    SingleTapRecyclerView rvRead;
    @BindView(R.id.tv_like_top)
    TextView tvLikeTop;
    @BindView(R.id.tv_comment_top)
    TextView tvCommentTop;
    @BindView(R.id.tv_next_chapter)
    TextView tvNextChapter;
    @BindView(R.id.ll_read_bottom)
    LinearLayout llReadBottom;
    @BindView(R.id.vs_progres)
    VerticalSeekBar vsProgres;
    @BindView(R.id.iv_center_icon)
    TextView ivCenterIcon;
    @BindView(R.id.iv_center_title)
    TextView ivCenterTitle;
    @BindView(R.id.iv_left_icon)
    AppCompatImageView ivLeftIcon;
    @BindView(R.id.iv_right_icon)
    AppCompatImageView ivRightIcon;
    @BindView(R.id.toolbar)
    RelativeLayout toolbar;
    private static final int DISAPPEAR_TIME = 3000;
    @BindView(R.id.iv_alpha)
    AppCompatImageView ivAlpha;
    private CustomPopWindow mListPopWindow;
    private ImageAdapter myAdapter;
    private boolean isLike;
    private boolean isShow;
    private ArrayList<ChapterEntity> chapterList;
    private int mPosition;
    private int firstVisible;
    private ChapterEntity mChapterEntity;
    private int lastVisible;
    private int mlastPosition = -1;
    private MyChapterListAdapter myChapterListAdapter;

    public static void start(Context context, ArrayList<ChapterEntity> chapterEntities, int position) {
        Intent starter = new Intent(context, ChapterActivity.class);
        starter.putExtra(AppConstants.SERIES_CHAPTER_LIST, chapterEntities);
        starter.putExtra(AppConstants.SERIES_CHAPTER_LIST_POSITION, position);
        context.startActivity(starter);
    }


    @Override
    protected int getLayoutId() {
        return R.layout.activity_read;
    }

    @Override
    protected void getIntent(Intent intent) {
        // 禁止截屏
//        getWindow().addFlags(WindowManager.LayoutParams.FLAG_SECURE);
        chapterList = (ArrayList<ChapterEntity>) intent.getSerializableExtra(AppConstants.SERIES_CHAPTER_LIST);
        mPosition = intent.getIntExtra(AppConstants.SERIES_CHAPTER_LIST_POSITION, -1);
    }

    @Override
    protected void initView() {
        initHeader();
        initRecyclerview();
        showUP();
        ivAlpha.setAlpha(0.5f);
    }

    private void initHeader() {
        ivCenterIcon.setVisibility(View.GONE);
        ivCenterTitle.setVisibility(View.VISIBLE);
        ivRightIcon.setImageResource(R.mipmap.ic_read_chapter);
        ivRightIcon.setVisibility(View.VISIBLE);
    }

    private void initRecyclerview() {
        if (chapterList.get(mPosition).chapterImgList == null) {
            chapterList.get(mPosition).chapterImgList = new ArrayList<>();
        }
        myAdapter = new ImageAdapter(R.layout.item_read, chapterList.get(mPosition).chapterImgList);
        LinearLayoutManager layout = new LinearLayoutManager(this);
        rvRead.setLayoutManager(layout);
        //开启动画（默认为渐显效果）
        myAdapter.openLoadAnimation();
        //设置重复执行动画
//        myAdapter.isFirstOnly(false);
        rvRead.setAdapter(myAdapter);
        rvRead.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                LinearLayoutManager layout = (LinearLayoutManager) rvRead.getLayoutManager();
                firstVisible = layout.findFirstVisibleItemPosition();
                lastVisible = layout.findLastVisibleItemPosition();

                //进度条
                vsProgres.setProgress(lastVisible);
                if (!recyclerView.canScrollVertically(-1)) {
                    vsProgres.setProgress(0);
                }
                //显示遮层
                if (dy < 0) {
                    //手指向下滑动，列表向下滚动
                    if (!isShow) {
                        showUP();
                    }
                } else if (dy > 0) {
                    //手指向上滑动，列表向上滚动
                    if (isShow) {
                        dismissDown();
                    }
                }
                // 下一章显示
                showNextChapter();

            }
        });
        rvRead.setOnGestureListener(new SingleTapRecyclerView.OnGestureListener() {
            @Override
            public boolean onSingleTapConfirmed(MotionEvent e) {
                if (isShow) {
                    dismissDown();
                } else {
                    showUP();
                }
                return false;
            }
        });

        vsProgres.setOnSeekBarChangeListener(new VerticalSeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(VerticalSeekBar VerticalBar, int progress, boolean fromUser) {
            }

            @Override
            public void onStartTrackingTouch(VerticalSeekBar VerticalBar) {
            }

            @Override
            public void onStopTrackingTouch(VerticalSeekBar VerticalBar) {
                Log.i(TAG, "onStopTrackingTouch: " + VerticalBar.getProgress());
//                rvRead.smoothScrollToPosition(VerticalBar.getProgress());
                rvRead.scrollToPosition(VerticalBar.getProgress());
            }
        });
    }

    private void showNextChapter() {
        // 下一章显示
        if (mChapterEntity != null && mChapterEntity.chapterImgList != null && lastVisible == mChapterEntity.chapterImgList.size() - 1) {
            if (mPosition < chapterList.size() - 1) {
                tvNextChapter.setVisibility(View.VISIBLE);
            } else {
                tvNextChapter.setVisibility(View.GONE);
            }
            showUP();
        } else {
            tvNextChapter.setVisibility(View.GONE);
        }
    }


    private void showUP() {
        isShow = true;
        loadAnimation(toolbar, R.anim.top_in_anim, View.VISIBLE);
        loadAnimation(llReadBottom, R.anim.bottom_in_anim, View.VISIBLE);
        if (vsProgres.getVisibility() != View.VISIBLE) {
            loadAnimation(vsProgres, R.anim.right_in_anim, View.VISIBLE);
        }
    }

    private void dismissDown() {
        isShow = false;
        loadAnimation(toolbar, R.anim.top_out_anim, View.GONE);
        loadAnimation(llReadBottom, R.anim.bottom_out_anim, View.GONE);
        mPresenter.addDispose(Observable.timer(DISAPPEAR_TIME, TimeUnit.MILLISECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(aLong -> {
                    if (!isShow) {
                        loadAnimation(vsProgres, R.anim.right_out_anim, View.GONE);
                    }
                }));

    }

    private void loadAnimation(View view, int animationid, int visible) {
        //使用AnimationUtils类的静态方法loadAnimation()来加载XML中的动画XML文件
        Animation animation = AnimationUtils.loadAnimation(this, animationid);
        view.startAnimation(animation);
        view.setVisibility(visible);
    }


    @Override
    protected void initListener() {
        //界面更新
        mPresenter.addDispose(RxBus.getDefault().toObservable(ChapterNotifyEvent.class)
                .subscribe(new Consumer<ChapterNotifyEvent>() {
                    @Override
                    public void accept(ChapterNotifyEvent chapterNotifyEvent) throws Exception {
                        if (chapterNotifyEvent != null) {
                            // 刷新chapter
                            mPosition = chapterNotifyEvent.mChapterPosition > 0 ? chapterNotifyEvent.mChapterPosition : 0;
                            if (chapterList != null && chapterList.size() > 0) {
                                mPresenter.getChapterList(chapterList.get(mPosition).id, chapterList.get(mPosition).seriesId);
                            }
                            // 刷新menu 列表状态
                            for (int i = 0; i < chapterList.size(); i++) {
                                chapterList.get(i).chapterState = ChapterEntity.CHAPTERSTATE_PAYED;
                            }
                            myChapterListAdapter.notifyDataSetChanged();
                        }
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {

                    }
                }));
        //评论刷新处理
        mPresenter.addDispose(RxBus.getDefault().toObservable(CommentsNotifyEvent.class)
                .subscribe(new Consumer<CommentsNotifyEvent>() {
                    @Override
                    public void accept(CommentsNotifyEvent commentsNotifyEvent) throws Exception {
                        if (commentsNotifyEvent != null) {
                            //
                            setComment(true);
                        }
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
//                            ToastUtils.showShort(throwable.getMessage());
                    }
                }));
    }

    @Override
    protected void initData() {
        if (chapterList != null && chapterList.size() > 0) {
            mPresenter.getChapterList(chapterList.get(mPosition).id, chapterList.get(mPosition).seriesId);
        }
    }

    @Override
    protected ChapterPresenter createPresenter() {
        return new ChapterPresenter();
    }


    @Override
    public void likeSuccess() {
        isLike = !isLike;
        mChapterEntity.flagLikeChapter = isLike ? 1 : 0;
        setLike(isLike);
    }

    @Override
    public void storeLastSuccess() {
        RxBus.getDefault().post(new ContinueReadEvent(mPosition, lastVisible));
        RxBus.getDefault().post(new RecentReadNotifyEvent());
        finish();
    }

    @Override
    public void showlastChapterError(String s) {
        ToastUtils.showShort(s);
        RxBus.getDefault().post(new ContinueReadEvent(mPosition, lastVisible));
        finish();
    }

    @Override
    public void showChapter(ChapterEntity chapterEntity) {
        if (chapterEntity == null) {
            return;
        }
        saveRead(chapterEntity);
        if (chapterEntity.chapterState == ChapterEntity.CHAPTERSTATE_LOCK) {
            SubscribeActivity.start(this, chapterEntity.seriesId, mPosition);
            if (mlastPosition != -1) {
                mPosition = mlastPosition;
            }
//            finish();
            return;
        }
        if (chapterEntity.chapterState == ChapterEntity.CHAPTERSTATE_DATA) {
            ToastUtils.showShort("Coming soon！");
            return;
        }
        mChapterEntity = chapterEntity;
        myAdapter.setNewData(mChapterEntity.chapterImgList);
        isLike = mChapterEntity.flagLikeChapter == 0 ? false : true;
        setLike(isLike);
        if (mChapterEntity.chapterImgList != null && mChapterEntity.chapterImgList.size() > 0) {
            vsProgres.setMax(mChapterEntity.chapterImgList.size() - 1);
        } else {
            vsProgres.setMax(0);
        }
        rvRead.scrollToPosition(getScrollPosition(mChapterEntity));
        ivCenterTitle.setText(mChapterEntity.title);
        setComment(mChapterEntity.flagComments == 1);
        if (mChapterEntity.screenshotsCount>2) {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_SECURE);
        }
    }

    private void saveRead(ChapterEntity chapterEntity) {
        HashMap<String, String> readRecord = (HashMap) SharedPreferencesTool.getObj(MyApp.getContext(), SharedPreferencesTool.KEY_CHAPTER_READ_RECORD);
        if (readRecord == null) {
            readRecord = new HashMap<>();
        }
        readRecord.put(chapterEntity.id, chapterEntity.seriesId);
        SharedPreferencesTool.saveObj(MyApp.getContext(), SharedPreferencesTool.KEY_CHAPTER_READ_RECORD, readRecord);
        RxBus.getDefault().post(new ReadRecordEvent(mPosition));
    }

    @Override
    public void screenshots(ScreenShotrEntity screenShotrEntity, String path) {
        screenshots(screenShotrEntity.screenshotsCount, path);
    }

    private void screenshots(int shot, String path) {
        switch (shot) {
            case 1:
                Tools.screenShotAlertDialog(this, getString(R.string.screen_first_title), getString(R.string.screen_first_content));
                break;
            case 2:
                getWindow().addFlags(WindowManager.LayoutParams.FLAG_SECURE);
                Tools.screenShotAlertDialog(this, getString(R.string.screen_second_title), getString(R.string.screen_second_content));
                break;
            default:
            case 3:
                getWindow().addFlags(WindowManager.LayoutParams.FLAG_SECURE);
                Tools.screenShotAlertDialog(this, getString(R.string.screen_third_title), getString(R.string.screen_third_content));
                if (!TextUtils.isEmpty(path)) {
                    DeleteFileUtil.deleteFile(path);
                }
                break;

        }
    }

    private int getScrollPosition(ChapterEntity chapterEntity) {
        int scrollposition = 0;
        if (chapterEntity != null && chapterEntity.chapterImgList != null && chapterEntity.chapterImgList.size() > 0) {
            for (int i = 0; i < chapterEntity.chapterImgList.size(); i++) {
                if (chapterEntity.chapterImgList.get(i).id.equals(chapterEntity.chapterRowsSort)) {
                    scrollposition = i;
                    break;
                }
            }
        }
        return scrollposition;
    }

    private void setLike(boolean isLike) {
        final Drawable drawable;
        if (!isLike) {
            drawable = ContextCompat.getDrawable(this, R.mipmap.ic_unlike);
            tvLikeTop.setCompoundDrawablesWithIntrinsicBounds(drawable, null, null, null);
        } else {
            drawable = ContextCompat.getDrawable(this, R.mipmap.ic_like);
            tvLikeTop.setCompoundDrawablesWithIntrinsicBounds(drawable, null, null, null);
        }
        drawable.setBounds(0, 0, drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight());
    }

    private void setComment(boolean isCommented) {
        final Drawable drawable;
        if (!isCommented) {
            drawable = ContextCompat.getDrawable(this, R.mipmap.ic_comment);
            tvCommentTop.setCompoundDrawablesWithIntrinsicBounds(drawable, null, null, null);
        } else {
            drawable = ContextCompat.getDrawable(this, R.mipmap.ic_commented);
            tvCommentTop.setCompoundDrawablesWithIntrinsicBounds(drawable, null, null, null);
        }
        drawable.setBounds(0, 0, drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight());
    }

    @Override
    public void showError(String msg) {
        ToastUtils.showShort(msg);
    }


    @OnClick({R.id.tv_like_top, R.id.tv_comment_top, R.id.tv_next_chapter, R.id.iv_left_icon, R.id.iv_right_icon})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_like_top:
                if (mChapterEntity != null) {
                    int flagLike = isLike == true ? 0 : 1;
                    mPresenter.setchapterLikes(mChapterEntity.id, mChapterEntity.seriesId, flagLike);
                }
                break;
            case R.id.tv_comment_top:
                //埋点
                Page page = new Page();
                page.setPageId(mChapterEntity.id);
                page.setPageName("comments");
                StelaAnalyticsUtil.page(page);
                CommentsListActivity.start(ChapterActivity.this, mChapterEntity.seriesId, mChapterEntity.id);
                break;
            case R.id.tv_next_chapter:
                mlastPosition = mPosition;
                mPosition++;
                goChapter(true);
                StelaAnalyticsUtil.click("next_chapter");
                break;
            case R.id.iv_left_icon:
                saveLastReadChapter();
                break;
            case R.id.iv_right_icon:
                showChapterListView();
                StelaAnalyticsUtil.click("chapter_menu");
                break;
            default:
                break;
        }
    }


    public class ImageAdapter extends BaseQuickAdapter<SeriesAsset, BaseViewHolder> {

        ImageAdapter(@LayoutRes int layoutResId, @Nullable List<SeriesAsset> data) {
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

    private void showChapterListView() {

        View contentView = LayoutInflater.from(this).inflate(R.layout.pop_read_list, null);
        //处理popWindow 显示内容
        handlePopListView(contentView);
        //创建并显示popWindow
        mListPopWindow = new CustomPopWindow.PopupWindowBuilder(this)
                .setView(contentView)
//                .size(DisplayUtils.getScreenWidth(this)/2, )//显示大小
                .create()
                .showAsDropDown(ivRightIcon, 0, CommonUtils.dp2px(8));
    }


    private void handlePopListView(View contentView) {
        RecyclerView recyclerView = (RecyclerView) contentView.findViewById(R.id.rv_pop_read);
        LinearLayoutManager manager = new LinearLayoutManager(this);
        manager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(manager);
        myChapterListAdapter = new MyChapterListAdapter(R.layout.item_chapter_list, chapterList);
        recyclerView.setAdapter(myChapterListAdapter);
        myChapterListAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                mlastPosition = mPosition;
                mPosition = position;
                goChapter(false);
                if (chapterList != null && chapterList.size() > 0 && chapterList.get(mPosition) != null) {
                    StelaAnalyticsUtil.click("chapter_list_" + chapterList.get(mPosition).title + "");
                }
            }
        });

    }

    private void goChapter(boolean ifHide) {
        if (chapterList != null && chapterList.size() > 0) {
            mPresenter.getChapterList(chapterList.get(mPosition).id, chapterList.get(mPosition).seriesId);
        }
        if (mListPopWindow != null) {
            mListPopWindow.dissmiss();
        }
        if (ifHide) {
            dismissDown();
        }
    }

    public class MyChapterListAdapter extends BaseQuickAdapter<ChapterEntity, BaseViewHolder> {

        MyChapterListAdapter(@LayoutRes int layoutResId, @Nullable List<ChapterEntity> data) {
            super(layoutResId, data);
        }

        @Override
        protected void convert(BaseViewHolder helper, ChapterEntity item) {
            TextView mTvTitle = helper.getView(R.id.tv_chapter_title);
            mTvTitle.setText(item.title);
            if (item.chapterState == ChapterEntity.CHAPTERSTATE_FREE || item.chapterState == ChapterEntity.CHAPTERSTATE_PAYED) {
                mTvTitle.setTextColor(ContextCompat.getColor(mContext, R.color.color_73));
            } else {
                mTvTitle.setTextColor(ContextCompat.getColor(mContext, R.color.color_b2));
            }
        }
    }

    @Override
    public void onBackPressed() {
        saveLastReadChapter();
    }

    private void saveLastReadChapter() {
        if (mChapterEntity != null && mChapterEntity.chapterImgList != null && mChapterEntity.chapterImgList.size() > 0) {
            mPresenter.storelastChapter(mChapterEntity.id, mChapterEntity.seriesId,
                    mChapterEntity.chapterImgList.get(lastVisible).id);
        } else {
            RxBus.getDefault().post(new ContinueReadEvent(mPosition, lastVisible));
            finish();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        RxScreenshotDetector.start(this)
                .compose(bindToLifecycle())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(path -> mPresenter.screenshotsCount(path),
                        throwable -> throwable.printStackTrace());

    }
}
