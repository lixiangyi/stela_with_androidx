package com.stela.comics_unlimited.ui.collection;

import com.lxy.baselibs.mvp.BaseModel;
import com.lxy.baselibs.net.BaseHttpResult;
import com.stela.comics_unlimited.data.entity.CollectionEntity;
import com.stela.comics_unlimited.data.entity.HomeBrowseEntity;
import com.stela.comics_unlimited.data.entity.PageEntity;
import com.stela.comics_unlimited.data.entity.SeriesEntity;
import com.stela.comics_unlimited.data.repository.RetrofitUtils;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;


/**
 * @author xuhao
 * @date 2018/6/13 15:35
 * @desc
 */
public class CollectionModel extends BaseModel implements CollectionContract.Model {


    @Override
    public Observable<BaseHttpResult<PageEntity<SeriesEntity>>> findByUserCollectSeries(String userId, String collectionId, int pageNo) {
        return RetrofitUtils.getHttpService().findByUserCollectSeries(userId,collectionId,pageNo);
    }

    @Override
    public Observable<BaseHttpResult<String>> addUserCollect(String seriesId, String userId, int flag, String seriesType) {
        return RetrofitUtils.getHttpService().addUserCollect(seriesId,userId,flag,seriesType);
    }
}
