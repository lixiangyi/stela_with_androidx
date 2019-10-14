package com.stela.comics_unlimited.ui.subscribe;


import com.lxy.baselibs.mvp.IModel;
import com.lxy.baselibs.mvp.IView;
import com.lxy.baselibs.net.BaseHttpResult;
import com.stela.comics_unlimited.data.entity.SubscriptionEntity;
import com.stela.comics_unlimited.pay.Purchase;

import io.reactivex.Observable;

/**
 * @author xuhao
 * @date 2018/6/12 22:57
 * @desc 契约类
 */
public interface SubscribeContract {


    //对于经常使用的关于UI的方法可以定义到IView中,如显示隐藏进度条,和显示文字消息
    interface View extends IView {

        void showData(SubscriptionEntity subscriptionEntity);
        void verifyGooglePaySuccess(String s, Purchase purchase);
    }

    interface Model extends IModel {

        Observable<BaseHttpResult<SubscriptionEntity>> getSubPageInfo(String mSeriesId);
        Observable<BaseHttpResult<String>> verifyGooglePay(String userId,String purchaseToken,String packageName,String productId);
    }

}
