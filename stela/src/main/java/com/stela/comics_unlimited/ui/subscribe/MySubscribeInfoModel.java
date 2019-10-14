package com.stela.comics_unlimited.ui.subscribe;


import com.lxy.baselibs.mvp.BaseModel;
import com.lxy.baselibs.net.BaseHttpResult;
import com.stela.comics_unlimited.data.entity.SubscriptionEntity;
import com.stela.comics_unlimited.data.repository.RetrofitUtils;

import io.reactivex.Observable;

/**
 * @author xuhao
 * @date 2018/6/13 13:58
 * @desc 对数据层进行操作（比如，是否需要缓存，调用不同 apiservce，外界无需知道内部怎么实现）
 */
public class MySubscribeInfoModel extends BaseModel implements MySubscribeInfoContract.Model {


    @Override
    public Observable<BaseHttpResult<SubscriptionEntity>> getMySubInfo(String userid) {
        return RetrofitUtils.getHttpService().getMySubscribeInfo(userid);
    }
}
