package com.stela.comics_unlimited.widget.ScaleView;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;

import com.stela.comics_unlimited.R;

/**
 * 按照宽度比例显示图片
 * Created by HB on 2017/7/10.
 */

public class ScaleImage extends androidx.appcompat.widget.AppCompatImageView {

    /**
     * 宽高比
     */
    private float scale = 1.0F;

    public ScaleImage(Context context) {
        super(context);
    }

    public ScaleImage(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ScaleImage(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.ScaleView);
        scale = typedArray.getFloat(R.styleable.ScaleView_scale, 1.0F);
        typedArray.recycle();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        setMeasuredDimension(getDefaultSize(0, widthMeasureSpec), getDefaultSize(0, heightMeasureSpec));
        int childWidthSize = getMeasuredWidth();
        //高度和宽度一样
        widthMeasureSpec = MeasureSpec.makeMeasureSpec(childWidthSize, MeasureSpec.EXACTLY);
        heightMeasureSpec = MeasureSpec.makeMeasureSpec((int) (childWidthSize * scale), MeasureSpec.EXACTLY);
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }
}
