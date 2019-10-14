package com.stela.comics_unlimited.widget;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import android.util.AttributeSet;

import com.youth.banner.view.BannerViewPager;

public class HomeBannerViewPager extends BannerViewPager {
    private int mLastX;
    private int mLastY;

    public HomeBannerViewPager(@NonNull Context context) {
        super(context);
    }

    public HomeBannerViewPager(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

//    /**
//     * 事件拦截
//     *
//     * @param ev
//     * @return
//     */
//    @Override
//    public boolean onInterceptTouchEvent(MotionEvent ev) {
//
//        int x = (int) ev.getX();
//        int y = (int) ev.getY();
//
//        switch (ev.getAction()) {
//
//            case MotionEvent.ACTION_DOWN:
//                Log.i("CustomViewPager:", "Intercept_ACTION_DOWN");
//                mLastX = x;
//                mLastY = y;
//                break;
//            case MotionEvent.ACTION_MOVE:
//                Log.i("CustomViewPager:", "Intercept_ACTION_MOVE");
//                int dx = Math.abs(x - mLastX);
//                int dy = Math.abs(y - mLastY);
//
//                if (dx * 4 > dy) {
//                    // 上一页
//                    if (x >= mLastX) {
//                        return ViewCompat.canScrollHorizontally(this, -1);
//                    } else {
//                        // 下一页
//                        return ViewCompat.canScrollHorizontally(this, 1);
//                    }
//                }
//                break;
//        }
//
//        return super.onInterceptTouchEvent(ev);
//    }
//
//    /**
//     * 事件分发
//     *
//     * @param ev
//     * @return
//     */
//    @Override
//    public boolean dispatchTouchEvent(MotionEvent ev) {
//
//        int x = (int) ev.getX();
//        int y = (int) ev.getY();
//
//        switch (ev.getAction()) {
//
//            case MotionEvent.ACTION_DOWN:
//                Log.i("CustomViewPager:", "dispatch_ACTION_DOWN");
//                mLastX = x;
//                mLastY = y;
//                getParent().requestDisallowInterceptTouchEvent(true);
//                break;
//            case MotionEvent.ACTION_MOVE:
//                Log.i("CustomViewPager:", "dispatch_ACTION_MOVE");
//                int dx = Math.abs(x - mLastX);
//                int dy = Math.abs(y - mLastY);
//
//                if (dx * 4 > dy) {
//                    if (x >= mLastX) {
//                        // 上一页
//                        if (ViewCompat.canScrollHorizontally(this, -1)) {
//                            getParent().requestDisallowInterceptTouchEvent(true);
//                        } else {
//                            getParent().requestDisallowInterceptTouchEvent(false);
//                        }
//                    } else {
//                        // 下一页
//                        if (ViewCompat.canScrollHorizontally(this, 1)) {
//                            getParent().requestDisallowInterceptTouchEvent(true);
//                        } else {
//                            getParent().requestDisallowInterceptTouchEvent(false);
//                        }
//                    }
//                } else {
//                    getParent().requestDisallowInterceptTouchEvent(false);
//                }
//                break;
//            case MotionEvent.ACTION_UP:
//            case MotionEvent.ACTION_CANCEL:
//                Log.i("CustomViewPager:", "ACTION_UP/ACTION_CANCEL");
//                getParent().requestDisallowInterceptTouchEvent(false);
//                break;
//        }
//
//        return super.dispatchTouchEvent(ev);
//    }
//
//
//    PointF downP = new PointF();
//    PointF curP = new PointF();
//    private float xDown;// 记录手指按下时的横坐标。
//    private float xMove;// 记录手指移动时的横坐标。
//    private float yDown;// 记录手指按下时的纵坐标。
//
//
//    @Override
//    public boolean onTouchEvent(MotionEvent ev) {
//        // 每次进行onTouch事件都记录当前的按下的坐标
//        curP.x = ev.getX();
//        curP.y = ev.getY();
//        if (ev.getAction() == MotionEvent.ACTION_DOWN) {
//
//            downP.x = ev.getX();
//            downP.y = ev.getY();
//
//            xDown = ev.getX();
//            yDown = ev.getY();
//
//            // 此句代码是为了通知他的父ViewPager现在进行的是本控件的操作，不要对我的操作进行干扰
//            getParent().requestDisallowInterceptTouchEvent(true);
//        }
//
//        //移动的时候进行判断
//        if (ev.getAction() == MotionEvent.ACTION_MOVE) {
//
//            xMove = ev.getX();
//            float yMove = ev.getY();
//            // 这里判断是横向还是纵向移动，
//            if (Math.abs(yMove - yDown) < Math.abs(xMove - xDown) && Math.abs(xMove - xDown) > 2) {
//                // 横向滑动的处理
//                if (Math.abs(xMove - xDown) > 2) {
//                    // 左右滑动的时候进行拦截，自己处理
//                    getParent().requestDisallowInterceptTouchEvent(true);
//                } else {
//                    // 自己进行处理，不在上传给父布局
//                    return false;
//                }
//            } else {
//                // 父布局进行事件拦截
//                getParent().requestDisallowInterceptTouchEvent(false);
//            }
//        }
//        return super.onTouchEvent(ev);
//    }

}
