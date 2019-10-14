package com.lxy.baselibs.utils.displayCut;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import androidx.annotation.RequiresApi;
import android.util.Log;
import android.view.DisplayCutout;
import android.view.View;
import android.view.WindowInsets;
import android.view.WindowManager;

import com.lxy.baselibs.base.BaseActivity;

/**
 * Created by Administrator on 2018/5/21 0021.
 */
@RequiresApi(28)
public class GoogleDiscut implements DisplayCutoutImpl {
    private BaseActivity mActivity;
    private boolean hasCutout;
    private int height;
    public static final String TAG = "lixiangyi";

    @TargetApi(Build.VERSION_CODES.P)
    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public boolean hasDisplayCutout(Context context) {
        mActivity = ((BaseActivity) context);
        View decorView = mActivity.getWindow().getDecorView();
        if (decorView != null) {
            WindowInsets windowInsets = decorView.getRootWindowInsets();
            if (windowInsets != null) {
                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.P) {
                    DisplayCutout displayCutout = windowInsets.getDisplayCutout();
                    if (displayCutout == null) {
                        hasCutout = false;
                    } else {
                        hasCutout = true;
                        height = displayCutout.getSafeInsetTop();
                    }
                }
            }
        }
        return hasCutout;
    }


    //
    @Override
    public int initDisplayCutout(Context context) {
        mActivity = ((BaseActivity) context);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            hasDisplayCutout(context);
        }
        if (hasCutout) {
            if (height > 0) {
                return height;
            }
            return getStatusBarHeight(context);
        }
        return 0;
    }

    //获取状态栏高度
    public static int getStatusBarHeight(Context context) {
        int result = 0;
        int resourceId = context.getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            result = context.getResources().getDimensionPixelSize(resourceId);
        }
        Log.d("lixiangyi", "**getStatusBarHeight**" + result);
        return result;
    }

    //开启使用危险区域
    public static void setNotchWindow(Context context) {
        WindowManager.LayoutParams lp = ((BaseActivity) context).getWindow().getAttributes();
        lp.layoutInDisplayCutoutMode = WindowManager.LayoutParams.LAYOUT_IN_DISPLAY_CUTOUT_MODE_SHORT_EDGES;
        ((BaseActivity) context).getWindow().setAttributes(lp);
    }
}
