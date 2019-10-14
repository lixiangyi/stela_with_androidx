package com.stela.comics_unlimited.ui.innerBrowse;

import com.lxy.baselibs.mvp.BaseModel;
import com.lxy.baselibs.net.BaseHttpResult;
import com.stela.comics_unlimited.data.entity.HomeBrowseEntity;
import com.stela.comics_unlimited.data.entity.HomeNewBrowseEntity;
import com.stela.comics_unlimited.data.entity.HomePageEntity;
import com.stela.comics_unlimited.data.repository.RetrofitUtils;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;


/**
 * @author xuhao
 * @date 2018/6/13 15:35
 * @desc
 */
public class InnerBrowseModel extends BaseModel implements InnerBrowseContract.Model {

    @Override
    public Observable<BaseHttpResult<HomeNewBrowseEntity>> getBrowseList(int pageNo, String seriesId, String seriesName) {
        return RetrofitUtils.getHttpService().getBrowseList(pageNo,10,seriesId,seriesName);
    }

    @Override
    public Observable<BaseHttpResult<HomePageEntity>> getBrowseListNew(String homeId,String userId) {
        return RetrofitUtils.getHttpService().pageHome(homeId,userId,"");
    }


}
