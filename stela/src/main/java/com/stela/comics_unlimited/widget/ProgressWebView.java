package com.stela.comics_unlimited.widget;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.ProgressBar;

import com.lxy.baselibs.base.BaseActivity;
import com.stela.comics_unlimited.R;
import com.stela.comics_unlimited.util.SharedPreferencesTool;

import java.util.HashMap;
import java.util.Map;

/***
 * 使用说明
 * 外部activity 必须对ProgressWebView 实现如下方法
 * 1，activity.onMediaResume--->mWebView.onMediaResume
 * 2，activity.onMediaPause--->mWebView.onMediaPause;
 * 3，activity.onStop--->mWebView.onStop;
 * 4，activity.onDestroy--->mWebView.destroy
 * 5,activity.onActivityResult----->mWebView.onActivityResult;
 * 6，mWebView.setOnErrorExitActivity(activity); // 需要finish的activity
 * 7，mWebView.setProgressWebViewListener(listener); // 实现webview自动读取标题设置到activity中
 *
 * @author lxy
 * @createTime 2018年12月25日 上午10:20:08
 */
@SuppressLint("SetJavaScriptEnabled")
public class ProgressWebView extends WebView {

    private View mErrorView;
    private ProgressBar progressbar;
    private Map<String, String> additionalHttpHeaders = new HashMap<String, String>();
    private ProgressWebViewListener progressWebViewListener;
    private BaseActivity onErrorExitActivity;
    private boolean needOnErrorExit = true;
    private String firstUrl = "";

    public ProgressBar getProgressbar() {
        return progressbar;
    }

    public ProgressWebView(Context context) {
        super(context);
        init(context);
    }

    public ProgressWebView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public ProgressWebView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context);
    }

    private void init(Context context) {
        if (isInEditMode()) {
            return;
        }
        initComponentValue(context);
        initComponentEvent(context);

    }

    private void initComponentValue(final Context context) {

        progressbar = new ProgressBar(context, null, android.R.attr.progressBarStyleHorizontal);
        progressbar.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, 10, 0, 0));
        progressbar.setProgressDrawable(getResources().getDrawable(R.drawable.webview_progress));
        addView(progressbar);
        // 设置
        getSettings().setCacheMode(WebSettings.LOAD_NO_CACHE);
        getSettings().setJavaScriptEnabled(true);
        getSettings().setDomStorageEnabled(true);
        getSettings().setDatabaseEnabled(true);
        getSettings().setAllowFileAccess(true);
        if (Build.VERSION.SDK_INT >= 21) {
            getSettings().setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);
        }
        // 自适应屏幕
        getSettings().setLoadWithOverviewMode(true);
        getSettings().setLayoutAlgorithm(WebSettings.LayoutAlgorithm.NARROW_COLUMNS);
        // 扩大比例的缩放  
        getSettings().setUseWideViewPort(true);
        setWebChromeClient(new WebChromeClient());
    }

    private void initComponentEvent(final Context context) {
        // 设置url跳转规则
        setWebViewClient(new WebViewClient() {
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                // 调用拨号程序
                if (url.startsWith("mailto:") || url.startsWith("geo:") || url.startsWith("tel:")) {
                    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                    context.startActivity(intent);
                } else {
                    // 当有新连接时，使用当前的 WebView
                    if (TextUtils.isEmpty(firstUrl)) {
                        firstUrl = url;
                    }
                    view.loadUrl(url);
                }
                return true;
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                if (progressWebViewListener != null) {
                    progressWebViewListener.onPageFinished(view, url);
                }
                // 获取标题的实现【页面返回时，能够正常显示返回页面的标题】
//                view.loadUrl("javascript:zhlWebView.setTitle(document.messageTitle)");
                //获取保存的位置position
                int position =  SharedPreferencesTool.getInt(context, url, 0);
                view.scrollTo(0, position);//webview加载完成后直接定位到上次访问的位置

            }

            @Override
            public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
                initErrorPage();
                super.onReceivedError(view, errorCode, description, failingUrl);
            }
        });
        // 取消长按复制文本功能
        this.setOnLongClickListener(new OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                return true;
            }
        });
    }

    /***
     * 提供一个公共的方法，让网页判断是否需要额外的操作（如刷新？？等）
     */
    @SuppressLint("NewApi")
    public void onResume() {
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.HONEYCOMB) {
            super.onResume();
        }

        this.loadUrl("javascript:onWebViewResume()");
    }

    @SuppressLint("NewApi")
    @Override
    public void onPause() {
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.HONEYCOMB) {
            super.onPause();
        }



    }

    /**
     * 外部调用、用于停止播放音频(webView中做题听音频需要用到)
     *
     * @author zqs
     * @createTime 2016年3月10日 下午5:04:15
     */
    public void onStop() {
    }

    @Override
    public void destroy() {
        super.destroy();
        try {
            onErrorExitActivity = null;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @Override
    protected void onScrollChanged(int l, int t, int oldl, int oldt) {
        super.onScrollChanged(l, t, oldl, oldt);
    }


    public interface ProgressWebViewListener {
        public void onPageFinished(WebView view, String url);

        public void onReceivedTitle(WebView view, String title);

        public void onReceivedRightTitle(WebView view, String title, String loadUrl);

        public void openTaskDialog();
    }

    public class WebChromeClient extends android.webkit.WebChromeClient {
        @Override
        public void onProgressChanged(WebView view, int newProgress) {
            if (newProgress == 100) {
                progressbar.setVisibility(GONE);
            } else {
                if (progressbar.getVisibility() == GONE)
                    progressbar.setVisibility(VISIBLE);
                progressbar.setProgress(newProgress);
            }
            super.onProgressChanged(view, newProgress);
        }
    }


    /**
     * 回调web端的方法
     */
    public void invokeWebFunction(String funStr) {
        loadUrl(funStr);
    }

    /***
     * 当webview出错时，能够提供退出activity的渠道
     *
     * @author zqs
     * @createTime 2016年1月28日 下午2:47:30
     */
    private void initErrorPage() {
        if (mErrorView == null) {
            mErrorView = View.inflate(getContext(), R.layout.common_webview_error, null);
            // 添加到视图
            ViewGroup vg = (ViewGroup) this.getParent();
            int index = 0;
            for (int i = 0; i < vg.getChildCount(); i++) {
                if (vg.getChildAt(i) == this) {
                    index = i;
                    break;
                }
            }
            vg.addView(mErrorView, index, this.getLayoutParams());
            // 事件
            Button btnBack = (Button) mErrorView.findViewById(R.id.btn_back);
            Button btnReload = (Button) mErrorView.findViewById(R.id.btn_reload);
            if (needOnErrorExit) {
                btnBack.setVisibility(View.VISIBLE);
            } else {
                btnBack.setVisibility(View.INVISIBLE);
            }
            mErrorView.setVisibility(View.GONE);
            btnBack.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (onErrorExitActivity != null) {
                        onErrorExitActivity.finish();
                    } else {
                        if (ProgressWebView.this.canGoBack()) {
                            ProgressWebView.this.goBack();
                        }
                    }
                }
            });
            btnReload.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    ProgressWebView.this.setVisibility(View.VISIBLE);
                    mErrorView.setVisibility(View.GONE);
                    loadUrl(firstUrl);
                }
            });
        }
        mErrorView.setVisibility(View.VISIBLE);
        this.setVisibility(View.GONE);
    }
}