package com.stela.comics_unlimited.util.banner;

import android.content.Context;

import com.lxy.baselibs.glide.GlideUtils;
import com.stela.comics_unlimited.R;
import com.stela.comics_unlimited.data.entity.SeriesAsset;
import com.stela.comics_unlimited.widget.HomeImageView;
import com.youth.banner.loader.ImageLoaderInterface;

import java.util.ArrayList;

public class BannerImageLoaderHome implements ImageLoaderInterface<HomeImageView> {
    @Override
    public void displayImage(Context context, Object path, HomeImageView homeImageView) {
        /**
         注意：
         1.图片加载器由自己选择，这里不限制，只是提供几种使用方法
         2.返回的图片路径为Object类型，由于不能确定你到底使用的那种图片加载器，
         传输的到的是什么格式，那么这种就使用Object接收和返回，你只需要强转成你传输的类型就行，
         切记不要胡乱强转！
         */

        //Glide 加载图片简单用法
        if (((ArrayList<SeriesAsset>) path).size() > 0 && ((ArrayList<SeriesAsset>) path).get(0) != null) {
            homeImageView.setScale( ((ArrayList<SeriesAsset>) path).get(0).scale);
            GlideUtils.loadImageHttps(context, homeImageView.getImageView(), ((ArrayList<SeriesAsset>) path).get(0).url, R.color.stela_blue);
            homeImageView.setLock(((ArrayList<SeriesAsset>) path).get(0).isLock == 1, true);
        }
//        homeImageView.setScale(0.71f);
    }

    //提供createImageView 方法，如果不用可以不重写这个方法，主要是方便自定义ImageView的创建
    @Override
    public HomeImageView createImageView(Context context) {
        //使用fresco，需要创建它提供的ImageView，当然你也可以用自己自定义的具有图片加载功能的ImageView
        HomeImageView homeImageView = new HomeImageView(context);
        return homeImageView;
    }
}
