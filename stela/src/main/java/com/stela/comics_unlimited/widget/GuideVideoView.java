package com.stela.comics_unlimited.widget;

import android.content.Context;
import android.media.MediaPlayer;
import android.net.Uri;
import android.util.AttributeSet;
import android.widget.VideoView;

import com.lxy.baselibs.utils.LogUtils;


public class GuideVideoView extends VideoView {

    public GuideVideoView(Context context) {
        super(context);
    }

    public GuideVideoView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public GuideVideoView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int width = MeasureSpec.getSize(widthMeasureSpec);
        int height = MeasureSpec.getSize(heightMeasureSpec);
        LogUtils.d("onMeasure():width = " + width);
        LogUtils.d("onMeasure():height = " + height);
//        if ((height / width) >= 2) {
//            height = (width * 16) / 9;
//        } else {
//            width = (height * 9) / 16;
//
//        }
        LogUtils.d("onMeasure():change width = " + width);
        LogUtils.d("onMeasure():change height = " + height);
        setMeasuredDimension(width, height);
    }

    public void playVideo(Uri uri) {
        if (uri == null) {
            throw new IllegalArgumentException("uri can not be null");
        }
        setVideoURI(uri);
        start();
        setOnPreparedListener(new MediaPlayer.OnPreparedListener() {

            @Override
            public void onPrepared(MediaPlayer mp) {
                mp.setLooping(true);//设置循环播放
            }
        });
        setOnErrorListener(new MediaPlayer.OnErrorListener() {

            @Override
            public boolean onError(MediaPlayer mp, int what, int extra) {
                return true;//错误监听
            }
        });
    }

}
