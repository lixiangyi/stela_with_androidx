package com.lxy.baselibs.glide;

import com.bumptech.glide.load.model.GlideUrl;

public class MyGlideUrl extends GlideUrl {

    private String mUrl;

    public MyGlideUrl(String url) {
        super(url);
        mUrl = url;
    }

    @Override
    public String getCacheKey() {
        return findTokenParam();
    }

    private String findTokenParam() {
        String tokenParam = mUrl;
        int tokenKeyIndex = mUrl.indexOf("?");
        if (tokenKeyIndex != -1) {
            tokenParam = mUrl.substring(0, tokenKeyIndex);
        }
        return tokenParam;
    }

}

