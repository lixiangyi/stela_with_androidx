package com.stela.comics_unlimited.ui.subscribe;

import com.lxy.baselibs.mvp.BasePresenter;
import com.lxy.baselibs.net.BaseHttpResult;
import com.lxy.baselibs.net.BaseObserver;
import com.lxy.baselibs.rx.RxSchedulers;
import com.stela.comics_unlimited.app.DataStore;
import com.stela.comics_unlimited.data.entity.PageEntity;
import com.stela.comics_unlimited.data.entity.SubscriptionEntity;


/**
 * @author xuhao
 * @date 2018/6/12 16:18
 * @desc
 */
public class MySubscribeInfoPresenter extends BasePresenter<MySubscribeInfoContract.Model, MySubscribeInfoContract.View> {

    @Override
    protected MySubscribeInfoContract.Model createModel() {
        return new MySubscribeInfoModel();
    }

    public void getMySubInfo() {
        getModel().getMySubInfo(DataStore.getUserInfo().id)
                .compose(RxSchedulers.applySchedulers(getLifecycleProvider()))
                .subscribe(new BaseObserver<SubscriptionEntity>(getView(), false) {
                    @Override
                    public void onSuccess(BaseHttpResult<SubscriptionEntity> result) {
                        if (result != null) {
                            getView().showListData(result.getData());
                        }
                    }

                    @Override
                    public void onFailure(String errMsg, int code) {
                        getView().showError(errMsg);
                    }
                });
    }


}
