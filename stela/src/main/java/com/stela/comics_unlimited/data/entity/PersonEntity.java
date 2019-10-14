package com.stela.comics_unlimited.data.entity;

import java.io.Serializable;

public class PersonEntity implements Serializable {
    private static final long serialVersionUID = 2861205689170177397L;
    public String authorization;
    public String id;
    public String nickname;
    /**
     * 头像
     */
    public String imgPortrait;
    public String email;
    /**
     * 0默认头像1已修改的头像
     */
    public int ifPortrait;
    /**
     * 0默认用户名1已修改的用户名 ,
     */
    public int ifNickname;
    /**
     *  fire base 推送 authorization
     * */
    public String notificationToken;
    /**
     *  订阅过期状态(0未订阅,1已订阅) ,
     * */
    public int subscriptionStatus;
    /**
     *  版本号
     * */
    public String version ;
    /**
     *   0没有选择兴趣1已修有了
     *
     * */
    public int ifInterest;
    /**
     *   0 普通权限 1 超级用户权限
     *
     * */
    public int comp;
    /**
     *   0 普通 1 少儿模式
     *
     * */
    public int childrenState;
    /**
     *   0 系统用户 1 Google 用户
     *
     * */
    public int source;

}
