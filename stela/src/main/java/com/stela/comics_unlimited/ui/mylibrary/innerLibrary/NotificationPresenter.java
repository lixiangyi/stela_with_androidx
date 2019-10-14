package com.stela.comics_unlimited.ui.mylibrary.innerLibrary;


import com.lxy.baselibs.mvp.BasePresenter;
import com.lxy.baselibs.net.BaseHttpResult;
import com.lxy.baselibs.net.BaseObserver;
import com.lxy.baselibs.rx.RxSchedulers;
import com.stela.comics_unlimited.app.DataStore;
import com.stela.comics_unlimited.data.entity.NotificationEntity;
import com.stela.comics_unlimited.data.entity.PageEntity;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * @author LXY
 * @date 2019/5/12 22:57
 * @desc
 */
public class NotificationPresenter extends BasePresenter<NotificationContract.Model, NotificationContract.View> {
    @Override
    protected NotificationContract.Model createModel() {
        return new NotificationModel();
    }


    public void requestData(int pageNo) {
        getModel().getNotificationList(DataStore.getUserInfo().id, pageNo)
                .compose(RxSchedulers.applySchedulers(getLifecycleProvider()))
                .subscribe(new BaseObserver<PageEntity<NotificationEntity>>(getView(), false) {
                    @Override
                    public void onSuccess(BaseHttpResult<PageEntity<NotificationEntity>> result) {
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

    public void delete(String notifacationId, int position) {
        getModel().deleteNotification(notifacationId)
                .compose(RxSchedulers.applySchedulers(getLifecycleProvider()))
                .subscribe(new BaseObserver<Object>(getView(), false) {
                    @Override
                    public void onSuccess(BaseHttpResult<Object> result) {
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

    public void deleteAll() {
        getModel().deleteNotificationAll(DataStore.getUserInfo().id)
                .compose(RxSchedulers.applySchedulers(getLifecycleProvider()))
                .subscribe(new BaseObserver<String>(getView(), false) {
                    @Override
                    public void onSuccess(BaseHttpResult<String> result) {
                        if (result != null) {
                            getView().deleteAllSuccess(result.getData());
                        }
                    }

                    @Override
                    public void onFailure(String errMsg, int code) {
                        getView().showError(errMsg);
                    }
                });
    }

    public void updateNotification(String notificationId, int position) {
        getModel().updateNotificationStateById(notificationId)
//                .compose(RxSchedulers.applySchedulers(getLifecycleProvider()))
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseObserver<Object>(getView(), false) {
                    @Override
                    public void onSuccess(BaseHttpResult<Object> result) {
                        if (result != null) {
                            getView().updateSuccess(result.getData(), position);
                        }
                    }

                    @Override
                    public void onFailure(String errMsg, int code) {
                        getView().showError(errMsg);
                    }
                });
    }

    public void updateNotificationAll() {
        getModel().updateNotificationStateByUserId(DataStore.getUserInfo().id)
                .compose(RxSchedulers.applySchedulers(getLifecycleProvider()))
                .subscribe(new BaseObserver<String>(getView(), false) {
                    @Override
                    public void onSuccess(BaseHttpResult<String> result) {
                        if (result != null) {
                            getView().deleteAllSuccess(result.getData());
                        }
                    }

                    @Override
                    public void onFailure(String errMsg, int code) {
                        getView().showError(errMsg);
                    }
                });
    }
}
