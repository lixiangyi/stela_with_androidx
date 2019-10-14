package com.stela.comics_unlimited.event;

/**
 * @author lixiangyi
 * @date 2019/3/13 16:26
 * @desc
 */
public class ReadRecordEvent {
    public int mChapterPosition;

    public ReadRecordEvent(int chapterPosition) {
        this.mChapterPosition = chapterPosition;
    }

}
