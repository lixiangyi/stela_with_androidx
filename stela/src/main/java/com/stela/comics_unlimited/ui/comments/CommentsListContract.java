package com.stela.comics_unlimited.ui.comments;


import com.lxy.baselibs.mvp.IModel;
import com.lxy.baselibs.mvp.IView;
import com.lxy.baselibs.net.BaseHttpResult;
import com.stela.comics_unlimited.data.entity.CommentsEntity;
import com.stela.comics_unlimited.data.entity.PageEntity;

import io.reactivex.Observable;

/**
 * @author xuhao
 * @date 2018/6/12 16:18
 * @desc 契约类
 */
public interface CommentsListContract {

    //对于经常使用的关于UI的方法可以定义到IView中,如显示隐藏进度条,和显示文字消息
    interface View extends IView {
        void showListData(PageEntity<CommentsEntity> commentsEntities);
        void showFreshListData(PageEntity<CommentsEntity> commentsEntities);
        void likeSuccess(int postion);
    }

    interface Model extends IModel {
        Observable<BaseHttpResult<PageEntity<CommentsEntity>>> getSeriesCommentList(String userid, String seriesId, int mPageNo,int pageSize);
        Observable<BaseHttpResult<PageEntity<CommentsEntity>>> getChapterCommentList(String userid, String seriesId, String chaperId,int mPageNo,int pageSize);

        Observable<BaseHttpResult<String>>  addSeriesCommentsLike(String commentid, String userid,int flag);
    }

}
