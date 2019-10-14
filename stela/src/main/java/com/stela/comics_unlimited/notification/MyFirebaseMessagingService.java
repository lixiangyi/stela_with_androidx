package com.stela.comics_unlimited.notification;

/**
 * Copyright 2016 Google Inc. All Rights Reserved.
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */


import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.text.TextUtils;
import android.util.Log;

import androidx.core.app.NotificationCompat;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.lxy.baselibs.app.AppConstants;
import com.lxy.baselibs.net.BaseHttpResult;
import com.lxy.baselibs.net.BaseObserver;
import com.lxy.baselibs.net.function.RetryWithDelay;
import com.lxy.baselibs.rx.RxBus;
import com.lxy.baselibs.utils.LogUtils;
import com.stela.comics_unlimited.R;
import com.stela.comics_unlimited.app.DataStore;
import com.stela.comics_unlimited.data.entity.NotificationEntity;
import com.stela.comics_unlimited.data.repository.RetrofitUtils;
import com.stela.comics_unlimited.event.MessageNeedLoadNotifyEvent;
import com.stela.comics_unlimited.event.NotificationListNotifyEvent;
import com.stela.comics_unlimited.ui.MainActivity;
import com.stela.comics_unlimited.ui.login.LoginActivity;
import com.stela.comics_unlimited.util.JsonHp;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;


/**
 * NOTE: There can only be one service in each app that receives FCM messages. If multiple
 * are declared in the Manifest then the first one will be chosen.
 * <p>
 * In order to make this Java sample functional, you must remove the following from the Kotlin messaging
 * service in the AndroidManifest.xml:
 * <p>
 * <intent-filter>
 * <action android:name="com.google.firebase.MESSAGING_EVENT" />
 * </intent-filter>
 */
public class MyFirebaseMessagingService extends FirebaseMessagingService {
    CompositeDisposable compositeDisposable;
    private static final String TAG = "MyFirebaseMessaging";
    private int requestCode = 0;

    /**
     * Called when message is received.
     *
     * @param remoteMessage Object representing the message received from Firebase Cloud Messaging.
     */
    // [START receive_message]
    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        // [START_EXCLUDE]
        // There are two types of messages data messages and notification messages. Data messages
        // are handled
        // here in onMessageReceived whether the app is in the foreground or background. Data
        // messages are the rowType
        // traditionally used with GCM. Notification messages are only received here in
        // onMessageReceived when the app
        // is in the foreground. When the app is in the background an automatically generated
        // notification is displayed.
        // When the user taps on the notification they are returned to the app. Messages
        // containing both notification
        // and data payloads are treated as notification messages. The Firebase console always
        // sends notification
        // messages. For more see: https://firebase.google.com/docs/cloud-messaging/concept-options
        // [END_EXCLUDE]

        // TODO(developer): Handle FCM messages here.
        // Not getting messages here? See why this may be: https://goo.gl/39bRNJ
        Log.d(TAG, "From: " + remoteMessage.getFrom());
        // Check if message contains a data payload.
        if (remoteMessage.getData().size() > 0) {
            Log.d(TAG, "Message data payload: " + remoteMessage.getData());

//            if (/* Check if data needs to be processed by long running job */ true) {
//                // For long-running tasks (10 seconds or more) use WorkManager.
//                scheduleJob();
//            } else {
//             Handle message within 10 seconds
//                handleNow();
////            }
            try {
                NotificationEntity notificationEntity = JsonHp.getGsonConverter().fromJson(remoteMessage.getData().toString(), NotificationEntity.class);
                getNotificationInfoNew(notificationEntity);
            } catch (Exception e) {

            }
//            doMyJobNew();
        }

        // Check if message contains a notification payload.
        if (remoteMessage.getNotification() != null) {
            Log.d(TAG, "Message Notification Body: " + remoteMessage.getNotification().getBody());
            Log.d(TAG, "Message Notification Title: " + remoteMessage.getNotification().getTitle());
        }

        // Also if you intend on generating your own notifications as a result of a received FCM
        // message, here is where that should be initiated. See sendNotification method below.
    }

    private void getNotificationInfoNew(NotificationEntity notificationEntity) {
        if (!DataStore.isLogin()) {
            LoginActivity.start(MyFirebaseMessagingService.this);
            return;
        }
        doMyJob(notificationEntity);

    }

    private void getNotificationInfo(NotificationEntity notificationEntity) {
        if (compositeDisposable == null) {
            compositeDisposable = new CompositeDisposable();
        }
        RetrofitUtils.getHttpService().getNotification(notificationEntity.id)
                .retryWhen(new RetryWithDelay())
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseObserver<NotificationEntity>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        compositeDisposable.add(d);
                    }

                    @Override
                    public void onSuccess(BaseHttpResult<NotificationEntity> result) {
                        if (result != null) {
                            doMyJob(result.getData());
                        }
                    }

                    @Override
                    public void onFailure(String errMsg, int code) {
                        if (code == BaseHttpResult.OUT_DATA_CODE) {
                            LoginActivity.start(MyFirebaseMessagingService.this);
                        }
                    }
                });
    }

    private void doMyJob(NotificationEntity notificationEntity) {
//        switch (notificationEntity.locationValue) {
//            case 0://显示notification
//                sendNotification(notificationEntity);
//                break;
//            case 1://内部  未读消息数量更新以及消息列表更新
//                RxBus.getDefault().post(new MessageNeedLoadNotifyEvent());
//                RxBus.getDefault().post(new NotificationListNotifyEvent());
//                break;
//            case 2://  内外部发送
        RxBus.getDefault().post(new MessageNeedLoadNotifyEvent());
        RxBus.getDefault().post(new NotificationListNotifyEvent());
//                sendNotification(notificationEntity);
//                break;
//        }
    }
    private void doMyJobNew() {
        RxBus.getDefault().post(new MessageNeedLoadNotifyEvent());
        RxBus.getDefault().post(new NotificationListNotifyEvent());

    }
    // [END receive_message]


    // [START on_new_token]

    /**
     * Called if InstanceID authorization is updated. This may occur if the security of
     * the previous authorization had been compromised. Note that this is called when the InstanceID authorization
     * is initially generated so this is where you would retrieve the authorization.
     */
    @Override
    public void onNewToken(String token) {
        LogUtils.i("lixiangyi", "Refreshed authorization: " + token);
        // If you want to send messages to this application instance or
        // manage this apps subscriptions on the server side, send the
        // Instance ID authorization to your app server.
        sendRegistrationToServer(token);
    }
    // [END on_new_token]

    /**
     * Schedule async work using WorkManager.
     */
    private void scheduleJob() {
        // [START dispatch_job]
//        OneTimeWorkRequest work = new OneTimeWorkRequest.Builder(MyWorker.class)
//                .build();
//        WorkManager.getInstance().beginWith(work).enqueue();
        // [END dispatch_job]
    }

    /**
     * Handle time allotted to BroadcastReceivers.
     */
    private void handleNow() {
        Log.d(TAG, "Short lived task is done.");
    }

    /**
     * Persist authorization to third-party servers.
     * <p>
     * Modify this method to associate the user's FCM InstanceID authorization with any server-side account
     * maintained by your application.
     *
     * @param token The new authorization.
     */
    private void sendRegistrationToServer(String token) {
        // TODO: Implement this method to send authorization to your app server.
        if (compositeDisposable == null) {
            compositeDisposable = new CompositeDisposable();
        }
        RetrofitUtils.getHttpService().updateNotificationToken(token,DataStore.getUserInfo().id)
                .retryWhen(new RetryWithDelay())
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseObserver<String>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        compositeDisposable.add(d);
                    }

                    @Override
                    public void onSuccess(BaseHttpResult<String> result) {

                    }

                    @Override
                    public void onFailure(String errMsg, int code) {
                    }
                });
    }

    /**
     * Create and show a simple notification containing the received FCM message.
     */
    public void sendNotification(NotificationEntity notification) {
        if (TextUtils.isEmpty(notification.url)) {
            sendNotificationUrl(notification, null);
        } else {
            Glide.with(this)
                    .asBitmap()
                    .load(notification.url)
                    .into(new SimpleTarget<Bitmap>() {
                        @Override
                        public void onResourceReady(Bitmap resource, Transition<? super Bitmap> transition) {
                            sendNotificationUrl(notification, resource);
                        }
                    });
        }

    }

    private void sendNotificationUrl(NotificationEntity notification, Bitmap imgBitmap) {
        Intent intent = new Intent(MyFirebaseMessagingService.this, MainActivity.class);
        if (notification.placementValue != null) {
            switch (notification.placementValue) {
                case NotificationEntity.PLACEMENTVALUE_PAGE://Home page
                    intent.putExtra(AppConstants.NOTIFICATION_VALUE_ID, notification.destinationValue);
                    intent.putExtra(AppConstants.HOME_PAGE_TITLE, notification.messageTitle);
                    intent.putExtra(AppConstants.NOTIFICATION_TYPE, notification.placementValue);
                    break;
                case NotificationEntity.PLACEMENTVALUE_SERIES://seriespage
                case NotificationEntity.PLACEMENTVALUE_WEB://web url
                    intent.putExtra(AppConstants.NOTIFICATION_VALUE_ID, notification.destinationValue);
                    intent.putExtra(AppConstants.NOTIFICATION_TYPE, notification.placementValue);
                    break;
                case NotificationEntity.PLACEMENTVALUE_CHAPTER://chapter page
                    intent.putExtra(AppConstants.NOTIFICATION_VALUE_ID, notification.destinationValue);
                    intent.putExtra(AppConstants.NOTIFICATION_VALUE_ID_1, notification.seriesValue);
                    intent.putExtra(AppConstants.NOTIFICATION_TYPE, notification.placementValue);
                    break;
                default:
                    break;
            }
        } else {
            intent.putExtra(AppConstants.NOTIFICATION_ID, notification.id);
            intent.putExtra(AppConstants.NOTIFICATION_TYPE, NotificationEntity.PLACEMENTVALUE_NO);
        }
        requestCode = requestCode + 1;
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, requestCode /* Request code */, intent,
                PendingIntent.FLAG_UPDATE_CURRENT);
//                PendingIntent.FLAG_ONE_SHOT);
        Log.i(TAG, "sendNotification: requestCode ： " + requestCode);
        String channelId = notification.id;
        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder notificationBuilder =
                new NotificationCompat.Builder(MyFirebaseMessagingService.this, channelId)
                        .setSmallIcon(R.drawable.push_icon)
                        .setContentTitle(notification.messageTitle)
                        .setContentText(notification.messageBody)
                        .setLargeIcon(imgBitmap)
                        .setAutoCancel(true)
                        .setSound(defaultSoundUri)
                        .setContentIntent(pendingIntent);

        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        // Since android Oreo notification channel is needed.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(channelId,
                    "Channel human readable messageTitle",
                    NotificationManager.IMPORTANCE_DEFAULT);
            notificationManager.createNotificationChannel(channel);
        }
        notificationManager.notify(requestCode/* ID of notification */, notificationBuilder.build());
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (compositeDisposable != null) {
            compositeDisposable.clear();
        }
    }
}