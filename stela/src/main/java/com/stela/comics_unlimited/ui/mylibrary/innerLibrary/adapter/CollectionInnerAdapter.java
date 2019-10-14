package com.stela.comics_unlimited.ui.mylibrary.innerLibrary.adapter;

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

public class CollectionInnerAdapter extends BaseQuickAdapter<SeriesEntity, BaseViewHolder> {

    public CollectionInnerAdapter(@LayoutRes int layoutResId, @Nullable List<SeriesEntity> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, SeriesEntity item) {
        ((TextView) (helper.getView(R.id.tv_browse_title))).setText(item.title);
        ((TextView) (helper.getView(R.id.tv_browse_subtitle))).setText(item.description);
        ImageView mIvBrowse = helper.getView(R.id.iv_browse);
        if (item.assets != null && item.assets.size() > 0 && item.assets.get(0) != null) {
            GlideUtils.loadImageHttps(mContext, mIvBrowse, item.assets.get(0).url, R.color.stela_blue);
        }
        helper.addOnLongClickListener(R.id.rl_series);
        helper.addOnClickListener(R.id.rl_series);
//        helper.itemView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                SeriesActivity.start(mContext, item.id);
//                //埋点
//                Series series = new Series();
//                series.setSeriesId(item.id);
//                series.setSeriesName(item.messageTitle);
//                series.setCardName("");
//                series.setPageName("collection");
//                series.setPageId("");
//                StelaAnalyticsUtil.click(mContext, "series_view", "", "");
//                StelaAnalyticsUtil.series(mContext, series);
//            }
//        });
    }
}

