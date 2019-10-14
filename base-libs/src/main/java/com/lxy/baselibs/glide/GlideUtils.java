package com.lxy.baselibs.glide;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.appcompat.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.ImageView;

import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.load.resource.bitmap.CircleCrop;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.common.baselibs.R;
import com.lxy.baselibs.glide.transform.GlideCircleTransformWithBorder;

import java.util.Random;

/**
 * @author xuhao
 * @date 2018/6/11 17:36
 * @desc 图片加载工具类
 */
public class GlideUtils {


    private static int[] imgBgColor = new int[]{R.color.img_bg_1, R.color.img_bg_2, R.color.img_bg_3,
            R.color.img_bg_4, R.color.img_bg_5, R.color.img_bg_6, R.color.img_bg_7, R.color.img_bg_8,
            R.color.img_bg_9, R.color.img_bg_10, R.color.img_bg_11, R.color.img_bg_12, R.color.img_bg_13,
            R.color.img_bg_14, R.color.img_bg_15, R.color.img_bg_16, R.color.img_bg_17, R.color.img_bg_18,
            R.color.img_bg_19, R.color.img_bg_20, R.color.img_bg_21, R.color.img_bg_22, R.color.img_bg_23,
            R.color.img_bg_24, R.color.img_bg_25, R.color.img_bg_26, R.color.img_bg_27, R.color.img_bg_28,
            R.color.img_bg_29, R.color.img_bg_30, R.color.img_bg_31, R.color.img_bg_32, R.color.img_bg_33,
            R.color.img_bg_34, R.color.img_bg_35, R.color.img_bg_36, R.color.img_bg_37, R.color.img_bg_38,
            R.color.img_bg_39, R.color.img_bg_40, R.color.img_bg_41, R.color.img_bg_42, R.color.img_bg_43,
            R.color.img_bg_44, R.color.img_bg_45, R.color.img_bg_46, R.color.img_bg_47, R.color.img_bg_48,
            R.color.img_bg_49, R.color.img_bg_50, R.color.img_bg_51, R.color.img_bg_52, R.color.img_bg_53,
            R.color.img_bg_54, R.color.img_bg_55, R.color.img_bg_56, R.color.img_bg_57, R.color.img_bg_58,
            R.color.img_bg_59, R.color.img_bg_60, R.color.img_bg_61, R.color.img_bg_62, R.color.img_bg_63,
            R.color.img_bg_64, R.color.img_bg_65, R.color.img_bg_66, R.color.img_bg_67, R.color.img_bg_68,
            R.color.img_bg_69, R.color.img_bg_70, R.color.img_bg_71, R.color.img_bg_72, R.color.img_bg_73,
            R.color.img_bg_74, R.color.img_bg_75, R.color.img_bg_76, R.color.img_bg_77, R.color.img_bg_78,
            R.color.img_bg_79, R.color.img_bg_80, R.color.img_bg_81, R.color.img_bg_82, R.color.img_bg_83,
            R.color.img_bg_84, R.color.img_bg_85, R.color.img_bg_86, R.color.img_bg_87, R.color.img_bg_88,
            R.color.img_bg_89, R.color.img_bg_90, R.color.img_bg_91, R.color.img_bg_92, R.color.img_bg_93, R.color.img_bg_94};

    public static int getRandomColor(String url) {
//        int index = new Random().nextInt(94);
        int index = getRandomNum(url);
        return imgBgColor[index];
    }

    public static int getRandomNum(String url) {
        int tokenKeyIndex = url.indexOf("?");
        if (tokenKeyIndex != -1) {
            url = url.substring(0, tokenKeyIndex);
        }
        int size = (int) (url.charAt(url.length() - 6)) + (int) (url.charAt(url.length() - 7));
        return size % 94;
    }
    public static int getRandomColor() {
        int index = new Random().nextInt(94);
        return imgBgColor[index];
    }

    /**
     * 加载图片
     *
     * @param context  context
     * @param iv       imageView
     * @param url      图片地址
     * @param emptyImg 默认展位图
     */
    public static void loadImage(Context context, ImageView iv, String url, int emptyImg) {
        if (!TextUtils.isEmpty(url)) {
            GlideApp.with(context)
                    .load(url)
                    .error(emptyImg)
                    .placeholder(getRandomColor(url))
                    .transition(new DrawableTransitionOptions().crossFade())
                    .into(iv);
        } else {
            loadImage(context, iv, emptyImg, emptyImg);
        }
    }

    /**
     * 加载圆角图片
     *
     * @param context  context
     * @param iv       imageView
     * @param url      图片地址
     * @param emptyImg 默认展位图
     */
    public static void loadRoundImage(Context context, ImageView iv, String url, int emptyImg) {
        if (!TextUtils.isEmpty(url)) {
            GlideApp.with(context)
                    .load(url)
                    .error(emptyImg)
                    .placeholder(iv.getDrawable())
                    .transition(new DrawableTransitionOptions().crossFade())
                    .transform(new RoundedCorners(20)).into(iv);
        } else {
            loadRoundImage(context, iv, emptyImg, emptyImg);
        }
    }

    /**
     * 加载圆形图片
     *
     * @param context  context
     * @param iv       imageView
     * @param url      图片地址
     * @param emptyImg 默认展位图
     */
    public static void loadCircleImage(Context context, ImageView iv, String url, int emptyImg) {
        if (!TextUtils.isEmpty(url)) {
            GlideApp.with(context)
                    .load(url)
                    .error(emptyImg)
                    .placeholder(iv.getDrawable())
                    .transition(new DrawableTransitionOptions().crossFade())
                    .transform(new CircleCrop()).into(iv);
        } else {
            loadCircleImage(context, iv, emptyImg, emptyImg);
        }
    }

    /**
     * 加载drawable中的图片资源
     *
     * @param context  context
     * @param iv       imageView
     * @param resId    图片地址
     * @param emptyImg 默认展位图
     */
    public static void loadImage(Context context, ImageView iv, int resId, int emptyImg) {
        GlideApp.with(context).load(resId).placeholder(emptyImg).into(iv);
    }

    /**
     * 加载drawable中的图片资源 圆角
     *
     * @param context  context
     * @param iv       imageView
     * @param resId    图片地址
     * @param emptyImg 默认展位图
     */
    public static void loadRoundImage(Context context, ImageView iv, int resId, int emptyImg) {
        GlideApp.with(context).load(emptyImg).placeholder(getRandomColor(resId + "")).transform(new RoundedCorners(20)).into(iv);
    }

    /**
     * 加载drawable中的图片资源 圆形
     *
     * @param context  context
     * @param iv       imageView
     * @param resId    图片地址
     * @param emptyImg 默认展位图
     */
    public static void loadCircleImage(Context context, ImageView iv, int resId, int emptyImg) {
        GlideApp.with(context).load(emptyImg).placeholder(getRandomColor(resId + "")).transform(new CircleCrop()).into(iv);
    }

    /**
     * 加载drawable中的图片资源 圆形
     *
     * @param context  context
     * @param iv       imageView
     * @param resId    图片地址
     * @param emptyImg 默认展位图
     */
    public static void loadCircleImageWithBoder(Context context, ImageView iv, String resId, int emptyImg) {
        if (!TextUtils.isEmpty(resId)) {
            GlideApp.with(context)
                    .load(new MyGlideUrl(resId))
                    .circleCrop()
                    .placeholder(emptyImg)
                    .transform(new GlideCircleTransformWithBorder(context, 2, context.getResources().getColor(R.color.white)))
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .dontAnimate()
                    .into(iv);
        } else {
            GlideApp.with(context)
                    .load("")
                    .circleCrop()
                    .placeholder(emptyImg)
                    .transform(new GlideCircleTransformWithBorder(context, 2, context.getResources().getColor(R.color.white)))
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .dontAnimate()
                    .into(iv);
        }

    }
    /**
     * 加载图片 https
     *
     * @param context  context
     * @param iv       imageView
     * @param url      图片地址
     * @param emptyImg 默认展位图
     */
    public static void loadImageHttpsChapter(Context context, ImageView iv, String url, int emptyImg) {
        if (!TextUtils.isEmpty(url)) {
//            final AnimationDrawable animationDrawable = (AnimationDrawable) ContextCompat.getDrawable(context, R.drawable.stela_loading_96);
//            iv.setBackground(animationDrawable);
//            animationDrawable.setOneShot(false);
//            animationDrawable.start();
            GlideApp.with(context)
                    .load(new MyGlideUrl(url))
                    .error(getRandomColor(url))
//                    .placeholder(iv.getDrawable())
                    .placeholder(R.color.black)
//                    .thumbnail(0.1f)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .dontAnimate()
//                    .transition(new DrawableTransitionOptions().crossFade())
//                    .listener(new RequestListener<Drawable>() {
//                        @Override
//                        public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
////                            animationDrawable.stop();
//                            return false;
//                        }
//
//                        @Override
//                        public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
////                            animationDrawable.stop();
//                            return false;
//                        }
//                    })
                    .into(iv);
        } else {
            loadImage(context, iv, emptyImg, emptyImg);
        }
    }

    /**
     * 加载图片 https
     *
     * @param context  context
     * @param iv       imageView
     * @param url      图片地址
     * @param emptyImg 默认展位图
     */
    public static void loadImageHttps(Context context, ImageView iv, String url, int emptyImg) {
        if (!TextUtils.isEmpty(url)) {
//            final AnimationDrawable animationDrawable = (AnimationDrawable) ContextCompat.getDrawable(context, R.drawable.stela_loading_96);
//            iv.setBackground(animationDrawable);
//            animationDrawable.setOneShot(false);
//            animationDrawable.start();
            GlideApp.with(context)
                    .load(new MyGlideUrl(url))
                    .error(getRandomColor(url))
//                    .placeholder(iv.getDrawable())
                    .placeholder(getRandomColor(url))
//                    .thumbnail(0.1f)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .dontAnimate()
//                    .transition(new DrawableTransitionOptions().crossFade())
//                    .listener(new RequestListener<Drawable>() {
//                        @Override
//                        public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
////                            animationDrawable.stop();
//                            return false;
//                        }
//
//                        @Override
//                        public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
////                            animationDrawable.stop();
//                            return false;
//                        }
//                    })
                    .into(iv);
        } else {
            loadImage(context, iv, emptyImg, emptyImg);
        }
    }

    /**
     * 加载共享图片 https
     *
     * @param context  context
     * @param iv       imageView
     * @param url      图片地址
     * @param emptyImg 默认展位图
     */
    public static void loadImageShareHttps(final AppCompatActivity context, final ImageView iv, String url, int emptyImg) {
        if (!TextUtils.isEmpty(url)) {
            final AnimationDrawable animationDrawable = (AnimationDrawable) ContextCompat.getDrawable(context, R.drawable.stela_loading_96);
            iv.setBackground(animationDrawable);
            animationDrawable.setOneShot(false);
            animationDrawable.start();
            GlideApp.with(context)
                    .load(new MyGlideUrl(url))
                    .error(getRandomColor(url))
                    .placeholder(getRandomColor(url))
                    .thumbnail(0.1f)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .transition(new DrawableTransitionOptions().crossFade())
                    .listener(new RequestListener<Drawable>() {
                        @Override
                        public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                            animationDrawable.stop();
                            scheduleStartPostponedTransivition(context, iv);
                            return false;
                        }

                        @Override
                        public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                            animationDrawable.stop();
                            //图片加载完成的回调中，启动过渡动画
                            context.supportStartPostponedEnterTransition();
                            iv.setImageDrawable(resource);
                            scheduleStartPostponedTransivition(context, iv);
                            return false;
                        }
                    })
                    .into(iv);
        } else {
            loadImage(context, iv, emptyImg, emptyImg);
        }
    }

    private static void scheduleStartPostponedTransivition(final Activity context, final View sharedElement) {
        if (sharedElement == null) return;
        sharedElement.getViewTreeObserver().addOnPreDrawListener(
                new ViewTreeObserver.OnPreDrawListener() {
                    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
                    @Override
                    public boolean onPreDraw() {
                        if (sharedElement == null) return false;
                        sharedElement.getViewTreeObserver().removeOnPreDrawListener(this);
                        context.startPostponedEnterTransition();//开启动画
                        return true;
                    }
                });
    }


}
