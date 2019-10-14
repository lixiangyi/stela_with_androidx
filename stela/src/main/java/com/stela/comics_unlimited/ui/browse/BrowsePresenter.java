package com.stela.comics_unlimited.ui.browse;


import com.lxy.baselibs.mvp.BasePresenter;
import com.lxy.baselibs.net.BaseHttpResult;
import com.lxy.baselibs.net.BaseObserver;
import com.lxy.baselibs.rx.RxSchedulers;
import com.stela.comics_unlimited.app.DataStore;
import com.stela.comics_unlimited.data.entity.ImgEntity;

import java.util.List;

/**
 * @author xuhao
 * @date 2018/6/12 22:57
 * @desc
 */
public class BrowsePresenter extends BasePresenter<BrowseContract.Model, BrowseContract.View> {
    @Override
    protected BrowseContract.Model createModel() {
        return new BrowseModel();
    }


    public void requestData(boolean isShow){
        getModel().getBrowseTabList(DataStore.getUserInfo().id)
                .compose(RxSchedulers.applySchedulers(getLifecycleProvider()))
                .subscribe(new BaseObserver<List<ImgEntity>>(getView(),isShow) {
                    @Override
                    public void onSuccess(BaseHttpResult<List<ImgEntity>> result) {
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
}
