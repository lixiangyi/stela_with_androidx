package com.stela.comics_unlimited.ui.collection;


import com.lxy.baselibs.mvp.BasePresenter;
import com.lxy.baselibs.net.BaseHttpResult;
import com.lxy.baselibs.net.BaseObserver;
import com.lxy.baselibs.rx.RxSchedulers;
import com.stela.comics_unlimited.app.DataStore;
import com.stela.comics_unlimited.data.entity.CollectionEntity;
import com.stela.comics_unlimited.data.entity.PageEntity;
import com.stela.comics_unlimited.data.entity.SeriesEntity;

import java.util.List;

/**
 * @author xuhao
 * @date 2018/6/12 22:57
 * @desc
 */
public class CollectionPresenter extends BasePresenter<CollectionContract.Model, CollectionContract.View> {
    @Override
    protected CollectionContract.Model createModel() {
        return new CollectionModel();
    }


    public void requestData(String collectionId ,int pageNo) {
        getModel().findByUserCollectSeries(DataStore.getUserInfo().id,collectionId,pageNo)
                .compose(RxSchedulers.applySchedulers(getLifecycleProvider()))
                .subscribe(new BaseObserver<PageEntity<SeriesEntity>>(getView(), false) {
                    @Override
                    public void onSuccess(BaseHttpResult<PageEntity<SeriesEntity>> result) {
                        if (result != null) {
                            getView().showData(result.getData());
                        }
                    }

                    @Override
                    public void onFailure(String errMsg, int code) {
                        getView().showError(errMsg);
                    }
                });
    }
    public void cancelCollection(String seriesId, String seriesType, int position) {
        getModel().addUserCollect(seriesId, DataStore.getUserInfo().id, 0, seriesType)
                .compose(RxSchedulers.applySchedulers(getLifecycleProvider()))
                .subscribe(new BaseObserver<String>(getView()) {
                    @Override
                    public void onSuccess(BaseHttpResult<String> result) {
                        if (result != null) {
                            getView().cancelSuccess(result.getData(), position);
                        }
                    }

                    @Override
                    public void onFailure(String errMsg,int code) {
                        getView().showError(errMsg);
                    }
                });
    }
}
