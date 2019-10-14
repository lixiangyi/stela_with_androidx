package com.stela.comics_unlimited.ui.mylibrary.innerLibrary;

import com.lxy.baselibs.mvp.BaseModel;
import com.lxy.baselibs.net.BaseHttpResult;
import com.stela.comics_unlimited.data.entity.CollectionEntity;
import com.stela.comics_unlimited.data.repository.RetrofitUtils;

import java.util.List;

import io.reactivex.Observable;


/**
 * @author xuhao
 * @date 2018/6/13 15:35
 * @desc
 */
public class CollectionInnerModel extends BaseModel implements CollectionInnerContract.Model {

    @Override
    public Observable<BaseHttpResult<List<CollectionEntity>>> findByUserCollectComicList( String userId) {
        return RetrofitUtils.getHttpService().findByUserCollectComicList(userId);
    }
    @Override
    public Observable<BaseHttpResult<String>> addUserCollect( String seriesId, String userid,int flag,String seriesType) {
        return RetrofitUtils.getHttpService().addUserCollect(seriesId,userid,flag,seriesType);
    }
}
