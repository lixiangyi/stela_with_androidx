package com.stela.comics_unlimited.ui.person;

import com.lxy.baselibs.mvp.BasePresenter;
import com.lxy.baselibs.net.BaseHttpResult;
import com.lxy.baselibs.net.BaseObserver;
import com.lxy.baselibs.rx.RxSchedulers;


/**
 * @author xuhao
 * @date 2018/6/12 16:18
 * @desc
 */
public class ForgotPasswordPresenter extends BasePresenter<ForgotPasswordContract.Model, ForgotPasswordContract.View> {

    @Override
    protected ForgotPasswordContract.Model createModel() {
        return new ForgotPasswordModel();
    }

    public void submitpassword(String email) {
        getModel().forgotPassword(email,0/*0忘记密码1忘记pin*/)
                .compose(RxSchedulers.applySchedulers(getLifecycleProvider()))
                .subscribe(new BaseObserver<String>(getView()) {
                    @Override
                    public void onSuccess(BaseHttpResult<String> result) {
                        if (result != null) {
                            getView().sendSuccess(result.getData());
                        }
                    }

                    @Override
                    public void onFailure(String errMsg, int coder) {
                        getView().showError(errMsg);
                    }
                })
        ;

    }


}
