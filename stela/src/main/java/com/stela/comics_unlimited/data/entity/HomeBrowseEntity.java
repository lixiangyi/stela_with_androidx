package com.stela.comics_unlimited.data.entity;

import java.io.Serializable;

public class HomeBrowseEntity implements Serializable {
    private static final long serialVersionUID = 4401821076984223871L;
    public String id;
    public String parent_id;
    public static final int HEADER = 1;
    public static final int NORMAL = 2;
    public static final int FOOTER = 3;
    public String imgurl;
    public String title;
    public String content;
    public int type = NORMAL;
    public boolean more;
    public boolean like;
    public boolean isSelect;
    public String description;
    public String url;
    public String link_id;
}
