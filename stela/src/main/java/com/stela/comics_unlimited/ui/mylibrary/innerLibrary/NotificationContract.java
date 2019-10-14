package com.stela.comics_unlimited.ui.mylibrary.innerLibrary;


import com.lxy.baselibs.mvp.IModel;
import com.lxy.baselibs.mvp.IView;
import com.lxy.baselibs.net.BaseHttpResult;
import com.stela.comics_unlimited.data.entity.NotificationEntity;
import com.stela.comics_unlimited.data.entity.PageEntity;

import io.reactivex.Observable;

/**
 * @author xuhao
 * @date 2018/6/12 22:57
 * @desc 契约类
 */
public interface NotificationContract {


    //对于经常使用的关于UI的方法可以定义到IView中,如显示隐藏进度条,和显示文字消息
    interface View extends IView {

        void showData(PageEntity<NotificationEntity> pageEntity);
        void deleteSuccess(Object s,int position);
        void deleteAllSuccess(String s);
        void updateSuccess(Object s,int position);
        void updateAllSuccess(String s);
    }

    interface Model extends IModel {

        Observable<BaseHttpResult<PageEntity<NotificationEntity>>> getNotificationList(String userId, int pageNo);
        Observable<BaseHttpResult<Object>> deleteNotification( String notificationId);
        Observable<BaseHttpResult<String>> deleteNotificationAll( String userId);
        Observable<BaseHttpResult<Object>> updateNotificationStateById( String notificationId);
        Observable<BaseHttpResult<String>> updateNotificationStateByUserId( String userId);
    }

}
