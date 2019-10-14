package com.stela.comics_unlimited.ui.chapter;

import com.lxy.baselibs.mvp.BasePresenter;
import com.lxy.baselibs.net.BaseHttpResult;
import com.lxy.baselibs.net.BaseObserver;
import com.lxy.baselibs.rx.RxSchedulers;
import com.stela.comics_unlimited.app.DataStore;
import com.stela.comics_unlimited.data.entity.ChapterEntity;
import com.stela.comics_unlimited.data.entity.ScreenShotrEntity;

public class ChapterPresenter extends BasePresenter<ChapterContract.Model, ChapterContract.View> {

    @Override
    protected ChapterContract.Model createModel() {
        return new ChapterModel();
    }

    public void getChapterList(String chapterId, String seriesId) {
        getModel().getChapterList(chapterId, seriesId, DataStore.getUserInfo().id)
                .compose(RxSchedulers.applySchedulers(getLifecycleProvider()))
                .subscribe(new BaseObserver<ChapterEntity>(getView(),true) {
                    @Override
                    public void onSuccess(BaseHttpResult<ChapterEntity> result) {
                        if (result != null) {
                            getView().showChapter(result.getData());
                        }
                    }

                    @Override
                    public void onFailure(String errMsg, int code) {
                        getView().showError(errMsg);
                    }
                });
    }

    public void setchapterLikes(String chapterId, String seriesId, int flag) {
        getModel().userLikeChapter(chapterId, seriesId, DataStore.getUserInfo().id, flag)
                .compose(RxSchedulers.applySchedulers(getLifecycleProvider()))
                .subscribe(new BaseObserver<String>(getView()) {
                    @Override
                    public void onSuccess(BaseHttpResult<String> result) {
                        if (result != null) {

                            getView().likeSuccess();
                        }
                    }

                    @Override
                    public void onFailure(String errMsg, int code) {
                        getView().showError(errMsg);
                    }
                });
    }

    public void storelastChapter(String chapterId, String seriesId, String imgid) {
        getModel().addChapter(chapterId, seriesId, DataStore.getUserInfo().id, imgid)
                .compose(RxSchedulers.applySchedulers(getLifecycleProvider()))
                .subscribe(new BaseObserver<String>(getView()) {
                    @Override
                    public void onSuccess(BaseHttpResult<String> result) {
                        if (result != null) {
                            getView().storeLastSuccess();
                        }
                    }

                    @Override
                    public void onFailure(String errMsg, int code) {
                        getView().showlastChapterError(errMsg);
                    }
                });

    }
    public void screenshotsCount(String path) {
        getModel().screenshotsCount( DataStore.getUserInfo().id)
                .compose(RxSchedulers.applySchedulers(getLifecycleProvider()))
                .subscribe(new BaseObserver<ScreenShotrEntity>(getView()) {
                    @Override
                    public void onSuccess(BaseHttpResult<ScreenShotrEntity> result) {
                        if (result != null) {
                            getView().screenshots(result.getData(),path );
                        }
                    }

                    @Override
                    public void onFailure(String errMsg, int code) {
                        getView().showlastChapterError(errMsg);
                    }
                });

    }

}
