package com.stela.comics_unlimited.ui;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;
import com.lxy.baselibs.base.BaseActivity;
import com.lxy.baselibs.mvp.BasePresenter;
import com.lxy.baselibs.net.BaseHttpResult;
import com.lxy.baselibs.net.BaseObserver;
import com.lxy.baselibs.rx.RxBus;
import com.lxy.baselibs.rx.RxSchedulers;
import com.lxy.baselibs.utils.AppUtils;
import com.stela.comics_unlimited.app.DataStore;
import com.stela.comics_unlimited.data.entity.DialyInfo;
import com.stela.comics_unlimited.data.entity.NotificationEntity;
import com.stela.comics_unlimited.data.entity.PersonEntity;
import com.stela.comics_unlimited.data.entity.VersionEntity;
import com.stela.comics_unlimited.event.MessageNotifyEvent;
import com.stela.comics_unlimited.pay.Purchase;
import com.stela.comics_unlimited.util.SharedPreferencesTool;
import com.stela.comics_unlimited.util.stela_sdk.StelaAnalyticsUtil;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;


/**
 * @author xuhao
 * @date 2018/6/12 16:18
 * @desc
 */
public class MainPresenter extends BasePresenter<MainContract.Model, MainContract.View> {

    @Override
    protected MainContract.Model createModel() {
        return new MainModel();
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

    public void verifyGooglePay(Purchase purchase) {
        getModel().verifyGooglePay(DataStore.getUserInfo().id, purchase.getToken(), purchase.getPackageName(), purchase.getSku())
                .compose(RxSchedulers.applySchedulers(getLifecycleProvider()))
                .subscribe(new BaseObserver<String>(getView(), false) {
                    @Override
                    public void onSuccess(BaseHttpResult<String> result) {
                        if (result != null) {
                            getView().verifyGooglePaySuccess(result.getData(), purchase);
                        }
                    }

                    @Override
                    public void onFailure(String errMsg, int code) {
                        getView().showError(errMsg);
                        com.stela.analytics.model.Purchase p = new com.stela.analytics.model.Purchase();
                        p.setProduct(purchase.getSku());
                        p.setStatus("failure");
                        StelaAnalyticsUtil.purchase(p);
                    }
                })
        ;
    }

    public void userActive(BaseActivity baseActivity) {
        getModel().userActive(DataStore.getUserInfo().id)
                .compose(RxSchedulers.applySchedulers(getLifecycleProvider()))
                .subscribe(new BaseObserver<Object>(getView(), false) {
                    @Override
                    public void onSuccess(BaseHttpResult<Object> result) {
                        DialyInfo dialyInfo = new DialyInfo();
                        dialyInfo.userId = DataStore.getUserInfo().id;
                        dialyInfo.saveTime = System.currentTimeMillis();
                        SharedPreferencesTool.saveObj(baseActivity, SharedPreferencesTool.USERACTIVE_DIALY, dialyInfo);
                    }

                    @Override
                    public void onFailure(String errMsg, int code) {

                    }
                });
    }

    public void loadUnReadMessageNum() {
        getModel().selectUserNotificationByCount(DataStore.getUserInfo().id)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseObserver<NotificationEntity>(getView(), false) {
                    @Override
                    public void onSuccess(BaseHttpResult<NotificationEntity> result) {
                        if (result != null) {
                            RxBus.getDefault().postSticky(new MessageNotifyEvent(result.getData().notificationCount));
//                            getView().notificationCount(result.getData());
                        }
                    }

                    @Override
                    public void onFailure(String errMsg, int code) {

                    }
                });
    }

    public void updateVersion(BaseActivity baseActivity) {
        getModel().updateVersion(AppUtils.getVersionCode(baseActivity) + "")
                .compose(RxSchedulers.applySchedulers(getLifecycleProvider()))
                .subscribe(new BaseObserver<VersionEntity>(getView(), false) {
                    @Override
                    public void onSuccess(BaseHttpResult<VersionEntity> result) {
                        if (result != null) {
                            getView().versionResult(result.getData());
                        }
                    }

                    @Override
                    public void onFailure(String errMsg, int code) {
                    }
                });
    }

    public void updateNotificationToken() {
        FirebaseInstanceId.getInstance().getInstanceId()
                .addOnCompleteListener(new OnCompleteListener<InstanceIdResult>() {
                    @Override
                    public void onComplete(@NonNull Task<InstanceIdResult> task) {
                        String token = "";
                        if (task.isSuccessful()) {
                            token = task.getResult().getToken();
                        }
                        getModel().updateNotificationToken(token, DataStore.getUserInfo().id)
                                .subscribeOn(Schedulers.newThread())
                                .observeOn(AndroidSchedulers.mainThread())
                                .subscribe(new BaseObserver<String>(getView(), false) {
                                    @Override
                                    public void onSuccess(BaseHttpResult<String> result) {
                                        if (result != null) {
                                            getView().updateNotificationSuccess(result.getData());
                                        }
                                    }

                                    @Override
                                    public void onFailure(String errMsg, int code) {

                                    }
                                });
                    }
                });
    }


}
