package com.stela.comics_unlimited.ui.person;

import com.lxy.baselibs.mvp.BasePresenter;
import com.lxy.baselibs.net.BaseHttpResult;
import com.lxy.baselibs.net.BaseObserver;
import com.lxy.baselibs.rx.RxSchedulers;
import com.stela.comics_unlimited.app.DataStore;
import com.stela.comics_unlimited.data.entity.ImgEntity;

import java.util.List;


/**
 * @author xuhao
 * @date 2018/6/12 16:18
 * @desc
 */
public class SelectTitlePresenter extends BasePresenter<SelectTitleContract.Model, SelectTitleContract.View> {

    @Override
    protected SelectTitleContract.Model createModel() {
        return new SelectTitleModel();
    }

    public void selectTitle(String ids) {
        getModel().addUserLikeCartoonSeries(DataStore.getUserInfo().id,ids)
                .compose(RxSchedulers.applySchedulers(getLifecycleProvider()))
                .subscribe(new BaseObserver<String>(getView()) {
                    @Override
                    public void onSuccess(BaseHttpResult<String> result) {
                        if (result != null) {
                            getView().selecSuccess(result.getData());
                        }
                    }

                    @Override
                    public void onFailure(String errMsg, int code) {
                        getView().showError(errMsg);
                    }
                });
    }


    public void getTitleList() {
        getModel().findBySeriesGenresImg(DataStore.getUserInfo().id)
                .compose(RxSchedulers.applySchedulers(getLifecycleProvider()))
                .subscribe(new BaseObserver<List<ImgEntity>>(getView()) {
                    @Override
                    public void onSuccess(BaseHttpResult<List<ImgEntity>> result) {
                        if (result != null) {
                            getView().findBySeriesGenresImg(result.getData());
                        }
                    }

                    @Override
                    public void onFailure(String errMsg, int code) {
                        getView().showError(errMsg);
                    }
                });
    }


}
