package com.stela.comics_unlimited.util.stela_sdk;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;
import androidx.core.content.ContextCompat;

import com.amazonaws.auth.CognitoCachingCredentialsProvider;
import com.amazonaws.mobileconnectors.apigateway.ApiClientFactory;
import com.amazonaws.regions.Regions;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;
import com.lxy.baselibs.app.BaseApplication;
import com.lxy.baselibs.base.BaseActivity;
import com.lxy.baselibs.utils.AppUtils;
import com.lxy.baselibs.utils.DateUtils;
import com.lxy.baselibs.utils.DisplayUtils;
import com.lxy.baselibs.utils.LogUtils;
import com.stela.analytics.StelaAnalyticsClient;
import com.stela.analytics.model.App;
import com.stela.analytics.model.Basic;
import com.stela.analytics.model.Chapter;
import com.stela.analytics.model.Device;
import com.stela.analytics.model.Empty;
import com.stela.analytics.model.Page;
import com.stela.analytics.model.Purchase;
import com.stela.analytics.model.Register;
import com.stela.analytics.model.Series;
import com.stela.analytics.model.Target;
import com.stela.comics_unlimited.R;
import com.stela.comics_unlimited.app.DataStore;
import com.stela.comics_unlimited.ui.MainActivity;
import com.stela.comics_unlimited.util.AdvertisingIdClient;

import static com.stela.comics_unlimited.app.MyApp.mFirebaseAnalytics;

public class StelaAnalyticsUtil {

    private static final String AWS_KEY = "EZI17d1NJM5i7Dvtghl8B8lSuOw8EBSy9aqUS9Fk";
    private static final String AWS_SCRECT_KEY = "BHXt8aoq8i7hXWCCstgrmfURlS6bVH3A4UZraaRf";
    private static final String AWS_POOL_ID = "us-west-2:55855e1d-eebd-4ca0-916c-aa3ad256e2d6";
    private static ApiClientFactory factory;
    private static StelaAnalyticsClient client;
    //    private static boolean checkApi = BuildConfig.DEBUG;//ture 抓接口  false 不抓     抓接口因为证书会crash
    private static boolean checkApi = false;//ture 抓接口  false 不抓     抓接口因为证书会crash
    private static Context context = BaseApplication.getContext();//content

    private static void setAWS_AnalyticsFactory() {
        // Initialize the Amazon Cognito credentials provider
        CognitoCachingCredentialsProvider credentialsProvider = new CognitoCachingCredentialsProvider(
                context,
                // Identity pool ID
                AWS_POOL_ID,
                // Region
                Regions.US_WEST_2
        );
        factory = new ApiClientFactory()
                .credentialsProvider(credentialsProvider);
        factory.apiKey(AWS_KEY);
    }

    public static ApiClientFactory getAWS_AnalyticsFactory() {
        if (factory != null) {
            return factory;
        } else {
            setAWS_AnalyticsFactory();
        }
        return factory;
    }

    public static StelaAnalyticsClient getStelaAnalyticsClient() {
        if (client != null) {
            return client;
        } else {
            client = getAWS_AnalyticsFactory().build(StelaAnalyticsClient.class);
        }
        return client;
    }

    public static void appOpenPost() {
        App app = new App();
//        firebase authorization
        app.setUuid(mFirebaseAnalytics.getFirebaseInstanceId());
        //userid
        app.setGuid(DataStore.getUserInfo().id);
        // sys
        app.setDatetime(DateUtils.getNowTimePST());

        new Thread(new Runnable() {
            @Override
            public void run() {
                if (!checkApi) {
                    try {
                        Empty output = getStelaAnalyticsClient().appOpenPost(app);
                        LogUtils.i("lixiangyi", "appOpenPost");
                    } catch (Exception e) {
                        LogUtils.i("lixiangyi", "appOpenPost  failed");
                    }

                }
            }
        }).start();
    }

    public static void close() {
        Basic basic = new Basic();
//        firebase authorization
        basic.setUuid(mFirebaseAnalytics.getFirebaseInstanceId());
        //userid
        basic.setGuid(DataStore.getUserInfo().id);
        // sys
        basic.setDatetime(DateUtils.getNowTimePST());
        new Thread(new Runnable() {
            @Override
            public void run() {
                if (!checkApi) {
                    try {
                        Empty output = getStelaAnalyticsClient().appClosePost(basic);
                        LogUtils.i("lixiangyi", "appClosePost");
                    } catch (Exception e) {
                        LogUtils.i("lixiangyi", "appClosePost  failed");
                    }
                }
            }
        }).start();
    }

    public static void click(String clickName) {
        Target target = new Target();
//        firebase authorization
        target.setUuid(mFirebaseAnalytics.getFirebaseInstanceId());
        //userid
        target.setGuid(DataStore.getUserInfo().id);
        // sys
        target.setDatetime(DateUtils.getNowTimePST());
        //click name
        target.setTarget(clickName);
        new Thread(new Runnable() {
            @Override
            public void run() {
                if (!checkApi) {
                    try {
                        Empty output = getStelaAnalyticsClient().clickPost(target);
                        LogUtils.i("lixiangyi", "clickPost");
                    } catch (Exception e) {
                        LogUtils.i("lixiangyi", "clickPost  failed");
                    }

                }
            }
        }).start();
    }

    public static void purchase(Purchase purchase) {
//        firebase authorization
        purchase.setUuid(mFirebaseAnalytics.getFirebaseInstanceId());
        //userid
        purchase.setGuid(DataStore.getUserInfo().id);
        // sys
        purchase.setDatetime(DateUtils.getNowTimePST());
        purchase.setPlatform("android");
        purchase.setWall("");
        purchase.setPackage(context.getPackageName());
        new Thread(new Runnable() {
            @Override
            public void run() {
                if (!checkApi) {
                    try {
                        Empty output = getStelaAnalyticsClient().purchasePost(purchase);
                        LogUtils.i("lixiangyi", "purchase  " + purchase.getStatus());
                    } catch (Exception e) {
                        LogUtils.i("lixiangyi", "purchase  failed");
                    }

                }
            }
        }).start();
    }

    public static void series(Series series) {
//        firebase authorization
        series.setUuid(mFirebaseAnalytics.getFirebaseInstanceId());
        //userid
        series.setGuid(DataStore.getUserInfo().id);
        // sys
        series.setDatetime(DateUtils.getNowTimePST());
        new Thread(new Runnable() {
            @Override
            public void run() {
                if (!checkApi) {
                    try {
                        Empty output = getStelaAnalyticsClient().seriesPost(series);
                        LogUtils.i("lixiangyi", "series");
                    } catch (Exception e) {
                        LogUtils.i("lixiangyi", "series  failed");
                    }

                }
            }
        }).start();
    }

    public static void stelaAnalyticsSeries(String pageId, String pageName, String groupId, String groupName, String seriesId, String title) {
        //埋点
        Series series = new Series();
        series.setPageId(pageId);
        series.setPageName(pageName);
        series.setGroupId(groupId);
        series.setGroupName(groupName);
        series.setSeriesId(seriesId);
        series.setSeriesName(title);
        StelaAnalyticsUtil.click("series_view");
        StelaAnalyticsUtil.series(series);
    }

    public static void page(Page page) {
//        firebase authorization
        page.setUuid(mFirebaseAnalytics.getFirebaseInstanceId());
        //userid
        page.setGuid(DataStore.getUserInfo().id);
        // sys
        page.setDatetime(DateUtils.getNowTimePST());

        new Thread(new Runnable() {
            @Override
            public void run() {
                if (!checkApi) {

                    try {
                        Empty output = getStelaAnalyticsClient().pagePost(page);
                        LogUtils.i("lixiangyi", "pagePost");
                    } catch (Exception e) {
                        LogUtils.i("lixiangyi", "pagePost  failed");
                    }


                }
            }
        }).start();
    }

    public static void chapter(Chapter chapter) {
//        firebase authorization
        chapter.setUuid(mFirebaseAnalytics.getFirebaseInstanceId());
        //userid
        chapter.setGuid(DataStore.getUserInfo().id);
        // sys
        chapter.setDatetime(DateUtils.getNowTimePST());

        new Thread(new Runnable() {
            @Override
            public void run() {
                if (!checkApi) {
                    try {
                        Empty output = getStelaAnalyticsClient().chapterPost(chapter);
                        LogUtils.i("lixiangyi", "chapterPost");
                    } catch (Exception e) {
                        LogUtils.i("lixiangyi", "chapterPost  failed");
                    }


                }
            }
        }).start();
    }

    public static void register(Register register, String email) {
//        firebase authorization
        register.setUuid(mFirebaseAnalytics.getFirebaseInstanceId());
        //userid
        register.setGuid("");
        register.setEmail(email);
        register.setNickname("");
        register.setSource("Android");
        // sys
        register.setDatetime(DateUtils.getNowTimePST());

        new Thread(new Runnable() {
            @Override
            public void run() {
                if (!checkApi) {
                    try {
                        Empty output = getStelaAnalyticsClient().registerPost(register);
                        LogUtils.i("lixiangyi", "register");
                    } catch (Exception e) {
                        LogUtils.i("lixiangyi", "register  failed");
                    }

                }
            }
        }).start();
    }

    public static void login() {
        Device device = new Device();
//        firebase authorization
        device.setUuid(mFirebaseAnalytics.getFirebaseInstanceId());
        //userid
        device.setGuid(DataStore.getUserInfo().id);
        // sys
        device.setDatetime(DateUtils.getNowTimePST());
        device.setManufacturer(AppUtils.getDeviceBrand());
        device.setModelId(AppUtils.getSystemModel());
        device.setImei(AppUtils.getIMEI(context));
        device.setOsName(AppUtils.getSystemModel());
        device.setOsVersion(AppUtils.getSystemVersion());
        device.setScreenHeight(DisplayUtils.getScreenHeight(context) + "");
        device.setScreenWidth(DisplayUtils.getScreenWidth(context) + "");
        device.setCellularProvider("Android");

        FirebaseInstanceId.getInstance().getInstanceId()
                .addOnCompleteListener(new OnCompleteListener<InstanceIdResult>() {
                    @Override
                    public void onComplete(@NonNull Task<InstanceIdResult> task) {
                        if (task.isSuccessful()) {
                            String token = task.getResult().getToken();
                            LogUtils.i("lixiangyi", "token: " + token);
                            device.setPushToken(token);
                            new Thread(new Runnable() {
                                @Override
                                public void run() {
                                    if (!checkApi) {
                                        try {
                                            device.setAdvertsingId(AdvertisingIdClient.getGoogleAdId(context));
                                            Empty output = getStelaAnalyticsClient().loginPost(device);
                                            LogUtils.i("lixiangyi", "login");
                                        } catch (Exception e) {
                                            e.printStackTrace();
                                            LogUtils.i("lixiangyi", "login fail");
                                        }
                                    }
                                }
                            }).start();
                        }
                    }
                });

    }

    public static void install() {
        Device device = new Device();
//        firebase authorization
        device.setUuid(mFirebaseAnalytics.getFirebaseInstanceId());
        //userid
        device.setGuid("");
        // sys
        device.setDatetime(DateUtils.getNowTimePST());
        device.setManufacturer(AppUtils.getDeviceBrand());
        device.setModelId(AppUtils.getSystemModel());
        device.setImei(AppUtils.getIMEI(context));
        device.setOsName(AppUtils.getSystemModel());
        device.setOsVersion(AppUtils.getSystemVersion());
        device.setScreenHeight(DisplayUtils.getScreenHeight(context) + "");
        device.setScreenWidth(DisplayUtils.getScreenWidth(context) + "");
        device.setCellularProvider("Android");

        FirebaseInstanceId.getInstance().getInstanceId()
                .addOnCompleteListener(new OnCompleteListener<InstanceIdResult>() {
                    @Override
                    public void onComplete(@NonNull Task<InstanceIdResult> task) {
                        if (task.isSuccessful()) {
                            String token = task.getResult().getToken();
                            LogUtils.i("lixiangyi", "token: " + token);
                            device.setPushToken(token);
                            new Thread(new Runnable() {
                                @Override
                                public void run() {
                                    if (!checkApi) {
                                        try {
                                            device.setAdvertsingId(AdvertisingIdClient.getGoogleAdId(context));
                                            Empty output = getStelaAnalyticsClient().appInstallPost(device);
                                            LogUtils.i("lixiangyi", "install");
                                        } catch (Exception e) {
                                            e.printStackTrace();
                                            LogUtils.i("lixiangyi", "install fail");
                                        }
                                    }
                                }
                            }).start();
                        }
                    }
                });
    }

    public static void sendNotification(BaseActivity baseActivity) {
        //todo 测试 notifcation
        Glide.with(baseActivity)
                .asBitmap()
                .load("http://cdns3.stela.com/assets/series/d6791d6e-91e6-4776-978a-b1cb37f495fe/large_square/Lumi_LumiCover_1-1_V2_Layer0_095e83912713a4d951569ccfebf098ca.webp")
                .into(new SimpleTarget<Bitmap>() {
                    @Override
                    public void onResourceReady(Bitmap resource, Transition<? super Bitmap> transition) {
                        Intent intent = new Intent(baseActivity, MainActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        PendingIntent pendingIntent = PendingIntent.getActivity(baseActivity, 0 /* Request code */, intent,
                                PendingIntent.FLAG_ONE_SHOT);

                        String channelId = baseActivity.getString(R.string.default_notification_channel_id);
                        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
                        NotificationCompat.Builder notificationBuilder =
                                new NotificationCompat.Builder(baseActivity, channelId)
                                        .setSmallIcon(R.mipmap.eye)
                                        .setLargeIcon(BitmapFactory.decodeResource(baseActivity.getResources(), R.mipmap.ic_launcher))
                                        .setContentTitle(baseActivity.getString(R.string.app_name))
                                        .setContentText("测试")
                                        .setColor(ContextCompat.getColor(baseActivity, R.color.stela_blue))
                                        .setLargeIcon(resource)
                                        .setAutoCancel(true)
                                        .setSound(defaultSoundUri)
                                        .setContentIntent(pendingIntent);

                        NotificationManager notificationManager =
                                (NotificationManager) baseActivity.getSystemService(Context.NOTIFICATION_SERVICE);

                        // Since android Oreo notification channel is needed.
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                            NotificationChannel channel = new NotificationChannel(channelId,
                                    "Channel human readable messageTitle",
                                    NotificationManager.IMPORTANCE_DEFAULT);
                            notificationManager.createNotificationChannel(channel);
                        }
                        notificationManager.notify(0 /* ID of notification */, notificationBuilder.build());
                    }
                });
//        notificationManager.notify(0 /* ID of notification */, notificationBuilder.build());

    }


}
