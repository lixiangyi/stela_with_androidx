package com.stela.comics_unlimited.data.entity;

import java.io.Serializable;
import java.util.ArrayList;

public class HomePageEntity implements Serializable {
    private static final long serialVersionUID = -7088379287131634328L;
    public String id;
    public String title;
    public ArrayList<HomeGroupEntity> childList;
}
