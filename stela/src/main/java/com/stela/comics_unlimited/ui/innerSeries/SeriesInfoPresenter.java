package com.stela.comics_unlimited.ui.innerSeries;


import com.lxy.baselibs.mvp.BasePresenter;
import com.lxy.baselibs.net.BaseHttpResult;
import com.lxy.baselibs.net.BaseObserver;
import com.lxy.baselibs.rx.RxSchedulers;
import com.stela.comics_unlimited.app.DataStore;

/**
 * @author xuhao
 * @date 2018/6/12 22:57
 * @desc
 */
public class SeriesInfoPresenter extends BasePresenter<SeriesInfoContract.Model, SeriesInfoContract.View> {
    @Override
    protected SeriesInfoContract.Model createModel() {
        return new SeriesInfoModel();
    }


    public void setCollection(String seriesId,int flag,String seriesType ){
        getModel().addUserCollect(seriesId, DataStore.getUserInfo().id,flag,seriesType )
                .compose(RxSchedulers.applySchedulers(getLifecycleProvider()))
                .subscribe(new BaseObserver<String>(getView()) {
                    @Override
                    public void onSuccess(BaseHttpResult<String> result) {
                        if (result != null) {
                            getView().collectSuccess();
                        }
                    }

                    @Override
                    public void onFailure(String errMsg, int code) {
                        getView().showError(errMsg);
                    }
                });
    }
    public void setLikes(String seriesId,int flag){
        getModel().userLikes(seriesId, DataStore.getUserInfo().id,flag)
                .compose(RxSchedulers.applySchedulers(getLifecycleProvider()))
                .subscribe(new BaseObserver<String>(getView()) {
                    @Override
                    public void onSuccess(BaseHttpResult<String> result) {
                        if (result != null) {
                            getView().likeSuccess();
                        }
                    }

                    @Override
                    public void onFailure(String errMsg, int code) {
                        getView().showError(errMsg);
                    }
                });
    }
}
