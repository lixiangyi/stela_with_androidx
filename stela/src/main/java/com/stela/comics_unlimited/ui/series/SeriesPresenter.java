package com.stela.comics_unlimited.ui.series;

import com.lxy.baselibs.mvp.BasePresenter;
import com.lxy.baselibs.net.BaseHttpResult;
import com.lxy.baselibs.net.BaseObserver;
import com.lxy.baselibs.rx.RxSchedulers;
import com.stela.comics_unlimited.app.DataStore;
import com.stela.comics_unlimited.data.entity.SeriesEntity;
import com.stela.comics_unlimited.data.entity.PersonEntity;


/**
 * @author xuhao
 * @date 2018/6/12 16:18
 * @desc
 */
public class SeriesPresenter extends BasePresenter<SeriesContract.Model, SeriesContract.View> {

    @Override
    protected SeriesContract.Model createModel() {
        return new SeriesModel();
    }

    public void requestData(String seriesid) {
        getModel().getSeriesInfo(seriesid, DataStore.getUserInfo().id)
                .compose(RxSchedulers.applySchedulers(getLifecycleProvider()))
                .subscribe(new BaseObserver<SeriesEntity>(getView()) {
                    @Override
                    public void onSuccess(BaseHttpResult<SeriesEntity> result) {
                        if (result != null) {
                            getView().showData(setAWSSeriesEntity(result.getData()));
                        }
                    }

                    @Override
                    public void onFailure(String errMsg, int code) {
                        getView().showError(errMsg);
                    }
                });
    }

    public SeriesEntity setAWSSeriesEntity(SeriesEntity seriesEntity) {
        if (seriesEntity != null && seriesEntity.chapterList != null) {
            for (int i = 0; i < seriesEntity.chapterList.size(); i++) {
                seriesEntity.chapterList.get(i).seriesTitle = seriesEntity.title;
            }
        }
        return seriesEntity;
    }


    public void updataUser() {

        getModel().updataUser(DataStore.getUserInfo().id)
                .compose(RxSchedulers.applySchedulers(getLifecycleProvider()))
                .subscribe(new BaseObserver<PersonEntity>(getView(), false) {
                    @Override
                    public void onSuccess(BaseHttpResult<PersonEntity> result) {
                        if (result != null) {
                            getView().updateSuccess(result.getData());
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
