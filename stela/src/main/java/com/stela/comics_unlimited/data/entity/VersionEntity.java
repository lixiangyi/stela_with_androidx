package com.stela.comics_unlimited.data.entity;


import java.io.Serializable;

/**
 * Created by nb on 4/21/16.
 */
public class VersionEntity implements Serializable {
    public static final int NEW_VERSION = 0;
    public static final int NEW_VERSION_MUST = 1;
    public static final int NO_VERSION = 2;
    private static final long serialVersionUID = -3168246489551064291L;

    public String androidVersion;
    public String id;
    public String note;
    public int state;//是否强制更新(0有新版本不强制更新，1有新版本强制更新，2没有新版本) ,
    public String title;
    public String appVersion;
    public String iosVersion;

}
