package com.stela.comics_unlimited.util;

import android.graphics.drawable.Drawable;
import android.view.Display;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.vectordrawable.graphics.drawable.AnimatedVectorDrawableCompat;

import com.lxy.baselibs.base.BaseActivity;
import com.stela.comics_unlimited.R;
import com.stela.comics_unlimited.data.entity.VersionEntity;

public class VersionUtil {

    public static void doVersionUpdate(BaseActivity baseActivity, VersionEntity versionEntity){
        switch (versionEntity.state) {
            case VersionEntity.NEW_VERSION:
                VersionUtil.showVersionDialog(baseActivity, versionEntity, false);
                break;
            case VersionEntity.NEW_VERSION_MUST:
                VersionUtil.showVersionDialog(baseActivity, versionEntity, true);
                break;
            default:
            case VersionEntity.NO_VERSION:
                break;

        }
    }

    private static void showVersionDialog(BaseActivity baseActivity, VersionEntity versionEntity, boolean must) {
        View version = View.inflate(baseActivity, R.layout.dialog_version, null);//填充布局
        TextView versionTitle = version.findViewById(R.id.tv_version_title);
        TextView versionContent = version.findViewById(R.id.tv_version_content);
        TextView versionBtn = version.findViewById(R.id.tv_version_btn);
        RelativeLayout versionBg = version.findViewById(R.id.rl_dialog);
        AlertDialog builder = new AlertDialog.Builder(baseActivity, R.style.version_dialog).create();
        builder.show();
        if (must) {
            builder.setCancelable(false);
            builder.setCanceledOnTouchOutside(false);
        }
        //
        Window window = builder.getWindow() ;
        WindowManager m = baseActivity.getWindowManager();
        Display d = m.getDefaultDisplay(); // 获取屏幕宽、高用
        WindowManager.LayoutParams p = window.getAttributes(); // 获取对话框当前的参数值
        p.width = (int) (d.getWidth() * 8/9); // 宽度设置为屏幕的0.65
        p.height = d.getHeight()*9/16; // 改变的是dialog框在屏幕中的位置而不是大小
        window.setAttributes(p);
        //
        builder.getWindow().setContentView(version);//设置弹出框加载的布局
        builder.getWindow().setGravity(Gravity.CENTER);
         //
        versionTitle.setText(versionEntity.title);
        versionContent.setText(versionEntity.note);
        Drawable avd = AnimatedVectorDrawableCompat.create(baseActivity, R.drawable.ic_version_bg);
        versionBg.setBackground(avd);
        versionBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Tools.launchAppDetail(baseActivity);
                if (!must) {
                    builder.dismiss();
                }
            }
        });
    }
}
