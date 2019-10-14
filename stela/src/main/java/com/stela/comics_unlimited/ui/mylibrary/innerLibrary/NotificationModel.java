package com.stela.comics_unlimited.ui.mylibrary.innerLibrary;

import com.lxy.baselibs.mvp.BaseModel;
import com.lxy.baselibs.net.BaseHttpResult;
import com.stela.comics_unlimited.data.entity.NotificationEntity;
import com.stela.comics_unlimited.data.entity.PageEntity;
import com.stela.comics_unlimited.data.repository.RetrofitUtils;

import io.reactivex.Observable;


/**
 * @author xuhao
 * @date 2018/6/13 15:35
 * @desc
 */
public class NotificationModel extends BaseModel implements NotificationContract.Model {


    @Override
    public Observable<BaseHttpResult<PageEntity<NotificationEntity>>> getNotificationList(String userId, int pageNo) {
        return RetrofitUtils.getHttpService().getNotificationList(userId,pageNo);
    }

    @Override
    public Observable<BaseHttpResult<Object>> deleteNotification(String userId) {
        return RetrofitUtils.getHttpService().deleteNotificationById(userId);
    }

    @Override
    public Observable<BaseHttpResult<String>> deleteNotificationAll(String userId) {
        return RetrofitUtils.getHttpService().deleteNotificationList(userId);
    }

    @Override
    public Observable<BaseHttpResult<Object>> updateNotificationStateById(String notificationId) {
        return RetrofitUtils.getHttpService().updateNotification(notificationId);
    }

    @Override
    public Observable<BaseHttpResult<String>> updateNotificationStateByUserId(String userId) {
        return RetrofitUtils.getHttpService().updateAllNotificationState(userId);
    }
}
