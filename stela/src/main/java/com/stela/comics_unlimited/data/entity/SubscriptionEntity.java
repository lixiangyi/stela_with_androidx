package com.stela.comics_unlimited.data.entity;

import java.io.Serializable;
import java.util.ArrayList;

public class SubscriptionEntity implements Serializable {

    private static final long serialVersionUID = 8217630145387656947L;
    public String id;
    public String activeState;
    public String backgroundColor;
    public int bestDeal;
    public String countryCode;
    public String notes;//备注
    public String packageName;//包名
    public String state;//数据状态（1view2android3apple）
    public ArrayList<SubscriptionEntity> subscriptionList;
    public String textColor;
    public String footerImage;//尾图片地址名称 ,
    public String footerImageUrl;//尾照片路径
    public String headerImage;//头图片地址名称
    public String headerImageUrl;//头照片路径  ,
    public String picAssets;//资产图片名称
    public String picAssetsUrl;//资源照片路径
    public String productIdentifier;//产品ID
    public String productName;//产品名称
    public String price;//价格
    public boolean isSelect;
    public String expiresDateStr;

}
