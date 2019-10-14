package com.lxy.baselibs.widget;

import android.content.Context;
import android.graphics.drawable.Drawable;
import androidx.core.content.ContextCompat;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.common.baselibs.R;


/**
 * Created by  on 19/18/2.
 * 导航栏
 */
public class NavigationBar extends RelativeLayout {
    public AppCompatTextView mTxtTitle;
    public AppCompatTextView mTxtRight;
    public RelativeLayout mlayoutBg;
    public AppCompatImageView mIvBack;
    public AppCompatImageView mIvRight;
    public AppCompatImageView mIvIcon;

    public NavigationBar(Context context) {
        super(context);
        initWidthContext(context);
    }

    public NavigationBar(Context context, AttributeSet attrs) {
        super(context, attrs);
        initWidthContext(context);
    }

    public NavigationBar(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        initWidthContext(context);
    }

    protected void initWidthContext(Context context) {
        LayoutInflater.from(context).inflate(R.layout.nav_common, this, true);

        mIvBack = (AppCompatImageView) findViewById(R.id.iv_nav_left_icon);
        mIvIcon = (AppCompatImageView) findViewById(R.id.iv_nav_icon);
        mTxtTitle = (AppCompatTextView) findViewById(R.id.tv_nav_title);
        mlayoutBg = (RelativeLayout) findViewById(R.id.nav_toolbar);
        mTxtRight = (AppCompatTextView) findViewById(R.id.tv_nav_right_title);
        mIvRight = (AppCompatImageView) findViewById(R.id.iv_nav_right_icon);
    }

    public void setTitle(String title) {
        mIvIcon.setVisibility(GONE);
        mTxtTitle.setVisibility(View.VISIBLE);
        mTxtTitle.setText(title);
    }

    public CharSequence getTitle() {
        return mTxtTitle.getText();
    }

    public void setTitle(int resid) {
        mIvIcon.setVisibility(GONE);
        mTxtTitle.setVisibility(View.VISIBLE);
        mTxtTitle.setText(resid);
    }

    public void registerRightTitle(String title, OnClickListener listener) {
        if (listener == null) {
            mTxtRight.setVisibility(View.INVISIBLE);
        } else {
            mTxtRight.setText(title);

            mTxtRight.setVisibility(View.VISIBLE);
            mTxtRight.setOnClickListener(listener);
        }
    }

    public void registerRightTitle(String title, int Color, OnClickListener listener) {
        if (listener == null) {
            mTxtRight.setVisibility(View.INVISIBLE);
        } else {
            mTxtRight.setText(title);
            mTxtRight.setTextColor(ContextCompat.getColor(getContext(), Color));
            mTxtRight.setVisibility(View.VISIBLE);
            mTxtRight.setOnClickListener(listener);
        }
    }

    public void registerRightImage(int resid, OnClickListener listener) {
        if (listener == null) {
            mIvRight.setVisibility(View.INVISIBLE);
        } else {
            mIvRight.setImageResource(resid);
            mIvRight.setVisibility(View.VISIBLE);
            mIvRight.setOnClickListener(listener);
        }
    }

    public void registerIcon(int icon, OnClickListener listener) {
        if (listener == null) {
            mTxtTitle.setVisibility(View.INVISIBLE);
        } else {
            mTxtTitle.setVisibility(View.GONE);
            mIvIcon.setImageResource(icon);
            mIvIcon.setVisibility(View.VISIBLE);
            mIvIcon.setOnClickListener(listener);
        }
    }

    public void registerRightTitle(int title, OnClickListener listener) {
        if (listener == null) {
            mTxtRight.setVisibility(View.INVISIBLE);
        } else {
            mTxtRight.setText(title);
            mTxtRight.setVisibility(View.VISIBLE);
            mTxtRight.setOnClickListener(listener);
        }
    }


    public void registerRightImageTitle(int title, int resid, OnClickListener listener) {
        if (listener == null) {
            mTxtRight.setVisibility(View.INVISIBLE);
        } else {
            mTxtRight.setText(title);
            Drawable drawable = ContextCompat.getDrawable(getContext(), resid);
            drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
            mTxtRight.setCompoundDrawables(null, drawable, null, null);
            mTxtRight.setVisibility(View.VISIBLE);
            mTxtRight.setOnClickListener(listener);
        }
    }

    public void registerRightImageTitle(String title, int resid, OnClickListener listener) {
        if (listener == null) {
            mTxtRight.setVisibility(View.INVISIBLE);
        } else {
            mTxtRight.setText(title);
            Drawable drawable = ContextCompat.getDrawable(getContext(), resid);
            drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
            mTxtRight.setCompoundDrawables(null, drawable, null, null);
            mTxtRight.setVisibility(View.VISIBLE);
            mTxtRight.setOnClickListener(listener);
        }
    }

    public void registerBack(int resid, OnClickListener listener) {
        if (listener == null) {
            mIvBack.setVisibility(View.INVISIBLE);
        } else {
            mIvBack.setImageResource(resid);
            mIvBack.setVisibility(View.VISIBLE);
            mIvBack.setOnClickListener(listener);
        }
    }


    public View getmTxtBack() {
        return mIvBack;
    }

    public ImageView getRightImage() {
        return mIvRight;
    }

    public TextView getRightTextView() {
        return mTxtRight;
    }


}
