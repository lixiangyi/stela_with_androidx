package com.stela.comics_unlimited.ui.innerSeries;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import androidx.core.content.ContextCompat;

import com.lxy.baselibs.app.AppConstants;
import com.lxy.baselibs.glide.GlideUtils;
import com.lxy.baselibs.rx.RxBus;
import com.lxy.baselibs.utils.ToastUtils;
import com.stela.analytics.model.Page;
import com.stela.analytics.model.Series;
import com.stela.comics_unlimited.Base.BaseFragment;
import com.stela.comics_unlimited.R;
import com.stela.comics_unlimited.app.DataStore;
import com.stela.comics_unlimited.data.entity.CreditorEntity;
import com.stela.comics_unlimited.data.entity.SeriesEntity;
import com.stela.comics_unlimited.event.CollectionNotifyEvent;
import com.stela.comics_unlimited.event.CommentsNotifyEvent;
import com.stela.comics_unlimited.ui.comments.CommentsListActivity;
import com.stela.comics_unlimited.ui.series.SeriesActivity;
import com.stela.comics_unlimited.util.stela_sdk.StelaAnalyticsUtil;
import com.stela.comics_unlimited.widget.ScaleView.ScaleImage;

import butterknife.BindView;
import butterknife.OnClick;
import io.reactivex.functions.Consumer;


/**
 * @author LXY
 * @date 2019/1/23 22:57
 * @desc
 */
public class SeriesInfoFragment extends BaseFragment<SeriesInfoPresenter> implements SeriesInfoContract.View {


    @BindView(R.id.tv_series_des)
    TextView tvSeriesDes;
    @BindView(R.id.tv_collection)
    TextView tvCollection;
    @BindView(R.id.tv_like)
    TextView tvLike;
    @BindView(R.id.tv_comment)
    TextView tvComment;
    @BindView(R.id.tv_creator_credits)
    TextView tvCreatorCredits;
    @BindView(R.id.iv_like1)
    ScaleImage ivLike1;
    @BindView(R.id.iv_like2)
    ScaleImage ivLike2;
    @BindView(R.id.iv_like3)
    ScaleImage ivLike3;
    private boolean isLike;
    private boolean isCollection;
    private boolean isCommented;
    private SeriesEntity mSeriesEntity;
    ScaleImage[] ivLike;

    public static SeriesInfoFragment getInstance(SeriesEntity seriesEntity) {
        SeriesInfoFragment fragment = new SeriesInfoFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable(AppConstants.SERIES_ENTITY, seriesEntity);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_series_info;
    }

    @Override
    protected SeriesInfoPresenter createPresenter() {
        return new SeriesInfoPresenter();
    }

    @Override
    protected void initView() {
        mSeriesEntity = (SeriesEntity) getArguments().getSerializable(AppConstants.SERIES_ENTITY);
        if (mSeriesEntity == null) {
            ToastUtils.showShort("something wrong,try again");
            return;
        }
        tvSeriesDes.setText(mSeriesEntity.description);
        isCollection = mSeriesEntity.flagCollect == 0 ? false : true;
        isLike = mSeriesEntity.flagLike == 0 ? false : true;
        isCommented = mSeriesEntity.flagComments == 0 ? false : true;
        setCollection(isCollection);
        setLike(isLike);
        setComment(isCommented);
        initSeeMore();
        initAuthor();
        if (DataStore.getUserInfo().childrenState==1) {
            tvComment.setVisibility(View.GONE);
        }else {
            tvComment.setVisibility(View.VISIBLE);
        }
    }

    private void initAuthor() {
        String autherString = "";
        if (mSeriesEntity.creditorList != null) {
            for (int i = 0; i < mSeriesEntity.creditorList.size(); i++) {
                if (mSeriesEntity.creditorList.get(i) != null && mSeriesEntity.creditorList.get(i) instanceof CreditorEntity) {
                    autherString += mSeriesEntity.creditorList.get(i).name + ", ";
                }
            }
        }
        if (autherString.endsWith(",")) {
            autherString = autherString.substring(0, autherString.length() - 1);
        }
        tvCreatorCredits.setText(autherString);
    }

    private void initSeeMore() {
        ivLike = new ScaleImage[]{ivLike1, ivLike2, ivLike3};
        if (mSeriesEntity.youMayAlsLlike != null && mSeriesEntity.youMayAlsLlike.size() > 0) {
            if (mSeriesEntity.youMayAlsLlike.size() < 4) {
                for (int i = 0; i < mSeriesEntity.youMayAlsLlike.size(); i++) {
                    if (mSeriesEntity.youMayAlsLlike.get(i).assets != null && mSeriesEntity.youMayAlsLlike.get(i).assets.size() > 0) {
                        GlideUtils.loadImageHttps(getActivity(), ivLike[i], mSeriesEntity.youMayAlsLlike.get(i).assets.get(0).url, R.color.stela_blue);
                    }
                }
            } else {
                for (int i = 0; i < 3; i++) {
                    if (mSeriesEntity.youMayAlsLlike.get(i).assets != null && mSeriesEntity.youMayAlsLlike.get(i).assets.size() > 0) {
                        GlideUtils.loadImageHttps(getActivity(), ivLike[i], mSeriesEntity.youMayAlsLlike.get(i).assets.get(0).url, R.color.stela_blue);
                    }
                }
            }
        }
    }

    @Override
    protected void initListener() {
        // 评论刷新处理
        mPresenter.addDispose(RxBus.getDefault().toObservable(CommentsNotifyEvent.class)
                .subscribe(new Consumer<CommentsNotifyEvent>() {
                    @Override
                    public void accept(CommentsNotifyEvent commentsNotifyEvent) throws Exception {
                        if (commentsNotifyEvent != null) {
                            //
                            if (TextUtils.isEmpty(commentsNotifyEvent.mChapterId)) {
                                setComment(true);
                            }
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
    }

    @Override
    protected boolean useEventBus() {
        return false;
    }

    @Override
    public void showError(String msg) {
        ToastUtils.showShort(msg);
    }


    @Override
    public void likeSuccess() {
        isLike = !isLike;
        setLike(isLike);
    }


    @Override
    public void collectSuccess() {
        isCollection = !isCollection;
        setCollection(isCollection);
        RxBus.getDefault().post(new CollectionNotifyEvent());
    }

    @OnClick({R.id.tv_collection, R.id.tv_like, R.id.tv_comment, R.id.iv_like1, R.id.iv_like2, R.id.iv_like3})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_collection:
                int flagCollection = isCollection == true ? 0 : 1;
                mPresenter.setCollection(mSeriesEntity.id, flagCollection, mSeriesEntity.seriesType);
                StelaAnalyticsUtil.click("add");
                break;
            case R.id.tv_like:
                int flagLike = isLike == true ? 0 : 1;
                mPresenter.setLikes(mSeriesEntity.id, flagLike);
                StelaAnalyticsUtil.click("like");
                break;
            case R.id.tv_comment:
                //埋点
                Page page = new Page();
                page.setPageId(mSeriesEntity.id);
                page.setPageName("comments");
                StelaAnalyticsUtil.page(page);
                CommentsListActivity.start(getActivity(), mSeriesEntity.id, "");
                break;
            case R.id.iv_like1:
                if (mSeriesEntity.youMayAlsLlike != null && mSeriesEntity.youMayAlsLlike.size() > 0 && !TextUtils.isEmpty(mSeriesEntity.youMayAlsLlike.get(0).id)) {
                    stelaAnalyticsSeries(mSeriesEntity.youMayAlsLlike.get(0).id, mSeriesEntity.youMayAlsLlike.get(0).title);
                    SeriesActivity.start(getActivity(), mSeriesEntity.youMayAlsLlike.get(0).id);
                }
                StelaAnalyticsUtil.click("recommend");
                break;
            case R.id.iv_like2:
                if (mSeriesEntity.youMayAlsLlike != null && mSeriesEntity.youMayAlsLlike.size() > 1 && !TextUtils.isEmpty(mSeriesEntity.youMayAlsLlike.get(1).id)) {
                    stelaAnalyticsSeries(mSeriesEntity.youMayAlsLlike.get(1).id, mSeriesEntity.youMayAlsLlike.get(1).title);
                    SeriesActivity.start(getActivity(), mSeriesEntity.youMayAlsLlike.get(1).id);
                }
                StelaAnalyticsUtil.click("recommend");
                break;
            case R.id.iv_like3:
                if (mSeriesEntity.youMayAlsLlike != null && mSeriesEntity.youMayAlsLlike.size() > 2 && !TextUtils.isEmpty(mSeriesEntity.youMayAlsLlike.get(2).id)) {
                    stelaAnalyticsSeries(mSeriesEntity.youMayAlsLlike.get(2).id, mSeriesEntity.youMayAlsLlike.get(2).title);
                    SeriesActivity.start(getActivity(), mSeriesEntity.youMayAlsLlike.get(2).id);
                }
                StelaAnalyticsUtil.click("recommend");
                break;
        }
    }

    private void stelaAnalyticsSeries(String seriesId, String title) {
        //埋点
        Series series = new Series();
        series.setPageId(TextUtils.isEmpty(mSeriesEntity.id) ? "" : mSeriesEntity.id);
        series.setPageName(TextUtils.isEmpty(mSeriesEntity.title) ? "" : mSeriesEntity.title);
        series.setGroupId("");
        series.setGroupName("youMayAlsLlike");
        series.setSeriesId(seriesId);
        series.setSeriesName(title);
        StelaAnalyticsUtil.click("series_view");
        StelaAnalyticsUtil.series(series);
    }


    private void setCollection(boolean isCollection) {
        final Drawable drawableCollection;
        if (!isCollection) {
            drawableCollection = ContextCompat.getDrawable(getContext(), R.mipmap.ic_uncollection);
            tvCollection.setCompoundDrawablesWithIntrinsicBounds(drawableCollection, null, null, null);
        } else {
            drawableCollection = ContextCompat.getDrawable(getContext(), R.mipmap.ic_collection);
            tvCollection.setCompoundDrawablesWithIntrinsicBounds(drawableCollection, null, null, null);
        }
        drawableCollection.setBounds(0, 0, drawableCollection.getIntrinsicWidth(), drawableCollection.getIntrinsicHeight());
    }

    private void setLike(boolean isLike) {
        final Drawable drawable;
        if (!isLike) {
            drawable = ContextCompat.getDrawable(getContext(), R.mipmap.ic_unlike);
            tvLike.setCompoundDrawablesWithIntrinsicBounds(drawable, null, null, null);
        } else {
            drawable = ContextCompat.getDrawable(getContext(), R.mipmap.ic_like);
            tvLike.setCompoundDrawablesWithIntrinsicBounds(drawable, null, null, null);
        }
        drawable.setBounds(0, 0, drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight());
    }

    private void setComment(boolean isCommented) {
        final Drawable drawable;
        if (!isCommented) {
            drawable = ContextCompat.getDrawable(getContext(), R.mipmap.ic_comment);
            tvComment.setCompoundDrawablesWithIntrinsicBounds(drawable, null, null, null);
        } else {
            drawable = ContextCompat.getDrawable(getContext(), R.mipmap.ic_commented);
            tvComment.setCompoundDrawablesWithIntrinsicBounds(drawable, null, null, null);
        }
        drawable.setBounds(0, 0, drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight());
    }

    @Override
    public void onResume() {
        super.onResume();
    }
}
