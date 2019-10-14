package com.stela.comics_unlimited.util.permission;

import android.Manifest;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.os.Build;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.lxy.baselibs.base.BaseActivity;
import com.lxy.baselibs.utils.ToastUtils;
import com.stela.comics_unlimited.R;

import java.util.ArrayList;

public class PermissionHelper {

    public static AlertDialog mPermissonDialog;
    public static long permissionTime;
    public PermissionInterface permissionInterface;
    public static final int GOOGLE_LOGIN = 1;
    public static final int NORMAL = 0;


    public void setPermissionInterface(PermissionInterface mPermissionInterface) {
        permissionInterface = mPermissionInterface;
    }

    public void dissPermissionInterface() {
        if (permissionInterface != null) {
            permissionInterface = null;
        }
    }

    public void initDialog(BaseActivity baseActivity, int type) {
        AlertDialog.Builder builder = new AlertDialog.Builder(baseActivity, R.style.BDAlertDialog);
        builder.setCancelable(false);
        builder.setMessage(R.string.permission_tip);
        builder.setPositiveButton("SURE", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                mPermissonDialog.dismiss();
                configAndroid6Permission(baseActivity, false, type);
            }
        });
        mPermissonDialog = builder.create();
    }

    public void showPermissionsDialog(BaseActivity baseActivity, int type) {
        if (mPermissonDialog == null) {
            initDialog(baseActivity, type);
        }
        mPermissonDialog.show();
    }

    /**
     * Android6.0的权限判断
     *
     * @param type Google 1  普通 0
     * @author zqs
     * @createTime 2016年2月25日 下午12:11:28
     */
    public void configAndroid6Permission(BaseActivity baseActivity, boolean showDiaog, int type) {
        if (Build.VERSION.SDK_INT >= 23) {
            String[] permission = new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_PHONE_STATE};
            ArrayList<String> arrayPermission = new ArrayList<>();
            for (String p : permission) {
                if (ContextCompat.checkSelfPermission(baseActivity, p) != PackageManager.PERMISSION_GRANTED) {
                    arrayPermission.add(p);
                }
            }
            if (arrayPermission.size() > 0) {
                if (showDiaog) {
                    showPermissionsDialog(baseActivity, type);
                } else {
                    String[] temp = new String[arrayPermission.size()];
                    for (int i = 0; i < arrayPermission.size(); i++) {
                        temp[i] = arrayPermission.get(i);
                    }
                    permissionTime = System.currentTimeMillis();
                    ActivityCompat.requestPermissions(baseActivity, temp, 0);
                }
            } else {
                if (permissionInterface != null) {
                    permissionInterface.permissionSuccess(type);
                }
            }
        } else {
            if (permissionInterface != null) {
                permissionInterface.permissionSuccess(type);
            }
        }
    }

    public void onRequestPermissionsResult(BaseActivity baseActivity, int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults,int type) {
        if (System.currentTimeMillis() - permissionTime < 300) {
            // 正常权限申请需要弹出框确认选择，如果时间过短，则系统权限申请被系统过滤！
            if (Build.VERSION.SDK_INT >= 23) {
                ToastUtils.showShort(baseActivity.getString(R.string.permissions_hint));
            }
        } else if (requestCode == 0) {
            for (int result : grantResults) {
                if (result != PackageManager.PERMISSION_GRANTED) {
                    showPermissionsDialog(baseActivity,type);
                    break;
                }
            }
        }
    }


    public interface PermissionInterface {
        void permissionSuccess(int type);
    }
}
