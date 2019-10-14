package com.stela.comics_unlimited.ui.person;

import com.lxy.baselibs.mvp.BasePresenter;


/**
 * @author xuhao
 * @date 2018/6/12 16:18
 * @desc
 */
public class PersonInfoPresenter extends BasePresenter<PersonInfoContract.Model, PersonInfoContract.View> {

    @Override
    protected PersonInfoContract.Model createModel() {
        return new PersonInfoModel();
    }

    public void getMySubInfo() {
//        getModel().getMySubInfo(DataStore.getUserInfo().id)
//                .compose(RxSchedulers.applySchedulers(getLifecycleProvider()))
//                .subscribe(new BaseObserver<SubscriptionEntity>(getView(), false) {
//                    @Override
//                    public void onSuccess(BaseHttpResult<SubscriptionEntity> result) {
//                        if (result != null) {
//                            getView().showListData(result.getData());
//                        }
//                    }
//
//                    @Override
//                    public void onFailure(String errMsg, boolean isNetError) {
//                        getView().showError(errMsg);
//                    }
//                });
    }


}
