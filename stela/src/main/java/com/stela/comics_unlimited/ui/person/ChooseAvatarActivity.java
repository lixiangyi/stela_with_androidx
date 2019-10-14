package com.stela.comics_unlimited.ui.person;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.LayoutRes;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.lxy.baselibs.app.AppConstants;
import com.lxy.baselibs.glide.GlideUtils;
import com.lxy.baselibs.rx.RxBus;
import com.lxy.baselibs.utils.CommonUtils;
import com.lxy.baselibs.utils.ToastUtils;
import com.stela.comics_unlimited.Base.BaseMvpActivity;
import com.stela.comics_unlimited.R;
import com.stela.comics_unlimited.app.DataStore;
import com.stela.comics_unlimited.data.entity.CommentsEntity;
import com.stela.comics_unlimited.data.entity.ImgEntity;
import com.stela.comics_unlimited.data.entity.PersonEntity;
import com.stela.comics_unlimited.event.UserNotifyEvent;
import com.stela.comics_unlimited.ui.comments.SubmitCommentActivity;
import com.stela.comics_unlimited.util.SpaceItemDecoration;
import com.stela.comics_unlimited.util.stela_sdk.StelaAnalyticsUtil;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

public class ChooseAvatarActivity extends BaseMvpActivity<ChooseAvatarPresenter> implements ChooseAvatarContract.View {


    @BindView(R.id.rv_choose_avatar)
    RecyclerView rvChooseAvatar;
    @BindView(R.id.tv_select_avatar)
    TextView tvSelect;
    private ArrayList<ImgEntity> assets = new ArrayList<>();
    private MyAdapter myAdapter;
    private String fileName;
    private int mType;
    private CommentsEntity mCommentsEntity;

    public static void start(Context context, int i, CommentsEntity commentsEntity) {
        Intent starter = new Intent(context, ChooseAvatarActivity.class);
        starter.putExtra(AppConstants.TYPE, i);
        starter.putExtra(AppConstants.COMMENTENTITY, commentsEntity);
        context.startActivity(starter);
    }

    public static void start(Context context) {
        Intent starter = new Intent(context, ChooseAvatarActivity.class);
        context.startActivity(starter);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_choose_avatar;
    }

    @Override
    protected void getIntent(Intent intent) {
        mType = intent.getIntExtra(AppConstants.TYPE, 0);
        mCommentsEntity = (CommentsEntity) intent.getSerializableExtra(AppConstants.COMMENTENTITY);
    }

    @Override
    protected void initView() {
        showBack();
        setTitle("Choose Avatar");
        mPresenter.getAvatarList();

    }

    @Override
    protected void initListener() {

    }

    @Override
    protected void initData() {
        myAdapter = new MyAdapter(R.layout.item_avatar, assets);
        GridLayoutManager layout = new GridLayoutManager(this, 3);
        rvChooseAvatar.setLayoutManager(layout);
        rvChooseAvatar.addItemDecoration(new SpaceItemDecoration(CommonUtils.dp2px(4)));
        rvChooseAvatar.setAdapter(myAdapter);
        myAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                for (int i = 0; i < assets.size(); i++) {
                    assets.get(i).is_select = 0;
                }
                if (assets.get(position).is_select == 0) {
                    assets.get(position).is_select = 1;
                } else {
                    assets.get(position).is_select = 0;
                }
                for (int i = 0; i < assets.size(); i++) {
                    if (assets.get(i).is_select == 1) {
                        fileName = assets.get(i).fileName;
                        tvSelect.setEnabled(true);
                        break;
                    }
                }
                adapter.notifyDataSetChanged();
            }
        });
    }

    @Override
    protected ChooseAvatarPresenter createPresenter() {
        return new ChooseAvatarPresenter();
    }


    @Override
    public void showError(String msg) {
        ToastUtils.showShort(msg);
    }


    @OnClick(R.id.tv_select_avatar)
    public void onViewClicked() {
        mPresenter.selectAvatar(fileName);
        StelaAnalyticsUtil.click("avatar");
    }

    @Override
    public void selectAvatar(String s) {
        PersonEntity userEntity = DataStore.getUserInfo();
        userEntity.imgPortrait = s;
        userEntity.ifPortrait = 1;
        DataStore.setUserInfo(userEntity);
        RxBus.getDefault().post(new UserNotifyEvent());
        if (mCommentsEntity != null) {
            SubmitCommentActivity.start(this, mType, mCommentsEntity);
        }
        finish();
    }

    @Override
    public void getAvatarList(List<ImgEntity> assetList) {
        assets.addAll(assetList);
        myAdapter.setNewData(assets);
    }

    public class MyAdapter extends BaseQuickAdapter<ImgEntity, BaseViewHolder> {

        MyAdapter(@LayoutRes int layoutResId, @Nullable List<ImgEntity> data) {
            super(layoutResId, data);
        }

        @Override
        protected void convert(BaseViewHolder helper, ImgEntity item) {
            GlideUtils.loadImageHttps(mContext, ((ImageView) helper.getView(R.id.iv_avatar)), item.url, R.color.stela_blue);
            if (item.is_select == 0) {
                helper.setGone(R.id.iv_select, false);
                helper.setGone(R.id.iv_select_title, false);
            } else {
                helper.setGone(R.id.iv_select, true);
                helper.setGone(R.id.iv_select_title, true);
            }

        }

    }
}
