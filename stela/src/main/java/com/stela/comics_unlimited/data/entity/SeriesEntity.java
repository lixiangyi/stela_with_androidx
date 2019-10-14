package com.stela.comics_unlimited.data.entity;

import java.io.Serializable;
import java.util.ArrayList;

public class SeriesEntity implements Serializable {

    public static final int HEADER = 1;
    public static final int NORMAL = 0;
    public static final int FOOTER = 2;
    public int type = 0;

    public String id;
    public String url;
    public String title;
    public String subTitle;
    public String description;
    public String metaDescription;//作者对内容的描述
    public int flagCollect;//用户是否收藏该作品（1收藏 0未收藏）
    public int flagLike;//用户是否喜欢该作品（1喜欢 0未喜欢） ,
    public int flagComments;//用户是否评论该作品（1是 0未） ,
    public String seriesType;//作品的类型
    public ArrayList<SeriesAsset> assets;
    public ArrayList<ChapterEntity> chapterList;
    public ArrayList<SeriesEntity> youMayAlsLlike;//推荐该系列类似作品
    public ArrayList<CreditorEntity> creditorList;//推荐该系列类似作品

}
