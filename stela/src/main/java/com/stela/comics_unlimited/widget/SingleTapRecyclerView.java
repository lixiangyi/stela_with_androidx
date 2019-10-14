package com.stela.comics_unlimited.widget;

import android.content.Context;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;

/**
 * Created byli on 2017/3/16.
 */


public class SingleTapRecyclerView extends RecyclerView {
    private final static String TAG = SingleTapRecyclerView.class.getSimpleName();
    private final Context mContext;

    /**
     * 单击、双击手势
     */
    private GestureDetector mGestureDetector;

    /**
     * 开放监听接口
     */
    private OnGestureListener mOnGestureListener;

    public interface OnGestureListener {
        boolean onSingleTapConfirmed(MotionEvent e);
    }


    /**
     * 数据变化监听
     */
    private AdapterDataObserver observer;
    /**
     * 数据为空时显示
     */
    private View emptyView;

    public SingleTapRecyclerView(Context context) {
        this(context, null);
    }

    public SingleTapRecyclerView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SingleTapRecyclerView(Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        mContext = context;
        initView();
        initDetector();
    }



    /**
     * 初始化View
     */
    private void initView() {
        observer = new AdapterDataObserver() {
            @Override
            public void onChanged() {
                adapterIsEmpty();
            }
        };

    }

    /**
     * 初始化手势监听
     */
    private void initDetector() {


        mGestureDetector = new GestureDetector(mContext, new GestureDetector.SimpleOnGestureListener() {
            @Override
            public boolean onSingleTapConfirmed(MotionEvent e) {
                if (mOnGestureListener != null) {
                    return mOnGestureListener.onSingleTapConfirmed(e);
                }
                return false;
            }

            @Override
            public boolean onDoubleTap(MotionEvent e) {
                return true;
            }
        });
    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {
        super.onTouchEvent(event);
        /** 单击、双击事件的处理 */
        if (mGestureDetector.onTouchEvent(event)) {
            return true;
        }
        return true;
    }

    @Override
    public void setAdapter(Adapter adapter) {
        Adapter oldAdapter = getAdapter();
        if (oldAdapter != null && observer != null) {
            oldAdapter.unregisterAdapterDataObserver(observer);
        }
        if (adapter != null && observer != null) {
            adapter.registerAdapterDataObserver(observer);
        }
        super.setAdapter(adapter);
        adapterIsEmpty();
    }

    /**
     * 判断数据为空,显示emptyView
     */
    private void adapterIsEmpty() {
        if (emptyView != null) {
            emptyView.setVisibility(getAdapter().getItemCount() > 0 ? GONE : VISIBLE);
        }
    }



    /**
     * setter and getter
     */
    public View getEmptyView() {
        return emptyView;
    }

    public void setEmptyView(View emptyView) {
        this.emptyView = emptyView;
    }



    public OnGestureListener getOnGestureListener() {
        return mOnGestureListener;
    }

    public void setOnGestureListener(OnGestureListener onGestureListener) {
        this.mOnGestureListener = onGestureListener;
    }



}
