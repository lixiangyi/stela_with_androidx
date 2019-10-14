package com.stela.comics_unlimited.util;

import android.content.Intent;
import android.net.Uri;
import android.os.Build;

import com.lxy.baselibs.base.BaseActivity;
import com.lxy.baselibs.utils.AppUtils;
import com.stela.comics_unlimited.app.DataStore;

public class SupportHelper {
    public static void goEmail(BaseActivity baseActivity) {
        try {
            Uri uri = Uri.parse("mailto:support@stela.com");
            Intent intent = new Intent(Intent.ACTION_SENDTO, uri);
            // 主题
            intent.putExtra(Intent.EXTRA_SUBJECT, "[Contact Support - " + AppUtils.getVersionName(baseActivity) + "]");
            //messageBody
            intent.putExtra(Intent.EXTRA_TEXT, "MyApp Version:" + AppUtils.getVersionName(baseActivity) + "\n"
                    + "OS: Android " + Build.VERSION.RELEASE + "\n"
                    + "Email:Optional('" + DataStore.getUserInfo().email + "')" + "\n"
                    + "Question or Problem:" + "\n\n\n" + "Sent from " + AppUtils.getDeviceBrand() + " " + AppUtils.getSystemModel()
            );
            baseActivity.startActivity(intent);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
