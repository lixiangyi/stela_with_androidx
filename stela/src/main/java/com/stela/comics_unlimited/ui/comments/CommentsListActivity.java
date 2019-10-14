package com.stela.comics_unlimited.ui.comments;

import android.content.Context;
import android.content.Intent;
import androidx.annotation.LayoutRes;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.SimpleItemAnimator;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.lxy.baselibs.app.AppConstants;
import com.stela.comics_unlimited.Base.BaseMvpActivity;
import com.lxy.baselibs.glide.GlideUtils;
import com.lxy.baselibs.rx.RxBus;
import com.lxy.baselibs.utils.ToastUtils;
import com.makeramen.roundedimageview.RoundedImageView;
import com.stela.comics_unlimited.R;
import com.stela.comics_unlimited.app.DataStore;
import com.stela.comics_unlimited.data.entity.CommentsEntity;
import com.stela.comics_unlimited.data.entity.PageEntity;
import com.stela.comics_unlimited.event.CommentsNotifyEvent;
import com.stela.comics_unlimited.event.UserNotifyEvent;
import com.stela.comics_unlimited.ui.person.ChangeNicknameActivity;
import com.stela.comics_unlimited.ui.person.ChooseAvatarActivity;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import io.reactivex.functions.Consumer;

import static com.lxy.baselibs.app.BaseApplication.getContext;

public class CommentsListActivity extends BaseMvpActivity<CommentsListPresenter> implements CommentsListContract.View {


    @BindView(R.id.rv_comments_list)
    RecyclerView rvCommentsList;
    @BindView(R.id.iv_head)
    RoundedImageView ivHead;
    @BindView(R.id.tv_head)
    TextView tvHead;
    @BindView(R.id.tv_add_comment)
    TextView tvAddComment;
    @BindView(R.id.rl_comment)
    RelativeLayout rlComment;
    @BindView(R.id.srl_series_comment)
    SwipeRefreshLayout srlSeriesComment;
    private MyAdapter myAdapter;
    private String mSeriesId;
    private String mChapterId;
    private int mPageNo = 1;
    private List<CommentsEntity> mCommentsEntities = new ArrayList<>();
    private boolean isLastPage;
    private View emptyView;

    public static void start(Context context, String seriesId, String chapterId) {
        Intent starter = new Intent(context, CommentsListActivity.class);
        starter.putExtra(AppConstants.SERIES_ID, seriesId);
        starter.putExtra(AppConstants.CHAPTER_ID, chapterId);
        context.startActivity(starter);

    }


    @Override
    protected int getLayoutId() {
        return R.layout.activity_comment_list;
    }

    @Override
    protected void getIntent(Intent intent) {
        mSeriesId = intent.getStringExtra(AppConstants.SERIES_ID);
        mChapterId = intent.getStringExtra(AppConstants.CHAPTER_ID);
    }

    @Override
    protected void initView() {
        showBack();
        setTitle("Comments");
        initEmptyView("No comments yet.");
        initRecyclerview();
        initEvent();
        GlideUtils.loadCircleImageWithBoder(getContext(), ivHead, DataStore.getUserInfo().imgPortrait, R.drawable.avatar_bg_1);
    }

    private void initRecyclerview() {
        myAdapter = new MyAdapter(R.layout.item_comment_list, mCommentsEntities);
        LinearLayoutManager layout = new LinearLayoutManager(getContext());
        rvCommentsList.setLayoutManager(layout);
        rvCommentsList.setAdapter(myAdapter);
        ((SimpleItemAnimator) rvCommentsList.getItemAnimator()).setSupportsChangeAnimations(false);
        rvCommentsList.getItemAnimator().setChangeDuration(0);
        myAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                mCommentsEntities.get(position).chapterId = mChapterId;
                if (checkUser(1, mCommentsEntities.get(position))) {
                    SubmitCommentActivity.start(CommentsListActivity.this, 1, mCommentsEntities.get(position));
                }
            }
        });
        myAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                if (view.getId() == R.id.ll_second_comment) {

                }
            }
        });
        myAdapter.setEmptyView(emptyView);
        srlSeriesComment.setColorSchemeColors(ContextCompat.getColor(getContext(), R.color.stela_blue));
        srlSeriesComment.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mPageNo = 1;
                mPresenter.getSeriesCommentList(mSeriesId, mChapterId, mPageNo, 0);
            }
        });
        myAdapter.setEnableLoadMore(true);
        myAdapter.setOnLoadMoreListener(new BaseQuickAdapter.RequestLoadMoreListener() {
            @Override
            public void onLoadMoreRequested() {
                if (!isLastPage) {
                    mPresenter.getSeriesCommentList(mSeriesId, mChapterId, mPageNo, 0);
                } else {
                    myAdapter.loadMoreEnd(true);
                }
            }
        }, rvCommentsList);
    }

    private void initEmptyView(String msg) {
        emptyView = LayoutInflater.from(getContext()).inflate(R.layout.empty_refresh, null);
        emptyView.findViewById(R.id.btn_refresh).setVisibility(View.GONE);
        ((TextView) (emptyView.findViewById(R.id.tv_empty_msg))).setText(msg);
    }

    @Override
    protected void initListener() {

    }

    @Override
    protected void initData() {
        srlSeriesComment.setRefreshing(true);
        mPresenter.getSeriesCommentList(mSeriesId, mChapterId, mPageNo, 0);

    }

    @Override
    protected boolean useEventBus() {
        return false;
    }

    private void initEvent() {
        //列表更新
        mPresenter.addDispose(RxBus.getDefault().toObservable(CommentsNotifyEvent.class)
                .subscribe(new Consumer<CommentsNotifyEvent>() {
                    @Override
                    public void accept(CommentsNotifyEvent commentsNotifyEvent) throws Exception {
                        if (commentsNotifyEvent != null) {
                            if (commentsNotifyEvent.mType == 1) {
                                mPresenter.getSeriesCommentList(mSeriesId, mChapterId, 1, mPageNo * CommentsListPresenter.PAGESIZE);
                            } else {
                                mPageNo = 1;
                                mPresenter.getSeriesCommentList(mSeriesId, mChapterId, mPageNo, 0);
                            }
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
                            mPresenter.getSeriesCommentList(mSeriesId, mChapterId, 1, mPageNo * CommentsListPresenter.PAGESIZE);
                            GlideUtils.loadCircleImageWithBoder(getContext(), ivHead, DataStore.getUserInfo().imgPortrait, R.drawable.avatar_bg_1);
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
    protected CommentsListPresenter createPresenter() {
        return new CommentsListPresenter();
    }


    @Override
    public void showError(String msg) {
        ToastUtils.showShort(msg);
        if (srlSeriesComment.isRefreshing()) {
            srlSeriesComment.setRefreshing(false);
        }
    }


    @Override
    public void likeSuccess(int position) {
        TextView mTvLike = (TextView) myAdapter.getViewByPosition(position, R.id.tv_like_num);
        if (myAdapter.getItem(position).flagLikeComments == 1) {
            mTvLike.setSelected(true);
        } else {
            mTvLike.setSelected(false);
        }
//        myAdapter.notifyItemChanged(position);
    }


    @OnClick(R.id.rl_comment)
    public void onViewClicked() {
        CommentsEntity commentsEntity = new CommentsEntity();
        commentsEntity.seriesId = mSeriesId;
        commentsEntity.chapterId = mChapterId;
        if (checkUser(0, commentsEntity)) {
            SubmitCommentActivity.start(CommentsListActivity.this, 0, commentsEntity);
        }
    }

    private boolean checkUser(int i, CommentsEntity commentsEntity) {
        if (DataStore.getUserInfo().ifNickname == 0) {
            ChangeNicknameActivity.start(this, i, commentsEntity);
            return false;
        }
        if (DataStore.getUserInfo().ifPortrait == 0) {
            ChooseAvatarActivity.start(this, i, commentsEntity);
            return false;
        }
        return true;
    }

    @Override
    public void showListData(PageEntity<CommentsEntity> pageEntity) {
        if (srlSeriesComment.isRefreshing()) {
            srlSeriesComment.setRefreshing(false);
        }
        isLastPage = pageEntity.isLastPage;
        if (mPageNo == 1) {
            mCommentsEntities.clear();
            mCommentsEntities = pageEntity.list;
            myAdapter.setNewData(mCommentsEntities);
//            myAdapter.setEmptyView(ViewUtil.getEmptyView(this, "消息列表空荡荡的~", false));
            mPageNo++;
        } else {
            List<CommentsEntity> commentsEntityList = pageEntity.list;
            if (commentsEntityList != null) {
                if (commentsEntityList.size() > 0) {
                    mCommentsEntities.addAll(commentsEntityList);
                    myAdapter.setNewData(mCommentsEntities);
                    myAdapter.loadMoreComplete();
                    mPageNo++;
                } else {
                    myAdapter.loadMoreEnd(true);
                }
            }
        }
    }

    @Override
    public void showFreshListData(PageEntity<CommentsEntity> pageEntity) {
        isLastPage = pageEntity.isLastPage;
        mCommentsEntities.clear();
        mCommentsEntities = pageEntity.list;
        myAdapter.setNewData(mCommentsEntities);
    }


    public class MyAdapter extends BaseQuickAdapter<CommentsEntity, BaseViewHolder> {

        MyAdapter(@LayoutRes int layoutResId, @Nullable List<CommentsEntity> data) {
            super(layoutResId, data);
        }

        @Override
        protected void convert(BaseViewHolder helper, CommentsEntity item) {
            ImageView mIvAvatar = helper.getView(R.id.iv_avatar);
//            if (mIvAvatar.getTag(R.id.iv_avatar) != null && !mIvAvatar.getTag(R.id.iv_avatar).equals(item.imgPortraitUrl)) {
//            }else {
//                mIvAvatar.setTag(R.id.iv_avatar,null);
            GlideUtils.loadCircleImageWithBoder(getContext(), mIvAvatar, item.imgPortraitUrl, R.drawable.avatar_bg_1);
//                mIvAvatar.setTag(R.id.iv_avatar,item.imgPortraitUrl);
//            }
            TextView mTvNickName = helper.getView(R.id.tv_nickname);
            mTvNickName.setText(item.nickname);
            TextView mTvTime = helper.getView(R.id.tv_time);
            mTvTime.setText(" | " + item.createdAtStr);
            TextView mTvLike = helper.getView(R.id.tv_like_num);
//            mTvLike.setText(item.likeCount + "");
            TextView mTvContent = helper.getView(R.id.tv_comment_content);
            mTvContent.setText(item.content + "");
            mTvLike.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (item.flagLikeComments == 1) {
                        item.flagLikeComments = 0;
                        item.likeCount = item.likeCount - 1;
                    } else {
                        item.flagLikeComments = 1;
                        item.likeCount = item.likeCount + 1;
                    }
                    mPresenter.dolike(item.id, helper.getAdapterPosition(), item.flagLikeComments);
                }
            });
            if (item.flagLikeComments == 1) {
                mTvLike.setSelected(true);
            } else {
                mTvLike.setSelected(false);
            }
            LinearLayout mLinearLayout = helper.getView(R.id.ll_second_comment);
            helper.addOnClickListener(R.id.ll_second_comment);
            mLinearLayout.removeAllViews();
            if (item.sonCommentsList != null && item.sonCommentsList.size() > 0) {
                mLinearLayout.setVisibility(View.VISIBLE);
                for (int i = 0; i < item.sonCommentsList.size(); i++) {
                    View relativeLayout = LayoutInflater.from(getContext()).inflate(R.layout.item_comment_list_second, null, false);
                    ImageView imageView = relativeLayout.findViewById(R.id.iv_avatar);
//                    if (imageView.getTag(R.id.iv_avatar) != null && !imageView.getTag(R.id.iv_avatar).equals(item.imgPortraitUrl)) {
//                    }else {
//                        imageView.setTag(R.id.iv_avatar,null);
                    GlideUtils.loadCircleImageWithBoder(getContext(), imageView, item.sonCommentsList.get(i).imgPortraitUrl, R.drawable.avatar_bg_1);
//                        imageView.setTag(R.id.iv_avatar,item.sonCommentsList.get(i).imgPortraitUrl);
//                    }
                    TextView mTvComtent = relativeLayout.findViewById(R.id.tv_comment_content);
                    mTvComtent.setText(item.sonCommentsList.get(i).content);
                    TextView mTvNickName2 = relativeLayout.findViewById(R.id.tv_nickname);
                    mTvNickName2.setText(item.sonCommentsList.get(i).nickname);
                    TextView mTvTime2 = relativeLayout.findViewById(R.id.tv_time);
                    mTvTime2.setText(" | " + item.sonCommentsList.get(i).createdAtStr);
                    mLinearLayout.addView(relativeLayout);
                }
            } else {
                mLinearLayout.setVisibility(View.GONE);
            }
        }
    }
}
