package com.stela.comics_unlimited.ui.subscribe;


import com.lxy.baselibs.mvp.BasePresenter;
import com.lxy.baselibs.net.BaseHttpResult;
import com.lxy.baselibs.net.BaseObserver;
import com.lxy.baselibs.rx.RxSchedulers;
import com.stela.comics_unlimited.app.DataStore;
import com.stela.comics_unlimited.data.entity.SubscriptionEntity;
import com.stela.comics_unlimited.pay.Purchase;
import com.stela.comics_unlimited.util.stela_sdk.StelaAnalyticsUtil;

/**
 * @author xuhao
 * @date 2018/6/12 22:57
 * @desc
 */
public class SubscribePresenter extends BasePresenter<SubscribeContract.Model, SubscribeContract.View> {
    @Override
    protected SubscribeContract.Model createModel() {
        return new SubscribeModel();
    }


    public void getSubpageInfo(String mSeriesId){
        getModel().getSubPageInfo(mSeriesId)
                .compose(RxSchedulers.applySchedulers(getLifecycleProvider()))
                .subscribe(new BaseObserver<SubscriptionEntity>(getView(), true) {
                    @Override
                    public void onSuccess(BaseHttpResult<SubscriptionEntity> result) {
                        if (result != null) {
                            getView().showData(result.getData());
                        }
                    }

                    @Override
                    public void onFailure(String errMsg, int code) {
                        getView().showError(errMsg);
                    }
                })
        ;
    }
    public void verifyGooglePay(Purchase purchase){
       getModel().verifyGooglePay(DataStore.getUserInfo().id,purchase.getToken(),purchase.getPackageName(),purchase.getSku())
               .compose(RxSchedulers.applySchedulers(getLifecycleProvider()))
               .subscribe(new BaseObserver<String>(getView(), false) {
                   @Override
                   public void onSuccess(BaseHttpResult<String> result) {
                       if (result != null) {
                           getView().verifyGooglePaySuccess(result.getData(),purchase);
                       }
                   }

                   @Override
                   public void onFailure(String errMsg, int code) {
                       getView().showError(errMsg);
                       com.stela.analytics.model.Purchase p = new com.stela.analytics.model.Purchase();
                       p.setProduct(purchase.getSku());
                       p.setStatus("failure");
                       StelaAnalyticsUtil.purchase(p);
                   }
               })
       ;
    }

}
