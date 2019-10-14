package com.stela.comics_unlimited.data.entity;


import java.io.Serializable;

/**
 * Created by nb on 4/21/16.
 */
public class SeriesAsset implements Serializable {
    private static final long serialVersionUID = -5238038455529349735L;
    public String url = "";
    public float scale ;
    public String id = "";
    public float scrollFactor = 0;
    public int width = 1200;
    public int height = 1200;
    public int link_id;
    public int fileSize;
    public String contentType;
    public String fileName;
    public int is_select;
    /**
     * 0  有free 不显示锁1 全是lock*/
    public int isLock;
    public String seriesId;
    public String description ;
    public String title ;


    public String page_id;
    public String page_name;
    public String group_id;
    public String group_name;


}
