package com.stela.comics_unlimited.data.entity;

import java.io.Serializable;
import java.util.ArrayList;

public class HomeItemEntity implements Serializable {
    public static final int a = 0;
    public static final int b = 1;
    public static final int c = 2;
    public static final int d = 3;
    public static final int e = 4;
    public static final int f = 5;
    public static final int g = 6;
    public static final int h = 7;
    public static final int i = 8;
    private static final long serialVersionUID = -6489554229216166936L;
    public int rowType;//card
    public String description;
    public String title;
    public ArrayList<ArrayList<SeriesAsset>> rowAssets;
    public String group_id;
    public String group_name;

}
