package com.stela.comics_unlimited.ui.innerSeries;

import com.lxy.baselibs.mvp.BaseModel;
import com.lxy.baselibs.net.BaseHttpResult;
import com.stela.comics_unlimited.data.entity.HomeBrowseEntity;
import com.stela.comics_unlimited.data.repository.RetrofitUtils;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;


/**
 * @author xuhao
 * @date 2018/6/13 15:35
 * @desc
 */
public class SeriesInfoModel extends BaseModel implements SeriesInfoContract.Model {


    @Override
    public Observable<BaseHttpResult<String>> userLikes(String seriesId, String userid, int flag) {
        return RetrofitUtils.getHttpService().userLikes(seriesId, userid, flag);
    }
    @Override
    public Observable<BaseHttpResult<String>> addUserCollect( String seriesId, String userid,int flag,String seriesType) {
        return RetrofitUtils.getHttpService().addUserCollect(seriesId,userid,flag,seriesType);
    }
}
