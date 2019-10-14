package com.stela.comics_unlimited.data.entity;

import java.io.Serializable;
import java.util.ArrayList;

public class ChapterEntity implements Serializable {
    public static final int CHAPTERSTATE_LOCK = 0;
    public static final int CHAPTERSTATE_FREE = 1;
    public static final int CHAPTERSTATE_DATA = 2;
    public static final int CHAPTERSTATE_PAYED = 4;
    public static final int CHAPTERSTATE_READED = 1;
    public static final int CHAPTERSTATE_UNREAD = 0;
    public static final int LASTREADSTATE_NO = 0;
    public static final int LASTREADSTATE_YES = 1;
    private static final long serialVersionUID = 6003173860860493470L;
    public String id;
    public String seriesId;
    public String seriesTitle;
    public String title;
    public String url;
    public ArrayList<SeriesAsset> chapterImgList;
    public int chapterState;//0 lock  1.free  2 data 4 pay for it
    public int browseState;//0   1 最后读
    public int ifRead;//0 未读  1已读
    public String chapterRowsSort;//章节所在的行数
    public int flagLikeChapter;//0 不喜欢  1.last
    public int flagComments;//0 未评论  1.已评论
    public int screenshotsCount ;//当前截屏次数
    public String displayStartAt ;//开始时间
    public String expectedDate  ;//开始时间

}
