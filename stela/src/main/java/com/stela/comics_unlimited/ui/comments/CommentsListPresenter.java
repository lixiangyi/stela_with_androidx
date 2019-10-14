package com.stela.comics_unlimited.ui.comments;

import android.text.TextUtils;

import com.lxy.baselibs.mvp.BasePresenter;
import com.lxy.baselibs.net.BaseHttpResult;
import com.lxy.baselibs.net.BaseObserver;
import com.lxy.baselibs.rx.RxSchedulers;
import com.stela.comics_unlimited.app.DataStore;
import com.stela.comics_unlimited.data.entity.CommentsEntity;
import com.stela.comics_unlimited.data.entity.PageEntity;


/**
 * @author xuhao
 * @date 2018/6/12 16:18
 * @desc
 */
public class CommentsListPresenter extends BasePresenter<CommentsListContract.Model, CommentsListContract.View> {
    public static int PAGESIZE = 10;

    @Override
    protected CommentsListContract.Model createModel() {
        return new CommentsListModel();
    }


    public void getSeriesCommentList(String seriesId, String chaperId, int mPageNo, int pageSize) {
        PAGESIZE = 10;
        if (pageSize > 0) {
            PAGESIZE = pageSize;
        }
        if (TextUtils.isEmpty(chaperId)) {
            getModel().getSeriesCommentList(DataStore.getUserInfo().id, seriesId, mPageNo, PAGESIZE)
                    .compose(RxSchedulers.applySchedulers(getLifecycleProvider()))
                    .subscribe(new BaseObserver<PageEntity<CommentsEntity>>(getView(), false) {
                        @Override
                        public void onSuccess(BaseHttpResult<PageEntity<CommentsEntity>> result) {
                            if (result != null) {
                                if (pageSize>0) {
                                    getView().showFreshListData(result.getData());
                                }else {
                                    getView().showListData(result.getData());
                                }
                            }
                        }

                        @Override
                        public void onFailure(String errMsg, int code) {
                            getView().showError(errMsg);
                        }
                    });
        } else {
            getModel().getChapterCommentList(DataStore.getUserInfo().id, seriesId, chaperId, mPageNo, PAGESIZE)
                    .compose(RxSchedulers.applySchedulers(getLifecycleProvider()))
                    .subscribe(new BaseObserver<PageEntity<CommentsEntity>>(getView(), false) {
                        @Override
                        public void onSuccess(BaseHttpResult<PageEntity<CommentsEntity>> result) {
                            if (result != null) {
                                if (pageSize>0) {
                                    getView().showFreshListData(result.getData());
                                }else {
                                    getView().showListData(result.getData());
                                }
                            }
                        }

                        @Override
                        public void onFailure(String errMsg, int code) {
                            getView().showError(errMsg);
                        }
                    });
        }
    }


    public void dolike(String commentid, final int position, int flag) {
        getModel().addSeriesCommentsLike(commentid, DataStore.getUserInfo().id, flag)
                .compose(RxSchedulers.applySchedulers(getLifecycleProvider()))
                .subscribe(new BaseObserver<String>(getView(), false) {
                    @Override
                    public void onSuccess(BaseHttpResult<String> result) {
                        if (result != null) {
                            getView().likeSuccess(position);
                        }
                    }

                    @Override
                    public void onFailure(String errMsg, int code) {
                        getView().showError(errMsg);
                    }
                });

    }

}
