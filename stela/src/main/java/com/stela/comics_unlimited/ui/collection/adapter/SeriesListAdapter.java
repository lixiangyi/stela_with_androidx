package com.stela.comics_unlimited.ui.collection.adapter;

import androidx.annotation.LayoutRes;
import androidx.annotation.Nullable;

import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.stela.comics_unlimited.R;
import com.stela.comics_unlimited.data.entity.HomeBrowseEntity;

import java.util.List;

public class SeriesListAdapter extends BaseQuickAdapter<HomeBrowseEntity, BaseViewHolder> {

    public SeriesListAdapter(@LayoutRes int layoutResId, @Nullable List<HomeBrowseEntity> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, HomeBrowseEntity item) {
        ((TextView) (helper.getView(R.id.tv_browse_title))).setText(item.title);
        ((TextView) (helper.getView(R.id.tv_browse_subtitle))).setText(item.content);
//        GlideUtils.loadImage(mContext, ((ImageView) helper.getView(R.id.iv_browse)), item.imgurl, R.mipmap.stela_logo);
    }


}

