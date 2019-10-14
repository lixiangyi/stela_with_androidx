package com.stela.comics_unlimited.ui.parental_controls;

import com.lxy.baselibs.mvp.BasePresenter;
import com.lxy.baselibs.net.BaseHttpResult;
import com.lxy.baselibs.net.BaseObserver;
import com.lxy.baselibs.rx.RxSchedulers;
import com.stela.comics_unlimited.app.DataStore;


/**
 * @author xuhao
 * @date 2018/6/12 16:18
 * @desc
 */
public class EnterPINPresenter extends BasePresenter<EnterPINContract.Model, EnterPINContract.View> {

    @Override
    protected EnterPINContract.Model createModel() {
        return new EnterPINModel();
    }

    public void updatePin(String pin) {
        getModel().updatePin(0, pin, DataStore.getUserInfo().id)
                .compose(RxSchedulers.applySchedulers(getLifecycleProvider()))
                .subscribe(new BaseObserver<String>(getView()) {
                    @Override
                    public void onSuccess(BaseHttpResult<String> result) {
                        if (result != null) {
                            getView().updataSuccess(result.getData());
                        }
                    }

                    @Override
                    public void onFailure(String errMsg, int code) {
                        getView().showError(errMsg);
                    }
                })
        ;
    }

    public void forgetPin(String email) {
        getModel().forgotPin(email, 1)
                .compose(RxSchedulers.applySchedulers(getLifecycleProvider()))
                .subscribe(new BaseObserver<String>(getView()) {
                    @Override
                    public void onSuccess(BaseHttpResult<String> result) {
                        if (result != null) {
                            getView().forgotPinSendSuccess(result.getData());
                        }
                    }

                    @Override
                    public void onFailure(String errMsg, int code) {
                        getView().showError(errMsg);
                    }
                })
        ;
    }


}
