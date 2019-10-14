package com.stela.comics_unlimited.ui.home.model;

import com.lxy.baselibs.mvp.BaseModel;
import com.lxy.baselibs.net.BaseHttpResult;
import com.stela.comics_unlimited.data.entity.HomePageEntity;
import com.stela.comics_unlimited.data.repository.RetrofitUtils;
import com.stela.comics_unlimited.ui.home.contract.HomeContract;

import io.reactivex.Observable;


/**
 * @author xuhao
 * @date 2018/6/13 15:35
 * @desc
 */
public class HomeModel extends BaseModel implements HomeContract.Model {

    @Override
    public Observable<BaseHttpResult<HomePageEntity>> pageHome(String id,String userId,String deeplinkId) {
        return RetrofitUtils.getHttpService().pageHome(id,userId,deeplinkId);
    }

}
