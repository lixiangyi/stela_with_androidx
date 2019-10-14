package com.stela.comics_unlimited.app;

import android.content.Context;
import androidx.multidex.MultiDex;

import com.facebook.FacebookSdk;
import com.facebook.appevents.AppEventsLogger;
import com.facebook.applinks.AppLinkData;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.lxy.baselibs.app.BaseApplication;
import com.lxy.baselibs.utils.AppUtils;
import com.lxy.baselibs.utils.CommonUtils;
import com.stela.comics_unlimited.util.deeplink.DeepLinkHelper;

/**
 * @author xuhao
 * @date 2018/6/10 16:20
 * @desc
 */
public class MyApp extends BaseApplication {

    /**
     * 版本号
     */
    public static String NEED_UPDATE = null;
    public static FirebaseAnalytics mFirebaseAnalytics;


    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
        NEED_UPDATE = AppUtils.getVersionName(this);

    }

    @Override
    public void onCreate() {
        super.onCreate();
        //Bugly
        initBugly();
        //fire base
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);
        //FaceBook
        initFaceBook();
        AppLinkData.fetchDeferredAppLinkData(this, new AppLinkData.CompletionHandler() {
            @Override
            public void onDeferredAppLinkDataFetched(AppLinkData appLinkData) {
                if (appLinkData != null) {
                    DeepLinkHelper.setAppLinkData(appLinkData);
                }
            }
        });

    }

    private void initFaceBook() {
        FacebookSdk.sdkInitialize(this);
//        FacebookSdk.addLoggingBehavior(LoggingBehavior.APP_EVENTS);//TODO 正式版开启去掉
//        FacebookSdk.setIsDebugEnabled(true);//TODO 正式版开启去掉
        FacebookSdk.setAutoInitEnabled(true);
        FacebookSdk.setAdvertiserIDCollectionEnabled(true);
        AppEventsLogger.activateApp(this);

    }


    private void initBugly() {

        // 获取当前包名
        String packageName = getApplicationContext().getPackageName();
        // 获取当前进程名
        String processName = CommonUtils.getProcessName(android.os.Process.myPid());
        // 设置是否为上报进程
//        CrashReport.UserStrategy strategy = new CrashReport.UserStrategy(getApplicationContext());
//        strategy.setUploadProcess(processName == null || processName.equals(packageName));
//        CrashReport.initCrashReport(getApplicationContext(), MyConstants.BUGLY_ID, false, strategy);
    }


}
