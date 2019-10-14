package com.stela.comics_unlimited.ui.mylibrary;


import com.lxy.baselibs.mvp.BasePresenter;

/**
 * @author xuhao
 * @date 2018/6/12 22:57
 * @desc
 */
public class MyLibraryPresenter extends BasePresenter<MyLibraryContract.Model, MyLibraryContract.View> {
    @Override
    protected MyLibraryContract.Model createModel() {
        return new MyLibraryModel();
    }

}
