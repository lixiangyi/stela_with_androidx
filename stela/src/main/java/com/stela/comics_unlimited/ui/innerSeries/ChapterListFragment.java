package com.stela.comics_unlimited.ui.innerSeries;

import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.LayoutRes;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.lxy.baselibs.app.AppConstants;
import com.lxy.baselibs.rx.RxBus;
import com.lxy.baselibs.utils.ToastUtils;
import com.stela.analytics.model.Chapter;
import com.stela.comics_unlimited.Base.BaseFragment;
import com.stela.comics_unlimited.R;
import com.stela.comics_unlimited.app.MyApp;
import com.stela.comics_unlimited.data.entity.ChapterEntity;
import com.stela.comics_unlimited.event.ContinueReadEvent;
import com.stela.comics_unlimited.event.ReadRecordEvent;
import com.stela.comics_unlimited.ui.chapter.ChapterActivity;
import com.stela.comics_unlimited.ui.subscribe.SubscribeActivity;
import com.stela.comics_unlimited.util.SharedPreferencesTool;
import com.stela.comics_unlimited.util.stela_sdk.StelaAnalyticsUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import io.reactivex.functions.Consumer;


/**
 * @author LXY
 * @date 2019/1/23 22:57
 * @desc
 */
public class ChapterListFragment extends BaseFragment<ChapterListPresenter> implements ChapterListContract.View {


    @BindView(R.id.rv_chapter_list)
    RecyclerView rvChapterList;
    private ArrayList<ChapterEntity> chapterEntities;
    private MyAdapter myAdapter;

    public static ChapterListFragment getInstance(ArrayList<ChapterEntity> chapterEntities) {
        ChapterListFragment fragment = new ChapterListFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable(AppConstants.SERIES_CHAPTER_LIST, chapterEntities);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_chapter_list;
    }

    @Override
    protected ChapterListPresenter createPresenter() {
        return new ChapterListPresenter();
    }

    @Override
    protected void initView() {
        chapterEntities = (ArrayList<ChapterEntity>) getArguments().getSerializable(AppConstants.SERIES_CHAPTER_LIST);
    }

    @Override
    protected void initListener() {
        //继续阅读更新
        mPresenter.addDispose(RxBus.getDefault().toObservable(ContinueReadEvent.class)
                .subscribe(new Consumer<ContinueReadEvent>() {
                    @Override
                    public void accept(ContinueReadEvent continueReadEvent) throws Exception {
                        if (continueReadEvent != null) {
                            // TODO: 2019/9/11  已读未读
                            // 刷新状态
                            if (chapterEntities != null && chapterEntities.size() > 0) {
                                for (int i = 0; i < chapterEntities.size(); i++) {
                                    if (i == continueReadEvent.mChapterPosition) {
                                        chapterEntities.get(i).browseState = ChapterEntity.LASTREADSTATE_YES;
                                    } else {
                                        chapterEntities.get(i).browseState = ChapterEntity.LASTREADSTATE_NO;
                                    }

                                }
                                myAdapter.notifyDataSetChanged();
                            }
                        }
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {

                    }
                }));
        //已读未读更新
        mPresenter.addDispose(RxBus.getDefault().toObservable(ReadRecordEvent.class)
                .subscribe(new Consumer<ReadRecordEvent>() {
                    @Override
                    public void accept(ReadRecordEvent readRecordEvent) throws Exception {
                        if (readRecordEvent != null) {
                            // 刷新状态
                            if (chapterEntities != null && chapterEntities.size() > 0) {
                                for (int i = 0; i < chapterEntities.size(); i++) {
                                    if (i == readRecordEvent.mChapterPosition) {
                                        chapterEntities.get(i).ifRead = ChapterEntity.CHAPTERSTATE_READED;
                                        break;
                                    }
                                }
                                myAdapter.notifyItemChanged(readRecordEvent.mChapterPosition);
                            }
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
        initRecyclerview();
    }

    private void initRecyclerview() {
        //本地修改阅读记录
        changeReadState();
        myAdapter = new MyAdapter(R.layout.item_chapter_list, chapterEntities);
        LinearLayoutManager layout = new LinearLayoutManager(getContext());
        rvChapterList.setLayoutManager(layout);
        rvChapterList.setAdapter(myAdapter);
        myAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                switch (chapterEntities.get(position).chapterState) {
                    case ChapterEntity.CHAPTERSTATE_LOCK:
                        SubscribeActivity.start(getActivity(), chapterEntities.get(position).seriesId);
                        break;
                    case ChapterEntity.CHAPTERSTATE_FREE:
                    case ChapterEntity.CHAPTERSTATE_PAYED:
                        //埋点
                        Chapter chapter = new Chapter();
                        chapter.setChapterId(chapterEntities.get(position).id);
                        chapter.setChapterName(chapterEntities.get(position).title);
                        chapter.setSeriesId(chapterEntities.get(position).seriesId);
                        chapter.setSeriesName(chapterEntities.get(position).seriesTitle);
                        StelaAnalyticsUtil.chapter(chapter);
                        ChapterActivity.start(getActivity(), chapterEntities, position);
                        break;
                    case ChapterEntity.CHAPTERSTATE_DATA:
                    default:
                        break;
                }
            }
        });
        myAdapter.setEmptyView(R.layout.empty_normal, rvChapterList);
    }

    private void changeReadState() {
        HashMap<String, String> readRecord = (HashMap) SharedPreferencesTool.getObj(MyApp.getContext(), SharedPreferencesTool.KEY_CHAPTER_READ_RECORD);
        if (readRecord != null&&readRecord.size()>0&&chapterEntities!=null&&chapterEntities.size()>0) {
            for (ChapterEntity chapterEntity : chapterEntities) {
                if (readRecord.containsKey(chapterEntity.id)) {
                    chapterEntity.ifRead = ChapterEntity.CHAPTERSTATE_READED;
                }
            }

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

    public class MyAdapter extends BaseQuickAdapter<ChapterEntity, BaseViewHolder> {

        MyAdapter(@LayoutRes int layoutResId, @Nullable List<ChapterEntity> data) {
            super(layoutResId, data);
        }

        @Override
        protected void convert(BaseViewHolder helper, ChapterEntity item) {
            TextView mTvTitle = helper.getView(R.id.tv_chapter_title);
            View mViewContinue = helper.getView(R.id.view_continue);
            RelativeLayout itemBg = helper.getView(R.id.rl_item_chapter);
            mTvTitle.setText(item.title + "");
            if ((item.chapterState == ChapterEntity.CHAPTERSTATE_FREE ||
                    item.chapterState == ChapterEntity.CHAPTERSTATE_PAYED) && item.ifRead == ChapterEntity.CHAPTERSTATE_UNREAD) {
                mTvTitle.setTextColor(ContextCompat.getColor(mContext, R.color.stela_blue));
            } else {
                mTvTitle.setTextColor(ContextCompat.getColor(mContext, R.color.color_73));
            }
            if (item.browseState == ChapterEntity.LASTREADSTATE_YES) {
                mViewContinue.setVisibility(View.VISIBLE);
            } else {
                mViewContinue.setVisibility(View.GONE);
            }
            switch (item.chapterState) {
                case ChapterEntity.CHAPTERSTATE_LOCK:
                case ChapterEntity.CHAPTERSTATE_DATA:
                    itemBg.setBackgroundColor(ContextCompat.getColor(mContext, R.color.color_d8));
                    break;
                case ChapterEntity.CHAPTERSTATE_FREE:
                case ChapterEntity.CHAPTERSTATE_PAYED:
                default:
                    itemBg.setBackgroundColor(ContextCompat.getColor(mContext, R.color.white));
                    break;
            }
        }

    }
}
