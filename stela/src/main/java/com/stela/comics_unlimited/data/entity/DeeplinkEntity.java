package com.stela.comics_unlimited.data.entity;

import java.io.Serializable;
import java.util.ArrayList;

public class DeeplinkEntity implements Serializable {
    private static final long serialVersionUID = 6003173860860493470L;
    public String id;
    public String seriesId;
    public String name;
    public String url;
    public ArrayList<SeriesAsset> deepLinkAssetsList;


}
