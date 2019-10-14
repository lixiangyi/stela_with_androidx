package com.stela.comics_unlimited.ui.web;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.text.TextUtils;

import com.lxy.baselibs.base.BaseActivity;
import com.stela.comics_unlimited.R;
import com.stela.comics_unlimited.widget.ProgressWebView;

import butterknife.BindView;

public class CommonWebActivity extends BaseActivity {


    private static final String WEB_URL = "web_url";
    private static final String NEEDTITLE = "needtitle";
    @BindView(R.id.webview_page)
    ProgressWebView webviewPage;
    private String url;


    public static void start(Context context, String url, boolean needTitlebar) {
        Intent starter = new Intent(context, CommonWebActivity.class);
        starter.putExtra(WEB_URL, url);
        starter.putExtra(NEEDTITLE, needTitlebar);
        context.startActivity(starter);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_common_web;
    }

    @Override
    protected void getIntent(Intent intent) {
         url = intent.getStringExtra(WEB_URL);
    }

    @Override
    protected void initView() {
        //        boolean needTitle = intent.getBooleanExtra(NEEDTITLE, true);
//        if (needTitle) {
        showBack();
        showIcon(R.mipmap.stela_logo);
//        } else {
//            showNavigationBar(false);
//        }
        if (!TextUtils.isEmpty(url)) {
            webviewPage.loadUrl(url);
        }
    }

    @Override
    protected void initListener() {

    }

    @Override
    protected void initData() {

    }

    @Override
    protected void onResume() {
        super.onResume();
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.HONEYCOMB) {
            if (webviewPage != null) {
                webviewPage.onResume();
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (webviewPage != null) {
            webviewPage.destroy();
        }

    }

    @Override
    protected void onPause() {
        super.onPause();
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.HONEYCOMB) {
            if (webviewPage != null) {
                webviewPage.onPause();
                //todo 记录网页最后阅读位置
//                int scrollY = webviewPage.getScrollY();
//                SharedPreferencesTool.putInt(this, url, scrollY);//保存访问的位置
            }
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (webviewPage != null) {
            webviewPage.onStop();
        }
    }

    @Override
    public void onBackPressed() {
        if (this.webviewPage.canGoBack()) {
            this.webviewPage.goBack();
        } else {
            super.onBackPressed();
        }
    }

}
