package com.stela.comics_unlimited.ui.welcome;

import com.lxy.baselibs.mvp.BasePresenter;

import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;


/**
 * @author xuhao
 * @date 2018/6/12 16:18
 * @desc
 */
public class SplashPresenter extends BasePresenter<SplashContract.Model, SplashContract.View> {

    @Override
    protected SplashContract.Model createModel() {
        return new SplashModel();
    }

    public void jump2Login() {
        long splashTime = 3000;

        addDispose(Observable.timer(splashTime, TimeUnit.MILLISECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(aLong -> getView().jump2Login()));
    }


}
