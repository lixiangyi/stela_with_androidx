package com.stela.comics_unlimited.data.entity;

import java.io.Serializable;
import java.util.ArrayList;

public class PageEntity<T> implements Serializable {
    private static final long serialVersionUID = 8296421117860816324L;

    public String id;
    public ArrayList<T> list;
    public boolean isLastPage;

}
