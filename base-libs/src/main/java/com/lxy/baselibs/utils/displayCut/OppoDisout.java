package com.lxy.baselibs.utils.displayCut;

import android.content.Context;
import android.util.Log;

/**
 * Created by LXY on 2018/5/21 0021.
 */

public class OppoDisout implements DisplayCutoutImpl {

    private static final int Hight = 80;//固定80px


    @Override
    public boolean hasDisplayCutout( Context context) {
        return  context.getPackageManager().hasSystemFeature("com.oppo.feature.screen.heteromorphism");
    }

    @Override
    public int initDisplayCutout( Context context) {
        Log.i("lixiangyi", "initDisplayCutout: "+"oppo");
        if (hasDisplayCutout(context)) {
           return Hight;
        }else {
            return 0;
        }
    }

    //获取状态栏高度
    public int getStatusBarHeight(Context context) {
        int result = 0;
        int resourceId = context.getResources().getIdentifier("status_bar_height","dimen", "android");
        if (resourceId > 0) {
            result = context.getResources().getDimensionPixelSize(resourceId);
        }
        Log.d("lixiangyi","**getStatusBarHeight**" + result);
        return result;
    }
}
