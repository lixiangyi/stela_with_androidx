package com.stela.comics_unlimited.data.entity;

import java.io.Serializable;

public class NotificationEntity implements Serializable {

    public static final String PLACEMENTVALUE_PAGE = "0";
    public static final String PLACEMENTVALUE_SERIES = "1";
    public static final String PLACEMENTVALUE_CHAPTER = "2";
    public static final String PLACEMENTVALUE_WEB = "3";
    public static final String PLACEMENTVALUE_NO = "4";
    public String id;
    public String userNotificationId ;
    public String url;
    public String createdAtStr;
    public String messageTitle;
    public String messageBody;
    public String description;
    public int locationValue;//:0系统消息、App内部消息都展示1App内部消息2系统通知消息
    public String placementValue;//0跳see more page 1跳series page 2跳chapter page 3跳web page  4 跳 notification
    public String destinationName;//app 显示名字
    public String destinationValue;//app跳转路径
    public String seriesValue;// 目前只代表seriesId  跳转chapter 使用
    public int state;// 状态 0未读,1已读 ,
    public int notificationCount;

}
