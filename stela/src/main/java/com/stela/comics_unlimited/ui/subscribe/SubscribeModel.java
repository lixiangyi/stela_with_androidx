package com.stela.comics_unlimited.ui.subscribe;

import com.lxy.baselibs.mvp.BaseModel;
import com.lxy.baselibs.net.BaseHttpResult;
import com.stela.comics_unlimited.data.entity.HomeBrowseEntity;
import com.stela.comics_unlimited.data.entity.HomePageEntity;
import com.stela.comics_unlimited.data.entity.SubscriptionEntity;
import com.stela.comics_unlimited.data.repository.RetrofitUtils;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;


/**
 * @author xuhao
 * @date 2018/6/13 15:35
 * @desc
 */
public class SubscribeModel extends BaseModel implements SubscribeContract.Model {


    @Override
    public Observable<BaseHttpResult<SubscriptionEntity>> getSubPageInfo(String mSeriesId) {
        return RetrofitUtils.getHttpService().getSubscriptionView(2,mSeriesId);
    }

    @Override
    public Observable<BaseHttpResult<String>> verifyGooglePay(String userId,String purchaseToken,String packageName,String productId) {
        return RetrofitUtils.getHttpService().googlePlaySubscription(userId,purchaseToken,packageName,productId);
    }
}
