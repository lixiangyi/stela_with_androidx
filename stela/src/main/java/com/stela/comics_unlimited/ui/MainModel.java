package com.stela.comics_unlimited.ui;


import com.lxy.baselibs.mvp.BaseModel;
import com.lxy.baselibs.net.BaseHttpResult;
import com.stela.comics_unlimited.data.entity.NotificationEntity;
import com.stela.comics_unlimited.data.entity.PersonEntity;
import com.stela.comics_unlimited.data.entity.VersionEntity;
import com.stela.comics_unlimited.data.repository.RetrofitUtils;

import io.reactivex.Observable;

/**
 * @author xuhao
 * @date 2018/6/13 13:58
 * @desc 对数据层进行操作（比如，是否需要缓存，调用不同 apiservce，外界无需知道内部怎么实现）
 */
public class MainModel extends BaseModel implements MainContract.Model {


    @Override
    public Observable<BaseHttpResult<PersonEntity>> updataUser(String userId) {
        return RetrofitUtils.getHttpService().updataUser(userId);
    }
    @Override
    public Observable<BaseHttpResult<String>> verifyGooglePay(String userId,String purchaseToken,String packageName,String productId) {
        return RetrofitUtils.getHttpService().googlePlaySubscription(userId,purchaseToken,packageName,productId);
    }

    @Override
    public Observable<BaseHttpResult<Object>> userActive(String userId) {
        return RetrofitUtils.getHttpService().userActive(userId);
    }

    @Override
    public Observable<BaseHttpResult<VersionEntity>> updateVersion(String versionCode) {
        return RetrofitUtils.getHttpService().selectVersionCount(versionCode);
    }

    @Override
    public Observable<BaseHttpResult<NotificationEntity>> selectUserNotificationByCount(String userId) {
        return RetrofitUtils.getHttpService().selectUserNotificationByCount(userId);
    }

    @Override
    public Observable<BaseHttpResult<String>> updateNotificationToken(String notificationToken, String userId) {
        return RetrofitUtils.getHttpService().updateNotificationToken(notificationToken,userId);
    }

}
