package com.stela.comics_unlimited.data.entity;

import java.io.Serializable;
import java.util.ArrayList;

public class HomeLibraryEntity implements Serializable {
    private static final long serialVersionUID = -2067994892403432474L;
    public String id;
    public String title;
    public String subtitle;
    public ArrayList<HomeBrowseEntity> contentList;
}
