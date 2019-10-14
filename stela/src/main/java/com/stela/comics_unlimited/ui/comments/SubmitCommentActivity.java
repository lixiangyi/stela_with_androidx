package com.stela.comics_unlimited.ui.comments;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.lxy.baselibs.app.AppConstants;
import com.lxy.baselibs.glide.GlideUtils;
import com.lxy.baselibs.rx.RxBus;
import com.lxy.baselibs.utils.DisplayUtils;
import com.lxy.baselibs.utils.ToastUtils;
import com.stela.analytics.model.Page;
import com.stela.comics_unlimited.Base.BaseMvpActivity;
import com.stela.comics_unlimited.R;
import com.stela.comics_unlimited.data.entity.CommentsEntity;
import com.stela.comics_unlimited.event.CommentsNotifyEvent;
import com.stela.comics_unlimited.util.stela_sdk.StelaAnalyticsUtil;
import com.stela.comics_unlimited.util.Tools;

import butterknife.BindView;
import butterknife.OnClick;

import static com.lxy.baselibs.app.BaseApplication.getContext;

public class SubmitCommentActivity extends BaseMvpActivity<SubmitCommentPresenter> implements SubmitCommentContract.View {

    @BindView(R.id.et_comment)
    EditText etComment;
    @BindView(R.id.tv_submit_comment)
    TextView tvSubmitComment;
    @BindView(R.id.iv_avatar)
    ImageView ivAvatar;
    @BindView(R.id.tv_nickname)
    TextView tvNickName;
    @BindView(R.id.tv_time)
    TextView tvTime;
    @BindView(R.id.tv_comment_content)
    TextView tvCommentContent;
    @BindView(R.id.tv_like_num)
    TextView tvLikeNum;
    @BindView(R.id.item_comment_layout)
    RelativeLayout itemCommentLayout;
    @BindView(R.id.rl_bottom)
    RelativeLayout rlBottom;
    private int type;//0 common  1 replay
    private String mId = "";
    private CommentsEntity mCommentsEntity;

    public static void start(Context context, int type, CommentsEntity commentContent) {
        Intent starter = new Intent(context, SubmitCommentActivity.class);
        starter.putExtra(AppConstants.TYPE, type);
        starter.putExtra(AppConstants.COMMENTENTITY, commentContent);
        context.startActivity(starter);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_comment_edit;
    }

    @Override
    protected void getIntent(Intent intent) {
        type = intent.getIntExtra(AppConstants.TYPE, -1);
        mCommentsEntity = (CommentsEntity) intent.getSerializableExtra(AppConstants.COMMENTENTITY);
        if (type == -1) {
            finish();
            ToastUtils.showShort("No Data found,please try again!");
        }
    }

    @Override
    protected void initView() {
        showBack();
        initTitle();
        Tools.showSoftInputFromWindow(this, etComment);

    }

    private void initTitle() {
        String title = "";
        String key = "";
        tvLikeNum.setVisibility(View.GONE);
        if (type == 1) {
            title = "Reply to   " + mCommentsEntity.nickname;
            key = "eply to omment";
            initReplyComment();
            mId = mCommentsEntity.id;
            itemCommentLayout.setVisibility(View.VISIBLE);
        } else if (type == 0) {
            title = "Leave a Comment";
            key = "leave a comment";
            mId = mCommentsEntity.seriesId;
            itemCommentLayout.setVisibility(View.GONE);
        }
        setTitle(title);
        //埋点
        Page page = new Page();
        page.setPageId(mId + "");
        page.setPageName(key);
        StelaAnalyticsUtil.page(page);
    }


    private void initReplyComment() {
        GlideUtils.loadCircleImageWithBoder(getContext(), ivAvatar, mCommentsEntity.imgPortraitUrl, R.drawable.avatar_bg_1);
        tvNickName.setText(TextUtils.isEmpty(mCommentsEntity.nickname) ? "" : (mCommentsEntity.nickname + " | "));
        tvTime.setText(" | " + mCommentsEntity.createdAtStr);
        tvCommentContent.setText(mCommentsEntity.content);
    }

    @Override
    protected void initListener() {
        addLayoutListener(rlBottom, tvSubmitComment);
//        SoftKeyBoardListener softKeyBoardListener = new SoftKeyBoardListener(this);
//        //软键盘状态监听
//        softKeyBoardListener.setListener(new SoftKeyBoardListener.OnSoftKeyBoardChangeListener() {
//            @Override
//            public void keyBoardShow(int height) {
//                //当软键盘显示的时候
//                tvSubmitComment.requestLayout();
//                ToastUtils.showShort(height + "");
//
//            }
//
//            @Override
//            public void keyBoardHide(int height) {
//                //当软键盘隐藏的时候
//                tvSubmitComment.requestLayout();
//                ToastUtils.showShort(height + "");
//            }
//        });


    }

    @Override
    protected void initData() {

    }

    @Override
    protected SubmitCommentPresenter createPresenter() {
        return new SubmitCommentPresenter();
    }


    @Override
    public void showError(String msg) {
        ToastUtils.showShort(msg);
    }


    @OnClick(R.id.tv_submit_comment)
    public void onViewClicked() {
        DisplayUtils.closeInputMethod(this, etComment);
        String content = etComment.getText().toString().trim();
        if (TextUtils.isEmpty(content)) {
            ToastUtils.showShort("Comments cannot be empty!");
            return;
        }
        if (type == 0) {
            mPresenter.submitSeriesComment(content, mId, mCommentsEntity.chapterId);
        } else {
            mPresenter.submitReplyComment(content, mId, mCommentsEntity.seriesId, mCommentsEntity.chapterId);
        }
    }

    @Override
    public void submitCommentSuccess(String s) {
        if (mCommentsEntity != null) {
            RxBus.getDefault().post(new CommentsNotifyEvent(type, mCommentsEntity.chapterId));
        } else {
            RxBus.getDefault().post(new CommentsNotifyEvent(type));
        }
//        ToastUtils.showShort(s);
        finish();
    }

}
