package com.stela.comics_unlimited.ui.comments;

import android.text.TextUtils;

import com.lxy.baselibs.mvp.BasePresenter;
import com.lxy.baselibs.net.BaseHttpResult;
import com.lxy.baselibs.net.BaseObserver;
import com.lxy.baselibs.rx.RxSchedulers;
import com.stela.comics_unlimited.app.DataStore;


/**
 * @author xuhao
 * @date 2018/6/12 16:18
 * @desc
 */
public class SubmitCommentPresenter extends BasePresenter<SubmitCommentContract.Model, SubmitCommentContract.View> {

    @Override
    protected SubmitCommentContract.Model createModel() {
        return new SubmitCommentModel();
    }

    public void submitSeriesComment(String comment, String seriesid,String chapterId) {
        if (TextUtils.isEmpty(chapterId)) {
            getModel().submitSeriesComment(comment, seriesid, DataStore.getUserInfo().id)
                    .compose(RxSchedulers.applySchedulers(getLifecycleProvider()))
                    .subscribe(new BaseObserver<String>(getView()) {
                        @Override
                        public void onSuccess(BaseHttpResult<String> result) {
                            if (result != null) {
                                getView().submitCommentSuccess(result.getData());
                            }
                        }

                        @Override
                        public void onFailure(String errMsg,int code) {
                            getView().showError(errMsg);
                        }
                    });
        }else {
            getModel().submitChapterComment(comment, seriesid, DataStore.getUserInfo().id,chapterId)
                    .compose(RxSchedulers.applySchedulers(getLifecycleProvider()))
                    .subscribe(new BaseObserver<String>(getView()) {
                        @Override
                        public void onSuccess(BaseHttpResult<String> result) {
                            if (result != null) {
                                getView().submitCommentSuccess(result.getData());
                            }
                        }

                        @Override
                        public void onFailure(String errMsg,int code) {
                            getView().showError(errMsg);
                        }
                    });
        }
    }

    public void submitReplyComment(String comment, String commentid, String seriesId,String chapterId) {
        if (TextUtils.isEmpty(chapterId)) {
            getModel().submitCommentReply(comment, commentid, DataStore.getUserInfo().id, seriesId)
                    .compose(RxSchedulers.applySchedulers(getLifecycleProvider()))
                    .subscribe(new BaseObserver<String>(getView()) {
                        @Override
                        public void onSuccess(BaseHttpResult<String> result) {
                            if (result != null) {
                                getView().submitCommentSuccess(result.getData());
                            }
                        }

                        @Override
                        public void onFailure(String errMsg, int code) {
                            getView().showError(errMsg);
                        }
                    });
        }else {
            getModel().submitChapterCommentReply(comment, commentid, DataStore.getUserInfo().id, seriesId,chapterId)
                    .compose(RxSchedulers.applySchedulers(getLifecycleProvider()))
                    .subscribe(new BaseObserver<String>(getView()) {
                        @Override
                        public void onSuccess(BaseHttpResult<String> result) {
                            if (result != null) {
                                getView().submitCommentSuccess(result.getData());
                            }
                        }

                        @Override
                        public void onFailure(String errMsg, int code) {
                            getView().showError(errMsg);
                        }
                    });
        }
    }


}
