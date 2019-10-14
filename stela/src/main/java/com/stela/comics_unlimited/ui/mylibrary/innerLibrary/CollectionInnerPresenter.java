package com.stela.comics_unlimited.ui.mylibrary.innerLibrary;


import com.lxy.baselibs.mvp.BasePresenter;
import com.lxy.baselibs.net.BaseHttpResult;
import com.lxy.baselibs.net.BaseObserver;
import com.lxy.baselibs.rx.RxSchedulers;
import com.stela.comics_unlimited.app.DataStore;
import com.stela.comics_unlimited.data.entity.CollectionEntity;
import com.stela.comics_unlimited.data.entity.SeriesEntity;

import java.util.ArrayList;
import java.util.List;

/**
 * @author xuhao
 * @date 2018/6/12 22:57
 * @desc
 */
public class CollectionInnerPresenter extends BasePresenter<CollectionInnerContract.Model, CollectionInnerContract.View> {

    @Override
    protected CollectionInnerContract.Model createModel() {
        return new CollectionInnerModel();
    }


    public void requestData() {
        getModel().findByUserCollectComicList(DataStore.getUserInfo().id)
                .compose(RxSchedulers.applySchedulers(getLifecycleProvider()))
                .subscribe(new BaseObserver<List<CollectionEntity>>(getView(), false) {
                    @Override
                    public void onSuccess(BaseHttpResult<List<CollectionEntity>> result) {
                        if (result != null) {
//                            getView().showListData(setDatalist(result.getData()));
                            getView().showListDataNew(result.getData());
                        }
                    }

                    @Override
                    public void onFailure(String errMsg, int code) {
                        getView().showError(errMsg);
                    }
                });
    }

    private List<SeriesEntity> setDatalist(List<CollectionEntity> collectionEntities) {
        List<SeriesEntity> seriesEntities = new ArrayList<>();
        for (int i = 0; i < collectionEntities.size(); i++) {
            if (collectionEntities.get(i).list != null && collectionEntities.get(i).list.size() > 0) {
                SeriesEntity header = new SeriesEntity();
                header.title = collectionEntities.get(i).seriesType;
                header.type = SeriesEntity.HEADER;
                header.subTitle = collectionEntities.get(i).description;
                seriesEntities.add(header);
                seriesEntities.addAll(collectionEntities.get(i).list);
                SeriesEntity footer = new SeriesEntity();
                footer.title = collectionEntities.get(i).seriesType;
                footer.type = SeriesEntity.FOOTER;
                footer.id = collectionEntities.get(i).id;
                seriesEntities.add(footer);
            }
        }
        return seriesEntities;
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
                    public void onFailure(String errMsg, int code) {
                        getView().showError(errMsg);
                    }
                });
    }


}
