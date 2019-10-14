package com.stela.comics_unlimited.data.entity;

import java.io.Serializable;

public class DialyInfo implements Serializable {
    private static final long serialVersionUID = -3194702752279269062L;
    // 使用了加密存储、串码字段值不允许更改
    public String userId;
    public long saveTime;
}
