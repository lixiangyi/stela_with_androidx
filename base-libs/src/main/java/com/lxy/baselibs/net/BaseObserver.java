package com.lxy.baselibs.net;


import android.accounts.NetworkErrorException;

import com.common.baselibs.R;
import com.lxy.baselibs.mvp.IView;
import com.lxy.baselibs.net.exception.ServerException;
import com.lxy.baselibs.utils.StringUtils;

import java.net.ConnectException;
import java.net.UnknownHostException;
import java.util.concurrent.TimeoutException;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import retrofit2.HttpException;

/**
 * @author xuhao
 * @date 2018/6/12 10:51
 * @desc Observer基类
 */
public abstract class BaseObserver<T> implements Observer<BaseHttpResult<T>> {

    private IView mView;

    private boolean isShowDialog = true;

    public BaseObserver() {
    }

    public BaseObserver(IView mView) {
        this.mView = mView;
    }

    public BaseObserver(IView mView, boolean isShowDialog) {
        this.mView = mView;
        this.isShowDialog = isShowDialog;
    }


    @Override
    public void onSubscribe(Disposable d) {
        showLoadingDialog();
    }

    @Override
    public void onNext(BaseHttpResult<T> result) {
        hideLoadingDialog();
        //公共代码逻辑code 处理
        switch (result.getStatus()) {
            case BaseHttpResult.SUCCESS_CODE:
                if (null != result.getData()) {
                    onSuccess(result);
                } else {
                    onFailure(result.getMessage(), result.getStatus());
                }
                break;
            case BaseHttpResult.OUT_DATA_CODE:
                if (null != mView) {
                    mView.loginOut(result.getMessage());
                }else {
                    onFailure(result.getMessage(), result.getStatus());
                }
                break;
            default:
                // API异常处理
                onFailure(result.getMessage(), result.getStatus());
                break;
        }
    }

    @Override
    public void onError(Throwable e) {
        hideLoadingDialog();
        if (e instanceof ConnectException
                || e instanceof TimeoutException
                || e instanceof NetworkErrorException
                || e instanceof UnknownHostException
                || e instanceof HttpException
                || e instanceof retrofit2.adapter.rxjava2.HttpException) {
//            onFailure(ServerException.handleException(e).getMessage(), true);
            onFailure(StringUtils.getString(R.string.net_con_error), 0);
        } else {
            onFailure(ServerException.handleException(e).getMessage(), 0);
        }
    }

    /**
     * Notifies the Observer that the {@link Observable} has finished sending push-based notifications.
     * <p>
     * The {@link Observable} will not call this method if it calls {@link #onError}.
     * * 请求成功了才会调用 onComplete
     * onError 时不会调用
     */
    @Override
    public void onComplete() {
        hideLoadingDialog();
    }


    /**
     * 请求成功返回
     *
     * @param result 服务器返回数据
     */
    public abstract void onSuccess(BaseHttpResult<T> result);

    /**
     * 请求失败返回
     *
     * @param errMsg 失败信息
     * @param code   code
     */
    public abstract void onFailure(String errMsg, int code);


    /**
     * 显示 LoadingDialog
     */
    private void showLoadingDialog() {
        if (isShowDialog && mView != null) {
            mView.showLoading();
        }
    }

    /**
     * 隐藏 Loading
     */
    private void hideLoadingDialog() {
        if (isShowDialog && mView != null) {
            mView.hideLoading();
        }
    }

}
