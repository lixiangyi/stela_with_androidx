package com.stela.comics_unlimited.util.facebook;

import android.os.Bundle;

import com.facebook.appevents.AppEventsConstants;
import com.facebook.appevents.AppEventsLogger;
import com.lxy.baselibs.app.BaseApplication;

import java.math.BigDecimal;
import java.util.Currency;
import java.util.Locale;

public class FaceBookLogUtil {
   private  static AppEventsLogger logger;
   private static Currency currency = Currency.getInstance(Locale.US);
    private static void init() {
        logger = AppEventsLogger.newLogger(BaseApplication.getContext());

    }



    public static void logAddToCartEvent(double price) {
        if (logger == null) {
            init();
        }
        Bundle bundle = new Bundle();
        bundle.putString(AppEventsConstants.EVENT_PARAM_CURRENCY,currency.getCurrencyCode());
        logger.logEvent(AppEventsConstants.EVENT_NAME_ADDED_TO_CART, price,bundle);
    }
    public static void logInitiateCheckoutEvent(double price) {
        if (logger == null) {
            init();
        }
        Bundle bundle = new Bundle();
        bundle.putString(AppEventsConstants.EVENT_PARAM_CURRENCY,currency.getCurrencyCode());
        logger.logEvent(AppEventsConstants.EVENT_NAME_INITIATED_CHECKOUT, price,bundle);
    }
    public static void logFBSDKAppEventNameStartTrialEvent() {
        if (logger == null) {
            init();
        }
        Bundle bundle = new Bundle();
        bundle.putString(AppEventsConstants.EVENT_PARAM_CURRENCY,currency.getCurrencyCode());
        logger.logEvent(AppEventsConstants.EVENT_NAME_START_TRIAL,bundle);
    }
    public static void logPurchaseEvent(BigDecimal price) {
        if (logger == null) {
            init();
        }
        logger.logPurchase(price,currency);
    }
    /**
     * This function assumes logger is an instance of AppEventsLogger and has been
     * created using AppEventsLogger.newLogger() call.
     */
    public static void log_fb_purchase_failedEvent(double price) {
        if (logger == null) {
            init();
        }
        Bundle bundle = new Bundle();
        bundle.putString(AppEventsConstants.EVENT_PARAM_CURRENCY,currency.getCurrencyCode());
        bundle.putDouble(AppEventsConstants.EVENT_PARAM_VALUE_TO_SUM,price);
        logger.logEvent("purchasefailed",bundle);
    }
    /**
     * A submission of information by a customer in exchange for a service provided by your business. For example, sign up for an email subscription.
     */
    public static void logCompleteRegistrationEvent() {
        if (logger == null) {
            init();
        }
        logger.logEvent("Complete Registration");
    }
    public static void logCompleteTutorialEvent() {
        if (logger == null) {
            init();
        }
        logger.logEvent("Complete Tutorial");
    }
}
