package com.stela.comics_unlimited.ui.person;

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
public class ResetPasswordPresenter extends BasePresenter<ResetPasswordContract.Model, ResetPasswordContract.View> {

    @Override
    protected ResetPasswordContract.Model createModel() {
        return new ResetPasswordModel();
    }

    public void updatePasswordOldAndNew(String oldPassword, String newPassword) {
        getModel().updatePasswordOldAndNew(oldPassword, newPassword, DataStore.getUserInfo().id)
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


}
