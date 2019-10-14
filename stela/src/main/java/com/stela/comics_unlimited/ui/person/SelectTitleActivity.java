package com.stela.comics_unlimited.ui.person;

import android.content.Context;
import android.content.Intent;
import androidx.annotation.LayoutRes;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.lxy.baselibs.glide.GlideUtils;
import com.lxy.baselibs.utils.CommonUtils;
import com.lxy.baselibs.utils.ToastUtils;
import com.stela.comics_unlimited.Base.BaseMvpActivity;
import com.stela.comics_unlimited.R;
import com.stela.comics_unlimited.data.entity.ImgEntity;
import com.stela.comics_unlimited.ui.MainActivity;
import com.stela.comics_unlimited.util.SpaceItemDecoration;
import com.stela.comics_unlimited.util.stela_sdk.StelaAnalyticsUtil;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

public class SelectTitleActivity extends BaseMvpActivity<SelectTitlePresenter> implements SelectTitleContract.View {


    @BindView(R.id.rv_select_title)
    RecyclerView rvSelectTitle;
    @BindView(R.id.tv_select_title)
    TextView tvSelectTitle;
    private ArrayList<ImgEntity> imgEntities = new ArrayList<>();
    private ArrayList<ImgEntity> mSelectImgEntities = new ArrayList<>();
    private MyAdapter myAdapter;
    private String ids;

    public static void start(Context context) {
        Intent starter = new Intent(context, SelectTitleActivity.class);
        context.startActivity(starter);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_select_title;
    }

    @Override
    protected void getIntent(Intent intent) {

    }

    @Override
    protected void initView() {
//        showBack();
        setTitle("Titles You May Like");
        mPresenter.getTitleList();

    }

    @Override
    protected void initListener() {

    }

    @Override
    protected void initData() {
        myAdapter = new MyAdapter(R.layout.item_title, imgEntities);
        GridLayoutManager layout = new GridLayoutManager(this, 3);
        rvSelectTitle.setLayoutManager(layout);
        rvSelectTitle.addItemDecoration(new SpaceItemDecoration(CommonUtils.dp2px(4)));
        rvSelectTitle.setAdapter(myAdapter);
        myAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {

                if (imgEntities.get(position).is_select == 0) {
                    if (mSelectImgEntities.size() > 2) {
                        ToastUtils.showShort("only can select three titles!");
                        return;
                    }
                    imgEntities.get(position).is_select = 1;
                    mSelectImgEntities.add(imgEntities.get(position));
                } else {
                    imgEntities.get(position).is_select = 0;
                    mSelectImgEntities.remove(imgEntities.get(position));
                }
                if (mSelectImgEntities.size() > 0) {
                    tvSelectTitle.setEnabled(true);
                } else {
                    tvSelectTitle.setEnabled(false);
                }
                adapter.notifyDataSetChanged();
            }
        });
    }

    @Override
    protected SelectTitlePresenter createPresenter() {
        return new SelectTitlePresenter();
    }


    @Override
    public void showError(String msg) {
        ToastUtils.showLong(msg);
    }


    @OnClick(R.id.tv_select_title)
    public void onViewClicked() {
        for (int i = 0; i < imgEntities.size(); i++) {
            if (imgEntities.get(i).is_select == 1) {
                ids = imgEntities.get(i).id + ",";
            }
        }
        if (ids.endsWith(",")) {
            ids = ids.substring(0, ids.length() - 1);
        }
        mPresenter.selectTitle(ids);
        StelaAnalyticsUtil.click( "interest");
    }

    @Override
    public void selecSuccess(String s) {
//        ToastUtils.showShort(s);
        MainActivity.start(this);
        finish();
    }

    @Override
    public void findBySeriesGenresImg(List<ImgEntity> assetList) {
        imgEntities.addAll(assetList);
        myAdapter.setNewData(imgEntities);
    }

    @Override
    public void onBackPressed() {
//        super.onBackPressed();
    }

    public class MyAdapter extends BaseQuickAdapter<ImgEntity, BaseViewHolder> {

        MyAdapter(@LayoutRes int layoutResId, @Nullable List<ImgEntity> data) {
            super(layoutResId, data);
        }

        @Override
        protected void convert(BaseViewHolder helper, ImgEntity item) {
            GlideUtils.loadImageHttps(mContext, ((ImageView) helper.getView(R.id.iv_title)), item.url, GlideUtils.getRandomColor());
            if (item.is_select == 0) {
                helper.setGone(R.id.iv_select_title, false);
                helper.setGone(R.id.iv_select, false);
            } else {
                helper.setGone(R.id.iv_select_title, true);
                helper.setGone(R.id.iv_select, true);
            }

        }

    }
}
