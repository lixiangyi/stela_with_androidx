package com.lxy.baselibs.utils.displayCut;

import android.content.Context;
import android.util.Log;

import java.lang.reflect.Method;

/**
 * Created by LXY on 2018/5/21 0021.
 */

public class VivoDiscut implements DisplayCutoutImpl {
    public static final int NOTCH_IN_SCREEN_VOIO = 0x00000020;//是否有凹槽
    public static final int ROUNDED_IN_SCREEN_VOIO = 0x00000008;//是否有圆角

    @Override
    public boolean hasDisplayCutout(Context context) {
        boolean ret = false;
        try {
            ClassLoader cl = context.getClassLoader();
            Class FtFeature = cl.loadClass("android.util.FtFeature");
            Method get = FtFeature.getMethod("isFeatureSupport", int.class);
            ret = (boolean) get.invoke(FtFeature, NOTCH_IN_SCREEN_VOIO);
        } catch (ClassNotFoundException e) {
            Log.e("lixiangyi", "hasNotchInScreen ClassNotFoundException");
        } catch (NoSuchMethodException e) {
            Log.e("lixiangyi", "hasNotchInScreen NoSuchMethodException");
        } catch (Exception e) {
            Log.e("lixiangyi", "hasNotchInScreen Exception");
        } finally {
            return ret;
        }
    }


    @Override
    public int initDisplayCutout( Context context) {

        if (hasDisplayCutout(context)) {
            return 0;
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
