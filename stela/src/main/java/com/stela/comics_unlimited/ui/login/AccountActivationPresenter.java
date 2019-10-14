package com.stela.comics_unlimited.ui.login;

import com.lxy.baselibs.mvp.BasePresenter;
import com.lxy.baselibs.net.BaseHttpResult;
import com.lxy.baselibs.net.BaseObserver;
import com.lxy.baselibs.rx.RxSchedulers;


/**
 * @author xuhao
 * @date 2018/6/12 16:18
 * @desc
 */
public class AccountActivationPresenter extends BasePresenter<AccountActivationContract.Model, AccountActivationContract.View> {

    @Override
    protected AccountActivationContract.Model createModel() {
        return new AccountActivationModel();
    }


    public void register(String email) {
        getModel().retrieveEmail(email)
                .compose(RxSchedulers.applySchedulers(getLifecycleProvider()))
                .subscribe(new BaseObserver<String>(getView()) {
                    @Override
                    public void onSuccess(BaseHttpResult<String> result) {
                        if (result != null) {
                            getView().reSendSuccess(result.getData());
                        }
                    }

                    @Override
                    public void onFailure(String errMsg, int code) {
                        getView().showError(errMsg);
                    }
                });
    }


}
