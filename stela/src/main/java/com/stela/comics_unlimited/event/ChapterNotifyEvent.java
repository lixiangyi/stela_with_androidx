package com.stela.comics_unlimited.event;

/**
 * @author lixiangyi
 * @date 2019/5/13 16:26
 * @desc
 */
public class ChapterNotifyEvent {
    public int mChapterPosition;

    public  ChapterNotifyEvent() {
    }

    public  ChapterNotifyEvent(int chapterPosition) {
        mChapterPosition = chapterPosition;
    }


}
