package com.stela.comics_unlimited.ui.collection.adapter;

import androidx.annotation.LayoutRes;
import androidx.annotation.Nullable;
import android.widget.ImageView;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.lxy.baselibs.glide.GlideUtils;
import com.stela.comics_unlimited.R;
import com.stela.comics_unlimited.data.entity.SeriesEntity;

import java.util.List;

public class CollectionAllAdapter extends BaseQuickAdapter<SeriesEntity, BaseViewHolder> {

    public CollectionAllAdapter(@LayoutRes int layoutResId, @Nullable List<SeriesEntity> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, SeriesEntity item) {
        ((TextView) (helper.getView(R.id.tv_browse_title))).setText(item.title);
        ((TextView) (helper.getView(R.id.tv_browse_subtitle))).setText(item.description);
        ImageView mIvBrowse = helper.getView(R.id.iv_browse);
        if (item.assets != null && item.assets.size() > 0 && item.assets.get(0) != null) {
//            if (mIvBrowse.getTag(R.id.iv_browse) != null && !mIvBrowse.getTag().equals(item.assets.get(0).url)) {
//
//            } else {
//                mIvBrowse.setTag(R.id.iv_browse, null);
                GlideUtils.loadImageHttps(mContext, mIvBrowse, item.assets.get(0).url, R.color.stela_blue);
//                mIvBrowse.setTag(R.id.iv_browse,item.assets.get(0).url);
//            }
        } else {
            GlideUtils.loadImageHttps(mContext, mIvBrowse, "", R.color.stela_blue);
        }


    }


}

