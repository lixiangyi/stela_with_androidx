package com.stela.comics_unlimited.ui.series;


import com.lxy.baselibs.mvp.BaseModel;
import com.lxy.baselibs.net.BaseHttpResult;
import com.stela.comics_unlimited.data.entity.SeriesEntity;
import com.stela.comics_unlimited.data.entity.PersonEntity;
import com.stela.comics_unlimited.data.repository.RetrofitUtils;

import io.reactivex.Observable;

/**
 * @author xuhao
 * @date 2018/6/13 13:58
 * @desc 对数据层进行操作（比如，是否需要缓存，调用不同 apiservce，外界无需知道内部怎么实现）
 */
public class SeriesModel extends BaseModel implements SeriesContract.Model {

    @Override
    public Observable<BaseHttpResult<SeriesEntity>> getSeriesInfo(String seriesId, String userid) {
        return RetrofitUtils.getHttpService().getSeriesInfo(seriesId,userid);
    }
    @Override
    public Observable<BaseHttpResult<PersonEntity>> updataUser(String userId) {
        return RetrofitUtils.getHttpService().updataUser(userId);
    }


}
