package com.lxy.baselibs.utils.displayCut;


import android.content.Context;
import android.os.Build;
import android.util.Log;

/**
 * Created by Administrator on 2018/5/21 0021.
 */

public class DisplayCutoutUtils {
    public static final String HUAWEI = "HUAWEI";
    public static final String OPPO = "OPPO";
    public static final String VIVO = "vivo";
    public static final String XIAOMI = "Xiaomi";
    public static final String GOOGLE = "Google";
    private static final DisplayCutoutImpl IMPL;


    static {
//        Log.i("lixiangyi", "版本:" + Build.MANUFACTURER);
//        Log.i("lixiangyi", "SDK_INT:" + Build.VERSION.SDK_INT);
        switch (Build.MANUFACTURER) {
//            case HUAWEI:
//                IMPL = new HuaweiDiscut();
//                break;
            case OPPO:
                IMPL = new OppoDisout();
                break;
            case VIVO:
                IMPL = new VivoDiscut();
                break;
//            case XIAOMI:
//                IMPL = new XiaomiDiscut();
//                break;
            default:
                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.P) {
                    IMPL = new GoogleDiscut();
                } else {
                    IMPL = new VivoDiscut();
                }
                break;
        }
    }

    public String PhoneBrand() {
        return Build.MANUFACTURER;
    }

    public  static int doDisplayCutout(Context context) {
        return IMPL.initDisplayCutout(context);
    }


}
