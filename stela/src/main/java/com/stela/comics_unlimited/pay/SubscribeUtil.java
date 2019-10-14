package com.stela.comics_unlimited.pay;

import android.app.AlertDialog;
import android.content.IntentFilter;
import android.util.Log;

import com.lxy.baselibs.base.BaseActivity;
import com.lxy.baselibs.utils.ToastUtils;

public class SubscribeUtil implements
        IabBroadcastReceiver.IabBroadcastListener {
    private static final String UNFORGOTTEN =
            Why.why +
                    "/9P4MbAEZvoqOcan/1zKuEZkOi034j3GJ6ZP4BsAPdlcXteGUKbxQvSECFYSP6bRoGo382Jg6252C83OtpkXpGs5q7Pnl9OX8+" +
                    What.what +
                    "4AbwktrsWTv3YNYGcEmbQPP/L/1y5hy3k6bOCu+QswWCmBcxRzHvhBrb25Xjpn3elF4A5CpYCBlZdfpsc+0ZGsL58/hNdpYHeQIDAQAB";
    private static final String TAG = "lixiangyi";
    static final int RC_REQUEST = 10001;
    // The helper object
    IabHelper mHelper;
    // Provides purchase notification while this app is running
    IabBroadcastReceiver mBroadcastReceiver;
    private String mInfiniteGasSku = "";
    private String mSelectedSubscriptionPeriod = "";
    private Inventory mInventory;
    private BaseActivity mBaseActivity;

    public void SubscribeUtil(BaseActivity baseActivity) {
        mBaseActivity = baseActivity;
    }


    private void initSubcribe() {
        //        String base64EncodedPublicKey = "CONSTRUCT_YOUR_KEY_AND_PLACE_IT_HERE";
        String base64EncodedPublicKey = UNFORGOTTEN;
        // Create the helper, passing it our context and the public key to verify signatures with
        Log.d(TAG, "Creating IAB helper.");
        mHelper = new IabHelper(mBaseActivity, base64EncodedPublicKey);

        // enable debug logging (for a production application, you should set this to false).
        mHelper.enableDebugLogging(true);

        // Start setup. This is asynchronous and the specified listener
        // will be called once setup completes.
        Log.d("lixiangyi", "Starting setup.");
        mHelper.startSetup(new IabHelper.OnIabSetupFinishedListener() {
            @Override
            public void onIabSetupFinished(IabResult result) {
                Log.d(TAG, "Setup finished.");

                if (!result.isSuccess()) {
                    // Oh noes, there was a problem.
                    complain("Problem setting up in-app billing: " + result);
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
                mBroadcastReceiver = new IabBroadcastReceiver(SubscribeUtil.this);
                IntentFilter broadcastFilter = new IntentFilter(IabBroadcastReceiver.ACTION);
                mBaseActivity.registerReceiver(mBroadcastReceiver, broadcastFilter);

                // IAB is fully set up. Now, let's get an inventory of stuff we own.
                try {
                    mHelper.queryInventoryAsync(mGotInventoryListener);
                } catch (IabHelper.IabAsyncInProgressException e) {
                    complain("Error querying inventory. Another async operation in progress.");
                }
            }
        });
    }

    void complain(String message) {
        ToastUtils.showShort("Error: " + message);
//        alert("Error: " + message);
    }

    void alert(String message) {
        AlertDialog.Builder bld = new AlertDialog.Builder(mBaseActivity);
        bld.setMessage(message);
        bld.setNeutralButton("OK", null);
        bld.create().show();
    }

    protected void onDestroy() {

        // very important:
        if (mBroadcastReceiver != null) {
            mBaseActivity.unregisterReceiver(mBroadcastReceiver);
        }

        // very important:
        if (mHelper != null) {
            mHelper.disposeWhenFinished();
            mHelper = null;
        }
    }

    private void doPay() {

    }

    public void receivedBroadcast() {
        // Received a broadcast notification that the inventory of items has changed
        Log.d(TAG, "Received broadcast notification. Querying inventory.");
        try {
            mHelper.queryInventoryAsync(mGotInventoryListener);
        } catch (IabHelper.IabAsyncInProgressException e) {
            complain("Error querying inventory. Another async operation in progress.");
        }
    }

    // Listener that's called when we finish querying the items and subscriptions we own
    IabHelper.QueryInventoryFinishedListener mGotInventoryListener = new IabHelper.QueryInventoryFinishedListener() {
        @Override
        public void onQueryInventoryFinished(IabResult result, Inventory inventory) {
            Log.d(TAG, "Query inventory finished.");

            // Have we been disposed of in the meantime? If so, quit.
            if (mHelper == null) {
                return;
            }

            // Is it a failure?
            if (result.isFailure()) {
                complain("Failed to query inventory: " + result);
                return;
            }

            Log.d(TAG, "Query inventory was successful.");
            mInventory = inventory;
        }
    };


}
