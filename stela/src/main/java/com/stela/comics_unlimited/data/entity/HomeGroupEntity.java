package com.stela.comics_unlimited.data.entity;

import java.io.Serializable;
import java.util.ArrayList;

public class HomeGroupEntity implements Serializable {
  public static final int cardNo = 0;
  public static final int cardYes = 1;
  private static final long serialVersionUID = -5416293273726397778L;
  public  String id;
  public  String seeMoreLinkTo;
  public String title;
  public String pageTitle;
  public String subTitle;

  public  ArrayList<HomeItemEntity> childList;
  //0 没有阴影 1有阴影
  public int shadowState;

}
