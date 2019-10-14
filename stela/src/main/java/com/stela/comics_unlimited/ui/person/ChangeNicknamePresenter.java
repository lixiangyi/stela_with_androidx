package com.stela.comics_unlimited.ui.person;

import com.lxy.baselibs.mvp.BasePresenter;
import com.lxy.baselibs.net.BaseHttpResult;
import com.lxy.baselibs.net.BaseObserver;
import com.lxy.baselibs.rx.RxSchedulers;
import com.stela.comics_unlimited.app.DataStore;

import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;


/**
 * @author xuhao
 * @date 2018/6/12 16:18
 * @desc
 */
public class ChangeNicknamePresenter extends BasePresenter<ChangeNicknameContract.Model, ChangeNicknameContract.View> {

    @Override
    protected ChangeNicknameContract.Model createModel() {
        return new ChangeNicknameModel();
    }

    public void UpdataNickname(String nickname) {
        getModel().updataNickname(nickname, DataStore.getUserInfo().id)
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
