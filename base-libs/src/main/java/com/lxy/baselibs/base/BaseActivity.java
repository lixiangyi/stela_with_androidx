package com.lxy.baselibs.base;

import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Rect;
import android.graphics.drawable.AnimationDrawable;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.DisplayCutout;
import android.view.Gravity;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.WindowInsets;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import androidx.annotation.LayoutRes;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.core.content.ContextCompat;

import com.common.baselibs.R;
import com.lxy.baselibs.utils.DisplayUtils;
import com.lxy.baselibs.widget.NavigationBar;
import com.trello.rxlifecycle2.components.support.RxAppCompatActivity;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * @author xuhao
 * @date 2018/6/9 17:12
 * @desc 基类 BaseMvpActivity
 */
public abstract class BaseActivity extends RxAppCompatActivity {
    protected static final String TAG = "lixiangyi";
    private Unbinder unBinder;
    private NavigationBar _navBar;
    private AlertDialog loadingDialog = null;
    private RelativeLayout _rootView;
    protected RelativeLayout _containerLayout;
    protected View _contentView;
    protected View _shadow_view;
    protected int _rootViewId = 99;
    protected int _contaninerViewId = 100;
    protected int _navBarViewId = 101;
    private AnimationDrawable animationDrawable;
    private View view;

    protected View createView() {
//        requestWindowFeature(Window.FEATURE_NO_TITLE);//去掉标题栏
//        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
//        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        DisplayUtils.setStatusBarColor(this, R.color.white);
        DisplayUtils.setAndroidNativeLightStatusBar(this, true);
//        DisplayCutoutUtils.doDisplayCutout(this);
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT_WATCH) {
//            ss();
//        }
        _rootView = new RelativeLayout(this);
        _rootView.setId(_rootViewId);
//        _rootView.setOrientation(LinearLayout.VERTICAL);
        _rootView.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT));
        _rootView.setFitsSystemWindows(true);
        //头部
        _navBar = onCreateNavbar();
        if (_navBar != null) {
            _navBar.setLayoutParams(new RelativeLayout.LayoutParams(
                    RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT));
            _navBar.setId(_navBarViewId);
            _navBar.setBackgroundResource(R.color.white);
            _rootView.addView(_navBar);
            //标题阴影
            _shadow_view = new View(this);
            _shadow_view.setBackground(ContextCompat.getDrawable(this, R.drawable.tool_bar_shadow));
            _shadow_view.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, DisplayUtils.dip2px(this, 3)));
            //noinspection ResourceType
            _rootView.addView(_shadow_view);
            RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) _shadow_view.getLayoutParams();
            params.addRule(RelativeLayout.BELOW, _navBarViewId);
        }
        //内容区
        _containerLayout = new RelativeLayout(this);
        _containerLayout.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT));
        //noinspection ResourceType
        _containerLayout.setId(_contaninerViewId);
        _rootView.addView(_containerLayout);
        RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) _containerLayout.getLayoutParams();
        params.addRule(RelativeLayout.BELOW, _navBarViewId);
        //嵌入内容区
        _contentView = inflateContentView(getLayoutId());
        if (_contentView != null) {
            _containerLayout.addView(_contentView);
        }

        return _rootView;
    }

    protected void addLayoutListener(final View out, final View scroll) {
        FrameLayout content = (FrameLayout) findViewById(android.R.id.content);
        final View mChildOfContent = content.getChildAt(0);
        int cut = 0;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.P) {
//            cut = DisplayCutoutUtils.doDisplayCutout(BaseActivity.this);
//            cut = GoogleDiscut.getStatusBarHeight(BaseActivity.this);
        }
        final int finalCut = cut;
        mChildOfContent.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {

            @Override
            public void onGlobalLayout() {
                Rect rect = new Rect();
                //1.获取main 在窗体的可视区域
                out.getWindowVisibleDisplayFrame(rect);
                //2.获取main在窗体的不可视区域高度，在键盘没有弹起时，
                //main.getRootView().getHeight()调节度应该和rect.bottom高度一样
                int mianInvisibleHeight = out.getRootView().getHeight() - rect.bottom;

                int[] location = new int[2];
                scroll.getLocationInWindow(location);
                //4.获取Scroll的窗体坐标，算出main 需要滚动的高度
                int scrollHeight = (location[1] + scroll.getHeight()) - rect.bottom + finalCut;
                //3.不可见区域大于100：说明键盘弹起了
                if (mianInvisibleHeight > 100) {
                    //5.让界面整体向上移键盘的高度
                    out.scrollBy(0, scrollHeight);
                } else {
                    //3.不可见区域小于100，说明键盘隐藏了，把界面下移，移回到原有高度
                    out.scrollBy(0, scrollHeight);
                }
            }
        });
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT_WATCH)
    public void ss() {
        getWindow().getDecorView().setOnApplyWindowInsetsListener(new View.OnApplyWindowInsetsListener() {
            @RequiresApi(api = Build.VERSION_CODES.P)
            @Override
            public WindowInsets onApplyWindowInsets(View view, WindowInsets windowInsets) {
                DisplayCutout cutout = windowInsets.getDisplayCutout();
                if (cutout == null) {
                    Log.e(TAG, "cutout==null, is not notch screen");//通过cutout是否为null判断是否刘海屏手机
                } else {
                    List<Rect> rects = cutout.getBoundingRects();
                    if (rects == null || rects.size() == 0) {
                        Log.e(TAG, "rects==null || rects.size()==0, is not notch screen");
                    } else {
                        Log.e(TAG, "rect size:" + rects.size());//注意：刘海的数量可以是多个
                        for (Rect rect : rects) {
                            Log.e(TAG, "cutout.getSafeInsetTop():" + cutout.getSafeInsetTop()
                                    + ", cutout.getSafeInsetBottom():" + cutout.getSafeInsetBottom()
                                    + ", cutout.getSafeInsetLeft():" + cutout.getSafeInsetLeft()
                                    + ", cutout.getSafeInsetRight():" + cutout.getSafeInsetRight()
                                    + ", cutout.rects:" + rect
                            );
                        }
                    }
                }
                return windowInsets;
            }
        });
    }


    protected NavigationBar onCreateNavbar() {
        return new NavigationBar(this);
    }

    protected View inflateContentView(int resId) {
        return getLayoutInflater().inflate(resId, _containerLayout, false);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        beforeSetView();
        setContentView(createView());
        unBinder = ButterKnife.bind(this);
        Intent intent = getIntent();
        if (intent != null)
            getIntent(intent);
        if (useEventBus()) {
            EventBus.getDefault().register(this);//注册eventBus
        }
        showNavigationBar(false);
        initP();
        initView();
        initData();
        initListener();
    }

    protected void showNavigationBar(boolean show) {

        if (_navBar == null)
            return;

        if (show) {
            _navBar.setVisibility(View.VISIBLE);
        } else {
            _navBar.setVisibility(View.GONE);
        }
    }

    protected void goBack() {
//        if (this.isTaskRoot()) {
//            moveTaskToBack(false);
//        } else {
            finish();
//        }
    }

    protected void goNext() {

    }

    @Override
    public void onBackPressed() {
        goBack();
    }

    protected void setRightImage(int resid) {
        if (_navBar == null)
            return;

        showNavigationBar(true);
        _navBar.registerRightImage(resid, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goNext();
            }
        });
    }

    protected void setTitle(String title) {
        if (_navBar == null)
            return;

        if (!TextUtils.isEmpty(title)) {
            showNavigationBar(true);
        }
        _navBar.setTitle(title);
    }

    public void setTitle(int title) {
        String str_title = getResourceString(title);
        setTitle(str_title);
    }

    protected void setRightIcon(int icon) {
        if (_navBar == null)
            return;
        showNavigationBar(true);
        _navBar.registerRightImage(icon, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goNext();
            }
        });
    }

    protected void setRightTitle(String title) {
        if (_navBar == null)
            return;
        showNavigationBar(true);
        _navBar.registerRightTitle(title, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goNext();
            }
        });
    }

    protected void setRightTitle(String title, int color) {
        if (_navBar == null)
            return;
        showNavigationBar(true);
        _navBar.registerRightTitle(title, color, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goNext();
            }
        });
    }

    protected void setRightTitle(int resId) {
        this.setRightTitle(getResourceString(resId));
    }

    protected ImageView getRightImageView() {
        return _navBar.getRightImage();
    }


    protected String getResourceString(int resId) {
        String result = null;
        try {
            result = this.getResources().getString(resId);
        } catch (Exception e) {
        }
        return result;
    }

    protected void showBack(int resId) {
        if (_navBar == null)
            return;

        showNavigationBar(true);
        _navBar.registerBack(resId, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goBack();
            }
        });
    }

    protected void showBack(View.OnClickListener onClickListener) {
        if (_navBar == null)
            return;

        showNavigationBar(true);
        _navBar.registerBack(R.mipmap.back, onClickListener);
    }

    protected void showBack() {
        showBack(R.mipmap.back);
    }

    protected void showIcon(int icon, View.OnClickListener onClickListener) {
        if (_navBar == null)
            return;

        showNavigationBar(true);
        _navBar.registerIcon(icon, onClickListener);
    }

    protected void showIcon(int icon) {
        if (_navBar == null)
            return;

        showNavigationBar(true);
        _navBar.registerIcon(icon, new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }


    /**
     * 是否使用eventBus
     *
     * @return
     */
    protected boolean useEventBus() {
        return false;
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (unBinder != null) {
            unBinder.unbind();
        }
        if (useEventBus()) {
            if (EventBus.getDefault().isRegistered(this)) {
                EventBus.getDefault().unregister(this);//注销eventBus
            }
        }

    }

    /**
     * 通过Class跳转界面
     **/
    public void startActivity(Class<?> cls) {
        startActivity(cls, null);
    }

    /**
     * 通过Class跳转界面
     **/
    public void startActivityForResult(Class<?> cls, int requestCode) {
        startActivityForResult(cls, null, requestCode);
    }

    /**
     * 含有Bundle通过Class跳转界面
     **/
    public void startActivityForResult(Class<?> cls, Bundle bundle, int requestCode) {
        Intent intent = new Intent();
        intent.setClass(this, cls);
        if (bundle != null) {
            intent.putExtras(bundle);
        }
        startActivityForResult(intent, requestCode);
    }

    /**
     * 含有Bundle通过Class跳转界面
     **/
    public void startActivity(Class<?> cls, Bundle bundle) {
        Intent intent = new Intent();
        intent.setClass(this, cls);
        if (bundle != null) {
            intent.putExtras(bundle);
        }
        startActivity(intent);
    }

    /**
     * 显示带消息的进度框
     *
     * @param title 提示
     */
    protected void showLoadingDialog(String title) {
        createLoadingDialog();
        loadingDialog.setMessage(title);
        if (!loadingDialog.isShowing())
            loadingDialog.show();
    }

    /**
     * 显示进度框
     */
    public void showLoadingDialog() {
        createLoadingDialog();
        if (!loadingDialog.isShowing())
            loadingDialog.show();
        animationDrawable.start();
        loadingDialog.getWindow().setContentView(view);//设置弹出框加载的布局
        loadingDialog.getWindow().setGravity(Gravity.CENTER);
    }

    /**
     * 创建LoadingDialog
     */
    private void createLoadingDialog() {
        if (loadingDialog == null) {
            view = View.inflate(this, R.layout.loading, null);//填充ListView布局
            animationDrawable = (AnimationDrawable) ContextCompat.getDrawable(this, R.drawable.stela_loading_96);
            view.findViewById(R.id.iv_loading).setBackground(animationDrawable);
            animationDrawable.setOneShot(false);
            loadingDialog = new AlertDialog.Builder(this, R.style.BDAlertDialogNew).create();
            loadingDialog.setCancelable(false);
            loadingDialog.setCanceledOnTouchOutside(false);
            loadingDialog.getWindow().setDimAmount(0f);
        }
    }

    /**
     * 隐藏进度框
     */
    public void hideLoadingDialog() {

        if (loadingDialog != null && loadingDialog.isShowing()) {
            if (animationDrawable != null) {
                animationDrawable.stop();
            }
            loadingDialog.dismiss();
        }
    }


    /**
     * 获取布局 Id
     *
     * @return
     */
    protected abstract @LayoutRes
    int getLayoutId();

    /**
     * 获取 Intent 数据
     **/
    protected abstract void getIntent(Intent intent);

    protected void initP() {
    }

    protected void beforeSetView() {
    }

    ;

    /**
     * 初始化View的代码写在这个方法中
     */
    protected abstract void initView();

    /**
     * 初始化监听器的代码写在这个方法中
     */
    protected abstract void initListener();

    /**
     * 初始数据的代码写在这个方法中，用于从服务器获取数据
     */
    protected abstract void initData();


}
