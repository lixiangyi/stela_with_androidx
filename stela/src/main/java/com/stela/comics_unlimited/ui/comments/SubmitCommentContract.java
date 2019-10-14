package com.stela.comics_unlimited.ui.comments;


import com.lxy.baselibs.mvp.IModel;
import com.lxy.baselibs.mvp.IView;
import com.lxy.baselibs.net.BaseHttpResult;

import io.reactivex.Observable;

/**
 * @author xuhao
 * @date 2018/6/12 16:18
 * @desc 契约类
 */
public interface SubmitCommentContract {

    //对于经常使用的关于UI的方法可以定义到IView中,如显示隐藏进度条,和显示文字消息
    interface View extends IView {
        void submitCommentSuccess(String s);
//        void submitReplySuccess(String s);
    }

    interface Model extends IModel {
        Observable<BaseHttpResult<String>> submitSeriesComment(String content,String seriesId,String userId);
        Observable<BaseHttpResult<String>> submitChapterComment(String content,String seriesId,String userId,String chapterId);
        Observable<BaseHttpResult<String>> submitCommentReply(String content,String commentableId,String userId,String seriesId);
        Observable<BaseHttpResult<String>> submitChapterCommentReply(String content,String commentableId,String userId,String seriesId,String chapterId);

    }
}
