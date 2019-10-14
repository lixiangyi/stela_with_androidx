package com.stela.comics_unlimited.ui.innerBrowse;


import com.lxy.baselibs.mvp.BasePresenter;
import com.lxy.baselibs.net.BaseHttpResult;
import com.lxy.baselibs.net.BaseObserver;
import com.lxy.baselibs.rx.RxSchedulers;
import com.stela.comics_unlimited.app.DataStore;
import com.stela.comics_unlimited.data.entity.HomeGroupEntity;
import com.stela.comics_unlimited.data.entity.HomeItemEntity;
import com.stela.comics_unlimited.data.entity.HomeNewBrowseEntity;
import com.stela.comics_unlimited.data.entity.HomePageEntity;
import com.stela.comics_unlimited.data.entity.SeriesAsset;
import com.stela.comics_unlimited.data.entity.SeriesEntity;

import java.util.ArrayList;

/**
 * @author xuhao
 * @date 2018/6/12 22:57
 * @desc
 */
public class InnerBrowsePresenter extends BasePresenter<InnerBrowseContract.Model, InnerBrowseContract.View> {
    @Override
    protected InnerBrowseContract.Model createModel() {
        return new InnerBrowseModel();
    }


    public void getBrowseList(int pageNo, String seriesId, String seriesName) {
        getModel().getBrowseList(pageNo, seriesId, seriesName)
                .compose(RxSchedulers.applySchedulers(getLifecycleProvider()))
                .subscribe(new BaseObserver<HomeNewBrowseEntity>(getView(), false) {
                    @Override
                    public void onSuccess(BaseHttpResult<HomeNewBrowseEntity> result) {
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

    public void getBrowseListNew(String id) {
        getModel().getBrowseListNew(id, DataStore.getUserInfo().id)
                .compose(RxSchedulers.applySchedulers(getLifecycleProvider()))
                .subscribe(new BaseObserver<HomePageEntity>(getView(), false) {
                    @Override
                    public void onSuccess(BaseHttpResult<HomePageEntity> result) {
                        if (result != null) {
                            getView().showNewData(setBrowseData(result.getData()),result.getData());
                        }
                    }

                    @Override
                    public void onFailure(String errMsg, int code) {
                        getView().showError(errMsg);
                    }
                });
    }

    private ArrayList<SeriesAsset> setBrowseData(HomePageEntity homePageEntity) {
        ArrayList<SeriesAsset> seriesEntities = new ArrayList<>();
        for (int i = 0; i < homePageEntity.childList.size(); i++) {
            HomeGroupEntity homeGroupEntity = homePageEntity.childList.get(i);
            for (int j = 0; j < homeGroupEntity.childList.size(); j++) {
                HomeItemEntity homeItemEntity = homeGroupEntity.childList.get(j);
                if (homeItemEntity.rowType == HomeItemEntity.i) {
                    if (homeItemEntity.rowAssets != null && homeItemEntity.rowAssets.size() > 0) {
                        for (int k = 0; k < homeItemEntity.rowAssets.size(); k++) {
//                            for (int n = 0; n < homeItemEntity.rowAssets.get(k).size(); n++) {
//                                SeriesAsset seriesAsset = homeItemEntity.rowAssets.get(k).get(n);
//                                seriesAsset.page_id = homePageEntity.id;
//                                seriesAsset.page_name= homePageEntity.messageTitle;
//                            }
                            seriesEntities.addAll(homeItemEntity.rowAssets.get(k));
                        }
                    }
                }

            }
        }
        for (int i = 0; i < seriesEntities.size(); i++) {
            seriesEntities.get(i).id = i + "";
        }
        return seriesEntities;
    }


}
