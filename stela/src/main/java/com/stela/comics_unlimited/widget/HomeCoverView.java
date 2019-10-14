package com.stela.comics_unlimited.widget;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.Drawable;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.recyclerview.widget.RecyclerView;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.widget.ImageView;

import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.lxy.baselibs.glide.GlideApp;
import com.lxy.baselibs.glide.GlideUtils;
import com.lxy.baselibs.glide.MyGlideUrl;
import com.lxy.baselibs.utils.CommonUtils;
import com.stela.comics_unlimited.R;
import com.stela.comics_unlimited.data.entity.SeriesAsset;

import java.util.ArrayList;

/**
 * Created by nb on 4/27/16.
 */
public class HomeCoverView extends ViewGroup implements SensorEventListener {
    /**
     * 宽高比
     */
    private float scale = 1.0F;
    private AppCompatImageView[] imageViews;
    private float[] scrollFactors;
    private float motionX = 0, motionY = 0, scrollY = 0;

    private SensorManager sensorManager;
    private Sensor sensor;
    private AppCompatImageView imageViewLock;

    public HomeCoverView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public HomeCoverView(Context context) {
        super(context);
        setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {


            }
        });
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        if (imageViews != null) {
            for (int i = 0; i < imageViews.length; i++) {
                imageViews[i].layout(0, 0, r - l, b - t);
            }
        }
        int x = CommonUtils.dp2px(50);
        int s = CommonUtils.dp2px(20);

        if (imageViewLock != null) {
//            imageViewLock.layout(r - CommonUtils.dp2px(50), b - CommonUtils.dp2px(50),
//                    r - CommonUtils.dp2px(20), b - CommonUtils.dp2px(20));
            imageViewLock.layout(r-l-x, r-l-x,
                    r-l-s, b - t-s);
        }
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        if (event.sensor.getType() == Sensor.TYPE_ROTATION_VECTOR) {
            motionX = event.values[0] * 0.5f;
            motionY = event.values[1] * 0.75f;
            updateParallax();
        }
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

    public void setRowAssets(ArrayList<SeriesAsset> seriesAssets) {
        // Display loading animation
        AnimationDrawable animationDrawable = (AnimationDrawable) getResources().getDrawable(R.drawable.stela_loading_192);
        setBackground(animationDrawable);
        animationDrawable.setOneShot(false);
        animationDrawable.start();
        if (seriesAssets != null && seriesAssets.size() > 0) {

            // Standard items have one asset, parallax items have many with first being used as a fallback for old clients
            final int layers = seriesAssets.size() == 1 ? 1 : seriesAssets.size() - 1;
            final int baseLayer = seriesAssets.size() == 1 ? 0 : 1;

            imageViews = new AppCompatImageView[layers];
            scrollFactors = new float[layers];

            for (int i = baseLayer; i < seriesAssets.size(); i++) {
                SeriesAsset seriesAsset = seriesAssets.get(i);

                final AppCompatImageView imageView = new AppCompatImageView(getContext());
//                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//                    imageView.setTransitionName("image");
//                }
//                imageView.setScaleType(ImageView.ScaleType.FIT_CENTER);
                addView(imageView);

                imageViews[i - baseLayer] = imageView;
                scrollFactors[i - baseLayer] = seriesAsset.scrollFactor;

                final boolean isBaseLayer = i == baseLayer;
                // 图片重新刷新问题
                loadImg(seriesAsset, imageView, isBaseLayer, animationDrawable);

            }

            if (layers > 1) {
                sensorManager = (SensorManager) getContext().getSystemService(Context.SENSOR_SERVICE);
                sensor = sensorManager.getDefaultSensor(Sensor.TYPE_ROTATION_VECTOR);
                sensorManager.registerListener(this, sensor, 10000); // 10ms
            }
            if (seriesAssets.get(0).isLock == 1) {
                imageViewLock = new AppCompatImageView(getContext());
                imageViewLock.setImageResource(R.mipmap.big_home_lock);
//                LayoutParams params = new LayoutParams(CommonUtils.dp2px(30), CommonUtils.dp2px(30));
//                imageViewLock.setLayoutParams(params);
                addView(imageViewLock);
            }
        }
    }

    private void loadImg(SeriesAsset seriesAsset, AppCompatImageView imageView, boolean baseLayer, AnimationDrawable animationDrawable) {
        if (!TextUtils.isEmpty(seriesAsset.url)) {
            GlideApp.with(getContext())
                    .load(new MyGlideUrl(seriesAsset.url))
                    .fitCenter()
                    .listener(new RequestListener<Drawable>() {
                        @Override
                        public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                            if (baseLayer && animationDrawable.isRunning()) {
                                animationDrawable.stop();
                            }
                            return false;
                        }

                        @Override
                        public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                            if (imageView == null) {
                                return false;
                            }
                            if (baseLayer) {
                                setBackgroundColor(Color.TRANSPARENT);
                                AlphaAnimation alphaAnimation = new AlphaAnimation(0, 1);
                                alphaAnimation.setDuration(350);
                                alphaAnimation.setFillAfter(true);
                                startAnimation(alphaAnimation);
                                animationDrawable.stop();
                            }
                            return false;
                        }
                    })
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .dontAnimate()
//                .fitCenter()
                    .error(baseLayer?GlideUtils.getRandomColor(seriesAsset.url):R.color.transparent)
//                .thumbnail(0.1f)
                    .into(imageView);
        }
    }

    public void release() {
        for (int i = 0; i < imageViews.length; i++) {
            imageViews[i].setImageBitmap(null);
            removeView(imageViews[i]);
        }
        if (sensorManager != null) {
            sensorManager.unregisterListener(this);
            sensorManager = null;
            sensor = null;
        }
    }

    private void updateParallax() {
        float xx = Math.max(-1.0f, Math.min(1.0f, motionX));
        float yy = Math.max(-1.0f, Math.min(1.0f, -scrollY + -motionY));
        float w = this.getWidth();
        float h = this.getHeight();
        for (int i = 0; i < imageViews.length; i++) {
            float scale = 1.0f + Math.abs(scrollFactors[i]) * 2.0f;
            float tx = xx * scrollFactors[i] * w;
            float ty = yy * scrollFactors[i] * h;
            imageViews[i].setScaleX(scale);
            imageViews[i].setScaleY(scale);
            imageViews[i].setTranslationX(tx);
            imageViews[i].setTranslationY(ty);
        }
    }

    public void updateScroll(RecyclerView scroller) {
        if (imageViews == null) {
            return;
        }
        if (imageViews.length <= 1) {
            return;
        }
        final int[] scrollerPos = new int[2];
        scroller.getLocationOnScreen(scrollerPos);
        float sh = scroller.getHeight();
        final int[] viewPos = new int[2];
        this.getLocationOnScreen(viewPos);
        float th = this.getHeight();
        scrollY = (float) (viewPos[1] - scrollerPos[1]) / (sh + th);
        updateParallax();
    }

    public AppCompatImageView getImageView() {

        if (imageViews != null && imageViews.length > 0) {
            return imageViews[0];
        }
        return null;
    }

}
