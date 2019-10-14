package com.stela.comics_unlimited.ui.mylibrary.innerLibrary.adapter;

import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.LayoutRes;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.SimpleItemAnimator;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.stela.analytics.model.Series;
import com.stela.comics_unlimited.R;
import com.stela.comics_unlimited.data.entity.CollectionEntity;
import com.stela.comics_unlimited.data.entity.SeriesEntity;
import com.stela.comics_unlimited.ui.collection.CollectionActivity;
import com.stela.comics_unlimited.ui.series.SeriesActivity;
import com.stela.comics_unlimited.util.stela_sdk.StelaAnalyticsUtil;
import com.stela.comics_unlimited.widget.InnerRecyclerview;

import java.util.List;

/**
 * @author Administrator
 */
public class CollectionAdapter extends BaseQuickAdapter<CollectionEntity, BaseViewHolder> {
    private ICallBack callBack;

    public interface ICallBack {
        void callBackMethod(SeriesEntity seriesEntity, View view);
    }

    public void setOnDeleteListenter(ICallBack callBack) {
        this.callBack = callBack;
    }


    public CollectionAdapter(@LayoutRes int layoutResId, @Nullable List<CollectionEntity> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, CollectionEntity item) {
        //head
        ((TextView) (helper.getView(R.id.tv_inner_library_title))).setText(item.seriesType);
        //foot
        TextView textView = helper.getView(R.id.tv_collection_more);
        View collectionFooterLine = helper.getView(R.id.view_foot_line);
        if (TextUtils.isEmpty(item.id)) {
            collectionFooterLine.setVisibility(View.GONE);
            textView.setVisibility(View.GONE);
        } else {
            textView.setVisibility(View.VISIBLE);
            collectionFooterLine.setVisibility(View.VISIBLE);
            textView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    CollectionActivity.start(mContext, item.id, item.seriesType);
                }
            });
        }
        //normal
        CollectionInnerAdapter myAdapter = new CollectionInnerAdapter(R.layout.item_library_normal, item.list);
        LinearLayoutManager layout = new LinearLayoutManager(mContext);
        InnerRecyclerview innerRv = helper.getView(R.id.rv_inner_collection);
        ((SimpleItemAnimator) innerRv.getItemAnimator()).setSupportsChangeAnimations(false);
        innerRv.setLayoutManager(layout);
        innerRv.setAdapter(myAdapter);
        innerRv.requestFocus();
        innerRv.setFocusableInTouchMode(false);
        innerRv.setNestedScrollingEnabled(false);
        myAdapter.setOnItemChildClickListener(new OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                SeriesEntity seriesEntity = myAdapter.getData().get(position);
                SeriesActivity.start(mContext, seriesEntity.id);
                //埋点
                Series series = new Series();
                series.setSeriesId(seriesEntity.id);
                series.setSeriesName(seriesEntity.title);
                series.setGroupId("");
                series.setGroupName("");
                series.setPageName("collection");
                series.setPageId("");
                StelaAnalyticsUtil.click( "series_view");
                StelaAnalyticsUtil.series(series);
            }
        });
        myAdapter.setOnItemChildLongClickListener(new OnItemChildLongClickListener() {
            @Override
            public boolean onItemChildLongClick(BaseQuickAdapter adapter, View view, int position) {
                if (callBack != null) {
                    callBack.callBackMethod((SeriesEntity) adapter.getData().get(position), view);
                }
                return true;
            }
        });
    }


}
