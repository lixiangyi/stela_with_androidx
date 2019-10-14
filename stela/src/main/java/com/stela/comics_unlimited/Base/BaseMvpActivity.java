package com.stela.comics_unlimited.Base;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.Nullable;

import com.lxy.baselibs.base.BaseActivity;
import com.lxy.baselibs.mvp.BasePresenter;
import com.lxy.baselibs.mvp.IView;
import com.stela.comics_unlimited.app.DataStore;

/**
 * @author xuhao
 * @date 2018/6/9 17:12
 * @desc 基类 BaseMvpActivity
 */
public abstract class BaseMvpActivity<T extends BasePresenter> extends BaseActivity implements IView {
    protected T mPresenter;
    private AlertDialog alertDialog;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void initP() {
        mPresenter = createPresenter();
        if (mPresenter != null) {
            mPresenter.attachView(this);
        }
    }

    @Override
    public void showLoading() {
        showLoadingDialog();
    }

    @Override
    public void hideLoading() {
        hideLoadingDialog();
    }


    @Override
    public void loginOut(String msg) {
         alertDialog = new AlertDialog.Builder(this)
                .setTitle("Message")
                .setMessage(msg + "Please Login")
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        DataStore.logout(BaseMvpActivity.this);
                    }
                })
                .show();

    }

    protected abstract T createPresenter();

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mPresenter != null) {
            mPresenter.detachView();
        }
        if (alertDialog!=null) {
            alertDialog.dismiss();
        }
    }

}
