package com.stela.comics_unlimited.util;

import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import androidx.fragment.app.FragmentActivity;

import com.stela.comics_unlimited.R;


/**
 * 快捷键添加到桌面
 *
 * @author zqs
 * @createTime 2016年3月8日 下午9:11:11
 */
public class ShortcutUtil {

    /**
     * 添加桌面快捷
     *
     * @param context
     * @author zqs
     * @createTime 2016年3月8日 下午9:30:21
     */
    public static void addShortcutToDesktop(FragmentActivity context) {
        try {
            Intent shortcut = new Intent("com.android.launcher.action.INSTALL_SHORTCUT");
            // 不允许重建
            shortcut.putExtra("duplicate", false);
            // 设置名字
            shortcut.putExtra(Intent.EXTRA_SHORTCUT_NAME, context.getResources().getString(R.string.app_name));// 桌面快捷方式名称
            // 设置图标
            shortcut.putExtra(Intent.EXTRA_SHORTCUT_ICON_RESOURCE, Intent.ShortcutIconResource.fromContext(context, R.mipmap.ic_launcher));
            // 设置意图和快捷方式关联程序
            Intent intent = new Intent(context, context.getClass());
            // 桌面图标和应用绑定，卸载应用后系统会同时自动删除图标
            intent.setAction("android.intent.action.MAIN");
            intent.addCategory("android.intent.category.LAUNCHER");
            shortcut.putExtra(Intent.EXTRA_SHORTCUT_INTENT, intent);
            // 发送广播
            context.sendBroadcast(shortcut);
        } catch (Exception e) {
            e.printStackTrace();
        }
        SharedPreferencesTool.putBoolean(context, SharedPreferencesTool.KEY_HAS_DESTOP_SHORT_CUT, true);
    }

    /***
     * 是否已经添加桌面快捷键。 很多机型判断不准确。所以使用 SharedPreferencesTool 确保只执行一次
     *
     * @param context
     * @return
     * @author zqs
     * @createTime 2016年3月8日 下午9:29:51
     */
    public static boolean isShortcutInstalled(FragmentActivity context) {
        boolean isInstallShortcut = SharedPreferencesTool.getBoolean(context, SharedPreferencesTool.KEY_HAS_DESTOP_SHORT_CUT, false);
        if (isInstallShortcut) {
            return isInstallShortcut;
        }
        try {
            final ContentResolver cr = context.getContentResolver();
            // 2.2系统是”com.android.launcher2.settings”,网上见其他的为"com.android.launcher.settings"
            String AUTHORITY = "com.android.launcher2.settings";
            Uri CONTENT_URI = Uri.parse("content://" + AUTHORITY + "/favorites?notify=true");
            Cursor c = cr.query(CONTENT_URI, new String[]{"messageTitle", "iconResource"}, "messageTitle=?", new String[]{context.getResources().getString(R.string.app_name)}, null);// 这里得保证app_name与创建
            // 快捷方式名的一致，否则会出现提示“快捷方式已经创建”
            if (c != null && c.getCount() > 0) {
                isInstallShortcut = true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return isInstallShortcut;
    }
}
