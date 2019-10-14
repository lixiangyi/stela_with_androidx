package com.stela.comics_unlimited.ui.subscribe;


import com.lxy.baselibs.mvp.IModel;
import com.lxy.baselibs.mvp.IView;
import com.lxy.baselibs.net.BaseHttpResult;
import com.stela.comics_unlimited.data.entity.CommentsEntity;
import com.stela.comics_unlimited.data.entity.PageEntity;
import com.stela.comics_unlimited.data.entity.SubscriptionEntity;

import io.reactivex.Observable;

/**
 * @author xuhao
 * @date 2018/6/12 16:18
 * @desc 契约类
 */
public interface MySubscribeInfoContract {

    //对于经常使用的关于UI的方法可以定义到IView中,如显示隐藏进度条,和显示文字消息
    interface View extends IView {
        void showListData(SubscriptionEntity subscriptionEntity);
    }

    interface Model extends IModel {
        Observable<BaseHttpResult<SubscriptionEntity>> getMySubInfo(String userid);

    }
}
