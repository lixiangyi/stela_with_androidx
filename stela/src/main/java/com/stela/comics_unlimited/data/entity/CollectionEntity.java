package com.stela.comics_unlimited.data.entity;

import java.io.Serializable;
import java.util.ArrayList;

public class CollectionEntity implements Serializable {
    private static final long serialVersionUID = 8296421117860816324L;

    public static final int HEADER = 1;
    public static final int NORMAL = 2;
    public static final int FOOTER = 3;
    public  int type = 2;
    public String id;
    public String subTitle ;
    public String seriesType ;
    public String description ;
    public String title ;
    public ArrayList<SeriesEntity> seriesList;
    public ArrayList<SeriesEntity> list;
    public boolean isLastPage;

}
