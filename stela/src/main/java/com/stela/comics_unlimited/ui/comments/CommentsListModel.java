package com.stela.comics_unlimited.ui.comments;


import com.lxy.baselibs.mvp.BaseModel;
import com.lxy.baselibs.net.BaseHttpResult;
import com.stela.comics_unlimited.data.entity.CommentsEntity;
import com.stela.comics_unlimited.data.entity.PageEntity;
import com.stela.comics_unlimited.data.repository.RetrofitUtils;

import io.reactivex.Observable;

/**
 * @author xuhao
 * @date 2018/6/13 13:58
 * @desc 对数据层进行操作（比如，是否需要缓存，调用不同 apiservce，外界无需知道内部怎么实现）
 */
public class CommentsListModel extends BaseModel implements CommentsListContract.Model {

    @Override
    public Observable<BaseHttpResult<PageEntity<CommentsEntity>>> getSeriesCommentList(String userid, String seriesId, int mPageNo,int mPageSize) {
        return RetrofitUtils.getHttpService().findUserComment(userid, seriesId, mPageNo,mPageSize);
    }

    @Override
    public Observable<BaseHttpResult<PageEntity<CommentsEntity>>> getChapterCommentList(String userid, String seriesId, String chaperId, int mPageNo,int mPageSize) {
        return RetrofitUtils.getHttpService().findByChapterComments(userid, seriesId, chaperId, mPageNo,mPageSize);
    }

    @Override
    public Observable<BaseHttpResult<String>> addSeriesCommentsLike(String commentid, String userid, int mPageNo) {
        return RetrofitUtils.getHttpService().addSeriesCommentsLike(commentid, userid, mPageNo);
    }

}
