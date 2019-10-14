package com.stela.comics_unlimited.ui.mylibrary.innerLibrary;


import com.lxy.baselibs.mvp.BasePresenter;
import com.lxy.baselibs.net.BaseHttpResult;
import com.lxy.baselibs.net.BaseObserver;
import com.lxy.baselibs.rx.RxSchedulers;
import com.stela.comics_unlimited.app.DataStore;
import com.stela.comics_unlimited.data.entity.PageEntity;
import com.stela.comics_unlimited.data.entity.SeriesEntity;

/**
 * @author xuhao
 * @date 2018/6/12 22:57
 * @desc
 */
public class RecentReadPresenter extends BasePresenter<RecentReadContract.Model, RecentReadContract.View> {
    @Override
    protected RecentReadContract.Model createModel() {
        return new RecentReadModel();
    }


    public void requestData(int pageNo) {
        getModel().findByUserBrowseList(DataStore.getUserInfo().id, pageNo)
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

    public void delete(String seriesId, int position) {
        getModel().deleteBrowseList(seriesId, DataStore.getUserInfo().id)
                .compose(RxSchedulers.applySchedulers(getLifecycleProvider()))
                .subscribe(new BaseObserver<String>(getView(), false) {
                    @Override
                    public void onSuccess(BaseHttpResult<String> result) {
                        if (result != null) {
                            getView().deleteSuccess(result.getData(), position);
                        }
                    }

                    @Override
                    public void onFailure(String errMsg, int code) {
                        getView().showError(errMsg);
                    }
                });
    }

}
