package com.stela.comics_unlimited.ui.home.presenter;


import android.text.TextUtils;

import com.lxy.baselibs.mvp.BasePresenter;
import com.lxy.baselibs.net.BaseHttpResult;
import com.lxy.baselibs.net.BaseObserver;
import com.lxy.baselibs.rx.RxSchedulers;
import com.stela.comics_unlimited.app.DataStore;
import com.stela.comics_unlimited.data.entity.HomeGroupEntity;
import com.stela.comics_unlimited.data.entity.HomeItemEntity;
import com.stela.comics_unlimited.data.entity.HomePageEntity;
import com.stela.comics_unlimited.data.entity.SeriesAsset;
import com.stela.comics_unlimited.ui.home.contract.HomeContract;
import com.stela.comics_unlimited.ui.home.model.HomeModel;

/**
 * @author xuhao
 * @date 2018/6/12 22:57
 * @desc
 */
public class HomePresenter extends BasePresenter<HomeContract.Model, HomeContract.View> {
    @Override
    protected HomeContract.Model createModel() {
        return new HomeModel();
    }

    public void HomeData(String id) {
        getModel().pageHome(id, DataStore.getUserInfo().id, TextUtils.isEmpty(DataStore.getDeeplinkId()) ? "" : DataStore.getDeeplinkId())
                .compose(RxSchedulers.applySchedulers(getLifecycleProvider()))
                .subscribe(new BaseObserver<HomePageEntity>(getView(), false) {
                    @Override
                    public void onSuccess(BaseHttpResult<HomePageEntity> result) {
                        if (result != null) {
                            getView().showData(setAwsEntity(result.getData()));
                        }
                    }

                    @Override
                    public void onFailure(String errMsg, int code) {
                        getView().showError(errMsg);
                    }
                })
        ;
    }

    public HomePageEntity setAwsEntity(HomePageEntity homePageEntity) {
        if (homePageEntity.childList != null) {
            for (int i = 0; i < homePageEntity.childList.size(); i++) {
                HomeGroupEntity homeGroupEntity = homePageEntity.childList.get(i);
                if (homeGroupEntity.childList != null) {
                    for (int j = 0; j < homeGroupEntity.childList.size(); j++) {
                        HomeItemEntity homeItemEntity = homeGroupEntity.childList.get(j);
                        if (homeItemEntity.rowAssets != null) {
                            for (int k = 0; k < homeItemEntity.rowAssets.size(); k++) {
                                if (homeItemEntity.rowAssets.get(k) != null) {
                                    for (int n = 0; n < homeItemEntity.rowAssets.get(k).size(); n++) {
                                        SeriesAsset seriesAsset = homeItemEntity.rowAssets.get(k).get(n);
                                        seriesAsset.page_id = homePageEntity.id;
                                        seriesAsset.page_name = homePageEntity.title;
                                        seriesAsset.group_id = homeGroupEntity.id;
                                        seriesAsset.group_name = homeGroupEntity.title;
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
        return homePageEntity;
    }


}
