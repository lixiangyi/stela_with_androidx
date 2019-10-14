package com.stela.comics_unlimited.util.deeplink;

import android.content.Intent;
import android.net.Uri;
import android.text.TextUtils;

import androidx.annotation.NonNull;

import com.facebook.applinks.AppLinkData;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.dynamiclinks.FirebaseDynamicLinks;
import com.google.firebase.dynamiclinks.PendingDynamicLinkData;
import com.lxy.baselibs.base.BaseActivity;
import com.lxy.baselibs.utils.LogUtils;
import com.stela.comics_unlimited.app.DataStore;
import com.stela.comics_unlimited.ui.deeplink.DeepLinkActivity;

public class DeepLinkHelper {
    private static AppLinkData mAppLinkData;

    //stela://?type=1&pageId=&seriesId=f6afe7db-8fa4-4ef6-8f42-b6c9bcb04bd4&chapterId=039ad9f8-9c72-4b53-8290-3705cbb173aa
    public static boolean AppLinkDataJump(BaseActivity baseActivity, Intent intent) {

        AppLinkData appLinkData = AppLinkData.createFromAlApplinkData(intent);
        if (appLinkData != null) {
            getUri(baseActivity, appLinkData);
        } else {
            return false;
        }
        return true;
    }

    private static void getUri(BaseActivity baseActivity, AppLinkData appLinkData) {
        if (appLinkData != null) {
            Uri targetUrl = appLinkData.getTargetUri();
            if (targetUrl != null) {
                LogUtils.d("lixiangyi", "getFacebook");
                jumpNew(baseActivity, targetUrl.toString());
            }
        }
    }

    public static void deferredJump(BaseActivity baseActivity) {
        getUri(baseActivity, mAppLinkData);
    }

    public static void setAppLinkData(AppLinkData appLinkData) {
        mAppLinkData = appLinkData;
    }

    public static AppLinkData getAppLinkData() {
        return mAppLinkData;
    }



    private static void jumpNew(BaseActivity baseActivity, String url) {
        String deeplinkId = "";
        if (!TextUtils.isEmpty(url)) {
            String[] urls = url.split("&");
            for (int i = 0; i < urls.length; i++) {
                if (urls[i].contains("deepLinkId=")) {
                    deeplinkId = urls[i].substring(urls[i].indexOf("=") + 1);
                    break;
                }
            }
        }
        if (TextUtils.isEmpty(deeplinkId)) {
            deeplinkId = url.substring(url.indexOf("=") + 1);
        }
        if (!TextUtils.isEmpty(deeplinkId)) {
            LogUtils.d("lixiangyi", "deepLink= "+ deeplinkId);
            DataStore.setDeeplinkId(deeplinkId);
            DeepLinkActivity.start(baseActivity, deeplinkId);
            baseActivity.finish();
        }
    }

    public static void getFirebaseDeeplinks(BaseActivity baseActivity, Intent intent) {

        FirebaseDynamicLinks.getInstance()
                .getDynamicLink(intent)
                .addOnSuccessListener(baseActivity, new OnSuccessListener<PendingDynamicLinkData>() {
                    @Override
                    public void onSuccess(PendingDynamicLinkData pendingDynamicLinkData) {
                        // Get deep link from result (may be null if no link is found)
                        Uri deepLink = null;
                        if (pendingDynamicLinkData != null) {
                            deepLink = pendingDynamicLinkData.getLink();
                            LogUtils.d("lixiangyi", "getFirebaseDeeplinks:"+deepLink);
                            jumpNew(baseActivity, deepLink.toString());
                        }
                        // Handle the deep link. For example, open the linked
                        // content, or apply promotional credit to the user's
                        // account.
                        // ...

                        // ...
                    }
                })
                .addOnFailureListener(baseActivity, new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        LogUtils.w("lixiangyi", "getDynamicLink:onFailure", e);
                    }
                });
    }


}
//    Uri targetUrl1 = AppLinks.getTargetUrlFromInboundIntent(this, intent);
//        if (targetUrl != null) {
//            LogUtils.e("Splash---", "App Link Target URL: " + targetUrl.toString());
//                ToastUtils.showShort(targetUrl.toString());
//            if(targetUrl.toString() == "stela://test.com/test"){
//                //做你记录的操作，这里添加判断是为了区分在多个deeplink广告点击设置的时候，进行区分是哪个deeplink跳转过来的
//            }
//        }
//private static void jump(BaseActivity baseActivity, String url) {
//    String[] strs = url.split("&");
//    String type = "", pageId = "", seriesId = "", chapterId = "";
//    if (strs.length > 0) {
//        type = strs[0].substring(strs[0].length() - 1);
//    }
//    if (strs.length > 1) {
//        pageId = strs[1].substring(strs[1].indexOf("=") + 1);
//    }
//    if (strs.length > 2) {
//        seriesId = strs[2].substring(strs[2].indexOf("=") + 1);
//    }
//    if (strs.length > 3) {
//        chapterId = strs[3].substring(strs[3].indexOf("=") + 1);
//    }
//    switch (type) {
//        case "0":
//            //page
//            HomeInnerActivity.start(baseActivity, pageId, "");
//            break;
//        case "1":
//            //series
//            SeriesActivity.start(baseActivity, seriesId);
//            break;
//        case "2":
//            //chapterId
////                        DeepLinkActivity.start();
//            break;
//    }
//
//}