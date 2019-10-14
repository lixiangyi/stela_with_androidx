package com.stela.comics_unlimited.ui.innerSeries;


import com.lxy.baselibs.mvp.BasePresenter;

/**
 * @author xuhao
 * @date 2018/6/12 22:57
 * @desc
 */
public class ChapterListPresenter extends BasePresenter<ChapterListContract.Model, ChapterListContract.View> {
    @Override
    protected ChapterListContract.Model createModel() {
        return new ChapterListModel();
    }


}
