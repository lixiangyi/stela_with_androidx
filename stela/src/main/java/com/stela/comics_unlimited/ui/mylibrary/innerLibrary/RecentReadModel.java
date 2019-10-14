package com.stela.comics_unlimited.ui.mylibrary.innerLibrary;

import com.lxy.baselibs.mvp.BaseModel;
import com.lxy.baselibs.net.BaseHttpResult;
import com.stela.comics_unlimited.data.entity.PageEntity;
import com.stela.comics_unlimited.data.entity.SeriesEntity;
import com.stela.comics_unlimited.data.repository.RetrofitUtils;

import io.reactivex.Observable;


/**
 * @author xuhao
 * @date 2018/6/13 15:35
 * @desc
 */
public class RecentReadModel extends BaseModel implements RecentReadContract.Model {

    @Override
    public Observable<BaseHttpResult<PageEntity<SeriesEntity>>> findByUserBrowseList(String userId, int pageNo) {
        return RetrofitUtils.getHttpService().findByUserBrowseList(userId,pageNo);
    }

    @Override
    public Observable<BaseHttpResult<String>> deleteBrowseList(String seriesId, String userId) {
        return RetrofitUtils.getHttpService().deleteRecentReadList(userId,seriesId);
    }
}
