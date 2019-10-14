package com.lxy.baselibs.utils.displayCut;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.provider.Settings;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;

import com.lxy.baselibs.base.BaseActivity;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Properties;


/**
 * Created by LXY on 2018/5/21 0021.
 */

public class XiaomiDiscut implements DisplayCutoutImpl {


    @Override
    public boolean hasDisplayCutout(Context context) {
        try {
            Class<?> c = Class.forName("android.os.SystemProperties");
            Method getInt = c.getMethod("getInt", String.class,int.class);
            if (((int)getInt.invoke(c,"ro.miui.notch",0))== 1) {
                return true;
            }
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }

        return false;
    }
    //小米手机是否隐藏刘海
    public boolean hiddenDisplayCutout(Context context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            if (Settings.Global.getInt(context.getContentResolver(), "force_black", 0) == 1) {
                return true;
            }
        }
        return false;
    }

    @Override
    public int initDisplayCutout(Context context) {
        if (hasDisplayCutout(context)) {
            if (hiddenDisplayCutout(context)) {
                return 0;
            }
            return getNotchHeight(context);
        } else {
            return 0;
        }
    }

    //获取小米刘海的高度
    public static int getNotchHeight(Context context) {
        int resourceId = context.getResources().getIdentifier("notch_height", "dimen", "android");
        if (resourceId > 0) {
            int result = context.getResources().getDimensionPixelSize(resourceId);
            return result;
        }else {
            return getStatusBarHeight(context);
        }
    }

    //获取小米刘海的宽度
    public static int getNotchWidth(Context context) {
        int resourceId = context.getResources().getIdentifier("notch_width", "dimen", "android");
        if (resourceId > 0) {
            int result = context.getResources().getDimensionPixelSize(resourceId);
            return result;
        }
        return 0;
    }

    //获取小米状态栏的高度
    public static int getStatusBarHeight(Context context) {
        int result = 0;
        int resourceId = context.getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            result = context.getResources().getDimensionPixelSize(resourceId);
        }
        return result;
    }
    //小米设置windows级别的使用耳朵区
    private static final int START_NOTCH = 0x00000100;//开启配置
    private static final int START_NOTCH_VERTICAL= 0x00000200;// 竖屏配置
    private static final int START_NOTCH_HORIZONTAL = 0x00000400;//横屏配置
    //开启使用危险区域
    public static void setNotchWindow(Context context) {
        int flag = START_NOTCH | START_NOTCH_VERTICAL ;
        try {
            Method method = Window.class.getMethod("addExtraFlags",
                    int.class);
            method.invoke(((BaseActivity)context).getWindow(), flag);
        } catch (Exception e) {
            Log.i("lixiangyi", "addExtraFlags not found.");
        }
    }



}
