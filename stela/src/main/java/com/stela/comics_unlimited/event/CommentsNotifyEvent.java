package com.stela.comics_unlimited.event;

/**
 * @author lixiangyi
 * @date 2019/3/13 16:26
 * @desc
 */
public class CommentsNotifyEvent {
    public String mChapterId;
    public int mType;//0 正常的，1是reply

    public CommentsNotifyEvent(int type) {
        mType = type;
    }
    public CommentsNotifyEvent(int type,String chapterId) {
        mType = type;
        mChapterId = chapterId;
    }

}
