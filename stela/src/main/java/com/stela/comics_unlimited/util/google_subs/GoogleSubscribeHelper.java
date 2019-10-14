package com.stela.comics_unlimited.util.google_subs;

import android.content.Intent;
import android.content.IntentFilter;
import android.text.TextUtils;

import com.lxy.baselibs.base.BaseActivity;
import com.lxy.baselibs.utils.LogUtils;
import com.lxy.baselibs.utils.ToastUtils;
import com.stela.comics_unlimited.Base.BaseMvpActivity;
import com.stela.comics_unlimited.BuildConfig;
import com.stela.comics_unlimited.app.DataStore;
import com.stela.comics_unlimited.data.entity.SubscriptionEntity;
import com.stela.comics_unlimited.pay.IabBroadcastReceiver;
import com.stela.comics_unlimited.pay.IabHelper;
import com.stela.comics_unlimited.pay.IabResult;
import com.stela.comics_unlimited.pay.Inventory;
import com.stela.comics_unlimited.pay.Purchase;
import com.stela.comics_unlimited.util.facebook.FaceBookLogUtil;

import java.util.ArrayList;
import java.util.List;

public class GoogleSubscribeHelper {

    private static String TAG;
    // (arbitrary) request code for the purchase flow
    private static final int RC_REQUEST = 10001;
    // The helper object
    private static IabHelper mHelper;
    // Provides purchase notification while this app is running
    private static IabBroadcastReceiver mBroadcastReceiver;


    private static GoogleSubsInventoryInterface googleSubsInventoryInterface;
    private static GoogleSubsPurchaseInterface googleSubsPurchaseInterface;

    // Listener that's called when we finish querying the items and subscriptions we own
    private static IabHelper.QueryInventoryFinishedListener mGotInventoryListener = new IabHelper.QueryInventoryFinishedListener() {
        @Override
        public void onQueryInventoryFinished(IabResult result, Inventory inventory) {
            LogUtils.d(TAG, "Query inventory finished.");

            // Have we been disposed of in the meantime? If so, quit.
            if (mHelper == null) {
                return;
            }

            // Is it a failure?
            if (result.isFailure()) {
                LogUtils.d("Failed to query inventory: " + result.getMessage());
                return;
            }
            if (inventory == null) {
                return;
            }

            LogUtils.d(TAG, "Query inventory was successful.");
//            List<Purchase> purchases = inventory.getAllPurchases();
            if (googleSubsInventoryInterface != null) {
                googleSubsInventoryInterface.subInvSuccess(inventory);
            }
        }
    };
    // Callback for when a purchase is finished
    private static IabHelper.OnIabPurchaseFinishedListener mPurchaseFinishedListener = new IabHelper.OnIabPurchaseFinishedListener() {
        @Override
        public void onIabPurchaseFinished(IabResult result, Purchase purchase) {
            LogUtils.d(TAG, "Purchase finished: " + result + ", purchase: " + purchase);

            // if we were disposed of in the meantime, quit.
            if (mHelper == null) {
                return;
            }


            if (result.isFailure()) {
                if (googleSubsPurchaseInterface != null) {
                    LogUtils.d(TAG, "Error purchasing: " + result);
                    googleSubsPurchaseInterface.subFailure(result);
                    //facebook 埋点
//                    for (int i = 0; i < mSubscriptionEntitys.size(); i++) {
//                        if (mSubscriptionEntitys.get(i).isSelect) {
//                            FaceBookLogUtil.log_fb_purchase_failedEvent(Double.parseDouble(mSubscriptionEntitys.get(i).price));
//                        }
//                    }
//                    hideLoading();
                }
                return;
            }
            if (googleSubsPurchaseInterface != null) {
                LogUtils.d(TAG, "Error purchasing: " + result);
                googleSubsPurchaseInterface.subSuccess(purchase);
//            verifyDeveloperPayloadNew(purchase);
            }

        }
    };


    public static void initSubcribe(BaseActivity baseActivity) {
        //        String base64EncodedPublicKey = "CONSTRUCT_YOUR_KEY_AND_PLACE_IT_HERE";
        // Create the helper, passing it our context and the public key to verify signatures with
        TAG = baseActivity.getClass().getSimpleName();
        LogUtils.d(TAG, "Creating IAB helper.");
        mHelper = new IabHelper(baseActivity);

        // enable debug logging (for a production application, you should set this to false).
        mHelper.enableDebugLogging(BuildConfig.DEBUG);

        // Start setup. This is asynchronous and the specified listener
        // will be called once setup completes.
        mHelper.startSetup(new IabHelper.OnIabSetupFinishedListener() {
            @Override
            public void onIabSetupFinished(IabResult result) {
                LogUtils.d(TAG, "Setup finished.");

                if (!result.isSuccess()) {
                    // Oh noes, there was a problem.
                    LogUtils.d(TAG, result.getMessage());
                    return;
                }

                // Have we been disposed of in the meantime? If so, quit.
                if (mHelper == null) {
                    return;
                }
                // Important: Dynamically register for broadcast messages about updated purchases.
                // We register the receiver here instead of as a <receiver> in the Manifest
                // because we always call getPurchases() at startup, so therefore we can ignore
                // any broadcasts sent while the app isn't running.
                // Note: registering this listener in an Activity is a bad idea, but is done here
                // because this is a SAMPLE. Regardless, the receiver must be registered after
                // IabHelper is setup, but before first call to getPurchases().
                mBroadcastReceiver = new IabBroadcastReceiver(new IabBroadcastReceiver.IabBroadcastListener() {
                    @Override
                    public void receivedBroadcast() {
                        // Received a broadcast notification that the inventory of items has changed
                        LogUtils.d(TAG, "Received broadcast notification. Querying inventory.");
                        try {
                            mHelper.queryInventoryAsync(mGotInventoryListener);
                        } catch (IabHelper.IabAsyncInProgressException e) {
                            LogUtils.d(TAG, "Error querying inventory. Another async operation in progress.");
                        }
                    }
                });
                IntentFilter broadcastFilter = new IntentFilter(IabBroadcastReceiver.ACTION);
                  baseActivity.registerReceiver(mBroadcastReceiver, broadcastFilter);

                // IAB is fully set up. Now, let's get an inventory of stuff we own.
                try {
                    mHelper.queryInventoryAsync(mGotInventoryListener);
                } catch (IabHelper.IabAsyncInProgressException e) {
                    LogUtils.d(TAG, e.getMessage());
//                    complain("Error querying inventory. Another async operation in progress.");
                }
            }
        });
    }

    public static boolean onActivityResult(int requestCode, int resultCode, Intent data) {
        LogUtils.d(TAG, "onActivityResult(" + requestCode + "," + resultCode + "," + data);
        if (mHelper == null) {
            return false;
        }
        // Pass on the activity result to the helper for handling
        if (!mHelper.handleActivityResult(requestCode, resultCode, data)) {
            return true;
//            super.onActivityResult(requestCode, resultCode, data);
        } else {
            LogUtils.d(TAG, "onActivityResult handled by IABUtil.");
            return false;
        }
    }

    public static boolean subscriptionsSupported() {
        if (!mHelper.subscriptionsSupported()) {
            ToastUtils.showShort("you google account can not subscribe");
            return false;
        }
        return true;
    }


    public static void doPay(BaseMvpActivity baseActivity, Inventory mInventory, List<SubscriptionEntity> mSubscriptionEntitys, String mSelectedSubscriptionPeriod) {
        //获取之前的订阅
        String mInfiniteGasSku = "";
        for (int i = 0; i < mSubscriptionEntitys.size(); i++) {
            if (mInventory != null) {
                Purchase purchase = mInventory.getPurchase(mSubscriptionEntitys.get(i).productIdentifier);
                if (purchase != null && purchase.isAutoRenewing()) {
                    mInfiniteGasSku = mSubscriptionEntitys.get(i).productIdentifier;
                    break;
                }
            }
        }

        //开始现在的订阅
        String payload = "";
        if (DataStore.getUserInfo() != null && !TextUtils.isEmpty(DataStore.getUserInfo().id)) {
            payload = DataStore.getUserInfo().id;
        }
        if (TextUtils.isEmpty(mSelectedSubscriptionPeriod)) {
            // The user has not changed from the default selection
            if (mSubscriptionEntitys != null && mSubscriptionEntitys.size() > 0) {
                for (int i = 0; i < mSubscriptionEntitys.size(); i++) {
                    if (mSubscriptionEntitys.get(i).isSelect) {
                        mSelectedSubscriptionPeriod = mSubscriptionEntitys.get(i).productIdentifier;
                        FaceBookLogUtil.logInitiateCheckoutEvent(Double.parseDouble(mSubscriptionEntitys.get(i).price));
                        break;
                    }
                }
                if (TextUtils.isEmpty(mSelectedSubscriptionPeriod)) {
                    mSelectedSubscriptionPeriod = mSubscriptionEntitys.get(0).productIdentifier;
                }
            } else {
                return;
            }
        }

        List<String> oldSkus = null;
        if (!TextUtils.isEmpty(mInfiniteGasSku)
                && !mInfiniteGasSku.equals(mSelectedSubscriptionPeriod)) {
            // The user currently has a valid subscription, any purchase action is going to
            // replace that subscription
            oldSkus = new ArrayList<String>();
            oldSkus.add(mInfiniteGasSku);
        }

        baseActivity.showLoading();
        LogUtils.d(TAG, "Launching purchase flow for gas subscription.");
        try {
            mHelper.launchPurchaseFlow(baseActivity, mSelectedSubscriptionPeriod, IabHelper.ITEM_TYPE_SUBS,
                    oldSkus, RC_REQUEST, mPurchaseFinishedListener, payload);
        } catch (IabHelper.IabAsyncInProgressException e) {
            LogUtils.d(TAG, "Error launching purchase flow. Another async operation in progress.");
            baseActivity.hideLoading();
        }
        // Reset the dialog options
        mSelectedSubscriptionPeriod = "";
    }


    public static void onDestroy(BaseActivity baseActivity) {
        // very important:
        if (mBroadcastReceiver != null) {
            baseActivity.unregisterReceiver(mBroadcastReceiver);
        }
        // very important:
        if (mHelper != null) {
            mHelper.disposeWhenFinished();
            mHelper = null;
        }
    }

    public static void setGoogleSubsInventoryInterface(GoogleSubsInventoryInterface googleSubsInventoryInterface) {
        GoogleSubscribeHelper.googleSubsInventoryInterface = googleSubsInventoryInterface;
    }

    public interface GoogleSubsInventoryInterface {
        void subInvSuccess(Inventory inventory);
    }

    public static void setGoogleSubsPurchaseInterface(GoogleSubsPurchaseInterface googleSubsPurchaseInterface) {
        GoogleSubscribeHelper.googleSubsPurchaseInterface = googleSubsPurchaseInterface;
    }

    public interface GoogleSubsPurchaseInterface {
        void subSuccess(Purchase purchase);

        void subFailure(IabResult result);
    }
}
