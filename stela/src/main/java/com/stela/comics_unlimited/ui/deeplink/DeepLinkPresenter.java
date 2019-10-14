package com.stela.comics_unlimited.ui.deeplink;

import com.lxy.baselibs.mvp.BasePresenter;
import com.lxy.baselibs.net.BaseHttpResult;
import com.lxy.baselibs.net.BaseObserver;
import com.lxy.baselibs.rx.RxSchedulers;
import com.stela.comics_unlimited.app.DataStore;
import com.stela.comics_unlimited.data.entity.ChapterEntity;
import com.stela.comics_unlimited.data.entity.DeeplinkEntity;

import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;

public class DeepLinkPresenter extends BasePresenter<DeepLinkContract.Model, DeepLinkContract.View> {

    @Override
    protected DeepLinkContract.Model createModel() {
        return new DeepLinkModel();
    }

    public void getDeeplinkList( String deeplinkId) {
        getModel().getDeeplinkList(deeplinkId)
                .compose(RxSchedulers.applySchedulers(getLifecycleProvider()))
                .subscribe(new BaseObserver<DeeplinkEntity>(getView(),false) {
                    @Override
                    public void onSuccess(BaseHttpResult<DeeplinkEntity> result) {
                        if (result != null) {
                            getView().showChapter(result.getData());
                        }
                    }

                    @Override
                    public void onFailure(String errMsg,int code) {
                        getView().showError(errMsg);
                    }
                });
    }
    public void closeOut() {
        long splashTime = 3000;

        addDispose(Observable.timer(splashTime, TimeUnit.MILLISECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(aLong -> getView().closeOut()));
    }



}
