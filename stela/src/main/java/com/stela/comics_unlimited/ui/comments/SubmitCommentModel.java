package com.stela.comics_unlimited.ui.comments;


import com.lxy.baselibs.mvp.BaseModel;
import com.lxy.baselibs.net.BaseHttpResult;
import com.stela.comics_unlimited.data.repository.RetrofitUtils;

import io.reactivex.Observable;

/**
 * @author xuhao
 * @date 2018/6/13 13:58
 * @desc 对数据层进行操作（比如，是否需要缓存，调用不同 apiservce，外界无需知道内部怎么实现）
 */
public class SubmitCommentModel extends BaseModel implements SubmitCommentContract.Model {



    @Override
    public Observable<BaseHttpResult<String>> submitSeriesComment(String content,String seriesId,String userId) {
        return RetrofitUtils.getHttpService().addComment(content,seriesId,userId);
    }

    @Override
    public Observable<BaseHttpResult<String>> submitChapterComment(String content, String seriesId, String userId, String chapterId) {
        return RetrofitUtils.getHttpService().addUserChapterComments(content,seriesId,userId,chapterId);
    }

    @Override
    public Observable<BaseHttpResult<String>> submitCommentReply(String content,String commentableId,String userId,String seriesId) {
        return RetrofitUtils.getHttpService().addCommentReply(content,commentableId,userId,seriesId);
    }

    @Override
    public Observable<BaseHttpResult<String>> submitChapterCommentReply(String content, String commentableId, String userId, String seriesId,String chapterid) {
        return RetrofitUtils.getHttpService().addUserChapterCommentsReply(content,commentableId,userId,seriesId,chapterid);
    }
}
