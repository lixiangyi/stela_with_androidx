package com.stela.comics_unlimited.util;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.view.WindowManager;
import android.widget.EditText;

import com.lxy.baselibs.app.AppConstants;
import com.lxy.baselibs.utils.ToastUtils;
import com.stela.comics_unlimited.ui.web.CommonWebActivity;
import com.stela.comics_unlimited.util.stela_sdk.StelaAnalyticsUtil;

public class Tools {
    /**
     * EditText获取焦点并显示软键盘
     */
    public static void showSoftInputFromWindow(Activity activity, EditText editText) {
        editText.setFocusable(true);
        editText.setFocusableInTouchMode(true);
        editText.requestFocus();
        activity.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
    }


    public  static void screenShotAlertDialog(Context context, String title , String content){
        new AlertDialog.Builder(context)
                .setTitle(title)
                .setMessage(content)
                .setPositiveButton("Okay", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .setNegativeButton("Terms", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        StelaAnalyticsUtil.click( "term");
                        CommonWebActivity.start(context, AppConstants.TERMS_AND_SERVICE_URL, false);
                    }
                })
                .show();
    }

    public static void launchAppDetail(Context context) {	//appPkg 是应用的包名
        final String GOOGLE_PLAY = "com.android.vending";//这里对应的是谷歌商店，跳转别的商店改成对应的即可
        try {
            Uri uri = Uri.parse("market://details?id=" + context.getPackageName());
            Intent intent = new Intent(Intent.ACTION_VIEW, uri);
            intent.setPackage(GOOGLE_PLAY);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(intent);
        } catch (Exception e) {
            ToastUtils.showShort(e.getMessage());    //跳转失败的处理
        }
    }



}
