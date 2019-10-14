package com.stela.comics_unlimited.ui;


import com.lxy.baselibs.mvp.IModel;
import com.lxy.baselibs.mvp.IView;
import com.lxy.baselibs.net.BaseHttpResult;
import com.stela.comics_unlimited.data.entity.NotificationEntity;
import com.stela.comics_unlimited.data.entity.PersonEntity;
import com.stela.comics_unlimited.data.entity.VersionEntity;
import com.stela.comics_unlimited.pay.Purchase;

import io.reactivex.Observable;

/**
 * @author xuhao
 * @date 2018/6/12 16:18
 * @desc 契约类
 */
public interface MainContract {

    //对于经常使用的关于UI的方法可以定义到IView中,如显示隐藏进度条,和显示文字消息
    interface View extends IView {
        void updateSuccess(PersonEntity result);
        void verifyGooglePaySuccess(String s, Purchase purchase);
        void notificationCount(NotificationEntity num);
        void versionResult(VersionEntity versionEntity);
        void updateNotificationSuccess(String s);

    }

    interface Model extends IModel {
        Observable<BaseHttpResult<PersonEntity>> updataUser(String userId);
        Observable<BaseHttpResult<String>> verifyGooglePay(String userId,String purchaseToken,String packageName,String productId);
        Observable<BaseHttpResult<Object>> userActive(String userId);
        Observable<BaseHttpResult<VersionEntity>> updateVersion(String versionCode);
        Observable<BaseHttpResult<NotificationEntity>> selectUserNotificationByCount(String userId);
        Observable<BaseHttpResult<String>> updateNotificationToken(String notificationToken,String userId);

    }
}
