package com.stela.comics_unlimited.ui.login;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;
import com.lxy.baselibs.app.AppConstants;
import com.lxy.baselibs.mvp.BasePresenter;
import com.lxy.baselibs.net.BaseHttpResult;
import com.lxy.baselibs.net.BaseObserver;
import com.lxy.baselibs.rx.RxSchedulers;
import com.lxy.baselibs.utils.AppUtils;
import com.stela.analytics.model.Register;
import com.stela.comics_unlimited.app.MyApp;
import com.stela.comics_unlimited.data.entity.PersonEntity;
import com.stela.comics_unlimited.util.PasswordHelper;
import com.stela.comics_unlimited.util.stela_sdk.StelaAnalyticsUtil;


/**
 * @author xuhao
 * @date 2018/6/12 16:18
 * @desc
 */
public class RegisterPresenter extends BasePresenter<RegisterContract.Model, RegisterContract.View> {
    public static final int GOOGLE_LOGIN_EXIST_CODE = 202;

    @Override
    protected RegisterContract.Model createModel() {
        return new RegisterModel();
    }


    public void register(String email, String password, String versionCode) {
        getModel().register(email, password, versionCode)
                .compose(RxSchedulers.applySchedulers(getLifecycleProvider()))
                .subscribe(new BaseObserver<String>(getView()) {
                    @Override
                    public void onSuccess(BaseHttpResult<String> result) {
                        if (result != null) {
                            getView().register(result.getData());

                        }
                    }

                    @Override
                    public void onFailure(String errMsg, int code) {
                        Register register = new Register();
                        register.setStatus("failure");
                        StelaAnalyticsUtil.register(register, "");
                        getView().showError(errMsg);
                    }
                });
    }

    //todo 相同代码合并到一起
    public void google_login(String email, String nickname) {
        FirebaseInstanceId.getInstance().getInstanceId()
                .addOnCompleteListener(new OnCompleteListener<InstanceIdResult>() {
                    @Override
                    public void onComplete(@NonNull Task<InstanceIdResult> task) {
                        String token = "";
                        if (task.isSuccessful()) {
                            token = task.getResult().getToken();
                        }
                        getModel().google_login(PasswordHelper.setPasswordSecret(email), AppConstants.GOOGLE_LOGIN_TYPE, nickname, AppUtils.getSystemModel(), "android",
                                AppUtils.getSystemVersion(), AppUtils.getVersionName(MyApp.getContext()),
                                AppUtils.getVersionCode(MyApp.getContext()) + "", token, 1 + "")
                                .compose(RxSchedulers.applySchedulers(getLifecycleProvider()))
                                .subscribe(new BaseObserver<PersonEntity>(getView()) {
                                    @Override
                                    public void onSuccess(BaseHttpResult<PersonEntity> result) {
                                        if (result != null) {
                                            getView().google_login(result.getData());
                                        }
                                    }

                                    @Override
                                    public void onFailure(String errMsg, int code) {
                                        if (code == GOOGLE_LOGIN_EXIST_CODE) {
                                            getView().google_exist(errMsg, email);
                                        }
                                        getView().showError(errMsg);
                                    }
                                })
                        ;
                    }
                });
    }


}
