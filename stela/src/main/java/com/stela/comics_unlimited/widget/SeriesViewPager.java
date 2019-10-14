package com.stela.comics_unlimited.widget;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.view.ViewCompat;
import androidx.viewpager.widget.ViewPager;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;


/**
 * @author LXY
 */
public class SeriesViewPager extends ViewPager {
    private int mLastX;
    private int mLastY;

    public SeriesViewPager(@NonNull Context context) {
        super(context);
    }

    public SeriesViewPager(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }
    /**
     * 事件拦截
     * @param ev
     * @return
     */
    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {

        int x = (int)ev.getX();
        int y = (int)ev.getY();

        switch (ev.getAction()){

            case MotionEvent.ACTION_DOWN:
                Log.i("CustomViewPager:","Intercept_ACTION_DOWN");
                mLastX = x;
                mLastY = y;
                break;
            case MotionEvent.ACTION_MOVE:
                Log.i("CustomViewPager:","Intercept_ACTION_MOVE");
                int dx = Math.abs(x - mLastX);
                int dy = Math.abs(y - mLastY);

                if(dx * 4 > dy){
                    // 上一页
                    if(x >= mLastX){
                        return ViewCompat.canScrollHorizontally(this,-1);
                    }else {
                        // 下一页
                        return ViewCompat.canScrollHorizontally(this,1);
                    }
                }
                break;
        }

        return super.onInterceptTouchEvent(ev);
    }

    /**
     * 事件分发
     * @param ev
     * @return
     */
    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {

        int x = (int)ev.getX();
        int y = (int)ev.getY();

        switch (ev.getAction()){

            case MotionEvent.ACTION_DOWN:
                Log.i("CustomViewPager:","dispatch_ACTION_DOWN");
                mLastX = x;
                mLastY = y;
                getParent().requestDisallowInterceptTouchEvent(true);
                break;
            case MotionEvent.ACTION_MOVE:
                Log.i("CustomViewPager:","dispatch_ACTION_MOVE");
                int dx = Math.abs(x - mLastX);
                int dy = Math.abs(y - mLastY);

                if(dx * 4 > dy){
                    if(x >= mLastX){
                        // 上一页
                        if(ViewCompat.canScrollHorizontally(this,-1)){
                            getParent().requestDisallowInterceptTouchEvent(true);
                        }else {
                            getParent().requestDisallowInterceptTouchEvent(false);
                        }
                    }else {
                        // 下一页
                        if(ViewCompat.canScrollHorizontally(this,1)){
                            getParent().requestDisallowInterceptTouchEvent(true);
                        }else {
                            getParent().requestDisallowInterceptTouchEvent(false);
                        }
                    }
                }else {
                    getParent().requestDisallowInterceptTouchEvent(false);
                }
                break;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
                Log.i("CustomViewPager:","ACTION_UP/ACTION_CANCEL");
                getParent().requestDisallowInterceptTouchEvent(false);
                break;
        }

        return super.dispatchTouchEvent(ev);
    }
}
