package com.stela.comics_unlimited.widget;

import android.content.Context;
import android.content.res.TypedArray;
import androidx.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.stela.comics_unlimited.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * @author Administrator
 */
public class HomeImageView extends RelativeLayout {

    /**
     * 宽高比
     */
    private float scale = 1.0F;
    @BindView(R.id.iv_home_img)
    ImageView ivHomeImg;
    @BindView(R.id.iv_home_lock)
    ImageView ivHomeLock;
    private Unbinder unbinder;

    public HomeImageView(Context context) {
        super(context);
        init(context,null);
    }

    public HomeImageView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context,attrs);
    }

    public HomeImageView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context,attrs);
    }

    private void init(Context context,  AttributeSet attrs) {
        if (attrs!=null) {
            TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.ScaleView);
            scale = typedArray.getFloat(R.styleable.ScaleView_scale, 1.0F);
            typedArray.recycle();
        }
        View itemView = LayoutInflater.from(getContext()).inflate(R.layout.home_image_view, this, true);
        unbinder = ButterKnife.bind(this, itemView);
        getImageView();
    }

    public ImageView getImageView() {
        return ivHomeImg;
    }


    public void setLock(boolean b, boolean isBig) {
        ivHomeLock.setImageResource(isBig ? R.mipmap.big_home_lock : R.mipmap.small_home_lock);
        ivHomeLock.setVisibility(b ? VISIBLE : GONE);
    }
    public void setScale(float scale){
        this.scale = scale;
        requestLayout();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        setMeasuredDimension(getDefaultSize(0, widthMeasureSpec), getDefaultSize(0, heightMeasureSpec));
        if (scale>0) {
            int childWidthSize = getMeasuredWidth();
            //高度和宽度一样
            widthMeasureSpec = MeasureSpec.makeMeasureSpec(childWidthSize, MeasureSpec.EXACTLY);
            heightMeasureSpec = MeasureSpec.makeMeasureSpec((int) (childWidthSize * scale), MeasureSpec.EXACTLY);
        }
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }
}
