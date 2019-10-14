package com.stela.comics_unlimited.app;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;

import com.lxy.baselibs.app.AppManager;
import com.lxy.baselibs.app.BaseApplication;
import com.lxy.baselibs.base.BaseActivity;
import com.lxy.baselibs.utils.CommonApkShare;
import com.lxy.baselibs.utils.LogUtils;
import com.stela.comics_unlimited.data.entity.PersonEntity;
import com.stela.comics_unlimited.ui.login.LoginActivity;
import com.stela.comics_unlimited.util.SharedPreferencesTool;


public class DataStore {

    public static void setUserInfo(PersonEntity userInfo) {
        SharedPreferencesTool.saveObj(MyApp.getContext(), CommonApkShare.KEY_LOGIN_USER, userInfo);
        CommonApkShare.putString(BaseApplication.getContext(), CommonApkShare.KEY_USER_ID, userInfo.id);
        if (!TextUtils.isEmpty(userInfo.authorization)) {
            CommonApkShare.putString(BaseApplication.getContext(), CommonApkShare.KEY_TOKEN, userInfo.authorization);
        }
    }
    public static PersonEntity getUserInfo() {
        PersonEntity userEntity = (PersonEntity) SharedPreferencesTool.getObj(MyApp.getContext(), CommonApkShare.KEY_LOGIN_USER);
        if (userEntity == null) {
            userEntity = new PersonEntity();
        }
        return userEntity;
    }


    public static void clearUserInfo() {
        SharedPreferencesTool.saveObj(MyApp.getContext(), CommonApkShare.KEY_LOGIN_USER, null);
        CommonApkShare.putString(BaseApplication.getContext(), CommonApkShare.KEY_TOKEN, "");
        CommonApkShare.putString(BaseApplication.getContext(), CommonApkShare.KEY_USER_ID, "");
        CommonApkShare.putString(BaseApplication.getContext(), CommonApkShare.KEY_DEEP_LINK, "");
    }
    public static void setDeeplinkId(String deeplink) {
        CommonApkShare.putString(BaseApplication.getContext(), CommonApkShare.KEY_DEEP_LINK, deeplink);
    }
    public static String getDeeplinkId() {
       return CommonApkShare.getString(BaseApplication.getContext(), CommonApkShare.KEY_DEEP_LINK);
    }



    public static boolean isLogin() {
        return !TextUtils.isEmpty(CommonApkShare.getString(BaseApplication.getContext(), CommonApkShare.KEY_TOKEN));
    }

    public static void logout(Context context) {
        clearUserInfo();
        AppManager.getInstance().finishAllActivity();
        LoginActivity.start(context);
    }






    public static String getEmail(Context context) {
        AccountManager accountManager = AccountManager.get(context);
        Account[] accounts = accountManager.getAccountsByType("com.google");//获取google账户
        Account account = accounts.length > 0 ? accounts[0] : null;//取第一个账户
        return account == null ? null : account.name;
    }

    public static void getEmail8(BaseActivity context, int code) {
        Intent googlePicker = AccountManager.newChooseAccountIntent(null, null,
                new String[]{"com.google"}, true, null, null, null, null);
        context.startActivityForResult(googlePicker, code);
    }

    public static Account[] handleActivityResult(BaseActivity context, int requestCode, int resultCode, Intent data) {
        String accountName = data.getStringExtra(AccountManager.KEY_ACCOUNT_NAME);
        LogUtils.d("lixiangyi", "Account Name=" + accountName);
        String accountType = data.getStringExtra(AccountManager.KEY_ACCOUNT_TYPE);
        LogUtils.d("lixiangyi", "Account type=" + accountType);

        AccountManager accountManager = AccountManager.get(context);
        Account[] accounts = accountManager.getAccounts();
        for (Account a : accounts) {
            LogUtils.d("lixiangyi", "type--- " + a.type + " ---- name---- " + a.name);
        }
        return accounts;
    }

}
