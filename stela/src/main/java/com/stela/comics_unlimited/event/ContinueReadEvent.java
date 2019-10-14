package com.stela.comics_unlimited.event;

/**
 * @author lixiangyi
 * @date 2019/3/13 16:26
 * @desc
 */
public class ContinueReadEvent {
    public int mChapterPosition;
    public int mImgposition;

    public ContinueReadEvent(int chapterPosition,int imgPosition) {
        this.mChapterPosition = chapterPosition;
        this.mImgposition = imgPosition;
    }

}
